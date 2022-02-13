package github.tornaco.android.thanos.services.patch.common.accessibility

import util.XposedHelpers

object AccessibilityManagerServiceHelper {

    fun accessibilityManagerServiceClass(classLoader: ClassLoader): Class<*> {
        return XposedHelpers.findClass(
            "com.android.server.accessibility.AccessibilityManagerService",
            classLoader
        )
    }

    fun lifeCycleClass(classLoader: ClassLoader): Class<*> {
        return XposedHelpers.findClass(
            "com.android.server.accessibility.AccessibilityManagerService\$Lifecycle",
            classLoader
        )
    }
}