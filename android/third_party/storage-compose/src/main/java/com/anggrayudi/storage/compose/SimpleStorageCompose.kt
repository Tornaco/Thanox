package com.anggrayudi.storage.compose

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.pm.PackageManager
import android.os.Parcelable
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import com.anggrayudi.storage.R
import com.anggrayudi.storage.SimpleStorageHelper.Companion.redirectToSystemSettings
import com.anggrayudi.storage.contract.FilePickerResult
import com.anggrayudi.storage.contract.FolderPickerResult
import com.anggrayudi.storage.contract.OpenFilePickerContract
import com.anggrayudi.storage.contract.OpenFolderPickerContract
import com.anggrayudi.storage.contract.RequestStorageAccessContract
import com.anggrayudi.storage.contract.RequestStorageAccessResult
import com.anggrayudi.storage.contract.StoragePermissionContract
import com.anggrayudi.storage.contract.StoragePermissionDeniedException
import com.anggrayudi.storage.extension.getStorageId
import com.anggrayudi.storage.file.DocumentFileCompat
import com.anggrayudi.storage.file.FileFullPath
import com.anggrayudi.storage.file.StorageId
import com.anggrayudi.storage.file.StorageType
import com.anggrayudi.storage.file.getStorageId
import com.anggrayudi.storage.permission.PermissionCallback
import com.anggrayudi.storage.permission.PermissionReport
import com.anggrayudi.storage.permission.PermissionRequest
import com.anggrayudi.storage.permission.PermissionResult
import kotlinx.parcelize.Parcelize

class PermissionRequestCompose(
  private val context: Activity,
  private val permissions: Array<String>,
  private val callback: PermissionCallback,
) : PermissionRequest {

  private lateinit var launcher:
    ManagedActivityResultLauncher<Unit, Map<String, @JvmSuppressWildcards Boolean>>

  @Composable
  fun registerLauncher():
    ManagedActivityResultLauncher<Unit, Map<String, @JvmSuppressWildcards Boolean>> {
    launcher =
      rememberLauncherForActivityResult(StoragePermissionContract()) { result ->
        onRequestPermissionsResult(result)
      }
    return launcher
  }

  override fun check() {
    if (
      permissions.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
      }
    ) {
      callback.onPermissionsChecked(
        PermissionResult(
          permissions.map { PermissionReport(it, isGranted = true, deniedPermanently = false) }
        ),
        false,
      )
    } else {
      callback.onDisplayConsentDialog(this)
    }
  }

  private fun onRequestPermissionsResult(result: Map<String, Boolean>) {
    if (result.isEmpty()) {
      callback.onPermissionRequestInterrupted(permissions)
      return
    }
    val reports =
      result.map {
        PermissionReport(
          it.key,
          it.value,
          !it.value && !ActivityCompat.shouldShowRequestPermissionRationale(context, it.key),
        )
      }
    val blockedPermissions = reports.filter { it.deniedPermanently }
    if (blockedPermissions.isEmpty()) {
      callback.onPermissionsChecked(PermissionResult(reports), true)
    } else {
      callback.onShouldRedirectToSystemSettings(blockedPermissions)
    }
  }

  /**
   * If you override [PermissionCallback.onDisplayConsentDialog], then call this method in the
   * `onPositive` callback of the dialog.
   */
  override fun continueToPermissionRequest() {
    permissions.forEach {
      if (ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED) {
        launcher.launch(Unit)
        return
      }
    }
    callback.onPermissionsChecked(
      PermissionResult(
        permissions.map { PermissionReport(it, isGranted = true, deniedPermanently = false) }
      ),
      false,
    )
  }
}

private fun handleMissingActivityHandler(context: Context) {
  Toast.makeText(context, R.string.ss_missing_saf_activity_handler, Toast.LENGTH_SHORT).show()
}

@Composable
fun rememberLauncherForStoragePermission(
  onPermissionsResult: (isGranted: Boolean) -> Unit
): ManagedActivityResultLauncher<Unit, Map<String, @JvmSuppressWildcards Boolean>> {
  val context = LocalActivity.current!!
  val currentOnPermissionsCallback = rememberUpdatedState(onPermissionsResult)

  val request =
    PermissionRequestCompose(
      context,
      arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
      ),
      callback =
        object : PermissionCallback {
          override fun onPermissionsChecked(result: PermissionResult, fromSystemDialog: Boolean) {
            val granted = result.areAllPermissionsGranted
            if (!granted) {
              Toast.makeText(
                  context,
                  R.string.ss_please_grant_storage_permission,
                  Toast.LENGTH_SHORT,
                )
                .show()
            }
            currentOnPermissionsCallback.value(granted)
          }

          override fun onShouldRedirectToSystemSettings(
            blockedPermissions: List<PermissionReport>
          ) {
            redirectToSystemSettings(context)
            currentOnPermissionsCallback.value(false)
          }
        },
    )
  return request.registerLauncher()
}

