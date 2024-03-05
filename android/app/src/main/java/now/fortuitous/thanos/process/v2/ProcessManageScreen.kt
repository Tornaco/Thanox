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
package now.fortuitous.thanos.process.v2

import android.app.Activity
import android.text.format.DateUtils
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.elvishew.xlog.XLog
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.module.compose.common.loader.AppSetFilterItem
import github.tornaco.android.thanos.module.compose.common.requireActivity
import github.tornaco.android.thanos.module.compose.common.theme.ColorDefaults
import github.tornaco.android.thanos.module.compose.common.widget.AppIcon
import github.tornaco.android.thanos.module.compose.common.widget.AppLabelText
import github.tornaco.android.thanos.module.compose.common.widget.ExtendableFloatingActionButton
import github.tornaco.android.thanos.module.compose.common.widget.FilterDropDown
import github.tornaco.android.thanos.module.compose.common.widget.MD3Badge
import github.tornaco.android.thanos.module.compose.common.widget.SmallSpacer
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxSmallAppBarScaffold
import github.tornaco.android.thanos.module.compose.common.widget.TinySpacer
import github.tornaco.android.thanos.module.compose.common.widget.clickableWithRipple
import github.tornaco.android.thanos.module.compose.common.widget.rememberSearchBarState
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ProcessManageScreen(
    onBackPressed: () -> Unit,
    toLegacyUi: () -> Unit
) {
    val viewModel = hiltViewModel<ProcessManageViewModel>(LocalContext.current.requireActivity())
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    viewModel.bindLifecycle(lifecycle)
    val state by viewModel.state.collectAsState()
    val detailLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.refresh(0)
            }
        }
    )

    XLog.d("viewModel= $viewModel by owner: ${LocalViewModelStoreOwner.current}")

    LaunchedEffect(viewModel) {
        viewModel.init()
    }

    val listState = rememberLazyListState()
    val searchBarState = rememberSearchBarState()

    LaunchedEffect(searchBarState) {
        snapshotFlow { searchBarState.keyword }
            .distinctUntilChanged()
            .collect {
                viewModel.keywordChanged(it)
            }
    }

    BackHandler(searchBarState.showSearchBar) {
        searchBarState.closeSearchBar()
    }

    ThanoxSmallAppBarScaffold(
        title = {
            Text(
                stringResource(id = R.string.feature_title_process_manage),
            )
        },
        actions = {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    toLegacyUi()
                }) {
                    Icon(
                        imageVector = Icons.Filled.OpenInNew,
                        contentDescription = "Back to legacy ui"
                    )
                }
                IconButton(onClick = {
                    searchBarState.showSearchBar()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search"
                    )
                }
            }
        },
        searchBarState = searchBarState,
        floatingActionButton = {
            ExtendableFloatingActionButton(
                extended = false,
                text = { Text(text = stringResource(id = R.string.feature_title_one_key_boost)) },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_rocket_line),
                        contentDescription = "Boost"
                    )
                }) {
                viewModel.clearBgTasks()
            }
        },
        onBackPressed = onBackPressed
    ) { contentPadding ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(state.isLoading),
            onRefresh = { viewModel.refresh() },
            // Shift the indicator to match the list content padding
            indicatorPadding = contentPadding,
            // We want the indicator to draw within the padding
            clipIndicatorToPadding = false,
            // Tweak the indicator to scale up/down
            indicator = { state, refreshTriggerDistance ->
                SwipeRefreshIndicator(
                    state = state,
                    refreshTriggerDistance = refreshTriggerDistance,
                    scale = true,
                    arrowEnabled = false,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            }
        ) {
            val context = LocalContext.current
            RunningAppList(
                modifier = Modifier.padding(contentPadding),
                lazyListState = listState,
                state = state,
                onRunningItemClick = {
                    detailLauncher.launch(
                        RunningAppStateDetailsActivity.Starter.intent(
                            context,
                            RunningAppStateDetails(it)
                        )
                    )
                },
                onNotRunningItemClick = {
                    now.fortuitous.thanos.apps.AppDetailsActivity.start(context, it)
                },
                onFilterItemSelected = {
                    viewModel.onFilterItemSelected(it)
                },
                setRunningExpand = {
                    viewModel.expandRunning(it)
                },
                setCachedExpand = {
                    viewModel.expandCached(it)
                })
        }
    }
}

@Composable
private fun AppFilterDropDown(
    state: ProcessManageState,
    onFilterItemSelected: (AppSetFilterItem) -> Unit
) {
    FilterDropDown(
        icon = Icons.Filled.FilterAlt,
        selectedItem = state.selectedAppSetFilterItem,
        allItems = state.appFilterItems,
        onItemSelected = onFilterItemSelected
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RunningAppList(
    modifier: Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    state: ProcessManageState,
    onRunningItemClick: (RunningAppState) -> Unit,
    onNotRunningItemClick: (AppInfo) -> Unit,
    onFilterItemSelected: (AppSetFilterItem) -> Unit,
    setRunningExpand: (Boolean) -> Unit,
    setCachedExpand: (Boolean) -> Unit
) {
    LazyColumn(
        state = lazyListState,
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.surface)
            .fillMaxSize()
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                AppFilterDropDown(state, onFilterItemSelected)
                Spacer(modifier = Modifier.size(16.dp))
            }
        }

        if (state.runningAppStates.isNotEmpty()) {
            stickyHeader {
                RunningGroupHeader(
                    state.runningAppStates.size,
                    state.isRunningExpand,
                    setRunningExpand
                )
            }
            items(state.runningAppStates) {
                AnimatedVisibility(visible = state.isRunningExpand) {
                    RunningAppItem(
                        it,
                        state.cpuUsageRatioStates[it.appInfo],
                        state.netSpeedStates[it.appInfo],
                        onRunningItemClick
                    )
                }
            }
        }

        if (state.runningAppStatesBg.isNotEmpty()) {
            stickyHeader {
                CachedGroupHeader(
                    state.runningAppStatesBg.size,
                    state.isCacheExpand,
                    setCachedExpand
                )
            }
            items(state.runningAppStatesBg) {
                AnimatedVisibility(visible = state.isCacheExpand) {
                    RunningAppItem(
                        it,
                        state.cpuUsageRatioStates[it.appInfo],
                        state.netSpeedStates[it.appInfo],
                        onRunningItemClick
                    )
                }
            }
        }

        if (state.appsNotRunning.isNotEmpty()) {
            stickyHeader { NotRunningGroupHeader(state.appsNotRunning.size) }
            items(state.appsNotRunning) {
                NotRunningAppItem(it, onNotRunningItemClick)
            }
        }
    }
}

