package github.tornaco.android.thanox.module.notification.recorder.source

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import com.elvishew.xlog.XLog
import github.tornaco.android.thanos.core.annotation.DoNotStrip
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.n.NotificationRecord
import github.tornaco.android.thanos.core.pm.PackageManager
import github.tornaco.android.thanos.core.util.DateUtils
import github.tornaco.android.thanos.core.util.GsonUtils
import github.tornaco.android.thanos.core.util.OsUtils
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class NRExport(private val context: Context) {
    fun exportAllNRs() {
        val notificationManager = ThanosManager.from(context).notificationManager
        val pkgManager = ThanosManager.from(context).pkgManager
        var start = 0
        val limit = 10
        val nrs = mutableListOf<NR>()

        while (true) {
            val records = notificationManager.getAllNotificationRecordsByPage(start, limit)

            if (records.isEmpty()) {
                break
            }

            nrs.addAll(records.mapNotNull { runCatching { it.toNR(pkgManager) }.getOrNull() })

            start += limit
        }

        XLog.w("nrs size: ${nrs.size}")

        val json = GsonUtils.GSON.toJson(nrs)
        val fileName =
            "Thanox_Export_Notifications_${DateUtils.formatForFileName(System.currentTimeMillis())}.json"
        val mimeType = "application/json"

        if (OsUtils.isQOrAbove()) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
            uri?.let {
                val outputStream: OutputStream? = resolver.openOutputStream(it)
                outputStream?.use { stream ->
                    stream.write(json.toByteArray())
                }
                XLog.w("File saved to: $uri")
            }
        } else {
            val downloadDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadDir, fileName)

            try {
                val fileOutputStream = FileOutputStream(file)
                fileOutputStream.write(json.toByteArray())
                fileOutputStream.close()
                XLog.w("File saved to: ${file.absolutePath}")
            } catch (e: Exception) {
                XLog.e(e, "Failed to save file: ${e.message}")
            }
        }
    }
}

@DoNotStrip
internal data class NR(
    val appLabel: String,
    val pkgName: String,
    val date: String,
    val title: String,
    val content: String
)

private fun NotificationRecord.toNR(pkgManager: PackageManager): NR {
    val appInfo = pkgManager.getAppInfo(this.pkgName)
    return NR(
        appLabel = appInfo?.appLabel ?: pkgName.orEmpty(),
        pkgName = pkgName.orEmpty(),
        date = DateUtils.formatLongForMessageTime(`when`).orEmpty(),
        title = title.orEmpty(),
        content = content.orEmpty()
    )
}