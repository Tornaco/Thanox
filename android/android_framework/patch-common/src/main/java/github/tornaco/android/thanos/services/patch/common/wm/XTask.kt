package github.tornaco.android.thanos.services.patch.common.wm

import android.content.Intent
import util.XposedHelpers
import util.XposedHelpersExt

data class XTask(val intent: Intent, val taskId: Int, val userId: Int)

object XTaskHelper {

    @JvmStatic
    fun Any.toXTask(): XTask {
        val intent = XposedHelpers.getObjectField(this, "intent") as Intent
        // TaskRecord class has merge with Task class since R.
        val userId = XposedHelpersExt.getIntFieldWithPotentialNames(this, "mUserId", "userId")
        val taskId = XposedHelpersExt.getIntFieldWithPotentialNames(this, "mTaskId", "taskId")
        return XTask(intent, taskId, userId)
    }
}

