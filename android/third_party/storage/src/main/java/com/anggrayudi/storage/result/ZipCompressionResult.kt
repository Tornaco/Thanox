package com.anggrayudi.storage.result

import androidx.documentfile.provider.DocumentFile

/**
 * Created on 7/6/24
 *
 * @author Anggrayudi Hardiannico A.
 */
sealed class ZipCompressionResult {
  data object CountingFiles : ZipCompressionResult()

  data class Compressing(
    val progress: Float,
    val bytesCompressed: Long,
    val writeSpeed: Int,
    val fileCount: Int,
  ) : ZipCompressionResult()

  data class Completed(
    val zipFile: DocumentFile,
    val bytesCompressed: Long,
    val totalFilesCompressed: Int,
    val compressionRate: Float,
  ) : ZipCompressionResult()

  data object DeletingEntryFiles : ZipCompressionResult()

  data class Error(val errorCode: ZipCompressionErrorCode, val message: String? = null) :
    ZipCompressionResult()
}

enum class ZipCompressionErrorCode {
  STORAGE_PERMISSION_DENIED,
  CANNOT_CREATE_FILE_IN_TARGET,
  MISSING_ENTRY_FILE,
  DUPLICATE_ENTRY_FILE,
  UNKNOWN_IO_ERROR,
  CANCELED,
  NO_SPACE_LEFT_ON_TARGET_PATH,
}
