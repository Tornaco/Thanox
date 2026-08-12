package com.anggrayudi.storage.media

import com.anggrayudi.storage.file.MimeType

/**
 * Created on 05/09/20
 *
 * @author Anggrayudi H
 */
class FileDescription @JvmOverloads constructor(var name: String, var subFolder: String = "") {
  private var _mimeType: String? = MimeType.BINARY_FILE

  val mimeType: String
    get() {
      var type = _mimeType
      if (
        type.isNullOrEmpty() ||
          MimeType.hasExtension(name) && (type == MimeType.BINARY_FILE || type == MimeType.UNKNOWN)
      ) {
        type = MimeType.getMimeTypeFromFileName(name)
      }
      _mimeType = type
      return type
    }

  constructor(name: String, subFolder: String, mimeType: String?) : this(name, subFolder) {
    _mimeType = mimeType?.takeIf { !it.contains("*") }
  }

  val fullName: String
    get() = MimeType.getFullFileName(name, mimeType)
}
