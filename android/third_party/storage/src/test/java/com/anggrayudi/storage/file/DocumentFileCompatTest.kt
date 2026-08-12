package com.anggrayudi.storage.file

import android.content.Context
import android.os.Environment
import com.anggrayudi.storage.SimpleStorage.Companion.LIBRARY_PACKAGE_NAME
import com.anggrayudi.storage.file.StorageId.PRIMARY
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created on 12/1/20
 *
 * @author Anggrayudi H
 */
class DocumentFileCompatTest {

  private val context =
    mockk<Context> {
      val dataDirectoryPath = "/data/user/0/${LIBRARY_PACKAGE_NAME}"
      val fileDir = File("$dataDirectoryPath/files")
      every { filesDir } returns fileDir
      every { dataDir } returns File(dataDirectoryPath)
    }

  @Before
  fun setUp() {
    mockkStatic(Environment::class)
    every { Environment.getExternalStorageDirectory() } answers { File("/storage/emulated/0") }
  }

  @Test
  fun getStorageId() {
    assertEquals(PRIMARY, DocumentFileCompat.getStorageId(context, "/storage/emulated/0"))
    assertEquals(PRIMARY, DocumentFileCompat.getStorageId(context, "/storage/emulated/0/Music"))
    assertEquals(PRIMARY, DocumentFileCompat.getStorageId(context, "primary:Music"))
    assertEquals("AAAA-BBBB", DocumentFileCompat.getStorageId(context, "/storage/AAAA-BBBB/Music"))
    assertEquals("AAAA-BBBB", DocumentFileCompat.getStorageId(context, "AAAA-BBBB:Music"))
  }

  @Test
  fun getBasePath() {
    assertEquals("", DocumentFileCompat.getBasePath(context, "/storage/emulated/0"))
    assertEquals("", DocumentFileCompat.getBasePath(context, "AAAA-BBBB:"))
    assertEquals("Music", DocumentFileCompat.getBasePath(context, "/storage/emulated/0/Music"))
    assertEquals("Music", DocumentFileCompat.getBasePath(context, "primary:Music"))
    assertEquals(
      "Music/Pop",
      DocumentFileCompat.getBasePath(context, "/storage/AAAA-BBBB//Music///Pop/"),
    )
    assertEquals("Music", DocumentFileCompat.getBasePath(context, "AAAA-BBBB:Music"))
  }

  @Test
  fun findUniqueParents() {
    val folderPaths =
      listOf(
        "/storage/9016-4EF8/Downloads",
        "/storage/9016-4EF8/Downloads/Archive",
        "/storage/9016-4EF8/Video",
        "/storage/9016-4EF8/Music",
        "/storage/9016-4EF8/Music/Favorites/Pop",
        "/storage/emulated/0/Music",
        "/storage/emulated/0/Music Indo",
        "/folder/subFolder/b/c",
        "/folder/subFolder/b",
        "/folder/subFolder/b/d",
        "primary:Alarm/Morning",
        "primary:Alarm",
      )

    val expected =
      listOf(
        "/storage/9016-4EF8/Downloads",
        "/storage/9016-4EF8/Video",
        "/storage/9016-4EF8/Music",
        "/storage/emulated/0/Music",
        "/storage/emulated/0/Music Indo",
        "/folder/subFolder/b",
        "/storage/emulated/0/Alarm",
      )

    val results = DocumentFileCompat.findUniqueParents(context, folderPaths)
    assertEquals(expected, results)
  }

  @Test
  fun findUniqueDeepestSubFolders() {
    val folderPaths =
      listOf(
        "/storage/9016-4EF8/Downloads",
        "/storage/9016-4EF8/Downloads/Archive",
        "/storage/9016-4EF8/Video",
        "/storage/9016-4EF8/Music",
        "/storage/9016-4EF8/Music/Favorites/Pop",
        "/storage/emulated/0/Music",
        "/tree/primary/b/c",
        "/tree/primary/b",
        "/tree/primary/b/d",
        "primary:Alarm/Morning",
        "primary:Alarm",
      )

    val expected =
      listOf(
        "/storage/9016-4EF8/Downloads/Archive",
        "/storage/9016-4EF8/Video",
        "/storage/9016-4EF8/Music/Favorites/Pop",
        "/storage/emulated/0/Music",
        "/tree/primary/b/c",
        "/tree/primary/b/d",
        "/storage/emulated/0/Alarm/Morning",
      )

    val results = DocumentFileCompat.findUniqueDeepestSubFolders(context, folderPaths)
    assertEquals(expected, results)
  }
}
