package github.tornaco.thanos.android.module.profile.engine.timepicker


import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import github.tornaco.android.thanos.module.compose.common.theme.ColorDefaults
import java.time.LocalTime


@Composable
fun BottomTimePicker(
    currentTime: LocalTime? = null,
    is24TimeFormat: Boolean,
    onTimeChanged: (LocalTime) -> Unit
) {

    var time by remember { mutableStateOf(currentTime ?: LocalTime.now()) }

    PickerContainer(
        modifier = Modifier.padding(18.dp),
        backgroundColor = ColorDefaults.backgroundSurfaceColor(),
        cornerRadius = 16.dp,
        fadingEdgeLength = 60.dp
    ) {
        TimePicker(
            modifier = Modifier.height(130.dp),
            itemHeight = 40.dp,
            is24TimeFormat = is24TimeFormat,
            itemStyles = ItemStyles(
                defaultTextStyle = TextStyle(
                    MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                ),
                selectedTextStyle = TextStyle(
                    MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )
            ),
            onTimeChanged = {
                time = it
                onTimeChanged(it)
            },
            currentTime = time
        )
    }
}