package now.fortuitous.thanos.apps

import android.content.Context
import androidx.annotation.UiThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elvishew.xlog.XLog
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.core.pm.Pkg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import util.JsonFormatter
import java.io.OutputStream

class AppDetailsViewModel : ViewModel() {
    @SuppressWarnings("UnstableApiUsage")
    fun performComponentsBackup(
        context: Context,
        listener: BackupListener,
        os: OutputStream,
        app: AppInfo
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                runCatching {
                    val thanos = ThanosManager.from(context)
                    val disabledComponents: List<String> =
                        thanos.pkgManager.getAllDisabledComponentsForPackage(Pkg.fromAppInfo(app))
                            .map {
                                it.flattenToString()
                            }
                    val backup = AppComponentBackup(disabledComponents)

                    val json = JsonFormatter.toPrettyJson(backup)
                    XLog.d(
                        "performComponentsBackup, json: $json"
                    )
                    os.use {
                        it.write(json.toByteArray())
                        it.flush()
                    }
                    withContext(Dispatchers.Main) {
                        listener.onSuccess()
                    }
                }.onFailure {
                    XLog.e(it, "performComponentsBackup error")
                    withContext(Dispatchers.Main) {
                        listener.onFail(it.stackTraceToString())
                    }
                }
            }
        }
    }


    interface BackupListener {
        @UiThread
        fun onSuccess()

        @UiThread
        fun onFail(errMsg: String?)
    }


    interface RestoreListener {
        @UiThread
        fun onSuccess()

        @UiThread
        fun onFail(errMsg: String?)
    }

}