package github.tornaco.android.thanox.module.notification.recorder.source

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig

class NotificationRecordRepository(private val context: Context) {
    fun all(pageSize: Int, keyword: String) = Pager(PagingConfig(pageSize)) {
        NotificationRecordPagingSource(context, keyword)
    }.flow
}
