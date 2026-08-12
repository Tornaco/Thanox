package com.anggrayudi.storage.callback

import android.content.Intent
import androidx.documentfile.provider.DocumentFile

/**
 * Created on 06/08/21
 *
 * @author Anggrayudi H
 */
interface FileReceiverCallback {

  fun onFileReceived(files: List<DocumentFile>)

  fun onNonFileReceived(intent: Intent)
}
