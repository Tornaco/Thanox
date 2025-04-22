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

package now.fortuitous.thanos.process.v2

import android.app.ActivityManager
import android.os.SystemClock
import android.text.format.DateUtils
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LabelImportant
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.module.compose.common.requireActivity
import github.tornaco.android.thanos.module.compose.common.theme.ColorDefaults
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.AppIcon
import github.tornaco.android.thanos.module.compose.common.widget.AppLabelText
import github.tornaco.android.thanos.module.compose.common.widget.DropdownPopUpMenu
import github.tornaco.android.thanos.module.compose.common.widget.MD3Badge
import github.tornaco.android.thanos.module.compose.common.widget.MenuItem
import github.tornaco.android.thanos.module.compose.common.widget.SmallSpacer
import github.tornaco.android.thanos.module.compose.common.widget.StandardSpacer
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxSmallAppBarScaffold
import github.tornaco.android.thanos.module.compose.common.widget.TinySpacer
import github.tornaco.android.thanos.module.compose.common.widget.clickableWithRipple
import github.tornaco.android.thanos.util.ToastUtils
import github.tornaco.thanos.android.module.profile.AddToGlobalVarDialog
import github.tornaco.thanos.module.component.manager.AddToSmartStandByKeepsVarDialog
import github.tornaco.thanos.module.component.manager.redesign.BlockerRuleIconWithInfoDialog
import github.tornaco.thanos.module.component.manager.redesign.LCRuleIconWithInfoDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun RunningAppStateDetailsPage(details: RunningAppStateDetails, closeSetResult: (Boolean) -> Unit) {
    val viewModel: RunningAppDetailViewModel = hiltViewModel()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    viewModel.bindLifecycle(lifecycle)
    val state = details.state

    val detailState by viewModel.state.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.startQueryCpuUsage(state)
    }

    RunningAppStateDetailsScreen(state, detailState.cpuState, viewModel) {
        closeSetResult(it)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun RunningAppStateDetailsScreen(
    runningAppState: RunningAppState,
    cpuUsageStatsState: CpuUsageStatsState,
    viewModel: RunningAppDetailViewModel,
    closeScreen: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    ThanoxSmallAppBarScaffold(
        title = {
            Text(
                text = runningAppState.appInfo.appLabel,
                style = TypographyDefaults.appBarTitleTextStyle()
            )
        },
        actions = {
            IconButton(onClick = {
                now.fortuitous.thanos.apps.AppDetailsActivity.start(
                    context,
                    runningAppState.appInfo
                )
            }) {
                Icon(
                    painter = painterResource(id = github.tornaco.android.thanos.module.common.R.drawable.module_common_ic_md_outline_settings_24),
                    contentDescription = "Settings"
                )
            }
        },
        onBackPressed = {
            closeScreen(viewModel.appStateChanged)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            runningAppState.processState.forEach { runningProcessState ->
                AnimatedVisibility(visible = !runningProcessState.isStopped) {
                    Card(
                        modifier = Modifier.padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = ColorDefaults.backgroundSurfaceColor()
                        )
                    ) {
                        Column {
                            StandardSpacer()
                            ProcessSection(
                                runningAppState.appInfo,
                                runningProcessState,
                                cpuUsageStatsState,
                                viewModel
                            )
                            StandardSpacer()
                            ServiceSection(runningAppState, runningProcessState, viewModel)
                            StandardSpacer()
                        }
                    }
                }
            }

            FilledTonalButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = {
                    viewModel.forceStop(runningAppState)
                    closeScreen(true)
                }) {
                Text(text = "${stringResource(id = github.tornaco.android.thanos.res.R.string.service_stop)} ${runningAppState.appInfo.appLabel}")
            }
        }
    }
    BackHandler {
        closeScreen(viewModel.appStateChanged)
    }
}

@Composable
private fun ServiceSection(
    runningAppState: RunningAppState,
    runningProcessState: RunningProcessState,
    viewModel: RunningAppDetailViewModel,
) {
    Column(modifier = Modifier) {
        val runningServiceCount = runningProcessState.runningServices.size
        Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = CenterVertically) {
            Text(
                text = if (runningServiceCount > 0) stringResource(
                    id = github.tornaco.android.thanos.res.R.string.running_processes_item_description_s,
                    runningServiceCount
                ) else stringResource(id = github.tornaco.android.thanos.res.R.string.runningservicedetails_services_title),
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp)
            )
        }
        Spacer(modifier = Modifier.size(8.dp))
        if (runningServiceCount == 0) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = stringResource(id = github.tornaco.android.thanos.res.R.string.no_running_services),
                style = MaterialTheme.typography.titleSmall
            )
        } else {
            runningProcessState.runningServices.forEach { service ->
                AnimatedVisibility(visible = !service.isStopped) {
                    ServiceTile(runningAppState, service, viewModel)
                }
            }
        }
    }
}

