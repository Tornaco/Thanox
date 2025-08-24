@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3ExpressiveApi::class)

package github.tornaco.android.thanos.module.compose.common.infra

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AppBarRow
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalFloatingToolbar
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.module.compose.common.loader.AppSetFilterItem
import github.tornaco.android.thanos.module.compose.common.theme.ColorDefaults
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.AppIcon
import github.tornaco.android.thanos.module.compose.common.widget.AppLabelText
import github.tornaco.android.thanos.module.compose.common.widget.FilterDropDown
import github.tornaco.android.thanos.module.compose.common.widget.MD3Badge
import github.tornaco.android.thanos.module.compose.common.widget.Md3ExpPullRefreshIndicator
import github.tornaco.android.thanos.module.compose.common.widget.MenuDialog
import github.tornaco.android.thanos.module.compose.common.widget.MenuDialogItem
import github.tornaco.android.thanos.module.compose.common.widget.MenuDialogState
import github.tornaco.android.thanos.module.compose.common.widget.ProgressDialog
import github.tornaco.android.thanos.module.compose.common.widget.SmallSpacer
import github.tornaco.android.thanos.module.compose.common.widget.SortToolDropdown
import github.tornaco.android.thanos.module.compose.common.widget.StandardSpacer
import github.tornaco.android.thanos.module.compose.common.widget.SwitchBar
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxMediumAppBarScaffold
import github.tornaco.android.thanos.module.compose.common.widget.rememberMenuDialogState
import github.tornaco.android.thanos.module.compose.common.widget.rememberProgressDialogState
import github.tornaco.android.thanos.module.compose.common.widget.rememberSearchBarState
import github.tornaco.android.thanos.res.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                contentPadding = PaddingValues(
                    bottom = if (config.fabs.isEmpty() && !uiState.isInSelectionMode) {
                        0.dp
                    } else {
                        200.dp
                    }
                )
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
                                tip = config.featureDescription(LocalContext.current),
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
                            val isCompatMode =
                                config.appItemConfig.itemType is AppItemConfig.ItemType.OptionSelectable
                            AppFilterDropDown(
                                state = uiState,
                                onFilterItemSelected = { vm.onFilterItemSelected(it) },
                                isCompatMode = isCompatMode
                            )
                            Spacer(modifier = Modifier.size(if (isCompatMode) 8.dp else 16.dp))

                            SortToolDropdown(
                                selectedItem = uiState.appSort,
                                allItems = uiState.sortToolItems,
                                isReverse = uiState.sortReverse,
                                setReverse = {
                                    vm.updateSortReverse(it)
                                },
                                onItemSelected = {
                                    vm.updateSort(it)
                                },
                                isCompatMode = isCompatMode
                            )
                            Spacer(modifier = Modifier.size(if (isCompatMode) 8.dp else 16.dp))

                            if (uiState.optionsFilterItems.isNotEmpty()) {
                                OptionsFilterDropDown(
                                    state = uiState,
                                    onFilterItemSelected = { vm.onOptionFilterItemSelected(it) },
                                    isCompatMode = isCompatMode
                                )
                            }
                        }
                    }
                }

                items(uiState.apps) { modelItem ->
                    val model by rememberUpdatedState(modelItem)
                    when (val itemType = config.appItemConfig.itemType) {
                        is AppItemConfig.ItemType.Checkable -> {
                            val onAppItemCheckChange: (AppUiModel, Boolean) -> Unit =
                                { app, check ->
                                    vm.updateAppCheckState(app, check)
                                    itemType.onCheckChanged(app, check)
                                }
                            val isSelected = uiState.selectedAppItems.contains(model)
                            AppListItem(
                                appInfo = model.appInfo,
                                isSelected = isSelected,
                                isRunning = model.isRunning,
                                isIdle = model.isIdle,
                                isPlayingSound = model.isPlayingSound,
                                description1 = model.description?.let {
                                    { Text(it, fontSize = 12.sp, lineHeight = 12.5.sp) }
                                },
                                description2 = null,
                                badges = model.badges,
                                onClick = {
                                    if (uiState.isInSelectionMode) {
                                        vm.selectApp(!isSelected, model)
                                    } else {
                                        onAppItemCheckChange(model, !model.isChecked)
                                    }
                                },
                                onLongClick = {
                                    vm.selectApp(!isSelected, model)
                                },
                                actions = {
                                    Switch(checked = model.isChecked, onCheckedChange = {
                                        onAppItemCheckChange(model, it)
                                    })
                                }
                            )
                        }

                        is AppItemConfig.ItemType.Plain -> {
                            val isSelected = uiState.selectedAppItems.contains(model)
                            AppListItem(
                                appInfo = model.appInfo,
                                isSelected = isSelected,
                                isRunning = model.isRunning,
                                isIdle = model.isIdle,
                                isPlayingSound = model.isPlayingSound,
                                description1 = model.description?.let {
                                    { Text(it, fontSize = 12.sp, lineHeight = 12.5.sp) }
                                },
                                description2 = null,
                                badges = model.badges,
                                onClick = {
                                    if (uiState.isInSelectionMode) {
                                        vm.selectApp(!isSelected, model)
                                    } else {
                                        itemType.onAppClick(model)
                                    }
                                },
                                onLongClick = {
                                    vm.selectApp(!isSelected, model)
                                },
                            )
                        }

                        is AppItemConfig.ItemType.OptionSelectable -> {
                            val optionDialog = optionMenuDialog(model, itemType) {
                                itemType.onSelected(model, it)
                                vm.updateAppOptionState(model, it)
                            }
                            val selectedOption =
                                itemType.options.firstOrNull { it.id == model.selectedOptionId }
                            val isSelected = uiState.selectedAppItems.contains(model)
                            AppListItem(
                                appInfo = model.appInfo,
                                isSelected = isSelected,
                                isRunning = model.isRunning,
                                isIdle = model.isIdle,
                                isPlayingSound = model.isPlayingSound,
                                description1 = model.description?.let {
                                    { Text(it, fontSize = 12.sp, lineHeight = 12.5.sp) }
                                },
                                description2 = selectedOption?.takeIf { it.showOnAppListItem }
                                    ?.let {
                                        {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    painter = painterResource(it.iconRes),
                                                    tint = it.iconTintColor,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                SmallSpacer()
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
                                    if (uiState.isInSelectionMode) {
                                        vm.selectApp(!isSelected, model)
                                    } else {
                                        optionDialog.show()
                                    }
                                },
                                onLongClick = {
                                    vm.selectApp(!isSelected, model)
                                },
                            )
                        }
                    }
                }

                item {
                    config.footContent()
                }
            }
            Md3ExpPullRefreshIndicator(
                uiState.isLoading,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter)
            )

            if (config.batchOperationConfig != null) {
                SelectionModeToolbar(
                    config = config.batchOperationConfig,
                    uiState = uiState,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    opApplied = {
                        vm.enterSelectionMode(false)
                        vm.refresh("batchOperationConfig.opApplied")
                    },
                    selectAll = {
                        vm.selectAll()
                    },
                    unselectAll = {
                        vm.unSelectAll()
                    }
                )
                BackHandler(uiState.isInSelectionMode) {
                    vm.enterSelectionMode(false)
                }
            }
        }
    }
}

