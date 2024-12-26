package github.tornaco.android.thanos.services.patch.common.am

import com.elvishew.xlog.XLog
import github.tornaco.android.thanos.core.pm.Pkg
import github.tornaco.android.thanos.core.process.ProcessRecord
import github.tornaco.android.thanos.core.util.OsUtils
import github.tornaco.android.thanos.services.patch.common.am.XProcessRecordHelper.toPkg
import github.tornaco.android.thanos.services.patch.common.am.XProcessRecordHelper.toXProcessRecord
import util.XposedHelpers

object XAMS {
    fun amsClass(classLoader: ClassLoader): Class<*> {
        return XposedHelpers.findClass(
            "com.android.server.am.ActivityManagerService",
            classLoader
        )
    }

    fun activeServicesClass(classLoader: ClassLoader): Class<*> {
        return XposedHelpers.findClass(
            "com.android.server.am.ActiveServices",
            classLoader
        )
    }

    @JvmStatic
    private fun getProcessListAsObject(ams: Any): Any? {
        return kotlin.runCatching {
            // final ProcessList mProcessList;
            return XposedHelpers.getObjectField(ams, "mProcessList")
        }.getOrElse {
            XLog.e("XAMS, getProcessListAsObject error", it)
            null
        }
    }

    @JvmStatic
    fun getLruProcessList(ams: Any): List<ProcessRecord> {
        return getLruProcessListOrEmpty(requireProcessListHost(ams)) {
            it.toXProcessRecord()
        }
    }

    @JvmStatic
    fun getLruMainProcessList(ams: Any): List<ProcessRecord> {
        return getLruProcessListOrEmpty(requireProcessListHost(ams)) {
            it.toXProcessRecord()
        }.filter { it.isMainProcess }
    }

    @JvmStatic
    fun getLruProcessPackages(ams: Any): List<Pkg> {
        return getLruProcessListOrEmpty(requireProcessListHost(ams)) {
            it.toPkg()
        }
    }

    @JvmStatic
    fun getLruMainProcessPackages(ams: Any): List<Pkg> {
        return getLruMainProcessList(ams).map {
            Pkg.from(it.packageName, it.uid)
        }
    }

    private fun requireProcessListHost(ams: Any): Any {
        val host = if (OsUtils.isQOrAbove()) {
            requireNotNull(getProcessListAsObject(ams)) { "Process list is null." }
        } else {
            ams
        }
        return host
    }

    @JvmStatic
    private fun <T> getLruProcessListOrEmpty(
        processListOrAMS: Any,
        transform: (Any) -> T
    ): List<T> {
        // Android 10+ In ProcessList
        // 9 in AMS.

        //     @CompositeRWLock({"mService", "mProcLock"})
        //    private final ArrayList<ProcessRecord> mLruProcesses = new ArrayList<ProcessRecord>();
        return kotlin.runCatching {
            // final ProcessList mProcessList;
            val lru = XposedHelpers.getObjectField(processListOrAMS, "mLruProcesses") as List<*>
            return lru.toList().mapNotNull { item ->
                item?.let { transform(it) }
            }
        }.getOrElse {
            XLog.e("XAMS, getLruProcessListOrNull error", it)
            emptyList()
        }
    }
}