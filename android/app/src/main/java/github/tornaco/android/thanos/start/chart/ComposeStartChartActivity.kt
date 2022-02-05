package github.tornaco.android.thanos.start.chart

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import com.google.android.material.composethemeadapter3.Mdc3Theme
import dagger.hilt.android.AndroidEntryPoint
import github.tornaco.android.thanos.apps.AppDetailsActivity
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.start.DetailedStartRecordsActivity
import github.tornaco.android.thanos.theme.ThemeActivity
import github.tornaco.android.thanos.util.ActivityUtils

@AndroidEntryPoint
class ComposeStartChartActivity : ThemeActivity() {

    object Starter {
        fun start(context: Context?) {
            ActivityUtils.startActivity(context,
                ComposeStartChartActivity::class.java)
        }
    }

    override fun isADVF(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Mdc3Theme {
                Surface {
                    StartChartScreen(
                        onBackPressed = {
                            finish()
                        }, onItemSelected = {
                            AppDetailsActivity.start(this,
                                ThanosManager.from(this).pkgManager.getAppInfo(
                                    it.data))
                        }, onCenterSelected = {
                            DetailedStartRecordsActivity.start(thisActivity(), null)
                        })
                }
            }
        }
    }
}