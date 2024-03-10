package now.fortuitous.thanos.launchother

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dagger.hilt.android.AndroidEntryPoint
import github.tornaco.android.thanos.BuildProp
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import github.tornaco.android.thanos.module.compose.common.DisposableEffectWithLifeCycle
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.ListItem
import github.tornaco.android.thanos.module.compose.common.widget.TextInputDialog
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxSmallAppBarScaffold
import github.tornaco.android.thanos.module.compose.common.widget.rememberTextInputState
import github.tornaco.android.thanos.util.BrowserUtils

@AndroidEntryPoint
class LaunchOtherAppRuleActivity : ComposeThemeActivity() {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, LaunchOtherAppRuleActivity::class.java)
            context.startActivity(starter)
        }
    }

    @Composable
    override fun Content() {
        val viewModel = hiltViewModel<LaunchOtherAppRuleViewModel>()
        val state by viewModel.state.collectAsState()

        val inputDialog = rememberTextInputState(
            title = stringResource(id = github.tornaco.android.thanos.R.string.menu_title_rules),
            message = ""
        ) {
            viewModel.add(it)
        }
        TextInputDialog(state = inputDialog)

        ThanoxSmallAppBarScaffold(
            title = {
                Text(
                    text = stringResource(id = github.tornaco.android.thanos.R.string.menu_title_rules),
                    style = TypographyDefaults.appBarTitleTextStyle()
                )
            },
            onBackPressed = {
                thisActivity().finish()
            },
            actions = {
                TextButton(onClick = {
                    BrowserUtils.launch(thisActivity(), BuildProp.THANOX_URL_DOCS_LAUNCH_OTHER_APP_RULES)
                }) {
                    Text(stringResource(id = github.tornaco.android.thanos.module.common.R.string.common_menu_title_wiki))
                }
                IconButton(onClick = {
                    inputDialog.show()
                }) {
                    Icon(
                        painter = painterResource(id = github.tornaco.android.thanos.R.drawable.ic_add_fill),
                        contentDescription = "Add"
                    )
                }
            }
        ) { paddings ->

            DisposableEffectWithLifeCycle(onResume = {
                viewModel.refresh()
            })

            LaunchedEffect(viewModel) {
                viewModel.refresh()
            }

            SwipeRefresh(
                state = rememberSwipeRefreshState(state.isLoading),
                onRefresh = { viewModel.refresh() },
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
                    items(state.ruleItems) {
                        ListItem(modifier = Modifier, title = it.rule,
                            action1 = {
                                IconButton(onClick = {
                                    viewModel.remove(it.rule)
                                }) {
                                    Icon(
                                        painter = painterResource(id = github.tornaco.android.thanos.R.drawable.ic_delete_bin_2_fill),
                                        contentDescription = "Delete"
                                    )
                                }
                            })
                    }
                }
            }
        }
    }
}