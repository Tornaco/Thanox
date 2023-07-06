package github.tornaco.android.thanos.core.ops

import github.tornaco.android.thanos.core.pm.Pkg

class OpsManager(private val ops: IOps) {
    // AppOpsManager._NUM_OP
    // it will inline.
    val opNum get() = 121

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

    fun setMode(code: Int, pkg: Pkg, permStateName: String) {
        ops.setMode(code, pkg, permStateName)
    }

    fun getPackagePermInfo(code: Int, pkg: Pkg): PermInfo? {
        return ops.getPackagePermInfo(code, pkg)
    }

    fun opToName(code: Int): String = ops.opToName(code)
    fun opToPermission(code: Int): String? = ops.opToPermission(code)
    fun getPermissionFlags(permName: String, pkg: Pkg): Int = ops.getPermissionFlags(permName, pkg)
    fun permissionFlagToString(flag: Int): String = ops.permissionFlagToString(flag)
}