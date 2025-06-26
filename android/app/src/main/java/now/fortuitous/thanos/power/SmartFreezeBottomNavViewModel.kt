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

package now.fortuitous.thanos.power

import android.content.Context
import androidx.lifecycle.ViewModel
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.pm.PREBUILT_PACKAGE_SET_ID_3RD
import github.tornaco.android.thanos.core.pm.PREBUILT_PACKAGE_SET_ID_ALL
import github.tornaco.android.thanos.core.pm.PREBUILT_PACKAGE_SET_ID_SYSTEM

class SmartFreezeBottomNavViewModel : ViewModel() {
    private val _tabItems = mutableListOf<TabItem>()
    val tabItems get() = _tabItems.toList()

    fun getTabs(context: Context) {
        val items = with(ThanosManager.from(context)) {
            val hasFreezedWhiteListPkg = this.pkgManager.hasFreezedPackageInUserWhiteListPkgSet()

            pkgManager.getAllPackageSets(false).filter {
                it.id == PREBUILT_PACKAGE_SET_ID_ALL
                        || it.id == PREBUILT_PACKAGE_SET_ID_3RD
                        || it.id == PREBUILT_PACKAGE_SET_ID_SYSTEM
                        || !it.isPrebuilt
            }.filter {
                hasFreezedWhiteListPkg || !it.isUserWhiteListed
            }.sortedBy { now.fortuitous.thanos.pref.AppPreference.getPkgSetSort(context, it) }
                .mapIndexed { index, pkgSet ->
                    return@mapIndexed TabItem(index, pkgSet)
                }
        }
        _tabItems.clear()
        _tabItems.addAll(items)
    }

    fun applySort(context: Context, items: List<TabItem>) {
        now.fortuitous.thanos.pref.AppPreference.setPkgSetSort(
            context,
            items.map { it.pkgSet.label })
    }
}