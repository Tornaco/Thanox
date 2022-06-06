package github.tornaco.android.thanos.services.patch.common.wm

import android.annotation.SuppressLint
import android.view.WindowManager
import com.elvishew.xlog.XLog
import github.tornaco.android.thanos.core.wm.WindowState
import util.XposedHelpers


object XWindowState {

    @SuppressLint("PrivateApi")
    @JvmStatic
    fun getState(state: Any?): WindowState? {
        return if (state == null) null else try {
            val visible = XposedHelpers.callMethod(state, "isVisible") as Boolean
            val type = kotlin.runCatching {
                XposedHelpers.callMethod(state, "getWindowType") as Int
            }.getOrElse {
                kotlin.runCatching {
                    val lp =
                        XposedHelpers.getObjectField(state, "mAttrs") as WindowManager.LayoutParams
                    lp.type
                }.getOrElse {
                    XLog.w("XWindowState getWindowType, try to get from mAttrs error", it)
                    -1
                }
            }
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