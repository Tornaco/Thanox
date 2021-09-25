package github.tornaco.android.thanos.services.patch.common.wm

import util.XposedHelpersExt

object XActivityStackSupervisor {

    @JvmStatic
    fun supervisorClass(classLoader: ClassLoader): Class<*> = XposedHelpersExt.anyClassFromNames(
        classLoader,
        "com.android.server.am.ActivityStackSupervisor",
        "com.android.server.wm.ActivityStackSupervisor",
        // Android S emulator named.
        "com.android.server.wm.ActivityTaskSupervisor"
    )
}