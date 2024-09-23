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

package now.fortuitous.thanos.apps

import android.content.Context
import android.content.DialogInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.pm.PackageSet
import github.tornaco.android.thanos.core.pm.Pkg

class PackageSetChooserDialog(
    private val context: Context,
    private val targetPackage: Pkg,
    private val onDismiss: (isChanged: Boolean) -> Unit
) {

    fun show() {
        val all = loadAll()
        val items = all.map { it.label }.toTypedArray()
        val checked = all.map { it.pkgList.contains(targetPackage) }.toBooleanArray()
        val toCheckItemsId = mutableSetOf<String>()
        val toRemoveItemsId = mutableSetOf<String>()

        MaterialAlertDialogBuilder(context)
            .setTitle(github.tornaco.android.thanos.res.R.string.title_package_sets)
            .setMultiChoiceItems(
                items, checked
            ) { _, which, isChecked ->
                val pkgSetId = all[which].id
                if (isChecked) toCheckItemsId.add(pkgSetId) else toRemoveItemsId.add(pkgSetId)
            }
            .setNegativeButton(android.R.string.cancel) { _: DialogInterface, _: Int -> }
            .setPositiveButton(android.R.string.ok) { _: DialogInterface, _: Int ->
                toCheckItemsId.forEach { checkedId ->
                    val thanos = ThanosManager.from(context)
                    thanos.pkgManager.addToPackageSet(targetPackage, checkedId)
                }

                toRemoveItemsId.forEach { checkedId ->
                    val thanos = ThanosManager.from(context)
                    thanos.pkgManager.removeFromPackageSet(targetPackage, checkedId)
                }
            }.setCancelable(false)
            .setOnDismissListener {
                onDismiss(toCheckItemsId.isNotEmpty() || toRemoveItemsId.isNotEmpty())
            }
            .show()
    }

    private fun loadAll(): List<PackageSet> {
        val thanos = ThanosManager.from(context)
        if (!thanos.isServiceInstalled) {
            return emptyList()
        }
        val pm = thanos.pkgManager
        return pm.getAllPackageSets(true).filter { !it.isPrebuilt }
    }

}