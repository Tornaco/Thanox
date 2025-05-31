package github.tornaco.android.thanos.core.util

import android.content.Intent
import android.os.IBinder
import android.view.Display
import com.elvishew.xlog.XLog
import github.tornaco.android.thanos.core.os.Classes.activityRecordClass
import util.XposedHelpers
import util.XposedHelpersExt


object ActivityRecordUtils {
    // final ActivityRecord r = ActivityRecord.forTokenLocked(token);
    fun forTokenLocked(token: IBinder?, classLoader: ClassLoader): Any? {
        return if (token == null) null else try {
            val clazz = classLoader.activityRecordClass()
            XposedHelpers.callStaticMethod(
                clazz,
                "forTokenLocked",
                token
            )
        } catch (e: Throwable) {
            XLog.e(e, "ActivityRecordUtils#forTokenLocked error")
            null
        }
    }

    fun getIntent(activityRecord: Any?): Intent? {
        return if (activityRecord == null) {
            null
        } else try {
            XposedHelpers.getObjectField(activityRecord, "intent") as Intent
        } catch (e: Throwable) {
            XLog.e(e, "ActivityRecordUtils#getIntent error")
            null
        }
    }

    fun getUid(activityRecord: Any?): Int {
        return if (activityRecord == null) {
            -1
        } else try {
            XposedHelpers.callMethod(activityRecord, "getUid") as Int
        } catch (e: Throwable) {
            XLog.e(e, "ActivityRecordUtils#getUid error")
            -1
        }
    }

    fun getDisplayId(activityRecord: Any?): Int {
        return if (activityRecord == null) {
            Display.INVALID_DISPLAY
        } else try {
            XposedHelpers.callMethod(activityRecord, "getDisplayId") as Int
        } catch (e: Throwable) {
            XLog.e(e, "ActivityRecordUtils#getDisplayId error")
            Display.INVALID_DISPLAY
        }
    }


    // This method is useless, can not be used as check if Activity is shown.
    fun isVisible(activityRecord: Any?): Boolean {
        return if (OsUtils.isROrAbove()) {
            isVisibleAndroid11Plus(activityRecord)
        } else {
            nowVisibleAndroid10(activityRecord)
        }
    }

    // Since Android11
    private fun isVisibleAndroid11Plus(activityRecord: Any?): Boolean {
        return if (activityRecord == null) {
            false
        } else try {
            XposedHelpers.callMethod(activityRecord, "isVisible") as Boolean
        } catch (e: Throwable) {
            XLog.e(e, "ActivityRecordUtils#isVisible error")
            false
        }
    }

    // Android 10
    private fun nowVisibleAndroid10(activityRecord: Any?): Boolean {
        return if (activityRecord == null) {
            false
        } else try {
            XposedHelpers.getBooleanField(activityRecord, "nowVisible")
        } catch (e: Throwable) {
            XLog.e(e, "ActivityRecordUtils#nowVisible error")
            false
        }
    }

    fun getTaskRecord(activityRecord: Any?): Any? {
        if (activityRecord == null) return null
        return kotlin.runCatching {
            XposedHelpers.getObjectField(activityRecord, "task").also {
                XLog.d("getTaskRecord .task: $it")
            }
        }.getOrElse {
            kotlin.runCatching {
                XLog.e(it, "getTaskRecord err, fallback to use #getTask.")
                XposedHelpers.callMethod(activityRecord, "getTask")
            }.getOrElse {
                XLog.e(it, "getTaskRecord err, fallback to use #getTaskRecord.")
                XposedHelpers.callMethod(activityRecord, "getTaskRecord")
            }
        }
    }

    fun getTaskId(activityRecord: Any?): Int {
        if (activityRecord == null) return -1
        val task = getTaskRecord(activityRecord)
        return XposedHelpersExt.getIntFieldWithPotentialNames(task, "mTaskId", "taskId")
    }
}