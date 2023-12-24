package now.fortuitous.thanos.process.v2

import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.module.compose.common.loader.AppSetFilterItem

data class ProcessManageState(
    val isLoading: Boolean,
    val selectedAppSetFilterItem: AppSetFilterItem? = null,
    val runningAppStates: List<RunningAppState>,
    val runningAppStatesBg: List<RunningAppState>,
    val appsNotRunning: List<AppInfo>,
    val appFilterItems: List<AppSetFilterItem>,
    val cpuUsageRatioStates: Map<AppInfo, String>,
    val netSpeedStates: Map<AppInfo, NetSpeedState>
)
