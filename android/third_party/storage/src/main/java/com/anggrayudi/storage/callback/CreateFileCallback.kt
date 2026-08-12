package com.anggrayudi.storage.callback

import android.content.Intent
import androidx.documentfile.provider.DocumentFile

/**
 * Created on 17/08/20
 *
 * @author Anggrayudi H
 */
interface CreateFileCallback {

  fun onCanceledByUser(requestCode: Int) {
    // default implementation
  }

  fun onActivityHandlerNotFound(requestCode: Int, intent: Intent) {
    // default implementation
  }

  fun onFileCreated(requestCode: Int, file: DocumentFile)
}
