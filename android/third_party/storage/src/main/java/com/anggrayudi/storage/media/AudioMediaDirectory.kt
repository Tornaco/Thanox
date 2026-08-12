package com.anggrayudi.storage.media

import android.os.Environment

/**
 * Created on 06/09/20
 *
 * @author Anggrayudi H
 */
enum class AudioMediaDirectory(val folderName: String) {
  MUSIC(Environment.DIRECTORY_MUSIC),
  PODCASTS(Environment.DIRECTORY_PODCASTS),
  RINGTONES(Environment.DIRECTORY_RINGTONES),
  ALARMS(Environment.DIRECTORY_ALARMS),
  NOTIFICATIONS(Environment.DIRECTORY_NOTIFICATIONS),
}
