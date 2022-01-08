package github.tornaco.android.thanox.module.notification.recorder.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import github.tornaco.android.thanos.core.util.ClipboardUtils
import github.tornaco.android.thanox.module.notification.recorder.NotificationRecordModel
import github.tornaco.android.thanox.module.notification.recorder.R
import github.tornaco.android.thanox.module.notification.recorder.databinding.ModuleNotificationRecorderItemBinding

class NotificationRecordViewHolder(private val binding: ModuleNotificationRecorderItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: NotificationRecordModel?) {
        if (model == null) {
            binding.card.visibility = View.INVISIBLE
            return
        }

        binding.app = model.appInfo
        binding.nrd = model.record
        binding.timeFormatted = model.formattedTime
        binding.card.setOnClickListener {
            ClipboardUtils.copyToClipboard(
                itemView.context,
                "thanox.notification.content",
                model.record.content
            )
            Toast.makeText(
                itemView.context,
                R.string.common_toast_copied_to_clipboard,
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.card.setOnLongClickListener {
            if (model.record.isToast) {
                TimeMachine.mockToast(itemView.context, model.record.content)
            } else {
                TimeMachine.mockNotification(
                    itemView.context,
                    model.record.title,
                    model.record.content,
                    model.record.getWhen()
                )
            }
            true
        }
        binding.executePendingBindings()
    }

    companion object Factory {
        fun create(parent: ViewGroup): NotificationRecordViewHolder =
            NotificationRecordViewHolder(
                ModuleNotificationRecorderItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
    }
}