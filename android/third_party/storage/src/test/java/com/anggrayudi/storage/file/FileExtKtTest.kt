package com.anggrayudi.storage.file

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito.whenNew
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

/**
 * Created on 12/16/20
 *
 * @author Anggrayudi H
 */
@RunWith(PowerMockRunner::class)
@PrepareForTest(File::class, FileExtKtTest::class)
class FileExtKtTest {

  @Ignore("JDK 17 cannot mock File object due to protected clone() method")
  @Test
  fun autoIncrementFileName() {
    val videoPath = "/stroage/emulated/0/Video"

    val existingFiles =
      arrayOf(
          "Action",
          "Horror",
          "Horror (1)",
          "Bunny.mp4",
          "Bunny (3).mp4",
          "MyMovie.mp4",
          "Papa Nakal.mp4",
          "Collections (24).gz",
          "Book",
          "Book (10)",
        )
        .map {
          val file = mock<File>()
          whenever(file.name).thenReturn(it)
          whenever(file.exists()).thenReturn(true)
          whenever(file.absolutePath).thenReturn("$videoPath/$it")
          whenNew(File::class.java).withArguments(file.absolutePath).thenReturn(file)
          whenNew(File::class.java).withArguments(videoPath, it).thenReturn(file)
          file
        }

    val videoFolder = mock<File>()
    whenever(videoFolder.absolutePath).thenReturn(videoPath)
    val fileList = existingFiles.map { it.name }.toTypedArray()
    whenever(videoFolder.list()).thenReturn(fileList)

    val filesToCreate =
      listOf(
          "Sci-Fi",
          "Horror",
          "Bunny.mp4",
          "Bunny (2).mp4",
          "Collections (24).gz",
          "Collections (28).gz",
          "Book",
        )
        .map {
          if (!existingFiles.any { file -> file.absolutePath == "$videoPath/$it" }) {
            val file = mock<File>()
            whenever(file.exists()).thenReturn(false)
            whenever(file.absolutePath).thenReturn("$videoPath/$it")
            whenNew(File::class.java).withArguments(videoPath, it).thenReturn(file)
          }
          videoFolder.autoIncrementFileName(it)
        }

    val expected =
      listOf(
        "Sci-Fi",
        "Horror (2)",
        "Bunny (4).mp4",
        "Bunny (2).mp4",
        "Collections (24) (1).gz",
        "Collections (28).gz",
        "Book (11)",
      )

    assertEquals(expected, filesToCreate)
  }

  /**
   * Mock constructor (whenNew) does not if the constructor `File()` is called from another class.
   * So we copy the code from `FileExt.autoIncrementFileName`
   */
  private fun File.autoIncrementFileName(filename: String): String {
    return if (File(absolutePath, filename).exists()) {
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
          .maxOfOrNull {
            it.substringAfterLast('(', "").substringBefore(')', "").toIntOrNull() ?: 0
          } ?: 0
      "$baseName (${++lastFileCount}).$ext".trimEnd('.')
    } else {
      filename
    }
  }
}
