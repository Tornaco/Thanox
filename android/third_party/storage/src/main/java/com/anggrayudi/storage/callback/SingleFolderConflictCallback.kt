package com.anggrayudi.storage.callback

import androidx.annotation.RestrictTo
import androidx.annotation.UiThread
import androidx.documentfile.provider.DocumentFile
import com.anggrayudi.storage.callback.SingleFileConflictCallback.FileConflictAction
import com.anggrayudi.storage.file.CreateMode
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope

/**
 * Created on 3/1/21
 *
 * @author Anggrayudi H
 */
abstract class SingleFolderConflictCallback
@OptIn(DelicateCoroutinesApi::class)
@JvmOverloads
constructor(var uiScope: CoroutineScope = GlobalScope) {

  /**
   * Do not call `super` when you override this function.
   *
   * The thread that does copy/move will be suspended until the user gives an answer via
   * [FileConflictAction.confirmResolution]. You have to give an answer, or the thread will be alive
   * until the app is killed and end up as a zombie thread. If you want to cancel, just pass
   * [ConflictResolution.SKIP] into [FileConflictAction.confirmResolution]. If the worker thread is
   * suspended for too long, it may be interrupted by the system.
   *
   * @param destinationFolder when copying `/storage/AAAA-BBBB/Movie` to
   *   `/storage/AAAA-BBBB/Others`, it will be `/storage/AAAA-BBBB/Others/Movie`
   * @param canMerge when conflict found, action `MERGE` may not exists. This happens if the
   *   destination is a file.
   */
  @UiThread
  open fun onParentConflict(
    destinationFolder: DocumentFile,
    action: ParentFolderConflictAction,
    canMerge: Boolean,
  ) {
    action.confirmResolution(ConflictResolution.CREATE_NEW)
  }

  @UiThread
  open fun onContentConflict(
    destinationFolder: DocumentFile,
    conflictedFiles: MutableList<FileConflict>,
    action: FolderContentConflictAction,
  ) {
    action.confirmResolution(conflictedFiles)
  }

  class ParentFolderConflictAction(
    private val continuation: CancellableContinuation<ConflictResolution>
  ) {

    fun confirmResolution(resolution: ConflictResolution) {
      continuation.resumeWith(Result.success(resolution))
    }
  }

  class FolderContentConflictAction(
    private val continuation: CancellableContinuation<List<FileConflict>>
  ) {

    fun confirmResolution(resolutions: List<FileConflict>) {
      continuation.resumeWith(Result.success(resolutions))
    }
  }

  enum class ConflictResolution {
    /** Delete the folder in destination if existed, then start copy/move. */
    REPLACE,

    /** Skip duplicate files inside the folder. */
    MERGE,

    /**
     * * If a folder named `ABC` already exist, then create a new one named `ABC (1)`
     * * If the folder is empty, just use it.
     */
    CREATE_NEW,

    /** Cancel copy/move. */
    SKIP;

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    fun toCreateMode() =
      when (this) {
        REPLACE -> CreateMode.REPLACE
        MERGE -> CreateMode.REUSE
        else -> CreateMode.CREATE_NEW
      }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    fun toFileConflictResolution() =
      when (this) {
        REPLACE -> SingleFileConflictCallback.ConflictResolution.REPLACE
        CREATE_NEW -> SingleFileConflictCallback.ConflictResolution.CREATE_NEW
        else -> SingleFileConflictCallback.ConflictResolution.SKIP
      }
  }

  class FileConflict(
    val source: DocumentFile,
    val target: DocumentFile,
    var solution: SingleFileConflictCallback.ConflictResolution =
      SingleFileConflictCallback.ConflictResolution.CREATE_NEW,
  )
}
