package github.tornaco.android.thanox.module.notification.recorder.ui.stats

import android.content.Context
import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import github.tornaco.android.thanos.util.ActivityUtils

@AndroidEntryPoint
class StatsActivity : ComposeThemeActivity() {

    object Starter {
        fun start(context: Context?) {
            ActivityUtils.startActivity(context, StatsActivity::class.java)
        }
    }

    @Composable
    override fun Content() {
        StatsChartScreen(
            onBackPressed = { finish() },
            onItemSelected = {},
            onCenterSelected = {}
        )
    }
}