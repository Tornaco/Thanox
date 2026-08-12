package com.anggrayudi.storage.file

import android.webkit.MimeTypeMap
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created on 15/12/21
 *
 * @author Anggrayudi H
 */
class MimeTypeTest {

  @Before
  fun setUp() {
    mockkStatic(MimeTypeMap::class)
    every { MimeTypeMap.getSingleton() } returns
      mockk {
        every { getExtensionFromMimeType("*/*") } returns null
        every { getExtensionFromMimeType("video/*") } returns null
        every { getExtensionFromMimeType("video/mp4") } returns "mp4"
        every { getExtensionFromMimeType("image/*") } returns "jpg"
        every { getExtensionFromMimeType("image/jpg") } returns "jpg"
        every { getExtensionFromMimeType("application/octet-stream") } returns "bin"
        every { getMimeTypeFromExtension("mp4") } returns "video/mp4"
      }
  }

  @Test
  fun getFullFileName() {
    assertEquals("ABC.mp4", MimeType.getFullFileName("ABC", "video/mp4"))
    assertEquals("ABC", MimeType.getFullFileName("ABC", null))
    assertEquals("ABC.mp4", MimeType.getFullFileName("ABC.mp4", "video/mp4"))
    assertEquals("abc.mp4.jpg", MimeType.getFullFileName("abc.mp4", "image/jpg"))
    assertEquals("abc.jpg", MimeType.getFullFileName("abc.jpg", "image/jpg"))
    assertEquals("abc.jpg", MimeType.getFullFileName("abc.jpg", "image/*"))
    assertEquals("abc.jpg", MimeType.getFullFileName("abc.jpg", "*/*"))
    assertEquals("abc.jpg", MimeType.getFullFileName("abc.jpg", "application/octet-stream"))
    assertEquals("abc.dwg", MimeType.getFullFileName("abc.dwg", "application/octet-stream"))
    assertEquals("abc.bin", MimeType.getFullFileName("abc", "application/octet-stream"))
    assertEquals("abc.bin", MimeType.getFullFileName("abc.bin", "application/octet-stream"))
  }
}
