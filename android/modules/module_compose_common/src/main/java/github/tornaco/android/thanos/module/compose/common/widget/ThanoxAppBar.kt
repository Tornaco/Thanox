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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.primarySurface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import github.tornaco.android.thanos.module.compose.common.R
import github.tornaco.android.thanos.module.compose.common.widget.md3.LargeTopAppBarX
import github.tornaco.android.thanos.module.compose.common.widget.md3.XTopAppBarDefaults
import github.tornaco.android.thanos.module.compose.common.widget.md3.TopAppBarScrollBehaviorX


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThanoxAppBar(
    title: @Composable () -> Unit,
    smallTitle: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehaviorX,
    onBackPressed: () -> Unit,
) {
    val backgroundColors = XTopAppBarDefaults.largeTopAppBarColors()
    val backgroundColor = backgroundColors.containerColor(
        scrollFraction = scrollBehavior.scrollFraction
    ).value
    MD3TopAppBar(
        backgroundColor = backgroundColor,
        scrollBehavior = scrollBehavior,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = rememberInsetsPaddingValues(
            LocalWindowInsets.current.statusBars,
            applyBottom = false,
        ),
        title = title,
        smallTitle = smallTitle,
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
        actions = actions,
        elevation = 0.dp
    )
}

@Composable
private fun MD3TopAppBar(
    title: @Composable () -> Unit,
    smallTitle: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    navigationIcon: @Composable (() -> Unit) = {},
    actions: @Composable RowScope.() -> Unit = {},
    backgroundColor: Color = androidx.compose.material.MaterialTheme.colors.primarySurface,
    elevation: Dp = AppBarDefaults.TopAppBarElevation,
    scrollBehavior: TopAppBarScrollBehaviorX? = null
) {
    androidx.compose.material.Surface(
        color = backgroundColor,
        elevation = elevation,
        modifier = modifier
    ) {
        LargeTopAppBarX(
            title = title,
            smallTitle = smallTitle,
            navigationIcon = navigationIcon,
            actions = actions,
            modifier = Modifier.padding(contentPadding),
            scrollBehaviorX = scrollBehavior
        )
    }
}
