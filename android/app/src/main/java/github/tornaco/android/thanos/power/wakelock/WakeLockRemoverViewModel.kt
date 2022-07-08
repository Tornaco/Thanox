package github.tornaco.android.thanos.power.wakelock

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.common.LifeCycleAwareViewModel
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.core.pm.Pkg
import github.tornaco.android.thanos.core.power.SeenWakeLock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.Collator
import java.util.*
import javax.inject.Inject

data class RemoverState(val isLoading: Boolean, val packageStates: List<PackageState>)

data class PackageState(val appInfo: AppInfo, val wakeLocks: List<SeenWakeLock>)

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class WakeLockRemoverViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    LifeCycleAwareViewModel() {
    private val _state =
        MutableStateFlow(RemoverState(isLoading = true, packageStates = emptyList()))
    val state = _state.asStateFlow()

    private val thanox by lazy { ThanosManager.from(context) }

    fun init() {
        refresh()
    }

    private suspend fun loadPackageStates() = withContext(Dispatchers.IO) {
        _state.value = _state.value.copy(isLoading = true)
        val packageStates = thanox.powerManager.getSeenWakeLocks(true)
            .groupBy { Pkg(it.ownerPackageName, it.ownerUserId) }
            .mapNotNull { entry ->
                val appInfo = thanox.pkgManager.getAppInfo(entry.key.pkgName)
                appInfo?.let {
                    PackageState(appInfo, entry.value)
                }
            }
            .sortedWith { o1, o2 ->
                Collator.getInstance(Locale.CHINESE)
                    .compare(o1.appInfo.appLabel, o2.appInfo.appLabel)
            }

        _state.value = _state.value.copy(isLoading = false, packageStates = packageStates)
    }

    fun refresh() {
        viewModelScope.launch {
            loadPackageStates()
        }
    }

}