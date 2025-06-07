/*
 * (C) Copyright 2022 Thanox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package now.fortuitous.thanos.process.v2

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.SystemClock
import android.text.format.Formatter
import androidx.lifecycle.viewModelScope
import com.elvishew.xlog.XLog
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.BuildProp
import github.tornaco.android.thanos.common.AppLabelSearchFilter
import github.tornaco.android.thanos.common.LifeCycleAwareViewModel
import github.tornaco.android.thanos.core.T
import github.tornaco.android.thanos.core.net.TrafficStatsState
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.core.pm.PREBUILT_PACKAGE_SET_ID_3RD
import github.tornaco.android.thanos.core.pm.Pkg
import github.tornaco.android.thanos.module.compose.common.loader.AppSetFilterItem
import github.tornaco.android.thanos.module.compose.common.loader.Loader
import github.tornaco.android.thanos.support.withThanos
import github.tornaco.thanos.module.component.manager.redesign.rule.BlockerRules.classNameToRule
import github.tornaco.thanos.module.component.manager.redesign.rule.RuleInit
import github.tornaco.thanos.module.component.manager.redesign.rule.fallbackRule
import github.tornaco.thanos.module.component.manager.redesign.rule.getServiceRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.Collator
import java.util.Locale
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class ProcessManageViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    LifeCycleAwareViewModel() {
    private val _state =
        MutableStateFlow(
            ProcessManageState(
                isLoading = true,
                selectedAppSetFilterItem = null,
                runningAppStates = emptyList(),
                runningAppStatesBg = emptyList(),
                appFilterItems = emptyList(),
                appsNotRunning = emptyList(),
                cpuUsageRatioStates = emptyMap(),
                netSpeedStates = emptyMap(),
            )
        )
    val state = _state.asStateFlow()

    private val pm by lazy { context.packageManager }

    private var searchKeyword: String = ""
    private val appLabelSearchFilter = AppLabelSearchFilter()

    private var searchJobs: MutableList<Job> = mutableListOf()

    private val trafficStats by lazy { TrafficStatsState(context) }

    fun init() {
        RuleInit.init(context)

        viewModelScope.launch {
            loadDefaultAppFilterItems()
            loadProcessStates()
        }

        viewModelScope.launch {
            startQueryCpuUsage()
        }

        viewModelScope.launch {
            startQueryNetUsage()
        }
    }

    fun refresh(delay: Long = 0L): Job {
        return viewModelScope.launch {
            delay(delay)
            loadProcessStates()
        }
    }

    private suspend fun loadProcessStates() {
        context.withThanos {
            val pkgManager = pkgManager
            val audio = audioManager
            val activityManager = activityManager

            withContext(Dispatchers.IO) {
                updateLoadingState(true)

                val filterPackages: List<Pkg> = state.value.selectedAppSetFilterItem?.let {
                    pkgManager.getPackageSetById(
                        it.id,
                        true,
                        true
                    ).pkgList.filterNot { pkg -> pkg.pkgName == BuildProp.THANOS_APP_PKG_NAME }
                } ?: emptyList()

                val runningServices = activityManager.getRunningServiceLegacy(Int.MAX_VALUE)
                val runningAppProcess =
                    activityManager.runningAppProcessLegacy.filter { it.pkgList != null && it.pkgList.isNotEmpty() }
                val runningPackages =
                    runningAppProcess.map { Pkg.from(it.pkgList[0], it.uid) }.distinct()

                val runningAppStates =
                    runningAppProcess.groupBy { Pkg.from(it.pkgList[0], it.uid) }.map { entry ->
                        val pkg: Pkg = entry.key
                        val runningProcessStates = entry.value.map { process ->
                            val processPss =
                                activityManager.getProcessPss(intArrayOf(process.pid)).sum()
                            RunningProcessState(
                                pkg = pkg,
                                process = process,
                                runningServices = runningServices.filter { service ->
                                    service.pid == process.pid
                                }.map { runningServiceInfo ->
                                    val label = getServiceLabel(runningServiceInfo)
                                    val clientLabel =
                                        if (runningServiceInfo.clientPackage != null && runningServiceInfo.clientLabel > 0) {
                                            kotlin.runCatching {
                                                val clientR: Resources =
                                                    pm.getResourcesForApplication(runningServiceInfo.clientPackage)
                                                clientR.getString(runningServiceInfo.clientLabel)
                                            }.getOrElse {
                                                XLog.e("getResourcesForApplication error", it)
                                                null
                                            }
                                        } else {
                                            null
                                        }
                                    val lcRule =
                                        runningServiceInfo?.service?.let { name ->
                                            getServiceRule(
                                                name
                                            ).takeIf { it != fallbackRule }
                                        }
                                    val blockerRule =
                                        runningServiceInfo?.service?.className?.classNameToRule()
                                    RunningService(
                                        running = runningServiceInfo,
                                        serviceLabel = label,
                                        clientLabel = clientLabel,
                                        lcRule = lcRule,
                                        blockRule = blockerRule
                                    )
                                },
                                sizeStr = Formatter.formatShortFileSize(context, processPss * 1024),
                            )
                        }.sortedByDescending { it.runningServices.size }.sortedBy { !it.isMain }

                        val isAllProcessCached =
                            entry.value.all { it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_CACHED }
                        val totalPss =
                            activityManager.getProcessPss(entry.value.map { it.pid }.toIntArray())
                                .sum()

                        val runningTimeMillis = runningProcessStates.map {
                            activityManager.getProcessStartTime(it.process.pid)
                        }.filter { it > 0L }.minOrNull()?.let {
                            SystemClock.elapsedRealtime() - it
                        }

                        val appInfo = pkgManager.getAppInfo(pkg)
                        appInfo?.let { app ->
                            RunningAppState(
                                appInfo = app,
                                processState = runningProcessStates,
                                allProcessIsCached = isAllProcessCached,
                                totalPss = totalPss,
                                sizeStr = Formatter.formatShortFileSize(context, totalPss * 1024),
                                runningTimeMillis = runningTimeMillis,
                                isPlayingBack = audio.hasAudioFocus(pkg)
                            )
                        }

                    }.filterNotNull().filter {
                        filterPackages.contains(Pkg.fromAppInfo(it.appInfo))
                    }.sortedByDescending { it.totalPss }

                val runningAppStatesGroupByCached =
                    runningAppStates.groupBy { it.allProcessIsCached }
                XLog.d("startLoading: %s", runningAppStatesGroupByCached)

                val notRunningApps =
                    filterPackages.filterNot { runningPackages.contains(it) }.mapNotNull {
                        pkgManager.getAppInfo(it)
                    }.sortedWith { o1, o2 ->
                        Collator.getInstance(Locale.CHINESE).compare(o1.appLabel, o2.appLabel)
                    }

                _state.value = _state.value.copy(
                    isLoading = false,
                    runningAppStates = runningAppStatesGroupByCached[false]?.filter {
                        searchFilter(it.appInfo)
                    } ?: emptyList(),
                    runningAppStatesBg = runningAppStatesGroupByCached[true]?.filter {
                        searchFilter(it.appInfo)
                    } ?: emptyList(),
                    appsNotRunning = notRunningApps.filter {
                        searchFilter(it)
                    }
                )
            }
        }
    }

    private fun searchFilter(app: AppInfo) =
        searchKeyword.isEmpty()
                // Package name.
                || searchKeyword.length > 2 && app.pkgName.contains(
            searchKeyword,
            ignoreCase = true
            // Label.
        ) || appLabelSearchFilter.matches(
            searchKeyword,
            app.appLabel
        )

    private fun getServiceLabel(runningService: ActivityManager.RunningServiceInfo): String {
        return kotlin.runCatching {
            val serviceInfo = context.packageManager.getServiceInfo(runningService.service, 0)
            return if (serviceInfo.labelRes != 0 || serviceInfo.nonLocalizedLabel != null) {
                serviceInfo.loadLabel(context.packageManager).toString()
            } else {
                runningService.service.className.substringAfterLast(".")
            }
        }.getOrElse {
            runningService.service.className.substringAfterLast(".")
        }
    }


    private suspend fun loadDefaultAppFilterItems() {
        val appFilterListItems = Loader.loadAllFromAppSet(context)
        _state.value = _state.value.copy(
            // Default select 3-rd
            selectedAppSetFilterItem = appFilterListItems.find {
                it.id == PREBUILT_PACKAGE_SET_ID_3RD
            },
            appFilterItems = appFilterListItems
        )
    }

    private fun updateLoadingState(isLoading: Boolean) {
        _state.value = _state.value.copy(isLoading = isLoading)
    }

    fun onFilterItemSelected(appSetFilterItem: AppSetFilterItem) {
        _state.value = _state.value.copy(selectedAppSetFilterItem = appSetFilterItem)
        refresh()
    }

    fun clearBgTasks() {
        viewModelScope.launch {
            context.sendBroadcast(Intent(T.Actions.ACTION_RUNNING_PROCESS_CLEAR))
            updateLoadingState(true)
            delay(1000)
            refresh()
        }
    }

    private suspend fun startQueryCpuUsage() = withContext(Dispatchers.IO) {
        XLog.w("startQueryCpuUsage...")
        while (true) {
            if (isResumed) {
                context.withThanos {
                    val cpuUsageMap = mutableMapOf<AppInfo, String>()
                    activityManager.updateProcessCpuUsageStats()
                    (_state.value.runningAppStates + _state.value.runningAppStatesBg).map {
                        val appInfo = it.appInfo
                        val pidsForThisApp =
                            it.processState.map { process -> process.process.pid.toLong() }
                                .toLongArray()
                        val cpuRatio = activityManager.queryCpuUsageRatio(pidsForThisApp, false)
                        cpuUsageMap[appInfo] = "${(cpuRatio * 100).toInt()}"
                    }
                    _state.value = _state.value.copy(cpuUsageRatioStates = cpuUsageMap)
                }
            }
            delay(1000)
        }
    }

    private suspend fun startQueryNetUsage() = withContext(Dispatchers.IO) {
        XLog.w("startQueryNetUsage...")
        while (true) {
            if (isResumed) {
                context.withThanos {
                    val netUsageMap = mutableMapOf<AppInfo, NetSpeedState>()
                    (_state.value.runningAppStates + _state.value.runningAppStatesBg).map {
                        val appInfo = it.appInfo
                        trafficStats.update(appInfo.uid, this)
                        val uidStats = trafficStats.getUidStats(appInfo.uid)
                        XLog.d("uidStats: ${appInfo.appLabel} $uidStats")
                        uidStats?.let {
                            if (uidStats.lastTxBytes > 0 && uidStats.lastRxBytes > 0) {
                                val up = Formatter.formatFileSize(context, uidStats.lastTxBytes)
                                val down = Formatter.formatFileSize(context, uidStats.lastRxBytes)
                                val netSpeedState = NetSpeedState(up = up, down = down)
                                netUsageMap[appInfo] = netSpeedState
                            }
                        }
                    }
                    _state.value = _state.value.copy(netSpeedStates = netUsageMap)
                }
            }
            delay(1000)
        }
    }

    fun keywordChanged(keyword: String) {
        searchKeyword = keyword

        searchJobs.removeAll {
            kotlin.runCatching {
                it.cancel()
                true
            }.getOrDefault(false)
        }
        // Wait the user to be quiet
        searchJobs.add(refresh(delay = 200))
    }

    fun expandRunning(expand: Boolean) {
        _state.value = _state.value.copy(isRunningExpand = expand)
    }

    fun expandCached(expand: Boolean) {
        _state.value = _state.value.copy(isCacheExpand = expand)
    }

    fun expandNotRunning(expand: Boolean) {
        _state.value = _state.value.copy(isNotRunningExpand = expand)
    }
}