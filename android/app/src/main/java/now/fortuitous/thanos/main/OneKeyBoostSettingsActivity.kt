package now.fortuitous.thanos.main

import android.content.Context
import android.content.Intent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import github.tornaco.android.thanos.module.compose.common.settings.Preference
import github.tornaco.android.thanos.module.compose.common.settings.PreferenceUi
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxMediumAppBarScaffold
import github.tornaco.android.thanos.res.R

class OneKeyBoostSettingsActivity : ComposeThemeActivity() {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, OneKeyBoostSettingsActivity::class.java)
            context.startActivity(starter)
        }
    }

    @Composable
    override fun Content() {
        ThanoxMediumAppBarScaffold(title = {
            Text(stringResource(R.string.feature_title_one_key_boost))
        }, onBackPressed = {
            finish()
        }) {
            val context = LocalContext.current
            var isFreezeOnBoost by remember {
                mutableStateOf(ThanosManager.from(context).pkgManager.isOneKeyBoostFreezeAppEnabled)
            }
            PreferenceUi(
                listOf(
                    Preference.SwitchPreference(
                        title = stringResource(R.string.smart_freeze_on_one_key_boost),
                        summary = stringResource(R.string.smart_freeze_on_one_key_boost_summary),
                        value = isFreezeOnBoost,
                        onCheckChanged = {
                            ThanosManager.from(context).pkgManager.isOneKeyBoostFreezeAppEnabled =
                                it
                            isFreezeOnBoost = it
                        }
                    )
                )
            )
        }
    }
}