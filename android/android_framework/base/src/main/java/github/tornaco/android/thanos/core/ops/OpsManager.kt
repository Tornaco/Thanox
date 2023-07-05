package github.tornaco.android.thanos.core.ops

import android.app.AppOpsManager
import github.tornaco.android.thanos.core.pm.Pkg

fun Int.isAllowed() = this == AppOpsManager.MODE_ALLOWED || this == AppOpsManager.MODE_FOREGROUND


class OpsManager(private val ops: IOps) {
    val opNum get() = AppOpsManager._NUM_OP

    companion object {
        private val MODE_NAMES = arrayOf(
            "allow",  // MODE_ALLOWED
            "ignore",  // MODE_IGNORED
            "deny",  // MODE_ERRORED
            "default",  // MODE_DEFAULT
            "foreground"
        )

        const val MODE_ALLOWED = 0
        const val MODE_IGNORED = 1
        const val MODE_ERRORED = 2
        const val MODE_DEFAULT = 3
        const val MODE_FOREGROUND = 4

        fun modeToName(mode: Int): String {
            return if (mode >= 0 && mode < MODE_NAMES.size) {
                MODE_NAMES[mode]
            } else "mode=$mode"
        }
    }

    fun setMode(code: Int, pkg: Pkg, mode: Int) {
        ops.setMode(code, pkg, mode)
    }

    fun getMode(code: Int, pkg: Pkg): Int {
        return ops.getMode(code, pkg)
    }

    fun opToName(code: Int): String = ops.opToName(code)
    fun opToPermission(code: Int): String? = ops.opToPermission(code)


}