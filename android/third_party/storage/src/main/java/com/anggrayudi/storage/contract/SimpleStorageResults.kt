package com.anggrayudi.storage.contract

import android.content.Intent
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.anggrayudi.storage.file.StorageType

sealed class FolderPickerResult {
  data class Picked(val folder: DocumentFile) : FolderPickerResult()

  data class AccessDenied(
    val folder: DocumentFile?,
    val storageType: StorageType,
    val storageId: String,
  ) : FolderPickerResult()

  data object CanceledByUser : FolderPickerResult()
}

sealed class FilePickerResult {
  data class Picked(val files: List<DocumentFile>) : FilePickerResult()

  data object CanceledByUser : FilePickerResult()

  /** @see [StoragePermissionDeniedException] */
  data class StoragePermissionDenied(val files: List<DocumentFile>) : FilePickerResult()
}

sealed class FileCreationResult {
  data class Created(val file: DocumentFile) : FileCreationResult()

  data object CanceledByUser : FileCreationResult()

  /** @see [StoragePermissionDeniedException] */
  data object StoragePermissionDenied : FileCreationResult()
}

sealed class RequestStorageAccessResult {
  data object CanceledByUser : RequestStorageAccessResult()

  /** @see [StoragePermissionDeniedException] */
  data object StoragePermissionDenied : RequestStorageAccessResult()

  /** Triggered on Android 10 and lower. */
  data class RootPathNotSelected(
    val rootPath: String,
    val uri: Uri,
    val selectedStorageType: StorageType,
    val expectedStorageType: StorageType,
    /** Expected [Intent] to launch when the root path is not selected. */
    val expectedIntent: Intent? = null,
  ) : RequestStorageAccessResult()

  /** Triggered on Android 11 and higher. */
  data class ExpectedStorageNotSelected(
    val selectedFolder: DocumentFile,
    val selectedStorageType: StorageType,
    val expectedBasePath: String,
    val expectedStorageType: StorageType,
  ) : RequestStorageAccessResult()

  data class RootPathPermissionGranted(val root: DocumentFile) : RequestStorageAccessResult()
}
