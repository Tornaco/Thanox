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

package now.fortuitous.thanos.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.format.Formatter
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.elvishew.xlog.XLog
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.BuildProp
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.core.pm.Pkg
import github.tornaco.android.thanos.core.util.OsUtils
import github.tornaco.android.thanos.module.compose.common.infra.LifeCycleAwareViewModel
import github.tornaco.android.thanos.support.AppFeatureManager.withSubscriptionStatus
import github.tornaco.android.thanos.support.main.CpuUsage
import github.tornaco.android.thanos.support.main.MemType
import github.tornaco.android.thanos.support.main.MemUsage
import github.tornaco.android.thanos.support.main.StatusHeaderInfo
import github.tornaco.android.thanos.support.main.defaultStatusHeaderInfo
import github.tornaco.android.thanos.support.withThanos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

enum class ActiveStatus {
    Active, InActive, RebootNeeded, Unknown
}

data class FeatureItem(
    val id: Int = 0,
    @StringRes val titleRes: Int,
    @DrawableRes val packedIconRes: Int,
    @DrawableRes val iconRes: Int,
    val requiredFeature: String? = null,
    @ColorRes val themeColor: Int,

    val menuItems: List<Pair<Int, (Activity) -> Unit>> = emptyList()
)

data class FeatureItemGroup(
    @StringRes val titleRes: Int,
    val items: List<FeatureItem>,
)

data class NavState(
    val isLoading: Boolean,
    val features: List<FeatureItemGroup>,
    val statusHeaderInfo: StatusHeaderInfo,
    val activeStatus: ActiveStatus,
    val hasFrameworkError: Boolean,
    val showPrivacyStatement: Boolean,
    val showFirstRunTips: Boolean,
    val patchSources: List<String>,
    val isAndroidVersionTooLow: Boolean,
)

