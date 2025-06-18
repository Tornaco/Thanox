package github.tornaco.android.thanos.module.compose.common.infra

import github.tornaco.android.thanos.core.pm.AppInfo

data class AppUiModel(
    val appInfo: AppInfo,
    val description: String? = null,
    val badges: List<String> = emptyList(),
    val lastUsedTimeMills: Long = 0L,
    val totalUsedTimeMills: Long = 0L,

    val isChecked: Boolean = false,
    val selectedOptionId: String? = null
)