package github.tornaco.thanos.android.ops2

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Switch
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxSmallAppBarScaffold

@AndroidEntryPoint
class Ops2Activity : ComposeThemeActivity() {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, Ops2Activity::class.java)
            context.startActivity(starter)
        }
    }

    @Composable
    override fun Content() {

        ThanoxSmallAppBarScaffold(
            title = {
                androidx.compose.material3.Text(
                    text = "Ops",
                    style = TypographyDefaults.appBarTitleTextStyle()
                )
            },
            onBackPressed = {},
            actions = {

            }
        ) { paddings ->
            val viewModel = hiltViewModel<OpsViewModel>()
            val state by viewModel.state.collectAsState()

            LaunchedEffect(viewModel) {
                viewModel.refresh()
            }
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddings)) {
                items(state.opsItems) {
                    Row {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = it.iconRes),
                                contentDescription = it.label
                            )
                            Column {
                                androidx.compose.material3.Text(
                                    it.label,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                androidx.compose.material3.Text(
                                    it.description,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        Switch(checked = it.mode.isAllowed(), onCheckedChange = { checked ->
                            viewModel.setMode(it, checked)
                        })
                    }
                }
            }
        }


    }
}