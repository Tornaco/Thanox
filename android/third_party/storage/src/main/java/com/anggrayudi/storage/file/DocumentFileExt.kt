@file:JvmName("DocumentFileUtils")

package com.anggrayudi.storage.file

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.text.format.Formatter
import android.util.Log
import androidx.annotation.RestrictTo
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.core.content.FileProvider
import androidx.core.content.MimeTypeFilter
import androidx.documentfile.provider.DocumentFile
import com.anggrayudi.storage.SimpleStorage
import com.anggrayudi.storage.callback.MultipleFilesConflictCallback
import com.anggrayudi.storage.callback.SingleFileConflictCallback
import com.anggrayudi.storage.callback.SingleFolderConflictCallback
import com.anggrayudi.storage.extension.awaitUiResultWithPending
import com.anggrayudi.storage.extension.childOf
import com.anggrayudi.storage.extension.closeEntryQuietly
import com.anggrayudi.storage.extension.closeStreamQuietly
import com.anggrayudi.storage.extension.fromTreeUri
import com.anggrayudi.storage.extension.getStorageId
import com.anggrayudi.storage.extension.hasParent
import com.anggrayudi.storage.extension.isDocumentsDocument
import com.anggrayudi.storage.extension.isDownloadsDocument
import com.anggrayudi.storage.extension.isExternalStorageDocument
import com.anggrayudi.storage.extension.isMediaDocument
import com.anggrayudi.storage.extension.isRawFile
import com.anggrayudi.storage.extension.isTreeDocumentFile
import com.anggrayudi.storage.extension.openInputStream
import com.anggrayudi.storage.extension.openOutputStream
import com.anggrayudi.storage.extension.parent
import com.anggrayudi.storage.extension.sendAll
import com.anggrayudi.storage.extension.sendAndClose
import com.anggrayudi.storage.extension.startCoroutineTimer
import com.anggrayudi.storage.extension.toDocumentFile
import com.anggrayudi.storage.extension.trimFileSeparator
import com.anggrayudi.storage.file.DocumentFileCompat.removeForbiddenCharsFromFilename
import com.anggrayudi.storage.file.StorageId.DATA
import com.anggrayudi.storage.file.StorageId.PRIMARY
import com.anggrayudi.storage.media.FileDescription
import com.anggrayudi.storage.media.MediaFile
import com.anggrayudi.storage.media.MediaStoreCompat
import com.anggrayudi.storage.result.FileProperties
import com.anggrayudi.storage.result.FilePropertiesResult
import com.anggrayudi.storage.result.FolderErrorCode
import com.anggrayudi.storage.result.MultipleFilesErrorCode
import com.anggrayudi.storage.result.MultipleFilesResult
import com.anggrayudi.storage.result.SingleFileErrorCode
import com.anggrayudi.storage.result.SingleFileResult
import com.anggrayudi.storage.result.SingleFolderResult
import com.anggrayudi.storage.result.ZipCompressionErrorCode
import com.anggrayudi.storage.result.ZipCompressionResult
import com.anggrayudi.storage.result.ZipDecompressionErrorCode
import com.anggrayudi.storage.result.ZipDecompressionResult
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.io.InterruptedIOException
import java.io.OutputStream
import java.util.Date
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

typealias CheckFileSize = (freeSpace: Long, fileSize: Long) -> Boolean

internal val defaultFileSizeChecker: CheckFileSize = { freeSpace, fileSize ->
  fileSize + 100 * FileSize.MB < freeSpace /* 100MB tolerance */
}

/**
 * Created on 16/08/20
 *
 * @author Anggrayudi H
 */

/**
 * ID of this storage. For external storage, it will return [PRIMARY], otherwise it is a SD Card and
 * will return integers like `6881-2249`. However, it will return empty `String` if this
 * [DocumentFile] is picked from [Intent.ACTION_OPEN_DOCUMENT] or [Intent.ACTION_CREATE_DOCUMENT]
 */
fun DocumentFile.getStorageId(context: Context) = uri.getStorageId(context)

val DocumentFile.isTreeDocumentFile: Boolean
  get() = uri.isTreeDocumentFile

val DocumentFile.isExternalStorageDocument: Boolean
  get() = uri.isExternalStorageDocument

val DocumentFile.isDownloadsDocument: Boolean
  get() = uri.isDownloadsDocument

val DocumentFile.isDocumentsDocument: Boolean
  get() = uri.isDocumentsDocument

val DocumentFile.isMediaDocument: Boolean
  get() = uri.isMediaDocument

fun DocumentFile.isReadOnly(context: Context) = canRead() && !isWritable(context)

val DocumentFile.id: String
  get() = DocumentsContract.getDocumentId(uri)

val DocumentFile.rootId: String
  get() = DocumentsContract.getRootId(uri)

fun DocumentFile.isExternalStorageManager(context: Context) =
  isRawFile && File(uri.path!!).isExternalStorageManager(context)

/**
 * Some media files do not return file extension from [DocumentFile.getName]. This function helps
 * you to fix this kind of issue.
 */
val DocumentFile.fullName: String
  get() =
    if (isRawFile || isExternalStorageDocument || isDirectory) {
      name.orEmpty()
    } else {
      MimeType.getFullFileName(name.orEmpty(), type)
    }

fun DocumentFile.inSameMountPointWith(context: Context, file: DocumentFile): Boolean {
  val storageId1 = getStorageId(context)
  val storageId2 = file.getStorageId(context)
  return storageId1 == storageId2 || inInternalStorage(storageId1) && inInternalStorage(storageId2)
}

@SuppressLint("NewApi")
fun DocumentFile.isEmpty(context: Context): Boolean {
  return isFile && length() == 0L ||
    isDirectory &&
      kotlin.run {
        if (isRawFile) {
          toRawFile(context)?.list().isNullOrEmpty()
        } else
          try {
            val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(uri, id)
            context.contentResolver
              .query(
                childrenUri,
                arrayOf(DocumentsContract.Document.COLUMN_DOCUMENT_ID),
                null,
                null,
                null,
              )
              ?.use { it.count == 0 } ?: true
          } catch (_: Exception) {
            true
          }
      }
}

/**
 * Similar to Get Info on MacOS or File Properties in Windows. Example:
 * ```
 * val job = ioScope.launch {
 *     getProperties(context)
 *         .onCompletion {
 *             if (it is CancellationException) {
 *                 // update UI
 *             }
 *         }
 *         .collect { result ->
 *             when (result) {
 *                 is FilePropertiesResult.OnUpdate -> // do something
 *                 is FilePropertiesResult.OnComplete -> // do something
 *                 is FilePropertiesResult.OnError -> // do something
 *             }
 *         }
 * }
 * // call this if you want to stop in the middle of process
 * job.cancel()
 * ```
 */
@WorkerThread
fun DocumentFile.getProperties(
  context: Context,
  updateInterval: Long = 500,
): Flow<FilePropertiesResult> = callbackFlow {
  when {
    !canRead() -> send(FilePropertiesResult.Error)

    isDirectory -> {
      val properties =
        FileProperties(
          name = name.orEmpty(),
          location = getAbsolutePath(context),
          isFolder = true,
          isVirtual = isVirtual,
          lastModified = lastModified().let { if (it > 0) Date(it) else null },
        )
      if (isEmpty(context)) {
        send(FilePropertiesResult.Completed(properties))
      } else {
        val timer =
          if (updateInterval < 1) null
          else
            startCoroutineTimer(repeatMillis = updateInterval) {
              trySend(FilePropertiesResult.Updating(properties))
            }
        walkFileTreeForInfo(properties, this)
        timer?.cancel()
        send(FilePropertiesResult.Completed(properties))
      }
    }

    isFile -> {
      val properties =
        FileProperties(
          name = fullName,
          location = getAbsolutePath(context),
          size = length(),
          isVirtual = isVirtual,
          lastModified = lastModified().let { if (it > 0) Date(it) else null },
        )
      send(FilePropertiesResult.Completed(properties))
    }
  }
  close()
}

@OptIn(DelicateCoroutinesApi::class)
private fun <E> DocumentFile.walkFileTreeForInfo(
  properties: FileProperties,
  scope: ProducerScope<E>,
) {
  val list = listFiles()
  if (list.isEmpty()) {
    properties.emptyFolders++
    return
  }
  list.forEach {
    if (scope.isClosedForSend) {
      return
    }
    if (it.isFile) {
      properties.files++
      val size = it.length()
      properties.size += size
      if (size == 0L) properties.emptyFiles++
    } else {
      properties.folders++
      it.walkFileTreeForInfo(properties, scope)
    }
  }
}

/**
 * Returns `null` if this [DocumentFile] is picked from [Intent.ACTION_OPEN_DOCUMENT] or
 * [Intent.ACTION_CREATE_DOCUMENT]
 */
fun DocumentFile.getStorageType(context: Context): StorageType {
  return if (isTreeDocumentFile) {
    if (inPrimaryStorage(context)) StorageType.EXTERNAL else StorageType.SD_CARD
  } else
    when {
      inSdCardStorage(context) -> StorageType.SD_CARD
      inDataStorage(context) -> StorageType.DATA
      else -> StorageType.UNKNOWN
    }
}

private fun inInternalStorage(storageId: String) = storageId == PRIMARY || storageId == DATA

fun DocumentFile.inInternalStorage(context: Context) = inInternalStorage(getStorageId(context))

/**
 * `true` if this file located in primary storage, i.e. external storage. All files created by
 * [DocumentFile.fromFile] are always treated from external storage.
 */
fun DocumentFile.inPrimaryStorage(context: Context) =
  isTreeDocumentFile && getStorageId(context) == PRIMARY ||
    isRawFile && uri.path.orEmpty().startsWith(SimpleStorage.externalStoragePath)

/** `true` if this file located in SD Card */
fun DocumentFile.inSdCardStorage(context: Context) =
  getStorageId(context).matches(DocumentFileCompat.SD_CARD_STORAGE_ID_REGEX)

fun DocumentFile.inDataStorage(context: Context) =
  isRawFile && File(uri.path!!).inDataStorage(context)

/** `true` if this file was created with [File] */
val DocumentFile.isRawFile: Boolean
  get() = uri.isRawFile

/** Filename without extension */
val DocumentFile.baseName: String
  get() = if (isFile) MimeType.getBaseFileName(fullName) else name.orEmpty()

/** File extension */
val DocumentFile.extension: String
  get() = if (isFile) MimeType.getExtensionFromFileName(fullName) else ""

/**
 * Advanced version of [DocumentFile.getType]. Returns:
 * * `null` if it is a directory or the file does not exist
 * * [MimeType.UNKNOWN] if the file exists but the mime type is not found
 */
val DocumentFile.mimeType: String?
  get() = if (isFile) type ?: MimeType.getMimeTypeFromExtension(extension) else null

val DocumentFile.mimeTypeByFileName: String?
  get() =
    if (isDirectory) null
    else {
      val extension = MimeType.getExtensionFromFileName(name)
      val mimeType = MimeType.getMimeTypeFromExtension(extension)
      if (mimeType == MimeType.UNKNOWN) type else mimeType
    }

/**
 * Please notice that accessing files with [File] only works on app private directory since
 * Android 10. You had better to stay using [DocumentFile].
 *
 * @return `null` if you try to read files from SD Card or you want to convert a file picked from
 *   [Intent.ACTION_OPEN_DOCUMENT] or [Intent.ACTION_CREATE_DOCUMENT].
 * @see toDocumentFile
 */
fun DocumentFile.toRawFile(context: Context): File? {
  return when {
    isRawFile -> File(uri.path ?: return null)
    inPrimaryStorage(context) ->
      File("${SimpleStorage.externalStoragePath}/${getBasePath(context)}")
    else ->
      getStorageId(context).let { storageId ->
        if (storageId.isNotEmpty()) {
          File("/storage/$storageId/${getBasePath(context)}")
        } else {
          null
        }
      }
  }
}

fun DocumentFile.toRawDocumentFile(context: Context): DocumentFile? {
  return if (isRawFile) this else DocumentFile.fromFile(toRawFile(context) ?: return null)
}

fun DocumentFile.toTreeDocumentFile(context: Context): DocumentFile? {
  return if (isRawFile) {
    DocumentFileCompat.fromFile(context, toRawFile(context) ?: return null, considerRawFile = false)
  } else if (isTreeDocumentFile) {
    this
  } else {
    val path = getAbsolutePath(context)
    if (path.isEmpty()) null
    else {
      DocumentFileCompat.fromFullPath(context, path, requiresWriteAccess = true)
    }
  }
}

fun DocumentFile.toMediaFile(context: Context) =
  if (isTreeDocumentFile) null else MediaFile(context, uri)

/**
 * It will try converting [androidx.documentfile.provider.SingleDocumentFile] to
 * [androidx.documentfile.provider.TreeDocumentFile] if possible, because `SingleDocumentFile` can't
 * do rename operation. It is also a safer option compared to
 * [androidx.documentfile.provider.DocumentFile.renameTo], because `renameTo()` has some issues
 * prior to scoped storage on SD card path.
 *
 * @see toTreeDocumentFile
 */
@JvmOverloads
fun DocumentFile.changeName(
  context: Context,
  newBaseName: String,
  newExtension: String? = null,
): DocumentFile? {
  val newFileExtension = newExtension ?: extension
  val newName = "$newBaseName.$newFileExtension".trimEnd('.')
  if (newName.isEmpty()) {
    return null
  }
  if (isRawFile && renameTo(newName)) {
    return this
  }
  val file = toTreeDocumentFile(context) ?: return null
  val parentFolder = file.findParent(context, true) ?: return null
  return if (Build.VERSION.SDK_INT < 29 && file.inSdCardStorage(context)) {
    //  Renaming files in SD card always throws FileNotFoundException, so we have a workaround here.
    if (parentFolder.child(context, newName, true) == null) {
      file.renameTo(newName)
      parentFolder.child(context, newName, true)
    } else {
      // Conflict with existing file
      null
    }
  } else {
    // Returning 'this' will lead to null getName(), false canWrite(), and other problems.
    // So we will create a new instance of the renamed file to avoid those issues.
    if (file.renameTo(newName)) parentFolder.child(context, newName, true) else null
  }
}

/**
 * It's faster than [DocumentFile.findFile]
 *
 * @param path single file name or file path. Empty string returns to itself.
 */
@JvmOverloads
fun DocumentFile.child(
  context: Context,
  path: String,
  requiresWriteAccess: Boolean = false,
): DocumentFile? {
  return when {
    path.isEmpty() -> this
    isDirectory -> {
      val file =
        if (isRawFile) {
          quickFindRawFile(path)
        } else {
          var currentDirectory = this
          val resolver = context.contentResolver
          DocumentFileCompat.getDirectorySequence(path).forEach {
            val directory = currentDirectory.quickFindTreeFile(context, resolver, it) ?: return null
            if (directory.canRead()) {
              currentDirectory = directory
            } else {
              return null
            }
          }
          currentDirectory
        }
      file?.takeIfWritable(context, requiresWriteAccess)
    }

    else -> null
  }
}

/**
 * Safer method than [DocumentFile.listFiles] which throws [UnsupportedOperationException] if this
 * [DocumentFile] is a file.
 */
val DocumentFile.children: List<DocumentFile>
  get() = if (isDirectory) listFiles().toList() else emptyList()

