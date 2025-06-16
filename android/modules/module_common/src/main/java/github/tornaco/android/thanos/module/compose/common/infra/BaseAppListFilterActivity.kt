package github.tornaco.android.thanos.module.compose.common.infra

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dagger.hilt.android.AndroidEntryPoint
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
    val featureId: String,
    val appBarConfig: AppBarConfig,
    val appItemConfig: AppItemConfig,
    val featureDescription: (Context) -> String? = { null },
    val fabs: List<FabItemConfig> = emptyList(),
    val switchBarConfig: SwitchBarConfig? = null,
)

data class AppBarConfig(
    val title: (Context) -> String,
    val actions: (Context) -> List<AppBarAction> = { emptyList() }
) {
    data class AppBarAction(
        val title: String,
        val icon: Int,
        val onClick: () -> Unit,
    )
}

data class FabItemConfig(
    val title: (Context) -> String,
    val onClick: () -> Unit
)

data class SwitchBarConfig(
    val title: (Context, Boolean) -> String,
    val isChecked: Boolean,
    val onCheckChanged: (Boolean) -> Unit
)

data class AppItemConfig(
    val isCheckable: Boolean = false,
    val onCheckChanged: (AppUiModel, Boolean) -> Unit = { _, _ -> },
    val loader: suspend (Context, pkgSetId: String) -> List<AppUiModel>,
    val onAppClick: (AppUiModel) -> Unit = {},
)