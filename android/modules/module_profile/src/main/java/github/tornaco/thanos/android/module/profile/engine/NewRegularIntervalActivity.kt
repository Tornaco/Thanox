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
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import github.tornaco.android.thanos.module.compose.common.widget.LargeSpacer
import github.tornaco.android.thanos.module.compose.common.widget.StandardSpacer
import github.tornaco.android.thanos.module.compose.common.widget.TinySpacer
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.ExtendableFloatingActionButton
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxSmallAppBarScaffold
import github.tornaco.thanos.android.module.profile.R
import kotlinx.parcelize.Parcelize
import kotlin.math.min

@Parcelize
object NewRegularInterval : NavigationKey.WithResult<NewRegularIntervalResult>

@Parcelize
data class NewRegularIntervalResult(
    val durationMillis: Long,
    val tag: String
) : Parcelable

@AndroidEntryPoint
@NavigationDestination(NewRegularInterval::class)
class NewRegularIntervalActivity : ComposeThemeActivity() {
    override fun isF(): Boolean {
        return true
    }

    override fun isADVF(): Boolean {
        return true
    }

    @Composable
    override fun Content() {
        NewRegularIntervalContent()
    }
}

@Composable
private fun NewRegularIntervalContent() {
    val state = DurationState()
    val navHandle = navigationHandle<NewRegularInterval>()

    ThanoxSmallAppBarScaffold(title = {
        Text(
            text = stringResource(id = R.string.module_profile_date_time_regular_interval),
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
                val timeMillis = (state.h.value * 60 * 60 * 1000L
                        + state.m.value * 60 * 1000L
                        + state.s.value * 1000L)
                navHandle.closeWithResult(
                    NewRegularIntervalResult(
                        durationMillis = timeMillis,
                        tag = state.tag
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
        15,
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

private class DurationState {
    var h = mutableStateOf(0)
    var m = mutableStateOf(15)
    var s = mutableStateOf(0)

    var tag by mutableStateOf("")
}