@RestrictTo(RestrictTo.Scope.LIBRARY)
fun DocumentFile.quickFindRawFile(name: String): DocumentFile? {
  return DocumentFile.fromFile(File(uri.path!!, name)).takeIf { it.canRead() }
}

/**
 * It's faster than [DocumentFile.findFile].
 *
 * Must set [ContentResolver] as additional parameter to improve performance.
 */
@SuppressLint("NewApi")
@RestrictTo(RestrictTo.Scope.LIBRARY)
fun DocumentFile.quickFindTreeFile(
  context: Context,
  resolver: ContentResolver,
  name: String,
): DocumentFile? {
  try {
    // Optimized algorithm. Do not change unless you really know algorithm complexity.
    val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(uri, id)
    resolver
      .query(childrenUri, arrayOf(DocumentsContract.Document.COLUMN_DOCUMENT_ID), null, null, null)
      ?.use {
        val columnName = arrayOf(DocumentsContract.Document.COLUMN_DISPLAY_NAME)
        while (it.moveToNext()) {
          try {
            val documentUri = DocumentsContract.buildDocumentUriUsingTree(uri, it.getString(0))
            resolver.query(documentUri, columnName, null, null, null)?.use { childCursor ->
              if (childCursor.moveToFirst() && name == childCursor.getString(0))
                return context.fromTreeUri(documentUri)
            }
          } catch (_: Exception) {
            // ignore
          }
        }
      }
  } catch (_: Exception) {
    // ignore
  }
  return null
}

@RestrictTo(RestrictTo.Scope.LIBRARY)
fun DocumentFile.shouldWritable(context: Context, requiresWriteAccess: Boolean) =
  requiresWriteAccess && isWritable(context) || !requiresWriteAccess

@RestrictTo(RestrictTo.Scope.LIBRARY)
fun DocumentFile.takeIfWritable(context: Context, requiresWriteAccess: Boolean) = takeIf {
  it.shouldWritable(context, requiresWriteAccess)
}

@RestrictTo(RestrictTo.Scope.LIBRARY)
fun DocumentFile.checkRequirements(
  context: Context,
  requiresWriteAccess: Boolean,
  considerRawFile: Boolean,
) =
  canRead() &&
    (considerRawFile || isExternalStorageManager(context)) &&
    shouldWritable(context, requiresWriteAccess)

/**
 * @return File path without storage ID. Returns empty `String` if:
 * * It is the root path
 * * It is not a raw file and the authority is neither
 *   [DocumentFileCompat.EXTERNAL_STORAGE_AUTHORITY] nor
 *   [DocumentFileCompat.DOWNLOADS_FOLDER_AUTHORITY]
 * * The authority is [DocumentFileCompat.DOWNLOADS_FOLDER_AUTHORITY], but [isTreeDocumentFile]
 *   returns `false`
 */
fun DocumentFile.getBasePath(context: Context): String {
  val path = uri.path.orEmpty()
  val storageID = getStorageId(context)
  return when {
    isRawFile -> File(path).getBasePath(context)

    isDocumentsDocument -> {
      "${Environment.DIRECTORY_DOCUMENTS}/${path.substringAfterLast("/home:", "")}".trimEnd('/')
    }

    isExternalStorageDocument && path.contains("/document/$storageID:") -> {
      path.substringAfterLast("/document/$storageID:", "").trimFileSeparator()
    }

    isDownloadsDocument -> {
      // content://com.android.providers.downloads.documents/tree/raw:/storage/emulated/0/Download/Denai/document/raw:/storage/emulated/0/Download/Denai
      // content://com.android.providers.downloads.documents/tree/downloads/document/raw:/storage/emulated/0/Download/Denai
      when {
        // API 26 - 27 => content://com.android.providers.downloads.documents/document/22
        Build.VERSION.SDK_INT < Build.VERSION_CODES.P && path.matches(Regex("/document/\\d+")) -> {
          val fileName = MediaFile(context, uri).name ?: return ""
          "${Environment.DIRECTORY_DOWNLOADS}/$fileName"
        }

        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
          path.matches(Regex("(.*?)/ms[f,d]:\\d+(.*?)")) -> {
          if (isTreeDocumentFile) {
            val parentTree = mutableListOf(name.orEmpty())
            var parent = this
            while (parent.parentFile?.also { parent = it } != null) {
              parentTree.add(parent.name.orEmpty())
            }
            parentTree.reversed().joinToString("/")
          } else {
            // we can't use msf/msd ID as MediaFile ID to fetch relative path, so just return empty
            // String
            ""
          }
        }

        else -> path.substringAfterLast(SimpleStorage.externalStoragePath, "").trimFileSeparator()
      }
    }

    else -> ""
  }
}

/**
 * **Case 1**: Should return `Pop/Albums` from the following folders:
 * * `/storage/AAAA-BBBB/Music`
 * * `/storage/AAAA-BBBB/Music/Pop/Albums`
 *
 * **Case 2**: Should return `Albums/A Day in the Hell` from the following folders:
 * * `/storage/AAAA-BBBB/Music/Pop/Albums/A Day in the Hell`
 * * `/storage/AAAA-BBBB/Other/Pop`
 *
 * **Case 3**: Should return empty string from the following folders:
 * * `/storage/AAAA-BBBB/Music`
 * * `/storage/AAAA-BBBB/Music`
 *
 * **Case 4**: Should return `null` from the following folders:
 * * `/storage/AAAA-BBBB/Music/Metal`
 * * `/storage/AAAA-BBBB/Music/Pop/Albums`
 */
private fun DocumentFile.getSubPath(context: Context, otherFolderAbsolutePath: String): String? {
  val a = getAbsolutePath(context).split('/').filter { it.isNotEmpty() }
  val b = otherFolderAbsolutePath.split('/').filter { it.isNotEmpty() }
  val trimPath = { longPath: List<String>, shortPath: List<String> ->
    if (longPath.take(shortPath.size) == shortPath) {
      if (longPath.size == shortPath.size) {
        ""
      } else {
        longPath.takeLast(longPath.size - shortPath.size).joinToString("/")
      }
    } else {
      null
    }
  }
  return if (a.size > b.size) trimPath(a, b) else trimPath(b, a)
}

/**
 * Root path of this file.
 * * For file picked from [Intent.ACTION_OPEN_DOCUMENT] or [Intent.ACTION_CREATE_DOCUMENT], it will
 *   return empty `String`
 * * For file stored in external or primary storage, it will return
 *   [SimpleStorage.externalStoragePath].
 * * For file stored in SD Card, it will return something like `/storage/6881-2249`
 */
fun DocumentFile.getRootPath(context: Context) =
  when {
    isRawFile -> uri.path?.let { File(it).getRootPath(context) }.orEmpty()
    !isTreeDocumentFile -> ""
    inSdCardStorage(context) -> "/storage/${getStorageId(context)}"
    else -> SimpleStorage.externalStoragePath
  }

fun DocumentFile.getRelativePath(context: Context) =
  getBasePath(context).substringBeforeLast('/', "")

/**
 * * For file in SD Card => `/storage/6881-2249/Music/song.mp3`
 * * For file in external storage => `/storage/emulated/0/Music/song.mp3`
 *
 * If you want to remember file locations in database or preference, please use this function. When
 * you reopen the file, just call [DocumentFileCompat.fromFullPath]
 *
 * @return File's actual path. Returns empty `String` if:
 * * It is not a raw file and the authority is neither
 *   [DocumentFileCompat.EXTERNAL_STORAGE_AUTHORITY] nor
 *   [DocumentFileCompat.DOWNLOADS_FOLDER_AUTHORITY]
 * * The authority is [DocumentFileCompat.DOWNLOADS_FOLDER_AUTHORITY], but [isTreeDocumentFile]
 *   returns `false`
 *
 * @see File.getAbsolutePath
 * @see getSimplePath
 */
fun DocumentFile.getAbsolutePath(context: Context): String {
  val path = uri.path.orEmpty()
  val storageID = getStorageId(context)
  return when {
    isRawFile -> path

    isDocumentsDocument -> {
      val basePath = path.substringAfterLast("/home:", "").trimFileSeparator()
      "${PublicDirectory.DOCUMENTS.absolutePath}/$basePath".trimEnd('/')
    }

    isExternalStorageDocument && path.contains("/document/$storageID:") -> {
      val basePath = path.substringAfterLast("/document/$storageID:", "").trimFileSeparator()
      if (storageID == PRIMARY) {
        "${SimpleStorage.externalStoragePath}/$basePath".trimEnd('/')
      } else {
        "/storage/$storageID/$basePath".trimEnd('/')
      }
    }

    uri.toString().let {
      it == DocumentFileCompat.DOWNLOADS_TREE_URI ||
        it == "${DocumentFileCompat.DOWNLOADS_TREE_URI}/document/downloads"
    } -> PublicDirectory.DOWNLOADS.absolutePath

    isDownloadsDocument -> {
      when {
        // API 26 - 27 => content://com.android.providers.downloads.documents/document/22
        Build.VERSION.SDK_INT < Build.VERSION_CODES.P && path.matches(Regex("/document/\\d+")) -> {
          val fileName = MediaFile(context, uri).name ?: return ""
          File(PublicDirectory.DOWNLOADS.file, fileName).absolutePath
        }

        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
          path.matches(Regex("(.*?)/ms[f,d]:\\d+(.*?)")) -> {
          if (isTreeDocumentFile) {
            val parentTree = mutableListOf(name.orEmpty())
            var parent = this
            while (parent.parentFile?.also { parent = it } != null) {
              parentTree.add(parent.name.orEmpty())
            }
            "${SimpleStorage.externalStoragePath}/${parentTree.reversed().joinToString("/")}"
              .trimEnd('/')
          } else {
            // we can't use msf/msd ID as MediaFile ID to fetch relative path, so just return empty
            // String
            ""
          }
        }

        else -> path.substringAfterLast("/document/raw:", "").trimEnd('/')
      }
    }

    !isTreeDocumentFile -> ""
    inPrimaryStorage(context) ->
      "${SimpleStorage.externalStoragePath}/${getBasePath(context)}".trimEnd('/')
    else -> "/storage/$storageID/${getBasePath(context)}".trimEnd('/')
  }
}

/** @see getAbsolutePath */
fun DocumentFile.getSimplePath(context: Context) =
  "${getStorageId(context)}:${getBasePath(context)}".removePrefix(":")

@JvmOverloads
fun DocumentFile.findParent(context: Context, requiresWriteAccess: Boolean = true): DocumentFile? {
  return parentFile
    ?: if (isTreeDocumentFile || isRawFile) {
      val parentPath = getAbsolutePath(context).parent()
      if (parentPath.isEmpty()) {
        null
      } else {
        DocumentFileCompat.fromFullPath(
            context,
            parentPath,
            requiresWriteAccess = requiresWriteAccess,
          )
          ?.also {
            try {
              val field = DocumentFile::class.java.getDeclaredField("mParent")
              field.isAccessible = true
              field.set(this, it)
            } catch (e: Exception) {
              Log.w(
                "DocumentFileUtils",
                "Cannot modify field mParent in androidx.documentfile.provider.DocumentFile. " +
                  "Please exclude DocumentFile from obfuscation.",
                e,
              )
            }
          }
      }
    } else null
}

/**
 * Delete this file and create new empty file using previous `filename` and `mimeType`. It cannot be
 * applied if current [DocumentFile] is a directory.
 */
fun DocumentFile.recreateFile(context: Context): DocumentFile? {
  return if (isFile && (isRawFile || isExternalStorageDocument)) {
    val filename = name.orEmpty()
    val parent = findParent(context)
    if (parent?.isWritable(context) == true) {
      val mimeType = type
      forceDelete(context)
      parent.makeFile(context, filename, mimeType)
    } else null
  } else null
}

@JvmOverloads
fun DocumentFile.getRootDocumentFile(context: Context, requiresWriteAccess: Boolean = false) =
  when {
    isTreeDocumentFile ->
      DocumentFileCompat.getRootDocumentFile(context, getStorageId(context), requiresWriteAccess)
    isRawFile ->
      uri.path?.run {
        File(this).getRootRawFile(context, requiresWriteAccess)?.let { DocumentFile.fromFile(it) }
      }
    else -> null
  }

/**
 * @return `true` if this file exists and writeable. [DocumentFile.canWrite] may return false if you
 *   have no URI permission for read & write access.
 */
fun DocumentFile.canModify(context: Context) = canRead() && isWritable(context)

/**
 * Use it, because [DocumentFile.canWrite] is unreliable on Android 10. Read
 * [this issue](https://github.com/anggrayudi/SimpleStorage/issues/24#issuecomment-830000378)
 */
fun DocumentFile.isWritable(context: Context) =
  if (isRawFile) File(uri.path!!).isWritable(context) else canWrite()

fun DocumentFile.isRootUriPermissionGranted(context: Context): Boolean {
  return isExternalStorageDocument &&
    DocumentFileCompat.isStorageUriPermissionGranted(context, getStorageId(context))
}

fun DocumentFile.getFormattedSize(context: Context): String =
  Formatter.formatFileSize(context, length())

/** Avoid duplicate file name. */
@WorkerThread
fun DocumentFile.autoIncrementFileName(context: Context, filename: String): String {
  toRawFile(context)?.let { if (it.canRead()) return it.autoIncrementFileName(filename) }
  val files = children
  return if (files.find { it.name == filename }?.exists() == true) {
    val baseName = MimeType.getBaseFileName(filename)
    val ext = MimeType.getExtensionFromFileName(filename)
    val prefix = "$baseName ("
    var lastFileCount =
      files
        .filter {
          val name = it.name.orEmpty()
          name.startsWith(prefix) &&
            (DocumentFileCompat.FILE_NAME_DUPLICATION_REGEX_WITH_EXTENSION.matches(name) ||
              DocumentFileCompat.FILE_NAME_DUPLICATION_REGEX_WITHOUT_EXTENSION.matches(name))
        }
        .maxOfOrNull {
          it.name.orEmpty().substringAfterLast('(', "").substringBefore(')', "").toIntOrNull() ?: 0
        } ?: 0
    "$baseName (${++lastFileCount}).$ext".trimEnd('.')
  } else {
    filename
  }
}

/** Useful for creating temporary files. The extension is `*.bin` */
@WorkerThread
@JvmOverloads
fun DocumentFile.createBinaryFile(
  context: Context,
  name: String,
  mode: CreateMode = CreateMode.CREATE_NEW,
) = makeFile(context, name, MimeType.BINARY_FILE, mode)

/**
 * Similar to [DocumentFile.createFile], but adds compatibility on API 28 and lower. Creating files
 * in API 28- with `createFile("my video.mp4", "video/mp4")` will create `my video.mp4`, whereas API
 * 29+ will create `my video.mp4.mp4`. This function helps you to fix this kind of bug.
 *
 * @param mimeType use [MimeType.UNKNOWN] if you're not sure about the file type
 * @param name you can input `My Video`, `My Video.mp4` or `My Folder/Sub Folder/My Video.mp4`
 * @param onConflict when this callback is set and `mode` is not [CreateMode.CREATE_NEW], then the
 *   user will be asked for resolution if conflict happens
 */
