package github.tornaco.android.thanox.module.notification.recorder.ui

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import github.tornaco.android.thanox.module.notification.recorder.NotificationRecordModel

class NotificationAdapter :
    PagingDataAdapter<NotificationRecordModel, NotificationRecordViewHolder>(POST_COMPARATOR) {

    companion object {
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<NotificationRecordModel>() {
            override fun areContentsTheSame(
                oldItem: NotificationRecordModel,
                newItem: NotificationRecordModel
            ): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(
                oldItem: NotificationRecordModel,
                newItem: NotificationRecordModel
            ): Boolean =
                oldItem == newItem

            override fun getChangePayload(
                oldItem: NotificationRecordModel,
                newItem: NotificationRecordModel
            ): Any? {
                return null
            }
        }
    }

    override fun onBindViewHolder(holder: NotificationRecordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationRecordViewHolder {
        return NotificationRecordViewHolder.create(parent)
    }
}

