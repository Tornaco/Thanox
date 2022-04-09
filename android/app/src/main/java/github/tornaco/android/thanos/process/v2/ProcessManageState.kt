package github.tornaco.android.thanos.process.v2

data class ProcessManageState(
    val isLoading: Boolean,
    val selectedAppSetFilterItem: AppSetFilterItem? = null,
    val runningAppStates: List<RunningAppState>,
    val runningAppStatesBg: List<RunningAppState>,
    val appFilterItems: List<AppSetFilterItem>
)
