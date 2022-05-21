package github.tornaco.android.thanos.module.compose.common.widget

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * CircularProgressBar-Compose
 * https://github.com/hitanshu-dhawan/CircularProgressBar-Compose
 * */
@Composable
fun CircularProgressBar(
    modifier: Modifier = Modifier,
    progress: Float = 0f,
    progressMax: Float = 100f,
    progressBarColor: Color = Color.Black,
    progressBarWidth: Dp = 7.dp,
    backgroundProgressBarColor: Color = Color.Gray,
    backgroundProgressBarWidth: Dp = 3.dp,
    roundBorder: Boolean = false,
    startAngle: Float = 0f,
    centerContent: @Composable (() -> Unit)? = null
) {
    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = modifier.fillMaxSize()) {
            val canvasSize = size.minDimension

            val radius =
                canvasSize / 2 - maxOf(backgroundProgressBarWidth, progressBarWidth).toPx() / 2

            drawCircle(
                color = backgroundProgressBarColor,
                radius = radius,
                center = size.center,
                style = Stroke(width = backgroundProgressBarWidth.toPx())
            )

            drawArc(
                color = progressBarColor,
                startAngle = 270f + startAngle,
                sweepAngle = (progress / progressMax) * 360f,
                useCenter = false,
                topLeft = size.center - Offset(radius, radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(
                    width = progressBarWidth.toPx(),
                    cap = if (roundBorder) StrokeCap.Round else StrokeCap.Butt
                )
            )
        }

        Box(
            modifier = Modifier.align(Alignment.Center),
        ) {
            centerContent?.invoke()
        }
    }
}