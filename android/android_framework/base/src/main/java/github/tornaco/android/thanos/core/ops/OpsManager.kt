package github.tornaco.android.thanos.core.ops

import android.app.AppOpsManager

class OpsManager(private val ops: IOps) {
    val opNum get() = AppOpsManager._NUM_OP


}