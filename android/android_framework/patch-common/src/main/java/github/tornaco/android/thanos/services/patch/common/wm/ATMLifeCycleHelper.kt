package github.tornaco.android.thanos.services.patch.common.wm

import util.XposedHelpers

object ATMLifeCycleHelper {

    fun lifeCycleClass(classLoader: ClassLoader): Class<*> {
        return XposedHelpers.findClass(
            "com.android.server.wm.ActivityTaskManagerService\$Lifecycle",
            classLoader
        )
    }

    fun getService(lifecycle: Any): Any? {
        return XposedHelpers.callMethod(lifecycle, "getService")
    }
}