@WorkerThread
@JvmOverloads
fun DocumentFile.makeFile(
  context: Context,
  name: String,
  mimeType: String? = MimeType.UNKNOWN,
  mode: CreateMode = CreateMode.CREATE_NEW,
  onConflict: SingleFileConflictCallback<DocumentFile>? = null,
): DocumentFile? {
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
  var existingFile: DocumentFile? = null
  if (onConflict != null) {
    parent.child(context, fullFileName)?.let { targetFile ->
      existingFile = targetFile
      createMode =
        awaitUiResultWithPending(onConflict.uiScope) {
            onConflict.onFileConflict(targetFile, SingleFileConflictCallback.FileConflictAction(it))
          }
          .toCreateMode(true)
    }
  }

  if (createMode != CreateMode.CREATE_NEW) {
    (existingFile ?: parent.child(context, fullFileName))?.let {
      return when {
        createMode == CreateMode.REPLACE -> it.recreateFile(context)
        createMode != CreateMode.SKIP_IF_EXISTS && it.isFile -> it
        else -> null
      }
    }
  }

  if (isRawFile) {
    // RawDocumentFile does not avoid duplicate file name, but TreeDocumentFile does.
    return DocumentFile.fromFile(
      toRawFile(context)?.makeFile(context, cleanName, mimeType, createMode) ?: return null
    )
  }

  val correctMimeType =
    MimeType.getMimeTypeFromExtension(extension).let {
      if (it == MimeType.UNKNOWN) MimeType.BINARY_FILE else it
    }

  return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
    parent.createFile(correctMimeType, baseFileName)?.also {
      if (correctMimeType == MimeType.BINARY_FILE && it.name != fullFileName)
        it.renameTo(fullFileName)
    }
  } else {
    parent.createFile(correctMimeType, fullFileName)
  }
}

/** @param name can input `MyFolder` or `MyFolder/SubFolder` */
@WorkerThread
@JvmOverloads
fun DocumentFile.makeFolder(
  context: Context,
  name: String,
  mode: CreateMode = CreateMode.CREATE_NEW,
): DocumentFile? {
  if (!isDirectory || !isWritable(context)) {
    return null
  }

  if (isRawFile) {
    return DocumentFile.fromFile(toRawFile(context)?.makeFolder(context, name, mode) ?: return null)
  }

  // if name is "Aduhhh/Now/Dee", system will convert it to Aduhhh_Now_Dee, so create a sequence
  val directorySequence =
    DocumentFileCompat.getDirectorySequence(name.removeForbiddenCharsFromFilename()).toMutableList()
  val folderNameLevel1 = directorySequence.removeFirstOrNull() ?: return null
  var currentDirectory =
    if (isDownloadsDocument && isTreeDocumentFile)
      (toWritableDownloadsDocumentFile(context) ?: return null)
    else this
  val folderLevel1 = currentDirectory.child(context, folderNameLevel1)

  currentDirectory =
    if (folderLevel1 == null || mode == CreateMode.CREATE_NEW) {
      currentDirectory.createDirectory(folderNameLevel1) ?: return null
    } else if (mode == CreateMode.REPLACE) {
      folderLevel1.forceDelete(context, true)
      if (folderLevel1.isDirectory) folderLevel1
      else currentDirectory.createDirectory(folderNameLevel1) ?: return null
    } else if (
      mode != CreateMode.SKIP_IF_EXISTS && folderLevel1.isDirectory && folderLevel1.canRead()
    ) {
      folderLevel1
    } else {
      return null
    }

  val resolver = context.contentResolver
  directorySequence.forEach { folder ->
    try {
      val directory = currentDirectory.quickFindTreeFile(context, resolver, folder)
      currentDirectory =
        if (directory == null) {
          currentDirectory.createDirectory(folder) ?: return null
        } else if (directory.isDirectory && directory.canRead()) {
          directory
        } else {
          return null
        }
    } catch (_: Exception) {
      return null
    }
  }
  return currentDirectory
}

/** Use this function if you cannot create or read file/folder in downloads directory. */
@WorkerThread
fun DocumentFile.toWritableDownloadsDocumentFile(context: Context): DocumentFile? {
  return if (isDownloadsDocument) {
    val path = uri.path.orEmpty()
    when {
      uri.toString() == "${DocumentFileCompat.DOWNLOADS_TREE_URI}/document/downloads" ->
        takeIf { it.isWritable(context) }

      // content://com.android.providers.downloads.documents/document/raw%3A%2Fstorage%2Femulated%2F0%2FDownload%2Fscreenshot.jpeg
      // content://com.android.providers.downloads.documents/tree/downloads/document/raw%3A%2Fstorage%2Femulated%2F0%2FDownload%2FIKO5
      // raw:/storage/emulated/0/Download/IKO5
      Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
        (path.startsWith("/tree/downloads/document/raw:") || path.startsWith("/document/raw:")) -> {
        val downloads =
          DocumentFileCompat.fromPublicFolder(
            context,
            PublicDirectory.DOWNLOADS,
            considerRawFile = false,
          ) ?: return null
        val fullPath = path.substringAfterLast("/document/raw:")
        val subFile = fullPath.substringAfter("/${Environment.DIRECTORY_DOWNLOADS}", "")
        downloads.child(context, subFile, true)
      }

      // msd for directories and msf for files
      Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
        (
        // // If comes from SAF file picker ACTION_OPEN_DOCUMENT on API 30+
        path.matches(Regex("/document/ms[f,d]:\\d+"))
        // If comes from SAF folder picker ACTION_OPEN_DOCUMENT_TREE,
        // e.g.
        // content://com.android.providers.downloads.documents/tree/msd%3A535/document/msd%3A535
        ||
          path.matches(Regex("/tree/ms[f,d]:\\d+(.*?)"))
          // If comes from findFile() or fromPublicFolder(),
          // e.g.
          // content://com.android.providers.downloads.documents/tree/downloads/document/msd%3A271
          ||
          path.matches(Regex("/tree/downloads/document/ms[f,d]:\\d+"))) ||
        Build.VERSION.SDK_INT < Build.VERSION_CODES.Q &&
          (
          // If comes from SAF folder picker ACTION_OPEN_DOCUMENT_TREE,
          // e.g.
          // content://com.android.providers.downloads.documents/tree/raw%3A%2Fstorage%2Femulated%2F0%2FDownload%2FDenai/document/raw%3A%2Fstorage%2Femulated%2F0%2FDownload%2FDenai
          path.startsWith("/tree/raw:")
          // If comes from findFile() or fromPublicFolder(),
          // e.g.
          // content://com.android.providers.downloads.documents/tree/downloads/document/raw%3A%2Fstorage%2Femulated%2F0%2FDownload%2FDenai
          ||
            path.startsWith("/tree/downloads/document/raw:")
            // API 26 - 27 => content://com.android.providers.downloads.documents/document/22
            ||
            path.matches(Regex("/document/\\d+"))) -> takeIf { it.isWritable(context) }

      else -> null
    }
  } else {
    null
  }
}

/** @param names full file names, with their extension */
fun DocumentFile.findFiles(
  names: Array<String>,
  documentType: DocumentFileType = DocumentFileType.ANY,
): List<DocumentFile> {
  val files = children.filter { it.name in names }
  return when (documentType) {
    DocumentFileType.FILE -> files.filter { it.isFile }
    DocumentFileType.FOLDER -> files.filter { it.isDirectory }
    else -> files
  }
}

fun DocumentFile.findFolder(name: String): DocumentFile? =
  children.find { it.name == name && it.isDirectory }

/** Expect the file is a file literally, not a folder. */
fun DocumentFile.findFileLiterally(name: String): DocumentFile? =
  children.find { it.name == name && it.isFile }

/**
 * @param recursive walk into sub folders
 * @param name find file name exactly
 * @param regex you can use regex `^.*containsName.*\$` to search file name that contains specific
 *   words
 * @param updateInterval in milliseconds. Set to `0` to emit all results at once.
 */
@OptIn(DelicateCoroutinesApi::class)
@JvmOverloads
@WorkerThread
fun DocumentFile.search(
  recursive: Boolean = false,
  documentType: DocumentFileType = DocumentFileType.ANY,
  mimeTypes: Array<String>? = null,
  name: String = "",
  regex: Regex? = null,
  updateInterval: Long = 0,
): Flow<List<DocumentFile>> = callbackFlow {
  when {
    !isDirectory || !canRead() -> send(emptyList())
    recursive -> {
      val fileTree = mutableListOf<DocumentFile>()
      val timer =
        if (updateInterval < 1) null
        else startCoroutineTimer(repeatMillis = updateInterval) { trySend(fileTree) }
      if (mimeTypes.isNullOrEmpty() || mimeTypes.any { it == MimeType.UNKNOWN }) {
        walkFileTreeForSearch(fileTree, documentType, emptyArray(), name, regex, this)
      } else {
        walkFileTreeForSearch(fileTree, DocumentFileType.FILE, mimeTypes, name, regex, this)
      }
      timer?.cancel()
      send(fileTree)
    }

    else -> {
      var sequence = listFiles().asSequence().filter { it.canRead() }
      if (regex != null) {
        sequence = sequence.filter { regex.matches(it.name.orEmpty()) }
      }
      val hasMimeTypeFilter =
        !mimeTypes.isNullOrEmpty() && !mimeTypes.any { it == MimeType.UNKNOWN }
      when {
        hasMimeTypeFilter || documentType == DocumentFileType.FILE ->
          sequence = sequence.filter { it.isFile }
        documentType == DocumentFileType.FOLDER -> sequence = sequence.filter { it.isDirectory }
      }
      if (hasMimeTypeFilter) {
        sequence = sequence.filter { it.matchesMimeTypes(mimeTypes!!) }
      }
      if (name.isNotEmpty()) {
        sequence = sequence.filter { it.name == name }
      }

      val fileTree = mutableListOf<DocumentFile>()
      val timer =
        if (updateInterval < 1) null
        else startCoroutineTimer(repeatMillis = updateInterval) { trySend(fileTree) }
      sequence.forEach {
        if (isClosedForSend) {
          return@forEach
        }
        fileTree.add(it)
      }
      timer?.cancel()
      send(fileTree)
    }
  }
  close()
}

private fun DocumentFile.matchesMimeTypes(filterMimeTypes: Array<String>): Boolean {
  return filterMimeTypes.isEmpty() ||
    !MimeTypeFilter.matches(mimeTypeByFileName, filterMimeTypes).isNullOrEmpty()
}

@OptIn(DelicateCoroutinesApi::class)
private fun DocumentFile.walkFileTreeForSearch(
  fileTree: MutableList<DocumentFile>,
  documentType: DocumentFileType,
  mimeTypes: Array<String>,
  nameFilter: String,
  regex: Regex?,
  scope: ProducerScope<List<DocumentFile>>,
): List<DocumentFile> {
  for (file in listFiles()) {
    if (scope.isClosedForSend) break
    if (!canRead()) continue

    if (file.isFile) {
      if (documentType == DocumentFileType.FOLDER) {
        continue
      }
      val filename = file.name.orEmpty()
      if (
        (nameFilter.isEmpty() || filename == nameFilter) &&
          (regex == null || regex.matches(filename)) &&
          file.matchesMimeTypes(mimeTypes)
      ) {
        fileTree.add(file)
      }
    } else {
      if (documentType != DocumentFileType.FILE) {
        val folderName = file.name.orEmpty()
        if (
          (nameFilter.isEmpty() || folderName == nameFilter) &&
            (regex == null || regex.matches(folderName))
        ) {
          fileTree.add(file)
        }
      }
      fileTree.addAll(
        file.walkFileTreeForSearch(fileTree, documentType, mimeTypes, nameFilter, regex, scope)
      )
    }
  }
  return fileTree
}

/**
 * @param childrenOnly `true` to delete the folder contents only
 * @see File.deleteRecursively
 */
@WorkerThread
@JvmOverloads
fun DocumentFile.deleteRecursively(context: Context, childrenOnly: Boolean = false): Boolean {
  return if (isDirectory && canRead()) {
    val files =
      if (isDownloadsDocument) {
        toWritableDownloadsDocumentFile(context)?.walkFileTreeForDeletion() ?: return false
      } else {
        walkFileTreeForDeletion()
      }
    var count = files.size
    for (index in files.size - 1 downTo 0) {
      if (files[index].delete()) count--
    }
    count == 0 && (childrenOnly || delete() || !exists())
  } else {
    false
  }
}

/**
 * @param childrenOnly `true` to delete the folder contents only
 * @return `true` if the file/folder was deleted or does not exist
 * @see File.forceDelete
 */
@WorkerThread
@JvmOverloads
fun DocumentFile.forceDelete(context: Context, childrenOnly: Boolean = false): Boolean {
  return if (isDirectory) {
    deleteRecursively(context, childrenOnly)
  } else {
    delete() || !exists()
  }
}

private fun DocumentFile.walkFileTreeForDeletion(): List<DocumentFile> {
  val fileTree = mutableListOf<DocumentFile>()
  listFiles().forEach {
    if (!it.delete()) {
      fileTree.add(it)
    }
    if (it.isDirectory) {
      fileTree.addAll(it.walkFileTreeForDeletion())
    }
  }
  return fileTree
}

fun DocumentFile.deleteEmptyFolders(context: Context): Boolean {
  return if (isRawFile) {
    File(uri.path!!).deleteEmptyFolders(context)
    true
  } else if (isDirectory && isWritable(context)) {
    walkFileTreeAndDeleteEmptyFolders().reversed().forEach { it.delete() }
    true
  } else false
}

private fun DocumentFile.walkFileTreeAndDeleteEmptyFolders(): List<DocumentFile> {
  val fileTree = mutableListOf<DocumentFile>()
  listFiles().forEach {
    if (it.isDirectory && !it.delete()) { // Deletion is only success if the folder is empty
      fileTree.add(it)
      fileTree.addAll(it.walkFileTreeAndDeleteEmptyFolders())
    }
  }
  return fileTree
}

/** @param append if `false` and the file already exists, it will recreate the file. */
@JvmOverloads
@WorkerThread
fun DocumentFile.openOutputStream(context: Context, append: Boolean = true) =
  uri.openOutputStream(context, append)

@WorkerThread fun DocumentFile.openInputStream(context: Context) = uri.openInputStream(context)

@UiThread
fun DocumentFile.openFileIntent(context: Context, authority: String) =
  Intent(Intent.ACTION_VIEW)
    .setData(
      if (isRawFile) FileProvider.getUriForFile(context, authority, File(uri.path!!)) else uri
    )
    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

fun DocumentFile.hasParent(context: Context, parent: DocumentFile) =
  getAbsolutePath(context).hasParent(parent.getAbsolutePath(context))

fun DocumentFile.childOf(context: Context, parent: DocumentFile) =
  getAbsolutePath(context).childOf(parent.getAbsolutePath(context))

private fun DocumentFile.walkFileTree(context: Context): List<DocumentFile> {
  val fileTree = mutableListOf<DocumentFile>()
  listFiles().forEach {
    if (it.isDirectory) {
      if (it.isEmpty(context)) {
        fileTree.add(it)
      } else {
        fileTree.addAll(it.walkFileTree(context))
      }
    } else {
      fileTree.add(it)
    }
  }
  return fileTree
}

private fun DocumentFile.walkFileTreeAndSkipEmptyFiles(): List<DocumentFile> {
  val fileTree = mutableListOf<DocumentFile>()
  listFiles().forEach {
    if (it.isDirectory) {
      fileTree.addAll(it.walkFileTreeAndSkipEmptyFiles())
    } else if (it.length() > 0) {
      fileTree.add(it)
    }
  }
  return fileTree
}

