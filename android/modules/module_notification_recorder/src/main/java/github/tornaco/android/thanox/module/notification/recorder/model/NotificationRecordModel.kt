package github.tornaco.android.thanox.module.notification.recorder.model

import github.tornaco.android.thanos.core.n.NotificationRecord
import github.tornaco.android.thanos.core.pm.AppInfo
import java.util.*

sealed class NotificationRecordModel(val date: Date, val formattedTime: String) {
    class Item(
        val record: NotificationRecord,
        val appInfo: AppInfo,
        formattedTime: String
    ) : NotificationRecordModel(Date(record.`when`), formattedTime)

    class Separator(date: Date, formattedTime: String) :
        NotificationRecordModel(date, formattedTime)
}