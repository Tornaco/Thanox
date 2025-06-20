package now.fortuitous.thanos.settings.v2

import android.annotation.SuppressLint
import android.content.Context
import android.os.ParcelFileDescriptor
import android.os.RemoteException
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.viewModelScope
import com.anggrayudi.storage.file.fullName
import com.anggrayudi.storage.file.openInputStream
import com.anggrayudi.storage.file.openOutputStream
import com.elvishew.xlog.XLog
import com.google.common.io.Files
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.common.CommonPreferences
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.backup.IBackupCallback
import github.tornaco.android.thanos.core.backup.IFileDescriptorConsumer
import github.tornaco.android.thanos.core.backup.IFileDescriptorInitializer
import github.tornaco.android.thanos.core.profile.ConfigTemplate
import github.tornaco.android.thanos.core.util.DateUtils
import github.tornaco.android.thanos.core.util.FileUtils
import github.tornaco.android.thanos.module.compose.common.infra.ContextViewModel
import github.tornaco.android.thanos.support.withThanos
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import now.fortuitous.thanos.pref.AppPreference
import java.io.File
import java.io.IOException
import java.io.OutputStream
import javax.inject.Inject

data class SettingsState(
    val isActivated: Boolean = false,
    val coreVersion: String = "N/A",
    val coreFP: String = "N/A",
    val hasFatalError: Boolean = false,

    val isAppStabilityUpKeepEnabled: Boolean = false,
    val isPowerSaveModeEnabled: Boolean = false,
    val isProtectedWhitelistEnabled: Boolean = false,

    val uiShowAppVersion: Boolean = false,
    val uiShowAppPkgName: Boolean = false,

    val isAutoApplyForNewInstalledAppsEnabled: Boolean = false,
    val isAutoConfigTemplateNotificationEnabled: Boolean = false,
    val autoConfigTemplateSelection: ConfigTemplate? = null,
    val allConfigTemplateSelection: List<ConfigTemplate> = emptyList(),

    val showCurrentComponent: Boolean = false,
    val showTrafficStats: Boolean = false,
    val newOPS: Boolean = false,
    val newHome: Boolean = false,
)

sealed interface BackupResult {
    data class Success(val path: String) : BackupResult
    data class Failed(val error: String, val tmpFile: File) : BackupResult
}

sealed interface RestoreResult {
    data object Success : RestoreResult
    data object ResetComplete : RestoreResult
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
                allConfigTemplateSelection = thanos.profileManager.allConfigTemplates,
                uiShowAppVersion = CommonPreferences.getInstance()
                    .isAppListShowVersionEnabled(context),
                uiShowAppPkgName = CommonPreferences.getInstance()
                    .isAppListShowPkgNameEnabled(context),

