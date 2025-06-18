@file:OptIn(ExperimentalMaterialApi::class)

package github.tornaco.android.thanos.module.compose.common.infra

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AppBarRow
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.module.compose.common.infra.sort.AppSortTools
import github.tornaco.android.thanos.module.compose.common.loader.AppSetFilterItem
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.AppIcon
import github.tornaco.android.thanos.module.compose.common.widget.AppLabelText
import github.tornaco.android.thanos.module.compose.common.widget.FilterDropDown
import github.tornaco.android.thanos.module.compose.common.widget.MD3Badge
import github.tornaco.android.thanos.module.compose.common.widget.Md3ExpPullRefreshIndicator
import github.tornaco.android.thanos.module.compose.common.widget.MenuDialog
import github.tornaco.android.thanos.module.compose.common.widget.MenuDialogItem
import github.tornaco.android.thanos.module.compose.common.widget.MenuDialogState
import github.tornaco.android.thanos.module.compose.common.widget.SortToolDropdown
import github.tornaco.android.thanos.module.compose.common.widget.StandardSpacer
import github.tornaco.android.thanos.module.compose.common.widget.SwitchBar
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxMediumAppBarScaffold
import github.tornaco.android.thanos.module.compose.common.widget.rememberMenuDialogState
import github.tornaco.android.thanos.module.compose.common.widget.rememberSearchBarState
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun BaseAppListFilterActivity.BaseAppListFilterContent(config: BaseAppListFilterContainerConfig) {
    val vm = hiltViewModel<BaseAppListFilterVM>()
    LaunchedEffect(Unit) { vm.installIn(config) }

    val context = LocalContext.current
    val title = remember { config.appBarConfig.title(context) }
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
            val customActions = remember(config.appBarConfig) {
                config.appBarConfig.actions(context)
            }
            val sizeClass =
                androidx.compose.material3.adaptive.currentWindowAdaptiveInfo().windowSizeClass
            // Material guidelines state 3 items max in compact, and 5 items max elsewhere.
            // To test this, try a resizable emulator, or a phone in landscape and portrait orientation.
            val maxItemCount =
                if (sizeClass.minWidthDp >= androidx.window.core.layout.WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) {
                    5
                } else {
                    3
                }
            AppBarRow(maxItemCount = maxItemCount, overflowIndicator = {
                IconButton(onClick = { it.show() }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "More"
                    )
                }
            }) {
                customActions.forEach { ca ->
                    clickableItem(onClick = {
                        ca.onClick()
                    }, icon = {
                        Icon(
                            painter = painterResource(ca.icon),
                            contentDescription = ca.title
                        )
                    }, label = ca.title)
                }

                clickableItem(onClick = {
                    searchBarState.showSearchBar()
                }, icon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search"
                    )
                }, label = "Search")
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
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 270.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = if (config.fabs.isEmpty()) 0.dp else (100 * config.fabs.size).dp)
            ) {
                stickyHeader {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        config.switchBarConfig?.let { sc ->
                            StandardSpacer()
                            var isChecked by remember(sc) { mutableStateOf(sc.isChecked) }
                            SwitchBar(
                                title = sc.title(context, isChecked),
                                isChecked = isChecked,
                                onCheckChange = {
                                    if (sc.onCheckChanged(it)) {
                                        isChecked = it
                                    }
                                },
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }

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
                                allItems = AppSortTools.entries.filter {
                                    if (config.appItemConfig.itemType is AppItemConfig.ItemType.Checkable) {
                                        true
                                    } else {
                                        it != AppSortTools.CheckState
                                    }
                                },
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
                }

                items(uiState.apps) { model ->
                    when (val itemType = config.appItemConfig.itemType) {
                        is AppItemConfig.ItemType.Checkable -> {
                            val onAppItemCheckChange: (AppUiModel, Boolean) -> Unit =
                                { app, check ->
                                    vm.updateAppCheckState(app, check)
                                    itemType.onCheckChanged(app, check)
                                }
                            AppListItem(
                                appInfo = model.appInfo,
                                description1 = model.description?.let {
                                    { Text(it, fontSize = 12.sp, lineHeight = 12.5.sp) }
                                },
                                description2 = null,
                                badges = model.badges,
                                onClick = {
                                    onAppItemCheckChange(model, !model.isChecked)
                                },
                                actions = {
                                    Switch(checked = model.isChecked, onCheckedChange = {
                                        onAppItemCheckChange(model, it)
                                    })
                                }
                            )
                        }

                        is AppItemConfig.ItemType.Plain -> {
                            AppListItem(
                                appInfo = model.appInfo,
                                description1 = model.description?.let {
                                    { Text(it, fontSize = 12.sp, lineHeight = 12.5.sp) }
                                },
                                description2 = null,
                                badges = model.badges,
                                onClick = { itemType.onAppClick(model) }
                            )
                        }

                        is AppItemConfig.ItemType.OptionSelectable -> {
                            val optionDialog = optionMenuDialog(itemType) {
                                itemType.onSelected(model, it)
                                vm.updateAppOptionState(model, it)
                            }
                            val selectedOption =
                                itemType.options.firstOrNull { it.id == model.selectedOptionId }
                            AppListItem(
                                appInfo = model.appInfo,
                                description1 = model.description?.let {
                                    { Text(it, fontSize = 12.sp, lineHeight = 12.5.sp) }
                                },
                                description2 = selectedOption?.let {
                                    {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                painter = painterResource(it.iconRes),
                                                tint = it.iconTintColor,
                                                contentDescription = null
                                            )
                                            Text(
                                                it.title(LocalContext.current),
                                                fontSize = 12.sp,
                                                lineHeight = 12.5.sp
                                            )
                                        }
                                    }
                                },
                                badges = model.badges,
                                onClick = {
                                    optionDialog.show()
                                }
                            )
                        }
                    }
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
private fun AppListItem(
    appInfo: AppInfo,
    onClick: () -> Unit,
    badges: List<String>,
    description1: @Composable (() -> Unit)?,
    description2: @Composable (() -> Unit)?,
    actions: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
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
            AppIcon(modifier = Modifier.size(38.dp), appInfo)
            Spacer(modifier = Modifier.size(12.dp))
            Column(verticalArrangement = Arrangement.Center) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AppLabelText(
                        Modifier.sizeIn(maxWidth = 240.dp),
                        appInfo.appLabel
                    )
                }
                description1?.invoke()
                description2?.invoke()
            }
        }

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            badges.forEach {
                MD3Badge(text = it, modifier = Modifier.padding(start = 6.dp))
            }
            if (actions != null) {
                StandardSpacer()
                actions()
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

@Composable
private fun optionMenuDialog(
    selectable: AppItemConfig.ItemType.OptionSelectable,
    onSelected: (String) -> Unit
): MenuDialogState<Unit> {
    val context = LocalContext.current
    val state = rememberMenuDialogState<Unit>(
        title = "",
        menuItems = selectable.options.map {
            MenuDialogItem(
                id = it.id,
                title = it.title(context),
                summary = it.summary
            )
        },
        onItemSelected = { _, id ->
            onSelected(id)
        }
    )
    MenuDialog(state = state)
    return state
}