@Composable
private fun ServiceTile(
    runningAppState: RunningAppState,
    service: RunningService,
    viewModel: RunningAppDetailViewModel,
) {
    val context = LocalContext.current.requireActivity()
    val scope = rememberCoroutineScope()

    val popMenuExpend = remember { mutableStateOf(false) }
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clickableWithRipple {
                popMenuExpend.value = true
            }
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.size(12.dp))
        Row(verticalAlignment = CenterVertically) {
            AppIcon(modifier = Modifier.size(42.dp), appInfo = runningAppState.appInfo)
            Spacer(modifier = Modifier.size(8.dp))
            Column(verticalArrangement = Arrangement.Center) {
                AppLabelText(appLabel = service.serviceLabel)
                ServiceRunningTime(service)
            }
        }
        TinySpacer()
        Text(
            text = service.running.service.flattenToShortString(),
            style = MaterialTheme.typography.labelMedium
        )
        if (service.lcRule != null || service.blockRule != null) {
            StandardSpacer()
            Row(verticalAlignment = CenterVertically) {
                service.lcRule?.let {
                    LCRuleIconWithInfoDialog(it)
                }
                service.blockRule?.let {
                    StandardSpacer()
                    BlockerRuleIconWithInfoDialog(it)
                }
            }
        }
        Spacer(modifier = Modifier.size(12.dp))
        ServicePopupMenu(
            popMenuExpend = popMenuExpend,
            service = service,
            viewModel = viewModel,
            addToGlobalVar = {
                scope.launch {
                    delay(120)
                    AddToGlobalVarDialog(
                        context = context,
                        service.running.service.flattenToShortString()
                    ).show()
                }
            }, addToSmartStandByKeeps = {
                scope.launch {
                    delay(120)
                    AddToSmartStandByKeepsVarDialog(
                        context = context,
                        service.running.service
                    ).show()
                }
            })
    }
}

