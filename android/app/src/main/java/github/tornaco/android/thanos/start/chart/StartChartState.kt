package github.tornaco.android.thanos.start.chart

import github.tornaco.android.thanos.module.compose.common.widget.pie.ChartItem

data class StartChartState(
    val isLoading: Boolean,
    val totalTimes: Long,
    val category: Category,
    val entries: List<ChartItem<String>>,
)
