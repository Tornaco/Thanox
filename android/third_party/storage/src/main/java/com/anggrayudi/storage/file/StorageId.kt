package com.anggrayudi.storage.file

import android.content.Context
import android.os.Environment
import androidx.annotation.RestrictTo

/**
 * Created on 03/06/21
 *
 * @author Anggrayudi H
 */
object StorageId {

  /** For files under [Environment.getExternalStorageDirectory] */
  const val PRIMARY = "primary"

  /**
   * For files under [Context.getFilesDir] or [Context.getDataDir]. It is not really a storage ID,
   * and can't be used in file tree URI.
   */
  const val DATA = "data"

  /** For `/storage/emulated/0/Documents` It is only exists on API 29- */
  @RestrictTo(RestrictTo.Scope.LIBRARY) const val HOME = "home"
}
