package github.tornaco.android.thanos.services.patch.common.am

import util.XposedHelpers

object XSyncNotedAppOpHelper {

    @JvmStatic
    fun Any.toXSyncNotedAppOp(): SyncNotedAppOp {
        val packageName = (XposedHelpers.getObjectField(this, "mPackageName") ?: "") as String
        val tag = (XposedHelpers.getObjectField(this, "mAttributionTag") ?: "") as String
        val code = XposedHelpers.getObjectField(this, "mOpCode") as Int
        val mode = XposedHelpers.getObjectField(this, "mOpMode") as Int
        return SyncNotedAppOp(
            pkgName = packageName,
            attrTag = tag,
            code = code,
            mode = mode
        )
    }

    @JvmStatic
    fun Any.setOpCode(code: Int) {
        XposedHelpers.setObjectField(this, "mOpMode", code)
    }

    data class SyncNotedAppOp(
        val pkgName: String,
        val attrTag: String,
        val code: Int,
        val mode: Int,
    )
}