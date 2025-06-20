@file:OptIn(ExperimentalMaterial3Api::class)

package now.fortuitous.thanos.settings.v2

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elvishew.xlog.XLog
import github.tornaco.android.thanos.common.settings.Preference
import github.tornaco.android.thanos.common.settings.PreferenceUi
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.core.profile.ConfigTemplate
import github.tornaco.android.thanos.core.profile.ProfileManager
import github.tornaco.android.thanos.module.compose.common.theme.ThanoxTheme
import github.tornaco.android.thanos.module.compose.common.widget.ConfirmDialog
import github.tornaco.android.thanos.module.compose.common.widget.MenuDialog
import github.tornaco.android.thanos.module.compose.common.widget.MenuDialogItem
import github.tornaco.android.thanos.module.compose.common.widget.StandardSpacer
import github.tornaco.android.thanos.module.compose.common.widget.TextInputDialog
import github.tornaco.android.thanos.module.compose.common.widget.rememberConfirmDialogState
import github.tornaco.android.thanos.module.compose.common.widget.rememberMenuDialogState
import github.tornaco.android.thanos.module.compose.common.widget.rememberTextInputState
import github.tornaco.android.thanos.res.R
import github.tornaco.android.thanos.support.withThanos
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import now.fortuitous.thanos.apps.AppDetailsActivity
import now.fortuitous.thanos.main.LocalSimpleStorageHelper
import java.util.UUID

@Composable
fun SettingsScreen() {
    ThanoxTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            val vm = hiltViewModel<SettingsViewModel>()
            val state by vm.state.collectAsStateWithLifecycle()

            LaunchedEffect(vm) {
                vm.loadState()
            }

            val snackbarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()

            val backupSuccessMsg = stringResource(R.string.pre_message_backup_success)
            val backupErrorTitle = stringResource(R.string.pre_message_backup_error)
            val actionLabel = stringResource(android.R.string.ok)

            val restoreSuccessMsg = stringResource(R.string.pre_message_restore_success)

            LaunchedEffect(vm) {
                vm.backupPerformed.collectLatest {
                    when (it) {
                        is BackupResult.Success -> {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "$backupSuccessMsg ${it.path}",
                                    duration = SnackbarDuration.Indefinite,
                                    actionLabel = actionLabel
                                )
                            }
                        }

                        is BackupResult.Failed -> {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "$backupErrorTitle ${it.tmpFile}",
                                    duration = SnackbarDuration.Indefinite,
                                    actionLabel = actionLabel
                                )
                            }
                        }
                    }
                }
            }

            LaunchedEffect(vm) {
                vm.restorePerformed.collectLatest {
                    when (it) {
                        is RestoreResult.Success, RestoreResult.ResetComplete -> {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = restoreSuccessMsg,
                                )
                            }
                        }

                        is RestoreResult.Failed -> {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "ERROR: ${it.error}",
                                )
                            }
                        }
                    }
                }
            }

            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    TopAppBar(
                        modifier = Modifier,
                        title = {
                            Text(stringResource(R.string.nav_title_settings))
                        }
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .verticalScroll(rememberScrollState())
                        .consumeWindowInsets(paddingValues)
                        .padding(bottom = 32.dp)
                        .animateContentSize()

                ) {
                    Spacer(Modifier.size(paddingValues.calculateTopPadding()))

                    PreferenceUi(mutableListOf<Preference>().apply {
                        addAll(
                            generalSettings(
                                state = state,
                                vm = vm,
                            )
                        )
                        addAll(
                            strategySettings(
                                state = state,
                                vm = vm,
                            )
                        )
                        addAll(
                            dataSettings(
                                state = state,
                                vm = vm,
                            )
                        )
                    })
                    StandardSpacer()
                    Spacer(Modifier.size(paddingValues.calculateBottomPadding()))
                }
            }
        }
    }

}


