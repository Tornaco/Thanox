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

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.preference.PreferenceManager
import com.anggrayudi.storage.SimpleStorageHelper
import com.elvishew.xlog.XLog
import dagger.hilt.android.AndroidEntryPoint
import github.tornaco.android.thanos.main.NavEntry
import github.tornaco.android.thanos.main.blockOnCreate
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import github.tornaco.android.thanos.support.subscribe.LVLStateEffects
import github.tornaco.android.thanos.support.subscribe.ThanosApp
import github.tornaco.android.thanos.util.ActivityUtils
import now.fortuitous.thanos.onboarding.OnBoardingActivity
import now.fortuitous.thanos.pref.AppPreference
import tornaco.apps.thanox.MainGraph

@AndroidEntryPoint
class NavActivity : ComposeThemeActivity() {
    private val storageHelper = SimpleStorageHelper(this)

    object Starter {
        @JvmStatic
        fun start(context: Context?) {
            ActivityUtils.startActivity(context, NavActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        XLog.w("onCreate.$this")
        val splash = installSplashScreen()
        super.onCreate(savedInstanceState)
    }

    @Composable
    override fun Content() {
        XLog.w("Content")
        // OnBoarding
        if (!AppPreference.hasOnBoarding(this)) {
            OnBoardingActivity.Starter.start(this)
            finish()
            return
        }
        XLog.w("CompositionLocalProvider.")
        CompositionLocalProvider(LocalSimpleStorageHelper provides storageHelper) {
            NavEntry()
        }
    }

}


@Composable
fun Activity.ThanoxXposed() {
    XLog.w("ThanoxXposed")
    // Block
    if (blockOnCreate(this)) {
        finish()
        return
    }

    LaunchedEffect(Unit) {
        ShortcutInit(this@ThanoxXposed).initOnBootThanox()
    }
    LVLStateEffects()
    val applyNewHome = AppPreference.isFeatureNoticeAccepted(this, "NEW_HOME")
    XLog.w("applyNewHome: $applyNewHome")
    if (applyNewHome) AllNewNavScreen() else NavScreen()
}

@SuppressLint("UseKtx")
@Composable
fun Activity.ThanoxShizuku() {
    XLog.w("ThanoxShizuku")
    LVLStateEffects()
    LaunchedEffect(Unit) {
        ShortcutInit(this@ThanoxShizuku).initOnBootThanos()
    }
    ThanosApp {
        var privacyAgreementAccept by remember {
            mutableStateOf(false)
        }
        LaunchedEffect(Unit) {
            privacyAgreementAccept =
                PreferenceManager.getDefaultSharedPreferences(this@ThanoxShizuku)
                    .getBoolean(privacyAgreementKey, false)
        }

        if (!privacyAgreementAccept) {
            PrivacyStatementDialog(onDismissRequest = {
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                    .putBoolean(privacyAgreementKey, true).apply()
                privacyAgreementAccept = true
            })
        }


        MainGraph()
    }
}

enum class AppType(val prefValue: String) {
    BasedOnXposed("thanox"),
    BasedOnShizuku("thanos"),
    Ask("ask"),
}