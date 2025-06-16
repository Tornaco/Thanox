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
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dagger.hilt.android.AndroidEntryPoint
import github.tornaco.android.thanos.core.analytics.SelectActiveMethodShizuku
import github.tornaco.android.thanos.core.analytics.SelectActiveMethodXposedOrMagisk
import github.tornaco.android.thanos.main.Analytics
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import github.tornaco.android.thanos.module.compose.common.widget.LargeSpacer
import github.tornaco.android.thanos.module.compose.common.widget.StandardSpacer
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxCardRoundedCornerShape
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

                    MethodCard(
                        title = "Shizuku",
                        summary = stringResource(id = github.tornaco.android.thanos.res.R.string.active_method_chooser_summary_shizuku),
                        onClick = {
                            Analytics.reportEvent(SelectActiveMethodShizuku)
                            AppPreference.setAppType(context, "thanos")
                            NavActivity.Starter.start(context)
                            finish()
                        })

                    LargeSpacer()


                    MethodCard(
                        title = "Xposed/Magisk",
                        summary = stringResource(id = github.tornaco.android.thanos.res.R.string.active_method_chooser_summary_xposed_magisk),
                        onClick = {
                            Analytics.reportEvent(SelectActiveMethodXposedOrMagisk)
                            AppPreference.setAppType(context, "thanox")
                            NavActivity.Starter.start(context)
                            finish()
                        })
                }
            }
        }
    }
}

@Composable
fun MethodCard(
    title: String,
    summary: String,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .padding(16.dp),
        shape = ThanoxCardRoundedCornerShape,
        onClick = onClick,
        content = {
            Column(Modifier.padding(16.dp)) {
                Text(text = title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                StandardSpacer()
                Text(text = summary)
            }
        }
    )
}