val privacyAgreementKey get() = "PREF_PRIVACY_STATEMENT_ACCEPTED2_V_" + BuildProp.THANOS_VERSION_CODE
val androidVersionTooLowKey get() = "PREF_ANDROID_VERSION_TOO_LOW_V_" + BuildProp.THANOS_VERSION_CODE

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class NavViewModel2 @Inject constructor(@ApplicationContext private val context: Context) :
    LifeCycleAwareViewModel() {

    private val _state = MutableStateFlow(
        NavState(
            isLoading = false,
            features = PrebuiltFeatures.all(),
            statusHeaderInfo = defaultStatusHeaderInfo,
            activeStatus = ActiveStatus.Unknown,
            hasFrameworkError = false,
            showPrivacyStatement = false,
            showFirstRunTips = false,
            isAndroidVersionTooLow = false,
            patchSources = emptyList()
        )
    )
    val state = _state.asStateFlow()

    fun loadFeatures() {
        context.withThanos {
            val feats = PrebuiltFeatures.all {
                now.fortuitous.thanos.pref.AppPreference.isAppFeatureEnabled(
                    context,
                    it.id
                ) && (it.requiredFeature == null || hasFeature(it.requiredFeature))
            }.filter { it.items.isNotEmpty() }
            _state.value = _state.value.copy(
                features = feats
            )
        }
    }

    fun loadCoreStatus() {
        val handled = context.withThanos {
            val status = if (BuildProp.THANOS_BUILD_FINGERPRINT != fingerPrint()) {
                ActiveStatus.RebootNeeded
            } else {
                ActiveStatus.Active
            }
            val hasFrameworkError = hasFrameworkInitializeError()
            _state.value =
                _state.value.copy(activeStatus = status, hasFrameworkError = hasFrameworkError)
            true
        } ?: false

        if (!handled) {
            _state.value = _state.value.copy(activeStatus = ActiveStatus.InActive)
        }
    }

    fun loadHeaderStatus(showLoading: Boolean = true) {
        withSubscriptionStatus(context) { isSubscribed: Boolean? ->
            isSubscribed?.let {
                if (it) {
                    context.withThanos {
                        viewModelScope.launch {
                            if (showLoading) {
                                _state.value = _state.value.copy(isLoading = true)
                            }
                            val state: StatusHeaderInfo = getStatusHeaderInfo()
                            _state.value =
                                _state.value.copy(statusHeaderInfo = state, isLoading = false)
                        }
                    }
                }
            }
        }
    }

    fun loadAppStatus() {
        viewModelScope.launch {
            context.withThanos {
                val isPrivacyStatementAccepted = isPrivacyStatementAccepted()
                val isAndroidTooLowAccepted = isAndroidVersionTooLowAccepted()
                _state.value = _state.value.copy(
                    showPrivacyStatement = !isPrivacyStatementAccepted,
                    showFirstRunTips = isPrivacyStatementAccepted && now.fortuitous.thanos.pref.AppPreference.isFirstRun(
                        context
                    ),
                    patchSources = patchingSource,
                    isAndroidVersionTooLow = !isAndroidTooLowAccepted && !OsUtils.isOOrAbove()
                )
            }
        }
    }

    private suspend fun getStatusHeaderInfo(): StatusHeaderInfo {
        return withContext(Dispatchers.IO) {
            kotlin.runCatching {
                context.withThanos {
                    var memTotalSizeString = ""
                    var memUsageSizeString = ""
                    var memAvailableSizeString = ""
                    var memUsedPercent = 0
                    var swapTotalSizeString = ""
                    var swapUsageSizeString = ""
                    var swapAvailableSizeString = ""
                    var swapUsedPercent = 0
                    var swapEnabled = false

                    val am = activityManager
                    val pm = pkgManager

                    val runningAppsCount: Int = am.runningAppsCount
                    val memoryInfo = am.memoryInfo
                    if (memoryInfo != null) {
                        memTotalSizeString = Formatter.formatFileSize(context, memoryInfo.totalMem)
                        memUsageSizeString =
                            Formatter.formatFileSize(
                                context,
                                memoryInfo.totalMem - memoryInfo.availMem
                            )
                        memAvailableSizeString =
                            Formatter.formatFileSize(context, memoryInfo.availMem)
                        memUsedPercent =
                            (100 * ((memoryInfo.totalMem - memoryInfo.availMem).toFloat() / memoryInfo.totalMem.toFloat()
                                .coerceAtLeast(1f))).toInt()
                    }
                    val swapInfo = am.swapInfo
                    if (swapInfo != null) {
                        swapEnabled = swapInfo.totalSwap > 0
                        if (swapEnabled) {
                            swapTotalSizeString =
                                Formatter.formatFileSize(context, swapInfo.totalSwap)
                            swapUsageSizeString = Formatter.formatFileSize(
                                context,
                                swapInfo.totalSwap - swapInfo.freeSwap
                            )
                            swapAvailableSizeString =
                                Formatter.formatFileSize(context, swapInfo.freeSwap)
                            swapUsedPercent =
                                (100 * ((swapInfo.totalSwap - swapInfo.freeSwap).toFloat() / swapInfo.totalSwap.toFloat()
                                    .coerceAtLeast(1f))).toInt()
                        }
                    }

                    val cpuPercent =
                        (am.getTotalCpuPercent(true)).coerceAtLeast(1f).toInt()

                    val runningAppProcess =
                        activityManager.runningAppProcessLegacy.filter { it.pkgList != null && it.pkgList.isNotEmpty() }
                            .sortedBy { it.importance }
                    val runningPackages =
                        runningAppProcess.map { Pkg.from(it.pkgList[0], it.uid) }.distinct()
                    val runningUserApps = runningPackages
                        .mapNotNull {
                            pm.getAppInfo(it)
                        }.filter {
                            it.flags == AppInfo.FLAGS_USER
                        }.filterNot {
                            it.pkgName == BuildProp.THANOS_APP_PKG_NAME
                        }
                    StatusHeaderInfo(
                        runningAppsCount = runningAppsCount,
                        runningApps = runningUserApps,
                        memory = MemUsage(
                            memType = MemType.MEMORY,
                            memTotalSizeString = memTotalSizeString,
                            memUsagePercent = memUsedPercent,
                            memUsageSizeString = memUsageSizeString,
                            memAvailableSizeString = memAvailableSizeString,
                            isEnabled = true
                        ),
                        swap = MemUsage(
                            memType = MemType.SWAP,
                            memTotalSizeString = swapTotalSizeString,
                            memUsagePercent = swapUsedPercent,
                            memUsageSizeString = swapUsageSizeString,
                            memAvailableSizeString = swapAvailableSizeString,
                            isEnabled = swapEnabled
                        ),
                        cpu = CpuUsage(totalPercent = cpuPercent)
                    )
                }
            }.getOrElse {
                XLog.e("getStatusHeaderInfo error", it)
                null
            } ?: defaultStatusHeaderInfo
        }
    }

    suspend fun autoRefresh() {
        while (true) {
            delay(2000)
            if (isResumed) {
                loadHeaderStatus(showLoading = false)
            }
        }
    }

    fun refresh() {
        loadHeaderStatus()
    }

    fun headerClick(activity: Activity) {
        PrebuiltFeatureLauncher(
            context = activity,
            onProcessCleared = {}).launch(PrebuiltFeatureIds.ID_PROCESS_MANAGER)
    }

    @SuppressLint("UseKtx")
    fun privacyStatementAccepted() {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putBoolean(privacyAgreementKey, true).apply()
        // Dismiss right now.
        _state.value = _state.value.copy(showPrivacyStatement = false)
        loadAppStatus()
    }

    @SuppressLint("UseKtx")
    fun androidVersionTooLowAccepted() {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putBoolean(androidVersionTooLowKey, true).apply()
        // Dismiss right now.
        _state.value = _state.value.copy(isAndroidVersionTooLow = false)
        loadAppStatus()
    }

    fun firstRunTipNoticed() {
        now.fortuitous.thanos.pref.AppPreference.setFirstRun(context, false)
        loadAppStatus()
    }

    private suspend fun isPrivacyStatementAccepted(): Boolean = withContext(Dispatchers.IO) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(privacyAgreementKey, false)
    }

    private suspend fun isAndroidVersionTooLowAccepted(): Boolean = withContext(Dispatchers.IO) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(androidVersionTooLowKey, false)
    }

    fun rebootDevice() {
        context.withThanos {
            powerManager.reboot()
        }
    }
}