package github.tornaco.android.thanos.services.patch.common.am

import android.annotation.SuppressLint
import android.content.Intent
import android.os.IBinder
import com.elvishew.xlog.XLog
import util.XposedHelpers
import util.XposedHelpersExt

object XActivityRecord {

    @JvmStatic
    fun ClassLoader.activityRecordClass() = XposedHelpersExt.anyClassFromNames(
        this, arrayOf(
            "com.android.server.wm.ActivityRecord",
            "com.android.server.am.ActivityRecord"
        )
    )

    // final ActivityRecord r = ActivityRecord.forTokenLocked(token);
    @SuppressLint("PrivateApi")
    @JvmStatic
    fun forTokenLocked(token: IBinder?, classLoader: ClassLoader?): Any? {
        return if (token == null) null else try {
            val clazz = XposedHelpersExt.anyClassFromNames(
                classLoader, arrayOf(
                    "com.android.server.wm.ActivityRecord",
                    "com.android.server.am.ActivityRecord"
                )
            )
            XposedHelpers.callStaticMethod(
                clazz,
                "forTokenLocked",
                token
            )
        } catch (e: Throwable) {
            XLog.e("ActivityRecordUtils#forTokenLocked error", e)
            null
        }
    }

    @JvmStatic
    fun getIntent(activityRecord: Any?): Intent? {
        return if (activityRecord == null) {
            null
        } else try {
            XposedHelpers.getObjectField(activityRecord, "intent") as Intent
        } catch (e: Throwable) {
            XLog.e("ActivityRecordUtils#getIntent error", e)
            null
        }
    }

    @JvmStatic
    fun getUid(activityRecord: Any?): Int {
        return if (activityRecord == null) {
            -1
        } else try {
            XposedHelpers.callMethod(activityRecord, "getUid") as Int
        } catch (e: Throwable) {
            XLog.e("ActivityRecordUtils#getUid error", e)
            -1
        }
    }
}