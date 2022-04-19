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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.FabPosition
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.rememberInsetsPaddingValues


@OptIn(ExperimentalMaterial3Api::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
fun ThanoxBottomSheetScaffold(
    title: @Composable () -> Unit,
    actions: @Composable (RowScope.() -> Unit) = {},
    onBackPressed: () -> Unit,
    scaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
    searchBarState: SearchBarState = rememberSearchBarState(),
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
                ThanoxSmallTopAppBarContainer(
                    searchBarState,
                    scrollBehavior,
                    title,
                    actions,
                    onBackPressed
                )
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
fun ThanoxSmallAppBarScaffold(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    actions: @Composable (RowScope.() -> Unit) = {},
    onBackPressed: () -> Unit,
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    isFloatingActionButtonDocked: Boolean = false,
    searchBarState: SearchBarState = rememberSearchBarState(),
    content: @Composable (PaddingValues) -> Unit
) {
    val containerColor = MaterialTheme.colorScheme.surfaceVariant
    val contentColor = contentColorFor(containerColor)
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
        com.google.accompanist.insets.ui.Scaffold(
            modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                ThanoxSmallTopAppBarContainer(
                    searchBarState,
                    scrollBehavior,
                    title,
                    actions,
                    onBackPressed
                )
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
private fun ThanoxSmallTopAppBarContainer(
    searchBarState: SearchBarState = rememberSearchBarState(),
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
        AnimatedVisibility(
            visible = searchBarState.showSearchBar,
            enter = fadeIn(), exit = fadeOut()
        ) {
            SearchBar(scrollBehavior, searchBarState)
        }
        AnimatedVisibility(
            visible = !searchBarState.showSearchBar,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            ThanoxSmallTopAppBar(foregroundColors, title, actions, onBackPressed, scrollBehavior)
        }
    }
}

@Composable
private fun ThanoxSmallTopAppBar(
    foregroundColors: TopAppBarColors,
    title: @Composable () -> Unit,
    actions: @Composable (RowScope.() -> Unit),
    onBackPressed: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
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
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun SearchBar(scrollBehavior: TopAppBarScrollBehavior, searchBarState: SearchBarState) {
    val foregroundColors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = Color.Transparent,
        scrolledContainerColor = Color.Transparent
    )
    val scrollFraction = scrollBehavior.scrollFraction
    val appBarContainerColor by foregroundColors.containerColor(scrollFraction)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                rememberInsetsPaddingValues(
                    LocalWindowInsets.current.statusBars,
                    applyBottom = false,
                )
            )
            .padding(horizontal = 4.dp, vertical = 4.dp),
        color = appBarContainerColor,
    ) {
        val focusRequester = remember {
            FocusRequester()
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                searchBarState.closeSearchBar()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
            SearchTextField(searchBarState, focusRequester)
        }

        SideEffect {
            focusRequester.requestFocus()
        }
    }
}

@Composable
private fun SearchTextField(searchBarState: SearchBarState, focusRequester: FocusRequester) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        value = searchBarState.keyword,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(6.dp),
        maxLines = 1,
        trailingIcon = {
            IconButton(onClick = {
                searchBarState.inputKeyword("")
            }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Clear keywords"
                )
            }
        },
        onValueChange = {
            searchBarState.inputKeyword(it)
        })
}

@Composable
fun rememberSearchBarState(): SearchBarState {
    return remember {
        SearchBarState()
    }
}

class SearchBarState {
    private var _showSearchBar by mutableStateOf(false)
    val showSearchBar get() = _showSearchBar

    private var _keyword by mutableStateOf("")
    val keyword get() = _keyword

    fun inputKeyword(value: String) {
        _keyword = value
    }

    fun closeSearchBar() {
        _showSearchBar = false
    }

    fun showSearchBar() {
        _showSearchBar = true
    }
}