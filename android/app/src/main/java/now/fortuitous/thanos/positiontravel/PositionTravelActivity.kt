package now.fortuitous.thanos.positiontravel

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity

@AndroidEntryPoint
class PositionTravelActivity : ComposeThemeActivity() {

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, PositionTravelActivity::class.java)
            context.startActivity(starter)
        }
    }

    @Composable
    override fun Content() {
        val viewModel: PositionTravelViewModel = hiltViewModel()
        PositionTravelScreen(viewModel = viewModel, onBack = { finish() })
    }
}
