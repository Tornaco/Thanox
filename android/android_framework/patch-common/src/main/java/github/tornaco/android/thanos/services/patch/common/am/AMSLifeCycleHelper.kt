package github.tornaco.android.thanos.services.patch.common.am

import util.XposedHelpers

object AMSLifeCycleHelper {

    fun lifeCycleClass(classLoader: ClassLoader): Class<*> {
        return XposedHelpers.findClass(
            "com.android.server.am.ActivityManagerService\$Lifecycle",
            classLoader
        )
    }

    fun getService(lifecycle: Any): Any? {
        return (kotlin.runCatching {
            XposedHelpers.callMethod(lifecycle, "getService")
        }.getOrNull()) ?: (kotlin.runCatching {
            XposedHelpers.getObjectField(lifecycle, "mService")
        }.getOrNull())
    }
}