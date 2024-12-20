package github.tornaco.thanos.module.component.manager.redesign

import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import github.tornaco.android.thanos.common.UiState
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.AppIcon
import github.tornaco.android.thanos.module.compose.common.widget.SmallSpacer
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxSmallAppBarScaffold
import github.tornaco.android.thanos.module.compose.common.widget.clickableWithRipple
import github.tornaco.android.thanos.module.compose.common.widget.rememberSearchBarState
import github.tornaco.thanos.module.component.manager.model.ComponentModel
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterialApi::class)
@AndroidEntryPoint
class ComponentsActivity : ComposeThemeActivity() {
    companion object {
        private const val EXTRA_APP = "app"
        private const val EXTRA_TYPE = "type"

        @JvmStatic
        fun startActivity(context: Context, appInfo: AppInfo) {
            start(context, appInfo, Type.ACTIVITY)
        }

        private fun start(context: Context, appInfo: AppInfo, type: Type) {
            val starter = Intent(context, ComponentsActivity::class.java).apply {
                putExtra(EXTRA_APP, appInfo)
                putExtra(EXTRA_TYPE, type.name)
            }
            context.startActivity(starter)
        }

        enum class Type {
            ACTIVITY,
            RECEIVER,
            SERVICE,
            PROVIDER
        }
    }

    override fun isADVF(): Boolean {
        return true
    }


    @Composable
    override fun Content() {
        val type = remember {
            requireNotNull(
                Type.valueOf(
                    intent.getStringExtra(EXTRA_TYPE).orEmpty()
                )
            ) { "Type is null." }
        }

        val viewModel = when (type) {
            Type.ACTIVITY -> {
                hiltViewModel<ActivitiesVM>()
            }

            Type.RECEIVER -> {
                hiltViewModel<ActivitiesVM>()
            }

            Type.SERVICE -> {
                hiltViewModel<ActivitiesVM>()
            }

            Type.PROVIDER -> {
                hiltViewModel<ActivitiesVM>()
            }
        }

        val app = remember {
            requireNotNull(intent.getParcelableExtra(EXTRA_APP)) { "App info is null." } as AppInfo
        }
        LaunchedEffect(viewModel) {
            viewModel.initApp(app)
        }
        val searchBarState = rememberSearchBarState()

        LaunchedEffect(searchBarState) {
            snapshotFlow { searchBarState.keyword }
                .distinctUntilChanged()
                .collect {
                    viewModel.search(it)
                }
        }

        BackHandler(searchBarState.showSearchBar) {
            searchBarState.closeSearchBar()
        }

        ThanoxSmallAppBarScaffold(
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
            searchBarState = searchBarState,
            onBackPressed = {
                thisActivity().finish()
            },
            actions = {
                IconButton(onClick = {
                    searchBarState.showSearchBar()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search"
                    )
                }
            }
        ) { paddings ->
            val components by viewModel.components.collectAsStateWithLifecycle()
            val refreshing = components is UiState.Loading
            val refreshState = rememberPullRefreshState(refreshing, onRefresh = {
                viewModel.refresh()
            })

            Box(
                Modifier
                    .fillMaxSize()
                    .padding(paddings)
                    .pullRefresh(refreshState)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    when (components) {
                        is UiState.Error -> {
                            item {
                                Text((components as UiState.Error).err.stackTraceToString())
                            }
                        }

                        is UiState.Loaded -> {
                            items((components as UiState.Loaded<List<ComponentModel>>).data) { component ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(min = 68.dp)
                                        .clickableWithRipple {
                                        }
                                        .padding(horizontal = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .weight(1f),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                component.label,
                                                style = MaterialTheme.typography.titleSmall
                                            )
                                            Text(
                                                component.name,
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        UiState.Loading -> {
                            item {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }

                PullRefreshIndicator(
                    refreshing = refreshing,
                    state = refreshState,
                    modifier = Modifier.align(Alignment.TopCenter),
                    contentColor = MaterialTheme.colorScheme.secondary
                )
            }

        }
    }
}