private fun DocumentFile.walkFileTreeAndGetFilesOnly(): List<DocumentFile> {
  val fileTree = mutableListOf<DocumentFile>()
  listFiles().forEach {
    if (it.isFile) {
      fileTree.add(it)
    } else {
      fileTree.addAll(it.walkFileTreeAndGetFilesOnly())
    }
  }
  return fileTree
}

/**
 * Use [Zip4j](https://github.com/srikanth-lingala/zip4j) if you want to protect the ZIP file with
 * password. Simple Storage library must be lightweight, so avoid adding external library unless it
 * is really needed.
 */
@WorkerThread
fun List<DocumentFile>.compressToZip(
  context: Context,
  targetZipFile: DocumentFile,
  deleteSourceWhenComplete: Boolean = false,
  updateInterval: Long = 500,
  isFileSizeAllowed: CheckFileSize = defaultFileSizeChecker,
): Flow<ZipCompressionResult> = callbackFlow {
  send(ZipCompressionResult.CountingFiles)
  val treeFiles = ArrayList<DocumentFile>(size)
  val mediaFiles = ArrayList<DocumentFile>(size)
  var foldersBasePath = mutableListOf<String>()
  val directories = mutableListOf<DocumentFile>()
  for (srcFile in distinctBy { it.uri }) {
    if (srcFile.exists()) {
      if (!srcFile.canRead()) {
        sendAndClose(
          ZipCompressionResult.Error(
            ZipCompressionErrorCode.STORAGE_PERMISSION_DENIED,
            "Can't read file: ${srcFile.uri}",
          )
        )
        return@callbackFlow
      } else if (srcFile.isFile) {
        if (srcFile.isTreeDocumentFile || srcFile.isRawFile) {
          treeFiles.add(srcFile)
        } else {
          mediaFiles.add(srcFile)
        }
      } else if (srcFile.isDirectory) {
        directories.add(srcFile)
      }
    } else {
      sendAndClose(
        ZipCompressionResult.Error(
          ZipCompressionErrorCode.MISSING_ENTRY_FILE,
          "File not found: ${srcFile.uri}",
        )
      )
      return@callbackFlow
    }
  }

  /*
  Given two folders:
  /storage/emulated/0/Movies
  /storage/emulated/0/Movies/Horror
  Then eleminate /storage/emulated/0/Movies/Horror from the list and archive all files under /storage/emulated/0/Movies, included Horror
   */
  class EntryFile(val file: DocumentFile, var path: String) {

    override fun equals(other: Any?) = this === other || other is EntryFile && path == other.path

    override fun hashCode() = path.hashCode()
  }

  val srcFolders =
    directories
      .map { EntryFile(it, it.getAbsolutePath(context)) }
      .distinctBy { it.path }
      .toMutableList()
  DocumentFileCompat.findUniqueParents(context, srcFolders.map { it.path }).forEach { parent ->
    srcFolders.removeAll { it.path.childOf(parent) }
  }
  srcFolders.forEach {
    // skip empty folders
    treeFiles.addAll(it.file.walkFileTreeAndGetFilesOnly())
    foldersBasePath.add(it.file.getBasePath(context))
  }

  // do not compress target ZIP file itself
  val targetZipPath = targetZipFile.getAbsolutePath(context)
  treeFiles.removeAll {
    it.uri == targetZipFile.uri || it.getAbsolutePath(context) == targetZipPath
  }
  mediaFiles.removeAll { it.uri == targetZipFile.uri }

  val totalFiles = treeFiles.size + mediaFiles.size
  if (totalFiles == 0) {
    sendAndClose(
      ZipCompressionResult.Error(ZipCompressionErrorCode.MISSING_ENTRY_FILE, "No entry files found")
    )
    return@callbackFlow
  }

  var actualFilesSize = 0L

  treeFiles.forEach { actualFilesSize += it.length() }
  mediaFiles.forEach { actualFilesSize += it.length() }
  if (
    !isFileSizeAllowed(
      DocumentFileCompat.getFreeSpace(context, targetZipFile.getStorageId(context)),
      actualFilesSize,
    )
  ) {
    sendAndClose(ZipCompressionResult.Error(ZipCompressionErrorCode.NO_SPACE_LEFT_ON_TARGET_PATH))
    return@callbackFlow
  }

  val entryFiles = ArrayList<EntryFile>(totalFiles)
  treeFiles.forEach { entryFiles.add(EntryFile(it, it.getBasePath(context))) }
  val parentPaths =
    DocumentFileCompat.findUniqueParents(
        context,
        entryFiles.map { "/" + it.path.substringBeforeLast('/') },
      )
      .map { it.trim('/') }
  foldersBasePath =
    DocumentFileCompat.findUniqueParents(context, foldersBasePath.map { "/$it" })
      .map { it.trim('/') }
      .toMutableList()
  entryFiles.forEach { entry ->
    for (parentPath in parentPaths) {
      if (entry.path.startsWith(parentPath)) {
        val delimiter =
          if (parentPath in foldersBasePath) {
            parentPath.substringBefore('/', "")
          } else {
            parentPath
          }
        entry.path = entry.path.substringAfter(delimiter).trim('/')
        break
      }
    }
  }
  mediaFiles.forEach { entryFiles.add(EntryFile(it, it.fullName)) }
  val duplicateFiles = entryFiles.groupingBy { it }.eachCount().filterValues { it > 1 }
  if (duplicateFiles.isNotEmpty()) {
    sendAndClose(
      ZipCompressionResult.Error(
        ZipCompressionErrorCode.DUPLICATE_ENTRY_FILE,
        "Found duplicate entry files: ${duplicateFiles.keys.map { it.file.uri }}",
      )
    )
    return@callbackFlow
  }

  var zipFile: DocumentFile? = targetZipFile
  if (!targetZipFile.exists() || targetZipFile.isDirectory) {
    zipFile =
      targetZipFile.findParent(context)?.makeFile(context, targetZipFile.fullName, MimeType.ZIP)
  }
  if (zipFile == null) {
    sendAndClose(ZipCompressionResult.Error(ZipCompressionErrorCode.CANNOT_CREATE_FILE_IN_TARGET))
    return@callbackFlow
  }
  if (!zipFile.isWritable(context)) {
    sendAndClose(
      ZipCompressionResult.Error(
        ZipCompressionErrorCode.STORAGE_PERMISSION_DENIED,
        "Destination ZIP file is not writable",
      )
    )
    return@callbackFlow
  }

  var success = false
  var bytesCompressed = 0L
  var timer: Job? = null
  var zos: ZipOutputStream? = null
  try {
    zos = ZipOutputStream(zipFile.openOutputStream(context))
    var writeSpeed = 0
    var fileCompressedCount = 0
    // using timer on small file is useless. We set minimum 10MB.
    if (updateInterval > 0 && actualFilesSize > 10 * FileSize.MB) {
      timer =
        startCoroutineTimer(repeatMillis = updateInterval) {
          trySend(
            ZipCompressionResult.Compressing(
              bytesCompressed * 100f / actualFilesSize,
              bytesCompressed,
              writeSpeed,
              fileCompressedCount,
            )
          )
          writeSpeed = 0
        }
    }
    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
    entryFiles.forEach { entry ->
      entry.file.openInputStream(context)?.use { input ->
        zos.putNextEntry(ZipEntry(entry.path))
        var bytes = input.read(buffer)
        while (bytes >= 0) {
          zos.write(buffer, 0, bytes)
          bytesCompressed += bytes
          writeSpeed += bytes
          bytes = input.read(buffer)
        }
      } ?: throw FileNotFoundException("File ${entry.file.uri} is not found")
      fileCompressedCount++
    }
    success = true
  } catch (_: InterruptedIOException) {
    send(ZipCompressionResult.Error(ZipCompressionErrorCode.CANCELED))
  } catch (e: FileNotFoundException) {
    send(ZipCompressionResult.Error(ZipCompressionErrorCode.MISSING_ENTRY_FILE, e.message))
  } catch (e: IOException) {
    send(ZipCompressionResult.Error(ZipCompressionErrorCode.UNKNOWN_IO_ERROR, e.message))
  } catch (e: SecurityException) {
    send(ZipCompressionResult.Error(ZipCompressionErrorCode.STORAGE_PERMISSION_DENIED, e.message))
  } finally {
    timer?.cancel()
    zos.closeEntryQuietly()
    zos.closeStreamQuietly()
  }
  if (success) {
    if (deleteSourceWhenComplete) {
      send(ZipCompressionResult.DeletingEntryFiles)
      forEach { it.forceDelete(context) }
    }
    val sizeReduction = (actualFilesSize - zipFile.length()).toFloat() / actualFilesSize * 100
    send(ZipCompressionResult.Completed(zipFile, actualFilesSize, totalFiles, sizeReduction))
  } else {
    zipFile.delete()
  }
  close()
}

/**
 * It can't unzip password-protected archives. You'll need
 * [Zip4j](https://github.com/srikanth-lingala/zip4j) to unzip encrypted archives.
 */
@WorkerThread
fun DocumentFile.decompressZip(
  context: Context,
  targetFolder: DocumentFile,
  updateInterval: Long = 500,
  isFileSizeAllowed: CheckFileSize = defaultFileSizeChecker,
  onConflict: SingleFileConflictCallback<DocumentFile>? = null,
): Flow<ZipDecompressionResult> = callbackFlow {
  send(ZipDecompressionResult.Validating)
  if (exists()) {
    if (!canRead()) {
      sendAndClose(
        ZipDecompressionResult.Error(
          ZipDecompressionErrorCode.STORAGE_PERMISSION_DENIED,
          "Can't read file: $uri",
        )
      )
      return@callbackFlow
    } else if (isFile) {
      if (type != MimeType.ZIP && name?.endsWith(".zip", ignoreCase = true) != false) {
        sendAndClose(
          ZipDecompressionResult.Error(
            ZipDecompressionErrorCode.NOT_A_ZIP_FILE,
            "Not a ZIP file: $uri",
          )
        )
        return@callbackFlow
      }
    } else {
      sendAndClose(
        ZipDecompressionResult.Error(
          ZipDecompressionErrorCode.NOT_A_ZIP_FILE,
          "Not a ZIP file: $uri",
        )
      )
      return@callbackFlow
    }
  } else {
    sendAndClose(
      ZipDecompressionResult.Error(
        ZipDecompressionErrorCode.MISSING_ZIP_FILE,
        "ZIP file not found: $uri",
      )
    )
    return@callbackFlow
  }

  var destFolder: DocumentFile? = targetFolder
  if (!targetFolder.exists() || targetFolder.isFile) {
    destFolder = targetFolder.findParent(context)?.makeFolder(context, targetFolder.fullName)
  }
  if (destFolder == null || !destFolder.isWritable(context)) {
    sendAndClose(
      ZipDecompressionResult.Error(
        ZipDecompressionErrorCode.STORAGE_PERMISSION_DENIED,
        "Destination folder is not writable",
      )
    )
    return@callbackFlow
  }

  val zipSize = length()
  if (
    !isFileSizeAllowed(
      DocumentFileCompat.getFreeSpace(context, targetFolder.getStorageId(context)),
      zipSize,
    )
  ) {
    sendAndClose(
      ZipDecompressionResult.Error(ZipDecompressionErrorCode.NO_SPACE_LEFT_ON_TARGET_PATH)
    )
    return@callbackFlow
  }

  var success = false
  var bytesDecompressed = 0L
  var skippedDecompressedBytes = 0L
  var fileDecompressedCount = 0
  var timer: Job? = null
  var zis: ZipInputStream? = null
  var targetFile: DocumentFile? = null
  try {
    zis = ZipInputStream(openInputStream(context))
    var writeSpeed = 0
    // using timer on small file is useless. We set minimum 10MB.
    if (updateInterval > 0 && zipSize > 10 * FileSize.MB) {
      timer =
        startCoroutineTimer(repeatMillis = updateInterval) {
          trySend(
            ZipDecompressionResult.Decompressing(
              bytesDecompressed,
              writeSpeed,
              fileDecompressedCount,
            )
          )
          writeSpeed = 0
        }
    }
    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
    var entry = zis.nextEntry
    var canSuccess = true
    while (entry != null) {
      if (entry.isDirectory) {
        destFolder.makeFolder(context, entry.name, CreateMode.REUSE)
      } else {
        val folder =
          entry.name.substringBeforeLast('/', "").let {
            if (it.isEmpty()) destFolder else destFolder.makeFolder(context, it, CreateMode.REUSE)
          } ?: throw IOException()
        val fileName = entry.name.substringAfterLast('/')
        targetFile = folder.makeFile(context, fileName, onConflict = onConflict)
        if (targetFile == null) {
          send(ZipDecompressionResult.Error(ZipDecompressionErrorCode.CANNOT_CREATE_FILE_IN_TARGET))
          canSuccess = false
          break
        }
        if (targetFile.length() > 0 && targetFile.isFile) {
          // user has selected 'SKIP'
          skippedDecompressedBytes += targetFile.length()
          entry = zis.nextEntry
          continue
        }
        targetFile.openOutputStream(context)?.use { output ->
          var bytes = zis.read(buffer)
          while (bytes >= 0) {
            output.write(buffer, 0, bytes)
            bytesDecompressed += bytes
            writeSpeed += bytes
            bytes = zis.read(buffer)
          }
        } ?: throw IOException()
        fileDecompressedCount++
      }
      entry = zis.nextEntry
    }
    success = canSuccess
  } catch (_: InterruptedIOException) {
    send(ZipDecompressionResult.Error(ZipDecompressionErrorCode.CANCELED))
  } catch (e: FileNotFoundException) {
    send(ZipDecompressionResult.Error(ZipDecompressionErrorCode.MISSING_ZIP_FILE, e.message))
  } catch (e: IOException) {
    if (e.message?.contains("no space", true) == true) {
      send(ZipDecompressionResult.Error(ZipDecompressionErrorCode.NO_SPACE_LEFT_ON_TARGET_PATH))
    } else {
      send(ZipDecompressionResult.Error(ZipDecompressionErrorCode.UNKNOWN_IO_ERROR, e.message))
    }
  } catch (e: SecurityException) {
    send(
      ZipDecompressionResult.Error(ZipDecompressionErrorCode.STORAGE_PERMISSION_DENIED, e.message)
    )
  } finally {
    timer?.cancel()
    zis.closeEntryQuietly()
    zis.closeStreamQuietly()
  }
  if (success) {
    // Sometimes, the decompressed size is smaller than the compressed size, and you may get
    // negative values. You should worry about this.
    val sizeExpansion = (bytesDecompressed - zipSize).toFloat() / zipSize * 100
    send(
      ZipDecompressionResult.Completed(
        this,
        destFolder,
        bytesDecompressed,
        skippedDecompressedBytes,
        fileDecompressedCount,
        sizeExpansion,
      )
    )
  } else {
    targetFile?.delete()
  }
  close()
}

@WorkerThread
fun List<DocumentFile>.moveTo(
  context: Context,
  targetParentFolder: DocumentFile,
  skipEmptyFiles: Boolean = true,
  updateInterval: Long = 500,
  isFileSizeAllowed: CheckFileSize = defaultFileSizeChecker,
  onConflict: MultipleFilesConflictCallback,
): Flow<MultipleFilesResult> {
  return copyTo(
    context,
    targetParentFolder,
    skipEmptyFiles,
    true,
    updateInterval,
    isFileSizeAllowed,
    onConflict,
  )
}

