package now.fortuitous.thanos.sf

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import github.tornaco.android.thanos.module.compose.common.settings.Preference
import github.tornaco.android.thanos.module.compose.common.settings.PreferenceUi
import github.tornaco.android.thanos.res.R

@Composable
fun SFSettings(vm: SFVM) {
    val isScreenOffEnabled by vm.isScreenOffEnabled.collectAsState()
    val isTaskRemovalEnabled by vm.isTaskRemovalEnabled.collectAsState()
    PreferenceUi(
        preferences = listOf(
            Preference.Category(title = stringResource(id = R.string.smart_freeze)),
            Preference.SwitchPreference(
                title = stringResource(id = R.string.smart_freeze_on_screen_off),
                summary = stringResource(id = R.string.smart_freeze_on_screen_off_summary),
                onCheckChanged = {
                    vm.setScreenOffEnabled(it)
                },
                value = isScreenOffEnabled
            ),
            Preference.SwitchPreference(
                title = stringResource(id = R.string.smart_freeze_on_task_removed),
                summary = stringResource(id = R.string.smart_freeze_on_task_removed_summary),
                onCheckChanged = {
                    vm.setTaskRemovalEnabled(it)
                },
                value = isTaskRemovalEnabled
            )

        )
    )
}