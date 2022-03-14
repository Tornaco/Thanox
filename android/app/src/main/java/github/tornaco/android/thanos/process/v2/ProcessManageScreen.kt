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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.primarySurface
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.module.compose.common.widget.AppIcon
import github.tornaco.android.thanos.module.compose.common.widget.AutoResizeText
import github.tornaco.android.thanos.module.compose.common.widget.FontSizeRange
import github.tornaco.android.thanos.module.compose.common.widget.MD3Badge
import github.tornaco.android.thanos.module.compose.common.widget.md3.LargeTopAppBarX
import github.tornaco.android.thanos.module.compose.common.widget.md3.TopAppBarDefaults
import github.tornaco.android.thanos.module.compose.common.widget.md3.TopAppBarScrollBehaviorX

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProcessManageScreen(
    onBackPressed: () -> Unit,
    toLegacyUi: () -> Unit
) {
    val viewModel = hiltViewModel<ProcessManageViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.startLoading()
    }

    val scrollBehavior = remember { TopAppBarDefaults.enterAlwaysScrollBehavior() }
    com.google.accompanist.insets.ui.Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior = scrollBehavior,
                onBackPressed = onBackPressed,
                toLegacyUi = toLegacyUi
            )
        },
        bottomBar = {
            // We add a spacer as a bottom bar, which is the same height as
            // the navigation bar
            Spacer(
                Modifier
                    .navigationBarsHeight()
                    .fillMaxWidth()
            )
        },
    ) { contentPadding ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(state.isLoading),
            onRefresh = { viewModel.startLoading() },
            // Shift the indicator to match the list content padding
            indicatorPadding = contentPadding,
            // We want the indicator to draw within the padding
            clipIndicatorToPadding = false,
            // Tweak the indicator to scale up/down
            indicator = { state, refreshTriggerDistance ->
                SwipeRefreshIndicator(
                    state = state,
                    refreshTriggerDistance = refreshTriggerDistance,
                    scale = true
                )
            }
        ) {
            RunningAppList(state, contentPadding)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(
    scrollBehavior: TopAppBarScrollBehaviorX,
    onBackPressed: () -> Unit,
    toLegacyUi: () -> Unit
) {
    MD3TopAppBar(
        scrollBehavior = scrollBehavior,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = rememberInsetsPaddingValues(
            LocalWindowInsets.current.statusBars,
            applyBottom = false,
        ),
        title = {
            Text(
                stringResource(id = R.string.feature_title_process_manage),
                style = MaterialTheme.typography.headlineLarge,
            )
        },
        smallTitle = {
            Text(
                stringResource(id = R.string.feature_title_process_manage),
                style = MaterialTheme.typography.titleMedium,
            )
        },
        backgroundColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        navigationIcon = {
            IconButton(onClick = {
                onBackPressed()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.module_common_ic_arrow_back_24dp),
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = {
                toLegacyUi()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_aliens_fill),
                    contentDescription = "Clear"
                )
            }
        },
        elevation = 1.dp
    )
}

@Composable
fun MD3TopAppBar(
    title: @Composable () -> Unit,
    smallTitle: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    navigationIcon: @Composable (() -> Unit) = {},
    actions: @Composable RowScope.() -> Unit = {},
    backgroundColor: Color = androidx.compose.material.MaterialTheme.colors.primarySurface,
    elevation: Dp = AppBarDefaults.TopAppBarElevation,
    scrollBehavior: TopAppBarScrollBehaviorX? = null
) {
    androidx.compose.material.Surface(
        color = backgroundColor,
        elevation = elevation,
        modifier = modifier
    ) {
        LargeTopAppBarX(
            title = title,
            smallTitle = smallTitle,
            navigationIcon = navigationIcon,
            actions = actions,
            modifier = Modifier.padding(contentPadding),
            scrollBehaviorX = scrollBehavior
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RunningAppList(state: ProcessManageState, contentPadding: PaddingValues) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surface)
            .fillMaxSize()
    ) {
        item { GroupHeader(false) }
        items(state.runningAppStates) {
            RunningAppItem(it)
        }

        item { GroupHeader(true) }
        items(state.runningAppStatesBg) {
            RunningAppItem(it)
        }
    }
}

@Composable
fun GroupHeader(isTotallyCached: Boolean) {
    Row(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = if (isTotallyCached) {
                painterResource(id = R.drawable.ic_baseline_cached_24)
            } else painterResource(id = R.drawable.ic_baseline_visibility_24),
            contentDescription = "Icon"
        )
        Spacer(modifier = Modifier.size(6.dp))
        Text(
            text = if (isTotallyCached) stringResource(id = R.string.running_process_background)
            else stringResource(id = R.string.running_process_running),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RunningAppItem(appState: RunningAppState) {
    Box(
        modifier = Modifier
            .clickable(interactionSource = remember { MutableInteractionSource() },
                // You can also change the color and radius of the ripple
                indication = rememberRipple(bounded = true),
                onClick = {})
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
                AppIcon(modifier = Modifier.size(38.dp), appState.appInfo)
                Spacer(modifier = Modifier.size(12.dp))
                Column(verticalArrangement = Arrangement.Center) {
                    AppLabelText(appState)
                    if (appState.serviceCount == 0) {
                        PText(appState)
                    } else {
                        PSText(appState)
                    }
                }
            }
            MemSizeBadge(appState)
        }
    }
}

@Composable
fun MemSizeBadge(appState: RunningAppState) {
    MD3Badge(appState.sizeStr)
}

@Composable
private fun PSText(appState: RunningAppState) {
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
private fun PText(appState: RunningAppState) {
    Text(
        text = stringResource(
            id = R.string.running_processes_item_description_p,
            appState.processState.size
        ),
        fontSize = 12.sp
    )
}

@Composable
private fun AppLabelText(appState: RunningAppState) {
    AutoResizeText(
        text = appState.appInfo.appLabel,
        maxLines = 1,
        modifier = Modifier.sizeIn(maxWidth = 220.dp),
        fontSizeRange = FontSizeRange(
            min = 12.sp,
            max = 16.sp,
        ),
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.titleMedium,
    )
}

