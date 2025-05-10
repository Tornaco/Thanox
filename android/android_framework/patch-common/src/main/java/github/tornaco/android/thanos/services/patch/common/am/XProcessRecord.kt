package github.tornaco.android.thanos.services.patch.common.am

import android.content.pm.ApplicationInfo
import com.elvishew.xlog.XLog
import github.tornaco.android.thanos.core.pm.Pkg
import github.tornaco.android.thanos.core.process.ProcessRecord
import util.XposedHelpers
import util.XposedHelpersExt

object XProcessRecordHelper {
    // mState SDK 33
    private var hasProcessStateRecordField: Boolean? = null

    // int maxAdj SDK <33
    private var hasADJFields: Boolean? = null

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
        return Pkg.from(applicationInfo.packageName, uid)
    }

    @JvmStatic
    fun Any.setOOMADJ(adj: Int) {
        val hasProcessStateRecordField = detectIfHasProcessStateRecordField(record = this)
        val hasADJFields = detectIfHasADJFields(record = this)

        if (hasProcessStateRecordField) {
            val processStateRecord = XposedHelpers.getObjectField(this, "mState")
            kotlin.runCatching {
                XposedHelpers.callMethod(processStateRecord, "setMaxAdj", adj)
            }.onFailure {
                // OneUI 7
                XposedHelpers.setIntField(processStateRecord, "mMaxAdj", adj)
            }
            XposedHelpers.callMethod(processStateRecord, "setCurRawAdj", adj)
            runCatching {
                XposedHelpers.callMethod(processStateRecord, "setSetRawAdj", adj)
            }.onFailure {
                // OneUI 7
                XposedHelpers.setIntField(processStateRecord, "mSetRawAdj", adj)
            }
            XposedHelpers.callMethod(processStateRecord, "setCurAdj", adj)
            runCatching {
                XposedHelpers.callMethod(processStateRecord, "setSetAdj", adj)
            }.onFailure {
                // OneUI 7
                XposedHelpers.setIntField(processStateRecord, "mSetAdj", adj)
            }
            XLog.d("setOOMADJ - ProcessStateRecordField - $this - $adj")
        } else if (hasADJFields) {
            XposedHelpers.setIntField(this, "maxAdj", adj)
            XposedHelpers.setIntField(this, "mCurRawAdj", adj)
            XposedHelpers.setIntField(this, "setRawAdj", adj)
            XposedHelpers.setIntField(this, "curAdj", adj)
            XposedHelpers.setIntField(this, "setAdj", adj)
            XLog.d("setOOMADJ - ADJFields - $this - $adj")
        } else {
            XLog.w("No ADJ fields or ProcessStateRecordField")
        }
    }

    private fun detectIfHasProcessStateRecordField(record: Any): Boolean {
        if (hasProcessStateRecordField == null) {
            val processStateRecord: Any? = runCatching {
                XposedHelpers.getObjectField(record, "mState")
            }.getOrNull()
            hasProcessStateRecordField =
                processStateRecord != null && processStateRecord.javaClass.simpleName == "ProcessStateRecord"
            XLog.w("processStateRecord = $processStateRecord ${processStateRecord?.javaClass?.simpleName} $hasProcessStateRecordField")
        }
        return hasProcessStateRecordField == true
    }

    private fun detectIfHasADJFields(record: Any): Boolean {
        if (hasADJFields == null) {
            val maxAdj: Int? = runCatching {
                XposedHelpers.getObjectField(record, "maxAdj") as Int
            }.getOrNull()
            hasADJFields = maxAdj != null
        }
        return hasADJFields == true
    }
}