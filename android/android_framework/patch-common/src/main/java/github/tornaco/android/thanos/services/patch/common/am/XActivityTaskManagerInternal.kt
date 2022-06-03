package github.tornaco.android.thanos.services.patch.common.am

import android.annotation.SuppressLint
import com.elvishew.xlog.XLog
import util.XposedHelpers

object XActivityTaskManagerInternal {

    @JvmStatic
    fun ClassLoader.activityTaskManagerInternalClass() = XposedHelpers.findClass(
        "com.android.server.wm.ActivityTaskManagerInternal",
        this
    )

    @SuppressLint("PrivateApi")
    @JvmStatic
    fun getTopVisibleActivities(atm: Any?): Any? {
        return if (atm == null) null else try {
            XposedHelpers.callMethod(
                atm,
                "getTopVisibleActivities"
            )
        } catch (e: Throwable) {
            XLog.e("XActivityTaskManagerInternal#getTopVisibleActivities error", e)
            null
        }
    }
}