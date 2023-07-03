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

package github.tornaco.practice.honeycomb.locker.ui.setup

import android.content.ComponentName
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.TextInputDialog
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxSmallAppBarScaffold
import github.tornaco.android.thanos.module.compose.common.widget.rememberTextInputState
import github.tornaco.android.thanos.util.ActivityUtils
import github.tornaco.practice.honeycomb.locker.R

@AndroidEntryPoint
class WhiteListComponentViewerActivity : ComposeThemeActivity() {
    object Starter {
        fun start(context: Context) {
            ActivityUtils.startActivity(context, WhiteListComponentViewerActivity::class.java)
        }
    }

    @Composable
    override fun Content() {
        val viewModel = hiltViewModel<WCVViewModel>()
        val state by viewModel.state.collectAsState()

        LaunchedEffect(viewModel) {
            viewModel.load()
        }
        val inputState = rememberTextInputState(
            title = stringResource(id = R.string.module_locker_title_white_list_components),
            message = null,
            onSelected = { input ->
                ComponentName.unflattenFromString(input)?.let {
                    viewModel.add(it)
                }
            })
        TextInputDialog(state = inputState)


        ThanoxSmallAppBarScaffold(title = {
            Text(
                text = stringResource(id = R.string.module_locker_title_white_list_components),
                style = TypographyDefaults.appBarTitleTextStyle()
            )
        },
            onBackPressed = { finish() },
            actions = {
                IconButton(onClick = { inputState.show() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.module_common_ic_add_fill),
                        contentDescription = "Add"
                    )
                }
            }) { contentPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .padding(horizontal = 16.dp)
            ) {
                items(state.components) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 64.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(modifier = Modifier.weight(1f), text = it.flattenToShortString())

                        IconButton(onClick = { viewModel.remove(it) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.module_common_ic_outline_delete_24),
                                contentDescription = "Delete"
                            )
                        }
                    }
                }
            }
        }

    }


}

