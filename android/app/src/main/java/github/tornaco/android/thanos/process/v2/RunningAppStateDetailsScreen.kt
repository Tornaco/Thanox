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

package github.tornaco.android.thanos.process.v2

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.enro.annotations.ExperimentalComposableDestination
import dev.enro.annotations.NavigationDestination
import dev.enro.core.NavigationKey
import dev.enro.core.close
import dev.enro.core.compose.navigationHandle
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.module.compose.common.AppLabelText
import github.tornaco.android.thanos.module.compose.common.theme.ColorDefaults
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.AppIcon
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxMediumAppBarScaffold
import kotlinx.parcelize.Parcelize

@Parcelize
data class RunningAppStateDetails(val state: RunningAppState) : NavigationKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@ExperimentalComposableDestination
@NavigationDestination(RunningAppStateDetails::class)
fun RunningAppStateDetailsPage() {
    val navHandle = navigationHandle<RunningAppStateDetails>()
    val viewModel: RunningAppDetailViewModel = hiltViewModel()
    val state = navHandle.key.state
    RunningAppStateDetailsScreen(state, viewModel) {
        navHandle.close()
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun RunningAppStateDetailsScreen(
    runningAppState: RunningAppState,
    viewModel: RunningAppDetailViewModel,
    onBackPressed: () -> Unit
) {
    ThanoxMediumAppBarScaffold(
        title = {
            Text(
                text = runningAppState.appInfo.appLabel,
                style = TypographyDefaults.appBarTitleTextStyle()
            )
        }, onBackPressed = onBackPressed
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            runningAppState.processState.forEach { runningProcessState ->
                Card(
                    modifier = Modifier.padding(16.dp),
                    containerColor = ColorDefaults.backgroundSurfaceColor()
                ) {
                    Column {
                        ProcessSection(runningProcessState, viewModel)
                        ServiceSection(runningAppState, runningProcessState, viewModel)
                    }
                }
            }
        }
    }
}

@Composable
private fun ServiceSection(
    runningAppState: RunningAppState,
    runningProcessState: RunningProcessState,
    viewModel: RunningAppDetailViewModel,

    ) {
    Column(modifier = Modifier.padding(16.dp)) {
        val runningServiceCount = runningProcessState.runningServices.size
        Row(verticalAlignment = CenterVertically) {
            Text(
                text = if (runningServiceCount > 0) stringResource(
                    id = R.string.running_processes_item_description_s, runningServiceCount
                ) else stringResource(id = R.string.runningservicedetails_services_title),
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp)
            )
        }
        Spacer(modifier = Modifier.size(8.dp))
        if (runningServiceCount == 0) {
            Text(
                text = stringResource(id = R.string.no_running_services),
                style = MaterialTheme.typography.titleSmall
            )
        } else {
            runningProcessState.runningServices.forEach { service ->
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.size(12.dp))
                    Row(verticalAlignment = CenterVertically) {
                        AppIcon(modifier = Modifier.size(24.dp), appInfo = runningAppState.appInfo)
                        Spacer(modifier = Modifier.size(8.dp))
                        AppLabelText(appLabel = service.serviceLabel)
                    }
                    Text(
                        text = service.running.service.flattenToShortString(),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        FilledTonalButton(onClick = {
                            viewModel.stopService(service)
                        }) {
                            Text(text = stringResource(id = R.string.service_stop))
                        }
                        Spacer(modifier = Modifier.size(12.dp))
                        FilledTonalButton(onClick = {
                            viewModel.copyServiceName(service)
                        }) {
                            Text(text = stringResource(id = R.string.menu_title_copy))
                        }
                    }

                    Spacer(modifier = Modifier.size(12.dp))
                }
            }
        }
    }
}

@Composable
private fun ProcessSection(
    runningProcessState: RunningProcessState,
    viewModel: RunningAppDetailViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(72.dp)
            .padding(16.dp)
    ) {
        Column {
            Row {
                Text(
                    modifier = Modifier.alignByBaseline(),
                    text = stringResource(id = R.string.runningservicedetails_processes_title),
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    modifier = Modifier.alignByBaseline(),
                    text = " (id: ${runningProcessState.process.pid})",
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Spacer(modifier = Modifier.size(12.dp))
            AppLabelText(appLabel = runningProcessState.process.processName)
            Spacer(modifier = Modifier.size(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.End
            ) {
                FilledTonalButton(onClick = { }) {
                    Text(text = stringResource(id = R.string.service_stop))
                }
                Spacer(modifier = Modifier.size(12.dp))
                FilledTonalButton(onClick = {
                    viewModel.copyProcessName(runningProcessState)
                }) {
                    Text(text = stringResource(id = R.string.menu_title_copy))
                }
            }
        }
    }
}