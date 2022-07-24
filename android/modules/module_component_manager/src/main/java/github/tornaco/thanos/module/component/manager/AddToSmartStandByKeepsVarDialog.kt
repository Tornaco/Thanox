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

package github.tornaco.thanos.module.component.manager

import android.content.ComponentName
import android.content.Context
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.util.ToastUtils
import github.tornaco.android.thanos.widget.ModernAlertDialog

class AddToSmartStandByKeepsVarDialog(val context: Context, val componentName: ComponentName) {
    private val thanox by lazy { ThanosManager.from(context) }

    fun show() {
        val rule = "KEEP ${componentName.flattenToString()}"

        val dialog = ModernAlertDialog(context)
        dialog.setDialogTitle(context.getString(R.string.module_component_manager_keep_service_smart_standby))
        dialog.setDialogMessage(rule)
        dialog.setCancelable(false)
        dialog.setPositive(context.getString(android.R.string.ok))
        dialog.setNegative(context.getString(android.R.string.cancel))

        dialog.setOnPositive {
            thanox.activityManager.addStandbyRule(rule)
            ToastUtils.ok(context)
        }
        dialog.show()
    }
}