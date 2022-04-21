package github.tornaco.android.thanos.process.v2

import android.content.Context
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import github.tornaco.android.thanos.process.ProcessManageActivity
import github.tornaco.android.thanos.util.ActivityUtils

@AndroidEntryPoint
class ProcessManageActivityV2 : ComposeThemeActivity() {

    object Starter {
        fun start(context: Context?) {
            ActivityUtils.startActivity(context, ProcessManageActivityV2::class.java)
        }
    }

    override fun isF(): Boolean {
        return true
    }

    override fun isADVF(): Boolean {
        return true
    }

    @Composable
    override fun Content() {
        Surface {
            ProcessManageScreen(
                onBackPressed = {
                    finish()
                },
                toLegacyUi = {
                    ProcessManageActivity.start(thisActivity())
                })
        }
    }
}