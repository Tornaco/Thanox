package com.anggrayudi.storage

import android.content.Context
import android.net.Uri
import androidx.annotation.WorkerThread
import androidx.documentfile.provider.DocumentFile
import com.anggrayudi.storage.file.*
import com.anggrayudi.storage.media.MediaFile
import java.io.InputStream
import java.io.OutputStream

/**
 * [DocumentFile] is developed by Google and is impossible to share the same interface with
 * [MediaFile] that is developed by me, because they have different behavior and capabilities. So we
 * need to create an abstraction by checking the class type. They both may share the same functions,
 * e.g. delete, get file name, open stream, etc.
 *
 * @author Anggrayudi Hardiannico A. (anggrayudi.hardiannico@dana.id)
 * @version FileWrapper, 09/07/22 18.41
 */
interface FileWrapper {

  val uri: Uri

  val name: String?

  val baseName: String

  val extension: String

  val mimeType: String?

  fun isEmpty(context: Context): Boolean

  fun getAbsolutePath(context: Context): String

  fun getBasePath(context: Context): String

  fun getRelativePath(context: Context): String

  @WorkerThread fun openOutputStream(context: Context, append: Boolean = true): OutputStream?

  @WorkerThread fun openInputStream(context: Context): InputStream?

  fun delete(): Boolean

  class Media(val mediaFile: MediaFile) : FileWrapper {

    override val uri: Uri
      get() = mediaFile.uri

    override val name: String?
      get() = mediaFile.name

    override val baseName: String
      get() = mediaFile.baseName

    override val extension: String
      get() = mediaFile.extension

    override val mimeType: String
      get() = mediaFile.mimeType

    override fun isEmpty(context: Context): Boolean = mediaFile.isEmpty

    override fun getAbsolutePath(context: Context): String = mediaFile.absolutePath

    override fun getBasePath(context: Context): String = mediaFile.basePath

    override fun getRelativePath(context: Context): String = mediaFile.relativePath

    override fun openOutputStream(context: Context, append: Boolean): OutputStream? =
      mediaFile.openOutputStream(append)

    override fun openInputStream(context: Context): InputStream? = mediaFile.openInputStream()

    override fun delete(): Boolean = mediaFile.delete()
  }

  class Document(val documentFile: DocumentFile) : FileWrapper {

    override val uri: Uri
      get() = documentFile.uri

    override val name: String?
      get() = documentFile.name

    override val baseName: String
      get() = documentFile.baseName

    override val extension: String
      get() = documentFile.extension

    override val mimeType: String?
      get() = documentFile.mimeTypeByFileName

    override fun isEmpty(context: Context): Boolean = documentFile.isEmpty(context)

    override fun getAbsolutePath(context: Context): String = documentFile.getAbsolutePath(context)

    override fun getBasePath(context: Context): String = documentFile.getBasePath(context)

    override fun getRelativePath(context: Context): String = documentFile.getRelativePath(context)

    override fun openOutputStream(context: Context, append: Boolean): OutputStream? =
      documentFile.openOutputStream(context, append)

    override fun openInputStream(context: Context): InputStream? =
      documentFile.openInputStream(context)

    override fun delete(): Boolean = documentFile.delete()
  }
}
