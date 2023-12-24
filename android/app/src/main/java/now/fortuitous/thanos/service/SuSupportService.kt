package now.fortuitous.thanos.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.annotation.Keep
import com.elvishew.xlog.XLog
import com.topjohnwu.superuser.Shell
import github.tornaco.android.thanos.core.app.ThanosManagerNative
import github.tornaco.android.thanos.core.su.ISu
import github.tornaco.android.thanos.core.su.SuRes


@Keep
class SuSupportService : Service() {
    companion object {
        init {
            XLog.w("Init shell")
            Shell.enableVerboseLogging = true
            Shell.setDefaultBuilder(
                Shell.Builder.create()
                    .setFlags(Shell.FLAG_REDIRECT_STDERR or Shell.FLAG_MOUNT_MASTER)
                    .setTimeout(10)
            )
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return object : ISu.Stub() {
            override fun exe(command: Array<out String>): SuRes {
                val customCommand: String? = ThanosManagerNative
                    .getDefault()
                    .profileManager
                    .customSuCommand
                if (!customCommand.isNullOrEmpty()) {
                    XLog.w("Execute shell with custom command: $customCommand")
                    val shell = com.topjohnwu.superuser.fallback.Shell.newInstance(customCommand)
                    val result = shell.newJob().add(*command).exec()
                    XLog.w(
                        "Custom SuSupportService, su: %s, exe: %s, result: %s",
                        customCommand,
                        command.contentToString(),
                        result
                    )
                    kotlin.runCatching { shell.waitAndClose() }
                        .onFailure { XLog.e(it, "Shell close err") }
                    return result.toSuRes()
                } else {
                    XLog.w("Execute shell with default command")
                    return Shell.cmd(*command)
                        .exec().apply {
                            XLog.w("Shell res: out: ${this.out} err: ${this.err}")
                        }.toSuRes()
                }
            }
        }
    }
}

fun Shell.Result.toSuRes() = SuRes(this.out, this.err, this.code)
fun com.topjohnwu.superuser.fallback.Shell.Result.toSuRes() = SuRes(this.out, this.err, this.code)