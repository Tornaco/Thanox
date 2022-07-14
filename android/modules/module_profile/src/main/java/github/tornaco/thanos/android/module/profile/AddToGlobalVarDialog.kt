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

package github.tornaco.thanos.android.module.profile

import android.content.Context
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.util.ToastUtils
import github.tornaco.android.thanos.widget.ModernSingleChoiceDialog

class AddToGlobalVarDialog(val context: Context, val value: String) {
    private val thanox by lazy { ThanosManager.from(context) }

    fun show() {
        val allVars = thanox.profileManager.allGlobalRuleVar
        val dialog = ModernSingleChoiceDialog(context)
        dialog.setTitle(context.getString(R.string.module_profile_add_to_global_var))
        dialog.setTips(value)
        dialog.setItems(
            allVars.map {
                ModernSingleChoiceDialog.Item(it.name, it.name, null)
            }
        )
        dialog.setOnConfirm { id: String? ->
            id?.let {
                thanox.profileManager.appendGlobalRuleVar(it, arrayOf(value))
                ToastUtils.ok(context)
            }
        }
        dialog.show()
    }
}