package com.anggrayudi.storage.extension

import android.os.Environment
import io.mockk.every
import io.mockk.mockkStatic
import java.io.File
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Created on 20/08/20
 *
 * @author Anggrayudi H
 */
class TextExtKtTest {

  @Before
  fun setUp() {
    mockkStatic(Environment::class)
    every { Environment.getExternalStorageDirectory() } answers { File("/storage/emulated/0") }
  }

  @Test
  fun count() {
    assertEquals(4, "abcs8abc88habcabci7h".count("abc"))
    assertEquals(6, "87jkakkubaakjnaaa".count("a"))
    assertEquals(0, "87jkakku baakjnaaa".count(""))
    assertEquals(0, "87jka kkubaakjnaaa".count("abc"))
    assertEquals(
      1,
      "primary:DCIM/document/primary:DCIM/document/assas/document/as".count("/document/") % 2,
    )
  }

  fun String.splitToPairAt(text: String, occurence: Int): Pair<String, String>? {
    var index = indexOf(text)
    if (text.isEmpty() || index == -1 || occurence < 1) {
      return null
    }
    var count = 0
    do {
      count++
      if (occurence == count) {
        return Pair(substring(0, index), substring(index + text.length, length))
      }
      index = indexOf(text, startIndex = index + text.length)
    } while (index in 1 until length)
    return null
  }

  @Test
  fun splitAt() {
    assertEquals(
      Pair("asosdisf/doc", "safsfsfaf/doc/8hhyjbh"),
      "asosdisf/doc/safsfsfaf/doc/8hhyjbh".splitToPairAt("/", 2),
    )
  }

  @Test
  fun replaceCompletely() {
    assertEquals("/storage/ABC//Movie/", "/storage/ABC////Movie/".replace("//", "/"))
    assertEquals("/storage/ABC/Movie/", "/storage/ABC///Movie/".replaceCompletely("//", "/"))
    assertEquals("/storage/ABC/Movie/", "/storage////ABC///Movie//".replaceCompletely("//", "/"))
    assertEquals("BB", "aaaaaaaaBaaaaaaBa".replaceCompletely("a", ""))
  }

  @Test
  fun hasParent() {
    assertTrue("/path/Music//Pop".hasParent("/path/Music"))
    assertTrue("/path/Music/Pop/".hasParent("/path/Music"))
    assertTrue("/path/Music".hasParent("/path/Music"))
    assertTrue("Download".hasParent("Download"))

    assertFalse("/path/Music".hasParent("/path/Music/Pop"))
    assertFalse("/path/Music".hasParent("/path/MusicMetal"))
    assertFalse("/path/Music".hasParent("/path/MusiC"))
    assertFalse("/path/Musics".hasParent("/path/Music"))
  }

  @Test
  fun childOf() {
    assertTrue("/path/Music/Pop".childOf("/path/Music"))

    assertFalse("/path/Music/Pop".childOf("/path/Music/Rock"))
    assertFalse("/path/Music".childOf("/path/Music/Rock"))
  }

  @Test
  fun parent() {
    assertEquals("/storage/AAAA-BBBB/Download", "/storage/AAAA-BBBB/Download/abc.txt".parent())
    assertEquals("/storage/AAAA-BBBB", "/storage/AAAA-BBBB/abc.txt".parent())
    assertEquals("", "/storage/AAAA-BBBB".parent())

    assertEquals("/storage/emulated/0/Download", "/storage/emulated/0/Download/abc.txt".parent())
    assertEquals("/storage/emulated/0", "/storage/emulated/0/abc.txt".parent())
    assertEquals("", "/storage/emulated/0".parent())
  }
}
