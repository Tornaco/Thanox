package github.tornaco.android.thanos.power

import android.content.Context
import androidx.lifecycle.ViewModel
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.pm.PREBUILT_PACKAGE_SET_ID_3RD
import github.tornaco.android.thanos.core.pm.PREBUILT_PACKAGE_SET_ID_ALL
import github.tornaco.android.thanos.core.pm.PREBUILT_PACKAGE_SET_ID_SYSTEM
import github.tornaco.android.thanos.pref.AppPreference
import okhttp3.internal.toImmutableList

class SmartFreezeBottomNavViewModel : ViewModel() {
    private val _tabItems = mutableListOf<TabItem>()
    val tabItems get() = _tabItems.toImmutableList()

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
            }.sortedBy { AppPreference.getPkgSetSort(context, it) }.mapIndexed { index, pkgSet ->
                return@mapIndexed TabItem(index, pkgSet)
            }
        }
        _tabItems.clear()
        _tabItems.addAll(items)
    }

    fun applySort(context: Context, items: List<TabItem>) {
        AppPreference.setPkgSetSort(context, items.map { it.pkgSet })
    }
}