package now.fortuitous.thanos.main

import androidx.annotation.ColorInt
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import android.graphics.Color as AndroidColor


@Composable
fun themedTextColor(color: Color): Color {
    val isDarkTheme = isSystemInDarkTheme()
    return if (isDarkTheme) Color.Unspecified else darkenColor(color)
}

@ColorInt
fun darkenColor(color: Color, factor: Float = 0.27f): Color {
    return darkenColor(color.toArgb(), factor).toComposeColor()
}

@ColorInt
fun darkenColor(@ColorInt color: Int, factor: Float): Int {
    return AndroidColor.HSVToColor(FloatArray(3).apply {
        AndroidColor.colorToHSV(color, this)
        this[2] *= factor
    })
}

fun Int.toComposeColor(): Color {
    return Color(this)
}