package github.tornaco.android.thanos.module.compose.common.infra

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dagger.hilt.android.AndroidEntryPoint
import github.tornaco.android.thanos.common.AppListModel
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity

@AndroidEntryPoint
abstract class BaseAppListFilterActivity : ComposeThemeActivity() {
    companion object {
        const val KEY_FEAT_ID = "feature.id"

        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, BaseAppListFilterActivity::class.java)
            context.startActivity(starter)
        }
    }

    @Composable
    override fun Content() {
        val featId = intent.getIntExtra(KEY_FEAT_ID, -1)
        val config = remember { getConfig(featId) }
        BaseAppListFilterContent(config)
    }

    abstract fun getConfig(featureId: Int): BaseAppListFilterContainerConfig
}

data class BaseAppListFilterContainerConfig(
    val title: (Context) -> String,
    val featureId: String,
    val loader: suspend (Context, pkgSetId: String) -> List<AppListModel>,
    val featureDescription: (Context) -> String? = { null }
)