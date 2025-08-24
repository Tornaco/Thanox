package github.tornaco.practice.honeycomb.locker.ui.verify.composelock

import android.util.Range
import android.view.MotionEvent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInteropFilter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ComposeLock(
    modifier: Modifier,
    callback: ComposeLockCallback,
    sensitivity: Float = 100f,
    dotsColor: Color = LocalContentColor.current,
    dotsSize: Float = 20f,
    linesColor: Color = LocalContentColor.current.copy(alpha = 0.8f),
    linesStroke: Float = 30f,
    dimension: Int = 4,
    animationDuration: Int = 200,
    animationDelay: Long = 100,
) {
    val scope = rememberCoroutineScope()
    val dotsList = remember {
        mutableListOf<Dot>()
    }
    val previewLine = remember {
        mutableStateOf(Line(Offset(0f, 0f), Offset(0f, 0f)))
    }
    val connectedLines = remember {
        mutableListOf<Line>()
    }
    val connectedDots = remember {
        mutableListOf<Dot>()
    }

    Canvas(
        modifier.pointerInteropFilter {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    for (dots in dotsList) {
                        if (
                            it.x in Range(
                                dots.offset.x - sensitivity,
                                dots.offset.x + sensitivity
                            ) &&
                            it.y in Range(dots.offset.y - sensitivity, dots.offset.y + sensitivity)
                        ) {
                            connectedDots.add(dots)
                            callback.onStart(dots)
                            scope.launch {
                                dots.size.animateTo(
                                    (dotsSize * 1.8).toFloat(),
                                    tween(animationDuration)
                                )
                                delay(animationDelay)
                                dots.size.animateTo(dotsSize, tween(animationDuration))
                            }
                            previewLine.value =
                                previewLine.value.copy(start = Offset(dots.offset.x, dots.offset.y))
                        }
                    }
                }

                MotionEvent.ACTION_MOVE -> {
                    previewLine.value = previewLine.value.copy(end = Offset(it.x, it.y))
                    for (dots in dotsList) {
                        if (!connectedDots.contains(dots)) {
                            if (
                                it.x in Range(
                                    dots.offset.x - sensitivity,
                                    dots.offset.x + sensitivity
                                ) &&
                                it.y in Range(
                                    dots.offset.y - sensitivity,
                                    dots.offset.y + sensitivity
                                )
                            ) {
                                connectedLines.add(
                                    Line(
                                        start = previewLine.value.start,
                                        end = dots.offset
                                    )
                                )
                                connectedDots.add(dots)
                                callback.onDotConnected(dots)
                                scope.launch {
                                    dots.size.animateTo(
                                        (dotsSize * 1.8).toFloat(),
                                        tween(animationDuration)
                                    )
                                    delay(animationDelay)
                                    dots.size.animateTo(dotsSize, tween(animationDuration))
                                }
                                previewLine.value = previewLine.value.copy(start = dots.offset)
                            }
                        }
                    }
                }

                MotionEvent.ACTION_UP -> {
                    previewLine.value =
                        previewLine.value.copy(start = Offset(0f, 0f), end = Offset(0f, 0f))
                    callback.onResult(connectedDots)
                    connectedLines.clear()
                    connectedDots.clear()
                }
            }
            true
        }) {
        val realDimension = dimension + 1
        val spaceBetweenWidthDots = size.width / realDimension
        val spaceBetweenHeightDots = size.height / realDimension
        val dotsOnWidth = arrayOfNulls<Int>(realDimension * realDimension)
        val dotsOnHeight = arrayOfNulls<Int>(realDimension * realDimension)
        dotsOnWidth.forEachIndexed { WidthIndex, _ ->
            val readWidthIndex = WidthIndex + 1
            dotsOnHeight.forEachIndexed { HeightIndex, _ ->
                val readHeightIndex = HeightIndex + 1
                if (readWidthIndex < realDimension && readHeightIndex < realDimension) {
                    if (dotsList.count() < dimension * dimension) {
                        val dotOffset = Offset(
                            (spaceBetweenWidthDots * readWidthIndex),
                            (spaceBetweenHeightDots * readHeightIndex)
                        )
                        dotsList.add(
                            Dot(
                                dotsList.size + 1,
                                dotOffset,
                                Animatable(dotsSize)
                            )
                        )
                    }
                }
            }
        }
        if (previewLine.value.start != Offset(0f, 0f) && previewLine.value.end != Offset(0f, 0f)) {
            drawLine(
                color = linesColor,
                start = previewLine.value.start,
                end = previewLine.value.end,
                strokeWidth = linesStroke,
                cap = StrokeCap.Round
            )
        }
        for (dots in dotsList) {
            drawCircle(
                color = dotsColor,
                radius = dots.size.value,
                center = dots.offset
            )
        }
        for (line in connectedLines) {
            drawLine(
                color = linesColor,
                start = line.start,
                end = line.end,
                strokeWidth = linesStroke,
                cap = StrokeCap.Round
            )
        }

    }
}