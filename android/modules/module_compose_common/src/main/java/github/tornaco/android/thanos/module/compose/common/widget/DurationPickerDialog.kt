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

package github.tornaco.android.thanos.module.compose.common.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.DialogProperties
import github.tornaco.android.thanos.module.compose.common.LargeSpacer
import github.tornaco.android.thanos.module.compose.common.StandardSpacer
import kotlin.time.Duration


class DurationPickerDialogState(
    val title: String,
    val onDurationPick: (Duration) -> Unit
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

@Composable
fun rememberDurationPickerDialogState(
    title: String,
    onDurationPick: (Duration) -> Unit
): DurationPickerDialogState {
    return remember {
        DurationPickerDialogState(title, onDurationPick)
    }
}

@Composable
fun DurationPickerDialog(state: DurationPickerDialogState) {
    if (state.isShowing) {
        AlertDialog(
            title = {
                Text(text = state.title)
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Hour()
                    StandardSpacer()
                    Minute()
                    StandardSpacer()
                    Second()
                }
            },
            confirmButton = {},
            onDismissRequest = {
                state.dismiss()
            },
            properties = DialogProperties()
        )
    }
}

@Composable
private fun Hour() {
    Field(
        "Hour",
        0,
        999
    )
}

@Composable
private fun Minute() {
    Field(
        "Minute",
        0,
        59
    )
}

@Composable
private fun Second() {
    Field(
        "Second",
        0,
        59
    )
}

@Composable
private fun Field(label: String, min: Int = 0, max: Int) {
    var value by remember {
        mutableStateOf(min)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = label)
        LargeSpacer()

        // -
        IconButton(onClick = {
            if (value > min) {
                value -= 1
            } else {
                value = max
            }
        }) {
            Icon(
                imageVector = Icons.Filled.Remove,
                contentDescription = "-"
            )
        }

        StandardSpacer()
        Text(text = "$value")
        StandardSpacer()

        // +
        IconButton(onClick = {
            if (value < max) {
                value += 1
            } else {
                value = min
            }
        }) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "+"
            )
        }
    }
}