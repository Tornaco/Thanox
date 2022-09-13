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

@file:OptIn(ExperimentalMaterial3Api::class)

package github.tornaco.thanos.android.module.profile.engine

import android.os.Parcelable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import dev.enro.annotations.NavigationDestination
import dev.enro.core.NavigationKey
import dev.enro.core.close
import dev.enro.core.compose.navigationHandle
import dev.enro.core.result.closeWithResult
import github.tornaco.android.thanos.core.alarm.Alarm
import github.tornaco.android.thanos.core.alarm.Repeat
import github.tornaco.android.thanos.core.alarm.TimeOfADay
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.*
import github.tornaco.thanos.android.module.profile.R
import kotlinx.parcelize.Parcelize
import kotlin.math.min

@Parcelize
object NewAlarm : NavigationKey.WithResult<NewAlarmResult>

@Parcelize
data class NewAlarmResult(
    val alarm: Alarm,
) : Parcelable

@AndroidEntryPoint
@NavigationDestination(NewAlarm::class)
class NewAlarmActivity : ComposeThemeActivity() {
    override fun isF(): Boolean {
        return true
    }

    override fun isADVF(): Boolean {
        return true
    }

    @Composable
    override fun Content() {
        NewAlarmContent()
    }


    @Composable
    private fun NewAlarmContent() {
        val state = AlarmState()
        val navHandle = navigationHandle<NewAlarm>()

        ThanoxSmallAppBarScaffold(title = {
            Text(
                text = stringResource(id = R.string.module_profile_date_time_alarm),
                style = TypographyDefaults.appBarTitleTextStyle()
            )
        },
            onBackPressed = { navHandle.close() },
            actions = {
            }, floatingActionButton = {
                ExtendableFloatingActionButton(
                    extended = true,
                    text = { Text(text = stringResource(id = R.string.module_profile_rule_edit_action_save)) },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(id = R.string.module_profile_rule_edit_action_save)
                        )
                    }) {
                    val alarm = Alarm(
                        label = state.tag,
                        triggerAt = TimeOfADay(
                            state.h.value,
                            state.m.value,
                            state.s.value
                        ),
                        repeat = Repeat()
                    )
                    navHandle.closeWithResult(
                        NewAlarmResult(
                            alarm = alarm
                        )
                    )
                }
            }) { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Hour(state)
                Minute(state)
                Second(state)
                Text(
                    text = stringResource(
                        id = R.string.module_profile_date_time_min_interval
                    ), style = MaterialTheme.typography.labelSmall
                )

                LargeSpacer()
                OutlinedTextField(
                    label = {
                        Text(text = "Tag")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Tag,
                            contentDescription = "tag"
                        )
                    },
                    value = state.tag,
                    maxLines = 1,
                    onValueChange = {
                        state.tag = it.substring(0, min(16, it.length)).replace("-", "_")
                            .trim()
                    })

                TinySpacer()
                Text(
                    text = stringResource(
                        id = R.string.module_profile_date_time_tag,
                        "\ncondition: \"timeTick && tag == \"${state.tag}\"\""
                    ), style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }


    @Composable
    private fun Hour(durationState: AlarmState) {
        Field(
            durationState.h,
            "Hour",
            0,
            24
        )
    }

    @Composable
    private fun Minute(durationState: AlarmState) {
        Field(
            durationState.m,
            "Minute",
            15,
            59
        )
    }

    @Composable
    private fun Second(durationState: AlarmState) {
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

    private class AlarmState {
        var h = mutableStateOf(0)
        var m = mutableStateOf(0)
        var s = mutableStateOf(0)

        var tag by mutableStateOf("")
    }

}
