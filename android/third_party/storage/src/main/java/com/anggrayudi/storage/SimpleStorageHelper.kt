package com.anggrayudi.storage

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import com.anggrayudi.storage.SimpleStorage.Companion.LIBRARY_PACKAGE_NAME
import com.anggrayudi.storage.callback.CreateFileCallback
import com.anggrayudi.storage.callback.FilePickerCallback
import com.anggrayudi.storage.callback.FolderPickerCallback
import com.anggrayudi.storage.callback.StorageAccessCallback
import com.anggrayudi.storage.extension.getStorageId
import com.anggrayudi.storage.file.FileFullPath
import com.anggrayudi.storage.file.StorageType
import com.anggrayudi.storage.file.getAbsolutePath
import com.anggrayudi.storage.permission.ActivityPermissionRequest
import com.anggrayudi.storage.permission.FragmentPermissionRequest
import com.anggrayudi.storage.permission.PermissionCallback
import com.anggrayudi.storage.permission.PermissionReport
import com.anggrayudi.storage.permission.PermissionRequest
import com.anggrayudi.storage.permission.PermissionResult

/**
 * Helper class to ease you using file & folder picker.
 *
 * @author Anggrayudi H.
 */
class SimpleStorageHelper {

  val storage: SimpleStorage
  private val permissionRequest: PermissionRequest

  private var originalRequestCode = 0
  private var pickerToOpenOnceGranted = 0
  private var filterMimeTypes: Set<String>? = null
  private var onPermissionsResult: ((Boolean) -> Unit)? = null

  // For unknown Activity type
  @JvmOverloads
  constructor(activity: Activity, requestCodeForPermissionDialog: Int, savedState: Bundle? = null) {
    storage = SimpleStorage(activity)
    init(savedState)
    permissionRequest =
      ActivityPermissionRequest.Builder(activity, requestCodeForPermissionDialog)
        .withPermissions(*rwPermission)
        .withCallback(permissionCallback)
        .build()
  }

  @JvmOverloads
  constructor(activity: ComponentActivity, savedState: Bundle? = null) {
    storage = SimpleStorage(activity)
    init(savedState)
    permissionRequest =
      ActivityPermissionRequest.Builder(activity)
        .withPermissions(*rwPermission)
        .withCallback(permissionCallback)
        .build()
  }

  @JvmOverloads
  constructor(fragment: Fragment, savedState: Bundle? = null) {
    storage = SimpleStorage(fragment)
    init(savedState)
    permissionRequest =
      FragmentPermissionRequest.Builder(fragment)
        .withPermissions(*rwPermission)
        .withCallback(permissionCallback)
        .build()
  }

  var onStorageAccessGranted: ((requestCode: Int, root: DocumentFile) -> Unit)? = null

  var onFolderSelected: ((requestCode: Int, folder: DocumentFile) -> Unit)? = null
    set(callback) {
      field = callback
      storage.folderPickerCallback =
        object : FolderPickerCallback {
          override fun onFolderSelected(requestCode: Int, folder: DocumentFile) {
            reset()
            callback?.invoke(requestCode, folder)
          }

          @SuppressLint("NewApi")
          override fun onStorageAccessDenied(
            requestCode: Int,
            folder: DocumentFile?,
            storageType: StorageType,
            storageId: String,
          ) {
            if (storageType == StorageType.UNKNOWN) {
              onStoragePermissionDenied(requestCode)
              return
            }
            AlertDialog.Builder(storage.context)
              .setCancelable(false)
              .setMessage(R.string.ss_storage_access_denied_confirm)
              .setNegativeButton(android.R.string.cancel) { _, _ -> reset() }
              .setPositiveButton(android.R.string.ok) { _, _ ->
                storage.requestStorageAccess(
                  initialPath = FileFullPath(storage.context, storageId, "")
                )
              }
              .show()
          }

          override fun onStoragePermissionDenied(requestCode: Int) {
            requestStoragePermission { if (it) storage.openFolderPicker() else reset() }
          }

          override fun onCanceledByUser(requestCode: Int) {
            reset()
          }

          override fun onActivityHandlerNotFound(requestCode: Int, intent: Intent) {
            handleMissingActivityHandler()
          }
        }
    }

  var onFileSelected:
    ((requestCode: Int, /* non-empty list */ files: List<DocumentFile>) -> Unit)? =
    null
    set(callback) {
      field = callback
      storage.filePickerCallback =
        object : FilePickerCallback {
          override fun onStoragePermissionDenied(requestCode: Int, files: List<DocumentFile>?) {
            requestStoragePermission { if (it) storage.openFilePicker() else reset() }
          }

          override fun onFileSelected(requestCode: Int, files: List<DocumentFile>) {
            reset()
            callback?.invoke(requestCode, files)
          }

          override fun onCanceledByUser(requestCode: Int) {
            reset()
          }

          override fun onActivityHandlerNotFound(requestCode: Int, intent: Intent) {
            handleMissingActivityHandler()
          }
        }
    }

