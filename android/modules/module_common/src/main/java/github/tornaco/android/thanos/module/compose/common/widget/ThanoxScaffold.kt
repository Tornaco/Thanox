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

@file:OptIn(ExperimentalMaterial3Api::class)

package github.tornaco.android.thanos.module.compose.common.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    onBackPressed: (() -> Unit)?,
    scaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
    searchBarState: SearchBarState = rememberSearchBarState(),
    sheetContent: @Composable ColumnScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    val containerColor = MaterialTheme.colorScheme.surfaceVariant
    val contentColor = contentColorFor(containerColor)
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        val appbarState = rememberTopAppBarState()
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(appbarState)
        BottomSheetScaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            scaffoldState = scaffoldState,
            topBar = {
                ThanoxSmallTopAppBarContainer(
                    searchBarState,
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

@Composable
fun ThanoxSmallAppBarScaffold(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    actions: @Composable (RowScope.() -> Unit) = {},
    onBackPressed: (() -> Unit)?,
    floatingActionButton: @Composable () -> Unit = {},
    searchBarState: SearchBarState = rememberSearchBarState(),
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            ThanoxSmallTopAppBarContainer(
                searchBarState,
                title,
                actions,
                onBackPressed
            )
        },
        floatingActionButton = floatingActionButton,
        bottomBar = {
            Column {
                bottomBar()
                Spacer(
                    Modifier
                        .navigationBarsHeight()
                        .fillMaxWidth()
                )
            }
        }, content = {
            content(it)
        }
    )
}

@Composable
private fun ThanoxSmallTopAppBarContainer(
    searchBarState: SearchBarState = rememberSearchBarState(),
    title: @Composable () -> Unit,
    actions: @Composable (RowScope.() -> Unit),
    onBackPressed: (() -> Unit)? = null,
) {
    Surface {
        AnimatedVisibility(
            visible = searchBarState.showSearchBar,
            enter = fadeIn(), exit = fadeOut()
        ) {
            SearchBar(searchBarState)
        }
        AnimatedVisibility(
            visible = !searchBarState.showSearchBar,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            ThanoxSmallTopAppBar(title, actions, onBackPressed)
        }
    }
}

@Composable
private fun ThanoxSmallTopAppBar(
    title: @Composable () -> Unit,
    actions: @Composable (RowScope.() -> Unit),
    onBackPressed: (() -> Unit)? = null,
) {
    androidx.compose.material3.TopAppBar(
        modifier = Modifier,
        title = title,
        actions = actions,
        navigationIcon = {
            onBackPressed?.let {
                IconButton(onClick = {
                    it()
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
    )
}

@Composable
private fun SearchBar(searchBarState: SearchBarState) {
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
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
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