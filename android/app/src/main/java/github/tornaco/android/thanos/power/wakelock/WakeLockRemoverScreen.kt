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

package github.tornaco.android.thanos.power.wakelock

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.outlined.FileCopy
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.module.compose.common.loader.AppSetFilterItem
import github.tornaco.android.thanos.module.compose.common.requireActivity
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.*

@Composable
fun WakeLockRemoverScreen(onBackPressed: () -> Unit) {
    val context = LocalContext.current
    val viewModel =
        hiltViewModel<WakeLockRemoverViewModel>(LocalContext.current.requireActivity())
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    viewModel.bindLifecycle(lifecycle)
    val state by viewModel.state.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.init()
    }

    ThanoxSmallAppBarScaffold(
        title = {
            Text(
                stringResource(id = R.string.feature_title_wakelock_remover),
                style = TypographyDefaults.appBarTitleTextStyle()
            )
        },
        actions = {
            Row(
                modifier = Modifier,
                verticalAlignment = CenterVertically
            ) {

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
            WakeLockList(contentPadding, state) {
                viewModel.onFilterItemSelected(it)
            }
        }
    }
}

@Composable
private fun WakeLockList(
    contentPadding: PaddingValues,
    state: RemoverState,
    onFilterItemSelected: (AppSetFilterItem) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = contentPadding) {

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                AppFilterDropDown(
                    state,
                    onFilterItemSelected
                )
                Spacer(modifier = Modifier.size(16.dp))
            }
        }

        items(state.packageStates) { item ->
            ExpandableContainer(
                defaultState = ExpandableState.Collapsed,
                mainContent = {
                    AppInfoItem(item)
                },
                expandContent = {
                    WakeLockListItem(item)
                })
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun AppInfoItem(
    packageState: PackageState,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .heightIn(min = 72.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f, fill = false)
                .padding(end = 12.dp),
            verticalAlignment = CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            AppIcon(modifier = Modifier.size(38.dp), packageState.appInfo)
            Spacer(modifier = Modifier.size(12.dp))

            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = packageState.appInfo.appLabel,
                    fontSize = 16.sp
                )
                SmallSpacer()
                Text(
                    text = "${packageState.wakeLocks.size}",
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun WakeLockListItem(packageState: PackageState) {
    Column(modifier = Modifier.fillMaxWidth()) {
        packageState.wakeLocks.forEach { wakeLock ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickableWithRipple {

                    }
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .heightIn(min = 72.dp),
                verticalAlignment = CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f, fill = false),
                    verticalAlignment = CenterVertically
                ) {
                    Icon(
                        modifier = Modifier,
                        imageVector = Icons.Outlined.FileCopy, contentDescription = null
                    )
                    Spacer(modifier = Modifier.size(12.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f, fill = false),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = wakeLock.tag,
                            fontSize = 14.sp
                        )
                    }

                    if (wakeLock.isHeld) {
                        MD3Badge(
                            text = stringResource(id = R.string.wakelock_state_holding)
                        )
                    }
                }

                Checkbox(checked = false, onCheckedChange = {})
            }
        }
    }
}

@Composable
private fun AppFilterDropDown(
    state: RemoverState,
    onFilterItemSelected: (AppSetFilterItem) -> Unit
) {
    FilterDropDown(
        icon = Icons.Filled.FilterAlt,
        selectedItem = state.selectedAppSetFilterItem,
        allItems = state.appFilterItems,
        onItemSelected = onFilterItemSelected
    )
}