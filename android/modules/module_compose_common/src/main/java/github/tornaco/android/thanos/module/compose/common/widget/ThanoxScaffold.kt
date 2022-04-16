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

package github.tornaco.android.thanos.module.compose.common.widget

import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.FabPosition
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.rememberInsetsPaddingValues
import github.tornaco.android.thanos.module.compose.common.R


@OptIn(ExperimentalMaterial3Api::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
fun ThanoxBottomSheetScaffold(
    title: @Composable () -> Unit,
    actions: @Composable (RowScope.() -> Unit) = {},
    onBackPressed: () -> Unit,
    scaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
    sheetContent: @Composable ColumnScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    val containerColor = MaterialTheme.colorScheme.surfaceVariant
    val contentColor = contentColorFor(containerColor)
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
        BottomSheetScaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            scaffoldState = scaffoldState,
            topBar = {
                ThanoxMediumTopAppBar(scrollBehavior, title, actions, onBackPressed)
            },
            sheetContent = sheetContent,
            sheetPeekHeight = 0.dp,
            content = {
                content(it)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThanoxMediumAppBarScaffold(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    actions: @Composable (RowScope.() -> Unit) = {},
    onBackPressed: () -> Unit,
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    isFloatingActionButtonDocked: Boolean = false,
    content: @Composable (PaddingValues) -> Unit
) {
    val containerColor = MaterialTheme.colorScheme.surfaceVariant
    val contentColor = contentColorFor(containerColor)
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
        com.google.accompanist.insets.ui.Scaffold(
            modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                ThanoxMediumTopAppBar(scrollBehavior, title, actions, onBackPressed)
            },
            floatingActionButton = floatingActionButton,
            floatingActionButtonPosition = floatingActionButtonPosition,
            isFloatingActionButtonDocked = isFloatingActionButtonDocked,
            bottomBar = {
                // We add a spacer as a bottom bar, which is the same height as
                // the navigation bar
                Spacer(
                    Modifier
                        .navigationBarsHeight()
                        .fillMaxWidth()
                )
            }, content = {
                content(it)
            }
        )
    }
}

@Composable
private fun ThanoxMediumTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    title: @Composable () -> Unit,
    actions: @Composable (RowScope.() -> Unit),
    onBackPressed: () -> Unit
) {
    val backgroundColors = TopAppBarDefaults.smallTopAppBarColors()
    val backgroundColor = backgroundColors.containerColor(
        scrollFraction = scrollBehavior.scrollFraction
    ).value
    val foregroundColors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = Color.Transparent,
        scrolledContainerColor = Color.Transparent
    )
    Surface(color = backgroundColor) {
        MediumTopAppBar(
            colors = foregroundColors,
            modifier = Modifier
                .padding(
                    rememberInsetsPaddingValues(
                        LocalWindowInsets.current.statusBars,
                        applyBottom = false,
                    )
                ),
            title = title,
            actions = actions,
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
            scrollBehavior = scrollBehavior
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThanoxSmallAppBarScaffold(
    title: @Composable () -> Unit,
    actions: @Composable (RowScope.() -> Unit) = {},
    onBackPressed: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val containerColor = MaterialTheme.colorScheme.surfaceVariant
    val contentColor = contentColorFor(containerColor)
    CompositionLocalProvider(LocalContentColor provides contentColor) {

        val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
        com.google.accompanist.insets.ui.Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                ThanoxSmallTopAppBar(scrollBehavior, title, actions, onBackPressed)
            },
            bottomBar = {
                // We add a spacer as a bottom bar, which is the same height as
                // the navigation bar
                Spacer(
                    Modifier
                        .navigationBarsHeight()
                        .fillMaxWidth()
                )
            }, content = {
                content(it)
            }
        )
    }
}

@Composable
fun ThanoxSmallTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    title: @Composable () -> Unit,
    actions: @Composable (RowScope.() -> Unit),
    onBackPressed: () -> Unit
) {
    val backgroundColors = TopAppBarDefaults.smallTopAppBarColors()
    val backgroundColor = backgroundColors.containerColor(
        scrollFraction = scrollBehavior.scrollFraction
    ).value
    val foregroundColors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = Color.Transparent,
        scrolledContainerColor = Color.Transparent
    )
    Surface(color = backgroundColor) {
        SmallTopAppBar(
            colors = foregroundColors,
            modifier = Modifier
                .padding(
                    rememberInsetsPaddingValues(
                        LocalWindowInsets.current.statusBars,
                        applyBottom = false,
                    )
                ),
            title = title,
            actions = actions,
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
            scrollBehavior = scrollBehavior
        )
    }
}