@Composable
private fun generalSettings(
    state: SettingsState,
    vm: SettingsViewModel,
): List<Preference> {
    val context = LocalContext.current
    return context.withThanos {
        listOf(
            Preference.Category(stringResource(R.string.pre_category_general)),

            Preference.SwitchPreference(
                icon = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_battery_saver_fill,
                title = stringResource(R.string.pref_title_enable_power_save),
                summary = stringResource(R.string.pref_summary_enable_power_save),
                hasLongSummary = false,
                value = state.isPowerSaveModeEnabled,
                onCheckChanged = { enable ->
                    powerManager.isPowerSaveModeEnabled = enable
                    vm.loadState()
                }
            ),
            Preference.SwitchPreference(
                icon = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_shield_cross_fill,
                title = stringResource(R.string.pref_title_app_stability_upkeep),
                summary = stringResource(R.string.pref_summary_app_stability_upkeep),
                hasLongSummary = false,
                value = state.isAppStabilityUpKeepEnabled,
                onCheckChanged = { enable ->
                    activityManager.isAppStabilityUpKeepEnabled = enable
                    vm.loadState()
                }
            ),
            Preference.SwitchPreference(
                icon = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_file_list_3_fill,
                title = stringResource(R.string.pref_title_enable_global_white_list),
                summary = stringResource(R.string.pref_summary_enable_global_white_list),
                hasLongSummary = true,
                value = state.isProtectedWhitelistEnabled,
                onCheckChanged = { enable ->
                    pkgManager.isProtectedWhitelistEnabled = enable
                    vm.loadState()
                }
            ),
        )
    } ?: emptyList()
}

@Composable
private fun strategySettings(
    state: SettingsState,
    vm: SettingsViewModel,
): List<Preference> {
    val context = LocalContext.current
    return context.withThanos {
        val templateSelectDialog = rememberMenuDialogState<Unit>(
            key1 = state.allConfigTemplateSelection,
            title = { context.getString(R.string.pref_title_new_installed_apps_config) },
            message = null,
            menuItems = state.allConfigTemplateSelection.map {
                MenuDialogItem(id = it.id, title = it.title)
            }
        ) { _, id ->
            profileManager.setAutoConfigTemplateSelection(id)
            vm.loadState()
        }
        MenuDialog(templateSelectDialog)

        val templateEditDialog = rememberMenuDialogState<ConfigTemplate>(
            title = { it?.title.orEmpty() },
            message = null,
            menuItems = listOf(
                MenuDialogItem(
                    id = "Edit",
                    title = stringResource(R.string.pref_action_edit_or_view_config_template)
                ),
                MenuDialogItem(
                    id = "Delete",
                    title = stringResource(R.string.pref_action_delete_config_template)
                )
            )
        ) { template, id ->
            if (template != null) {
                if (id == "Delete") {
                    profileManager.deleteConfigTemplate(template)
                } else {
                    val appInfo = AppInfo()
                    appInfo.isSelected = false
                    appInfo.pkgName = template.dummyPackageName
                    appInfo.appLabel = template.title
                    appInfo.isDummy = true
                    appInfo.versionCode = -1
                    appInfo.versionCode = -1
                    appInfo.uid = -1
                    appInfo.userId = 0
                    AppDetailsActivity.start(context, appInfo)
                }
                vm.loadState()
            }

        }
        MenuDialog(templateEditDialog)

        val addTemplateDialog = rememberTextInputState(
            title = stringResource(R.string.common_fab_title_add),
            message = null
        ) {
            val uuid = UUID.randomUUID().toString()
            val template =
                ConfigTemplate.builder()
                    .title(it)
                    .id(uuid)
                    .dummyPackageName(
                        ProfileManager
                            .PROFILE_AUTO_APPLY_NEW_INSTALLED_APPS_CONFIG_TEMPLATE_PACKAGE_PREFIX
                                + uuid
                    )
                    .createAt(System.currentTimeMillis())
                    .build()
            profileManager.addConfigTemplate(template)
            vm.loadState()
        }
        TextInputDialog(addTemplateDialog)

        listOf(
            Preference.Category(stringResource(R.string.pre_category_strategy)),

            Preference.SwitchPreference(
                icon = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_tools_fill,
                title = stringResource(R.string.pref_title_new_installed_apps_config_enabled),
                summary = stringResource(R.string.pref_summary_new_installed_apps_config_enabled),
                hasLongSummary = false,
                value = state.isAutoApplyForNewInstalledAppsEnabled,
                onCheckChanged = { enable ->
                    profileManager.isAutoApplyForNewInstalledAppsEnabled = enable
                    vm.loadState()
                }
            ),

            Preference.SwitchPreference(
                icon = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_notification_4_fill,
                title = stringResource(R.string.pref_title_new_installed_apps_config_notification_enabled),
                summary = stringResource(R.string.pref_summary_new_installed_apps_config_notification_enabled),
                hasLongSummary = false,
                value = state.isAutoConfigTemplateNotificationEnabled,
                onCheckChanged = { enable ->
                    profileManager.isAutoConfigTemplateNotificationEnabled = enable
                    vm.loadState()
                }
            ),

            Preference.TextPreference(
                icon = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_file_code_fill,
                title = stringResource(R.string.pref_title_new_installed_apps_config),
                summary = state.autoConfigTemplateSelection?.title
                    ?: stringResource(R.string.common_text_value_not_set),
                onClick = {
                    templateSelectDialog.show()
                }
            ),

            Preference.ExpandablePreference(
                title = stringResource(R.string.pref_title_config_template_category),
                preferences = state.allConfigTemplateSelection.map {
                    Preference.TextPreference(
                        title = it.title,
                        onClick = {
                            templateEditDialog.show(it)
                        }
                    )
                } + listOf(
                    Preference.TextPreference(
                        icon = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_add_fill,
                        title = stringResource(R.string.common_fab_title_add),
                        onClick = {
                            addTemplateDialog.show()
                        }
                    ),
                ),
            ),
        )
    } ?: emptyList()
}


