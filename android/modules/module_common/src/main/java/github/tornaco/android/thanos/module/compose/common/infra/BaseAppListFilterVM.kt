package github.tornaco.android.thanos.module.compose.common.infra

import github.tornaco.android.thanos.common.AppListModel

data class AppListUiState(
    val apps: List<AppListModel> = emptyList()
)

class BaseAppListFilterVM : StateViewModel<AppListUiState>(initState = { AppListUiState() }) {
    private lateinit var config: BaseAppListFilterContainerConfig

    fun installIn(activity: BaseAppListFilterActivity) {
        this.config = activity.config
    }
}