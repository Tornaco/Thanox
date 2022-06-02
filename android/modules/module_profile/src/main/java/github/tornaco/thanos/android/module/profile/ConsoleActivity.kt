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
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.navigationBarsWithImePadding
import dagger.hilt.android.AndroidEntryPoint
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import github.tornaco.android.thanos.module.compose.common.jetbrainMonoTypography
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxSmallAppBarScaffold
import github.tornaco.android.thanos.util.ActivityUtils
import github.tornaco.thanos.android.module.profile.databinding.ModuleProfileConsoleEditorBinding

@AndroidEntryPoint
class ConsoleActivity : ComposeThemeActivity() {
    object Starter {
        fun start(context: Context?) {
            ActivityUtils.startActivity(context, ConsoleActivity::class.java)
        }
    }

    @Composable
    override fun Content() {
        val viewModel = hiltViewModel<ConsoleViewModel>()
        val state by viewModel.state.collectAsState()


        ThanoxSmallAppBarScaffold(title = {
            Text(
                text = "Action Console",
                style = TypographyDefaults.appBarTitleTextStyle()
            )
        },
            onBackPressed = { finish() },
            actions = {
                IconButton(onClick = {
                    viewModel.execute()
                }) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Execute"
                    )
                }
            }) { contentPadding ->
            Console(
                contentPadding = contentPadding,
                state = state,
                onInput = {
                    viewModel.input(it)
                },
                insertSymbol = {
                    viewModel.insertSymbol(it)
                })
        }
    }
}

@Composable
private fun Console(
    contentPadding: PaddingValues,
    state: ConsoleState,
    onInput: (TextFieldValue) -> Unit,
    insertSymbol: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        CodeContent(state, onInput)
        ConsoleLog(state)
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsWithImePadding(),
            factory = { context ->
                val binding =
                    ModuleProfileConsoleEditorBinding.inflate(LayoutInflater.from(context))
                initView(context, binding, insertSymbol)
                return@AndroidView binding.root
            })
    }
}

@Composable
private fun ConsoleLog(state: ConsoleState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 160.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .navigationBarsWithImePadding()
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp),
            text = state.consoleLogOutput,
            style = jetbrainMonoTypography().body2
        )
    }

}

// https://github.com/codeckle/compose-code-editor
@Composable
private fun CodeContent(
    state: ConsoleState,
    onInput: (TextFieldValue) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 160.dp)
            .verticalScroll(rememberScrollState())
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = jetbrainMonoTypography().body2,
            value = state.textFieldValue,
            onValueChange = {
                onInput(it)
            }
        )
    }
}


private fun initView(
    context: Context,
    binding: ModuleProfileConsoleEditorBinding,
    insertSymbol: (String) -> Unit = {}
) {
    var symbols: Array<String?> =
        context.resources.getStringArray(R.array.module_profile_symbols_1)

    // Range 100~200
    var baseId = 100

    for (i in symbols.indices) {
        val item = binding.editorActionsToolbarSymbols1.menu.add(
            1000, baseId + i, Menu.NONE,
            symbols[i]
        )
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
    }

    symbols = context.resources.getStringArray(R.array.module_profile_symbols_2)
    baseId = 200
    for (i in symbols.indices) {
        val item = binding.editorActionsToolbarSymbols2.menu.add(
            1000, baseId + i, Menu.NONE,
            symbols[i]
        )
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
    }

    symbols = context.resources.getStringArray(R.array.module_profile_symbols_3)
    baseId = 300
    for (i in symbols.indices) {
        val item = binding.editorActionsToolbarSymbols3.menu.add(
            1000, baseId + i, Menu.NONE,
            symbols[i]
        )
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
    }

    val symbolClickListener = Toolbar.OnMenuItemClickListener { item: MenuItem ->
        insertSymbol(item.title.toString())
        true
    }
    binding.editorActionsToolbarSymbols1.setOnMenuItemClickListener(symbolClickListener)
    binding.editorActionsToolbarSymbols2.setOnMenuItemClickListener(symbolClickListener)
    binding.editorActionsToolbarSymbols3.setOnMenuItemClickListener(symbolClickListener)
}