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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.enro.core.compose.registerForNavigationResult
import github.tornaco.android.thanos.module.compose.common.TinySpacer
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.*
import github.tornaco.thanos.android.module.profile.R
import java.util.*
import kotlin.math.min
import kotlin.time.DurationUnit
import kotlin.time.toDuration

private const val ID_TIME_OF_A_DAY = "tod"
private const val ID_REGULAR_INTERVAL = "ri"

@Composable
fun Activity.DateTimeEngineScreen() {
    val viewModel = hiltViewModel<DateTimeEngineViewModel>()
    val state by viewModel.state.collectAsState()

    SideEffect {
        viewModel.loadPendingWorks()
    }

    val navHandle = registerForNavigationResult<NewRegularIntervalResult> { regularInterval ->
        viewModel.schedulePeriodicWork(
            regularInterval.tag,
            regularInterval.durationMillis.toDuration(DurationUnit.MILLISECONDS)
        )
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
                    label = stringResource(id = R.string.module_profile_date_time_regular_interval)
                ),
            ),
            onItemClick = {
                when (it) {
                    ID_REGULAR_INTERVAL -> {
                        navHandle.open(NewRegularInterval)
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

        WorkList(contentPadding = contentPadding, state = state.workStates) {
            viewModel.deleteById(it)
        }
    }
}

@Composable
private fun WorkList(
    state: List<WorkState>,
    contentPadding: PaddingValues,
    delete: (id: UUID) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxWidth(), contentPadding = contentPadding) {
        items(state) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .heightIn(min = 64.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                ) {
                    val typeText = if (it.type == Type.Periodic) {
                        stringResource(id = R.string.module_profile_date_time_regular_interval)
                    } else {
                        "Unknown"
                    }
                    val valueText = if (it.type == Type.Periodic) {
                        it.value.toDuration(DurationUnit.MILLISECONDS).toString()
                    } else {
                        ""
                    }

                    Text(
                        text = typeText, style = MaterialTheme.typography.titleMedium,
                        maxLines = 1
                    )
                    TinySpacer()
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Tag,
                            contentDescription = "tag"
                        )
                        TinySpacer()
                        Text(
                            text = it.tag.substring(0, min(18, it.tag.length)),
                            style = MaterialTheme.typography.labelMedium,
                            maxLines = 1
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Timer,
                            contentDescription = "interval"
                        )
                        TinySpacer()
                        Text(
                            text = valueText, style = MaterialTheme.typography.labelMedium,
                            maxLines = 1
                        )
                    }
                }

                IconButton(onClick = {
                    delete(it.id)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete_bin_fill),
                        contentDescription = "Remove"
                    )
                }
            }

        }
    }
}