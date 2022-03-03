package github.tornaco.android.thanos.power

import android.content.Context
import androidx.lifecycle.ViewModel
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.pm.PREBUILT_PACKAGE_SET_ID_ALL
import okhttp3.internal.toImmutableList

class SmartFreezeBottomNavViewModel : ViewModel() {
    private val _tabItems = mutableListOf<TabItem>()
    val tabItems get() = _tabItems.toImmutableList()

    fun getTabs(context: Context) {
        val items = with(ThanosManager.from(context)) {
            pkgManager.getAllPackageSets(false).filter {
                it.id == PREBUILT_PACKAGE_SET_ID_ALL || !it.isPrebuilt
            }.mapIndexed { index, pkgSet ->
                return@mapIndexed TabItem(index, pkgSet)
            }.sortedBy {
                it.pkgSet.isPrebuilt
            }
        }
        _tabItems.clear()
        _tabItems.addAll(items)
    }

}