package github.tornaco.android.thanox.module.notification.recorder.ui

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import github.tornaco.android.thanox.module.notification.recorder.model.NotificationRecordModel

class NotificationAdapter :
    PagingDataAdapter<NotificationRecordModel, RecyclerView.ViewHolder>(POST_COMPARATOR) {

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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) holder.bind(getItem(position) as NotificationRecordModel.Item?)
        else if (holder is HeaderViewHolder) holder.bind(getItem(position) as NotificationRecordModel.Separator?)
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item is NotificationRecordModel.Item) VIEW_TYPE_ITEM else VIEW_TYPE_HEADER
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) ItemViewHolder.create(parent)
        else HeaderViewHolder.create(parent)
    }
}

private const val VIEW_TYPE_ITEM = 0
private const val VIEW_TYPE_HEADER = 1

