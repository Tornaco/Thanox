@file:JvmName("FileUtils")

package com.anggrayudi.storage.file

import android.content.Context
import android.os.Build
import android.os.Environment
import androidx.annotation.RestrictTo
import androidx.annotation.WorkerThread
import androidx.documentfile.provider.DocumentFile
import com.anggrayudi.storage.SimpleStorage
import com.anggrayudi.storage.callback.SingleFileConflictCallback
import com.anggrayudi.storage.extension.awaitUiResultWithPending
import com.anggrayudi.storage.extension.trimFileSeparator
import com.anggrayudi.storage.file.DocumentFileCompat.removeForbiddenCharsFromFilename
import com.anggrayudi.storage.file.StorageId.DATA
import com.anggrayudi.storage.file.StorageId.HOME
import com.anggrayudi.storage.file.StorageId.PRIMARY
import java.io.File
import java.io.IOException

/**
 * Created on 07/09/20
 *
 * @author Anggrayudi H
 */

/**
 * ID of this storage. For external storage, it will return [PRIMARY], otherwise it is a SD Card and
 * will return integers like `6881-2249`.
 */
fun File.getStorageId(context: Context) =
  when {
    path.startsWith(SimpleStorage.externalStoragePath) -> PRIMARY
    path.startsWith(context.dataDirectory.path) -> DATA
    else ->
      if (path.matches(DocumentFileCompat.SD_CARD_STORAGE_PATH_REGEX)) {
        path.substringAfter("/storage/", "").substringBefore('/')
      } else ""
  }

val File.inPrimaryStorage: Boolean
  get() = path.startsWith(SimpleStorage.externalStoragePath)

fun File.inDataStorage(context: Context) = path.startsWith(context.dataDirectory.path)

fun File.inSdCardStorage(context: Context) =
  getStorageId(context).let { it != PRIMARY && it != DATA && path.startsWith("/storage/$it") }

fun File.inSameMountPointWith(context: Context, file: File): Boolean {
  val storageId1 = getStorageId(context)
  val storageId2 = file.getStorageId(context)
  return storageId1 == storageId2 ||
    (storageId1 == PRIMARY || storageId1 == DATA) && (storageId2 == PRIMARY || storageId2 == DATA)
}

fun File.getStorageType(context: Context) =
  when {
    inPrimaryStorage -> StorageType.EXTERNAL
    inDataStorage(context) -> StorageType.DATA
    inSdCardStorage(context) -> StorageType.SD_CARD
    else -> StorageType.UNKNOWN
  }

/** @param path single file name or file path */
fun File.child(path: String) = File(this, path)

/**
 * @see [Context.getDataDir]
 * @see [Context.getFilesDir]
 */
val Context.dataDirectory: File
  get() = if (Build.VERSION.SDK_INT > 23) dataDir else filesDir.parentFile!!

fun File.getBasePath(context: Context): String {
  val externalStoragePath = SimpleStorage.externalStoragePath
  if (path.startsWith(externalStoragePath)) {
    return path.substringAfter(externalStoragePath, "").trimFileSeparator()
  }
  val dataDir = context.dataDirectory.path
  if (path.startsWith(dataDir)) {
    return path.substringAfter(dataDir, "").trimFileSeparator()
  }
  val storageId = getStorageId(context)
  return path.substringAfter("/storage/$storageId", "").trimFileSeparator()
}

fun File.getRootPath(context: Context): String {
  val storageId = getStorageId(context)
  return when {
    storageId == PRIMARY || storageId == HOME -> SimpleStorage.externalStoragePath
    storageId == DATA -> context.dataDirectory.path
    storageId.isNotEmpty() -> "/storage/$storageId"
    else -> ""
  }
}

fun File.getSimplePath(context: Context) =
  "${getStorageId(context)}:${getBasePath(context)}".removePrefix(":")

/**
 * Returns:
 * * `null` if it is a directory or the file does not exist
 * * [MimeType.UNKNOWN] if the file exists but the mime type is not found
 */
val File.mimeType: String?
  get() = if (isFile) MimeType.getMimeTypeFromExtension(extension) else null

@JvmOverloads
fun File.getRootRawFile(context: Context, requiresWriteAccess: Boolean = false) =
  getRootPath(context).let {
    if (it.isEmpty()) null else File(it).run { takeIfWritable(context, requiresWriteAccess) }
  }

fun File.isReadOnly(context: Context) = canRead() && !isWritable(context)

fun File.canModify(context: Context) = canRead() && isWritable(context)

val File.isEmpty: Boolean
  get() = isFile && length() == 0L || isDirectory && list().isNullOrEmpty()

@RestrictTo(RestrictTo.Scope.LIBRARY)
fun File.shouldWritable(context: Context, requiresWriteAccess: Boolean) =
  requiresWriteAccess && isWritable(context) || !requiresWriteAccess

@RestrictTo(RestrictTo.Scope.LIBRARY)
fun File.takeIfWritable(context: Context, requiresWriteAccess: Boolean) = takeIf {
  it.canRead() && it.shouldWritable(context, requiresWriteAccess)
}

