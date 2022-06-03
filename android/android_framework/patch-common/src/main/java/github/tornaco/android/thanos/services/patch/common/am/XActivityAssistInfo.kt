package github.tornaco.android.thanos.services.patch.common.am

import android.annotation.SuppressLint
import com.elvishew.xlog.XLog
import util.XposedHelpers

object XActivityAssistInfo {

    @JvmStatic
    fun ClassLoader.activityAssistInfoClass() = XposedHelpers.findClass(
        "com.android.server.wm.ActivityAssistInfo",
        this
    )

    @SuppressLint("PrivateApi")
    @JvmStatic
    fun getActivityToken(info: Any?): Any? {
        return if (info == null) null else try {
            XposedHelpers.callMethod(
                info,
                "getActivityToken"
            )
        } catch (e: Throwable) {
            XLog.e("XActivityAssistInfo#getActivityToken error", e)
            null
        }
    }
}