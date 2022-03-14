/*
 * (C) Copyright 2022 Thanox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package github.tornaco.android.thanos.core.util

import com.elvishew.xlog.XLog
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

object FileUtilsKt {

    fun deleteDir(dir: File): Boolean = dir.deleteRecursively()

    fun delete(file: File): Boolean = if (file.isDirectory) deleteDir(file) else file.delete()

    fun deleteDirQuiet(dir: File) {
        kotlin.runCatching {
            DevNull.accept(deleteDir(dir))
            DevNull.accept(dir.delete())
        }
    }

    @Throws(IOException::class)
    fun createParentDirs(file: File) {
        val parent = file.canonicalFile.parentFile ?: return
        parent.mkdirs()
        if (!parent.isDirectory) {
            throw IOException("Unable to create parent directories of $file")
        }
    }

    fun writeString(str: String, path: String): Boolean {
        return kotlin.runCatching {
            File(path).writeText(str)
            true
        }.getOrElse {
            XLog.e("writeString error", it)
            false
        }
    }

    fun readString(path: String): String? {
        return kotlin.runCatching {
            File(path).readText()
        }.getOrElse {
            XLog.e("writeString error", it)
            null
        }
    }


    fun isEmptyDir(dir: File): Boolean {
        return dir.exists() && dir.isDirectory && dir.list().isNullOrEmpty()
    }

    fun getNameWithoutExtension(file: String): String? {
        val fileName = File(file).name
        val dotIndex = fileName.lastIndexOf('.')
        return if (dotIndex == -1) fileName else fileName.substring(0, dotIndex)
    }

    fun getFileExtension(fullName: String): String {
        val fileName = File(fullName).name
        val dotIndex = fileName.lastIndexOf('.')
        return if (dotIndex == -1) "" else fileName.substring(dotIndex + 1)
    }

    fun copy(src: File, dest: File) = src.copyTo(dest, overwrite = true)
    fun copy(src: InputStream, dest: File) = src.copyTo(dest.outputStream())
    fun copy(src: File, dest: OutputStream) = src.inputStream().copyTo(dest)
    fun inputStream(src: File) = src.inputStream()
    fun outputStream(src: File) = src.outputStream()
    fun write(buffer: ByteArray, dest: File) = dest.writeBytes(buffer)
}