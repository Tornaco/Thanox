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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.module.compose.common.widget.*

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

    ThanoxScaffold(
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
        onBackPressed = onBackPressed
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
                    scale = true,
                    arrowEnabled = false,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            }
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                AppFilterDropDown()
                RunningAppList(state, contentPadding)
            }
        }
    }
}

@Composable
fun AppFilterDropDown() {
    FilterDropDown(
        allItems = listOf(
            AppSetFilterItem("System", false),
            AppSetFilterItem("Installed", true),
            AppSetFilterItem("Media provider", false),
        )
    )
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
        item { GroupHeader(false, state.runningAppStates.size) }
        items(state.runningAppStates) {
            RunningAppItem(it)
        }

        item {
            Spacer(
                modifier = Modifier
                    .size(24.dp)
            )
        }

        item { GroupHeader(true, state.runningAppStatesBg.size) }
        items(state.runningAppStatesBg) {
            RunningAppItem(it)
        }
    }
}

@Composable
fun GroupHeader(isTotallyCached: Boolean, itemCount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val text = if (isTotallyCached) stringResource(id = R.string.running_process_background)
        else stringResource(id = R.string.running_process_running)
        TextButton(onClick = {}) {
            Icon(
                if (isTotallyCached) Icons.Filled.Cached else Icons.Filled.Visibility,
                contentDescription = "isTotallyCached",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(
                text = "$text $itemCount",
                style = MaterialTheme.typography.titleMedium
            )
        }
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

