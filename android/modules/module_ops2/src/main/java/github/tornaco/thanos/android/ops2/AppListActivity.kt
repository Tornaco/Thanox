package github.tornaco.thanos.android.ops2

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import github.tornaco.android.thanos.core.ops.PermState
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.AppIcon
import github.tornaco.android.thanos.module.compose.common.widget.MenuDialog
import github.tornaco.android.thanos.module.compose.common.widget.MenuDialogItem
import github.tornaco.android.thanos.module.compose.common.widget.StandardSpacer
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxSmallAppBarScaffold
import github.tornaco.android.thanos.module.compose.common.widget.clickableWithRipple
import github.tornaco.android.thanos.module.compose.common.widget.rememberMenuDialogState

private const val EXTRA_CODE = "code"

@AndroidEntryPoint
class AppListActivity : ComposeThemeActivity() {
    companion object {
        @JvmStatic
        fun start(context: Context, code: Int) {
            val starter = Intent(context, AppListActivity::class.java).apply {
                putExtra(EXTRA_CODE, code)
            }
            context.startActivity(starter)
        }
    }

    @Composable
    override fun Content() {
        val code = remember {
            intent.getIntExtra(EXTRA_CODE, Int.MIN_VALUE)
        }

        ThanoxSmallAppBarScaffold(
            title = {
                androidx.compose.material3.Text(
                    text = "App",
                    style = TypographyDefaults.appBarTitleTextStyle()
                )
            },
            onBackPressed = {},
            actions = {

            }
        ) { paddings ->
            val viewModel = hiltViewModel<AppListViewModel>()
            val state by viewModel.state.collectAsState()

            LaunchedEffect(viewModel) {
                viewModel.init(code)
                viewModel.refresh()
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddings)
            ) {
                items(state.appList) { appItem ->
                    val modeSelectDialogState = rememberMenuDialogState<AppInfo>(
                        title = appItem.appInfo.appLabel,
                        menuItems = if (appItem.permInfo.isRuntimePermission) {
                            if (appItem.permInfo.hasBackgroundPermission) {
                                listOf(
                                    PermState.ALLOW_ALWAYS,
                                    PermState.ALLOW_FOREGROUND_ONLY,
                                    PermState.IGNORE,
                                    PermState.DENY,
                                ).map {
                                    MenuDialogItem(it.name, it.name)
                                }
                            } else {
                                listOf(
                                    PermState.ALLOW_ALWAYS,
                                    PermState.IGNORE,
                                    PermState.DENY
                                ).map {
                                    MenuDialogItem(it.name, it.name)
                                }
                            }
                        } else {
                            listOf(PermState.ALLOW_ALWAYS, PermState.IGNORE).map {
                                MenuDialogItem(it.name, it.name)
                            }
                        },
                        onItemSelected = { appInfo, id ->
                            appInfo?.let { viewModel.setMode(it, PermState.valueOf(id)) }
                        }
                    )
                    MenuDialog(state = modeSelectDialogState)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 68.dp)
                            .clickableWithRipple {
                                modeSelectDialogState.show(appItem.appInfo)
                            }
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier
                                .weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AppIcon(modifier = Modifier.size(42.dp), appInfo = appItem.appInfo)
                            StandardSpacer()
                            Column {
                                androidx.compose.material3.Text(
                                    appItem.appInfo.appLabel,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                androidx.compose.material3.Text(
                                    appItem.appInfo.pkgName,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        Text(text = appItem.permInfo.permState.displayLabel())
                    }
                }
            }
        }


    }
}