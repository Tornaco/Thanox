package com.anggrayudi.storage.file

import android.os.Environment
import java.io.File

/**
 * Created on 05/09/20
 *
 * @author Anggrayudi H
 */
enum class PublicDirectory(val folderName: String) {

  /** @see DocumentFileCompat.fromPublicFolder */
  DOWNLOADS(Environment.DIRECTORY_DOWNLOADS),

  /**
   * Returns `null` if you have no URI permissions for read and write in Android 10.
   *
   * @see DocumentFileCompat.fromPublicFolder
   */
  MUSIC(Environment.DIRECTORY_MUSIC),

  /**
   * Returns `null` if you have no URI permissions for read and write in Android 10.
   *
   * @see DocumentFileCompat.fromPublicFolder
   */
  PODCASTS(Environment.DIRECTORY_PODCASTS),

  /**
   * Returns `null` if you have no URI permissions for read and write in Android 10.
   *
   * @see DocumentFileCompat.fromPublicFolder
   */
  RINGTONES(Environment.DIRECTORY_RINGTONES),

  /**
   * Returns `null` if you have no URI permissions for read and write in Android 10.
   *
   * @see DocumentFileCompat.fromPublicFolder
   */
  ALARMS(Environment.DIRECTORY_ALARMS),

  /**
   * Returns `null` if you have no URI permissions for read and write in Android 10.
   *
   * @see DocumentFileCompat.fromPublicFolder
   */
  NOTIFICATIONS(Environment.DIRECTORY_NOTIFICATIONS),

  /**
   * Returns `null` if you have no URI permissions for read and write in Android 10.
   *
   * @see DocumentFileCompat.fromPublicFolder
   */
  PICTURES(Environment.DIRECTORY_PICTURES),

  /**
   * Returns `null` if you have no URI permissions for read and write in Android 10.
   *
   * @see DocumentFileCompat.fromPublicFolder
   */
  MOVIES(Environment.DIRECTORY_MOVIES),

  /**
   * Returns `null` if you have no URI permissions for read and write in Android 10.
   *
   * @see DocumentFileCompat.fromPublicFolder
   */
  DCIM(Environment.DIRECTORY_DCIM),

  /**
   * Returns `null` if you have no URI permissions for read and write in Android 10.
   *
   * @see DocumentFileCompat.fromPublicFolder
   */
  DOCUMENTS(Environment.DIRECTORY_DOCUMENTS);

  val file: File
    get() = Environment.getExternalStoragePublicDirectory(folderName)

  val absolutePath: String
    get() = file.absolutePath
}
