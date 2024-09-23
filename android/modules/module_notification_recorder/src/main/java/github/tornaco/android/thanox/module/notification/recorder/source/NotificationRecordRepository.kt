package github.tornaco.android.thanox.module.notification.recorder.source

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.insertSeparators
import github.tornaco.android.thanos.core.util.DateUtils
import github.tornaco.android.thanox.module.notification.recorder.R
import github.tornaco.android.thanox.module.notification.recorder.model.NotificationRecordModel
import kotlinx.coroutines.flow.map

class NotificationRecordRepository(private val context: Context) {
    fun all(pageSize: Int, keyword: String) = Pager(PagingConfig(pageSize)) {
        NotificationRecordPagingSource(context, keyword)
    }.flow.map { pageData ->
        pageData.insertSeparators { before: NotificationRecordModel?, after: NotificationRecordModel? ->
            if (before == null && after == null) {
                // List is empty after fully loaded; return null to skip adding separator.
                null
            } else if (after == null) {
                // Footer; return null here to skip adding a footer.
                null
            } else if (before == null) {
                // Header
                separator(after)
            } else if (!DateUtils.isSameDay(before.date, after.date)) {
                // Between two items that start with different letters.
                separator(after)
            } else {
                null
            }
        }
    }

    private fun separator(after: NotificationRecordModel): NotificationRecordModel.Separator {
        val date = after.date
        val isToday = DateUtils.isToday(date)
        val isYesterday = DateUtils.isYesterday(date)
        val isTheDayBeforeYesterday = DateUtils.isTheDayBeforeYesterday(date)
        return NotificationRecordModel.Separator(
            date,
            when {
                isToday -> context.getString(github.tornaco.android.thanos.res.R.string.module_notification_recorder_toady)
                isYesterday -> context.getString(github.tornaco.android.thanos.res.R.string.module_notification_recorder_yesterday)
                isTheDayBeforeYesterday -> context.getString(github.tornaco.android.thanos.res.R.string.module_notification_recorder_the_day_before_yesterday)
                else -> after.formattedTime
            }
        )
    }
}
