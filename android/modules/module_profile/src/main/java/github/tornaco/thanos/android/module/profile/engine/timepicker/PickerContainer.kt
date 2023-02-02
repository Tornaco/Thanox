package github.tornaco.thanos.android.module.profile.engine.timepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import github.tornaco.android.thanos.module.compose.common.toPx


// https://github.com/KirillVolkov/Compose-TimePicker
@Composable
fun PickerContainer(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Transparent,
    cornerRadius: Dp = 0.dp,
    fadingEdgeLength: Dp = 0.dp,
    content: @Composable () -> Unit
) {

    val corners = cornerRadius.toPx()
    var height by remember { mutableStateOf(0) }

    Box(
        modifier = modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(corners))
            .onGloballyPositioned {
                if (height == 0) {
                    height = it.size.height
                }
            }
            .drawWithContent {
                drawContent()
                if (fadingEdgeLength > 0.dp) {
                    drawRoundRect(
                        cornerRadius = CornerRadius(corners, corners),
                        brush = Brush.verticalGradient(
                            listOf(backgroundColor, Color.Transparent),
                            startY = 0f,
                            endY = fadingEdgeLength.toPx()
                        )
                    )
                    drawRoundRect(
                        cornerRadius = CornerRadius(corners, corners),
                        brush = Brush.verticalGradient(
                            listOf(Color.Transparent, backgroundColor),
                            startY = height - fadingEdgeLength.toPx(),
                            endY = height.toFloat()
                        )
                    )
                }
            },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
