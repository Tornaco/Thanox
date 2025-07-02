package github.tornaco.android.thanos.main

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.elvishew.xlog.XLog
import github.tornaco.android.thanos.core.app.ThanosManager
import now.fortuitous.thanos.main.AppType
import now.fortuitous.thanos.main.ChooserActivity
import now.fortuitous.thanos.main.ThanoxShizuku
import now.fortuitous.thanos.main.ThanoxXposed
import now.fortuitous.thanos.pref.AppPreference

@Composable
fun Activity.NavEntry() {
    // Check if xopsed mode is running.
    if (ThanosManager.from(this).isServiceInstalled) {
        XLog.w("isServiceInstalled.")
        ThanoxXposed()
    } else {
        XLog.w("isServiceInstalled false.")
        val typeValue = AppPreference.getAppType(this)
        XLog.w("typeValue: $typeValue")
        when (typeValue) {
            AppType.BasedOnShizuku.prefValue -> {
                ThanoxShizuku()
            }

            AppType.BasedOnXposed.prefValue -> {
                ThanoxXposed()
            }

            else -> {
                SideEffect {
                    ChooserActivity.Starter.start(this)
                    finish()
                }
            }
        }
    }
}