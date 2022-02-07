package github.tornaco.android.thanos.services.patch.common

import util.XposedHelpers

object LocalServicesHelper {

    fun localServiceClass(classLoader: ClassLoader): Class<*> {
        return XposedHelpers.findClass("com.android.server.LocalServices", classLoader)
    }

    fun getService(serviceClass: Class<*>, classLoader: ClassLoader): Any? {
        val localServiceClass = localServiceClass(classLoader)
        return XposedHelpers.callStaticMethod(localServiceClass, "getService", serviceClass)
    }
}