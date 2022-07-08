package github.tornaco.android.thanos.services.patch.common.am

import com.elvishew.xlog.XLog
import github.tornaco.android.thanos.core.pm.Pkg
import github.tornaco.android.thanos.core.process.ProcessRecord
import github.tornaco.android.thanos.services.patch.common.am.XProcessRecordHelper.toPkg
import github.tornaco.android.thanos.services.patch.common.am.XProcessRecordHelper.toXProcessRecord
import util.XposedHelpers

object XAMS {

    @JvmStatic
    fun getProcessListAsObject(ams: Any): Any? {
        return kotlin.runCatching {
            // final ProcessList mProcessList;
            return XposedHelpers.getObjectField(ams, "mProcessList")
        }.getOrElse {
            XLog.e("XAMS, getProcessListAsObject error", it)
            null
        }
    }

    @JvmStatic
    fun getLruProcessList(processList: Any): List<ProcessRecord> {
        return getLruProcessListOrEmpty(processList) {
            it.toXProcessRecord()
        }
    }

    @JvmStatic
    fun getLruMainProcessList(processList: Any): List<ProcessRecord> {
        return getLruProcessListOrEmpty(processList) {
            it.toXProcessRecord()
        }.filter { it.isMainProcess }
    }

    @JvmStatic
    fun getLruProcessPackages(processList: Any): List<Pkg> {
        return getLruProcessListOrEmpty(processList) {
            it.toPkg()
        }
    }

    @JvmStatic
    fun getLruMainProcessPackages(processList: Any): List<Pkg> {
        return getLruMainProcessList(processList).map {
            Pkg.from(it.packageName, it.uid)
        }
    }

    @JvmStatic
    fun <T> getLruProcessListOrEmpty(processList: Any, transform: (Any) -> T): List<T> {
        //     @CompositeRWLock({"mService", "mProcLock"})
        //    private final ArrayList<ProcessRecord> mLruProcesses = new ArrayList<ProcessRecord>();
        return kotlin.runCatching {
            // final ProcessList mProcessList;
            val lru = XposedHelpers.getObjectField(processList, "mLruProcesses") as List<*>
            return lru.toList().mapNotNull { item ->
                item?.let { transform(it) }
            }
        }.getOrElse {
            XLog.e("XAMS, getLruProcessListOrNull error", it)
            emptyList()
        }
    }
}