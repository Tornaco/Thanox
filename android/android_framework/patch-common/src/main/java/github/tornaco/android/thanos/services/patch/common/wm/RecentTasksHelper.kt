package github.tornaco.android.thanos.services.patch.common.wm

import util.XposedHelpers

object RecentTasksHelper {

    fun recentTaskCallbacksClass(classLoader: ClassLoader): Class<*> {
        return XposedHelpers.findClass("com.android.server.wm.RecentTasks\$Callbacks", classLoader)
    }
}