package com.anggrayudi.storage.callback

import android.content.Intent
import androidx.documentfile.provider.DocumentFile

/**
 * Created on 17/08/20
 *
 * @author Anggrayudi H
 */
interface FilePickerCallback {

  fun onCanceledByUser(requestCode: Int) {
    // default implementation
  }

  fun onActivityHandlerNotFound(requestCode: Int, intent: Intent) {
    // default implementation
  }

  /** Called when you have no read permission to current path */
  fun onStoragePermissionDenied(requestCode: Int, files: List<DocumentFile>?)

  /** @param files non-empty list */
  fun onFileSelected(requestCode: Int, files: List<DocumentFile>)
}
