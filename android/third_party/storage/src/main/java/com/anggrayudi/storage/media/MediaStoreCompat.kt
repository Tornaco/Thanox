package com.anggrayudi.storage.media

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.os.Build
import android.os.Environment
import android.provider.BaseColumns
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.annotation.RequiresApi
import androidx.documentfile.provider.DocumentFile
import com.anggrayudi.storage.extension.getString
import com.anggrayudi.storage.extension.trimFileName
import com.anggrayudi.storage.extension.trimFileSeparator
import com.anggrayudi.storage.file.CreateMode
import com.anggrayudi.storage.file.DocumentFileCompat
import com.anggrayudi.storage.file.DocumentFileCompat.removeForbiddenCharsFromFilename
import com.anggrayudi.storage.file.DocumentFileType
import com.anggrayudi.storage.file.MimeType
import com.anggrayudi.storage.file.PublicDirectory
import com.anggrayudi.storage.file.autoIncrementFileName
import com.anggrayudi.storage.file.canModify
import com.anggrayudi.storage.file.child
import com.anggrayudi.storage.file.createNewFileIfPossible
import com.anggrayudi.storage.file.recreateFile
import com.anggrayudi.storage.file.search
import java.io.File
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

/**
 * Using Media Store doesn't require any storage permission for API 29+. Bear in mind that this is a
 * method to create files with "write and forget" strategy, which means you have no intention to
 * rename or search the file in the future.
 */
object MediaStoreCompat {

