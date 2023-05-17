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
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.pm.Pkg
import github.tornaco.android.thanos.core.secure.PrivacyManager
import io.github.libxposed.service.XposedService
import io.github.libxposed.service.XposedServiceHelper

object XposedScope {
    private var _service: XposedService? = null
    val service get() = _service

    inline fun withService(onService: (XposedService) -> Unit) {
        service?.let { onService(it) }
    }

    fun requestOrRemoveScope(context: Context, pkg: Pkg) {
        withService {
            if (hasEnabledFeatureDepOnScope(context, pkg)) {
                if (!it.scope.contains(pkg.pkgName)) {
                    XLog.i("XposedScope requestScope: $pkg")
                    it.requestScope(pkg.pkgName, object : XposedService.OnScopeEventListener {})
                } else {
                    XLog.i("XposedScope already scoped: $pkg")
                }
            } else {
                XLog.i("XposedScope removeScope: $pkg")
                it.removeScope(pkg.pkgName)
            }
        }
    }

    private fun hasEnabledFeatureDepOnScope(context: Context, pkg: Pkg): Boolean {
        val thanos = ThanosManager.from(context)

        val isDialogForceCancelable =
            thanos.hasFeature(BuildProp.THANOX_FEATURE_DIALOG_FORCE_CANCELABLE) && thanos.windowManager.isDialogForceCancelable(
                pkg.pkgName
            )

        val privMode = thanos.privacyManager.getSelectedFieldsProfileForPackage(
            pkg.pkgName,
            PrivacyManager.PrivacyOp.OP_NO_OP
        ) != null

        return isDialogForceCancelable || privMode
    }

    fun init() {
        XposedServiceHelper.registerListener(object : XposedServiceHelper.OnServiceListener {
            override fun onServiceBind(service: XposedService) {
                XLog.i("XposedScope onServiceBind: $service")
                _service = service
            }

            override fun onServiceDied(service: XposedService) {
                XLog.w("XposedScope onServiceDied: $service")
                _service = null
            }
        })
    }
}