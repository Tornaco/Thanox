package github.tornaco.android.thanos.widget.pie

import androidx.compose.ui.graphics.Color

data class ChartItem<T>(
    val data: T,
    val color: Color,
    val value: Long,
    val label: String,
)

internal fun <T> List<ChartItem<T>>.toStateList(): List<ChartItemState<T>> {
    val totalValue = sumOf { it.value }.toFloat()
    return map {
        ChartItemState(it, it.value / totalValue)
    }
}

internal data class ChartItemState<T>(
    val chartItem: ChartItem<T>,
    val percent: Float,
)
