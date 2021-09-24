package github.tornaco.android.thanos.services.patch.common.wm

import android.content.Intent
import github.tornaco.android.thanos.core.util.OsUtils
import util.XposedHelpers

data class XTask(val intent: Intent, val taskId: Int, val userId: Int)

object XTaskHelper {

    @JvmStatic
    fun Any.toXTask(): XTask {
        val intent = XposedHelpers.getObjectField(this, "intent") as Intent
        // TaskRecord class has merge with Task class since R.
        val userIdFieldName = if (OsUtils.isROrAbove()) "mUserId" else "userId"
        val userId = XposedHelpers.getIntField(this, userIdFieldName)

        val taskIdFieldName = if (OsUtils.isROrAbove()) "mTaskId" else "taskId"
        val taskId = XposedHelpers.getIntField(this, taskIdFieldName)

        return XTask(intent, taskId, userId)
    }
}

