@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class,
    ExperimentalLayoutApi::class,
    FlowPreview::class
)

package now.fortuitous.thanos.sf

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.core.pm.PREBUILT_PACKAGE_SET_ID_ALL
import github.tornaco.android.thanos.core.pm.Pkg
import github.tornaco.android.thanos.core.pm.PrebuiltPkgSets.isPrebuiltId
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import github.tornaco.android.thanos.module.compose.common.widget.AppIcon
import github.tornaco.android.thanos.module.compose.common.widget.CommonSortDialog
import github.tornaco.android.thanos.module.compose.common.widget.DropdownPopUpMenu
import github.tornaco.android.thanos.module.compose.common.widget.MenuItem
import github.tornaco.android.thanos.module.compose.common.widget.SortItem
import github.tornaco.android.thanos.module.compose.common.widget.TextInputDialog
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxBottomSheetScaffold
import github.tornaco.android.thanos.module.compose.common.widget.TinySpacer
import github.tornaco.android.thanos.module.compose.common.widget.rememberCommonSortState
import github.tornaco.android.thanos.module.compose.common.widget.rememberTextInputState
import github.tornaco.android.thanos.module.compose.common.widget.thenIf
import github.tornaco.android.thanos.picker.AppPickerActivity
import github.tornaco.android.thanos.support.subscribe.LVLStateHolder
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState

@AndroidEntryPoint
class SFActivity : ComposeThemeActivity() {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, SFActivity::class.java)
            context.startActivity(starter)
        }
    }

    @Composable
    override fun Content() {
        SFContent { finish() }
    }

}

