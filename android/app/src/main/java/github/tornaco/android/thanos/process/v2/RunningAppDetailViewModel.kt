package github.tornaco.android.thanos.process.v2

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import com.elvishew.xlog.XLog
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.common.LifeCycleAwareViewModel
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.util.ClipboardUtils
import github.tornaco.android.thanos.util.ToastUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class RunningAppDetailViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    LifeCycleAwareViewModel() {
    private val thanox by lazy { ThanosManager.from(context) }

    private val _state =
        MutableStateFlow(
            CpuUsageStatsState(emptyMap())
        )
    val state = _state.asStateFlow()

    val appStateChanged get() = _appStateChanged
    private var _appStateChanged: Boolean = false

    fun copyProcessName(state: RunningProcessState) {
        ClipboardUtils.copyToClipboard(context, "Process name", state.process.processName)
        ToastUtils.copiedToClipboard(context)
    }

    fun stopProcess(state: RunningProcessState): Boolean {
        return thanox.activityManager.killProcess(state.process.pid.toLong()).also {
            if (it) _appStateChanged = true
        }
    }

    fun copyServiceName(service: RunningService) {
        ClipboardUtils.copyToClipboard(
            context,
            "Service name",
            service.running.service.flattenToString()
        )
        ToastUtils.copiedToClipboard(context)
    }

    fun stopService(service: RunningService): Boolean {
        return thanox.activityManager.stopService(Intent().apply {
            component = service.running.service
            putExtra("uid", service.running.uid)
        }).also {
            if (it) _appStateChanged = true
        }
    }

    fun forceStop(runningAppState: RunningAppState) {
        thanox.activityManager.forceStopPackage(runningAppState.appInfo.pkgName)
    }

    suspend fun startQueryCpuUsage(runningAppState: RunningAppState) {
        XLog.w("startQueryCpuUsage...$runningAppState")
        val pids = runningAppState.processState.map { it.process.pid.toLong() }.toLongArray()
        while (true) {
            if (isResumed) {
                val queryProcessCpuUsageStats =
                    thanox.activityManager.queryProcessCpuUsageStats(pids, true)
                _state.value =
                    _state.value.copy(statsMap = queryProcessCpuUsageStats.associateBy { it.pid })
            }
            delay(1000)
        }
    }
}