  var onFileCreated: ((requestCode: Int, file: DocumentFile) -> Unit)? = null
    set(callback) {
      field = callback
      storage.createFileCallback =
        object : CreateFileCallback {
          override fun onCanceledByUser(requestCode: Int) {
            reset()
          }

          override fun onActivityHandlerNotFound(requestCode: Int, intent: Intent) {
            handleMissingActivityHandler()
          }

          override fun onFileCreated(requestCode: Int, file: DocumentFile) {
            reset()
            callback?.invoke(requestCode, file)
          }
        }
    }

  @SuppressLint("NewApi")
  private fun init(savedState: Bundle?) {
    savedState?.let { onRestoreInstanceState(it) }
    storage.storageAccessCallback =
      object : StorageAccessCallback {
        override fun onRootPathNotSelected(
          requestCode: Int,
          rootPath: String,
          uri: Uri,
          selectedStorageType: StorageType,
          expectedStorageType: StorageType,
        ) {
          val storageType =
            if (expectedStorageType.isExpected(selectedStorageType)) selectedStorageType
            else expectedStorageType
          val messageRes =
            if (rootPath.isEmpty()) {
              storage.context.getString(
                if (storageType == StorageType.SD_CARD)
                  R.string.ss_please_select_root_storage_sdcard
                else R.string.ss_please_select_root_storage_primary
              )
            } else {
              val resId =
                if (storageType == StorageType.SD_CARD)
                  R.string.ss_please_select_root_storage_sdcard_with_location
                else R.string.ss_please_select_root_storage_primary_with_location
              storage.context.getString(resId, rootPath)
            }
          AlertDialog.Builder(storage.context)
            .setCancelable(false)
            .setMessage(messageRes)
            .setNegativeButton(android.R.string.cancel) { _, _ -> reset() }
            .setPositiveButton(android.R.string.ok) { _, _ ->
              storage.requestStorageAccess(
                initialPath = FileFullPath(storage.context, uri.getStorageId(storage.context), ""),
                expectedStorageType = expectedStorageType,
              )
            }
            .show()
        }

        override fun onRootPathPermissionGranted(requestCode: Int, root: DocumentFile) {
          // if the original request was only asking for storage access, then stop here
          if (requestCode == originalRequestCode) {
            reset()
            onStorageAccessGranted?.invoke(requestCode, root)
            return
          }

          val context = storage.context
          val toastFilePicker: () -> Unit = {
            Toast.makeText(
                context,
                context.getString(
                  R.string.ss_selecting_root_path_success_with_open_folder_picker,
                  root.getAbsolutePath(context),
                ),
                Toast.LENGTH_LONG,
              )
              .show()
          }

          when (pickerToOpenOnceGranted) {
            TYPE_FILE_PICKER -> {
              storage.openFilePicker(filterMimeTypes = filterMimeTypes.orEmpty().toTypedArray())
              toastFilePicker()
            }
            TYPE_FOLDER_PICKER -> {
              storage.openFolderPicker()
              toastFilePicker()
            }
            else -> {
              Toast.makeText(
                  context,
                  context.getString(
                    R.string.ss_selecting_root_path_success_without_open_folder_picker,
                    root.getAbsolutePath(context),
                  ),
                  Toast.LENGTH_SHORT,
                )
                .show()
            }
          }
          reset()
        }

        override fun onExpectedStorageNotSelected(
          requestCode: Int,
          selectedFolder: DocumentFile,
          selectedStorageType: StorageType,
          expectedBasePath: String,
          expectedStorageType: StorageType,
        ) {
          val message =
            storage.context.getString(
              when (expectedStorageType) {
                StorageType.EXTERNAL ->
                  R.string.ss_please_select_base_path_with_storage_type_primary
                StorageType.SD_CARD -> R.string.ss_please_select_base_path_with_storage_type_sd_card
                else -> R.string.ss_please_select_base_path
              },
              expectedBasePath,
            )
          AlertDialog.Builder(storage.context)
            .setCancelable(false)
            .setMessage(message)
            .setNegativeButton(android.R.string.cancel) { _, _ -> reset() }
            .setPositiveButton(android.R.string.ok) { _, _ ->
              storage.requestStorageAccess(
                initialPath =
                  FileFullPath(
                    storage.context,
                    if (expectedStorageType == StorageType.UNKNOWN) selectedStorageType
                    else expectedStorageType,
                    expectedBasePath,
                  ),
                expectedStorageType = expectedStorageType,
                expectedBasePath = expectedBasePath,
              )
            }
            .show()
        }

        override fun onStoragePermissionDenied(requestCode: Int) {
          requestStoragePermission { if (it) storage.openFolderPicker() else reset() }
        }

        override fun onCanceledByUser(requestCode: Int) {
          reset()
        }

        override fun onActivityHandlerNotFound(requestCode: Int, intent: Intent) {
          handleMissingActivityHandler()
        }
      }
  }

  private fun requestStoragePermission(onResult: (Boolean) -> Unit) {
    onPermissionsResult = onResult
    permissionRequest.check()
  }

