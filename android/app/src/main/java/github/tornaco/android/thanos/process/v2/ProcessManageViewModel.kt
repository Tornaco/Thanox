package github.tornaco.android.thanos.process.v2

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.SystemClock
import android.text.format.Formatter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elvishew.xlog.XLog
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.BuildProp
import github.tornaco.android.thanos.common.AppLabelSearchFilter
import github.tornaco.android.thanos.core.T
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.net.TrafficStatsState
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.core.pm.PREBUILT_PACKAGE_SET_ID_3RD
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.Collator
import java.util.*
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class ProcessManageViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    ViewModel() {
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

    private val thanox by lazy { ThanosManager.from(context) }
    private val pm by lazy { context.packageManager }

    private var searchKeyword: String = ""
    private val appLabelSearchFilter = AppLabelSearchFilter()

    private var searchJobs: MutableList<Job> = mutableListOf()

    private val trafficStats by lazy { TrafficStatsState(context) }

    fun init() {
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

    private suspend fun loadProcessStates() = withContext(Dispatchers.IO) {
        updateLoadingState(true)

        val filterPackages = state.value.selectedAppSetFilterItem?.let {
            thanox.pkgManager.getPackageSetById(
                it.id,
                true
            ).pkgNames.filterNot { pkgName -> pkgName == BuildProp.THANOS_APP_PKG_NAME }
        } ?: emptyList()

        val runningServices = thanox.activityManager.getRunningServiceLegacy(Int.MAX_VALUE)
        val runningAppProcess =
            thanox.activityManager.runningAppProcessLegacy.filter { it.pkgList != null && it.pkgList.isNotEmpty() }
        val runningPackages = runningAppProcess.map { it.pkgList[0] }.distinct()

        val runningAppStates = runningAppProcess.groupBy { it.pkgList[0] }.map { entry ->
            val pkgName = entry.key
            val runningProcessStates = entry.value.map { process ->
                val processPss =
                    thanox.activityManager.getProcessPss(intArrayOf(process.pid)).sum()
                RunningProcessState(
                    process = process,
                    runningServices = runningServices.filter { service ->
                        service.pid == process.pid
                    }.map {
                        val label = getServiceLabel(it)
                        val clientLabel = if (it.clientPackage != null && it.clientLabel > 0) {
                            val clientR: Resources = pm.getResourcesForApplication(it.clientPackage)
                            clientR.getString(it.clientLabel)
                        } else {
                            null
                        }
                        RunningService(it, label, clientLabel)
                    },
                    sizeStr = Formatter.formatShortFileSize(context, processPss * 1024),
                )
            }.sortedByDescending { it.runningServices.size }
            val isAllProcessCached =
                entry.value.all { it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_CACHED }
            val totalPss =
                thanox.activityManager.getProcessPss(entry.value.map { it.pid }.toIntArray()).sum()
            val runningTimeMillis = runningProcessStates.map {
                thanox.activityManager.getProcessStartTime(it.process.pid)
            }.filter { it > 0L }.minOrNull()?.let {
                SystemClock.elapsedRealtime() - it
            }

            RunningAppState(
                appInfo = thanox.pkgManager.getAppInfo(pkgName),
                processState = runningProcessStates,
                allProcessIsCached = isAllProcessCached,
                totalPss = totalPss,
                sizeStr = Formatter.formatShortFileSize(context, totalPss * 1024),
                runningTimeMillis = runningTimeMillis
            )
        }.filter {
            filterPackages.contains(it.appInfo.pkgName)
        }.sortedByDescending { it.totalPss }

        val runningAppStatesGroupByCached = runningAppStates.groupBy { it.allProcessIsCached }
        XLog.d("startLoading: %s", runningAppStatesGroupByCached)

        val notRunningApps = filterPackages.filterNot { runningPackages.contains(it) }.map {
            thanox.pkgManager.getAppInfo(it)
        }.filterNotNull().sortedWith { o1, o2 ->
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
            val cpuUsageMap = mutableMapOf<AppInfo, String>()
            thanox.activityManager.updateProcessCpuUsageStats()
            (_state.value.runningAppStates + _state.value.runningAppStatesBg).map {
                val appInfo = it.appInfo
                val pidsForThisApp =
                    it.processState.map { process -> process.process.pid.toLong() }.toLongArray()
                val cpuRatio = thanox.activityManager.queryCpuUsageRatio(pidsForThisApp, false)
                cpuUsageMap[appInfo] = "${(cpuRatio * 100).toInt()}"
            }
            _state.value = _state.value.copy(cpuUsageRatioStates = cpuUsageMap)
            delay(1000)
        }
    }

    private suspend fun startQueryNetUsage() = withContext(Dispatchers.IO) {
        XLog.w("startQueryNetUsage...")
        while (true) {
            val netUsageMap = mutableMapOf<AppInfo, NetSpeedState>()
            (_state.value.runningAppStates + _state.value.runningAppStatesBg).map {
                val appInfo = it.appInfo
                trafficStats.update(appInfo.uid)
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
}