  @JvmStatic
  val volumeName: String
    @SuppressLint("InlinedApi")
    get() =
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) MediaStore.VOLUME_EXTERNAL
      else MediaStore.VOLUME_EXTERNAL_PRIMARY

  @JvmStatic
  @JvmOverloads
  fun createDownload(
    context: Context,
    file: FileDescription,
    mode: CreateMode = CreateMode.CREATE_NEW,
  ): MediaFile? {
    return createMedia(context, MediaType.DOWNLOADS, Environment.DIRECTORY_DOWNLOADS, file, mode)
  }

  @JvmOverloads
  @JvmStatic
  fun createImage(
    context: Context,
    file: FileDescription,
    relativeParentDirectory: ImageMediaDirectory = ImageMediaDirectory.PICTURES,
    mode: CreateMode = CreateMode.CREATE_NEW,
  ): MediaFile? {
    return createMedia(context, MediaType.IMAGE, relativeParentDirectory.folderName, file, mode)
  }

  @JvmOverloads
  @JvmStatic
  fun createAudio(
    context: Context,
    file: FileDescription,
    relativeParentDirectory: AudioMediaDirectory = AudioMediaDirectory.MUSIC,
    mode: CreateMode = CreateMode.CREATE_NEW,
  ): MediaFile? {
    return createMedia(context, MediaType.AUDIO, relativeParentDirectory.folderName, file, mode)
  }

  @JvmOverloads
  @JvmStatic
  fun createVideo(
    context: Context,
    file: FileDescription,
    relativeParentDirectory: VideoMediaDirectory = VideoMediaDirectory.MOVIES,
    mode: CreateMode = CreateMode.CREATE_NEW,
  ): MediaFile? {
    return createMedia(context, MediaType.VIDEO, relativeParentDirectory.folderName, file, mode)
  }

  @JvmStatic
  @JvmOverloads
  fun createMedia(
    context: Context,
    fullPath: String,
    file: FileDescription,
    mode: CreateMode = CreateMode.CREATE_NEW,
  ): MediaFile? {
    val basePath = DocumentFileCompat.getBasePath(context, fullPath).trimFileSeparator()
    if (basePath.isEmpty()) {
      return null
    }
    val mediaFolder = basePath.substringBefore('/')
    val mediaType =
      when (mediaFolder) {
        Environment.DIRECTORY_DOWNLOADS -> MediaType.DOWNLOADS
        in ImageMediaDirectory.entries.map { it.folderName } -> MediaType.IMAGE
        in AudioMediaDirectory.entries.map { it.folderName } -> MediaType.AUDIO
        in VideoMediaDirectory.entries.map { it.folderName } -> MediaType.VIDEO
        else -> return null
      }
    val subFolder = basePath.substringAfter('/', "")
    file.subFolder = "$subFolder/${file.subFolder}".trimFileSeparator()
    return createMedia(context, mediaType, mediaFolder, file, mode)
  }

  private fun createMedia(
    context: Context,
    mediaType: MediaType,
    folderName: String,
    file: FileDescription,
    mode: CreateMode,
  ): MediaFile? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      val fullName = file.fullName
      val mimeType = file.mimeType
      val baseName = MimeType.getBaseFileName(fullName)
      val ext = MimeType.getExtensionFromFileName(fullName)
      // Check if the OS recognizes the mime type, otherwise the name will be like "test-8.PNG.png"
      val displayName =
        if (MimeTypeMap.getSingleton().getExtensionFromMimeType(file.mimeType) != null) {
          baseName
        } else {
          fullName
        }
      val contentValues =
        ContentValues().apply {
          put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
          put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
          val dateCreated = System.currentTimeMillis()
          put(MediaStore.MediaColumns.DATE_ADDED, dateCreated)
          put(MediaStore.MediaColumns.DATE_MODIFIED, dateCreated)
        }
      val relativePath = "$folderName/${file.subFolder}".trimFileName()
      contentValues.apply {
        put(MediaStore.MediaColumns.OWNER_PACKAGE_NAME, context.packageName)
        if (relativePath.isNotBlank()) {
          put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
        }
      }
      val existingMedia = fromBasePath(context, mediaType, "$relativePath/$fullName")
      when {
        existingMedia?.isEmpty == true -> existingMedia
        existingMedia != null -> {
          if (mode == CreateMode.REUSE) {
            return existingMedia
          }
          if (mode == CreateMode.REPLACE) {
            existingMedia.delete()
            return tryInsertMediaFile(context, mediaType, contentValues)
          }

          if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            // Android R+ already has this check, thus no need to check empty media files for reuse
            val prefix = "$baseName ("
            fromFileNameContains(context, mediaType, baseName)
              .asSequence()
              .filter {
                relativePath.isBlank() || relativePath == it.relativePath.removeSuffix("/")
              }
              .filter {
                val name = it.name
                if (name.isNullOrEmpty() || MimeType.getExtensionFromFileName(name) != ext) false
                else {
                  name.startsWith(prefix) &&
                    (DocumentFileCompat.FILE_NAME_DUPLICATION_REGEX_WITH_EXTENSION.matches(name) ||
                      DocumentFileCompat.FILE_NAME_DUPLICATION_REGEX_WITHOUT_EXTENSION.matches(
                        name
                      ))
                }
              }
              // Use existing empty media file
              .firstOrNull { it.hasZeroLength }
              ?.let {
                return it
              }
          }

          tryInsertMediaFile(context, mediaType, contentValues)
        }

        else -> tryInsertMediaFile(context, mediaType, contentValues)
      }
    } else {
      val publicDirectory = Environment.getExternalStoragePublicDirectory(folderName)
      if (publicDirectory.canModify(context)) {
        val filename = file.fullName
        var media = File("$publicDirectory/${file.subFolder}", filename)
        val parentFile = media.parentFile ?: return null
        parentFile.mkdirs()
        if (media.exists() && mode == CreateMode.CREATE_NEW) {
          media = parentFile.child(parentFile.autoIncrementFileName(filename))
        }
        if (mode == CreateMode.REPLACE && !media.recreateFile()) {
          return null
        }
        if (media.createNewFileIfPossible()) {
          if (media.canRead()) MediaFile(context, media) else null
        } else null
      } else {
        null
      }
    }
  }

  private fun tryInsertMediaFile(
    context: Context,
    mediaType: MediaType,
    contentValues: ContentValues,
  ): MediaFile? {
    return try {
      MediaFile(
        context,
        context.contentResolver.insert(mediaType.writeUri!!, contentValues) ?: return null,
      )
    } catch (e: Exception) {
      e.printStackTrace()
      null
    }
  }

  /**
   * This action only deletes your app's created files.
   *
   * @see MediaFile.owner
   */
  @JvmStatic
  fun deleteEmptyMediaFiles(context: Context, mediaType: MediaType): Int {
    var deleted = 0
    fromMediaType(context, mediaType).forEach {
      if (it.hasZeroLength) {
        it.delete()
        deleted++
      }
    }
    return deleted
  }

  @JvmStatic
  fun fromMediaId(context: Context, mediaType: MediaType, id: String): MediaFile? {
    return mediaType.writeUri?.let { MediaFile(context, it.buildUpon().appendPath(id).build()) }
  }

  @JvmStatic
  fun fromMediaId(context: Context, mediaType: MediaType, id: Long): MediaFile? {
    return fromMediaId(context, mediaType, id.toString())
  }

  @JvmStatic
  fun fromFileName(context: Context, mediaType: MediaType, name: String): MediaFile? {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
      File(PublicDirectory.DOWNLOADS.file, name).let {
        if (it.isFile && it.canRead()) MediaFile(context, it) else null
      }
    } else {
      val selection = "${MediaStore.MediaColumns.DISPLAY_NAME} = ?"
      context.contentResolver
        .query(
          mediaType.readUri ?: return null,
          arrayOf(BaseColumns._ID),
          selection,
          arrayOf(name),
          null,
        )
        ?.use { fromCursorToMediaFile(context, mediaType, it) }
    }
  }

  /**
   * @param basePath is relative path + filename
   * @return `null` if base path does not contain relative path or the media is not found
   */
  @JvmStatic
  fun fromBasePath(context: Context, mediaType: MediaType, basePath: String): MediaFile? {
    val cleanBasePath = basePath.removeForbiddenCharsFromFilename().trimFileSeparator()
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
      File(Environment.getExternalStorageDirectory(), cleanBasePath).let {
        if (it.isFile && it.canRead()) MediaFile(context, it) else null
      }
    } else {
      val relativePath = cleanBasePath.substringBeforeLast('/', "")
      if (relativePath.isEmpty()) {
        return null
      }
      val filename = cleanBasePath.substringAfterLast('/')
      val selection =
        "${MediaStore.MediaColumns.DISPLAY_NAME} = ? AND ${MediaStore.MediaColumns.RELATIVE_PATH} = ?"
      context.contentResolver
        .query(
          mediaType.readUri ?: return null,
          arrayOf(BaseColumns._ID),
          selection,
          arrayOf(filename, "$relativePath/"),
          null,
        )
        ?.use { fromCursorToMediaFile(context, mediaType, it) }
    }
  }

  @RequiresApi(Build.VERSION_CODES.Q)
  private fun mediaTypeFromRelativePath(cleanRelativePath: String) =
    when (cleanRelativePath) {
      Environment.DIRECTORY_DCIM,
      Environment.DIRECTORY_PICTURES -> MediaType.IMAGE
      Environment.DIRECTORY_MOVIES,
      Environment.DIRECTORY_DCIM -> MediaType.VIDEO
      Environment.DIRECTORY_MUSIC,
      Environment.DIRECTORY_PODCASTS,
      Environment.DIRECTORY_RINGTONES,
      Environment.DIRECTORY_ALARMS,
      Environment.DIRECTORY_NOTIFICATIONS -> MediaType.AUDIO

      Environment.DIRECTORY_DOWNLOADS -> MediaType.DOWNLOADS
      else -> null
    }

  /** @see MediaStore.MediaColumns.RELATIVE_PATH */
  @JvmStatic
  fun fromRelativePath(context: Context, publicDirectory: PublicDirectory) =
    fromRelativePath(context, publicDirectory.folderName)

  /** @see MediaStore.MediaColumns.RELATIVE_PATH */
  @JvmStatic
  fun fromRelativePath(context: Context, relativePath: String): List<MediaFile> = runBlocking {
    val cleanRelativePath = relativePath.trimFileSeparator()
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
      DocumentFile.fromFile(File(Environment.getExternalStorageDirectory(), cleanRelativePath))
        .search(true, DocumentFileType.FILE)
        .first()
        .map { MediaFile(context, File(it.uri.path!!)) }
    } else {
      val mediaType = mediaTypeFromRelativePath(cleanRelativePath) ?: return@runBlocking emptyList()
      val relativePathWithSlashSuffix = relativePath.trimEnd('/') + '/'
      val selection = "${MediaStore.MediaColumns.RELATIVE_PATH} IN(?, ?)"
      val selectionArgs = arrayOf(relativePathWithSlashSuffix, cleanRelativePath)
      context.contentResolver
        .query(
          mediaType.readUri ?: return@runBlocking emptyList(),
          arrayOf(BaseColumns._ID),
          selection,
          selectionArgs,
          null,
        )
        ?.use { fromCursorToMediaFiles(context, mediaType, it) }
        .orEmpty()
    }
  }

  /** @see MediaStore.MediaColumns.RELATIVE_PATH */
  @JvmStatic
  fun fromRelativePath(context: Context, relativePath: String, name: String): MediaFile? =
    runBlocking {
      val cleanRelativePath = relativePath.trimFileSeparator()
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        DocumentFile.fromFile(File(Environment.getExternalStorageDirectory(), cleanRelativePath))
          .search(true, DocumentFileType.FILE, name = name)
          .first()
          .map { MediaFile(context, File(it.uri.path!!)) }
          .firstOrNull()
      } else {
        val mediaType = mediaTypeFromRelativePath(cleanRelativePath) ?: return@runBlocking null
        val relativePathWithSlashSuffix = relativePath.trimEnd('/') + '/'
        val selection =
          "${MediaStore.MediaColumns.DISPLAY_NAME} = ? AND ${MediaStore.MediaColumns.RELATIVE_PATH} IN(?, ?)"
        val selectionArgs = arrayOf(name, relativePathWithSlashSuffix, cleanRelativePath)
        context.contentResolver
          .query(
            mediaType.readUri ?: return@runBlocking null,
            arrayOf(BaseColumns._ID),
            selection,
            selectionArgs,
            null,
          )
          ?.use { fromCursorToMediaFile(context, mediaType, it) }
      }
    }

  @JvmStatic
  fun fromFileNameContains(
    context: Context,
    mediaType: MediaType,
    containsName: String,
  ): List<MediaFile> = runBlocking {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
      mediaType.directories
        .map { directory ->
          DocumentFile.fromFile(directory)
            .search(
              true,
              regex = Regex("^.*$containsName.*\$"),
              mimeTypes = arrayOf(mediaType.mimeType),
            )
            .first()
            .map { MediaFile(context, File(it.uri.path!!)) }
        }
        .flatten()
    } else {
      val selection = "${MediaStore.MediaColumns.DISPLAY_NAME} LIKE '%$containsName%'"
      context.contentResolver
        .query(
          mediaType.readUri ?: return@runBlocking emptyList(),
          arrayOf(BaseColumns._ID),
          selection,
          null,
          null,
        )
        ?.use { fromCursorToMediaFiles(context, mediaType, it) }
        .orEmpty()
    }
  }

  @JvmStatic
  fun fromMimeType(context: Context, mediaType: MediaType, mimeType: String): List<MediaFile> =
    runBlocking {
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        mediaType.directories
          .map { directory ->
            DocumentFile.fromFile(directory)
              .search(true, DocumentFileType.FILE, arrayOf(mimeType))
              .first()
              .map { MediaFile(context, File(it.uri.path!!)) }
          }
          .flatten()
      } else {
        val selection = "${MediaStore.MediaColumns.MIME_TYPE} = ?"
        context.contentResolver
          .query(
            mediaType.readUri ?: return@runBlocking emptyList(),
            arrayOf(BaseColumns._ID),
            selection,
            arrayOf(mimeType),
            null,
          )
          ?.use { fromCursorToMediaFiles(context, mediaType, it) }
          .orEmpty()
      }
    }

  @JvmStatic
  fun fromMediaType(context: Context, mediaType: MediaType): List<MediaFile> = runBlocking {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
      mediaType.directories
        .map { directory ->
          DocumentFile.fromFile(directory)
            .search(true, mimeTypes = arrayOf(mediaType.mimeType))
            .first()
            .map { MediaFile(context, File(it.uri.path!!)) }
        }
        .flatten()
    } else {
      context.contentResolver
        .query(
          mediaType.readUri ?: return@runBlocking emptyList(),
          arrayOf(BaseColumns._ID),
          null,
          null,
          null,
        )
        ?.use { fromCursorToMediaFiles(context, mediaType, it) }
        .orEmpty()
    }
  }

  private fun fromCursorToMediaFiles(
    context: Context,
    mediaType: MediaType,
    cursor: Cursor,
  ): List<MediaFile> {
    if (cursor.moveToFirst()) {
      val mediaFiles = ArrayList<MediaFile>(cursor.count)
      do {
        cursor
          .getString(BaseColumns._ID)
          ?.let { fromMediaId(context, mediaType, it) }
          ?.let { mediaFiles.add(it) }
      } while (cursor.moveToNext())
      return mediaFiles
    }
    return emptyList()
  }

  private fun fromCursorToMediaFile(
    context: Context,
    mediaType: MediaType,
    cursor: Cursor,
  ): MediaFile? {
    return if (cursor.moveToFirst()) {
      cursor.getString(BaseColumns._ID)?.let { fromMediaId(context, mediaType, it) }
    } else null
  }
}
