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
package github.tornaco.android.thanos.settings.access

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ClearAll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.core.util.ClipboardUtils
import github.tornaco.android.thanos.module.compose.common.widget.SmallSpacer
import github.tornaco.android.thanos.module.compose.common.widget.TinySpacer
import github.tornaco.android.thanos.module.compose.common.widget.clickableWithRipple
import github.tornaco.android.thanos.module.compose.common.requireActivity
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults.appBarTitleTextStyle
import github.tornaco.android.thanos.module.compose.common.widget.AppIcon
import github.tornaco.android.thanos.module.compose.common.widget.MD3Badge
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxSmallAppBarScaffold

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun SettingsAccessRecordViewerScreen(
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    val viewModel =
        hiltViewModel<SettingsAccessRecordViewerViewModel>(LocalContext.current.requireActivity())
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    viewModel.bindLifecycle(lifecycle)
    val state by viewModel.state.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.init()
    }

    val listState = rememberLazyListState()

    ThanoxSmallAppBarScaffold(
        title = {
            Text(
                stringResource(id = R.string.feature_title_settings_access_record),
                style = appBarTitleTextStyle()
            )
        },
        actions = {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    viewModel.clearAllRecords()
                }) {
                    Icon(
                        imageVector = Icons.Outlined.ClearAll,
                        contentDescription = "Clear"
                    )
                }
            }
        },
        onBackPressed = onBackPressed
    ) { contentPadding ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(state.isLoading),
            onRefresh = { viewModel.refresh() },
            indicatorPadding = contentPadding,
            clipIndicatorToPadding = false,
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
            RecordList(
                modifier = Modifier.padding(contentPadding),
                lazyListState = listState,
                state = state,
                itemSelectStateChanged = { item, selected ->
                    viewModel.onFilterItemSelected(item, selected)
                },
                onRecordItemClick = {
                    ClipboardUtils.copyToClipboard(
                        context,
                        "settings",
                        "${it.record.name} ${it.record.value}"
                    )
                    Toast.makeText(
                        context,
                        R.string.common_toast_copied_to_clipboard,
                        Toast.LENGTH_SHORT
                    ).show()
                })
        }
    }
}

@Composable
private fun AppFilterDropDown(
    state: ViewerState,
    itemSelectStateChanged: (SelectableFilterItem, Boolean) -> Unit
) {

    FlowRow(
        mainAxisSpacing = 8.dp,
        crossAxisSpacing = 8.dp,
        mainAxisAlignment = MainAxisAlignment.Start,
        crossAxisAlignment = FlowCrossAxisAlignment.Center
    ) {
        state.appFilterItems.forEach { item ->
            FilledTonalButton(
                onClick = { itemSelectStateChanged(item, !item.isSelected) },
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = if (item.isSelected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        Color.LightGray
                    }
                )
            ) {
                Text(
                    text = item.filterItem.label,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RecordList(
    modifier: Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    state: ViewerState,
    itemSelectStateChanged: (SelectableFilterItem, Boolean) -> Unit,
    onRecordItemClick: (DetailedSettingsAccessRecord) -> Unit
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
                AppFilterDropDown(state, itemSelectStateChanged)
                Spacer(modifier = Modifier.size(16.dp))
            }
        }

        if (state.mergedRecords.isNotEmpty()) {
            items(state.mergedRecords) {
                RecordItem(it, onRecordItemClick)
                TinySpacer()
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RecordItem(
    record: DetailedSettingsAccessRecord,
    onRecordItemClick: (DetailedSettingsAccessRecord) -> Unit
) {
    Box(
        modifier = Modifier
            .clickableWithRipple {
                onRecordItemClick(record)
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
                modifier = Modifier
                    .weight(1f, fill = false)
                    .padding(end = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                AppIcon(modifier = Modifier.size(38.dp), record.appInfo)
                Spacer(modifier = Modifier.size(12.dp))

                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        text = record.record.name,
                        fontSize = 14.sp
                    )
                    SmallSpacer()
                    Text(
                        text = record.record.value,
                        fontSize = 14.sp
                    )
                    SmallSpacer()
                    Text(
                        text = record.appInfo.appLabel,
                        fontSize = 12.sp
                    )
                    SmallSpacer()
                    Text(
                        text = record.timeStr,
                        fontSize = 12.sp
                    )
                }
            }

            MD3Badge(
                text = if (record.isRead) "Read" else "Write",
                containerColor = if (record.isRead) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.secondaryContainer
                },
                textSize = 12.sp
            )
        }
    }
}