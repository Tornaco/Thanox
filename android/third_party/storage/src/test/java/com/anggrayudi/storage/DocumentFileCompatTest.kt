package com.anggrayudi.storage

import com.anggrayudi.storage.file.DocumentFileCompat
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Created on 08/09/20
 *
 * @author Anggrayudi H
 */
class DocumentFileCompatTest {

  @Test
  fun fileNameDuplicationWithExtensionRegex() {
    DocumentFileCompat.FILE_NAME_DUPLICATION_REGEX_WITH_EXTENSION.run {
      assertTrue(matches("file (1).mp4"))
      assertTrue(matches("file (54).mp4"))
      assertTrue(matches("file %$#(*0@ (k) (12).mp4"))

      assertFalse(matches("file.mp4"))
      assertFalse(matches("file-(8).mp4"))
      assertFalse(matches("file (k)."))
      assertFalse(matches("file (k).mp4"))
    }
  }

  @Test
  fun fileNameDuplicationWithoutExtensionRegex() {
    DocumentFileCompat.FILE_NAME_DUPLICATION_REGEX_WITHOUT_EXTENSION.run {
      assertTrue(matches("file (1)"))
      assertTrue(matches("file two (54)"))
      assertTrue(matches("file %$#(*0@ (12)"))

      assertFalse(matches("file"))
      assertFalse(matches("file-(5)"))
      assertFalse(matches("file (k)."))
      assertFalse(matches("file (k).mp4"))
      assertFalse(matches("file (k) (9).mp4"))
    }
  }
}
