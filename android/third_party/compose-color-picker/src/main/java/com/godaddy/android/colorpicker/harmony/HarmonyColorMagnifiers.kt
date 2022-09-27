package com.godaddy.android.colorpicker.harmony

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import com.godaddy.android.colorpicker.HsvColor
import com.godaddy.android.colorpicker.toRadian
import kotlin.math.cos
import kotlin.math.sin

@Composable
internal fun HarmonyColorMagnifiers(
    diameterPx: Int,
    hsvColor: HsvColor,
    animateChanges: Boolean,
    currentlyChangingInput: Boolean,
    harmonyMode: ColorHarmonyMode
) {
    val size = IntSize(diameterPx, diameterPx)
    val position = remember(hsvColor, size) {
        positionForColor(hsvColor, size)
    }

    val positionAnimated = remember {
        Animatable(position, typeConverter = Offset.VectorConverter)
    }
    LaunchedEffect(hsvColor, size, animateChanges) {
        if (!animateChanges) {
            positionAnimated.snapTo(positionForColor(hsvColor, size))
        } else {
            positionAnimated.animateTo(
                positionForColor(hsvColor, size),
                animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)
            )
        }
    }

    val diameterDp = with(LocalDensity.current) {
        diameterPx.toDp()
    }

    val animatedDiameter = animateDpAsState(
        targetValue = if (!currentlyChangingInput) {
            diameterDp * diameterMainColorDragging
        } else {
            diameterDp * diameterMainColor
        }
    )

    hsvColor.getColors(harmonyMode).forEach { color ->
        val positionForColor = remember {
            Animatable(positionForColor(color, size), typeConverter = Offset.VectorConverter)
        }
        LaunchedEffect(color, size, animateChanges) {
            if (!animateChanges) {
                positionForColor.snapTo(positionForColor(color, size))
            } else {
                positionForColor.animateTo(
                    positionForColor(color, size),
                    animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)
                )
            }
        }
        Magnifier(position = positionForColor.value, color = color, diameter = diameterDp * diameterHarmonyColor)
    }
    Magnifier(position = positionAnimated.value, color = hsvColor, diameter = animatedDiameter.value)
}

internal fun positionForColor(color: HsvColor, size: IntSize): Offset {
    val radians = color.hue.toRadian()
    val phi = color.saturation
    val x: Float = ((phi * cos(radians)) + 1) / 2f
    val y: Float = ((phi * sin(radians)) + 1) / 2f
    return Offset(
        x = (x * size.width),
        y = (y * size.height)
    )
}

private const val diameterHarmonyColor = 0.10f
private const val diameterMainColorDragging = 0.18f
private const val diameterMainColor = 0.15f
