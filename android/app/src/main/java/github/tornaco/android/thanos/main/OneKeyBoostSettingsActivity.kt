package github.tornaco.android.thanos.main

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.DropdownItem
import github.tornaco.android.thanos.module.compose.common.widget.DropdownSelector
import github.tornaco.android.thanos.module.compose.common.widget.ListItem
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxSmallAppBarScaffold
import github.tornaco.android.thanos.module.compose.common.widget.rememberDropdownSelectorState
import github.tornaco.android.thanos.util.ActivityUtils

@AndroidEntryPoint
class OneKeyBoostSettingsActivity : ComposeThemeActivity() {

    object Starter {
        @JvmStatic
        fun start(context: Context?) {
            ActivityUtils.startActivity(context, OneKeyBoostSettingsActivity::class.java)
        }
    }

    override fun isF(): Boolean {
        return false
    }

    @Composable
    override fun Content() {
        ThanoxSmallAppBarScaffold(
            title = {
                Text(
                    text = stringResource(id = R.string.feature_title_one_key_boost),
                    style = TypographyDefaults.appBarTitleTextStyle()
                )
            },
            onBackPressed = {
                thisActivity().finish()
            },
            actions = {

            }
        ) { paddings ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddings)
                    .padding(16.dp)
            ) {
                val context = LocalContext.current
                val allPkgSets = remember {
                    ThanosManager.from(context).pkgManager.getAllPackageSets(false)
                }
                val currentSettingLabel: () -> Pair<String, String> = {
                    val setting = ThanosManager.from(context).activityManager.oneKeyBoostSetting
                    if (setting == "default") {
                        "Default" to ""
                    } else {
                        "App set: ${allPkgSets.firstOrNull { it.id == setting }?.label}" to ""
                    }
                }
                var currentSettingsLabel: Pair<String, String> by remember {
                    mutableStateOf(currentSettingLabel())
                }

                val dropdownState = rememberDropdownSelectorState()
                DropdownSelector(state = dropdownState, items = mutableListOf(
                    DropdownItem(labelLines = listOf("Default"), "default")
                ).apply {
                    addAll(allPkgSets.map {
                        DropdownItem(labelLines = listOf(it.label), it.id)
                    })
                }, onSelect = {
                    ThanosManager.from(context).activityManager.oneKeyBoostSetting = it.data
                    currentSettingsLabel = currentSettingLabel()
                })


                ListItem(
                    title = currentSettingsLabel.first,
                    text1 = currentSettingsLabel.second,
                    onClick = {
                        dropdownState.open()
                    })
            }
        }
    }
}