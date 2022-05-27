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

import android.app.Activity
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.*
import github.tornaco.android.thanos.util.ToastUtils
import github.tornaco.thanos.android.module.profile.R

private const val ID_TIME_OF_A_DAY = "tod"
private const val ID_REGULAR_INTERVAL = "ri"

@Composable
fun Activity.DateTimeEngineScreen() {
    val context = LocalContext.current

    val durationPickerDialogState = rememberDurationPickerDialogState(title = "Regular interval") {
        Toast.makeText(context, it.toString(), Toast.LENGTH_LONG).show()
    }

    val typeSelectDialogState =
        rememberSingleChoiceDialogState(title = stringResource(id = R.string.module_profile_rule_new),
            items = listOf(
                SingleChoiceItem(
                    id = ID_TIME_OF_A_DAY,
                    icon = Icons.Filled.Schedule,
                    label = "Time of a day (coming soon)"
                ),
                SingleChoiceItem(
                    id = ID_REGULAR_INTERVAL,
                    icon = Icons.Filled.Timer,
                    label = "Regular interval"
                ),
            ),
            onItemClick = {
                when (it) {
                    ID_REGULAR_INTERVAL -> {
                        durationPickerDialogState.show()
                    }
                    ID_TIME_OF_A_DAY -> {}
                }
            })

    ThanoxSmallAppBarScaffold(title = {
        Text(
            text = stringResource(id = R.string.module_profile_pref_title_rule_engine_date_time),
            style = TypographyDefaults.appBarTitleTextStyle()
        )
    },
        onBackPressed = { finish() },
        actions = {
        }, floatingActionButton = {
            ExtendableFloatingActionButton(
                extended = false,
                text = { Text(text = stringResource(id = R.string.module_profile_rule_new)) },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.module_profile_rule_new)
                    )
                }) {
                typeSelectDialogState.show()
            }
        }) { contentPadding ->

        SingleChoiceDialog(state = typeSelectDialogState)
        DurationPickerDialog(state = durationPickerDialogState)
    }
}