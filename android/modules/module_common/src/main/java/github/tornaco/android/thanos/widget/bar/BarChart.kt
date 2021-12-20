package github.tornaco.android.thanos.widget.bar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.elvishew.xlog.XLog

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BarChartPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        val items = listOf(
            ChartItem("", Color.Red, 12, "A", false),
            ChartItem("", Color.Red, 77, "B", false),
            ChartItem("", Color.Red, 20, "C", false),
            ChartItem("", Color.Red, 30, "D", true),
            ChartItem("", Color.Red, 100, "E", false),
            ChartItem("", Color.Red, 70, "F", false),
            ChartItem("", Color.Red, 43, "G", false),
        )
        BarChart(modifier = Modifier
            .fillMaxWidth(),
            items = items)
    }

}

class BarColors(
    val highLightBarColor: Color = Color(0xFF50DCAA),
    val barColor: Color = Color.LightGray,
    val xAxisColor: Color = Color(0xFFEEEEEE),
    val yAxisColor: Color = Color(0xFFFAFAFA),
)

class BarSize(
    val maxBarWidth: Dp = 72.dp,
    val maxBarHeight: Dp = 120.dp,
    val barPadding: Dp = 24.dp,
    val barCornerRadius: Dp = 2.dp,
    val xAxisWidth: Dp = 2.dp,
)

@Composable
fun <T> BarChart(
    modifier: Modifier = Modifier,
    barColors: BarColors = BarColors(),
    barSize: BarSize = BarSize(),
    items: List<ChartItem<T>>,
) {
    var offsetX by remember { mutableStateOf(0f) }
    var maxAllowedOffSetX = 888f
    var minAllowedOffSetX = -888f
    val scrollState = rememberScrollableState { delta ->
        XLog.i("delta-> $delta offsetX-> $offsetX maxAllowedOffSetX -> $maxAllowedOffSetX")
        if ((offsetX + delta) in minAllowedOffSetX..maxAllowedOffSetX) {
            offsetX += delta
        }
        delta
    }

    Canvas(modifier = modifier
        .height(barSize.maxBarHeight)
        .fillMaxWidth()
        .scrollable(state = scrollState, orientation = Orientation.Horizontal)) {

        translate(left = offsetX) {

            val canvasWidth = size.width
            val canvasHeight = size.height
            val maxBarWidth = barSize.maxBarWidth.toPx()
            val maxBarHeight = barSize.maxBarHeight.toPx()
            val maxBarSize = Size(maxBarWidth, maxBarHeight)
            val barPadding = barSize.barPadding.toPx()
            val cornerRadius = CornerRadius(barSize.barCornerRadius.toPx())

            // X axis
            drawLine(
                color = barColors.xAxisColor,
                start = Offset(0f, canvasHeight),
                end = Offset(canvasWidth, canvasHeight),
                strokeWidth = barSize.xAxisWidth.toPx()
            )

            val highLightItem = items.findLast { it.isHighLight } ?: items.last()
            val maxItemValue = items.maxByOrNull { it.value }?.value ?: 0L

            val highLightItemHeight = maxBarHeight * (highLightItem.value.toFloat() / maxItemValue.toFloat())
            val centerBarTopLeft = Offset((canvasWidth - maxBarSize.width) / 2, canvasHeight - highLightItemHeight)
            drawRoundRect(
                color = barColors.highLightBarColor,
                topLeft = centerBarTopLeft,
                size = Size(maxBarWidth, highLightItemHeight),
                cornerRadius = cornerRadius
            )

            // Lefts
            val leftItems = items.subList(0, items.indexOf(highLightItem))
            leftItems.forEachIndexed { index, chartItem ->
                val itemHeight = maxBarHeight * (chartItem.value.toFloat() / maxItemValue.toFloat())
                val leftBarTopLeft =
                    Offset((canvasWidth - maxBarSize.width) / 2 - maxBarSize.width * (index + 1) - barPadding * (index + 1),
                        canvasHeight - itemHeight)
                drawRoundRect(
                    color = barColors.barColor,
                    topLeft = leftBarTopLeft,
                    size = Size(maxBarWidth, itemHeight),
                    cornerRadius = cornerRadius
                )
            }

            val rightItems = items.subList(items.indexOf(highLightItem) + 1, items.size)
            rightItems.forEachIndexed { index, chartItem ->
                val itemHeight = maxBarHeight * (chartItem.value.toFloat() / maxItemValue.toFloat())
                val rightBarTopLeft =
                    Offset((canvasWidth - maxBarSize.width) / 2 + maxBarSize.width * (index + 1) + barPadding * (index + 1),
                        canvasHeight - itemHeight)
                XLog.i("rightBarTopLeft: $rightBarTopLeft")
                drawRoundRect(
                    color = barColors.barColor,
                    topLeft = rightBarTopLeft,
                    size = Size(maxBarWidth, itemHeight),
                    cornerRadius = cornerRadius
                )
            }
        }
    }
}