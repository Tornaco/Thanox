package github.tornaco.android.thanox.module.notification.recorder.ui

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.core.compat.NotificationCompat
import github.tornaco.android.thanos.core.util.OsUtils

object TimeMachine {
    fun mockToast(context: Context, content: String) {
        Toast.makeText(context, content, Toast.LENGTH_LONG).show()
    }

    @SuppressLint("MissingPermission")
    fun mockNotification(context: Context, title: String?, content: String?, `when`: Long) {
        createNotificationChannelIfNeed(context)
        val n = NotificationCompat.Builder(context, "NR-Mock")
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.module_notification_recorder_ic_chat_3_line)
            .setWhen(`when`)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setFullScreenIntent(
                PendingIntent.getBroadcast(
                    context,
                    System.currentTimeMillis().toString().hashCode(),
                    Intent("thanox.action.mock.notification"),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                ),
                true
            )
            .build()
        NotificationManagerCompat.from(context)
            .notify(System.currentTimeMillis().toString().hashCode(), n)
    }

    private fun createNotificationChannelIfNeed(context: Context) {
        if (!OsUtils.isOOrAbove()) {
            return
        }
        if (NotificationManagerCompat.from(context).getNotificationChannel("NR-Mock") != null) {
            return
        }
        NotificationManagerCompat.from(context)
            .createNotificationChannel(
                NotificationChannel(
                    "NR-Mock",
                    "NR-Mock",
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
    }
}