class StorageAccessLauncher
internal constructor(
  private val context: Context,
  internal var initialPath: FileFullPath?,
  private val permissionLauncher:
    ManagedActivityResultLauncher<Unit, Map<String, @JvmSuppressWildcards Boolean>>,
) {

  internal lateinit var launcher:
    ManagedActivityResultLauncher<RequestStorageAccessContract.Options, RequestStorageAccessResult>

  fun launch() {
    try {
      launcher.launch(RequestStorageAccessContract.Options(initialPath))
    } catch (_: ActivityNotFoundException) {
      handleMissingActivityHandler(context)
    } catch (_: StoragePermissionDeniedException) {
      permissionLauncher.launch(Unit)
    }
  }
}

@Parcelize
internal data class StorageAccessDialogData(
  val message: String,
  val initialPath: FileFullPath? = null,
) : Parcelable

@Composable
fun rememberLauncherForStorageAccess(
  expectedStorageType: StorageType = StorageType.UNKNOWN,
  expectedBasePath: String = "",
  /** It only takes effect on API 26+ */
  initialPath: FileFullPath? = null,
  onStorageAccessGranted: (root: DocumentFile) -> Unit,
): StorageAccessLauncher {
  val context = LocalActivity.current!!
  val currentStorageAccessCallback = rememberUpdatedState(onStorageAccessGranted)
  val permissionLauncher = rememberLauncherForStoragePermission { granted ->
    if (granted) {
      currentStorageAccessCallback.value(
        DocumentFileCompat.getRootDocumentFile(
          context,
          StorageId.PRIMARY,
          requiresWriteAccess = true,
        ) ?: return@rememberLauncherForStoragePermission
      )
    }
  }

  val accessLauncher = remember { StorageAccessLauncher(context, initialPath, permissionLauncher) }

  var dialogData by rememberSaveable { mutableStateOf<StorageAccessDialogData?>(null) }
  if (dialogData != null) {
    AlertDialog(
      text = { Text(dialogData!!.message) },
      properties = DialogProperties(dismissOnClickOutside = false),
      onDismissRequest = { dialogData = null },
      dismissButton = {
        FilledTonalButton(onClick = { dialogData = null }) {
          Text(stringResource(android.R.string.cancel))
        }
      },
      confirmButton = {
        Button(
          onClick = {
            accessLauncher.initialPath = dialogData!!.initialPath
            accessLauncher.launch()
            dialogData = null
          }
        ) {
          Text(stringResource(android.R.string.ok))
        }
      },
    )
  }

  accessLauncher.launcher =
    rememberLauncherForActivityResult(
      RequestStorageAccessContract(context, expectedStorageType, expectedBasePath)
    ) { result ->
      when (result) {
        is RequestStorageAccessResult.RootPathNotSelected -> {
          val storageType =
            if (expectedStorageType.isExpected(result.selectedStorageType)) {
              result.selectedStorageType
            } else {
              expectedStorageType
            }
          val messageRes =
            if (result.rootPath.isEmpty()) {
              context.getString(
                if (storageType == StorageType.SD_CARD) {
                  R.string.ss_please_select_root_storage_sdcard
                } else {
                  R.string.ss_please_select_root_storage_primary
                }
              )
            } else {
              val resId =
                if (storageType == StorageType.SD_CARD) {
                  R.string.ss_please_select_root_storage_sdcard_with_location
                } else {
                  R.string.ss_please_select_root_storage_primary_with_location
                }
              context.getString(resId, result.rootPath)
            }
          dialogData =
            StorageAccessDialogData(
              message = messageRes,
              initialPath = FileFullPath(context, result.uri.getStorageId(context), ""),
            )
        }

        is RequestStorageAccessResult.RootPathPermissionGranted -> {
          currentStorageAccessCallback.value(result.root)
        }

        is RequestStorageAccessResult.ExpectedStorageNotSelected -> {
          val message =
            context.getString(
              when (expectedStorageType) {
                StorageType.EXTERNAL ->
                  R.string.ss_please_select_base_path_with_storage_type_primary
                StorageType.SD_CARD -> R.string.ss_please_select_base_path_with_storage_type_sd_card
                else -> R.string.ss_please_select_base_path
              },
              expectedBasePath,
            )
          @SuppressLint("NewApi")
          dialogData =
            StorageAccessDialogData(
              message = message,
              initialPath =
                FileFullPath(
                  context,
                  if (expectedStorageType == StorageType.UNKNOWN) result.selectedStorageType
                  else expectedStorageType,
                  expectedBasePath,
                ),
            )
        }

        is RequestStorageAccessResult.StoragePermissionDenied -> {
          permissionLauncher.launch(Unit)
        }

        is RequestStorageAccessResult.CanceledByUser -> {
          // no-op, just dismiss the dialog
        }
      }
    }
  return accessLauncher
}

