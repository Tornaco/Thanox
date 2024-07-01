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

package now.fortuitous.thanos.main

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import github.tornaco.android.thanos.module.compose.common.widget.LargeSpacer
import github.tornaco.android.thanos.util.ActivityUtils
import now.fortuitous.thanos.pref.AppPreference
import tornaco.apps.thanox.base.ui.NiaGradientBackground
import tornaco.apps.thanox.base.ui.theme.ThanosTheme

@AndroidEntryPoint
class ChooserActivity : ComposeThemeActivity() {

    object Starter {
        @JvmStatic
        fun start(context: Context?) {
            ActivityUtils.startActivity(context, ChooserActivity::class.java)
        }
    }

    override fun isF(): Boolean {
        return false
    }

    @Composable
    override fun Content() {
        AppChooser()
    }

    @Composable
    private fun AppChooser() {
        val context = LocalContext.current
        ThanosTheme {
            NiaGradientBackground(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = stringResource(id = github.tornaco.android.thanos.res.R.string.app_select_activate_method),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    repeat(4) {
                        LargeSpacer()
                    }

                    Button(modifier = Modifier.fillMaxWidth(0.68f), onClick = {
                        AppPreference.setAppType(context, "thanos")
                        NavActivity.Starter.start(context)
                        finish()
                    }) {
                        Text(text = "Shizuku - " + stringResource(id = github.tornaco.android.thanos.module.common.R.string.common_badge_text_experiment))
                    }
                    LargeSpacer()
                    Button(modifier = Modifier.fillMaxWidth(0.68f), onClick = {
                        AppPreference.setAppType(context, "thanox")
                        NavActivity.Starter.start(context)
                        finish()
                    }) {
                        Text(text = "Xposed/Magisk")
                    }
                }
            }
        }
    }
}