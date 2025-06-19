package now.fortuitous.thanos.settings.v2

import android.annotation.SuppressLint
import android.content.Context
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.profile.ConfigTemplate
import github.tornaco.android.thanos.module.compose.common.infra.ContextViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

data class SettingsState(
    val isActivated: Boolean = false,
    val coreVersion: String = "N/A",
    val coreFP: String = "N/A",
    val hasFatalError: Boolean = false,

    val isAppStabilityUpKeepEnabled: Boolean = false,
    val isPowerSaveModeEnabled: Boolean = false,
    val isProtectedWhitelistEnabled: Boolean = false,

    val isAutoApplyForNewInstalledAppsEnabled: Boolean = false,
    val isAutoConfigTemplateNotificationEnabled: Boolean = false,
    val autoConfigTemplateSelection: ConfigTemplate? = null,
    val allConfigTemplateSelection: List<ConfigTemplate> = emptyList(),
)

sealed interface BackupResult {
    data class Success(val path: String) : BackupResult
    data class Failed(val error: String) : BackupResult
}

sealed interface RestoreResult {
    data object Success : RestoreResult
    data class Failed(val error: String) : RestoreResult
}

sealed interface ExportLogResult {
    data class Success(val path: String) : ExportLogResult
    data class Failed(val error: String) : ExportLogResult
}

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class SettingsViewModel @Inject constructor(@ApplicationContext context: Context) :
    ContextViewModel<SettingsState>(context, { SettingsState() }) {

    private val _backupPerformed = MutableSharedFlow<BackupResult>()
    val backupPerformed = _backupPerformed.asSharedFlow()

    private val _restorePerformed = MutableSharedFlow<RestoreResult>()
    val restorePerformed = _restorePerformed.asSharedFlow()

    private val _logExportPerformed = MutableSharedFlow<ExportLogResult>()
    val logExportPerformed = _logExportPerformed.asSharedFlow()

    private val thanos by lazy { ThanosManager.from(context) }

    fun loadState() {
        updateState {
            copy(
                isAppStabilityUpKeepEnabled = thanos.activityManager.isAppStabilityUpKeepEnabled,
                isPowerSaveModeEnabled = thanos.powerManager.isPowerSaveModeEnabled,
                isProtectedWhitelistEnabled = thanos.pkgManager.isProtectedWhitelistEnabled,
                isAutoApplyForNewInstalledAppsEnabled = thanos.profileManager.isAutoApplyForNewInstalledAppsEnabled,
                isAutoConfigTemplateNotificationEnabled = thanos.profileManager.isAutoConfigTemplateNotificationEnabled,
                autoConfigTemplateSelection = thanos.profileManager.autoConfigTemplateSelectionId?.let {
                    thanos.profileManager.getConfigTemplateById(it)
                },
                allConfigTemplateSelection = thanos.profileManager.allConfigTemplates
            )
        }
    }
}