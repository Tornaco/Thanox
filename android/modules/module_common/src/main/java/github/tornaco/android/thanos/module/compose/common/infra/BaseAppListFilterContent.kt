@file:OptIn(ExperimentalMaterialApi::class)

package github.tornaco.android.thanos.module.compose.common.infra

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import github.tornaco.android.thanos.module.compose.common.infra.sort.AppSortTools
import github.tornaco.android.thanos.module.compose.common.loader.AppSetFilterItem
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.AppIcon
import github.tornaco.android.thanos.module.compose.common.widget.AppLabelText
import github.tornaco.android.thanos.module.compose.common.widget.FilterDropDown
import github.tornaco.android.thanos.module.compose.common.widget.MD3Badge
import github.tornaco.android.thanos.module.compose.common.widget.Md3ExpPullRefreshIndicator
import github.tornaco.android.thanos.module.compose.common.widget.SortToolDropdown
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxMediumAppBarScaffold
import github.tornaco.android.thanos.module.compose.common.widget.rememberSearchBarState
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun BaseAppListFilterActivity.BaseAppListFilterContent(config: BaseAppListFilterContainerConfig) {
    val vm = hiltViewModel<BaseAppListFilterVM>()
    LaunchedEffect(Unit) { vm.installIn(config) }

    val context = LocalContext.current
    val title = remember { config.title(context) }
    val searchBarState = rememberSearchBarState()
    LaunchedEffect(searchBarState) {
        snapshotFlow { searchBarState.keyword }
            .distinctUntilChanged()
            .collect {
                vm.keywordChanged(it)
            }
    }
    BackHandler(searchBarState.showSearchBar) {
        searchBarState.closeSearchBar()
    }
    ThanoxMediumAppBarScaffold(
        title = {
            Text(
                text = title,
                style = TypographyDefaults.appBarTitleTextStyle()
            )
        },
        searchBarState = searchBarState,
        onBackPressed = {
            finish()
        },
        actions = {
            IconButton(onClick = {
                searchBarState.showSearchBar()
            }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search"
                )
            }
        },
        floatingActionButton = {
            config.fabs.forEach {
                ExtendedFloatingActionButton(
                    onClick = {
                        it.onClick()
                    }
                ) {
                    Text(it.title(context))
                }
            }
        }
    ) { paddings ->
        val uiState by vm.state.collectAsState()
        val pullRefreshState =
            rememberPullRefreshState(uiState.isLoading, { vm.refresh("pullRefreshState") })
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings)
                .pullRefresh(pullRefreshState),
        ) {
            LazyColumn(
                Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = if (config.fabs.isEmpty()) 0.dp else (100 * config.fabs.size).dp)
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        AppFilterDropDown(
                            state = uiState,
                            onFilterItemSelected = { vm.onFilterItemSelected(it) }
                        )
                        Spacer(modifier = Modifier.size(16.dp))

                        SortToolDropdown(
                            selectedItem = uiState.appSort,
                            allItems = AppSortTools.entries,
                            isReverse = uiState.sortReverse,
                            setReverse = {
                                vm.updateSortReverse(it)
                            },
                            onItemSelected = {
                                vm.updateSort(it)
                            }
                        )
                    }
                }
                items(uiState.apps) { model ->
                    AppListItem(model, onClick = { config.onAppClick(it) })
                }
            }
            Md3ExpPullRefreshIndicator(
                uiState.isLoading,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
private fun AppListItem(model: AppUiModel, onClick: (AppUiModel) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(model)
            }
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .heightIn(min = 72.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f, fill = false),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            AppIcon(modifier = Modifier.size(38.dp), model.appInfo)
            Spacer(modifier = Modifier.size(12.dp))
            Column(verticalArrangement = Arrangement.Center) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AppLabelText(
                        Modifier.sizeIn(maxWidth = 240.dp),
                        model.appInfo.appLabel
                    )
                }
                model.description?.let {
                    Text(it, fontSize = 12.sp, lineHeight = 12.5.sp)
                }
            }
        }

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            model.badges.forEach {
                MD3Badge(text = it, modifier = Modifier.padding(start = 6.dp))
            }
        }
    }
}

@Composable
private fun AppFilterDropDown(
    state: AppListUiState,
    onFilterItemSelected: (AppSetFilterItem) -> Unit
) {
    FilterDropDown(
        selectedItem = state.selectedAppSetFilterItem,
        allItems = state.appFilterItems,
        onItemSelected = onFilterItemSelected
    )
}