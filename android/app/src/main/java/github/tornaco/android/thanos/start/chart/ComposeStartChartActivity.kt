package github.tornaco.android.thanos.start.chart

import android.content.Context
import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import github.tornaco.android.thanos.apps.AppDetailsActivity
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import github.tornaco.android.thanos.start.DetailedStartRecordsActivity
import github.tornaco.android.thanos.util.ActivityUtils

@AndroidEntryPoint
class ComposeStartChartActivity : ComposeThemeActivity() {

    object Starter {
        fun start(context: Context?) {
            ActivityUtils.startActivity(
                context,
                ComposeStartChartActivity::class.java
            )
        }
    }

    override fun isADVF(): Boolean {
        return true
    }

    @Composable
    override fun Content() {
        StartChartScreen(
            onBackPressed = {
                finish()
            }, onItemSelected = {
                AppDetailsActivity.start(
                    this,
                    ThanosManager.from(this).pkgManager.getAppInfo(
                        it.data
                    )
                )
            }, onCenterSelected = {
                DetailedStartRecordsActivity.start(thisActivity(), null)
            })
    }
}