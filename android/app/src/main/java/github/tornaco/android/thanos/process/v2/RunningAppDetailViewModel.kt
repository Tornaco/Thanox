package github.tornaco.android.thanos.process.v2

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.util.ClipboardUtils
import github.tornaco.android.thanos.util.ToastUtils
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class RunningAppDetailViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    ViewModel() {
    private val thanox by lazy { ThanosManager.from(context) }

    fun copyProcessName(state: RunningProcessState) {
        ClipboardUtils.copyToClipboard(context, "Process name", state.process.processName)
        ToastUtils.copiedToClipboard(context)
    }

    fun stopProcess(state: RunningProcessState) {
        ToastUtils.nook(context)
    }

    fun copyServiceName(service: RunningService) {
        ClipboardUtils.copyToClipboard(
            context,
            "Service name",
            service.running.service.flattenToString()
        )
        ToastUtils.copiedToClipboard(context)
    }

    fun stopService(service: RunningService) {
        thanox.activityManager.stopService(Intent().apply { component = service.running.service })
        ToastUtils.ok(context)
    }

    fun forceStop(runningAppState: RunningAppState) {
        thanox.activityManager.forceStopPackage(runningAppState.appInfo.pkgName)
    }
}