                showCurrentComponent = thanos.activityStackSupervisor.isShowCurrentComponentViewEnabled,
                showTrafficStats = thanos.activityManager.isNetStatTrackerEnabled,
                newOPS = AppPreference.isFeatureNoticeAccepted(context, "NEW_OPS"),
                newHome = AppPreference.isFeatureNoticeAccepted(context, "NEW_HOME"),
            )
        }
    }

    fun backup(pickedFile: DocumentFile) {
        val pickedFileOS = pickedFile.openOutputStream(context)
        if (pickedFileOS == null) {
            viewModelScope.launch {
                _backupPerformed.emit(
                    BackupResult.Failed(
                        "Unable to open output stream.",
                        File(pickedFile.fullName)
                    )
                )
            }
            return
        }
        val backupTmpDir = File(context.externalCacheDir, "backup")
        thanos.backupAgent
            .performBackup(
                object : IFileDescriptorInitializer.Stub() {
                    @Throws(RemoteException::class)
                    override fun initParcelFileDescriptor(
                        domain: String, path: String,
                        consumer: IFileDescriptorConsumer
                    ) {
                        val subFile = File(backupTmpDir, path)
                        XLog.d("create sub file: $subFile")
                        try {
                            Files.createParentDirs(subFile)
                            if (subFile.createNewFile()) {
                                val pfd = ParcelFileDescriptor.open(
                                    subFile,
                                    ParcelFileDescriptor.MODE_READ_WRITE
                                )
                                consumer.acceptAppParcelFileDescriptor(pfd)
                            } else {
                                consumer.acceptAppParcelFileDescriptor(null)
                            }
                        } catch (e: IOException) {
                            XLog.e(e, "createParentDirs fail")
                            consumer.acceptAppParcelFileDescriptor(null)
                        }
                    }
                },
                null,
                null,
                object : IBackupCallback.Stub() {
                    override fun onBackupFinished(domain: String?, path: String) {
                        XLog.d("onBackupFinished: $path")
                        val subFile = File(backupTmpDir, path)
                        // Move it to dest.
                        try {
                            copyFileToOutputStream(subFile, pickedFileOS)
                            runCatching { backupTmpDir.deleteRecursively() }
                            viewModelScope.launch {
                                _backupPerformed.emit(
                                    BackupResult.Success(
                                        pickedFile.fullName
                                    )
                                )
                            }
                        } catch (e: Throwable) {
                            XLog.e(e, "copy err")
                            viewModelScope.launch {
                                _backupPerformed.emit(
                                    BackupResult.Failed(
                                        e.message ?: "Unknown err.",
                                        subFile
                                    )
                                )
                            }
                        }
                    }

                    override fun onRestoreFinished(domain: String?, path: String?) {

                    }

                    override fun onFail(message: String) {
                        viewModelScope.launch {
                            _backupPerformed.emit(
                                BackupResult.Failed(
                                    message,
                                    backupTmpDir
                                )
                            )
                        }
                    }

                    override fun onProgress(progressMessage: String?) {
                    }
                })
    }

    fun restore(pickedFile: DocumentFile) {
        XLog.d("storageHelper restore.")
        val tmpDir = File(context.externalCacheDir, "restore_tmp")
        try {
            val tmpZipFile = File(
                tmpDir,
                String.format("tem_restore_%s.zip", System.currentTimeMillis())
            )
            Files.createParentDirs(tmpZipFile)

            val inputStream = pickedFile.openInputStream(context)

            if (inputStream == null) {
                viewModelScope.launch {
                    _restorePerformed.emit(RestoreResult.Failed("Unable to open file input: ${pickedFile.fullName}"))
                }
                return
            }

            val buffer = ByteArray(inputStream.available())
            inputStream.read(buffer)
            Files.write(buffer, tmpZipFile)

            val pfd = ParcelFileDescriptor.open(tmpZipFile, ParcelFileDescriptor.MODE_READ_ONLY)
            thanos.backupAgent
                .performRestore(pfd, null, null, object : IBackupCallback.Stub() {
                    override fun onBackupFinished(domain: String?, path: String?) {
                        // Not for us.
                    }

                    override fun onRestoreFinished(domain: String?, path: String?) {
                        XLog.d("onRestoreFinished: $path")
                        viewModelScope.launch {
                            _restorePerformed.emit(RestoreResult.Success)
                        }
                    }

                    override fun onFail(message: String?) {
                        XLog.d("onFail: $message")
                        viewModelScope.launch {
                            _restorePerformed.emit(RestoreResult.Failed(message ?: "Unknown error"))
                        }
                    }

                    override fun onProgress(progressMessage: String?) {
                    }
                })
        } catch (e: Exception) {
            viewModelScope.launch {
                _restorePerformed.emit(RestoreResult.Failed(e.message ?: e.toString()))
            }
        } finally {
            FileUtils.deleteDirQuiet(tmpDir)
        }
    }

    fun restoreDefault() {
        context.withThanos {
            backupAgent.restoreDefault()
            viewModelScope.launch {
                _restorePerformed.emit(RestoreResult.ResetComplete)
            }
            loadState()
        }
    }
}


fun autoGenBackupFileName() =
    "Thanox-Backup-${DateUtils.formatForFileName(System.currentTimeMillis())}"

fun copyFileToOutputStream(file: File, outputStream: OutputStream) {
    file.inputStream().use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }
}