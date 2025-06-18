/*
 * (C) Copyright 2022 Thanox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package github.tornaco.thanos.android.module.profile.engine.danmu

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.godaddy.android.colorpicker.ClassicColorPicker
import com.godaddy.android.colorpicker.HsvColor
import com.godaddy.android.colorpicker.toColorInt
import github.tornaco.android.thanos.core.profile.DanmuUISettings
import github.tornaco.android.thanos.module.compose.common.theme.LocalThanoxColorSchema
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.StandardSpacer
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxMediumAppBarScaffold

@Composable
fun Activity.DanmuUISettingsScreen() {
    val viewModel = hiltViewModel<DanmuUISettingsViewModel>()
    val state by viewModel.state.collectAsState()

    SideEffect {
        viewModel.loadUISettings()
    }

    ThanoxMediumAppBarScaffold(title = {
        Text(text = "Danmu",
            style = TypographyDefaults.appBarTitleTextStyle())
    }, onBackPressed = { finish() }, actions = {}) { contentPadding ->

        Column(modifier = Modifier
            .padding(contentPadding)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())) {

            ItemCard("Background alpha") {
                Text(text = "${state.settings.alpha}")
                Slider(value = state.settings.alpha, onValueChange = {
                    viewModel.updateUISettings(state.settings.copy(alpha = it))
                })
            }
            StandardSpacer()


            val bgColorPickerState = remember(state.settings) {
                ColorPickerDialogState(colorInt = state.settings.backgroundColor, onColorChanged = {
                    viewModel.updateUISettings(state.settings.copy(backgroundColor = it))
                })
            }
            ColorPickerDialog(state = bgColorPickerState)
            ItemCard(itemLabel = "Background color") {
                FilledTonalButton(colors = ButtonDefaults.filledTonalButtonColors(containerColor = Color(
                    state.settings.backgroundColor)), onClick = { bgColorPickerState.show() }) {}
            }
            StandardSpacer()

            ItemCard("Text size") {
                Text(text = "${state.settings.textSizeSp}")
                Slider(value = state.settings.textSizeSp.toFloat(),
                    steps = 32,
                    valueRange = 1f..32f,
                    onValueChange = {
                        viewModel.updateUISettings(state.settings.copy(textSizeSp = it.toInt()))
                    })
            }
            StandardSpacer()

            ItemCard(itemLabel = "Duration") {
                Text(text = "${state.settings.duration / 1000} seconds")
                Slider(value = (state.settings.duration / 1000).toFloat(),
                    steps = 24,
                    valueRange = 1f..24f,
                    onValueChange = {
                        viewModel.updateUISettings(state.settings.copy(duration = it.toInt() * 1000L))
                    })
            }
            StandardSpacer()


            val textColorPickerState = remember(state.settings) {
                ColorPickerDialogState(colorInt = state.settings.textColor, onColorChanged = {
                    viewModel.updateUISettings(state.settings.copy(textColor = it))
                })
            }
            ColorPickerDialog(state = textColorPickerState)
            ItemCard(itemLabel = "Text color") {
                FilledTonalButton(colors = ButtonDefaults.filledTonalButtonColors(containerColor = Color(
                    state.settings.textColor)), onClick = { textColorPickerState.show() }) {}
            }
            StandardSpacer()
        }
    }
}

@Composable
private fun ItemCard(itemLabel: String, content: @Composable () -> Unit) {
    Card(modifier = Modifier,
        colors = CardDefaults.cardColors(containerColor = LocalThanoxColorSchema.current.cardBgColor)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = itemLabel,
                style = MaterialTheme.typography.titleMedium)
            StandardSpacer()
            content()
        }
    }
}

@Composable
private fun ColorPickerDialog(state: ColorPickerDialogState) {
    if (state.isShowing) {
        var colorValue by mutableStateOf(state.colorInt)
        Dialog(properties = DialogProperties(dismissOnClickOutside = false), onDismissRequest = {
            state.dismiss()
        }) {
            Surface(modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)) {
                Column(
                    modifier = Modifier.fillMaxWidth()) {
                    ClassicColorPicker(modifier = Modifier.height(180.dp),
                        color = Color(state.colorInt),
                        showAlphaBar = true,
                        onColorChanged = { color: HsvColor ->
                            colorValue = color.toColorInt()
                        })
                    StandardSpacer()

                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically) {

                        TextButton(
                            modifier = Modifier,
                            onClick = {
                                state.dismiss()
                                state.onColorChanged.invoke(DanmuUISettings.COLOR_AUTO)
                            }) {
                            Text(text = "Auto",
                                style = MaterialTheme.typography.bodySmall)
                        }

                        TextButton(
                            modifier = Modifier,
                            onClick = {
                                state.dismiss()
                                state.onColorChanged.invoke(colorValue)
                            }) {
                            Text(text = stringResource(id = android.R.string.ok),
                                style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

        }
    }
}

class ColorPickerDialogState(
    val colorInt: Int,
    val onColorChanged: (Int) -> Unit,
) {
    private var _isShowing: Boolean by mutableStateOf(false)
    val isShowing get() = _isShowing

    fun show() {
        _isShowing = true
    }

    fun dismiss() {
        _isShowing = false
    }
}