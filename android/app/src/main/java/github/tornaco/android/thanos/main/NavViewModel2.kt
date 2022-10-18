package github.tornaco.android.thanos.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.format.Formatter
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import com.elvishew.xlog.XLog
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.BuildProp
import github.tornaco.android.thanos.common.LifeCycleAwareViewModel
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.dashboard.*
import github.tornaco.android.thanos.feature.access.AppFeatureManager.showDonateIntroDialog
import github.tornaco.android.thanos.feature.access.AppFeatureManager.withSubscriptionStatus
import github.tornaco.android.thanos.process.v2.ProcessManageActivityV2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

enum class ActiveStatus {
    Active,
    InActive,
    RebootNeeded
}

data class FeatureItem(
    val id: Int = 0,
    @StringRes
    val titleRes: Int,
    @DrawableRes
    val iconRes: Int,
    val requiredFeature: String? = null
)

data class FeatureItemGroup(
    @StringRes
    val titleRes: Int,
    val items: List<FeatureItem>
)

data class NavState(
    val isLoading: Boolean, val features: List<FeatureItemGroup>,
    val statusHeaderInfo: StatusHeaderInfo,
    val activeStatus: ActiveStatus,
    val hasFrameworkError: Boolean,
    val isPurchased: Boolean
)

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class NavViewModel2 @Inject constructor(@ApplicationContext private val context: Context) :
    LifeCycleAwareViewModel() {

    private val _state =
        MutableStateFlow(
            NavState(
                isLoading = false,
                features = emptyList(),
                statusHeaderInfo = defaultStatusHeaderInfo,
                activeStatus = ActiveStatus.InActive,
                hasFrameworkError = false,
                isPurchased = false
            )
        )
    val state = _state.asStateFlow()

    private val thanox by lazy { ThanosManager.from(context) }

    fun loadFeatures() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            withContext(Dispatchers.IO) {
                val all = PrebuiltFeatures.all()
                _state.value = _state.value.copy(features = all)
            }
            _state.value = _state.value.copy(isLoading = false)
        }
    }

    fun loadCoreStatus() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
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
    }

    fun loadPurchaseStatus() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                withSubscriptionStatus(context) { isSubscribed: Boolean? ->
                    isSubscribed?.let {
                        _state.value =
                            _state.value.copy(isPurchased = it)
                    }
                }
            }
        }
    }

    fun loadStatusHeaderState(showLoading: Boolean = true) {
        if (thanox.isServiceInstalled) {
            viewModelScope.launch {
                if (showLoading) _state.value = _state.value.copy(isLoading = true)
                withContext(Dispatchers.IO) {
                    val state = getStatusHeaderInfo()
                    _state.value = _state.value.copy(statusHeaderInfo = state)
                }
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    private fun getStatusHeaderInfo(): StatusHeaderInfo = kotlin.runCatching {
        var memTotalSizeString = ""
        var memUsageSizeString = ""
        var memAvailableSizeString = ""
        var memUsedPercent = 0
        var swapTotalSizeString = ""
        var swapUsageSizeString = ""
        var swapAvailableSizeString = ""
        var swapUsedPercent = 0
        var swapEnabled = false
        var runningAppsCount = 0

        // Only load for pro.
        thanox.ifServiceInstalled { manager ->
            runningAppsCount = manager.activityManager.runningAppsCount
            val memoryInfo =
                manager.activityManager.memoryInfo
            if (memoryInfo != null) {
                memTotalSizeString = Formatter.formatFileSize(
                    context,
                    memoryInfo.totalMem
                )
                memUsageSizeString = Formatter.formatFileSize(
                    context,
                    memoryInfo.totalMem - memoryInfo.availMem
                )
                memAvailableSizeString = Formatter.formatFileSize(
                    context,
                    memoryInfo.availMem
                )
                memUsedPercent =
                    (100 * ((memoryInfo.totalMem - memoryInfo.availMem).toFloat() / memoryInfo.totalMem.toFloat()
                        .coerceAtLeast(1f))).toInt()
            }
            val swapInfo = manager.activityManager.swapInfo
            if (swapInfo != null) {
                swapEnabled = swapInfo.totalSwap > 0
                if (swapEnabled) {
                    swapTotalSizeString = Formatter.formatFileSize(
                        context,
                        swapInfo.totalSwap
                    )
                    swapUsageSizeString = Formatter.formatFileSize(
                        context,
                        swapInfo.totalSwap - swapInfo.freeSwap
                    )
                    swapAvailableSizeString = Formatter.formatFileSize(
                        context,
                        swapInfo.freeSwap
                    )
                    swapUsedPercent =
                        (100 * ((swapInfo.totalSwap - swapInfo.freeSwap).toFloat() / swapInfo.totalSwap.toFloat()
                            .coerceAtLeast(1f))).toInt()
                }
            }
        }

        val cpuPercent =
            (thanox.activityManager.getTotalCpuPercent(true)).coerceAtLeast(1f).toInt()

        return StatusHeaderInfo(
            runningAppsCount,
            MemUsage(
                MemType.MEMORY,
                memTotalSizeString,
                memUsedPercent,
                memUsageSizeString,
                memAvailableSizeString,
                true
            ),
            MemUsage(
                MemType.SWAP,
                swapTotalSizeString,
                swapUsedPercent,
                swapUsageSizeString,
                swapAvailableSizeString,
                swapEnabled
            ),
            CpuUsage(cpuPercent)
        )
    }.getOrElse {
        XLog.e("getStatusHeaderInfo error", it)
        defaultStatusHeaderInfo
    }

    suspend fun autoRefresh() {
        while (true) {
            delay(2000)
            if (isResumed) {
                loadStatusHeaderState(showLoading = false)
            }
        }
    }

    fun refresh() {
        loadStatusHeaderState()
    }

    fun headerClick(activity: Activity) {
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
}