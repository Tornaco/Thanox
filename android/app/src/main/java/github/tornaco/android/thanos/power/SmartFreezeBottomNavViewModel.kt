package github.tornaco.android.thanos.power

import android.content.Context
import androidx.lifecycle.ViewModel
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.pm.PREBUILT_PACKAGE_SET_ID_ALL
import github.tornaco.android.thanos.core.pm.PackageSet
import okhttp3.internal.toImmutableList

class SmartFreezeBottomNavViewModel : ViewModel() {
    private val _tabItems = mutableListOf<TabItem>()
    val tabItems get() = _tabItems.toImmutableList()

    fun getTabs(context: Context) {
        val items = with(ThanosManager.from(context)) {
            val smartFreezePkgs = pkgManager.smartFreezePkgs
            val smartFreezePkgNames = smartFreezePkgs.map { it.pkgName }
            pkgManager.getAllPackageSets(false).filter {
                it.id == PREBUILT_PACKAGE_SET_ID_ALL || !it.isPrebuilt
            }.sortedWith(packageSetComparator(smartFreezePkgNames)).mapIndexed { index, pkgSet ->
                return@mapIndexed TabItem(index, pkgSet)
            }
        }
        _tabItems.clear()
        _tabItems.addAll(items)
    }

    private fun packageSetComparator(smartFreezePkgNames: List<String>): java.util.Comparator<PackageSet> =
        Comparator { o1, o2 ->
            val o1HasPkg =
                o1.isPrebuilt || o1.pkgNames.find { pkgName -> smartFreezePkgNames.contains(pkgName) } != null
            val o2HasPkg =
                o2.isPrebuilt || o2.pkgNames.find { pkgName -> smartFreezePkgNames.contains(pkgName) } != null

            var scoreO1 = 0
            if (o1HasPkg) scoreO1 += 2
            if (!o1.isPrebuilt) scoreO1 += 1

            var scoreO2 = 0
            if (o2HasPkg) scoreO2 += 2
            if (!o2.isPrebuilt) scoreO2 += 1

            return@Comparator -scoreO1.compareTo(scoreO2)
        }

}