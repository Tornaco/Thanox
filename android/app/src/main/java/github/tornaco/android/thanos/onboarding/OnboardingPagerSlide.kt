package github.tornaco.android.thanos.onboarding

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun OnboardingPagerSlide(selected: Boolean, color: Color) {
    Canvas(modifier = Modifier.size(16.dp)) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        drawCircle(
            color = if (selected) color else Color.Gray,
            center = Offset(x = canvasWidth / 2, y = canvasHeight / 2),
            radius = size.minDimension / 4
        )
    }
}
