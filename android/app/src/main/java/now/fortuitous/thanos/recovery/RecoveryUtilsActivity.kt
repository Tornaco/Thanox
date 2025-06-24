package now.fortuitous.thanos.recovery

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import dagger.hilt.android.AndroidEntryPoint
import github.tornaco.android.thanos.module.compose.common.settings.Preference
import github.tornaco.android.thanos.module.compose.common.settings.PreferenceUi
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.pm.PackageManager
import github.tornaco.android.thanos.core.util.ClipboardUtils
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.ConfirmDialog
import github.tornaco.android.thanos.module.compose.common.widget.ProgressDialog
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxMediumAppBarScaffold
import github.tornaco.android.thanos.module.compose.common.widget.rememberConfirmDialogState
import github.tornaco.android.thanos.module.compose.common.widget.rememberProgressDialogState
import github.tornaco.android.thanos.res.R
import github.tornaco.android.thanos.support.ThanoxAppContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tornaco.apps.thanox.util.ToastUtils

@AndroidEntryPoint
class RecoveryUtilsActivity : ComposeThemeActivity() {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, RecoveryUtilsActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun getApplicationContext(): Context {
        return ThanoxAppContext(super.getApplicationContext())
    }

    @Composable
    private fun prefs(): List<Preference> {
        val context = LocalContext.current
        val restoreAllAppComponentSettingsDialog = rememberConfirmDialogState()

        val progressDialogState = rememberProgressDialogState(
            message = stringResource(R.string.feature_title_recovery_tools_component_settings),
            title = stringResource(R.string.common_text_wait_a_moment)
        )
        ProgressDialog(progressDialogState)

        val scope = rememberCoroutineScope()
        ConfirmDialog(
            title = stringResource(R.string.feature_title_recovery_tools_component_settings),
            state = restoreAllAppComponentSettingsDialog,
            data = stringResource(R.string.feature_title_recovery_tools_component_settings_summary),
            messageHint = {
                it
            }) {
            scope.launch {
                progressDialogState.show()
                withContext(Dispatchers.IO) {
                    ThanosManager.from(context).pkgManager.restoreAllAppComponentSettings()
                }
                delay(1000)
                progressDialogState.dismiss()
            }
        }

        return listOf(
            Preference.TextPreference(
                title = stringResource(R.string.feature_title_recovery_tools_component_settings),
                summary = stringResource(R.string.feature_title_recovery_tools_component_settings_summary),
                onClick = {
                    restoreAllAppComponentSettingsDialog.show()
                },
                icon = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_settings_3_fill
            ),

            Preference.Category(""),

            Preference.TextPreference(
                withCardBg = true,
                title = "",
                summary = stringResource(
                    R.string.feature_title_recovery_tools_component_settings_flags_summary,
                    PackageManager.RESTORE_ALL_APP_COMPONENT_SETTINGS_FILE_FLAGS
                ),
                onClick = {
                    ClipboardUtils.copyToClipboard(
                        context,
                        PackageManager.RESTORE_ALL_APP_COMPONENT_SETTINGS_FILE_FLAGS,
                        PackageManager.RESTORE_ALL_APP_COMPONENT_SETTINGS_FILE_FLAGS
                    )
                    ToastUtils.copiedToClipboard(context)
                },
                icon = github.tornaco.android.thanos.R.drawable.ic_information_fill
            )

        )
    }

    @Composable
    override fun Content() {
        ThanoxMediumAppBarScaffold(
            title = {
                Text(
                    text = stringResource(R.string.feature_title_recovery_tools),
                    style = TypographyDefaults.appBarTitleTextStyle()
                )
            },
            onBackPressed = {
                finish()
            },
            actions = {
            }
        ) { paddings ->
            Column(Modifier.padding(paddings)) {
                PreferenceUi(prefs())
            }
        }
    }
}