@RestrictTo(RestrictTo.Scope.LIBRARY)
fun File.checkRequirements(
  context: Context,
  requiresWriteAccess: Boolean,
  considerRawFile: Boolean,
) =
  canRead() &&
    shouldWritable(context, requiresWriteAccess) &&
    (considerRawFile || isExternalStorageManager(context))

fun File.createNewFileIfPossible(): Boolean =
  try {
    isFile || createNewFile()
  } catch (_: IOException) {
    false
  }

/**
 * Use it, because [File.canWrite] is unreliable on Android 10. Read
 * [this issue](https://github.com/anggrayudi/SimpleStorage/issues/24#issuecomment-830000378)
 */
fun File.isWritable(context: Context) = canWrite() && (isFile || isExternalStorageManager(context))

/**
 * @return `true` if you have full disk access
 * @see Environment.isExternalStorageManager
 */
fun File.isExternalStorageManager(context: Context) =
  Build.VERSION.SDK_INT > Build.VERSION_CODES.Q && Environment.isExternalStorageManager(this) ||
    Build.VERSION.SDK_INT < Build.VERSION_CODES.Q &&
      path.startsWith(SimpleStorage.externalStoragePath) &&
      SimpleStorage.hasStoragePermission(context) ||
    context.writableDirs.any { path.startsWith(it.path) }

/**
 * These directories do not require storage permissions. They are always writable with full disk
 * access.
 */
val Context.writableDirs: Set<File>
  get() {
    val dirs = mutableSetOf(dataDirectory)
    dirs.addAll(obbDirs.filterNotNull())
    dirs.addAll(getExternalFilesDirs(null).mapNotNull { it?.parentFile })
    return dirs
  }

/**
 * Create file and if exists, increment file name.
 *
 * @param onConflict when this callback is set and `mode` is not [CreateMode.CREATE_NEW], then the
 *   user will be asked for resolution if conflict happens
 */
@WorkerThread
@JvmOverloads
fun File.makeFile(
  context: Context,
  name: String,
  mimeType: String? = MimeType.UNKNOWN,
  mode: CreateMode = CreateMode.CREATE_NEW,
  onConflict: SingleFileConflictCallback<File>? = null,
): File? {
  if (!isDirectory || !isWritable(context)) {
    return null
  }

  val cleanName = name.removeForbiddenCharsFromFilename().trimFileSeparator()
  val subFolder = cleanName.substringBeforeLast('/', "")
  val parent =
    if (subFolder.isEmpty()) this
    else {
      makeFolder(context, subFolder, mode) ?: return null
    }

  val filename = cleanName.substringAfterLast('/')
  val extensionByName = MimeType.getExtensionFromFileName(cleanName)
  val extension =
    if (
      extensionByName.isNotEmpty() &&
        (mimeType == null || mimeType == MimeType.UNKNOWN || mimeType == MimeType.BINARY_FILE)
    ) {
      extensionByName
    } else {
      MimeType.getExtensionFromMimeTypeOrFileName(mimeType, cleanName)
    }
  val baseFileName = filename.removeSuffix(".$extension")
  val fullFileName = "$baseFileName.$extension".trimEnd('.')

  var createMode = mode
  val targetFile = File(parent, fullFileName)
  if (onConflict != null && targetFile.exists()) {
    createMode =
      awaitUiResultWithPending(onConflict.uiScope) {
          onConflict.onFileConflict(targetFile, SingleFileConflictCallback.FileConflictAction(it))
        }
        .toCreateMode(true)
  }

  if (createMode != CreateMode.CREATE_NEW && targetFile.exists()) {
    return when {
      createMode == CreateMode.REPLACE -> targetFile.takeIf { it.recreateFile() }
      createMode != CreateMode.SKIP_IF_EXISTS && targetFile.isFile -> targetFile
      else -> null
    }
  }

  return try {
    File(parent, autoIncrementFileName(fullFileName)).let { if (it.createNewFile()) it else null }
  } catch (_: IOException) {
    null
  }
}

/** @param name can input `MyFolder` or `MyFolder/SubFolder` */
@WorkerThread
@JvmOverloads
fun File.makeFolder(
  context: Context,
  name: String,
  mode: CreateMode = CreateMode.CREATE_NEW,
): File? {
  if (!isDirectory || !isWritable(context)) {
    return null
  }

  val directorySequence =
    DocumentFileCompat.getDirectorySequence(name.removeForbiddenCharsFromFilename()).toMutableList()
  val folderNameLevel1 = directorySequence.removeFirstOrNull() ?: return null
  val incrementedFolderNameLevel1 =
    if (mode == CreateMode.CREATE_NEW) autoIncrementFileName(folderNameLevel1) else folderNameLevel1
  val folderLevel1 = child(incrementedFolderNameLevel1)

  if (mode == CreateMode.REPLACE) {
    folderLevel1.forceDelete(true)
  } else if (mode == CreateMode.SKIP_IF_EXISTS && folderLevel1.exists()) {
    return null
  }
  folderLevel1.mkdir()

  val folder =
    folderLevel1.let {
      if (directorySequence.isEmpty()) it
      else it.child(directorySequence.joinToString("/")).apply { mkdirs() }
    }
  return if (folder.isDirectory) folder else null
}

