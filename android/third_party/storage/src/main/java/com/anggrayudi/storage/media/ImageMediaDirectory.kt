package com.anggrayudi.storage.media

import android.os.Environment

/**
 * Created on 06/09/20
 *
 * @author Anggrayudi H
 */
enum class ImageMediaDirectory(val folderName: String) {
  PICTURES(Environment.DIRECTORY_PICTURES),
  DCIM(Environment.DIRECTORY_DCIM),
}