@Composable
private fun SelectionModeToolbar(
    config: BatchOperationConfig,
    uiState: AppListUiState,
    modifier: Modifier,
    selectAll: () -> Unit,
    unselectAll: () -> Unit,
    opApplied: () -> Unit,
) {
    AnimatedVisibility(modifier = modifier, visible = uiState.isInSelectionMode) {
        HorizontalFloatingToolbar(
            modifier = Modifier,
            expanded = true,
            expandedShadowElevation = 0.dp,
            leadingContent = {
                Badge {
                    Text("${uiState.selectedAppItems.size}")
                }
            },
            trailingContent = { },
            content = {
                FlowRow {
                    IconButton(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        onClick = {
                            selectAll()
                        }
                    ) {
                        Icon(Icons.Filled.SelectAll, contentDescription = null)
                    }
                    IconButton(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        onClick = {
                            unselectAll()
                        }
                    ) {
                        Icon(Icons.Filled.ClearAll, contentDescription = null)
                    }
                    val scope = rememberCoroutineScope()
                    config.operations.forEach {
                        val progressDialogState =
                            rememberProgressDialogState(
                                stringResource(R.string.title_batch_tools),
                                it.title(LocalContext.current)
                            )
                        ProgressDialog(progressDialogState)
                        Button(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            onClick = {
                                scope.launch {
                                    progressDialogState.show()
                                    withContext(Dispatchers.IO) {
                                        it.onClick(uiState.selectedAppItems)
                                    }
                                    opApplied()
                                    progressDialogState.dismiss()
                                }
                            }
                        ) {
                            Text(it.title(LocalContext.current))
                        }
                    }
                }
            }
        )
    }
}

@Composable
private fun AppListItem(
    appInfo: AppInfo,
    isSelected: Boolean,
    isRunning: Boolean,
    isIdle: Boolean,
    isPlayingSound: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    badges: List<String>,
    description1: @Composable (() -> Unit)?,
    description2: @Composable (() -> Unit)?,
    actions: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isSelected) ColorDefaults.backgroundSurfaceColor(6.dp) else Color.Unspecified)
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
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

            if (isRunning) {
                CircularWavyProgressIndicator(
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .size(18.dp)
                )
            }
            if (isIdle) {
                Icon(
                    painter = painterResource(github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_zzz_fill),
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .size(18.dp)
                )
            }
            if (isPlayingSound) {
                Icon(
                    painter = painterResource(github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_netease_cloud_music_fill),
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .size(18.dp)
                )
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
    onFilterItemSelected: (AppSetFilterItem) -> Unit,
    isCompatMode: Boolean
) {
    FilterDropDown(
        selectedItem = state.selectedAppSetFilterItem,
        allItems = state.appFilterItems,
        onItemSelected = onFilterItemSelected,
        isCompatMode = isCompatMode
    )
}


@Composable
private fun OptionsFilterDropDown(
    state: AppListUiState,
    onFilterItemSelected: (AppSetFilterItem) -> Unit,
    isCompatMode: Boolean
) {
    FilterDropDown(
        selectedItem = state.selectedOptionFilterItem,
        allItems = state.optionsFilterItems,
        onItemSelected = onFilterItemSelected,
        isCompatMode = isCompatMode
    )
}

@Composable
private fun optionMenuDialog(
    model: AppUiModel,
    selectable: AppItemConfig.ItemType.OptionSelectable,
    onSelected: (String) -> Unit
): MenuDialogState<Unit> {
    val context = LocalContext.current
    val state = rememberMenuDialogState<Unit>(
        key1 = selectable,
        key2 = model,
        title = { model.appInfo.appLabel },
        menuItems = selectable.options.map { op ->
            MenuDialogItem(
                id = op.id,
                title = op.title(context),
                summary = op.summary,
                action = op.action?.let {
                    it.first to {
                        it.second(model)
                    }
                }
            )
        },
        onItemSelected = { _, id ->
            onSelected(id)
        }
    )
    MenuDialog(state = state)
    return state
}