package github.tornaco.android.thanos.services.patch.common.usage

import util.XposedHelpers

object UsageStatsManagerInternalHelper {

    fun usmInternalClass(classLoader: ClassLoader): Class<*> {
        return XposedHelpers.findClass("android.app.usage.UsageStatsManagerInternal", classLoader)
    }
}