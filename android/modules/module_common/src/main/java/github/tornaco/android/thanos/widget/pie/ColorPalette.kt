package github.tornaco.android.thanos.widget.pie

import androidx.compose.ui.graphics.Color

val chartColors = arrayOf(
    Color(0xFF5A5AE6),
    Color(0xFF00c853),
    Color(0xFFB446C8),
    Color(0xFFE57600),
    Color(0xFF4485AA),
    Color(0xFFe53935),
    Color(0xFFc0ca33),
    Color(0xFF0093E5),
    Color(0xFFf06292),
    Color(0xFF00838f),
    Color(0xFF6d4c41),
    Color(0xFF94E287),
    Color(0xFF455a64),
)

fun chartColorOfIndex(index: Int): Color {
    return chartColors[index % chartColors.size]
}