package com.anggrayudi.storage.result

import androidx.documentfile.provider.DocumentFile
import com.anggrayudi.storage.callback.SingleFolderConflictCallback.ConflictResolution

/**
 * Created on 7/6/24
 *
 * @author Anggrayudi Hardiannico A.
 */
sealed class MultipleFilesResult {
  data object Validating : MultipleFilesResult()

  data object Preparing : MultipleFilesResult()

  data object CountingFiles : MultipleFilesResult()

  data object DeletingConflictedFiles : MultipleFilesResult()

  data class Starting(val files: List<DocumentFile>, val totalFilesToCopy: Int) :
    MultipleFilesResult()

  /** @param fileCount total files/folders that are successfully copied/moved */
  data class InProgress(
    val progress: Float,
    val bytesMoved: Long,
    val writeSpeed: Int,
    val fileCount: Int,
  ) : MultipleFilesResult()

  /**
   * If `totalCopiedFiles` are less than `totalFilesToCopy`, then some files cannot be copied/moved
   * or the files are skipped due to [ConflictResolution.MERGE]
   *
   * @param files newly moved/copied parent files/folders
   * @param success `true` if the process is not canceled and no error during copy/move
   * @param totalFilesToCopy total files, not folders
   * @param totalCopiedFiles total files, not folders
   */
  data class Completed(
    val files: List<DocumentFile>,
    val totalFilesToCopy: Int,
    val totalCopiedFiles: Int,
    val success: Boolean,
  ) : MultipleFilesResult()

  data class Error(
    val errorCode: MultipleFilesErrorCode,
    val message: String? = null,
    val completedData: Completed? = null,
  ) : MultipleFilesResult()
}

enum class MultipleFilesErrorCode {
  STORAGE_PERMISSION_DENIED,
  CANNOT_CREATE_FILE_IN_TARGET,
  SOURCE_FILE_NOT_FOUND,
  INVALID_TARGET_FOLDER,
  UNKNOWN_IO_ERROR,
  CANCELED,
  NO_SPACE_LEFT_ON_TARGET_PATH,
}
