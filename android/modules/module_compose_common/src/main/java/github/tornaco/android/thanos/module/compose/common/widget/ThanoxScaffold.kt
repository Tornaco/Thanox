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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.google.accompanist.insets.navigationBarsHeight
import github.tornaco.android.thanos.module.compose.common.widget.md3.TopAppBarScrollBehaviorX
import github.tornaco.android.thanos.module.compose.common.widget.md3.XTopAppBarDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThanoxScaffold(
    title: @Composable () -> Unit,
    smallTitle: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
    onBackPressed: () -> Unit,
    content: @Composable (PaddingValues, TopAppBarScrollBehaviorX) -> Unit
) {
    val scrollBehavior = remember { XTopAppBarDefaults.enterAlwaysScrollBehavior() }
    com.google.accompanist.insets.ui.Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ThanoxAppBar(
                title = title,
                smallTitle = smallTitle,
                actions = actions,
                scrollBehavior = scrollBehavior,
                onBackPressed = onBackPressed,
            )
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
            content(it, scrollBehavior)
        }
    )
}