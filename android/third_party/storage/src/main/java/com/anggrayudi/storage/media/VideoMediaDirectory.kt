package com.anggrayudi.storage.media

import android.os.Environment

/**
 * Created on 06/09/20
 *
 * @author Anggrayudi H
 */
enum class VideoMediaDirectory(val folderName: String) {
  MOVIES(Environment.DIRECTORY_MOVIES),
  DCIM(Environment.DIRECTORY_DCIM),
}
