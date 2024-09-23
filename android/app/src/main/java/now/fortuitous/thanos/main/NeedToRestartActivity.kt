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
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PublishedWithChanges
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.support.withThanos
import github.tornaco.android.thanos.util.ActivityUtils

class NeedToRestartActivity : ComponentActivity() {

    object Starter {
        fun start(context: Context?) {
            ActivityUtils.startActivity(context, NeedToRestartActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NeedToStartScreen()
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    @Preview(name = "NeedToStartScreen", showSystemUi = true, showBackground = true)
    private fun NeedToStartScreen() {
        var visible by remember { mutableStateOf(false) }

        Surface(modifier = Modifier.fillMaxSize()) {
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(
                    initialOffsetY = {
                        // Slide in from top
                        -it
                    },
                    animationSpec = tween(
                        durationMillis = 400,
                        easing = CubicBezierEasing(0f, 0f, 0f, 1f)

                    )
                ),
            ) {
                Box(modifier = Modifier.background(color = colorResource(id = github.tornaco.android.thanos.module.common.R.color.md_red_a700))) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Icon(
                            modifier = Modifier.size(72.dp),
                            tint = Color.White,
                            imageVector = Icons.Filled.PublishedWithChanges,
                            contentDescription = "AlertIcon"
                        )
                        Text(
                            modifier = Modifier.padding(16.dp),
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            text = stringResource(id = github.tornaco.android.thanos.res.R.string.message_reboot_needed),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 32.dp, bottom = 12.dp)
                    ) {

                        TextButton(modifier = Modifier.padding(16.dp), onClick = { finish() }) {
                            Text(
                                color = Color.White,
                                text = AnnotatedString(stringResource(id = github.tornaco.android.thanos.res.R.string.reboot_later)).capitalize(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        TextButton(modifier = Modifier.padding(vertical = 16.dp),
                            onClick = {
                                withThanos {
                                    powerManager.reboot()
                                }
                            }) {
                            Text(
                                color = Color.White,
                                text = AnnotatedString(stringResource(id = github.tornaco.android.thanos.res.R.string.reboot_now)).capitalize(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }

        LaunchedEffect(true) {
            visible = true
        }
    }
}