@WorkerThread
fun List<DocumentFile>.copyTo(
  context: Context,
  targetParentFolder: DocumentFile,
  skipEmptyFiles: Boolean = true,
  updateInterval: Long = 500,
  isFileSizeAllowed: CheckFileSize = defaultFileSizeChecker,
  onConflict: MultipleFilesConflictCallback,
): Flow<MultipleFilesResult> {
  return copyTo(
    context,
    targetParentFolder,
    skipEmptyFiles,
    false,
    updateInterval,
    isFileSizeAllowed,
    onConflict,
  )
}

@OptIn(DelicateCoroutinesApi::class)
private fun List<DocumentFile>.copyTo(
  context: Context,
  targetParentFolder: DocumentFile,
  skipEmptyFiles: Boolean = true,
  deleteSourceWhenComplete: Boolean,
  updateInterval: Long = 500,
  isFileSizeAllowed: CheckFileSize = defaultFileSizeChecker,
  onConflict: MultipleFilesConflictCallback,
): Flow<MultipleFilesResult> = callbackFlow {
  send(MultipleFilesResult.Validating)
  val pair = doesMeetCopyRequirements(context, targetParentFolder, this, onConflict)
  if (pair == null) {
    close()
    return@callbackFlow
  }
  send(MultipleFilesResult.Preparing)

  val validSources = pair.second
  val writableTargetParentFolder = pair.first
  val conflictResolutions =
    validSources.handleParentFolderConflict(context, writableTargetParentFolder, this, onConflict)
  if (conflictResolutions == null) {
    close()
    return@callbackFlow
  }
  validSources.removeAll(
    conflictResolutions
      .filter { it.solution == SingleFolderConflictCallback.ConflictResolution.SKIP }
      .map { it.source }
  )
  if (validSources.isEmpty()) {
    close()
    return@callbackFlow
  }

  send(MultipleFilesResult.CountingFiles)

  class SourceInfo(
    val children: List<DocumentFile>?,
    val size: Long,
    val totalFiles: Int,
    val conflictResolution: SingleFolderConflictCallback.ConflictResolution,
  )

  val sourceInfos =
    validSources
      .associateWith { src ->
        val resolution =
          conflictResolutions.find { it.source == src }?.solution
            ?: SingleFolderConflictCallback.ConflictResolution.CREATE_NEW
        if (src.isFile) {
          SourceInfo(null, src.length(), 1, resolution)
        } else {
          val children =
            if (skipEmptyFiles) src.walkFileTreeAndSkipEmptyFiles() else src.walkFileTree(context)
          var totalFilesToCopy = 0
          var totalSizeToCopy = 0L
          children.forEach {
            if (it.isFile) {
              totalFilesToCopy++
              totalSizeToCopy += it.length()
            }
          }
          SourceInfo(children, totalSizeToCopy, totalFilesToCopy, resolution)
        }
        // allow empty folders, but empty files need check
      }
      .filterValues { it.children != null || (skipEmptyFiles && it.size > 0 || !skipEmptyFiles) }
      .toMutableMap()

  if (sourceInfos.isEmpty()) {
    sendAndClose(MultipleFilesResult.Completed(emptyList(), 0, 0, true))
    return@callbackFlow
  }

  // key=src, value=result
  val results = mutableMapOf<DocumentFile, DocumentFile>()

  if (deleteSourceWhenComplete) {
    sourceInfos.forEach { (src, info) ->
      when (
        val result =
          src.tryMoveFolderByRenamingPath(
            context,
            writableTargetParentFolder,
            src.fullName,
            skipEmptyFiles,
            null,
            info.conflictResolution,
          )
      ) {
        is DocumentFile -> {
          results[src] = result
        }

        is FolderErrorCode -> {
          val errorCode =
            when (result) {
              FolderErrorCode.INVALID_TARGET_FOLDER -> MultipleFilesErrorCode.INVALID_TARGET_FOLDER
              FolderErrorCode.STORAGE_PERMISSION_DENIED ->
                MultipleFilesErrorCode.STORAGE_PERMISSION_DENIED
              else -> {
                close()
                return@callbackFlow
              }
            }
          sendAndClose(MultipleFilesResult.Error(errorCode))
          return@callbackFlow
        }
      }
    }

    var copiedFiles = 0
    results.forEach { sourceInfos.remove(it.key)?.run { copiedFiles += totalFiles } }

    if (sourceInfos.isEmpty()) {
      sendAndClose(
        MultipleFilesResult.Completed(results.map { it.value }, copiedFiles, copiedFiles, true)
      )
      return@callbackFlow
    }
  }

  val totalSizeToCopy = sourceInfos.values.sumOf { it.size }
  if (
    !isFileSizeAllowed(
      DocumentFileCompat.getFreeSpace(context, writableTargetParentFolder.getStorageId(context)),
      totalSizeToCopy,
    )
  ) {
    sendAndClose(MultipleFilesResult.Error(MultipleFilesErrorCode.NO_SPACE_LEFT_ON_TARGET_PATH))
    return@callbackFlow
  }

  val totalFilesToCopy = sourceInfos.values.sumOf { it.totalFiles }
  send(MultipleFilesResult.Starting(sourceInfos.map { it.key }, totalFilesToCopy))

  var totalCopiedFiles = 0
  var timer: Job? = null
  var bytesMoved = 0L
  var writeSpeed = 0
  val startTimer: (Boolean) -> Unit = { start ->
    if (start && updateInterval > 0) {
      timer =
        startCoroutineTimer(repeatMillis = updateInterval) {
          trySend(
            MultipleFilesResult.InProgress(
              bytesMoved * 100f / totalSizeToCopy,
              bytesMoved,
              writeSpeed,
              totalCopiedFiles,
            )
          )
          writeSpeed = 0
        }
    }
  }
  startTimer(totalSizeToCopy > 10 * FileSize.MB)

  var targetFile: DocumentFile? = null
  // This boolean flag is required to prevent the callback from called again on next FOR iteration
  // after the thread was interrupted
  var canceled = false
  val notifyCanceled: (MultipleFilesErrorCode) -> Unit = { errorCode ->
    if (!canceled) {
      canceled = true
      timer?.cancel()
      targetFile?.delete()
      trySend(
        MultipleFilesResult.Error(
          errorCode,
          completedData =
            MultipleFilesResult.Completed(
              results.map { it.value },
              totalFilesToCopy,
              totalCopiedFiles,
              false,
            ),
        )
      )
    }
  }

  val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
  var success = true

  @Suppress("BlockingMethodInNonBlockingContext")
  fun copy(sourceFile: DocumentFile, destFile: DocumentFile) {
    val outputStream = destFile.openOutputStream(context)
    if (outputStream == null) {
      trySend(MultipleFilesResult.Error(MultipleFilesErrorCode.CANNOT_CREATE_FILE_IN_TARGET))
      return
    }
    val inputStream = sourceFile.openInputStream(context)
    if (inputStream == null) {
      trySend(MultipleFilesResult.Error(MultipleFilesErrorCode.SOURCE_FILE_NOT_FOUND))
      return
    }
    try {
      var read = inputStream.read(buffer)
      while (read != -1) {
        outputStream.write(buffer, 0, read)
        bytesMoved += read
        writeSpeed += read
        read = inputStream.read(buffer)
      }
    } finally {
      inputStream.closeStreamQuietly()
      outputStream.closeStreamQuietly()
    }
    totalCopiedFiles++
    if (deleteSourceWhenComplete) sourceFile.delete()
  }

  val handleError: (Exception) -> Boolean = {
    val errorCode = it.toMultipleFileCallbackErrorCode()
    if (
      errorCode == MultipleFilesErrorCode.CANCELED ||
        errorCode == MultipleFilesErrorCode.UNKNOWN_IO_ERROR
    ) {
      notifyCanceled(errorCode)
      true
    } else {
      timer?.cancel()
      trySend(MultipleFilesResult.Error(errorCode))
      false
    }
  }

  val conflictedFiles = mutableListOf<SingleFolderConflictCallback.FileConflict>()

  for ((src, info) in sourceInfos) {
    if (isClosedForSend) {
      notifyCanceled(MultipleFilesErrorCode.CANCELED)
      close()
      return@callbackFlow
    }
    val mode = info.conflictResolution.toCreateMode()
    val targetRootFile =
      writableTargetParentFolder.let {
        if (src.isDirectory) it.makeFolder(context, src.fullName, mode)
        else it.makeFile(context, src.fullName, src.mimeType, mode)
      }
    if (targetRootFile == null) {
      timer?.cancel()
      sendAndClose(MultipleFilesResult.Error(MultipleFilesErrorCode.CANNOT_CREATE_FILE_IN_TARGET))
      return@callbackFlow
    }

    try {
      if (targetRootFile.isFile || info.children == null) {
        copy(src, targetRootFile)
        results[src] = targetRootFile
        continue
      }

      val srcParentAbsolutePath = src.getAbsolutePath(context)

      for (sourceFile in info.children) {
        if (isClosedForSend) {
          notifyCanceled(MultipleFilesErrorCode.CANCELED)
          close()
          return@callbackFlow
        }
        if (!sourceFile.exists()) {
          continue
        }

        val filename = sourceFile.getSubPath(context, srcParentAbsolutePath) ?: sourceFile.fullName
        if (filename.isEmpty()) continue

        if (sourceFile.isDirectory) {
          val newFolder = targetRootFile.makeFolder(context, filename, CreateMode.REUSE)
          if (newFolder == null) {
            success = false
            break
          }
          continue
        }

        targetFile = targetRootFile.makeFile(context, filename, sourceFile.type, CreateMode.REUSE)
        if (targetFile != null && targetFile.length() > 0) {
          conflictedFiles.add(SingleFolderConflictCallback.FileConflict(sourceFile, targetFile))
          continue
        }

        if (targetFile == null) {
          notifyCanceled(MultipleFilesErrorCode.CANNOT_CREATE_FILE_IN_TARGET)
          close()
          return@callbackFlow
        }

        copy(sourceFile, targetFile)
      }
      results[src] = targetRootFile
    } catch (e: Exception) {
      if (handleError(e)) {
        close()
        return@callbackFlow
      }
      success = false
      break
    }
  }

  val finalize: () -> Boolean = {
    timer?.cancel()
    if (!success || conflictedFiles.isEmpty()) {
      if (deleteSourceWhenComplete && success) {
        sourceInfos.forEach { (src, _) -> src.forceDelete(context) }
      }
      trySend(
        MultipleFilesResult.Completed(
          results.map { it.value },
          totalFilesToCopy,
          totalCopiedFiles,
          success,
        )
      )
      true
    } else false
  }
  if (finalize()) {
    close()
    return@callbackFlow
  }

  val solutions =
    awaitUiResultWithPending(onConflict.uiScope) {
        onConflict.onContentConflict(
          writableTargetParentFolder,
          conflictedFiles,
          SingleFolderConflictCallback.FolderContentConflictAction(it),
        )
      }
      .filter {
        // free up space first, by deleting some files
        if (it.solution == SingleFileConflictCallback.ConflictResolution.SKIP) {
          if (deleteSourceWhenComplete) it.source.delete()
          totalCopiedFiles++
        }
        it.solution != SingleFileConflictCallback.ConflictResolution.SKIP
      }

  val leftoverSize = totalSizeToCopy - bytesMoved
  startTimer(solutions.isNotEmpty() && leftoverSize > 10 * FileSize.MB)

  for (conflict in solutions) {
    if (isClosedForSend) {
      notifyCanceled(MultipleFilesErrorCode.CANCELED)
      return@callbackFlow
    }
    if (!conflict.source.isFile) {
      continue
    }
    val filename = conflict.target.fullName
    if (
      conflict.solution == SingleFileConflictCallback.ConflictResolution.REPLACE &&
        conflict.target.let { !it.delete() || it.exists() }
    ) {
      continue
    }

    targetFile = conflict.target.findParent(context)?.makeFile(context, filename)
    if (targetFile == null) {
      notifyCanceled(MultipleFilesErrorCode.CANNOT_CREATE_FILE_IN_TARGET)
      close()
      return@callbackFlow
    }

    try {
      copy(conflict.source, targetFile)
    } catch (e: Exception) {
      if (handleError(e)) {
        close()
        return@callbackFlow
      }
      success = false
      break
    }
  }

  finalize()
  close()
}

private fun List<DocumentFile>.doesMeetCopyRequirements(
  context: Context,
  targetParentFolder: DocumentFile,
  scope: ProducerScope<MultipleFilesResult>,
  onConflict: MultipleFilesConflictCallback,
): Pair<DocumentFile, MutableList<DocumentFile>>? {
  if (!targetParentFolder.isDirectory) {
    scope.trySend(MultipleFilesResult.Error(MultipleFilesErrorCode.INVALID_TARGET_FOLDER))
    return null
  }
  if (!targetParentFolder.isWritable(context)) {
    scope.trySend(MultipleFilesResult.Error(MultipleFilesErrorCode.STORAGE_PERMISSION_DENIED))
    return null
  }

  val targetParentFolderPath = targetParentFolder.getAbsolutePath(context)
  val sourceFiles = distinctBy { it.name }
  val invalidSourceFiles =
    sourceFiles
      .mapNotNull {
        when {
          !it.exists() -> Pair(it, FolderErrorCode.SOURCE_FILE_NOT_FOUND)
          !it.canRead() -> Pair(it, FolderErrorCode.STORAGE_PERMISSION_DENIED)
          targetParentFolderPath == it.parentFile?.getAbsolutePath(context) ->
            Pair(it, FolderErrorCode.TARGET_FOLDER_CANNOT_HAVE_SAME_PATH_WITH_SOURCE_FOLDER)

          else -> null
        }
      }
      .toMap()

  if (invalidSourceFiles.isNotEmpty()) {
    val abort =
      awaitUiResultWithPending(onConflict.uiScope) {
        onConflict.onInvalidSourceFilesFound(
          invalidSourceFiles,
          MultipleFilesConflictCallback.InvalidSourceFilesAction(it),
        )
      }
    if (abort) {
      scope.trySend(MultipleFilesResult.Error(MultipleFilesErrorCode.CANCELED))
      return null
    }
    if (invalidSourceFiles.size == size) {
      scope.trySend(MultipleFilesResult.Completed(emptyList(), 0, 0, true))
      return null
    }
  }

  val writableFolder =
    targetParentFolder.let {
      if (it.isDownloadsDocument) it.toWritableDownloadsDocumentFile(context) else it
    }
  if (writableFolder == null || !writableFolder.isDirectory) {
    scope.trySend(MultipleFilesResult.Error(MultipleFilesErrorCode.STORAGE_PERMISSION_DENIED))
    return null
  }

  return Pair(
    writableFolder,
    sourceFiles.toMutableList().apply { removeAll(invalidSourceFiles.map { it.key }) },
  )
}

