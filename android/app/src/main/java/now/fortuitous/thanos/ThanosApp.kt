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

package now.fortuitous.thanos

import android.content.Context
import android.os.Build
import com.elvishew.xlog.XLog
import dagger.hilt.android.HiltAndroidApp
import github.tornaco.android.thanos.MultipleModulesApp
import github.tornaco.android.thanos.core.app.AppGlobals
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.main.installCrashHandler
import github.tornaco.android.thanos.main.launchSubscribeActivity
import github.tornaco.android.thanos.module.compose.common.ThemeActivityVM
import github.tornaco.android.thanos.support.AppFeatureManager
import github.tornaco.android.thanos.support.initThanos
import github.tornaco.android.thanos.util.ShortcutUtil
import github.tornaco.android.thanos.util.initAppLog
import io.reactivex.plugins.RxJavaPlugins
import kotlinx.coroutines.runBlocking
import now.fortuitous.thanos.power.ShortcutStubActivity
import org.lsposed.hiddenapibypass.HiddenApiBypass
import tornaco.apps.thanox.ThanosShizuku


@HiltAndroidApp
class ThanosApp : MultipleModulesApp() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        AppGlobals.setContext(base)
        initAppLog(base, "ThanosApp")
        this.installCrashHandler()
    }

    override fun onCreate() {
        AppGlobals.setContext(this.applicationContext)
        super.onCreate()

        // Install error handler.
        // Error handler default print error info.
        RxJavaPlugins.setErrorHandler { throwable: Throwable? ->
            XLog.e("\n")
            XLog.e("==== App un-handled error, please file a bug ====")
            XLog.e(throwable)
            XLog.e("\n")
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            HiddenApiBypass.addHiddenApiExemptions("")
        }

        XLog.w("onCreate.$this")

        initThanos {
            AppFeatureManager.launchSubscribeActivity = { launchSubscribeActivity(it) {} }

            XposedScope.init()

            runBlocking {
                ThemeActivityVM.init(this@ThanosApp)
                XLog.d("ThemeActivityVM: ${ThemeActivityVM.state.value}")
            }

            // Init Shizuku. if xposed mod is not available.
            if (!ThanosManager.from(this).isServiceInstalled) {
                ThanosShizuku.init(this)
                ThanosShizuku.installShortcut = { context, app ->
                    ShortcutUtil.addShortcut(context, app) {
                        ShortcutStubActivity.createIntent(
                            context,
                            app.pkgName,
                            app.userId
                        )
                    }
                }
            }
        }
    }
}