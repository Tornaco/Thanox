package github.tornaco.android.thanos.process.v2

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.text.format.Formatter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elvishew.xlog.XLog
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.core.app.ThanosManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class ProcessManageViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    ViewModel() {
    private val _state = MutableStateFlow(ProcessManageState(true, emptyList(), emptyList()))
    val state = _state.asStateFlow()

    private val thanox by lazy { ThanosManager.from(context) }

    fun startLoading() {
        viewModelScope.launch {
            loadProcessStates()
        }
    }

    private suspend fun loadProcessStates() = withContext(Dispatchers.IO) {
        updateLoadingState(true)

        val runningServices = thanox.activityManager.getRunningServiceLegacy(Int.MAX_VALUE)
        val runningAppProcess = thanox.activityManager.runningAppProcessLegacy
        val runningAppStates = runningAppProcess.groupBy { it.pkgList[0] }.map { entry ->
            val pkgName = entry.key
            val runningProcessStates = entry.value.map { process ->
                RunningProcessState(process = process,
                    runningServices = runningServices.filter { service ->
                        service.pid == process.pid
                    })
            }
            val isAllProcessCached =
                entry.value.all { it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_CACHED }
            val totalPss =
                thanox.activityManager.getProcessPss(entry.value.map { it.pid }.toIntArray()).sum()
            RunningAppState(
                appInfo = thanox.pkgManager.getAppInfo(pkgName),
                processState = runningProcessStates,
                allProcessIsCached = isAllProcessCached,
                totalPss = totalPss,
                sizeStr = Formatter.formatShortFileSize(context, totalPss * 1024)
            )
        }.sortedByDescending { it.totalPss }

        val runningAppStatesGroupByCached = runningAppStates.groupBy { it.allProcessIsCached }
        XLog.d("startLoading: %s", runningAppStatesGroupByCached)

        _state.value = _state.value.copy(
            isLoading = false,
            runningAppStates = runningAppStatesGroupByCached[false] ?: emptyList(),
            runningAppStatesBg = runningAppStatesGroupByCached[true] ?: emptyList()
        )
    }

    private fun updateLoadingState(isLoading: Boolean) {
        _state.value = _state.value.copy(isLoading = isLoading)
    }
}