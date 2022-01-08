package github.tornaco.android.thanos.module.compose.common.widget.pie

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.elvishew.xlog.XLog
import kotlinx.coroutines.delay
import kotlin.math.acos
import kotlin.math.sqrt

const val TAP_CENTER_DISTANCE = 100f

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun <T> PieChart(
    modifier: Modifier = Modifier,
    centerText: CenterText? = null,
    strokeSize: Dp,
    chartItems: List<ChartItem<T>>,
    onItemSelected: ((ChartItem<T>) -> Unit)? = null,
    onCenterSelected: (() -> Unit)? = null,
) {
    val stateList = chartItems.toStateList()
    var centerOffset: Offset = Offset.Zero
    val itemAngleMap = mutableMapOf<Pair<Float, Float>, ChartItemState<T>>()

    val spaceAngle = 2f
    val totalAngle = 360f - spaceAngle * stateList.size

    var animStarted by remember { mutableStateOf(false) }
    val animPercent by animateFloatAsState(
        targetValue = if (!animStarted) 0f else 1f,
        animationSpec = tween(1000)
    )
    LaunchedEffect(key1 = stateList) {
        delay(180)
        animStarted = true
    }

    Canvas(modifier = modifier
        .pointerInput(chartItems) {
            detectTapGestures(onTap = {
                val centerDistance = (it - centerOffset).getDistance()
                XLog.i("centerDistance: $centerDistance")
                if (centerDistance <= TAP_CENTER_DISTANCE) {
                    onCenterSelected?.invoke()
                } else {
                    val touchAngle = calculateAngleForPoint(centerOffset, it)
                    val tappedItems =
                        detectTappedItems(itemAngleMap, touchAngle)
                    tappedItems.firstOrNull()?.let { tapped ->
                        onItemSelected?.invoke(tapped.chartItem)
                    }
                }
            })
        },
        onDraw = {
            centerOffset = center

            val arcWidth = size.width
            val arcHeight = size.height
            val arcSize = Size(arcWidth, arcHeight)

            var startAngle = 0f

            val isEmpty = stateList.isEmpty()
            if (isEmpty) {
                drawArc(color = Color.LightGray,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    size = arcSize,
                    style = Stroke(width = strokeSize.toPx()))
            } else {
                stateList.forEach { state ->
                    val initialStartAngle = startAngle
                    // Item Arc
                    val sweepAngle = totalAngle * animPercent * state.percent

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


                    // Cache angle to map
                    itemAngleMap[Pair(initialStartAngle, startAngle)] = state
                }
            }


            // Center Text
            centerText?.let { centerText ->
                val paint = Paint()
                paint.isAntiAlias = true
                paint.textAlign = Paint.Align.CENTER
                paint.textSize = centerText.size.toPx() * animPercent
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

    )
}

private fun <T> detectTappedItems(
    itemAngleMap: MutableMap<Pair<Float, Float>, ChartItemState<T>>,
    touchAngle: Float,
) = itemAngleMap.filter { itemAngle ->
    touchAngle >= itemAngle.key.first && touchAngle <= itemAngle.key.second
}.values

private fun calculateAngleForPoint(center: Offset, point: Offset): Float {
    val x = point.x
    val y = point.y

    val tx: Double = (x - center.x).toDouble()
    val ty: Double = (y - center.y).toDouble()
    val length = sqrt(tx * tx + ty * ty)
    val r = acos(ty / length)

    var angle = Math.toDegrees(r).toFloat()

    if (x > center.x) angle = 360f - angle

    // add 90° because chart starts EAST
    angle += 90f

    // neutralize overflow
    if (angle > 360f) angle -= 360f

    return angle
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun ChartPreview() {
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val items = listOf(
            ChartItem("",
                color = Color(0xFFE57600),
                value = 1,
                label = "Contacts"),
            ChartItem("",
                color = Color(0xFF4485AA),
                value = 2,
                label = "Camera"),
            ChartItem("", color = Color(0xFF94E287),
                value = 3,
                label = "External Photos"),
            ChartItem("", color = Color(0xFF0093E5),
                value = 6,
                label = "Device Id"),
            ChartItem("", color = Color(0xFFB446C8),
                value = 4,
                label = "Audio recorder"),
            ChartItem("", color = Color(0xFF5A5AE6), value = 4, label = "Vib"),
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