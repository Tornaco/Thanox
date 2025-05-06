package github.tornaco.android.thanos.services.patch.common.am

import android.annotation.SuppressLint
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
        return if (info == null) null else {
            kotlin.runCatching {
                XposedHelpers.callMethod(
                    info,
                    "getActivityToken"
                )
            }.getOrElse {
                // One UI 7 has no getter method
                // public final class ActivityAssistInfo {
                //    public final IBinder mActivityToken;
                //    public final IBinder mAssistToken;
                //    public final ComponentName mComponentName;
                //    public final int mTaskId;
                //    public final int mUserId;
                //
                //    public ActivityAssistInfo(ActivityRecord activityRecord) {
                //        this.mActivityToken = activityRecord.token;
                //        this.mAssistToken = activityRecord.assistToken;
                //        this.mTaskId = activityRecord.task.mTaskId;
                //        this.mComponentName = activityRecord.mActivityComponent;
                //        this.mUserId = activityRecord.mUserId;
                //    }
                //}
                XposedHelpers.getObjectField(info, "mActivityToken")
            }
        }
    }
}