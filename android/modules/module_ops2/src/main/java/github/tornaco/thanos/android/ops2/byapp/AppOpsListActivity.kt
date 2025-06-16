package github.tornaco.thanos.android.ops2.byapp

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dagger.hilt.android.AndroidEntryPoint
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import github.tornaco.android.thanos.module.compose.common.DisposableEffectWithLifeCycle
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.AppIcon
import github.tornaco.android.thanos.module.compose.common.widget.SmallSpacer
import github.tornaco.android.thanos.module.compose.common.widget.StandardSpacer
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxMediumAppBarScaffold
import github.tornaco.android.thanos.module.compose.common.widget.clickableWithRipple
import github.tornaco.thanos.android.ops2.byop.displayColor
import github.tornaco.thanos.android.ops2.byop.displayLabel
import github.tornaco.thanos.android.ops2.byop.opModeMenuDialog

@AndroidEntryPoint
class AppOpsListActivity : ComposeThemeActivity() {
    companion object {
        private const val EXTRA_APP = "app"

        @JvmStatic
        fun start(context: Context, appInfo: AppInfo) {
            val starter = Intent(context, AppOpsListActivity::class.java).apply {
                putExtra(EXTRA_APP, appInfo)
            }
            context.startActivity(starter)
        }
    }

    @Composable
    override fun Content() {
        val viewModel = hiltViewModel<AppOpsListViewModel>()
        val state by viewModel.state.collectAsState()
        val app = remember {
            requireNotNull(intent.getParcelableExtra(EXTRA_APP)) { "App info is null." } as AppInfo
        }

        ThanoxMediumAppBarScaffold(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AppIcon(modifier = Modifier.size(32.dp), appInfo = app)
                    SmallSpacer()
                    Text(
                        text = app.appLabel,
                        style = TypographyDefaults.appBarTitleTextStyle()
                    )
                }
            },
            onBackPressed = {
                thisActivity().finish()
            },
            actions = {

            }
        ) { paddings ->

            DisposableEffectWithLifeCycle(onResume = {
                viewModel.refresh(app)
            })

            LaunchedEffect(viewModel) {
                viewModel.refresh(app)
            }

            SwipeRefresh(
                state = rememberSwipeRefreshState(state.isLoading),
                onRefresh = { viewModel.refresh(app) },
                indicatorPadding = paddings,
                clipIndicatorToPadding = false,
                indicator = { state, refreshTriggerDistance ->
                    SwipeRefreshIndicator(
                        state = state,
                        refreshTriggerDistance = refreshTriggerDistance,
                        scale = true,
                        arrowEnabled = false,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                }
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddings)
                ) {
                    items(state.opsItems) {
                        val modeSelectDialogState = opModeMenuDialog(
                            appInfo = app,
                            hasBackgroundPermission = it.permInfo.hasBackgroundPermission,
                            isRuntimePermission = it.permInfo.isRuntimePermission,
                            isSupportOneTimeGrant = it.permInfo.isSupportOneTimeGrant
                        ) { app, state ->
                            viewModel.setMode(app, it.code, state)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 68.dp)
                                .clickableWithRipple {
                                    modeSelectDialogState.show(app)
                                }
                                .padding(horizontal = 16.dp)
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = it.iconRes),
                                    contentDescription = it.label,
                                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.66f)
                                )
                                StandardSpacer()
                                Column(verticalArrangement = Arrangement.Center) {
                                    Text(
                                        it.label,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                    if (it.description.isNotBlank()) {
                                        Text(
                                            it.description,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                    if (it.permInfo.opAccessSummary.isNotBlank()) {
                                        Text(
                                            it.permInfo.opAccessSummary,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }

                            Text(
                                text = it.permInfo.permState.displayLabel(),
                                style = MaterialTheme.typography.titleSmall,
                                color = it.permInfo.permState.displayColor()
                            )
                        }
                    }
                }
            }
        }
    }
}