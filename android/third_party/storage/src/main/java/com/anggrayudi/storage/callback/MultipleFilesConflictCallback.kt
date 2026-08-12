package com.anggrayudi.storage.callback

import androidx.annotation.UiThread
import androidx.documentfile.provider.DocumentFile
import com.anggrayudi.storage.callback.SingleFileConflictCallback.FileConflictAction
import com.anggrayudi.storage.callback.SingleFolderConflictCallback.ConflictResolution
import com.anggrayudi.storage.result.FolderErrorCode
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope

/**
 * Created on 31/05/21
 *
 * @author Anggrayudi H
 */
abstract class MultipleFilesConflictCallback
@OptIn(DelicateCoroutinesApi::class)
@JvmOverloads
constructor(var uiScope: CoroutineScope = GlobalScope) {
  /**
   * The reason can be one of:
   * * [FolderErrorCode.SOURCE_FILE_NOT_FOUND]
   * * [FolderErrorCode.STORAGE_PERMISSION_DENIED]
   * * [FolderErrorCode.TARGET_FOLDER_CANNOT_HAVE_SAME_PATH_WITH_SOURCE_FOLDER]
   */
  @UiThread
  open fun onInvalidSourceFilesFound(
    invalidSourceFiles: Map<DocumentFile, FolderErrorCode>,
    action: InvalidSourceFilesAction,
  ) {
    action.confirmResolution(false)
  }

  /**
   * Do not call `super` when you override this function.
   *
   * The thread that does copy/move will be suspended until the user gives an answer via
   * [FileConflictAction.confirmResolution]. You have to give an answer, or the thread will be alive
   * until the app is killed and end up as a zombie thread. If you want to cancel, just pass
   * [ConflictResolution.SKIP] into [FileConflictAction.confirmResolution]. If the worker thread is
   * suspended for too long, it may be interrupted by the system.
   */
  @UiThread
  open fun onParentConflict(
    destinationParentFolder: DocumentFile,
    conflictedFolders: MutableList<ParentConflict>,
    conflictedFiles: MutableList<ParentConflict>,
    action: ParentFolderConflictAction,
  ) {
    action.confirmResolution(conflictedFiles)
  }

  @UiThread
  open fun onContentConflict(
    destinationParentFolder: DocumentFile,
    conflictedFiles: MutableList<SingleFolderConflictCallback.FileConflict>,
    action: SingleFolderConflictCallback.FolderContentConflictAction,
  ) {
    action.confirmResolution(conflictedFiles)
  }

  class InvalidSourceFilesAction(private val continuation: CancellableContinuation<Boolean>) {

    /** @param abort stop the process */
    fun confirmResolution(abort: Boolean) {
      continuation.resumeWith(Result.success(abort))
    }
  }

  class ParentFolderConflictAction(
    private val continuation: CancellableContinuation<List<ParentConflict>>
  ) {

    fun confirmResolution(resolution: List<ParentConflict>) {
      continuation.resumeWith(Result.success(resolution))
    }
  }

  class ParentConflict(
    val source: DocumentFile,
    val target: DocumentFile,
    val canMerge: Boolean,
    var solution: ConflictResolution = ConflictResolution.CREATE_NEW,
  )
}
