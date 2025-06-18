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

package github.tornaco.thanos.android.module.profile.example

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import github.tornaco.android.thanos.module.compose.common.requireActivity
import github.tornaco.android.thanos.module.compose.common.theme.LocalThanoxColorSchema
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.theme.getColorAttribute
import github.tornaco.android.thanos.module.compose.common.widget.SmallSpacer
import github.tornaco.android.thanos.module.compose.common.widget.StandardSpacer
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxMediumAppBarScaffold
import github.tornaco.android.thanos.module.compose.common.widget.clickableWithRipple
import github.tornaco.android.thanos.util.ActivityUtils
import github.tornaco.android.thanos.util.ToastUtils
import github.tornaco.thanos.android.module.profile.RuleEditorActivity

@AndroidEntryPoint
class ProfileExampleActivity : ComposeThemeActivity() {

    object Starter {
        fun start(context: Context?) {
            ActivityUtils.startActivity(context, ProfileExampleActivity::class.java)
        }
    }

    @Composable
    override fun Content() {
        val viewModel = hiltViewModel<ProfileExampleViewModel>()
        val state by viewModel.state.collectAsState()
        val context = LocalContext.current

        SideEffect {
            viewModel.loadAssetExamples()
        }

        LaunchedEffect(viewModel) {
            viewModel.events.collect { event ->
                when (event) {
                    is Event.ImportSuccess -> {
                        ToastUtils.ok(context)
                    }

                    is Event.ImportFail -> {
                        ToastUtils.nook(context, event.error)
                    }

                    is Event.ImportFailRuleWithSameNameAlreadyExists -> {
                        ToastUtils.nook(context, "${event.name} already exists")
                    }
                }
            }
        }

        ThanoxMediumAppBarScaffold(title = {
            Text(
                text = stringResource(id = github.tornaco.android.thanos.res.R.string.module_profile_rule_impor_example),
                style = TypographyDefaults.appBarTitleTextStyle()
            )
        },
            onBackPressed = { finish() },
            actions = {

            }) { contentPadding ->
            ExampleList(contentPadding, state) {
                viewModel.import(it)
            }
        }
    }
}

@Composable
fun ExampleList(contentPadding: PaddingValues, state: ExampleState, import: (Example) -> Unit) {
    val windowBgColor = getColorAttribute(android.R.attr.windowBackground)
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(windowBgColor))
            .padding(contentPadding)
    ) {

        items(state.examples) {
            ExampleItem(it, import)
        }
    }
}

@Composable
private fun ExampleItem(example: Example, import: (Example) -> Unit) {
    val activity = LocalContext.current.requireActivity()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color = LocalThanoxColorSchema.current.cardBgColor)
            .clickableWithRipple {
                RuleEditorActivity.start(activity, example.ruleInfo, example.ruleInfo.format, true)
            }
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = example.ruleInfo.name, style = MaterialTheme.typography.titleMedium)
            SmallSpacer()
            Text(
                text = example.ruleInfo.description,
                style = MaterialTheme.typography.labelMedium
            )

            StandardSpacer()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                OutlinedButton(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = {
                        import(example)
                    }) {
                    Icon(
                        imageVector = Icons.Filled.Download,
                        contentDescription = "Import",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(text = stringResource(id = github.tornaco.android.thanos.res.R.string.common_menu_title_import))
                }
            }
        }
    }
}