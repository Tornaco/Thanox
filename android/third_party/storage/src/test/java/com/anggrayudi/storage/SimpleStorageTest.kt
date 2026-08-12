package com.anggrayudi.storage

import android.content.ContentResolver
import android.content.Context
import android.content.UriPermission
import android.net.Uri
import android.os.Build
import com.anggrayudi.storage.SimpleStorage.Companion.LIBRARY_PACKAGE_NAME
import io.mockk.*
import java.io.File
import kotlin.concurrent.thread
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Created on 26/09/20
 *
 * @author Anggrayudi H
 */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [Build.VERSION_CODES.O_MR1])
class SimpleStorageTest {

  private val context =
    mockk<Context> {
      val dataDirectoryPath = "/data/user/0/${LIBRARY_PACKAGE_NAME}"
      val fileDir = File("$dataDirectoryPath/files")
      every { filesDir } returns fileDir
      every { dataDir } returns File(dataDirectoryPath)
    }

  @Test
  fun cleanupRedundantUriPermissions() {
    val persistedUris =
      listOf(
          "content://com.android.externalstorage.documents/tree/primary%3A a/b/c",
          "content://com.android.externalstorage.documents/tree/primary%3A a/b/cc",
          "content://com.android.externalstorage.documents/tree/primary%3A a/b",
          "content://com.android.externalstorage.documents/tree/primary%3A a/b/cbd",
          "content://com.android.externalstorage.documents/tree/primary%3A y/x",
          "content://com.android.externalstorage.documents/tree/primary%3A a/b/z",
          "content://com.android.externalstorage.documents/tree/primary%3A a/b/d",
          "content://com.android.externalstorage.documents/tree/primary%3A a/b/d/g",
          "content://com.android.externalstorage.documents/tree/primary%3A x/a",
          "content://com.android.externalstorage.documents/tree/primary%3A y",
          "content://com.android.externalstorage.documents/tree/primary%3A a/b",
        )
        .map { Uri.parse(it) }

    val persistedUriPermissions =
      persistedUris.map {
        val uriPermission = mockk<UriPermission>()
        every { uriPermission.isReadPermission } returns true
        every { uriPermission.isWritePermission } returns true
        every { uriPermission.uri } returns it
        uriPermission
      }
    val resolver = mockk<ContentResolver>()
    every { context.contentResolver } returns resolver
    every { resolver.persistedUriPermissions } returns persistedUriPermissions

    val revokedUris =
      setOf(
          "content://com.android.externalstorage.documents/tree/primary%3A a/b/c",
          "content://com.android.externalstorage.documents/tree/primary%3A a/b/cc",
          "content://com.android.externalstorage.documents/tree/primary%3A a/b/cbd",
          "content://com.android.externalstorage.documents/tree/primary%3A y/x",
          "content://com.android.externalstorage.documents/tree/primary%3A a/b/z",
          "content://com.android.externalstorage.documents/tree/primary%3A a/b/d",
          "content://com.android.externalstorage.documents/tree/primary%3A a/b/d/g",
        )
        .map { Uri.parse(it) }

    mockkStatic("kotlin.concurrent.ThreadsKt")
    every { thread(any(), any(), any(), any(), any(), any()) } answers
      {
        lastArg<() -> Unit>().invoke()
        Thread()
      }

    val capturedUris = mutableListOf<Uri>()
    every { resolver.releasePersistableUriPermission(capture(capturedUris), any()) } answers {}

    SimpleStorage.cleanupRedundantUriPermissions(context)

    assertEquals(revokedUris, capturedUris)
    verify(exactly = revokedUris.size) { resolver.releasePersistableUriPermission(any(), any()) }
    verify { resolver.persistedUriPermissions }
    confirmVerified(resolver)
  }
}
