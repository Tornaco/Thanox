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

package now.fortuitous.thanos.onboarding

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.material.composethemeadapter3.Mdc3Theme
import github.tornaco.android.thanos.theme.ThemeActivity
import github.tornaco.android.thanos.util.ActivityUtils
import now.fortuitous.thanos.main.NavActivity

class OnBoardingActivity : ThemeActivity() {

    object Starter {
        fun start(context: Context?) {
            ActivityUtils.startActivity(context, OnBoardingActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Mdc3Theme {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = androidx.compose.material.MaterialTheme.colors.isLight
                val colorSurface = MaterialTheme.colorScheme.surface

                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = colorSurface,
                        darkIcons = useDarkIcons
                    )
                }

                val context = LocalContext.current

                OnBoardingScreen {
                    now.fortuitous.thanos.pref.AppPreference.setHasOnBoarding(context, true)
                    NavActivity.Starter.start(thisActivity())
                    finish()
                }
            }
        }
    }
}