@Composable
fun SFContent(back: () -> Unit) {
    val subState by LVLStateHolder.collectAsState()
    if (subState.isInited) require(subState.isSubscribed)

    val context = LocalContext.current
    val sfVM = hiltViewModel<SFVM>()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(key1 = sfVM) {
        sfVM.bindLifecycle(lifecycle)
    }

    val sfPkgs by sfVM.sfPkgs.collectAsState()
    val sfPkgMapping by sfVM.sfPkgMapping.collectAsState()
    val state by sfVM.state.collectAsState()

    val pickPkgLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val list =
                    result.data?.getParcelableArrayListExtra<AppInfo>("apps")
                list?.let { sfVM.addPkgs(it) }
            }
        }
    )

    val searchBarState =
        github.tornaco.android.thanos.module.compose.common.widget.rememberSearchBarState()
    LaunchedEffect(searchBarState) {
        snapshotFlow { searchBarState.keyword }
            .distinctUntilChanged()
            .collect {
                sfVM.search(it)
            }
    }

    BackHandler(searchBarState.showSearchBar) {
        searchBarState.closeSearchBar()
    }

    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    ThanoxBottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            SFSettings(sfVM)
        },
        title = {
            if (state.isEditingMode) {
                Text(
                    text = stringResource(
                        id = github.tornaco.android.thanos.res.R.string.apps_count,
                        state.selectedApps.size.toString()
                    )
                )
            } else {
            }
        },
        onBackPressed = {
            if (state.isEditingMode) {
                sfVM.finishEdit()
            } else back()
        },
        searchBarState = searchBarState,
        actions = {
            AnimatedVisibility(visible = !state.isEditingMode) {
                Row {
                    IconButton(onClick = {
                        scope.launch {
                            scaffoldState.bottomSheetState.expand()
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_settings_2_line),
                            contentDescription = "Settings"
                        )
                    }
                    IconButton(onClick = {
                        searchBarState.showSearchBar()
                        sfVM.finishEdit()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search"
                        )
                    }
                    IconButton(onClick = {
                        val exclude: ArrayList<Pkg> = ArrayList(sfPkgs.map { Pkg.fromAppInfo(it) })
                        pickPkgLauncher.launch(AppPickerActivity.getIntent(context, exclude))
                    }) {
                        Icon(
                            painter = painterResource(id = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_add_fill),
                            contentDescription = "Add"
                        )
                    }
                }
            }
        }) {
        val refreshing = false
        val refreshState = rememberPullRefreshState(refreshing, onRefresh = {
        })
        Box(
            Modifier
                .fillMaxSize()
                .pullRefresh(refreshState)
        ) {
            BackHandler(state.isEditingMode) {
                sfVM.finishEdit()
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                val pkgSets by sfVM.pkgSets.collectAsState()
                val selectedPkgSet by sfVM.selectedPkgSetId.collectAsState()
                val inputDialogState = rememberTextInputState(
                    title = stringResource(id = github.tornaco.android.thanos.res.R.string.pkg_set),
                    message = null
                ) {
                    sfVM.addPkgSet(it)
                }
                TextInputDialog(state = inputDialogState)

                val pagerState = rememberPagerState {
                    pkgSets.size
                }

                LaunchedEffect(pagerState) {
                    snapshotFlow { pagerState.currentPage }
                        .distinctUntilChanged()
                        .debounce(300)
                        .collect {
                            if (pkgSets.isNotEmpty() && it in pkgSets.indices) {
                                sfVM.selectPkgSet(
                                    pkgSets[it]
                                )
                            }
                        }
                }

                LaunchedEffect(selectedPkgSet, pkgSets) {
                    val selectedIndex = pkgSets.indexOfFirst { it.id == selectedPkgSet }
                    if (selectedIndex != -1 && pagerState.pageCount > 0) {
                        kotlin.runCatching { pagerState.animateScrollToPage(selectedIndex) }
                    }
                }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f, fill = false),
                    beyondViewportPageCount = 1
                ) { pageIndex ->
                    val apps = sfPkgMapping[pkgSets[pageIndex]] ?: emptyList()
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(80.dp),
                        modifier = Modifier
                            .fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 32.dp)
                    ) {
                        items(apps, key = {
                            it.pkgName + it.versionName + it.state + it.userId
                        }) {
                            val isSelected = state.selectedApps.contains(Pkg.fromAppInfo(it))
                            Column(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .thenIf(
                                        Modifier.background(MaterialTheme.colorScheme.secondaryContainer),
                                        isSelected
                                    )
                                    .combinedClickable(
                                        onClick = {
                                            if (state.isEditingMode) {
                                                sfVM.toggleSelectApp(it)
                                            } else {
                                                sfVM.launchApp(
                                                    Pkg.newPkg(it.pkgName, it.userId)
                                                )
                                            }
                                        }, onLongClick = {
                                            if (state.isEditingMode) {
                                                sfVM.finishEdit()
                                            } else {
                                                sfVM.startEdit()
                                                sfVM.toggleSelectApp(it)
                                            }
                                        })
                                    .padding(6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                AppIcon(modifier = Modifier.size(50.dp), appInfo = it)
                                Text(
                                    text = it.appLabel,
                                    fontSize = 12.5.sp,
                                    textAlign = TextAlign.Center,
                                    maxLines = 2
                                )
                            }


                        }
                    }
                }

                AnimatedVisibility(visible = !state.isEditingMode) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f, fill = false)
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val updatedSets by rememberUpdatedState(pkgSets)
                            val sortDialogState = rememberCommonSortState {
                                sfVM.applySort(it)
                            }
                            CommonSortDialog(
                                state = sortDialogState,
                                items = updatedSets.mapIndexed { i, x ->
                                    SortItem(index = i, title = x.label)
                                })

                            updatedSets.forEach { pkgSet ->
                                val updatedSet by rememberUpdatedState(pkgSet)
                                Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                                    val pkgSetMenu = remember {
                                        mutableStateOf(false)
                                    }

                                    val renameDialog = rememberTextInputState(
                                        title = stringResource(id = github.tornaco.android.thanos.res.R.string.rename),
                                        message = null
                                    ) {
                                        sfVM.renamePkgSet(it, updatedSet.id)
                                    }
                                    TextInputDialog(state = renameDialog)

                                    DropdownPopUpMenu(
                                        pkgSetMenu,
                                        items = listOf(
                                            MenuItem(
                                                "delete",
                                                stringResource(id = github.tornaco.android.thanos.res.R.string.delete),
                                                github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_delete_bin_2_line
                                            ),
                                            MenuItem(
                                                "rename",
                                                stringResource(id = github.tornaco.android.thanos.res.R.string.rename),
                                                github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_edit_2_line
                                            ),
                                            MenuItem(
                                                "sort",
                                                stringResource(id = github.tornaco.android.thanos.res.R.string.sort),
                                                github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_sort_asc
                                            )
                                        )
                                    ) {
                                        if (it.id == "delete") {
                                            sfVM.removePkgSet(pkgSet.id)
                                        } else if (it.id == "rename") {
                                            renameDialog.show(pkgSet.label)
                                        } else if (it.id == "sort") {
                                            sortDialogState.show()
                                        }
                                    }

                                    Box(
                                        Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(24.dp))
                                            .background(
                                                color = if (selectedPkgSet == pkgSet.id) {
                                                    MaterialTheme.colorScheme.primaryContainer
                                                } else {
                                                    Color.Unspecified
                                                }
                                            )
                                            .combinedClickable(
                                                onClick = {
                                                    sfVM.selectPkgSet(pkgSet)
                                                }, onLongClick = {
                                                    pkgSetMenu.value = true
                                                })
                                            .padding(12.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (pkgSet.id == PREBUILT_PACKAGE_SET_ID_ALL) {
                                                stringResource(id = github.tornaco.android.thanos.res.R.string.all)
                                            } else {
                                                pkgSet.label
                                            },
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }

                        Row {
                            IconButton(onClick = {
                                inputDialogState.show()
                            }) {
                                Icon(
                                    painter = painterResource(id = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_add_fill),
                                    contentDescription = "Add"
                                )
                            }
                            IconButton(onClick = {
                                sfVM.freeze()
                            }) {
                                Icon(
                                    painter = painterResource(id = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_wifi_line),
                                    contentDescription = "Freeze"
                                )
                            }
                        }
                    }

                }

                AnimatedVisibility(visible = state.isEditingMode) {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                    ) {
                        if (state.selectedApps.size == 1) {
                            Button(onClick = {
                                scope.launch {
                                    // TODO Hao
                                }
                            }) {
                                Text(text = stringResource(id = github.tornaco.android.thanos.res.R.string.create_shortcut))
                            }
                            TinySpacer()
                        }

                        Button(onClick = {
                            sfVM.removeSelectedAppsFromSF()
                        }) {
                            Text(text = stringResource(id = github.tornaco.android.thanos.res.R.string.remove_from_sf))
                        }
                        if (!selectedPkgSet.isPrebuiltId()) {
                            TinySpacer()
                            Button(onClick = {
                                sfVM.removeSelectedAppsFromPkgSet()
                            }) {
                                Text(text = stringResource(id = github.tornaco.android.thanos.res.R.string.remove_from_pkg_set))
                            }
                        }
                        TinySpacer()
                        Box(modifier = Modifier) {
                            val pkgSetMenu = remember {
                                mutableStateOf(false)
                            }
                            DropdownPopUpMenu(
                                pkgSetMenu,
                                items = pkgSets.filterNot { it.isPrebuilt }.map {
                                    MenuItem(
                                        it.id,
                                        it.label,
                                        github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_folder_line
                                    )
                                }
                            ) {
                                sfVM.addSelectedAppsToPkgSet(it.id)
                            }
                            Button(onClick = {
                                pkgSetMenu.value = true
                            }) {
                                Text(text = stringResource(id = github.tornaco.android.thanos.res.R.string.add_to_pkg_set))
                            }
                        }
                        TinySpacer()
                        Button(onClick = {
                            sfVM.tempUnFreezeSelectedApps()
                        }) {
                            Text(text = stringResource(id = github.tornaco.android.thanos.res.R.string.temp_unfreeze))
                        }
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = refreshing,
                state = refreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                contentColor = MaterialTheme.colorScheme.secondary
            )
        }
    }
}