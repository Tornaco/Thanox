package github.tornaco.android.thanos.core.os

import android.util.Log
import java.io.ByteArrayOutputStream
import java.util.zip.Deflater
import java.util.zip.Inflater

internal object ByteArrayCompressor {

    fun compress(data: ByteArray): ByteArray {
        val deflater = Deflater()
        deflater.setInput(data)
        deflater.finish()

        val buffer = ByteArray(1024)
        val outputStream = ByteArrayOutputStream()
        try {
            while (!deflater.finished()) {
                val count = deflater.deflate(buffer)
                outputStream.write(buffer, 0, count)
            }
        } finally {
            deflater.end()
        }
        return outputStream.toByteArray().also {
            Log.v("ByteArrayCompressor", "After compress size: ${it.size}")
        }
    }


    fun decompress(data: ByteArray): ByteArray {
        return runCatching {
            val inflater = Inflater()
            inflater.setInput(data)

            val buffer = ByteArray(1024)
            val outputStream = ByteArrayOutputStream()
            try {
                while (!inflater.finished()) {
                    val count = inflater.inflate(buffer)
                    outputStream.write(buffer, 0, count)
                }
            } finally {
                inflater.end()
            }
            outputStream.toByteArray()
        }.getOrElse {
            // Fallback to raw data for compat(shortcut stub) issue.
            data
        }
    }
}