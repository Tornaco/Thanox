package github.tornaco.android.thanos.services.patch.common.notification

import android.content.ComponentName
import com.elvishew.xlog.XLog
import util.XposedHelpers

object NMSHelper {
    fun nmsClass(classLoader: ClassLoader): Class<*> {
        return XposedHelpers.findClass(
            "com.android.server.notification.NotificationManagerService",
            classLoader
        )
    }

    fun notificationListenersClass(classLoader: ClassLoader): Class<*> {
        return XposedHelpers.findClass(
            "com.android.server.notification.NotificationManagerService\$NotificationListeners",
            classLoader
        )
    }

    // ManagedServiceInfo
    fun getComponentNameFromManagedServiceInfo(info: Any): ComponentName? {
        return kotlin.runCatching {
            XposedHelpers.getObjectField(info,
                "component") as ComponentName
        }.getOrElse {
            XLog.e("getComponentNameFromManagedServiceInfo error", it)
            null
        }
    }
}