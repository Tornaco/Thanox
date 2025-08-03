@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package now.fortuitous.thanos.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.elvishew.xlog.XLog
import github.tornaco.android.thanos.module.compose.common.LocalActivity
import github.tornaco.android.thanos.module.compose.common.widget.CommonDialog
import github.tornaco.android.thanos.module.compose.common.widget.LinkText
import github.tornaco.android.thanos.module.compose.common.widget.rememberCommonDialogState
import github.tornaco.android.thanos.support.FeatureGrid

@Composable
fun SuggestedFeatEntries() {
    val activity = runCatching {
        LocalActivity.current
    }.getOrElse {
        XLog.e("No LocalActivity..${Throwable().stackTraceToString()}")
        null
    }
    if (activity != null) {
        val viewModel = hiltViewModel<NavViewModel2>()
        LaunchedEffect(viewModel) {
            viewModel.loadCoreStatus()
            viewModel.loadFeatures()
            viewModel.loadAppStatus()
            viewModel.loadHeaderStatus()
            viewModel.autoRefresh()
        }

        val state by viewModel.state.collectAsState()
        val featLauncher = remember { PrebuiltFeatureLauncher(activity) {} }
        val dialogState = rememberCommonDialogState()

        Column(Modifier.padding(16.dp)) {
            LinkText("All Features") {
                dialogState.show()
            }
        }
        CommonDialog(dialogState) {
            Column(
                Modifier
                    .fillMaxHeight(0.68f)
                    .verticalScroll(rememberScrollState())
            ) {
                FeatureGrid(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .heightIn(max = 1000.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.Center
                ) {
                    items(state.features.flatMap { it.items }) {
                        FeatureItem(item = it, onItemClick = {
                            featLauncher.launch(it.id)
                        }, onItemLongClick = {})
                    }
                }
            }
        }
    }
}