package github.tornaco.thanos.android.ops.ops.dashboard.pie

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun PieChart(
    modifier: Modifier = Modifier,
    centerText: CenterText? = null,
    strokeSize: Dp,
    chartItems: List<ChartItem>,
) {
    val stateList = chartItems.toStateList()

    Canvas(modifier = modifier,
        onDraw = {
            val arcWidth = size.width
            val arcHeight = size.height
            val arcSize = Size(arcWidth, arcHeight)

            val spaceAngle = 1f
            val totalAngle = 360f - spaceAngle * stateList.size
            var startAngle = -90f
            stateList.forEach { state ->
                // Item Arc
                val sweepAngle = totalAngle * state.percent
                drawArc(color = state.chartItem.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    size = arcSize,
                    style = Stroke(width = strokeSize.toPx()))
                startAngle += sweepAngle

                // Space Arc
                drawArc(color = Color.Transparent,
                    startAngle = startAngle,
                    sweepAngle = spaceAngle,
                    useCenter = false,
                    size = arcSize,
                    style = Stroke(width = strokeSize.toPx()))
                startAngle += spaceAngle


                // Center Text
                centerText?.let { centerText ->
                    val paint = Paint()
                    paint.isAntiAlias = true
                    paint.textAlign = Paint.Align.CENTER
                    paint.textSize = centerText.size.toPx()
                    paint.color = centerText.color.toArgb()
                    paint.typeface = Typeface.DEFAULT_BOLD
                    drawIntoCanvas {
                        it.nativeCanvas.drawText(centerText.text,
                            center.x,
                            center.y,
                            paint)
                    }
                }
            }
        }
    )
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun ChartPreview() {
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val items = listOf(
            ChartItem(color = Color(0xFFE57600), value = 1, label = "Contacts"),
            ChartItem(color = Color(0xFF4485AA), value = 2, label = "Camera"),
            ChartItem(color = Color(0xFF94E287), value = 3, label = "External Photos"),
            ChartItem(color = Color(0xFF0093E5),
                value = 6,
                label = "Device Id"),
            ChartItem(color = Color(0xFFB446C8),
                value = 4,
                label = "Audio recorder"),
            ChartItem(color = Color(0xFF5A5AE6), value = 4, label = "Vib"),
        )
        PieChart(modifier = Modifier
            .background(color = Color.LightGray)
            .fillMaxWidth(0.6f)
            .aspectRatio(1f),
            chartItems = items,
            strokeSize = 38.dp,
            centerText = CenterText(
                text = "Thanox",
                color = Color.DarkGray,
                size = 24.dp
            ))

        Spacer(modifier = Modifier.size(32.dp))

        Legend(
            modifier = Modifier
                .padding(32.dp),
            chartItems = items,
        )
    }
}