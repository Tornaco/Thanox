package github.tornaco.android.thanos

import android.app.Application
import com.elvishew.xlog.XLog
import kotlinx.coroutines.*

// Scoped type. Same instance is always returned
private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

fun Application.installCrashHandler() {
    applicationScope.launch {
        install()
    }
}

suspend fun install() =
    withContext(Dispatchers.IO) {
        XLog.w("No crash handler installed.")
    }