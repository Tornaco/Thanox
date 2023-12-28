package github.tornaco.android.thanos.core.ops

import android.app.AppOpsManager
import com.elvishew.xlog.XLog
import github.tornaco.android.thanos.core.pm.Pkg

class OpsManager(private val ops: IOps) {
    // AppOpsManager._NUM_OP
    // it will inline.
    val opNum get() = 121

    companion object {

        const val MODE_ALLOWED = 0
        const val MODE_IGNORED = 1
        const val MODE_ERRORED = 2
        const val MODE_DEFAULT = 3
        const val MODE_FOREGROUND = 4

        private val MODE_NAMES = arrayOf(
            "allow",  // MODE_ALLOWED
            "ignore",  // MODE_IGNORED
            "deny",  // MODE_ERRORED
            "default",  // MODE_DEFAULT
            "foreground"
        )
        private val unsupportedOps = arrayOf(
            AppOpEnum.APP_OP_DEPRECATED_1,
            AppOpEnum.APP_OP_AUTO_REVOKE_PERMISSIONS_IF_UNUSED,
            AppOpEnum.APP_OP_AUTO_REVOKE_MANAGED_BY_INSTALLER,
            AppOpEnum.APP_OP_NO_ISOLATED_STORAGE,
            AppOpEnum.APP_OP_PHONE_CALL_CAMERA,
            AppOpEnum.APP_OP_PHONE_CALL_MICROPHONE,
            AppOpEnum.APP_OP_UWB_RANGING,
            AppOpEnum.APP_OP_INTERACT_ACROSS_PROFILES,
            AppOpEnum.APP_OP_LEGACY_STORAGE,
            AppOpEnum.APP_OP_MANAGE_IPSEC_TUNNELS,
            AppOpEnum.APP_OP_ACCEPT_HANDOVER,
            AppOpEnum.APP_OP_ACCESS_NOTIFICATIONS,
            AppOpEnum.APP_OP_RECEIVE_AMBIENT_TRIGGER_AUDIO,
            AppOpEnum.APP_OP_ESTABLISH_VPN_MANAGER,
            AppOpEnum.APP_OP_ESTABLISH_VPN_SERVICE,
            AppOpEnum.APP_OP_ACTIVATE_PLATFORM_VPN,
            AppOpEnum.APP_OP_ACTIVATE_VPN,
            AppOpEnum.APP_OP_RECORD_AUDIO_HOTWORD,
        )

        fun modeToName(mode: Int): String {
            return if (mode >= 0 && mode < MODE_NAMES.size) {
                MODE_NAMES[mode]
            } else "mode=$mode"
        }

        fun isOpSupported(mode: Int): Boolean {
            return !unsupportedOps.contains(mode)
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
    fun opToSwitch(code: Int): Int? = runCatching { AppOpsManager.opToSwitch(code) }.getOrElse {
        XLog.w("opToSwitch error: ${it.stackTraceToString()}")
        null
    }
}