package com.anggrayudi.storage.file

import android.webkit.MimeTypeMap
import com.anggrayudi.storage.extension.normalizeFileName

/**
 * See [mime type list](https://www.freeformatter.com/mime-types-list.html)
 *
 * Created on 03/06/21
 *
 * @author Anggrayudi H
 */
object MimeType {
  const val UNKNOWN = "*/*"
  const val BINARY_FILE = "application/octet-stream"
  const val ZIP = "application/zip"
  const val IMAGE = "image/*"
  const val AUDIO = "audio/*"
  const val VIDEO = "video/*"
  const val TEXT = "text/*"
  const val FONT = "font/*"
  const val APPLICATION = "application/*"
  const val CHEMICAL = "chemical/*"
  const val MODEL = "model/*"

  /**
   * * Given `name` = `ABC` AND `mimeType` = `video/mp4`, then return `ABC.mp4`
   * * Given `name` = `ABC` AND `mimeType` = `null`, then return `ABC`
   * * Given `name` = `ABC.mp4` AND `mimeType` = `video/mp4`, then return `ABC.mp4`
   *
   * @param name can have file extension or not
   */
  @JvmStatic
  fun getFullFileName(name: String, mimeType: String?): String {
    // Prior to API 29, MimeType.BINARY_FILE has no file extension
    val cleanName = name.normalizeFileName()
    if (mimeType == BINARY_FILE || mimeType == UNKNOWN) {
      val extension = getExtensionFromFileName(cleanName)
      if (extension.isNotEmpty()) {
        return cleanName
      }
    }
    return getExtensionFromMimeType(mimeType).let {
      if (it.isEmpty() || cleanName.endsWith(".$it")) cleanName else "$cleanName.$it".trimEnd('.')
    }
  }

  /**
   * Some mime types return no file extension on older API levels. This function adds compatibility
   * across API levels. Since API 29, [MimeType.BINARY_FILE] has extension `*.bin`
   *
   * @see getExtensionFromMimeTypeOrFileName
   */
  @JvmStatic
  fun getExtensionFromMimeType(mimeType: String?): String {
    return mimeType
      ?.let {
        if (it == BINARY_FILE) "bin" else MimeTypeMap.getSingleton().getExtensionFromMimeType(it)
      }
      .orEmpty()
  }

  @JvmStatic
  fun getBaseFileName(filename: String?): String {
    return if (hasExtension(filename)) filename.orEmpty().substringBeforeLast('.')
    else filename.orEmpty()
  }

  @JvmStatic
  fun getExtensionFromFileName(filename: String?): String {
    return if (hasExtension(filename)) filename.orEmpty().substringAfterLast('.', "") else ""
  }

  /**
   * File extensions must be alphanumeric. The following filenames are considered as have no
   * extension:
   * * `abc.pq rs`
   * * `abc.あん`
   */
  @JvmStatic
  fun hasExtension(filename: String?) = filename?.matches(Regex("(.*?)\\.[a-zA-Z0-9]+")) == true

  /** @see getExtensionFromMimeType */
  @JvmStatic
  fun getExtensionFromMimeTypeOrFileName(mimeType: String?, filename: String): String {
    return if (mimeType == null || mimeType == UNKNOWN) getExtensionFromFileName(filename)
    else getExtensionFromMimeType(mimeType)
  }

  /**
   * Some file types return no mime type on older API levels. This function adds compatibility
   * accross API levels.
   */
  @JvmStatic
  fun getMimeTypeFromExtension(fileExtension: String): String {
    return if (fileExtension.equals("bin", ignoreCase = true)) BINARY_FILE
    else MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension) ?: UNKNOWN
  }

  @JvmStatic
  fun getMimeTypeFromFileName(filename: String?): String {
    return getMimeTypeFromExtension(getExtensionFromFileName(filename))
  }
}
