package now.fortuitous.thanos.sf

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.core.Logger
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.core.pm.PackageSet
import github.tornaco.android.thanos.core.pm.Pkg
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import now.fortuitous.thanos.pref.AppPreference
import javax.inject.Inject
import javax.inject.Singleton

interface SFRepo {
    fun update()
    fun freezePkgListFlow(): Flow<List<AppInfo>>
    suspend fun addPkg(pkg: Pkg)
    suspend fun addPkgs(pkg: List<Pkg>)
    suspend fun removePkg(pkg: Pkg)
    fun isScreenOffEnabled(): Flow<Boolean>
    suspend fun setScreenOffEnabled(boolean: Boolean)
    fun isTaskRemovalEnabled(): Flow<Boolean>
    suspend fun setTaskRemovalEnabled(boolean: Boolean)

    fun pkgSetListFlow(): Flow<List<PackageSet>>
    suspend fun addPkgSet(label: String)
    suspend fun removePkgSet(id: String)
    suspend fun removePkgFromSet(id: String, pkgs: List<Pkg>)
    suspend fun renamePkgSet(id: String, newLabel: String)
    suspend fun sortPkgSet(id: String, sort: Int)
    suspend fun addPkgToSet(id: String, pkgs: List<Pkg>)
}

@Singleton
class SFRepoImpl @Inject constructor(@ApplicationContext val context: Context) : SFRepo {
    private val updateFlags = MutableStateFlow(1)
    private val logger = Logger("SFRepo")
    private val pm = ThanosManager.from(context).pkgManager

    override fun update() {
        updateFlags.update { it + 1 }
        logger.d("update")
    }

    override fun freezePkgListFlow(): Flow<List<AppInfo>> {
        return updateFlags.map {
            pm.smartFreezePkgs.mapNotNull {
                pm.getAppInfo(it)
            }
        }
    }

    override suspend fun addPkg(pkg: Pkg) {
        pm.setPkgSmartFreezeEnabled(pkg, true)
        update()
    }

    override suspend fun addPkgs(pkg: List<Pkg>) {
        pkg.forEach { addPkg(it) }
        update()
    }

    override suspend fun removePkg(pkg: Pkg) {
        pm.setPkgSmartFreezeEnabled(pkg, false)
        update()
    }

    override fun isScreenOffEnabled(): Flow<Boolean> {
        return flowOf(pm.isSmartFreezeScreenOffCheckEnabled)
    }

    override suspend fun setScreenOffEnabled(boolean: Boolean) {
        pm.isSmartFreezeScreenOffCheckEnabled = boolean
    }

    override fun isTaskRemovalEnabled(): Flow<Boolean> {
        return flowOf(true)
    }

    override suspend fun setTaskRemovalEnabled(boolean: Boolean) {
    }

    override fun pkgSetListFlow(): Flow<List<PackageSet>> {
        return updateFlags.map {
            pm.getAllPackageSets(true).sortedBy {
                AppPreference.getPkgSetSort(context, it)
            }
        }
    }

    override suspend fun addPkgSet(label: String) {
        pm.createPackageSet(label)
        update()
    }

    override suspend fun removePkgSet(id: String) {
        pm.removePackageSet(id)
        update()
    }

    override suspend fun removePkgFromSet(id: String, pkgs: List<Pkg>) {
        pkgs.forEach {
            pm.removeFromPackageSet(it, id)
        }
        update()
    }

    override suspend fun renamePkgSet(id: String, newLabel: String) {
        pm.updatePackageSetLabel(newLabel, id)
    }

    override suspend fun sortPkgSet(id: String, sort: Int) {
        AppPreference.setPkgSetSort(context, id, sort)
        update()
    }

    override suspend fun addPkgToSet(id: String, pkgs: List<Pkg>) {
        pkgs.forEach {
            pm.addToPackageSet(it, id)
        }
        update()
    }
}