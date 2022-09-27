package com.godaddy.android.colorpicker

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.ceil

/**
 * Alpha side bar Component that invokes onAlphaChanged when the value is mutated.
 *
 * @param modifier modifiers to set on the Alpha Bar
 * @param currentColor the initial color to set on the alpha bar.
 * @param onAlphaChanged the callback that is invoked when alpha value changes. 0 - 1.
 */
@Composable
internal fun AlphaBar(
    modifier: Modifier = Modifier,
    currentColor: HsvColor,
    onAlphaChanged: (Float) -> Unit
) {
    val currentColorToAlphaBrush = remember(currentColor) {
        Brush.horizontalGradient(
            listOf(
                currentColor.copy(alpha = 1.0f).toColor(),
                Color(0x00ffffff)
            )
        )
    }
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                forEachGesture {
                    awaitPointerEventScope {
                        val down = awaitFirstDown()
                        onAlphaChanged(
                            getAlphaFromPosition(
                                down.position.x,
                                this.size.width.toFloat()
                            ).coerceIn(0f, 1f)
                        )
                        drag(down.id) { change ->
                            change.consumePositionChange()
                            onAlphaChanged(
                                getAlphaFromPosition(
                                    change.position.x,
                                    this.size.width.toFloat()
                                ).coerceIn(0f, 1f)
                            )
                        }
                    }
                }
            }
    ) {
        clipRect {
            drawCheckeredBackground()
        }

        drawAlphaBar(currentColorToAlphaBrush)

        val position = getPositionFromAlpha(
            color = currentColor,
            maxWidth = this.size.width
        )
        drawHorizontalSelector(amount = position)
    }
}

private fun DrawScope.drawAlphaBar(alphaBrush: Brush) {
    drawRect(alphaBrush)
    drawRect(Color.Gray, style = Stroke(0.5.dp.toPx()))
}

private fun DrawScope.drawCheckeredBackground() {
    val darkColor = Color.LightGray
    val lightColor = Color.White

    val gridSizePx = 8.dp.toPx()
    val cellCountX = ceil(this.size.width / gridSizePx).toInt()
    val cellCountY = ceil(this.size.height / gridSizePx).toInt()
    for (i in 0 until cellCountX) {
        for (j in 0 until cellCountY) {
            val color = if ((i + j) % 2 == 0) darkColor else lightColor

            val x = i * gridSizePx
            val y = j * gridSizePx
            drawRect(color, Offset(x, y), Size(gridSizePx, gridSizePx))
        }
    }
}

private fun getPositionFromAlpha(color: HsvColor, maxWidth: Float): Float {
    val alpha = 1 - color.alpha
    return maxWidth * alpha
}

/**
 * @return new alpha calculated from the maxWidth
 */
private fun getAlphaFromPosition(x: Float, maxWidth: Float): Float {
    return 1 - x / maxWidth
}