@Composable
fun CachedGroupHeader(itemCount: Int, expand: Boolean, setExpand: (Boolean) -> Unit) {
    Surface(tonalElevation = 2.dp) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ColorDefaults.backgroundSurfaceColor())
                .clickableWithRipple {
                    setExpand(!expand)
                }
                .padding(horizontal = 20.dp, vertical = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val text = stringResource(id = R.string.running_process_background)
                Text(
                    text = "$text - $itemCount",
                    style = MaterialTheme.typography.titleMedium
                )
                ExpandIndicator(expand = expand)
            }
        }
    }
}

@Composable
fun RunningGroupHeader(itemCount: Int, expand: Boolean, setExpand: (Boolean) -> Unit) {
    Surface(tonalElevation = 2.dp) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ColorDefaults.backgroundSurfaceColor())
                .clickableWithRipple {
                    setExpand(!expand)
                }
                .padding(horizontal = 20.dp, vertical = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val text = stringResource(id = R.string.running_process_running)
                Text(
                    text = "$text - $itemCount",
                    style = MaterialTheme.typography.titleMedium
                )
                ExpandIndicator(expand = expand)
            }

        }
    }
}

@Composable
private fun ExpandIndicator(expand: Boolean) {
    Icon(
        modifier = Modifier.size(24.dp),
        painter = painterResource(
            id = if (expand) {
                github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_arrow_drop_down_fill
            } else {
                github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_arrow_drop_up_fill
            }
        ),
        contentDescription = ""
    )
}

@Composable
fun NotRunningGroupHeader(itemCount: Int) {
    Surface(tonalElevation = 2.dp) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ColorDefaults.backgroundSurfaceColor())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            val text = stringResource(id = R.string.running_process_not_running)
            Text(
                text = "$text - $itemCount",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun RunningAppItem(
    appState: RunningAppState,
    cpuRatio: String?,
    netSpeed: NetSpeedState?,
    onItemClick: (RunningAppState) -> Unit
) {
    Box(
        modifier = Modifier
            .clickableWithRipple {
                onItemClick(appState)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .heightIn(min = 72.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                AppIcon(modifier = Modifier.size(38.dp), appState.appInfo)
                Spacer(modifier = Modifier.size(12.dp))
                Column(verticalArrangement = Arrangement.Center) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AppLabelText(
                            Modifier.sizeIn(maxWidth = 240.dp),
                            appState.appInfo.appLabel
                        )
                        if (appState.isPlayingBack) {
                            TinySpacer()
                            Icon(
                                modifier = Modifier.size(16.dp),
                                painter = painterResource(id = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_music_2_fill),
                                contentDescription = "Playing",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    if (appState.serviceCount == 0) {
                        PText(appState)
                    } else {
                        PSText(appState)
                    }
                    AppRunningTime(appState)
                }
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                MemSizeBadge(appState)

                cpuRatio?.let {
                    SmallSpacer()
                    MD3Badge("CPU $it%")
                }

                SmallSpacer()
                AnimatedVisibility(visible = netSpeed != null) {
                    netSpeed?.let {
                        NetSpeedBadge(it)
                    }
                }

            }
        }
    }
}

@Composable
private fun AppRunningTime(appState: RunningAppState) {
    if (appState.runningTimeMillis != null) {
        val runningTimeStr = DateUtils.formatElapsedTime(null, appState.runningTimeMillis / 1000L)
        Text(
            text = "${
                stringResource(
                    id = R.string.service_running_time
                )
            } $runningTimeStr",
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NotRunningAppItem(appInfo: AppInfo, onItemClick: (AppInfo) -> Unit) {
    Box(
        modifier = Modifier
            .clickableWithRipple {
                onItemClick(appInfo)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .heightIn(min = 64.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                AppIcon(modifier = Modifier.size(38.dp), appInfo)
                Spacer(modifier = Modifier.size(12.dp))
                Column(verticalArrangement = Arrangement.Center) {
                    AppLabelText(
                        Modifier.sizeIn(maxWidth = 220.dp),
                        appInfo.appLabel
                    )
                }
            }
        }
    }
}


@Composable
fun MemSizeBadge(appState: RunningAppState) {
    MD3Badge(appState.sizeStr)
}

@Composable
fun NetSpeedBadge(netSpeed: NetSpeedState) {
    MD3Badge("↑ ${netSpeed.up}/s ↓ ${netSpeed.down}/s")
}

@Composable
fun PSText(appState: RunningAppState) {
    Text(
        text = stringResource(
            id = R.string.running_processes_item_description_p_s,
            appState.processState.size,
            appState.serviceCount
        ),
        fontSize = 12.sp
    )
}

@Composable
fun PText(appState: RunningAppState) {
    Text(
        text = stringResource(
            id = R.string.running_processes_item_description_p,
            appState.processState.size
        ),
        fontSize = 12.sp
    )
}