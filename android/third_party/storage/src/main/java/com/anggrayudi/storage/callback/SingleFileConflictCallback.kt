package com.anggrayudi.storage.callback

import androidx.annotation.RestrictTo
import androidx.annotation.UiThread
import androidx.documentfile.provider.DocumentFile
import com.anggrayudi.storage.file.CreateMode
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope

/**
 * Created on 17/08/20
 *
 * @author Anggrayudi H
 */
abstract class SingleFileConflictCallback<T>
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
   * @param destinationFile can be [DocumentFile] or [java.io.File]
   */
  @UiThread
  open fun onFileConflict(destinationFile: T, action: FileConflictAction) {
    action.confirmResolution(ConflictResolution.CREATE_NEW)
  }

  class FileConflictAction(private val continuation: CancellableContinuation<ConflictResolution>) {

    fun confirmResolution(resolution: ConflictResolution) {
      continuation.resumeWith(Result.success(resolution))
    }
  }

  enum class ConflictResolution {
    /** Delete the file in destination if existed, then start copy/move. */
    REPLACE,

    /** * If a file named `ABC.zip` already exist, then create a new one named `ABC (1).zip` */
    CREATE_NEW,

    /** Cancel copy/move. */
    SKIP;

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    fun toCreateMode(allowReuseFile: Boolean = false) =
      when (this) {
        REPLACE -> CreateMode.REPLACE
        CREATE_NEW -> CreateMode.CREATE_NEW
        SKIP -> if (allowReuseFile) CreateMode.REUSE else CreateMode.CREATE_NEW
      }
  }
}
