package github.tornaco.android.thanos.process.v2

import github.tornaco.android.thanos.core.pm.AppInfo

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
