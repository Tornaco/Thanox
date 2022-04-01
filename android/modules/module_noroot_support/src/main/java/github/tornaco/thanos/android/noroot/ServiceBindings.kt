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

package github.tornaco.thanos.android.noroot

import android.content.ComponentName
import android.content.ServiceConnection
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.IBinder
import com.elvishew.xlog.XLog
import github.tornaco.android.thanos.BuildProp
import github.tornaco.android.thanos.core.IThanosProvider
import github.tornaco.android.thanos.core.app.ThanosManagerNative
import rikka.shizuku.Shizuku

object ServiceBindings {

    init {
        Shizuku.addRequestPermissionResultListener { code, res ->
            bindUserService()
        }
    }

    private val userServiceArgs = Shizuku.UserServiceArgs(
        ComponentName(BuildProp.THANOS_APP_PKG_NAME, ThanosProviderService::class.java.name)
    ).daemon(false)
        .processNameSuffix("noroot_service")
        .debuggable(BuildProp.THANOS_BUILD_DEBUG)
        .tag("Thanox-NoRoot-Support")


    private val userServiceConnection: ServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(componentName: ComponentName, binder: IBinder?) {
            val res = StringBuilder()
            res.append("onServiceConnected: ").append(componentName.className).append('\n')
            if (binder != null && binder.pingBinder()) {
                val service: IThanosProvider = IThanosProvider.Stub.asInterface(binder)
                ThanosManagerNative.setLocalService(service.thanos)
            } else {
                XLog.e("ServiceBindings, Invalid binder received")
            }
        }

        override fun onServiceDisconnected(componentName: ComponentName) {

        }
    }


    fun bindUserService() {
        kotlin.runCatching {
            Shizuku.bindUserService(userServiceArgs, userServiceConnection)
        }
    }

    fun unbindUserService() {
        kotlin.runCatching {
            Shizuku.unbindUserService(userServiceArgs, userServiceConnection, true)
        }
    }

    fun checkPermission(code: Int): Boolean {
        if (Shizuku.isPreV11()) {
            return false
        }
        return kotlin.runCatching {
            return when {
                Shizuku.checkSelfPermission() == PERMISSION_GRANTED -> {
                    true
                }
                Shizuku.shouldShowRequestPermissionRationale() -> {
                    XLog.e("User denied permission (shouldShowRequestPermissionRationale=true)")
                    false
                }
                else -> {
                    Shizuku.requestPermission(code)
                    false
                }
            }
        }.getOrElse { false }
    }
}