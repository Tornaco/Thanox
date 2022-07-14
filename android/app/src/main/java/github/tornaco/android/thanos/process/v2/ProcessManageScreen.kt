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

import android.text.format.DateUtils
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import dev.enro.core.compose.registerForNavigationResult
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.apps.AppDetailsActivity
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.module.compose.common.widget.AppLabelText
import github.tornaco.android.thanos.module.compose.common.widget.SmallSpacer
import github.tornaco.android.thanos.module.compose.common.widget.clickableWithRipple
import github.tornaco.android.thanos.module.compose.common.loader.AppSetFilterItem
import github.tornaco.android.thanos.module.compose.common.requireActivity
import github.tornaco.android.thanos.module.compose.common.theme.ColorDefaults
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults.appBarTitleTextStyle
import github.tornaco.android.thanos.module.compose.common.widget.*
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
    val navHandle = registerForNavigationResult<Boolean> { shouldUpdate ->
        if (shouldUpdate) {
            viewModel.refresh(0)
        }
    }
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
                style = appBarTitleTextStyle()
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
                    navHandle.open(RunningAppStateDetails(it))
                },
                onNotRunningItemClick = {
                    AppDetailsActivity.start(context, it)
                },
                onFilterItemSelected = {
                    viewModel.onFilterItemSelected(it)
                })
        }
    }
}

@Composable
private fun AppFilterDropDown(state: ProcessManageState, onFilterItemSelected: (AppSetFilterItem) -> Unit) {
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
    onFilterItemSelected: (AppSetFilterItem) -> Unit
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
            stickyHeader { RunningGroupHeader(state.runningAppStates.size) }
            items(state.runningAppStates) {
                RunningAppItem(
                    it,
                    state.cpuUsageRatioStates[it.appInfo],
                    state.netSpeedStates[it.appInfo],
                    onRunningItemClick
                )
            }
        }

        if (state.runningAppStatesBg.isNotEmpty()) {
            stickyHeader { CachedGroupHeader(state.runningAppStatesBg.size) }
            items(state.runningAppStatesBg) {
                RunningAppItem(
                    it,
                    state.cpuUsageRatioStates[it.appInfo],
                    state.netSpeedStates[it.appInfo],
                    onRunningItemClick
                )
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
fun CachedGroupHeader(itemCount: Int) {
    Surface(tonalElevation = 2.dp) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ColorDefaults.backgroundSurfaceColor())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            val text = stringResource(id = R.string.running_process_background)
            Text(
                text = "$text - $itemCount",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun RunningGroupHeader(itemCount: Int) {
    Surface(tonalElevation = 2.dp) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ColorDefaults.backgroundSurfaceColor())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            val text = stringResource(id = R.string.running_process_running)
            Text(
                text = "$text - $itemCount",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun NotRunningGroupHeader(itemCount: Int) {
    Surface(tonalElevation = 2.dp) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ColorDefaults.backgroundSurfaceColor())
                .padding(horizontal = 20.dp, vertical = 8.dp),
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

@OptIn(ExperimentalMaterialApi::class)
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
                    AppLabelText(
                        Modifier.sizeIn(maxWidth = 240.dp),
                        appState.appInfo.appLabel
                    )
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