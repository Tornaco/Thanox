package com.godaddy.android.colorpicker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Classic Color Picker Component that shows a HSV representation of a color, with a Hue Bar on the right,
 * Alpha Bar on the bottom and the rest of the area covered with an area with saturation value touch area.
 *
 * @param modifier modifiers to set to this color picker.
 * @param color the initial color to set on the picker.
 * @param showAlphaBar whether or not to show the bottom alpha bar on the color picker.
 * @param onColorChanged callback that is triggered when the color changes
 *
 */
@Composable
fun ClassicColorPicker(
    modifier: Modifier = Modifier,
    color: Color = Color.Red,
    showAlphaBar: Boolean = true,
    onColorChanged: (HsvColor) -> Unit
) {
    val colorPickerValueState = rememberSaveable(stateSaver = HsvColor.Saver) {
        mutableStateOf(HsvColor.from(color))
    }
    Row(modifier = modifier) {
        val barThickness = 32.dp
        val paddingBetweenBars = 8.dp
        val updatedOnColorChanged by rememberUpdatedState(onColorChanged)
        Column(modifier = Modifier.weight(0.8f)) {
            SaturationValueArea(
                modifier = Modifier.weight(0.8f),
                currentColor = colorPickerValueState.value,
                onSaturationValueChanged = { saturation, value ->
                    colorPickerValueState.value =
                        colorPickerValueState.value.copy(saturation = saturation, value = value)
                    updatedOnColorChanged(colorPickerValueState.value)
                }
            )
            if (showAlphaBar) {
                Spacer(modifier = Modifier.height(paddingBetweenBars))
                AlphaBar(
                    modifier = Modifier.height(barThickness),
                    currentColor = colorPickerValueState.value,
                    onAlphaChanged = { alpha ->
                        colorPickerValueState.value = colorPickerValueState.value.copy(alpha = alpha)
                        updatedOnColorChanged(colorPickerValueState.value)
                    }
                )
            }
        }
        Spacer(modifier = Modifier.width(paddingBetweenBars))
        HueBar(
            modifier = Modifier.width(barThickness),
            currentColor = colorPickerValueState.value,
            onHueChanged = { newHue ->
                colorPickerValueState.value = colorPickerValueState.value.copy(hue = newHue)
                updatedOnColorChanged(colorPickerValueState.value)
            }
        )
    }
}
