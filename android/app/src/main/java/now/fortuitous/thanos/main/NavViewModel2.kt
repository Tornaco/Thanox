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
import github.tornaco.android.thanos.common.LifeCycleAwareViewModel
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.util.OsUtils
import github.tornaco.android.thanos.feature.access.AppFeatureManager.showDonateIntroDialog
import github.tornaco.android.thanos.feature.access.AppFeatureManager.withSubscriptionStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import now.fortuitous.app.donate.DonateSettingsRegistry
import now.fortuitous.thanos.dashboard.CpuUsage
import now.fortuitous.thanos.dashboard.MemType
import now.fortuitous.thanos.dashboard.MemUsage
import now.fortuitous.thanos.dashboard.StatusHeaderInfo
import now.fortuitous.thanos.dashboard.defaultStatusHeaderInfo
import now.fortuitous.thanos.process.v2.ProcessManageActivityV2
import java.util.Observable
import javax.inject.Inject

enum class ActiveStatus {
    Active, InActive, RebootNeeded, Unknown
}

data class FeatureItem(
    val id: Int = 0,
    @StringRes val titleRes: Int,
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
    val isPurchased: Boolean,
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
            isPurchased = true,
            showPrivacyStatement = false,
            showFirstRunTips = false,
            isAndroidVersionTooLow = false,
            patchSources = emptyList()
        )
    )
    val state = _state.asStateFlow()

    private val thanox by lazy { ThanosManager.from(context) }

    private val donateObs = { _: Observable?, _: Any? -> loadPurchaseStatus() }

    init {
        registerReceivers()
    }

    fun loadFeatures() {
        viewModelScope.launch {
            thanox.ifServiceInstalled { thanox ->
                val feats = PrebuiltFeatures.all {
                    now.fortuitous.thanos.pref.AppPreference.isAppFeatureEnabled(
                        context,
                        it.id
                    ) && (it.requiredFeature == null || thanox.hasFeature(it.requiredFeature))
                }.filter { it.items.isNotEmpty() }
                _state.value = _state.value.copy(
                    features = feats
                )
            }
        }
    }

    fun loadCoreStatus() {
        viewModelScope.launch {
            val status = if (!thanox.isServiceInstalled) {
                ActiveStatus.InActive
            } else if (BuildProp.THANOS_BUILD_FINGERPRINT != thanox.fingerPrint()) {
                ActiveStatus.RebootNeeded
            } else {
                ActiveStatus.Active
            }
            val hasFrameworkError =
                thanox.isServiceInstalled && thanox.hasFrameworkInitializeError()
            _state.value =
                _state.value.copy(activeStatus = status, hasFrameworkError = hasFrameworkError)
        }
    }

    fun loadPurchaseStatus() {
        viewModelScope.launch {
            withSubscriptionStatus(context) { isSubscribed: Boolean? ->
                isSubscribed?.let {
                    _state.value = _state.value.copy(isPurchased = it)
                }
            }
        }
    }

    fun loadHeaderStatus(showLoading: Boolean = true) {
        withSubscriptionStatus(context) { isSubscribed: Boolean? ->
            isSubscribed?.let {
                if (it) {
                    if (thanox.isServiceInstalled) {
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
            val isPrivacyStatementAccepted = isPrivacyStatementAccepted()
            val isAndroidTooLowAccepted = isAndroidVersionTooLowAccepted()
            _state.value = _state.value.copy(
                showPrivacyStatement = !isPrivacyStatementAccepted,
                showFirstRunTips = isPrivacyStatementAccepted && !thanox.isServiceInstalled && now.fortuitous.thanos.pref.AppPreference.isFirstRun(
                    context
                ),
                patchSources = if (thanox.isServiceInstalled) thanox.patchingSource else emptyList(),
                isAndroidVersionTooLow = !isAndroidTooLowAccepted && !OsUtils.isOOrAbove()
            )
        }
    }

    private suspend fun getStatusHeaderInfo(): StatusHeaderInfo {
        return withContext(Dispatchers.IO) {
            kotlin.runCatching {
                var memTotalSizeString = ""
                var memUsageSizeString = ""
                var memAvailableSizeString = ""
                var memUsedPercent = 0
                var swapTotalSizeString = ""
                var swapUsageSizeString = ""
                var swapAvailableSizeString = ""
                var swapUsedPercent = 0
                var swapEnabled = false

                val runningAppsCount: Int = thanox.activityManager.runningAppsCount
                val memoryInfo = thanox.activityManager.memoryInfo
                if (memoryInfo != null) {
                    memTotalSizeString = Formatter.formatFileSize(context, memoryInfo.totalMem)
                    memUsageSizeString =
                        Formatter.formatFileSize(context, memoryInfo.totalMem - memoryInfo.availMem)
                    memAvailableSizeString = Formatter.formatFileSize(context, memoryInfo.availMem)
                    memUsedPercent =
                        (100 * ((memoryInfo.totalMem - memoryInfo.availMem).toFloat() / memoryInfo.totalMem.toFloat()
                            .coerceAtLeast(1f))).toInt()
                }
                val swapInfo = thanox.activityManager.swapInfo
                if (swapInfo != null) {
                    swapEnabled = swapInfo.totalSwap > 0
                    if (swapEnabled) {
                        swapTotalSizeString = Formatter.formatFileSize(context, swapInfo.totalSwap)
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
                    (thanox.activityManager.getTotalCpuPercent(true)).coerceAtLeast(1f).toInt()

                StatusHeaderInfo(
                    runningAppsCount = runningAppsCount,
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

            }.getOrElse {
                XLog.e("getStatusHeaderInfo error", it)
                defaultStatusHeaderInfo
            }
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
        if (!thanox.isServiceInstalled) {
            DialogUtils.showNotActivated(activity)
            return
        }
        withSubscriptionStatus(activity) { isSubscribed: Boolean ->
            if (isSubscribed) {
                ProcessManageActivityV2.Starter.start(activity)
            } else {
                showDonateIntroDialog(activity)
            }
        }
    }

    fun featureItemClick(activity: Activity, featureId: Int) {
        PrebuiltFeatureLauncher(activity) {}.launch(featureId)
    }

    fun privacyStatementAccepted() {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putBoolean(privacyAgreementKey, true).commit()
        // Dismiss right now.
        _state.value = _state.value.copy(showPrivacyStatement = false)
        loadAppStatus()
    }

    fun androidVersionTooLowAccepted() {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putBoolean(androidVersionTooLowKey, true).commit()
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

    private fun registerReceivers() {
        DonateSettingsRegistry.addObserver(donateObs)
    }

    override fun onCleared() {
        super.onCleared()
        DonateSettingsRegistry.deleteObserver(donateObs)
    }

    fun rebootDevice() {
        thanox.powerManager.reboot()
    }
}