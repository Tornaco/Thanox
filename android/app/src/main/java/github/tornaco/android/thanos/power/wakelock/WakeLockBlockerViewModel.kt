package github.tornaco.android.thanos.power.wakelock

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.elvishew.xlog.XLog
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.BuildProp
import github.tornaco.android.thanos.common.LifeCycleAwareViewModel
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.core.pm.PREBUILT_PACKAGE_SET_ID_3RD
import github.tornaco.android.thanos.core.pm.Pkg
import github.tornaco.android.thanos.module.compose.common.loader.AppSetFilterItem
import github.tornaco.android.thanos.module.compose.common.loader.Loader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.Collator
import java.util.*
import javax.inject.Inject

data class BlockerState(
    val isLoading: Boolean,
    val isWakeLockBlockerEnabled: Boolean,
    val packageStates: List<PackageState>,
    val appFilterItems: List<AppSetFilterItem>,
    val selectedAppSetFilterItem: AppSetFilterItem? = null
)

data class PackageState(val appInfo: AppInfo, val wakeLocks: List<WakeLockUiModel>) {
    val hasBlock get() = wakeLocks.any { it.isBlock }
    val isAllBlock get() = wakeLocks.all { it.isBlock }
    val hasHeld get() = wakeLocks.any { it.isHeld }
}

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class WakeLockBlockerViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    LifeCycleAwareViewModel() {
    private val _state =
        MutableStateFlow(
            BlockerState(
                isLoading = true,
                isWakeLockBlockerEnabled = false,
                packageStates = emptyList(),
                appFilterItems = emptyList(),
                selectedAppSetFilterItem = null
            )
        )
    val state = _state.asStateFlow()

    private val thanox by lazy { ThanosManager.from(context) }

    fun init() {
        viewModelScope.launch {
            loadBlockerState()
            loadDefaultAppFilterItems()
            loadPackageStates()
        }
    }

    fun refresh() {
        XLog.d("refresh")
        viewModelScope.launch {
            loadBlockerState()
            loadPackageStates()
        }
    }

    fun onFilterItemSelected(appSetFilterItem: AppSetFilterItem) {
        _state.value = _state.value.copy(selectedAppSetFilterItem = appSetFilterItem)
        viewModelScope.launch {
            loadPackageStates()
        }
    }

    private suspend fun loadDefaultAppFilterItems() {
        val appFilterListItems = Loader.loadAllFromAppSet(context)
        _state.value = _state.value.copy(
            // Default select 3-rd
            selectedAppSetFilterItem = appFilterListItems.find {
                it.id == PREBUILT_PACKAGE_SET_ID_3RD
            },
            appFilterItems = appFilterListItems
        )
    }

    private suspend fun loadPackageStates() = withContext(Dispatchers.IO) {
        _state.value = _state.value.copy(isLoading = true)

        val filterPackages = state.value.selectedAppSetFilterItem?.let {
            thanox.pkgManager.getPackageSetById(
                it.id,
                true
            ).pkgNames.filterNot { pkgName -> pkgName == BuildProp.THANOS_APP_PKG_NAME }
        } ?: emptyList()

        val whiteListPackages = thanox.pkgManager.whiteListPkgs
        val packageStates = thanox.powerManager.getSeenWakeLocks(true)
            .filterNotNull()
            .asSequence()
            .filterNot { whiteListPackages.contains(it.ownerPackageName) }
            .filter { filterPackages.contains(it.ownerPackageName) }
            .groupBy { Pkg(it.ownerPackageName, it.ownerUserId) }
            .mapNotNull { entry ->
                val appInfo =
                    thanox.pkgManager.getAppInfoForUser(entry.key.pkgName, entry.key.userId)
                appInfo?.let {
                    PackageState(appInfo, entry.value.map { it.toUiModel() })
                }
            }
            .sortedWith { o1, o2 ->
                Collator.getInstance(Locale.CHINESE)
                    .compare(o1.appInfo.appLabel, o2.appInfo.appLabel)
            }
            .toList()

        _state.value = _state.value.copy(isLoading = false, packageStates = packageStates)
    }

    private fun loadBlockerState() {
        _state.value =
            _state.value.copy(isWakeLockBlockerEnabled = thanox.powerManager.isWakeLockBlockerEnabled)
    }

    fun setWakeLockBlockerEnabled(enable: Boolean) {
        thanox.powerManager.isWakeLockBlockerEnabled = enable
        loadBlockerState()
    }

    fun setBlockWakeLock(model: WakeLockUiModel, block: Boolean) {
        thanox.powerManager.setBlockWakeLock(model.toWakeLock(), block)
        refresh()
    }

    fun batchSelect(packageState: PackageState) {
        val desiredBlockValue = !packageState.wakeLocks.all { it.isBlock }
        XLog.d("batchSelect, desiredBlockValue: $desiredBlockValue")

        packageState.wakeLocks.onEach {
            setBlockWakeLock(it, desiredBlockValue)
        }

        refresh()
    }
}