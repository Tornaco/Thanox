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

@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package github.tornaco.android.thanos.module.compose.common.widget

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
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

@Composable
fun ThanoxMediumAppBarScaffold(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    actions: @Composable (RowScope.() -> Unit) = {},
    onBackPressed: (() -> Unit)?,
    floatingActionButton: @Composable () -> Unit = {},
    searchBarState: SearchBarState = rememberSearchBarState(),
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ThanoxMediumTopAppBarContainer(
                title = title,
                actions = actions,
                onBackPressed = onBackPressed,
                searchBarState = searchBarState,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = floatingActionButton,
        bottomBar = {
            Column {
                bottomBar()
                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                )
            }
        }, content = {
            content(it)
        }
    )
}

@Composable
private fun ThanoxMediumTopAppBarContainer(
    title: @Composable () -> Unit,
    actions: @Composable (RowScope.() -> Unit),
    onBackPressed: (() -> Unit)? = null,
    searchBarState: SearchBarState = rememberSearchBarState(),
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    AnimatedContent(searchBarState.showSearchBar) {
        if (it) {
            SearchBar(searchBarState)
        } else {
            ThanoxMediumTopAppBar(title, actions, onBackPressed, scrollBehavior)
        }
    }
}

@Composable
private fun ThanoxMediumTopAppBar(
    title: @Composable () -> Unit,
    actions: @Composable (RowScope.() -> Unit),
    onBackPressed: (() -> Unit)? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    LargeFlexibleTopAppBar(
        modifier = Modifier,
        title = title,
        titleHorizontalAlignment = Alignment.Start,
        actions = actions,
        scrollBehavior = scrollBehavior,
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
            .statusBarsPadding()
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
        inputKeyword("")
        _showSearchBar = false
    }

    fun showSearchBar() {
        _showSearchBar = true
    }
}