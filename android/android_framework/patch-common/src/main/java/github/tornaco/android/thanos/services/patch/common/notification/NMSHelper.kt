package github.tornaco.android.thanos.services.patch.common.notification

import util.XposedHelpers

object NMSHelper {
    fun nmsClass(classLoader: ClassLoader): Class<*> {
        return XposedHelpers.findClass(
            "com.android.server.notification.NotificationManagerService",
            classLoader
        )
    }
}