fun File.toDocumentFile(context: Context) =
  if (canRead()) DocumentFileCompat.fromFile(context, this) else null

fun File.deleteEmptyFolders(context: Context): Boolean {
  return if (isDirectory && isWritable(context)) {
    walkFileTreeAndDeleteEmptyFolders().reversed().forEach { it.delete() }
    true
  } else false
}

private fun File.walkFileTreeAndDeleteEmptyFolders(): List<File> {
  val fileTree = mutableListOf<File>()
  listFiles()?.forEach {
    if (it.isDirectory && !it.delete()) { // Deletion is only success if the folder is empty
      fileTree.add(it)
      fileTree.addAll(it.walkFileTreeAndDeleteEmptyFolders())
    }
  }
  return fileTree
}

/**
 * @param childrenOnly `true` to delete the folder contents only
 * @see DocumentFile.deleteRecursively
 */
@JvmOverloads
fun File.forceDelete(childrenOnly: Boolean = false): Boolean {
  return if (isDirectory) {
    val success = deleteRecursively()
    if (childrenOnly) {
      mkdir()
      isDirectory && list().isNullOrEmpty()
    } else {
      success
    }
  } else {
    delete() || !exists()
  }
}

fun File.recreateFile(): Boolean {
  forceDelete()
  return tryCreateNewFile()
}

fun File.tryCreateNewFile() =
  try {
    createNewFile()
  } catch (_: IOException) {
    false
  }

/**
 * Avoid duplicate file name. It doesn't work if you are outside [Context.getExternalFilesDir] and
 * don't have full disk access for Android 10+.
 */
fun File.autoIncrementFileName(filename: String): String {
  return if (child(filename).exists()) {
    val baseName = MimeType.getBaseFileName(filename)
    val ext = MimeType.getExtensionFromFileName(filename)
    val prefix = "$baseName ("
    var lastFileCount =
      list()
        .orEmpty()
        .filter {
          it.startsWith(prefix) &&
            (DocumentFileCompat.FILE_NAME_DUPLICATION_REGEX_WITH_EXTENSION.matches(it) ||
              DocumentFileCompat.FILE_NAME_DUPLICATION_REGEX_WITHOUT_EXTENSION.matches(it))
        }
        .maxOfOrNull { it.substringAfterLast('(', "").substringBefore(')', "").toIntOrNull() ?: 0 }
        ?: 0
    "$baseName (${++lastFileCount}).$ext".trimEnd('.')
  } else {
    filename
  }
}

@JvmOverloads
fun File.moveTo(
  context: Context,
  targetFolder: String,
  newFileNameInTarget: String? = null,
  conflictResolution: SingleFileConflictCallback.ConflictResolution =
    SingleFileConflictCallback.ConflictResolution.CREATE_NEW,
): File? {
  return moveTo(context, File(targetFolder), newFileNameInTarget, conflictResolution)
}

/**
 * @param conflictResolution using [SingleFileConflictCallback.ConflictResolution.SKIP] will return
 *   `null`
 */
@JvmOverloads
fun File.moveTo(
  context: Context,
  targetFolder: File,
  newFileNameInTarget: String? = null,
  conflictResolution: SingleFileConflictCallback.ConflictResolution =
    SingleFileConflictCallback.ConflictResolution.CREATE_NEW,
): File? {
  if (!exists() || !isWritable(context)) {
    return null
  }
  targetFolder.mkdirs()
  if (!targetFolder.isDirectory || !targetFolder.isWritable(context)) {
    return null
  }
  val filename = newFileNameInTarget ?: name
  var dest = targetFolder.child(filename)
  if (parent == targetFolder.path) {
    return if (renameTo(dest)) dest else null
  }
  if (!inSameMountPointWith(context, targetFolder)) {
    return null
  }
  if (dest.exists()) {
    when (conflictResolution) {
      SingleFileConflictCallback.ConflictResolution.SKIP -> return null
      SingleFileConflictCallback.ConflictResolution.REPLACE -> if (!dest.forceDelete()) return null
      SingleFileConflictCallback.ConflictResolution.CREATE_NEW -> {
        dest = targetFolder.child(targetFolder.autoIncrementFileName(filename))
      }
    }
  }
  if (renameTo(dest)) { // true for files and empty folders
    return dest
  }
  if (isDirectory) {
    dest.mkdirs()
    walkFileTreeForMove(path, dest.path)
    deleteRecursively()
    return dest.takeIf { !it.isEmpty }
  }
  return null
}

private fun File.walkFileTreeForMove(srcPath: String, destFolderPath: String) {
  listFiles()?.forEach {
    val targetFile = File(destFolderPath, it.path.substringAfter(srcPath).trim('/'))
    if (it.isFile) {
      it.renameTo(targetFile)
    } else {
      targetFile.mkdirs()
      it.walkFileTreeForMove(srcPath, destFolderPath)
    }
  }
}
