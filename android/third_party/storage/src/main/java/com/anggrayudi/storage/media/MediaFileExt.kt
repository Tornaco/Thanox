@file:JvmName("MediaFileUtils")

package com.anggrayudi.storage.media

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.documentfile.provider.DocumentFile
import com.anggrayudi.storage.extension.closeEntryQuietly
import com.anggrayudi.storage.extension.closeStreamQuietly
import com.anggrayudi.storage.extension.sendAndClose
import com.anggrayudi.storage.extension.startCoroutineTimer
import com.anggrayudi.storage.file.CreateMode
import com.anggrayudi.storage.file.MimeType
import com.anggrayudi.storage.file.findParent
import com.anggrayudi.storage.file.fullName
import com.anggrayudi.storage.file.isWritable
import com.anggrayudi.storage.file.makeFile
import com.anggrayudi.storage.file.makeFolder
import com.anggrayudi.storage.file.openOutputStream
import com.anggrayudi.storage.result.ZipCompressionErrorCode
import com.anggrayudi.storage.result.ZipCompressionResult
import com.anggrayudi.storage.result.ZipDecompressionErrorCode
import com.anggrayudi.storage.result.ZipDecompressionResult
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InterruptedIOException
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Created on 21/01/22
 *
 * @author Anggrayudi H
 */
@WorkerThread
fun List<MediaFile>.compressToZip(
  context: Context,
  targetZipFile: DocumentFile,
  deleteSourceWhenComplete: Boolean = false,
  updateInterval: Long = 500,
): Flow<ZipCompressionResult> = callbackFlow {
  send(ZipCompressionResult.CountingFiles)
  val entryFiles = distinctBy { it.uri }.filter { !it.isEmpty }
  if (entryFiles.isEmpty()) {
    sendAndClose(
      ZipCompressionResult.Error(ZipCompressionErrorCode.MISSING_ENTRY_FILE, "No entry files found")
    )
    return@callbackFlow
  }

  var zipFile: DocumentFile? = targetZipFile
  if (!targetZipFile.exists() || targetZipFile.isDirectory) {
    zipFile =
      targetZipFile.findParent(context)?.makeFile(context, targetZipFile.fullName, MimeType.ZIP)
  }
  if (zipFile == null) {
    sendAndClose(
      ZipCompressionResult.Error(
        ZipCompressionErrorCode.CANNOT_CREATE_FILE_IN_TARGET,
        "Cannot create ZIP file in target",
      )
    )
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
    if (updateInterval > 0) {
      timer =
        startCoroutineTimer(repeatMillis = updateInterval) {
          trySend(
            ZipCompressionResult.Compressing(0f, bytesCompressed, writeSpeed, fileCompressedCount)
          )
          writeSpeed = 0
        }
    }
    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
    entryFiles.forEach { entry ->
      entry.openInputStream()?.use { input ->
        zos.putNextEntry(ZipEntry(entry.fullName))
        var bytes = input.read(buffer)
        while (bytes >= 0) {
          zos.write(buffer, 0, bytes)
          bytesCompressed += bytes
          writeSpeed += bytes
          bytes = input.read(buffer)
        }
      } ?: throw FileNotFoundException("File ${entry.fullName} is not found")
      fileCompressedCount++
    }
    success = true
  } catch (_: InterruptedIOException) {
    send(ZipCompressionResult.Error(ZipCompressionErrorCode.CANCELED, "Compression canceled"))
  } catch (e: FileNotFoundException) {
    send(ZipCompressionResult.Error(ZipCompressionErrorCode.MISSING_ENTRY_FILE, e.message))
  } catch (e: IOException) {
    if (e.message?.contains("no space", true) == true) {
      send(
        ZipCompressionResult.Error(ZipCompressionErrorCode.NO_SPACE_LEFT_ON_TARGET_PATH, e.message)
      )
    } else {
      send(ZipCompressionResult.Error(ZipCompressionErrorCode.UNKNOWN_IO_ERROR, e.message))
    }
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
      forEach { it.delete() }
    }
    val sizeReduction = (bytesCompressed - zipFile.length()).toFloat() / bytesCompressed * 100
    send(ZipCompressionResult.Completed(zipFile, bytesCompressed, entryFiles.size, sizeReduction))
  } else {
    zipFile.delete()
  }
  close()
}

@WorkerThread
fun MediaFile.decompressZip(
  context: Context,
  targetFolder: DocumentFile,
  updateInterval: Long = 500,
): Flow<ZipDecompressionResult> = callbackFlow {
  send(ZipDecompressionResult.Validating)
  if (isEmpty) {
    sendAndClose(
      ZipDecompressionResult.Error(ZipDecompressionErrorCode.MISSING_ZIP_FILE, "No zip file found")
    )
    return@callbackFlow
  }
  if (mimeType != MimeType.ZIP) {
    sendAndClose(
      ZipDecompressionResult.Error(ZipDecompressionErrorCode.NOT_A_ZIP_FILE, "Not a ZIP file")
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

  var success = false
  var bytesDecompressed = 0L
  var skippedDecompressedBytes = 0L
  var fileDecompressedCount = 0
  var timer: Job? = null
  var zis: ZipInputStream? = null
  var targetFile: DocumentFile? = null
  try {
    zis = ZipInputStream(openInputStream())
    var writeSpeed = 0
    if (updateInterval > 0) {
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
        targetFile = folder.makeFile(context, fileName)
        if (targetFile == null) {
          send(
            ZipDecompressionResult.Error(
              ZipDecompressionErrorCode.CANNOT_CREATE_FILE_IN_TARGET,
              "Cannot create file in target",
            )
          )
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
    send(ZipDecompressionResult.Error(ZipDecompressionErrorCode.CANCELED, "Decompression canceled"))
  } catch (e: FileNotFoundException) {
    send(ZipDecompressionResult.Error(ZipDecompressionErrorCode.MISSING_ZIP_FILE, e.message))
  } catch (e: IOException) {
    if (e.message?.contains("no space", true) == true) {
      send(
        ZipDecompressionResult.Error(
          ZipDecompressionErrorCode.NO_SPACE_LEFT_ON_TARGET_PATH,
          e.message,
        )
      )
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
    send(
      ZipDecompressionResult.Completed(
        this,
        destFolder,
        bytesDecompressed,
        skippedDecompressedBytes,
        fileDecompressedCount,
        0f,
      )
    )
  } else {
    targetFile?.delete()
  }
  close()
}
