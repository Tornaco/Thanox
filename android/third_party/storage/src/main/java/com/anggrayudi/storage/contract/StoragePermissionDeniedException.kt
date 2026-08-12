package com.anggrayudi.storage.contract

/**
 * Thrown when `android.permission.READ_EXTERNAL_STORAGE` and/or
 * `android.permission.WRITE_EXTERNAL_STORAGE` is not granted.
 */
class StoragePermissionDeniedException(
  message: String? =
    "Please grant permissions in manifest for android.permission.READ_EXTERNAL_STORAGE and android.permission.WRITE_EXTERNAL_STORAGE"
) : SecurityException(message)