private fun DocumentFile.tryMoveFolderByRenamingPath(
  context: Context,
  writableTargetParentFolder: DocumentFile,
  targetFolderParentName: String,
  skipEmptyFiles: Boolean,
  newFolderNameInTargetPath: String?,
  conflictResolution: SingleFolderConflictCallback.ConflictResolution,
): Any? {
  if (inSameMountPointWith(context, writableTargetParentFolder)) {
    if (inInternalStorage(context)) {
      toRawFile(context)
        ?.moveTo(
          context,
          writableTargetParentFolder.getAbsolutePath(context),
          targetFolderParentName,
          conflictResolution.toFileConflictResolution(),
        )
        ?.let {
          if (skipEmptyFiles) it.deleteEmptyFolders(context)
          return DocumentFile.fromFile(it)
        }
    }

    if (isExternalStorageManager(context)) {
      val sourceFile = toRawFile(context) ?: return FolderErrorCode.STORAGE_PERMISSION_DENIED
      writableTargetParentFolder.toRawFile(context)?.let { destinationFolder ->
        sourceFile
          .moveTo(
            context,
            destinationFolder,
            targetFolderParentName,
            conflictResolution.toFileConflictResolution(),
          )
          ?.let {
            if (skipEmptyFiles) it.deleteEmptyFolders(context)
            return DocumentFile.fromFile(it)
          }
      }
    }

    try {
      if (
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N &&
          !isRawFile &&
          writableTargetParentFolder.isTreeDocumentFile
      ) {
        val movedFileUri =
          parentFile?.uri?.let {
            DocumentsContract.moveDocument(
              context.contentResolver,
              uri,
              it,
              writableTargetParentFolder.uri,
            )
          }
        if (movedFileUri != null) {
          val newFile = context.fromTreeUri(movedFileUri)
          return if (newFile != null && newFile.isDirectory) {
            if (newFolderNameInTargetPath != null) newFile.renameTo(targetFolderParentName)
            if (skipEmptyFiles) newFile.deleteEmptyFolders(context)
            newFile
          } else {
            FolderErrorCode.INVALID_TARGET_FOLDER
          }
        }
      }
    } catch (_: Throwable) {
      return FolderErrorCode.STORAGE_PERMISSION_DENIED
    }
  }
  return null
}

@WorkerThread
fun DocumentFile.moveFolderTo(
  context: Context,
  targetParentFolder: DocumentFile,
  skipEmptyFiles: Boolean = true,
  newFolderNameInTargetPath: String? = null,
  updateInterval: Long = 500,
  isFileSizeAllowed: CheckFileSize = defaultFileSizeChecker,
  onConflict: SingleFolderConflictCallback,
): Flow<SingleFolderResult> {
  return copyFolderTo(
    context,
    targetParentFolder,
    skipEmptyFiles,
    newFolderNameInTargetPath,
    true,
    updateInterval,
    isFileSizeAllowed,
    onConflict,
  )
}

@WorkerThread
fun DocumentFile.copyFolderTo(
  context: Context,
  targetParentFolder: DocumentFile,
  skipEmptyFiles: Boolean = true,
  newFolderNameInTargetPath: String? = null,
  updateInterval: Long = 500,
  isFileSizeAllowed: CheckFileSize = defaultFileSizeChecker,
  onConflict: SingleFolderConflictCallback,
): Flow<SingleFolderResult> {
  return copyFolderTo(
    context,
    targetParentFolder,
    skipEmptyFiles,
    newFolderNameInTargetPath,
    false,
    updateInterval,
    isFileSizeAllowed,
    onConflict,
  )
}

/** @param skipEmptyFiles skip copying empty files & folders */
@OptIn(DelicateCoroutinesApi::class)
private fun DocumentFile.copyFolderTo(
  context: Context,
  targetParentFolder: DocumentFile,
  skipEmptyFiles: Boolean = true,
  newFolderNameInTargetPath: String? = null,
  deleteSourceWhenComplete: Boolean,
  updateInterval: Long = 500,
  isFileSizeAllowed: CheckFileSize = defaultFileSizeChecker,
  onConflict: SingleFolderConflictCallback,
): Flow<SingleFolderResult> = callbackFlow {
  val writableTargetParentFolder =
    doesMeetFolderCopyRequirements(context, targetParentFolder, newFolderNameInTargetPath, this)
  if (writableTargetParentFolder == null) {
    close()
    return@callbackFlow
  }

  send(SingleFolderResult.Preparing)

  val targetFolderParentName =
    (newFolderNameInTargetPath ?: name.orEmpty())
      .removeForbiddenCharsFromFilename()
      .trimFileSeparator()
  val conflictResolution =
    handleParentFolderConflict(
      context,
      targetParentFolder,
      targetFolderParentName,
      this,
      onConflict,
    )
  if (conflictResolution == SingleFolderConflictCallback.ConflictResolution.SKIP) {
    close()
    return@callbackFlow
  }

  send(SingleFolderResult.CountingFiles)

  val filesToCopy = if (skipEmptyFiles) walkFileTreeAndSkipEmptyFiles() else walkFileTree(context)
  if (filesToCopy.isEmpty()) {
    val targetFolder =
      writableTargetParentFolder.makeFolder(
        context,
        targetFolderParentName,
        conflictResolution.toCreateMode(),
      )
    if (targetFolder == null) {
      sendAndClose(SingleFolderResult.Error(FolderErrorCode.CANNOT_CREATE_FILE_IN_TARGET))
    } else {
      if (deleteSourceWhenComplete) delete()
      sendAndClose(SingleFolderResult.Completed(targetFolder, 0, 0, true))
    }
    return@callbackFlow
  }

  var totalFilesToCopy = 0
  var totalSizeToCopy = 0L
  filesToCopy.forEach {
    if (it.isFile) {
      totalFilesToCopy++
      totalSizeToCopy += it.length()
    }
  }

  if (isClosedForSend) {
    return@callbackFlow
  }

  send(SingleFolderResult.Starting(filesToCopy, totalFilesToCopy))

  if (deleteSourceWhenComplete) {
    when (
      val result =
        tryMoveFolderByRenamingPath(
          context,
          writableTargetParentFolder,
          targetFolderParentName,
          skipEmptyFiles,
          newFolderNameInTargetPath,
          conflictResolution,
        )
    ) {
      is DocumentFile -> {
        sendAndClose(SingleFolderResult.Completed(result, totalFilesToCopy, totalFilesToCopy, true))
        return@callbackFlow
      }

      is FolderErrorCode -> {
        sendAndClose(SingleFolderResult.Error(result))
        return@callbackFlow
      }
    }
  }

  if (
    !isFileSizeAllowed(
      DocumentFileCompat.getFreeSpace(context, writableTargetParentFolder.getStorageId(context)),
      totalSizeToCopy,
    )
  ) {
    sendAndClose(SingleFolderResult.Error(FolderErrorCode.NO_SPACE_LEFT_ON_TARGET_PATH))
    return@callbackFlow
  }

  val targetFolder =
    writableTargetParentFolder.makeFolder(
      context,
      targetFolderParentName,
      conflictResolution.toCreateMode(),
    )
  if (targetFolder == null) {
    sendAndClose(SingleFolderResult.Error(FolderErrorCode.CANNOT_CREATE_FILE_IN_TARGET))
    return@callbackFlow
  }

  var totalCopiedFiles = 0
  var timer: Job? = null
  var bytesMoved = 0L
  var writeSpeed = 0
  val startTimer: (Boolean) -> Unit = { start ->
    if (start && updateInterval > 0) {
      timer =
        startCoroutineTimer(repeatMillis = updateInterval) {
          trySend(
            SingleFolderResult.InProgress(
              bytesMoved * 100f / totalSizeToCopy,
              bytesMoved,
              writeSpeed,
              totalCopiedFiles,
            )
          )
          writeSpeed = 0
        }
    }
  }
  startTimer(totalSizeToCopy > 10 * FileSize.MB)

  var targetFile: DocumentFile? = null
  // This boolean flag is required to prevent the callback from called again on next FOR iteration
  // after the thread was interrupted
  var canceled = false
  val notifyCanceled: (FolderErrorCode) -> Unit = { errorCode ->
    if (!canceled) {
      canceled = true
      timer?.cancel()
      targetFile?.delete()
      trySend(
        SingleFolderResult.Error(
          errorCode,
          completedData =
            SingleFolderResult.Completed(targetFolder, totalFilesToCopy, totalCopiedFiles, false),
        )
      )
    }
  }

  val conflictedFiles = ArrayList<SingleFolderConflictCallback.FileConflict>(totalFilesToCopy)
  val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
  var success = true

  @Suppress("BlockingMethodInNonBlockingContext")
  fun copy(sourceFile: DocumentFile, destFile: DocumentFile) {
    val outputStream = destFile.openOutputStream(context)
    if (outputStream == null) {
      trySend(SingleFolderResult.Error(FolderErrorCode.CANNOT_CREATE_FILE_IN_TARGET))
      return
    }
    val inputStream = sourceFile.openInputStream(context)
    if (inputStream == null) {
      outputStream.closeStreamQuietly()
      trySend(SingleFolderResult.Error(FolderErrorCode.SOURCE_FILE_NOT_FOUND))
      return
    }
    try {
      var read = inputStream.read(buffer)
      while (read != -1) {
        outputStream.write(buffer, 0, read)
        bytesMoved += read
        writeSpeed += read
        read = inputStream.read(buffer)
      }
    } finally {
      inputStream.closeStreamQuietly()
      outputStream.closeStreamQuietly()
    }
    totalCopiedFiles++
    if (deleteSourceWhenComplete) sourceFile.delete()
  }

  val handleError: (Exception) -> Boolean = {
    val errorCode = it.toFolderCallbackErrorCode()
    if (errorCode == FolderErrorCode.CANCELED || errorCode == FolderErrorCode.UNKNOWN_IO_ERROR) {
      notifyCanceled(errorCode)
      true
    } else {
      timer?.cancel()
      trySend(SingleFolderResult.Error(errorCode))
      false
    }
  }

  val srcAbsolutePath = getAbsolutePath(context)
  for (sourceFile in filesToCopy) {
    try {
      if (isClosedForSend) {
        notifyCanceled(FolderErrorCode.CANCELED)
        return@callbackFlow
      }
      if (!sourceFile.exists()) {
        continue
      }

      val filename = sourceFile.getSubPath(context, srcAbsolutePath) ?: sourceFile.name
      if (filename.isNullOrEmpty()) continue

      if (sourceFile.isDirectory) {
        val newFolder = targetFolder.makeFolder(context, filename, CreateMode.REUSE)
        if (newFolder == null) {
          success = false
          break
        }
        continue
      }

      targetFile = targetFolder.makeFile(context, filename, sourceFile.type, CreateMode.REUSE)
      if (targetFile != null && targetFile.length() > 0) {
        conflictedFiles.add(SingleFolderConflictCallback.FileConflict(sourceFile, targetFile))
        continue
      }

      if (targetFile == null) {
        notifyCanceled(FolderErrorCode.CANNOT_CREATE_FILE_IN_TARGET)
        close()
        return@callbackFlow
      }

      copy(sourceFile, targetFile)
    } catch (e: Exception) {
      if (handleError(e)) {
        close()
        return@callbackFlow
      }
      success = false
      break
    }
  }

  val finalize: () -> Boolean = {
    timer?.cancel()
    if (!success || conflictedFiles.isEmpty()) {
      if (deleteSourceWhenComplete && success) forceDelete(context)
      trySend(
        SingleFolderResult.Completed(targetFolder, totalFilesToCopy, totalCopiedFiles, success)
      )
      true
    } else false
  }
  if (finalize()) {
    close()
    return@callbackFlow
  }

  val solutions =
    awaitUiResultWithPending(onConflict.uiScope) {
        onConflict.onContentConflict(
          targetFolder,
          conflictedFiles,
          SingleFolderConflictCallback.FolderContentConflictAction(it),
        )
      }
      .filter {
        // free up space first, by deleting some files
        if (it.solution == SingleFileConflictCallback.ConflictResolution.SKIP) {
          if (deleteSourceWhenComplete) it.source.delete()
          totalCopiedFiles++
        }
        it.solution != SingleFileConflictCallback.ConflictResolution.SKIP
      }

  val leftoverSize = totalSizeToCopy - bytesMoved
  startTimer(solutions.isNotEmpty() && leftoverSize > 10 * FileSize.MB)

  for (conflict in solutions) {
    if (isClosedForSend) {
      notifyCanceled(FolderErrorCode.CANCELED)
      return@callbackFlow
    }
    if (!conflict.source.isFile) {
      continue
    }
    val filename = conflict.target.name.orEmpty()
    targetFile =
      conflict.target
        .findParent(context)
        ?.makeFile(context, filename, mode = conflict.solution.toCreateMode())
    if (targetFile == null) {
      notifyCanceled(FolderErrorCode.CANNOT_CREATE_FILE_IN_TARGET)
      close()
      return@callbackFlow
    }

    try {
      copy(conflict.source, targetFile)
    } catch (e: Exception) {
      if (handleError(e)) {
        close()
        return@callbackFlow
      }
      success = false
      break
    }
  }

  finalize()
  close()
}

private fun Exception.toFolderCallbackErrorCode(): FolderErrorCode {
  return when (this) {
    is SecurityException -> FolderErrorCode.STORAGE_PERMISSION_DENIED
    is InterruptedIOException,
    is InterruptedException -> FolderErrorCode.CANCELED
    else -> FolderErrorCode.UNKNOWN_IO_ERROR
  }
}

private fun Exception.toMultipleFileCallbackErrorCode(): MultipleFilesErrorCode {
  return when (this) {
    is SecurityException -> MultipleFilesErrorCode.STORAGE_PERMISSION_DENIED
    is InterruptedIOException,
    is InterruptedException -> MultipleFilesErrorCode.CANCELED
    else -> MultipleFilesErrorCode.UNKNOWN_IO_ERROR
  }
}

private fun DocumentFile.doesMeetFolderCopyRequirements(
  context: Context,
  targetParentFolder: DocumentFile,
  newFolderNameInTargetPath: String?,
  scope: ProducerScope<SingleFolderResult>,
): DocumentFile? {
  scope.trySend(SingleFolderResult.Validating)

  if (!isDirectory) {
    scope.trySend(SingleFolderResult.Error(FolderErrorCode.SOURCE_FOLDER_NOT_FOUND))
    return null
  }

  if (!targetParentFolder.isDirectory) {
    scope.trySend(SingleFolderResult.Error(FolderErrorCode.INVALID_TARGET_FOLDER))
    return null
  }

  if (!canRead() || !targetParentFolder.isWritable(context)) {
    scope.trySend(SingleFolderResult.Error(FolderErrorCode.STORAGE_PERMISSION_DENIED))
    return null
  }

  if (
    targetParentFolder.getAbsolutePath(context) == parentFile?.getAbsolutePath(context) &&
      (newFolderNameInTargetPath.isNullOrEmpty() || name == newFolderNameInTargetPath)
  ) {
    scope.trySend(
      SingleFolderResult.Error(
        FolderErrorCode.TARGET_FOLDER_CANNOT_HAVE_SAME_PATH_WITH_SOURCE_FOLDER
      )
    )
    return null
  }

  val writableFolder =
    targetParentFolder.let {
      if (it.isDownloadsDocument) it.toWritableDownloadsDocumentFile(context) else it
    }
  if (writableFolder == null) {
    scope.trySend(SingleFolderResult.Error(FolderErrorCode.STORAGE_PERMISSION_DENIED))
  }
  return writableFolder
}

