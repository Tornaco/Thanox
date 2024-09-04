package now.fortuitous.thanos.launchother

import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.core.pm.Pkg
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.AppIcon
import github.tornaco.android.thanos.module.compose.common.widget.SmallSpacer
import github.tornaco.android.thanos.module.compose.common.widget.StandardSpacer
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxSmallAppBarScaffold
import github.tornaco.android.thanos.module.compose.common.widget.clickableWithRipple
import github.tornaco.android.thanos.picker.AppPickerActivity

@AndroidEntryPoint
class AllowListActivity : ComposeThemeActivity() {
    companion object {
        private const val EXTRA_APP = "app"

        @JvmStatic
        fun start(context: Context, appInfo: AppInfo) {
            val starter = Intent(context, AllowListActivity::class.java).apply {
                putExtra(EXTRA_APP, appInfo)
            }
            context.startActivity(starter)
        }
    }

    override fun isADVF(): Boolean {
        return true
    }

    @Composable
    override fun Content() {
        val viewModel = hiltViewModel<AllowListedVM>()
        val state by viewModel.state.collectAsState()
        val app = remember {
            requireNotNull(intent.getParcelableExtra(EXTRA_APP)) { "App info is null." } as AppInfo
        }
        LaunchedEffect(viewModel) {
            viewModel.init(app)
        }
        val context = LocalContext.current

        val pickAppLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
            onResult = { result ->
                val appInfos: List<AppInfo> =
                    result.data?.getParcelableArrayListExtra("apps") ?: emptyList()
                viewModel.addApp(appInfos)
            }
        )

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
            onBackPressed = {
                thisActivity().finish()
            },
            actions = {
                IconButton(onClick = {
                    pickAppLauncher.launch(
                        AppPickerActivity.getIntent(
                            context,
                            ArrayList(state.apps.map { Pkg.fromAppInfo(it) })
                        )
                    )
                }) {
                    Icon(
                        painter = painterResource(id = github.tornaco.android.thanos.R.drawable.ic_add_fill),
                        contentDescription = "Add"
                    )
                }
            }
        ) { paddings ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddings)
            ) {
                items(state.apps) { appItem ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 68.dp)
                            .clickableWithRipple {
                                viewModel.removeApp(appItem)
                            }
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier
                                .weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AppIcon(modifier = Modifier.size(42.dp), appInfo = appItem)
                            StandardSpacer()
                            Column {
                                Text(
                                    appItem.appLabel,
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}