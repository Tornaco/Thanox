package github.tornaco.android.thanos.module.compose.common.infra

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import github.tornaco.android.thanos.common.AppListModel
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity

abstract class BaseAppListFilterActivity : ComposeThemeActivity() {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, BaseAppListFilterActivity::class.java)
            context.startActivity(starter)
        }
    }

    @Composable
    override fun Content() {
        BaseAppListFilterContent()
    }

    abstract val config: BaseAppListFilterContainerConfig
}

data class BaseAppListFilterContainerConfig(
    val title: String,
    val featureId: String,
    val featureDescription: String?,
    val loader: suspend () -> List<AppListModel>
)