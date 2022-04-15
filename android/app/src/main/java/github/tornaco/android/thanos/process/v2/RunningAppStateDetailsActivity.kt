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

package github.tornaco.android.thanos.process.v2

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import dev.enro.annotations.NavigationDestination
import dev.enro.core.NavigationKey
import github.tornaco.android.thanos.module.compose.common.theme.ThanoxTheme
import github.tornaco.android.thanos.theme.ThemeActivity
import kotlinx.parcelize.Parcelize

@Parcelize
data class RunningAppStateDetails(val state: RunningAppState) : NavigationKey

@AndroidEntryPoint
@NavigationDestination(RunningAppStateDetails::class)
class RunningAppStateDetailsActivity : ThemeActivity() {
    override fun isF(): Boolean {
        return true
    }

    override fun isADVF(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Turn off the decor fitting system windows, which means we need to through handling
        // insets
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ThanoxTheme {
                // Update the system bars to be translucent
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = MaterialTheme.colors.isLight
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        Color.Transparent,
                        darkIcons = useDarkIcons
                    )
                }
                ProvideWindowInsets {
                    Surface {
                        RunningAppStateDetailsPage()
                    }
                }
            }
        }
    }
}