@Composable
private fun dataSettings(
    state: SettingsState,
    vm: SettingsViewModel,
): List<Preference> {
    val backUpFileNameDialogState = rememberTextInputState(
        title = stringResource(R.string.pre_title_backup),
        showSymbolButton = false,
        onSelected = {
        })
    TextInputDialog(state = backUpFileNameDialogState)

    val context = LocalContext.current
    val storageHelper = LocalSimpleStorageHelper.current
    SideEffect {
        storageHelper.onFileCreated = { code, file ->
            vm.backup(file)
        }

        storageHelper.onFileSelected = { code, files ->
            XLog.d("storageHelper onFileSelected- $files")
            val file = files.firstOrNull()
            file?.let {
                vm.restore(file)
            } ?: Toast.makeText(context, "Canceled.", Toast.LENGTH_SHORT).show()
        }

    }

    val resetConfigDialog = rememberConfirmDialogState()
    ConfirmDialog(
        title = stringResource(R.string.pre_title_restore_default),
        state = resetConfigDialog,
        messageHint = { it },
        data = stringResource(R.string.pre_summary_restore_default)
    ) {
        vm.restoreDefault()
    }

    return listOf(
        Preference.Category(stringResource(R.string.pre_category_data)),
        Preference.TextPreference(
            icon = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_upload_cloud_2_fill,
            title = stringResource(R.string.pre_title_backup),
            summary = stringResource(R.string.pre_summary_backup),
            onClick = {
                storageHelper.createFile(
                    "application/zip",
                    autoGenBackupFileName() + ".zip"
                )
            }
        ),
        Preference.ExpandablePreference(
            icon = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_download_cloud_fill,
            title = stringResource(R.string.pre_title_restore),
            summary = stringResource(R.string.pre_sumary_restore),
            onClick = {
                XLog.d("storageHelper openFilePicker")
                storageHelper.openFilePicker(
                    requestCode = 100,
                    allowMultiple = false,
                    initialPath = null,
                    "application/zip"
                )
            },
            preferences = listOf(
                Preference.TextPreference(
                    title = stringResource(R.string.pre_title_restore_default),
                    summary = stringResource(R.string.pre_summary_restore_default),
                    onClick = {
                        resetConfigDialog.show()
                    }
                ),
            ),
        ),
    )
}