class FilePickerLauncher
internal constructor(
  private val context: Context,
  private val allowMultiple: Boolean = false,
  private val initialPath: FileFullPath? = null,
  private val filterMimeTypes: Set<String> = emptySet(),
) {

  internal lateinit var launcher:
    ManagedActivityResultLauncher<OpenFilePickerContract.Options, FilePickerResult>

  fun launch() {
    try {
      launcher.launch(OpenFilePickerContract.Options(allowMultiple, initialPath, filterMimeTypes))
    } catch (_: ActivityNotFoundException) {
      handleMissingActivityHandler(context)
    }
  }
}

@Composable
fun rememberLauncherForFilePicker(
  allowMultiple: Boolean = false,
  /** It only takes effect on API 26+ */
  initialPath: FileFullPath? = null,
  filterMimeTypes: Set<String> = emptySet(),
  onFilesPicked: (files: List<DocumentFile>) -> Unit,
): FilePickerLauncher {
  val activity = LocalActivity.current!!
  val currentFilePickerCallback = rememberUpdatedState(onFilesPicked)
  val filePickerLauncher = remember {
    FilePickerLauncher(activity, allowMultiple, initialPath, filterMimeTypes)
  }

  var expectedStorageType by remember { mutableStateOf(StorageType.UNKNOWN) }
  val storageAccessLauncher =
    rememberLauncherForStorageAccess(expectedStorageType = expectedStorageType) { root ->
      filePickerLauncher.launch()
    }

  filePickerLauncher.launcher =
    rememberLauncherForActivityResult(OpenFilePickerContract(activity)) { result ->
      when (result) {
        is FilePickerResult.CanceledByUser -> {
          // no-op, just dismiss the dialog
        }

        is FilePickerResult.Picked -> {
          currentFilePickerCallback.value(result.files)
        }

        is FilePickerResult.StoragePermissionDenied -> {
          expectedStorageType =
            result.files.firstOrNull()?.getStorageId(activity)?.let {
              StorageType.fromStorageId(it)
            } ?: StorageType.EXTERNAL
          storageAccessLauncher.launch()
        }
      }
    }
  return filePickerLauncher
}

class FolderPickerLauncher
internal constructor(
  private val context: Context,
  private val initialPath: FileFullPath? = null,
  private val permissionLauncher:
    ManagedActivityResultLauncher<Unit, Map<String, @JvmSuppressWildcards Boolean>>,
) {

  internal lateinit var launcher:
    ManagedActivityResultLauncher<OpenFolderPickerContract.Options, FolderPickerResult>

  fun launch() {
    try {
      launcher.launch(OpenFolderPickerContract.Options(initialPath))
    } catch (_: ActivityNotFoundException) {
      handleMissingActivityHandler(context)
    } catch (_: StoragePermissionDeniedException) {
      permissionLauncher.launch(Unit)
    }
  }
}

@Composable
fun rememberLauncherForFolderPicker(
  /** It only takes effect on API 26+ */
  initialPath: FileFullPath? = null,
  onFolderPicked: (folder: DocumentFile) -> Unit,
): FolderPickerLauncher {
  val activity = LocalActivity.current!!
  val currentFolderPickerCallback = rememberUpdatedState(onFolderPicked)

  val folderPickerLauncher = remember { mutableStateOf<FolderPickerLauncher?>(null) }
  val permissionLauncher = rememberLauncherForStoragePermission { granted ->
    if (granted) {
      folderPickerLauncher.value?.launch()
    }
  }
  folderPickerLauncher.value = FolderPickerLauncher(activity, initialPath, permissionLauncher)

  var initialPath by remember { mutableStateOf<FileFullPath?>(null) }
  val storageAccessLauncher =
    rememberLauncherForStorageAccess(initialPath = initialPath) { root ->
      folderPickerLauncher.value?.launch()
    }

  var showDialog by rememberSaveable { mutableStateOf(false) }
  if (showDialog) {
    AlertDialog(
      text = { Text(stringResource(R.string.ss_storage_access_denied_confirm)) },
      properties = DialogProperties(dismissOnClickOutside = false),
      onDismissRequest = { showDialog = false },
      dismissButton = {
        FilledTonalButton(onClick = { showDialog = false }) {
          Text(stringResource(android.R.string.cancel))
        }
      },
      confirmButton = {
        Button(
          onClick = {
            storageAccessLauncher.launch()
            showDialog = false
          }
        ) {
          Text(stringResource(android.R.string.ok))
        }
      },
    )
  }

  folderPickerLauncher.value?.launcher =
    rememberLauncherForActivityResult(OpenFolderPickerContract(activity)) { result ->
      when (result) {
        is FolderPickerResult.CanceledByUser -> {
          // no-op, just dismiss the dialog
        }

        is FolderPickerResult.Picked -> {
          currentFolderPickerCallback.value(result.folder)
        }

        is FolderPickerResult.AccessDenied -> {
          if (result.storageType == StorageType.UNKNOWN) {
            storageAccessLauncher.launch()
            return@rememberLauncherForActivityResult
          }
          initialPath = FileFullPath(activity, result.storageId, "")
          showDialog = true
        }
      }
    }
  return folderPickerLauncher.value!!
}
