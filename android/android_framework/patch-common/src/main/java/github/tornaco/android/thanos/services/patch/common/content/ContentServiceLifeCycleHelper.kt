package github.tornaco.android.thanos.services.patch.common.content

import util.XposedHelpers

object ContentServiceLifeCycleHelper {

    fun lifeCycleClass(classLoader: ClassLoader): Class<*> {
        return XposedHelpers.findClass(
            "com.android.server.content.ContentService\$Lifecycle",
            classLoader
        )
    }

    fun getService(lifecycle: Any): Any? {
        return XposedHelpers.callMethod(lifecycle, "getService")
    }
}