/** @param fileDescription Use it if you want to change file name and type in the destination. */
@WorkerThread
fun DocumentFile.copyFileTo(
  context: Context,
  targetFolder: File,
  fileDescription: FileDescription? = null,
  updateInterval: Long = 500,
  isFileSizeAllowed: CheckFileSize = defaultFileSizeChecker,
  onConflict: SingleFileConflictCallback<DocumentFile>,
): Flow<SingleFileResult> {
  return copyFileTo(
    context,
    targetFolder.absolutePath,
    fileDescription,
    updateInterval,
    isFileSizeAllowed,
    onConflict,
  )
}

/**
 * @param targetFolderAbsolutePath use [DocumentFileCompat.buildAbsolutePath] to construct the path
 * @param fileDescription Use it if you want to change file name and type in the destination.
 */
@WorkerThread
fun DocumentFile.copyFileTo(
  context: Context,
  targetFolderAbsolutePath: String,
  fileDescription: FileDescription? = null,
  updateInterval: Long = 500,
  isFileSizeAllowed: CheckFileSize = defaultFileSizeChecker,
  onConflict: SingleFileConflictCallback<DocumentFile>,
): Flow<SingleFileResult> = callbackFlow {
  val targetFolder = DocumentFileCompat.mkdirs(context, targetFolderAbsolutePath, true)
  if (targetFolder == null) {
    sendAndClose(SingleFileResult.Error(SingleFileErrorCode.CANNOT_CREATE_FILE_IN_TARGET))
  } else {
    sendAll(
      copyFileTo(
        context,
        targetFolder,
        fileDescription,
        updateInterval,
        isFileSizeAllowed,
        onConflict,
      )
    )
  }
}

/** @param fileDescription Use it if you want to change file name and type in the destination. */
@WorkerThread
fun DocumentFile.copyFileTo(
  context: Context,
  targetFolder: DocumentFile,
  fileDescription: FileDescription? = null,
  updateInterval: Long = 500,
  isFileSizeAllowed: CheckFileSize = defaultFileSizeChecker,
  onConflict: SingleFileConflictCallback<DocumentFile>,
): Flow<SingleFileResult> = callbackFlow {
  if (fileDescription?.subFolder.isNullOrEmpty()) {
    copyFileTo(
      context,
      targetFolder,
      fileDescription?.name,
      fileDescription?.mimeType,
      updateInterval,
      this,
      isFileSizeAllowed,
      onConflict,
    )
  } else {
    val targetDirectory =
      targetFolder.makeFolder(context, fileDescription?.subFolder.orEmpty(), CreateMode.REUSE)
    if (targetDirectory == null) {
      send(SingleFileResult.Error(SingleFileErrorCode.CANNOT_CREATE_FILE_IN_TARGET))
    } else {
      copyFileTo(
        context,
        targetDirectory,
        fileDescription?.name,
        fileDescription?.mimeType,
        updateInterval,
        this,
        isFileSizeAllowed,
        onConflict,
      )
    }
  }
  close()
}

private fun DocumentFile.copyFileTo(
  context: Context,
  targetFolder: DocumentFile,
  newFilenameInTargetPath: String?,
  newMimeTypeInTargetPath: String?,
  updateInterval: Long,
  scope: ProducerScope<SingleFileResult>,
  isFileSizeAllowed: CheckFileSize,
  onConflict: SingleFileConflictCallback<DocumentFile>,
) {
  val writableTargetFolder =
    doesMeetFileCopyRequirements(context, targetFolder, newFilenameInTargetPath, scope) ?: return

  scope.trySend(SingleFileResult.Preparing)

  if (
    !isFileSizeAllowed(
      DocumentFileCompat.getFreeSpace(context, writableTargetFolder.getStorageId(context)),
      length(),
    )
  ) {
    scope.trySend(SingleFileResult.Error(SingleFileErrorCode.NO_SPACE_LEFT_ON_TARGET_PATH))
    return
  }

  val cleanFileName =
    MimeType.getFullFileName(
        newFilenameInTargetPath ?: name.orEmpty(),
        newMimeTypeInTargetPath ?: mimeTypeByFileName,
      )
      .removeForbiddenCharsFromFilename()
      .trimFileSeparator()
  val fileConflictResolution =
    handleFileConflict(context, writableTargetFolder, cleanFileName, scope, onConflict)
  if (fileConflictResolution == SingleFileConflictCallback.ConflictResolution.SKIP) {
    return
  }

  try {
    val targetFile =
      createTargetFile(
        context,
        writableTargetFolder,
        cleanFileName,
        newMimeTypeInTargetPath ?: mimeTypeByFileName,
        fileConflictResolution.toCreateMode(),
        scope,
      ) ?: return
    val outputStream = targetFile.openOutputStream(context)
    if (outputStream == null) {
      scope.trySend(SingleFileResult.Error(SingleFileErrorCode.TARGET_FILE_NOT_FOUND))
      return
    }
    val inputStream = openInputStream(context)
    if (inputStream == null) {
      outputStream.closeStreamQuietly()
      scope.trySend(SingleFileResult.Error(SingleFileErrorCode.SOURCE_FILE_NOT_FOUND))
      return
    }
    copyFileStream(inputStream, outputStream, targetFile, updateInterval, false, scope)
  } catch (e: Exception) {
    scope.trySend(SingleFileResult.Error(e.toFileCallbackErrorCode()))
  }
}

/** @return writable [DocumentFile] for `targetFolder` */
private fun DocumentFile.doesMeetFileCopyRequirements(
  context: Context,
  targetFolder: DocumentFile,
  newFilenameInTargetPath: String?,
  scope: ProducerScope<SingleFileResult>,
): DocumentFile? {
  scope.trySend(SingleFileResult.Validating)

  if (!isFile) {
    scope.trySend(SingleFileResult.Error(SingleFileErrorCode.SOURCE_FILE_NOT_FOUND))
    return null
  }

  if (!targetFolder.isDirectory) {
    scope.trySend(SingleFileResult.Error(SingleFileErrorCode.TARGET_FOLDER_NOT_FOUND))
    return null
  }

  if (!canRead() || !targetFolder.isWritable(context)) {
    scope.trySend(SingleFileResult.Error(SingleFileErrorCode.STORAGE_PERMISSION_DENIED))
    return null
  }

  if (
    parentFile?.getAbsolutePath(context) == targetFolder.getAbsolutePath(context) &&
      (newFilenameInTargetPath.isNullOrEmpty() || name == newFilenameInTargetPath)
  ) {
    scope.trySend(
      SingleFileResult.Error(
        SingleFileErrorCode.TARGET_FOLDER_CANNOT_HAVE_SAME_PATH_WITH_SOURCE_FOLDER
      )
    )
    return null
  }

  val writableFolder =
    targetFolder.let {
      if (it.isDownloadsDocument) it.toWritableDownloadsDocumentFile(context) else it
    }
  if (writableFolder == null) {
    scope.trySend(SingleFileResult.Error(SingleFileErrorCode.STORAGE_PERMISSION_DENIED))
  }
  return writableFolder
}

private fun createTargetFile(
  context: Context,
  targetFolder: DocumentFile,
  newFilenameInTargetPath: String,
  mimeType: String?,
  mode: CreateMode,
  scope: ProducerScope<SingleFileResult>,
): DocumentFile? {
  val targetFile = targetFolder.makeFile(context, newFilenameInTargetPath, mimeType, mode)
  if (targetFile == null) {
    scope.trySend(SingleFileResult.Error(SingleFileErrorCode.CANNOT_CREATE_FILE_IN_TARGET))
  }
  return targetFile
}

/** @param targetFile can be [MediaFile] or [DocumentFile] */
private fun DocumentFile.copyFileStream(
  inputStream: InputStream,
  outputStream: OutputStream,
  targetFile: Any,
  updateInterval: Long,
  deleteSourceFileWhenComplete: Boolean,
  scope: ProducerScope<SingleFileResult>,
) {
  var timer: Job? = null
  try {
    var bytesMoved = 0L
    var writeSpeed = 0
    val srcSize = length()
    // using timer on small file is useless. We set minimum 10MB.
    if (updateInterval > 0 && srcSize > 10 * FileSize.MB) {
      timer =
        startCoroutineTimer(repeatMillis = updateInterval) {
          scope.trySend(
            SingleFileResult.InProgress(bytesMoved * 100f / srcSize, bytesMoved, writeSpeed)
          )
          writeSpeed = 0
        }
    }
    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
    var read = inputStream.read(buffer)
    while (read != -1) {
      outputStream.write(buffer, 0, read)
      bytesMoved += read
      writeSpeed += read
      read = inputStream.read(buffer)
    }
    timer?.cancel()
    if (deleteSourceFileWhenComplete) {
      delete()
    }
    if (targetFile is MediaFile) {
      targetFile.length = srcSize
    }
    scope.trySend(SingleFileResult.Completed(targetFile))
  } finally {
    timer?.cancel()
    inputStream.closeStreamQuietly()
    outputStream.closeStreamQuietly()
  }
}

/** @param fileDescription Use it if you want to change file name and type in the destination. */
@WorkerThread
fun DocumentFile.moveFileTo(
  context: Context,
  targetFolder: File,
  fileDescription: FileDescription? = null,
  updateInterval: Long = 500,
  isFileSizeAllowed: CheckFileSize = defaultFileSizeChecker,
  onConflict: SingleFileConflictCallback<DocumentFile>,
): Flow<SingleFileResult> {
  return moveFileTo(
    context,
    targetFolder.absolutePath,
    fileDescription,
    updateInterval,
    isFileSizeAllowed,
    onConflict,
  )
}

/**
 * @param targetFolderAbsolutePath use [DocumentFileCompat.buildAbsolutePath] to construct the path
 * @param fileDescription Use it if you want to change file name and type in the destination.
 */
@WorkerThread
fun DocumentFile.moveFileTo(
  context: Context,
  targetFolderAbsolutePath: String,
  fileDescription: FileDescription? = null,
  updateInterval: Long = 500,
  isFileSizeAllowed: CheckFileSize = defaultFileSizeChecker,
  onConflict: SingleFileConflictCallback<DocumentFile>,
): Flow<SingleFileResult> = callbackFlow {
  val targetFolder = DocumentFileCompat.mkdirs(context, targetFolderAbsolutePath, true)
  if (targetFolder == null) {
    sendAndClose(SingleFileResult.Error(SingleFileErrorCode.CANNOT_CREATE_FILE_IN_TARGET))
  } else {
    sendAll(
      moveFileTo(
        context,
        targetFolder,
        fileDescription,
        updateInterval,
        isFileSizeAllowed,
        onConflict,
      )
    )
  }
}

/** @param fileDescription Use it if you want to change file name and type in the destination. */
@WorkerThread
fun DocumentFile.moveFileTo(
  context: Context,
  targetFolder: DocumentFile,
  fileDescription: FileDescription? = null,
  updateInterval: Long = 500,
  isFileSizeAllowed: CheckFileSize = defaultFileSizeChecker,
  onConflict: SingleFileConflictCallback<DocumentFile>,
): Flow<SingleFileResult> = callbackFlow {
  if (fileDescription?.subFolder.isNullOrEmpty()) {
    moveFileTo(
      context,
      targetFolder,
      fileDescription?.name,
      fileDescription?.mimeType,
      updateInterval,
      this,
      isFileSizeAllowed,
      onConflict,
    )
  } else {
    val targetDirectory =
      targetFolder.makeFolder(context, fileDescription?.subFolder.orEmpty(), CreateMode.REUSE)
    if (targetDirectory == null) {
      send(SingleFileResult.Error(SingleFileErrorCode.CANNOT_CREATE_FILE_IN_TARGET))
    } else {
      moveFileTo(
        context,
        targetDirectory,
        fileDescription?.name,
        fileDescription?.mimeType,
        updateInterval,
        this,
        isFileSizeAllowed,
        onConflict,
      )
    }
  }
  close()
}

private fun DocumentFile.moveFileTo(
  context: Context,
  targetFolder: DocumentFile,
  newFilenameInTargetPath: String?,
  newMimeTypeInTargetPath: String?,
  updateInterval: Long,
  scope: ProducerScope<SingleFileResult>,
  isFileSizeAllowed: CheckFileSize,
  onConflict: SingleFileConflictCallback<DocumentFile>,
) {
  val writableTargetFolder =
    doesMeetFileCopyRequirements(context, targetFolder, newFilenameInTargetPath, scope) ?: return

  scope.trySend(SingleFileResult.Preparing)

  val cleanFileName =
    MimeType.getFullFileName(
        newFilenameInTargetPath ?: name.orEmpty(),
        newMimeTypeInTargetPath ?: mimeTypeByFileName,
      )
      .removeForbiddenCharsFromFilename()
      .trimFileSeparator()
  val fileConflictResolution =
    handleFileConflict(context, writableTargetFolder, cleanFileName, scope, onConflict)
  if (fileConflictResolution == SingleFileConflictCallback.ConflictResolution.SKIP) {
    return
  }

  if (inInternalStorage(context)) {
    toRawFile(context)
      ?.moveTo(
        context,
        writableTargetFolder.getAbsolutePath(context),
        cleanFileName,
        fileConflictResolution,
      )
      ?.let {
        scope.trySend(SingleFileResult.Completed(DocumentFile.fromFile(it)))
        return
      }
  }

  val targetStorageId = writableTargetFolder.getStorageId(context)
  if (isExternalStorageManager(context) && getStorageId(context) == targetStorageId) {
    val sourceFile = toRawFile(context)
    if (sourceFile == null) {
      scope.trySend(SingleFileResult.Error(SingleFileErrorCode.STORAGE_PERMISSION_DENIED))
      return
    }
    writableTargetFolder.toRawFile(context)?.let { destinationFolder ->
      sourceFile.moveTo(context, destinationFolder, cleanFileName, fileConflictResolution)?.let {
        scope.trySend(SingleFileResult.Completed(DocumentFile.fromFile(it)))
        return
      }
    }
  }

  try {
    if (
      Build.VERSION.SDK_INT >= Build.VERSION_CODES.N &&
        !isRawFile &&
        writableTargetFolder.isTreeDocumentFile &&
        getStorageId(context) == targetStorageId
    ) {
      val movedFileUri =
        parentFile?.uri?.let {
          DocumentsContract.moveDocument(context.contentResolver, uri, it, writableTargetFolder.uri)
        }
      if (movedFileUri != null) {
        val newFile = context.fromTreeUri(movedFileUri)
        if (newFile != null && newFile.isFile) {
          if (newFilenameInTargetPath != null) newFile.renameTo(cleanFileName)
          scope.trySend(SingleFileResult.Completed(newFile))
        } else {
          scope.trySend(SingleFileResult.Error(SingleFileErrorCode.TARGET_FILE_NOT_FOUND))
        }
        return
      }
    }

    if (!isFileSizeAllowed(DocumentFileCompat.getFreeSpace(context, targetStorageId), length())) {
      scope.trySend(SingleFileResult.Error(SingleFileErrorCode.NO_SPACE_LEFT_ON_TARGET_PATH))
      return
    }
  } catch (_: Throwable) {
    scope.trySend(SingleFileResult.Error(SingleFileErrorCode.STORAGE_PERMISSION_DENIED))
    return
  }

  try {
    val targetFile =
      createTargetFile(
        context,
        writableTargetFolder,
        cleanFileName,
        newMimeTypeInTargetPath ?: mimeTypeByFileName,
        fileConflictResolution.toCreateMode(),
        scope,
      ) ?: return
    val outputStream = targetFile.openOutputStream(context)
    if (outputStream == null) {
      scope.trySend(SingleFileResult.Error(SingleFileErrorCode.TARGET_FILE_NOT_FOUND))
      return
    }
    val inputStream = openInputStream(context)
    if (inputStream == null) {
      outputStream.closeStreamQuietly()
      scope.trySend(SingleFileResult.Error(SingleFileErrorCode.SOURCE_FILE_NOT_FOUND))
      return
    }
    copyFileStream(inputStream, outputStream, targetFile, updateInterval, true, scope)
  } catch (e: Exception) {
    scope.trySend(SingleFileResult.Error(e.toFileCallbackErrorCode()))
  }
}

