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

package github.tornaco.thanos.android.module.profile

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.WrapText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dagger.hilt.android.AndroidEntryPoint
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import github.tornaco.android.thanos.module.compose.common.StandardSpacer
import github.tornaco.android.thanos.module.compose.common.clickableWithRipple
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxSmallAppBarScaffold
import github.tornaco.android.thanos.util.ActivityUtils
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LogActivity : ComposeThemeActivity() {
    object Starter {
        fun start(context: Context?) {
            ActivityUtils.startActivity(context, LogActivity::class.java)
        }
    }

    @Composable
    override fun Content() {
        val viewModel = hiltViewModel<LogViewModel>()
        val state by viewModel.state.collectAsState()

        LaunchedEffect(viewModel) {
            viewModel.refresh()
        }

        val listState = rememberLazyListState()
        val scope = rememberCoroutineScope()

        ThanoxSmallAppBarScaffold(title = {
            Text(
                text = "Logs",
                style = TypographyDefaults.appBarTitleTextStyle()
            )
        },
            onBackPressed = { finish() },
            actions = {
                MenuActions(
                    clearLogs = { viewModel.clearLogs() },
                    scrollToTop = {
                        scope.launch {
                            listState.scrollToItem(0)
                        }
                    },
                    scrollToBottom = {
                        scope.launch {
                            listState.scrollToItem(state.logs.size - 1)
                        }
                    },
                    toggleHorizontalScroll = {
                        viewModel.setHorizontalScrollEnabled(!state.horizontalScrollEnabled)
                    })
            }) { contentPadding ->
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .clip(
                                RoundedCornerShape(32.dp)
                            )
                            .background(color = MaterialTheme.colorScheme.surface)
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                            .clickableWithRipple {
                                viewModel.enableLog(!state.isLogEnabled)
                            }
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .align(Alignment.CenterStart),
                            text = if (state.isLogEnabled) stringResource(id = R.string.switch_on_text) else stringResource(
                                id = R.string.switch_off_text
                            )
                        )
                        Switch(
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .align(Alignment.CenterEnd),
                            checked = state.isLogEnabled,
                            onCheckedChange = {
                                viewModel.enableLog(it)
                            })
                    }

                    StandardSpacer()

                    LogList(
                        horizontalScrollEnabled = state.horizontalScrollEnabled,
                        listState = listState,
                        state = state
                    )
                }
            }

        }
    }

    @Composable
    private fun LogList(
        horizontalScrollEnabled: Boolean,
        listState: LazyListState,
        state: LogState
    ) {
        var modifier = Modifier
            .fillMaxSize()
        if (horizontalScrollEnabled) {
            modifier = modifier.horizontalScroll(state = rememberScrollState())
        }
        LazyColumn(
            modifier = modifier,
            state = listState,
            contentPadding = PaddingValues(16.dp)
        ) {
            items(state.logs) { item ->
                Text(text = item, style = MaterialTheme.typography.bodySmall)
            }
        }
    }

    @Composable
    private fun MenuActions(
        clearLogs: () -> Unit,
        scrollToTop: () -> Unit,
        scrollToBottom: () -> Unit,
        toggleHorizontalScroll: () -> Unit
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                clearLogs()
            }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Clear logs"
                )
            }
            IconButton(onClick = {
                toggleHorizontalScroll()
            }) {
                Icon(
                    imageVector = Icons.Filled.WrapText,
                    contentDescription = "Wrap text"
                )
            }
            IconButton(onClick = {
                scrollToTop()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowCircleUp,
                    contentDescription = "To top"
                )
            }
            IconButton(onClick = {
                scrollToBottom()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowCircleDown,
                    contentDescription = "To bottom"
                )
            }
        }
    }
}