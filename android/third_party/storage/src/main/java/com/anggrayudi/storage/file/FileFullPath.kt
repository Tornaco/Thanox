package com.anggrayudi.storage.file

import android.content.Context
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import android.os.storage.StorageManager
import androidx.annotation.RequiresApi
import androidx.annotation.RestrictTo
import com.anggrayudi.storage.SimpleStorage
import com.anggrayudi.storage.extension.fromTreeUri
import com.anggrayudi.storage.extension.trimFileSeparator
import java.io.File

/**
 * If you're using the following functions:
 * * [SimpleStorage.createFile]
 * * [SimpleStorage.openFilePicker]
 * * [SimpleStorage.openFolderPicker]
 * * [SimpleStorage.requestFullStorageAccess]
 *
 * then you can construct [FileFullPath]:
 * ```
 * // for API 30+
 * val fullPath = FileFullPath(context, StorageType.EXTERNAL, "DCIM")
 *
 * // for API 29-, define storage ID explicitly
 * val fullPath = FileFullPath(context, StorageId.PRIMARY, "DCIM")
 *
 * storageHelper.requestStorageAccess(initialPath = fullPath)
 * storageHelper.openFolderPicker(initialPath = fullPath)
 * ```
 *
 * @author Anggrayudi H
 */
class FileFullPath : Parcelable {

  val storageId: String
  val basePath: String
  lateinit var absolutePath: String
    private set

  lateinit var simplePath: String
    private set

  /** @param fullPath can be simple path or absolute path */
  constructor(context: Context, fullPath: String) {
    if (fullPath.startsWith('/')) {
      when {
        fullPath.startsWith(SimpleStorage.externalStoragePath) -> {
          storageId = StorageId.PRIMARY
          val rootPath = SimpleStorage.externalStoragePath
          basePath = fullPath.substringAfter(rootPath, "").trimFileSeparator()
          simplePath = "$storageId:$basePath"
          absolutePath = "$rootPath/$basePath".trimEnd('/')
        }
        fullPath.startsWith(context.dataDirectory.path) -> {
          storageId = StorageId.DATA
          val rootPath = context.dataDirectory.path
          basePath = fullPath.substringAfter(rootPath, "").trimFileSeparator()
          simplePath = "$storageId:$basePath"
          absolutePath = "$rootPath/$basePath".trimEnd('/')
        }
        else ->
          if (fullPath.matches(DocumentFileCompat.SD_CARD_STORAGE_PATH_REGEX)) {
            storageId = fullPath.substringAfter("/storage/", "").substringBefore('/')
            basePath = fullPath.substringAfter("/storage/$storageId", "").trimFileSeparator()
            simplePath = "$storageId:$basePath"
            absolutePath = "/storage/$storageId/$basePath".trimEnd('/')
          } else {
            storageId = ""
            basePath = ""
            simplePath = ""
            absolutePath = ""
          }
      }
    } else {
      simplePath = fullPath
      storageId = fullPath.substringBefore(':', "").substringAfterLast('/')
      basePath = fullPath.substringAfter(':', "").trimFileSeparator()
      absolutePath = buildAbsolutePath(context, storageId, basePath)
    }
  }

  constructor(context: Context, storageId: String, basePath: String) {
    this.storageId = storageId
    this.basePath = basePath.trimFileSeparator()
    buildBaseAndAbsolutePaths(context)
  }

  @RequiresApi(30)
  constructor(context: Context, storageType: StorageType, basePath: String = "") {
    this.basePath = basePath.trimFileSeparator()
    val sm = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
    storageId =
      when (storageType) {
        StorageType.SD_CARD ->
          sm.storageVolumes.firstOrNull { it.isRemovable }?.mediaStoreVolumeName.orEmpty()
        StorageType.EXTERNAL -> StorageId.PRIMARY
        StorageType.DATA -> StorageId.DATA
        else -> ""
      }
    buildBaseAndAbsolutePaths(context)
  }

  constructor(context: Context, file: File) : this(context, file.path.orEmpty())

  private constructor(
    storageId: String,
    basePath: String,
    absolutePath: String,
    simplePath: String,
  ) {
    this.storageId = storageId
    this.basePath = basePath
    this.absolutePath = absolutePath
    this.simplePath = simplePath
  }

  private fun buildAbsolutePath(context: Context, storageId: String, basePath: String) =
    if (storageId.isEmpty()) ""
    else
      when (storageId) {
        StorageId.PRIMARY -> "${SimpleStorage.externalStoragePath}/$basePath".trimEnd('/')
        StorageId.DATA -> "${context.dataDirectory.path}/$basePath".trimEnd('/')
        else -> "/storage/$storageId/$basePath".trimEnd('/')
      }

  private fun buildBaseAndAbsolutePaths(context: Context) {
    absolutePath = buildAbsolutePath(context, storageId, basePath)
    simplePath = if (storageId.isEmpty()) "" else "$storageId:$basePath"
  }

  val uri: Uri?
    get() =
      if (storageId.isEmpty()) null else DocumentFileCompat.createDocumentUri(storageId, basePath)

  fun toDocumentUri(context: Context): Uri? {
    return context.fromTreeUri(uri ?: return null)?.uri
  }

  val storageType: StorageType
    get() = StorageType.fromStorageId(storageId)

  @RestrictTo(RestrictTo.Scope.LIBRARY)
  fun checkIfStorageIdIsAccessibleInSafSelector() {
    if (storageId.isEmpty()) {
      throw IllegalArgumentException("Empty storage ID")
    }
    if (storageId == StorageId.DATA) {
      throw IllegalArgumentException(
        "Cannot use StorageType.DATA because it is never available in Storage Access Framework's folder selector."
      )
    }
  }

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(storageId)
    parcel.writeString(basePath)
    parcel.writeString(absolutePath)
    parcel.writeString(simplePath)
  }

  override fun describeContents(): Int = 0

  companion object CREATOR : Parcelable.Creator<FileFullPath> {
    override fun createFromParcel(parcel: Parcel): FileFullPath {
      val storageId = parcel.readString().orEmpty()
      val basePath = parcel.readString().orEmpty()
      val absolutePath = parcel.readString().orEmpty()
      val simplePath = parcel.readString().orEmpty()
      return FileFullPath(storageId, basePath, absolutePath, simplePath)
    }

    override fun newArray(size: Int): Array<FileFullPath?> = arrayOfNulls(size)
  }
}
