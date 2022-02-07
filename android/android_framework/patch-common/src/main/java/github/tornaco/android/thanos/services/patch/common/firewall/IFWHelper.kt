package github.tornaco.android.thanos.services.patch.common.firewall

import util.XposedHelpers

object IFWHelper {

    fun ifwClass(classLoader: ClassLoader): Class<*> {
        return XposedHelpers.findClass(
            "com.android.server.firewall.IntentFirewall",
            classLoader
        )
    }

    fun amsInterfaceClass(classLoader: ClassLoader): Class<*> {
        return XposedHelpers.findClass(
            "com.android.server.firewall.IntentFirewall\$AMSInterface",
            classLoader
        )
    }
}