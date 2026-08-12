package com.anggrayudi.storage.result

import androidx.documentfile.provider.DocumentFile
import com.anggrayudi.storage.media.MediaFile

/**
 * Created on 7/6/24
 *
 * @author Anggrayudi Hardiannico A.
 */
sealed class SingleFileResult {
  data object Validating : SingleFileResult()

  data object Preparing : SingleFileResult()

  data object CountingFiles : SingleFileResult()

  data object DeletingConflictedFile : SingleFileResult()

  data class Starting(val files: List<DocumentFile>, val totalFilesToCopy: Int) :
    SingleFileResult()

  data class InProgress(val progress: Float, val bytesMoved: Long, val writeSpeed: Int) :
    SingleFileResult()

  /** @param result can be [DocumentFile] or [MediaFile] */
  data class Completed(val result: Any) : SingleFileResult()

  data class Error(val errorCode: SingleFileErrorCode, val message: String? = null) :
    SingleFileResult()
}

enum class SingleFileErrorCode {
  STORAGE_PERMISSION_DENIED,
  CANNOT_CREATE_FILE_IN_TARGET,
  SOURCE_FILE_NOT_FOUND,
  TARGET_FILE_NOT_FOUND,
  TARGET_FOLDER_NOT_FOUND,
  UNKNOWN_IO_ERROR,
  CANCELED,
  TARGET_FOLDER_CANNOT_HAVE_SAME_PATH_WITH_SOURCE_FOLDER,
  NO_SPACE_LEFT_ON_TARGET_PATH,
}
