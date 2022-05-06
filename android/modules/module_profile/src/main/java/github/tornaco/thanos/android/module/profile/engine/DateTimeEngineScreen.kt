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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.*
import github.tornaco.thanos.android.module.profile.R

private class UIState {
    var showCreateRegularIntervalDialog by mutableStateOf(false)
}

@Composable
fun Activity.DateTimeEngineScreen() {
    val uiState = rememberUIState()

    val dialogState =
        rememberDialogState(title = stringResource(id = R.string.module_profile_rule_new),
            items = listOf(
                SingleChoiceItem(id = "", icon = Icons.Filled.Schedule, label = "Time of a day (coming soon)"),
                SingleChoiceItem(id = "", icon = Icons.Filled.Timer, label = "Regular interval (coming soon)"),
            ),
            onItemClick = {
                uiState.showCreateRegularIntervalDialog = true
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
                dialogState.show()
            }
        }) { contentPadding ->

        SingleChoiceDialog(state = dialogState)
    }
}


@Composable
private fun CreateRegularIntervalDialog(uiState: UIState) {
    if (uiState.showCreateRegularIntervalDialog) {
        // TODO Impl.
    }
}


@Composable
private fun rememberUIState(): UIState {
    return remember {
        UIState()
    }
}
