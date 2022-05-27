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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import github.tornaco.android.thanos.module.compose.common.LargeSpacer
import github.tornaco.android.thanos.module.compose.common.StandardSpacer
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

private class DurationState {
    var h = mutableStateOf(0)
    var m = mutableStateOf(0)
    var s = mutableStateOf(0)
}

class DurationPickerDialogState(
    val title: String,
    val onDurationPick: (Duration) -> Unit,
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
        val durationState = DurationState()
        AlertDialog(
            title = {
                Text(text = state.title)
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Hour(durationState)
                    StandardSpacer()
                    Minute(durationState)
                    StandardSpacer()
                    Second(durationState)
                }
            },
            confirmButton = {
                val timeMillis = (durationState.h.value * 60 * 60 * 1000
                        + durationState.m.value * 60 * 1000
                        + durationState.s.value * 1000)
                TextButton(onClick = {
                    state.onDurationPick(timeMillis.toDuration(DurationUnit.MILLISECONDS))
                }) {
                    Text(text = stringResource(id = android.R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { state.dismiss() }) {
                    Text(text = stringResource(id = android.R.string.cancel))
                }
            },
            onDismissRequest = {
                state.dismiss()
            },
            properties = DialogProperties()
        )
    }
}

@Composable
private fun Hour(durationState: DurationState) {
    Field(
        durationState.h,
        "Hour",
        0,
        999
    )
}

@Composable
private fun Minute(durationState: DurationState) {
    Field(
        durationState.m,
        "Minute",
        0,
        59
    )
}

@Composable
private fun Second(durationState: DurationState) {
    Field(
        durationState.s,
        "Second",
        0,
        59
    )
}

@Composable
private fun Field(fieldValue: MutableState<Int>, label: String, min: Int = 0, max: Int) {
    var value by fieldValue
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