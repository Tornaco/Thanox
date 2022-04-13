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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.elvishew.xlog.XLog
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
object RunningAppStateDetails : NavigationKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@ExperimentalComposableDestination
@NavigationDestination(RunningAppStateDetails::class)
fun RunningAppStateDetailsPage() {
    val navHandle = navigationHandle()
    val viewModel = hiltViewModel<ProcessManageViewModel>()
    XLog.d("viewModel= $viewModel")
    val state by viewModel.state.collectAsState()
    RunningAppStateDetailsScreen(state) {
        navHandle.close()
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun RunningAppStateDetailsScreen(state: ProcessManageState, onBackPressed: () -> Unit) {
    state.selectedRunningAppStateItem?.let { runningAppState ->
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
                            ProcessSection(runningProcessState)
                            Spacer(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .height(2.dp)
                                    .background(MaterialTheme.colorScheme.background)
                            )
                            ServiceSection(runningAppState, runningProcessState)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ServiceSection(
    runningAppState: RunningAppState,
    runningProcessState: RunningProcessState
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = CenterVertically) {
            Text(
                text = stringResource(id = R.string.runningservicedetails_services_title),
                style = MaterialTheme.typography.titleLarge
            )
        }
        Spacer(modifier = Modifier.size(8.dp))
        if (runningProcessState.runningServices.isEmpty()) {
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
                        FilledTonalButton(onClick = { }) {
                            Text(text = stringResource(id = R.string.service_stop))
                        }
                        Spacer(modifier = Modifier.size(12.dp))
                        FilledTonalButton(onClick = { }) {
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
private fun ProcessSection(runningProcessState: RunningProcessState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(72.dp)
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.runningservicedetails_processes_title),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.size(12.dp))
            AppLabelText(appLabel = "id: ${runningProcessState.process.pid}")
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
                FilledTonalButton(onClick = { }) {
                    Text(text = stringResource(id = R.string.menu_title_copy))
                }
            }
        }
    }
}