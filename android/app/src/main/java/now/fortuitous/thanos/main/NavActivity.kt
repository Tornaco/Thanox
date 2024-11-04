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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import github.tornaco.android.thanos.BuildProp
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.main.blockOnCreate
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import github.tornaco.android.thanos.util.ActivityUtils
import now.fortuitous.thanos.onboarding.OnBoardingActivity
import now.fortuitous.thanos.pref.AppPreference
import tornaco.apps.thanox.MainGraph
import tornaco.apps.thanox.base.ui.theme.ThanosTheme
import tornaco.apps.thanox.subscribe.ThanosApp

@AndroidEntryPoint
class NavActivity : ComposeThemeActivity() {

    object Starter {
        @JvmStatic
        fun start(context: Context?) {
            ActivityUtils.startActivity(context, NavActivity::class.java)
        }
    }

    override fun isF(): Boolean {
        return false
    }

    @Composable
    override fun Content() {
        // OnBoarding
        if (!AppPreference.hasOnBoarding(thisActivity())) {
            OnBoardingActivity.Starter.start(thisActivity())
            finish()
            return
        }

        // Check if x is available
        if (ThanosManager.from(thisActivity()).isServiceInstalled) {
            AppPreference.setAppType(thisActivity(), "ask")
            Thanox()
            return
        }

        @Suppress("KotlinConstantConditions")
        if (BuildProp.THANOS_BUILD_FLAVOR == "row") {
            RowNav()
        } else {
            PRCNav()
        }
    }

    @Composable
    private fun RowNav() {
        val sx = AppPreference.getAppType(thisActivity())
        when (sx) {
            "thanox" -> {
                Thanox()
            }

            "thanos" -> {
                Thanos()
            }

            else -> {
                ChooserActivity.Starter.start(thisActivity())
                finish()
            }
        }
    }

    @Composable
    private fun PRCNav() {
        val sx = AppPreference.getAppType(thisActivity())
        when (sx) {
            "thanos" -> {
                Thanos()
            }

            else -> {
                Thanox()
            }
        }
    }

    @Composable
    private fun Thanox() {
        // Block
        if (blockOnCreate(thisActivity())) {
            finish()
            return
        }

        LaunchedEffect(Unit) {
            ShortcutInit(thisActivity()).initOnBootThanox()
        }

        val applyNewHome = AppPreference.isFeatureNoticeAccepted(thisActivity(), "NEW_HOME")
        if (applyNewHome) AllNewNavScreen() else NavScreen()
    }

    @Composable
    private fun Thanos() {
        LaunchedEffect(Unit) {
            ShortcutInit(thisActivity()).initOnBootThanos()
        }

        ThanosApp {
            ThanosTheme {
                var privacyAgreementAccept by remember {
                    mutableStateOf(false)
                }
                LaunchedEffect(Unit) {
                    privacyAgreementAccept =
                        PreferenceManager.getDefaultSharedPreferences(thisActivity())
                            .getBoolean(privacyAgreementKey, false)
                }

                if (!privacyAgreementAccept) {
                    PrivacyStatementDialog(onDismissRequest = {
                        PreferenceManager.getDefaultSharedPreferences(thisActivity()).edit()
                            .putBoolean(privacyAgreementKey, true).apply()
                        privacyAgreementAccept = true
                    })
                }


                MainGraph()
            }
        }
    }
}