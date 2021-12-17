package github.tornaco.android.thanos.widget.pie

import androidx.compose.ui.graphics.Color

data class ChartItem(val color: Color, val value: Int, val label: String)

internal fun List<ChartItem>.toStateList(): List<ChartItemState> {
    val totalValue = sumOf { it.value }.toFloat()
    return map {
        ChartItemState(it, it.value / totalValue)
    }.sortedBy { it.percent }
}

internal data class ChartItemState(val chartItem: ChartItem, val percent: Float)
