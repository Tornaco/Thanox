package github.tornaco.android.thanox.module.notification.recorder.ui.stats

import github.tornaco.android.thanos.module.compose.common.widget.pie.ChartItem

data class StatsChartState(
    val isLoading: Boolean,
    val totalCount: Long,
    val entries: List<ChartItem<String>>,
)
