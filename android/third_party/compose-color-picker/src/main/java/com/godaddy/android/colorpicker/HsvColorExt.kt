// ktlint-disable filename
package com.godaddy.android.colorpicker

/**
 * Converst HsvColor to Android Color Int
 *
 * returns @ColorInt
 */
fun HsvColor.toColorInt(): Int {
    return android.graphics.Color.HSVToColor((alpha * 255).toInt(), floatArrayOf(hue, saturation, value))
}
