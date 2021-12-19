package github.tornaco.android.thanos.widget.bar

import androidx.compose.ui.graphics.Color

data class ChartItem<T>(
    val data: T,
    val color: Color,
    val value: Long,
    val label: String,
    val isHighLight: Boolean = false,
)

internal fun <T> List<ChartItem<T>>.toStateList(): List<ChartItemState<T>> {
    sumOf { it.value }.toFloat()
    return map {
        ChartItemState(it)
    }
}

internal data class ChartItemState<T>(
    val chartItem: ChartItem<T>,
)
