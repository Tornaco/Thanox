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

package github.tornaco.android.thanos

import android.content.Context
import com.elvishew.xlog.XLog
import dagger.hilt.android.HiltAndroidApp
import dev.enro.annotations.NavigationComponent
import dev.enro.core.controller.NavigationApplication
import dev.enro.core.controller.navigationController
import dev.enro.core.plugins.EnroLogger
import github.tornaco.android.thanos.app.FeatureAccessStats
import github.tornaco.android.thanos.app.Init
import github.tornaco.android.thanos.core.app.AppGlobals
import github.tornaco.thanos.android.noroot.NoRootSupport.install
import io.reactivex.plugins.RxJavaPlugins

@HiltAndroidApp
@NavigationComponent
class ThanosApp : MultipleModulesApp(), NavigationApplication {
    override val navigationController = navigationController {
        plugin(EnroLogger())
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        AppGlobals.setContext(base)
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
        if (BuildConfig.DEBUG) {
            DeveloperDiag.diag(this)
        }
        Init.init(this)
        FeatureAccessStats.init(this)
        install()
    }

    companion object {
        @JvmStatic
        fun isPrc(): Boolean {
            return Init.isPrc()
        }
    }
}