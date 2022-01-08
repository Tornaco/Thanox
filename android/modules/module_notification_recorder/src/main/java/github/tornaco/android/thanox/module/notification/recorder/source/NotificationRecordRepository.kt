package github.tornaco.android.thanox.module.notification.recorder.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import github.tornaco.android.thanos.core.app.ThanosManager

class NotificationRecordRepository(private val thanox: ThanosManager) {
    fun all(pageSize: Int, keyword: String) = Pager(PagingConfig(pageSize)) {
        NotificationRecordPagingSource(thanox, keyword)
    }.flow
}
