package github.tornaco.android.thanos.util

import android.content.Context
import android.os.ParcelFileDescriptor
import com.elvishew.xlog.XLog
import github.tornaco.android.thanos.core.util.DateUtils
import github.tornaco.android.thanos.core.util.ZipUtils
import github.tornaco.android.thanos.logFolderPath
import java.io.File

fun exportLogs(context: Context, export: (ParcelFileDescriptor) -> Unit): File {
    val tempDir = File(context.cacheDir, "Thanox-Log-${System.currentTimeMillis()}")

    val zipRootDir = requireNotNull(context.externalCacheDir ?: context.cacheDir) {
        "context.cache is null"
    }
    val name = autoGenLogFileName()

    // App log.
    File(context.logFolderPath).copyRecursively(tempDir)

    // Service log.
    val serviceLogFile = File(tempDir, "service.zip").apply { createNewFile() }
    val pfd =
        ParcelFileDescriptor.open(serviceLogFile, ParcelFileDescriptor.MODE_READ_WRITE)
    export.invoke(pfd)

    ZipUtils.zip(tempDir.absolutePath, zipRootDir.absolutePath, name)

    tempDir.deleteRecursively()
    return File(zipRootDir, name).also {
        XLog.d("Done exportLogs: $it")
    }
}

private fun autoGenLogFileName() =
    "Thanox-Log-${DateUtils.formatForFileName(System.currentTimeMillis())}.zip"