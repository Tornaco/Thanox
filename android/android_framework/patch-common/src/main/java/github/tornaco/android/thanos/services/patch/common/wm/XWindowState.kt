package github.tornaco.android.thanos.services.patch.common.wm

import android.annotation.SuppressLint
import com.elvishew.xlog.XLog
import github.tornaco.android.thanos.core.wm.WindowState
import util.XposedHelpers


object XWindowState {

    @SuppressLint("PrivateApi")
    @JvmStatic
    fun getState(state: Any?): WindowState? {
        return if (state == null) null else try {
            val visible = XposedHelpers.callMethod(state, "isVisible") as Boolean
            val type = XposedHelpers.callMethod(state, "getWindowType") as Int
            val uid = XposedHelpers.callMethod(state, "getOwningUid") as Int
            val ownPackageName =
                XposedHelpers.callMethod(state, "getOwningPackage") as String
            WindowState(
                ownPackageName,
                uid,
                visible,
                type
            )
        } catch (e: Throwable) {
            XLog.e("XWindowState#getState error", e)
            null
        }
    }
}