@Composable
private fun ServiceRunningTime(service: RunningService) {
    val activeSince: Long =
        if (service.running.restarting == 0L) service.running.activeSince else -1
    if (activeSince > 0) {
        var runningSeconds by remember {
            mutableStateOf((SystemClock.elapsedRealtime() - activeSince) / 1000L)
        }
        LaunchedEffect(runningSeconds) {
            delay(1000L)
            runningSeconds += 1
        }
        val runningTimeStr = DateUtils.formatElapsedTime(null, runningSeconds)
        Text(
            text = "${service.clientLabel ?: stringResource(id = github.tornaco.android.thanos.res.R.string.service_started_by_app)} • ${
                stringResource(
                    id = github.tornaco.android.thanos.res.R.string.service_running_time
                )
            } $runningTimeStr",
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
private fun ServicePopupMenu(
    popMenuExpend: MutableState<Boolean>,
    service: RunningService,
    viewModel: RunningAppDetailViewModel,
    addToGlobalVar: () -> Unit,
    addToSmartStandByKeeps: () -> Unit,
) {
    val context = LocalContext.current

    DropdownPopUpMenu(
        popMenuExpend,
        items = listOf(
            MenuItem(
                "copy",
                stringResource(id = github.tornaco.android.thanos.res.R.string.menu_title_copy),
                github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_file_copy_fill
            ),
            MenuItem(
                "stop",
                stringResource(id = github.tornaco.android.thanos.res.R.string.service_stop),
                github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_close_fill
            ),
            MenuItem(
                "addToGlobalVar",
                stringResource(id = github.tornaco.android.thanos.res.R.string.module_profile_add_to_global_var),
                R.drawable.ic_baseline_code_24
            ),
            MenuItem(
                "addToSmartStandByKeeps",
                stringResource(id = github.tornaco.android.thanos.res.R.string.module_component_manager_keep_service_smart_standby),
                R.drawable.ic_mickey_line
            )
        )
    ) {
        when (it.id) {
            "copy" -> {
                viewModel.copyServiceName(service)
            }

            "stop" -> {
                if (viewModel.stopService(service)) {
                    ToastUtils.ok(context)
                    service.isStopped = true
                } else {
                    ToastUtils.nook(context)
                }
            }

            "addToGlobalVar" -> {
                addToGlobalVar()
            }

            "addToSmartStandByKeeps" -> {
                addToSmartStandByKeeps()
            }

            else -> {

            }
        }
    }
}

@Composable
private fun ProcessSection(
    appInfo: AppInfo,
    runningProcessState: RunningProcessState,
    cpuUsageStatsState: CpuUsageStatsState,
    viewModel: RunningAppDetailViewModel,
) {
    val popMenuExpand = remember { mutableStateOf(false) }

    val context = LocalContext.current.requireActivity()
    val scope = rememberCoroutineScope()

    val isCached =
        runningProcessState.process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_CACHED
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(72.dp)
            .clickableWithRipple {
                popMenuExpand.value = true
            }
            .padding(horizontal = 16.dp)
    ) {
        Column {
            Spacer(modifier = Modifier.size(12.dp))
            Row(verticalAlignment = CenterVertically) {
                if (runningProcessState.isMain) {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        imageVector = Icons.Outlined.LabelImportant,
                        contentDescription = "Main process"
                    )
                    SmallSpacer()
                }
                Text(
                    modifier = Modifier.alignByBaseline(),
                    text = stringResource(id = github.tornaco.android.thanos.res.R.string.runningservicedetails_processes_title),
                    style = MaterialTheme.typography.titleLarge
                )

                Row(modifier = Modifier.alignByBaseline(), verticalAlignment = CenterVertically) {
                    Text(
                        text = " (id: ${runningProcessState.process.pid})",
                        style = MaterialTheme.typography.labelMedium
                    )
                    SmallSpacer()
                    MD3Badge(runningProcessState.sizeStr)
                    cpuUsageStatsState.forPid(runningProcessState.process.pid)?.let { stats ->
                        SmallSpacer()
                        MD3Badge("CPU ${stats.cpuRatioString}%")
                    }
                }
            }
            Spacer(modifier = Modifier.size(12.dp))

            Row(verticalAlignment = CenterVertically) {
                AppIcon(modifier = Modifier.size(42.dp), appInfo = appInfo)
                Spacer(modifier = Modifier.size(8.dp))
                Column(verticalArrangement = Arrangement.Center) {
                    AppLabelText(appLabel = runningProcessState.process.processName)
                    SmallSpacer()
                    Text(
                        text = if (isCached) stringResource(id = github.tornaco.android.thanos.res.R.string.cached) else stringResource(
                            id = github.tornaco.android.thanos.res.R.string.running_process_running
                        ),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            Spacer(modifier = Modifier.size(12.dp))
            ProcessPopupMenu(
                popMenuExpand = popMenuExpand,
                viewModel = viewModel,
                runningProcessState = runningProcessState,
                addToGlobalVar = {
                    scope.launch {
                        delay(120)
                        AddToGlobalVarDialog(
                            context = context,
                            runningProcessState.process.processName
                        ).show()
                    }
                })
        }
    }
}

@Composable
private fun ProcessPopupMenu(
    popMenuExpand: MutableState<Boolean>,
    viewModel: RunningAppDetailViewModel,
    runningProcessState: RunningProcessState,
    addToGlobalVar: () -> Unit,
) {
    val context = LocalContext.current
    DropdownPopUpMenu(
        popMenuExpand,
        items = listOf(
            MenuItem(
                "copy",
                stringResource(id = github.tornaco.android.thanos.res.R.string.menu_title_copy),
                github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_file_copy_fill
            ),
            MenuItem(
                "stop",
                stringResource(id = github.tornaco.android.thanos.res.R.string.service_stop),
                github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_close_fill
            ),
            MenuItem(
                "addToGlobalVar",
                stringResource(id = github.tornaco.android.thanos.res.R.string.module_profile_add_to_global_var),
                R.drawable.ic_baseline_code_24
            )
        )
    ) {
        when (it.id) {
            "copy" -> {
                viewModel.copyProcessName(runningProcessState)
            }

            "stop" -> {
                if (viewModel.stopProcess(runningProcessState)) {
                    ToastUtils.ok(context)
                    runningProcessState.isStopped = true
                } else {
                    ToastUtils.nook(context)
                }
            }

            "addToGlobalVar" -> {
                addToGlobalVar()
            }

            else -> {

            }
        }
    }
}