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
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.widget.addTextChangedListener
import androidx.hilt.navigation.compose.hiltViewModel
import com.elvishew.xlog.XLog
import com.google.accompanist.insets.navigationBarsWithImePadding
import dagger.hilt.android.AndroidEntryPoint
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxSmallAppBarScaffold
import github.tornaco.android.thanos.util.ActivityUtils
import github.tornaco.android.thanos.util.TypefaceHelper
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
            Console(contentPadding, state) {
                viewModel.input(it)
            }
        }
    }
}

@Composable
private fun Console(
    contentPadding: PaddingValues,
    state: ConsoleState,
    onInput: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsWithImePadding(),
            factory = { context ->
                val binding =
                    ModuleProfileConsoleEditorBinding.inflate(LayoutInflater.from(context))
                initView(context, binding, state, onInput)
                return@AndroidView binding.root
            })
    }
}

private fun initView(
    context: Context,
    binding: ModuleProfileConsoleEditorBinding,
    state: ConsoleState,
    onInput: (String) -> Unit
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
        // This is symbols.
        val s = item.title.toString()
        XLog.i("Input: %s", s)
        val start = binding.editText.selectionStart.coerceAtLeast(0)
        val end = binding.editText.selectionEnd.coerceAtLeast(0)
        if (binding.editText.text == null) return@OnMenuItemClickListener false
        binding.editText.text!!
            .replace(
                start.coerceAtMost(end),
                start.coerceAtLeast(end),
                s,
                0,
                s.length
            )
        true
    }
    binding.editorActionsToolbarSymbols1.setOnMenuItemClickListener(symbolClickListener)
    binding.editorActionsToolbarSymbols2.setOnMenuItemClickListener(symbolClickListener)
    binding.editorActionsToolbarSymbols3.setOnMenuItemClickListener(symbolClickListener)

    binding.placeholder = state.action
    binding.editText.typeface = TypefaceHelper.jetbrainsMono(context)
    binding.lineLayout.setTypeface(TypefaceHelper.jetbrainsMono(context))
    binding.editText.addSyntax(context.assets, "java.json")
    binding.lineLayout.attachEditText(binding.editText)
    binding.editText.startHighlight(true)
    binding.editText.updateVisibleRegion()
    binding.editText.addTextChangedListener {
        onInput(it.toString())
    }
}
