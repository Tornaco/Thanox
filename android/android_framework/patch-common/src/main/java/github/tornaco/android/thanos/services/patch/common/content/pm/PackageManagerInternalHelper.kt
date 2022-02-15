package github.tornaco.android.thanos.services.patch.common.content.pm

import util.XposedHelpers

object PackageManagerInternalHelper {

    fun packageManagerInternalClass(classLoader: ClassLoader): Class<*> {
        return XposedHelpers.findClass(
            "android.content.pm.PackageManagerInternal",
            classLoader
        )
    }
}