/** @return `true` if error */
private fun DocumentFile.simpleCheckSourceFile(scope: ProducerScope<SingleFileResult>): Boolean {
  if (!isFile) {
    scope.trySend(SingleFileResult.Error(SingleFileErrorCode.SOURCE_FILE_NOT_FOUND))
    return true
  }
  if (!canRead()) {
    scope.trySend(SingleFileResult.Error(SingleFileErrorCode.STORAGE_PERMISSION_DENIED))
    return true
  }
  return false
}

private fun DocumentFile.copyFileToMedia(
  context: Context,
  fileDescription: FileDescription,
  publicDirectory: PublicDirectory,
  deleteSourceFileWhenComplete: Boolean,
  mode: CreateMode,
  updateInterval: Long,
  scope: ProducerScope<SingleFileResult>,
  isFileSizeAllowed: CheckFileSize,
  onConflict: SingleFileConflictCallback<DocumentFile>,
) {
  if (simpleCheckSourceFile(scope)) return

  val publicFolder =
    DocumentFileCompat.fromPublicFolder(context, publicDirectory, fileDescription.subFolder, true)
  if (
    Build.VERSION.SDK_INT < Build.VERSION_CODES.Q ||
      deleteSourceFileWhenComplete && !isRawFile && publicFolder?.isTreeDocumentFile == true
  ) {
    if (publicFolder == null) {
      scope.trySend(SingleFileResult.Error(SingleFileErrorCode.STORAGE_PERMISSION_DENIED))
      return
    }
    publicFolder.child(context, fileDescription.fullName)?.let {
      if (mode == CreateMode.REPLACE) {
        if (!it.forceDelete(context)) {
          scope.trySend(SingleFileResult.Error(SingleFileErrorCode.CANNOT_CREATE_FILE_IN_TARGET))
          return
        }
      } else {
        fileDescription.name = publicFolder.autoIncrementFileName(context, it.name.orEmpty())
      }
    }
    fileDescription.subFolder = ""
    if (deleteSourceFileWhenComplete) {
      moveFileTo(
        context,
        publicFolder,
        fileDescription,
        updateInterval,
        isFileSizeAllowed,
        onConflict,
      )
    } else {
      copyFileTo(
        context,
        publicFolder,
        fileDescription,
        updateInterval,
        isFileSizeAllowed,
        onConflict,
      )
    }
  } else {
    val validMode = if (mode == CreateMode.REUSE) CreateMode.CREATE_NEW else mode
    val mediaFile =
      if (publicDirectory == PublicDirectory.DOWNLOADS) {
        MediaStoreCompat.createDownload(context, fileDescription, validMode)
      } else {
        MediaStoreCompat.createImage(context, fileDescription, mode = validMode)
      }
    if (mediaFile == null) {
      scope.trySend(SingleFileResult.Error(SingleFileErrorCode.CANNOT_CREATE_FILE_IN_TARGET))
    } else {
      copyFileTo(
        context,
        mediaFile,
        deleteSourceFileWhenComplete,
        updateInterval,
        scope,
        isFileSizeAllowed,
      )
    }
  }
}

@WorkerThread
@JvmOverloads
fun DocumentFile.copyFileToDownloadMedia(
  context: Context,
  fileDescription: FileDescription,
  mode: CreateMode = CreateMode.CREATE_NEW,
  updateInterval: Long = 500,
  isFileSizeAllowed: CheckFileSize = defaultFileSizeChecker,
  onConflict: SingleFileConflictCallback<DocumentFile>,
): Flow<SingleFileResult> = callbackFlow {
  copyFileToMedia(
    context,
    fileDescription,
    PublicDirectory.DOWNLOADS,
    false,
    mode,
    updateInterval,
    this,
    isFileSizeAllowed,
    onConflict,
  )
  close()
}

@WorkerThread
@JvmOverloads
fun DocumentFile.copyFileToPictureMedia(
  context: Context,
  fileDescription: FileDescription,
  mode: CreateMode = CreateMode.CREATE_NEW,
  updateInterval: Long = 500,
  isFileSizeAllowed: CheckFileSize = defaultFileSizeChecker,
  onConflict: SingleFileConflictCallback<DocumentFile>,
): Flow<SingleFileResult> = callbackFlow {
  copyFileToMedia(
    context,
    fileDescription,
    PublicDirectory.PICTURES,
    false,
    mode,
    updateInterval,
    this,
    isFileSizeAllowed,
    onConflict,
  )
  close()
}

@WorkerThread
@JvmOverloads
fun DocumentFile.moveFileToDownloadMedia(
  context: Context,
  fileDescription: FileDescription,
  mode: CreateMode = CreateMode.CREATE_NEW,
  updateInterval: Long = 500,
  isFileSizeAllowed: CheckFileSize = defaultFileSizeChecker,
  onConflict: SingleFileConflictCallback<DocumentFile>,
): Flow<SingleFileResult> = callbackFlow {
  copyFileToMedia(
    context,
    fileDescription,
    PublicDirectory.DOWNLOADS,
    true,
    mode,
    updateInterval,
    this,
    isFileSizeAllowed,
    onConflict,
  )
  close()
}

@WorkerThread
@JvmOverloads
fun DocumentFile.moveFileToPictureMedia(
  context: Context,
  fileDescription: FileDescription,
  mode: CreateMode = CreateMode.CREATE_NEW,
  updateInterval: Long = 500,
  isFileSizeAllowed: CheckFileSize = defaultFileSizeChecker,
  onConflict: SingleFileConflictCallback<DocumentFile>,
): Flow<SingleFileResult> = callbackFlow {
  copyFileToMedia(
    context,
    fileDescription,
    PublicDirectory.PICTURES,
    true,
    mode,
    updateInterval,
    this,
    isFileSizeAllowed,
    onConflict,
  )
  close()
}

/** @param targetFile create it with [MediaStoreCompat], e.g. [MediaStoreCompat.createDownload] */
@WorkerThread
fun DocumentFile.moveFileTo(
  context: Context,
  targetFile: MediaFile,
  updateInterval: Long = 500,
  isFileSizeAllowed: CheckFileSize = defaultFileSizeChecker,
): Flow<SingleFileResult> = callbackFlow {
  copyFileTo(context, targetFile, true, updateInterval, this, isFileSizeAllowed)
  close()
}

/** @param targetFile create it with [MediaStoreCompat], e.g. [MediaStoreCompat.createDownload] */
@WorkerThread
fun DocumentFile.copyFileTo(
  context: Context,
  targetFile: MediaFile,
  updateInterval: Long = 500,
  isFileSizeAllowed: CheckFileSize = defaultFileSizeChecker,
): Flow<SingleFileResult> = callbackFlow {
  copyFileTo(context, targetFile, false, updateInterval, this, isFileSizeAllowed)
  close()
}

private fun DocumentFile.copyFileTo(
  context: Context,
  targetFile: MediaFile,
  deleteSourceFileWhenComplete: Boolean,
  updateInterval: Long,
  scope: ProducerScope<SingleFileResult>,
  isFileSizeAllowed: CheckFileSize,
) {
  if (simpleCheckSourceFile(scope)) return

  if (!isFileSizeAllowed(DocumentFileCompat.getFreeSpace(context, PRIMARY), length())) {
    scope.trySend(SingleFileResult.Error(SingleFileErrorCode.NO_SPACE_LEFT_ON_TARGET_PATH))
    return
  }

  try {
    val outputStream = targetFile.openOutputStream()
    if (outputStream == null) {
      scope.trySend(SingleFileResult.Error(SingleFileErrorCode.TARGET_FILE_NOT_FOUND))
      return
    }
    val inputStream = openInputStream(context)
    if (inputStream == null) {
      outputStream.closeStreamQuietly()
      scope.trySend(SingleFileResult.Error(SingleFileErrorCode.SOURCE_FILE_NOT_FOUND))
      return
    }
    copyFileStream(
      inputStream,
      outputStream,
      targetFile,
      updateInterval,
      deleteSourceFileWhenComplete,
      scope,
    )
  } catch (e: Exception) {
    scope.trySend(SingleFileResult.Error(e.toFileCallbackErrorCode()))
  }
}

internal fun Exception.toFileCallbackErrorCode(): SingleFileErrorCode {
  return when (this) {
    is SecurityException -> SingleFileErrorCode.STORAGE_PERMISSION_DENIED
    is InterruptedIOException,
    is InterruptedException -> SingleFileErrorCode.CANCELED
    else -> SingleFileErrorCode.UNKNOWN_IO_ERROR
  }
}

private fun handleFileConflict(
  context: Context,
  targetFolder: DocumentFile,
  targetFileName: String,
  scope: ProducerScope<SingleFileResult>,
  onConflict: SingleFileConflictCallback<DocumentFile>,
): SingleFileConflictCallback.ConflictResolution {
  targetFolder.child(context, targetFileName)?.let { targetFile ->
    val resolution =
      awaitUiResultWithPending(onConflict.uiScope) {
        onConflict.onFileConflict(targetFile, SingleFileConflictCallback.FileConflictAction(it))
      }
    if (resolution == SingleFileConflictCallback.ConflictResolution.REPLACE) {
      scope.trySend(SingleFileResult.DeletingConflictedFile)
      if (!targetFile.forceDelete(context)) {
        scope.trySend(SingleFileResult.Error(SingleFileErrorCode.CANNOT_CREATE_FILE_IN_TARGET))
        return SingleFileConflictCallback.ConflictResolution.SKIP
      }
    }
    return resolution
  }
  return SingleFileConflictCallback.ConflictResolution.CREATE_NEW
}

private fun handleParentFolderConflict(
  context: Context,
  targetParentFolder: DocumentFile,
  targetFolderParentName: String,
  scope: ProducerScope<SingleFolderResult>,
  onConflict: SingleFolderConflictCallback,
): SingleFolderConflictCallback.ConflictResolution {
  targetParentFolder.child(context, targetFolderParentName)?.let { targetFolder ->
    val canMerge = targetFolder.isDirectory
    if (canMerge && targetFolder.isEmpty(context)) {
      return SingleFolderConflictCallback.ConflictResolution.MERGE
    }

    val resolution =
      awaitUiResultWithPending(onConflict.uiScope) {
        onConflict.onParentConflict(
          targetFolder,
          SingleFolderConflictCallback.ParentFolderConflictAction(it),
          canMerge,
        )
      }

    when (resolution) {
      SingleFolderConflictCallback.ConflictResolution.REPLACE -> {
        scope.trySend(SingleFolderResult.DeletingConflictedFiles)
        val isFolder = targetFolder.isDirectory
        if (targetFolder.forceDelete(context, true)) {
          if (!isFolder) {
            val newFolder = targetFolder.parentFile?.createDirectory(targetFolderParentName)
            if (newFolder == null) {
              scope.trySend(SingleFolderResult.Error(FolderErrorCode.CANNOT_CREATE_FILE_IN_TARGET))
              return SingleFolderConflictCallback.ConflictResolution.SKIP
            }
          }
        } else {
          scope.trySend(SingleFolderResult.Error(FolderErrorCode.CANNOT_CREATE_FILE_IN_TARGET))
          return SingleFolderConflictCallback.ConflictResolution.SKIP
        }
      }

      SingleFolderConflictCallback.ConflictResolution.MERGE -> {
        if (targetFolder.isFile) {
          if (targetFolder.delete()) {
            val newFolder = targetFolder.parentFile?.createDirectory(targetFolderParentName)
            if (newFolder == null) {
              scope.trySend(SingleFolderResult.Error(FolderErrorCode.CANNOT_CREATE_FILE_IN_TARGET))
              return SingleFolderConflictCallback.ConflictResolution.SKIP
            }
          } else {
            scope.trySend(SingleFolderResult.Error(FolderErrorCode.CANNOT_CREATE_FILE_IN_TARGET))
            return SingleFolderConflictCallback.ConflictResolution.SKIP
          }
        }
      }

      else -> {
        // skip
      }
    }
    return resolution
  }
  return SingleFolderConflictCallback.ConflictResolution.CREATE_NEW
}

private fun List<DocumentFile>.handleParentFolderConflict(
  context: Context,
  targetParentFolder: DocumentFile,
  scope: ProducerScope<MultipleFilesResult>,
  onConflict: MultipleFilesConflictCallback,
): List<MultipleFilesConflictCallback.ParentConflict>? {
  val sourceFileNames = map { it.name }
  val conflictedFiles = targetParentFolder.children.filter { it.name in sourceFileNames }
  val conflicts =
    conflictedFiles.map {
      val sourceFile = first { src -> src.name == it.name }
      val canMerge = sourceFile.isDirectory && it.isDirectory
      val solution =
        if (canMerge && it.isEmpty(context)) SingleFolderConflictCallback.ConflictResolution.MERGE
        else SingleFolderConflictCallback.ConflictResolution.CREATE_NEW
      MultipleFilesConflictCallback.ParentConflict(sourceFile, it, canMerge, solution)
    }
  val unresolvedConflicts =
    conflicts
      .filter { it.solution != SingleFolderConflictCallback.ConflictResolution.MERGE }
      .toMutableList()
  if (unresolvedConflicts.isNotEmpty()) {
    val unresolvedFiles = unresolvedConflicts.filter { it.source.isFile }.toMutableList()
    val unresolvedFolders = unresolvedConflicts.filter { it.source.isDirectory }.toMutableList()
    val resolution =
      awaitUiResultWithPending(onConflict.uiScope) {
        onConflict.onParentConflict(
          targetParentFolder,
          unresolvedFolders,
          unresolvedFiles,
          MultipleFilesConflictCallback.ParentFolderConflictAction(it),
        )
      }
    if (resolution.any { it.solution == SingleFolderConflictCallback.ConflictResolution.REPLACE }) {
      scope.trySend(MultipleFilesResult.DeletingConflictedFiles)
    }
    resolution.forEach { conflict ->
      when (conflict.solution) {
        SingleFolderConflictCallback.ConflictResolution.REPLACE -> {
          if (!conflict.target.let { it.forceDelete(context, true) || !it.exists() }) {
            scope.trySend(
              MultipleFilesResult.Error(MultipleFilesErrorCode.CANNOT_CREATE_FILE_IN_TARGET)
            )
            return null
          }
        }

        SingleFolderConflictCallback.ConflictResolution.MERGE -> {
          if (conflict.target.isFile && !conflict.target.delete()) {
            scope.trySend(
              MultipleFilesResult.Error(MultipleFilesErrorCode.CANNOT_CREATE_FILE_IN_TARGET)
            )
            return null
          }
        }

        else -> {
          // skip
        }
      }
    }
    return resolution.toMutableList().apply {
      addAll(
        conflicts.filter { it.solution == SingleFolderConflictCallback.ConflictResolution.MERGE }
      )
    }
  }
  return emptyList()
}