  /** Mandatory for [Activity], but not for [Fragment] and [ComponentActivity] */
  fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray,
  ) {
    if (permissionRequest is ActivityPermissionRequest) {
      permissionRequest.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
  }

  private val permissionCallback: PermissionCallback
    get() =
      object : PermissionCallback {
        override fun onPermissionsChecked(result: PermissionResult, fromSystemDialog: Boolean) {
          val granted = result.areAllPermissionsGranted
          if (!granted) {
            Toast.makeText(
                storage.context,
                R.string.ss_please_grant_storage_permission,
                Toast.LENGTH_SHORT,
              )
              .show()
          }
          onPermissionsResult?.invoke(granted)
          onPermissionsResult = null
        }

        override fun onShouldRedirectToSystemSettings(blockedPermissions: List<PermissionReport>) {
          redirectToSystemSettings(storage.context)
          onPermissionsResult?.invoke(false)
          onPermissionsResult = null
        }
      }

  private val rwPermission: Array<String>
    get() =
      arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

  private fun reset() {
    pickerToOpenOnceGranted = 0
    originalRequestCode = 0
    filterMimeTypes = null
  }

  private fun handleMissingActivityHandler() {
    reset()
    Toast.makeText(storage.context, R.string.ss_missing_saf_activity_handler, Toast.LENGTH_SHORT)
      .show()
  }

  @JvmOverloads
  fun openFolderPicker(
    requestCode: Int = storage.requestCodeFolderPicker,
    initialPath: FileFullPath? = null,
  ) {
    pickerToOpenOnceGranted = TYPE_FOLDER_PICKER
    originalRequestCode = requestCode
    storage.openFolderPicker(requestCode, initialPath)
  }

  @JvmOverloads
  fun openFilePicker(
    requestCode: Int = storage.requestCodeFilePicker,
    allowMultiple: Boolean = false,
    initialPath: FileFullPath? = null,
    vararg filterMimeTypes: String,
  ) {
    pickerToOpenOnceGranted = TYPE_FILE_PICKER
    originalRequestCode = requestCode
    filterMimeTypes.toSet().let {
      this.filterMimeTypes = it
      storage.openFilePicker(requestCode, allowMultiple, initialPath, *it.toTypedArray())
    }
  }

  @JvmOverloads
  fun requestStorageAccess(
    requestCode: Int = storage.requestCodeStorageAccess,
    initialPath: FileFullPath? = null,
    expectedStorageType: StorageType = StorageType.UNKNOWN,
    expectedBasePath: String = "",
  ) {
    pickerToOpenOnceGranted = 0
    originalRequestCode = requestCode
    storage.requestStorageAccess(requestCode, initialPath, expectedStorageType, expectedBasePath)
  }

  @JvmOverloads
  fun createFile(
    mimeType: String,
    fileName: String? = null,
    initialPath: FileFullPath? = null,
    requestCode: Int = storage.requestCodeCreateFile,
  ) {
    pickerToOpenOnceGranted = 0
    originalRequestCode = requestCode
    storage.createFile(mimeType, fileName, initialPath, requestCode)
  }

  fun onSaveInstanceState(outState: Bundle) {
    storage.onSaveInstanceState(outState)
    outState.putInt(KEY_ORIGINAL_REQUEST_CODE, originalRequestCode)
    outState.putInt(KEY_OPEN_FOLDER_PICKER_ONCE_GRANTED, pickerToOpenOnceGranted)
    filterMimeTypes?.let { outState.putStringArray(KEY_FILTER_MIME_TYPES, it.toTypedArray()) }
  }

  fun onRestoreInstanceState(savedInstanceState: Bundle) {
    storage.onRestoreInstanceState(savedInstanceState)
    originalRequestCode = savedInstanceState.getInt(KEY_ORIGINAL_REQUEST_CODE)
    pickerToOpenOnceGranted = savedInstanceState.getInt(KEY_OPEN_FOLDER_PICKER_ONCE_GRANTED)
    filterMimeTypes = savedInstanceState.getStringArray(KEY_FILTER_MIME_TYPES)?.toSet()
  }

  interface OnFileReceived {
    fun onFileReceived(files: List<DocumentFile>)

    fun onNonFileReceived(intent: Intent) {
      // default implementation
    }
  }

  companion object {
    const val TYPE_FILE_PICKER = 1
    const val TYPE_FOLDER_PICKER = 2

    private const val KEY_OPEN_FOLDER_PICKER_ONCE_GRANTED =
      LIBRARY_PACKAGE_NAME + ".pickerToOpenOnceGranted"
    private const val KEY_ORIGINAL_REQUEST_CODE =
      LIBRARY_PACKAGE_NAME + ".originalRequestCode"
    private const val KEY_FILTER_MIME_TYPES = LIBRARY_PACKAGE_NAME + ".filterMimeTypes"

    @JvmStatic
    fun redirectToSystemSettings(context: Context) {
      AlertDialog.Builder(context)
        .setMessage(R.string.ss_storage_permission_permanently_disabled)
        .setNegativeButton(android.R.string.cancel) { _, _ -> }
        .setPositiveButton(android.R.string.ok) { _, _ ->
          val intentSetting =
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                "package:${context.packageName}".toUri(),
              )
              .addCategory(Intent.CATEGORY_DEFAULT)
              .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
          context.startActivity(intentSetting)
        }
        .show()
    }
  }
}
