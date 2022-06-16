package github.tornaco.android.thanos.services.patch.common.am

import android.content.pm.ApplicationInfo
import github.tornaco.android.thanos.core.pm.Pkg
import github.tornaco.android.thanos.core.process.ProcessRecord
import util.XposedHelpers
import util.XposedHelpersExt

object XProcessRecordHelper {

    @JvmStatic
    fun Any.toXProcessRecord(): ProcessRecord {
        val applicationInfo = XposedHelpers
            .getObjectField(this, "info") as ApplicationInfo
        val processName = XposedHelpers
            .getObjectField(this, "processName") as String
        val pid = XposedHelpersExt.getIntFieldWithPotentialNames(this, "pid", "mPid")
        val uid = XposedHelpersExt.getIntFieldWithPotentialNames(this, "uid", "mUid")
        return ProcessRecord(
            applicationInfo.packageName, processName, pid.toLong(), uid, false, false
        )
    }

    @JvmStatic
    fun Any.toPkg(): Pkg {
        val applicationInfo = XposedHelpers
            .getObjectField(this, "info") as ApplicationInfo
        val uid = XposedHelpersExt.getIntFieldWithPotentialNames(this, "uid", "mUid")
        return Pkg(applicationInfo.packageName, uid)
    }
}