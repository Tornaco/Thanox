package github.tornaco.android.thanos.services.patch.common

import util.XposedHelpers

object SystemServiceManagerHelper {

    fun systemServiceManagerClass(classLoader: ClassLoader): Class<*> {
        return XposedHelpers.findClass("com.android.server.SystemServiceManager", classLoader)
    }
}