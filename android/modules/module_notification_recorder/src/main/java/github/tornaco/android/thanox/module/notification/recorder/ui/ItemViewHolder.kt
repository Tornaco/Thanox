package github.tornaco.android.thanox.module.notification.recorder.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import github.tornaco.android.thanos.core.util.ClipboardUtils
import github.tornaco.android.thanox.module.notification.recorder.R
import github.tornaco.android.thanox.module.notification.recorder.databinding.ModuleNotificationRecorderItemBinding
import github.tornaco.android.thanox.module.notification.recorder.model.NotificationRecordModel

class ItemViewHolder(private val binding: ModuleNotificationRecorderItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: NotificationRecordModel.Item?) {
        if (item == null) {
            binding.card.visibility = View.INVISIBLE
            return
        }

        bindItem(item)
    }

    private fun bindItem(item: NotificationRecordModel.Item) {
        binding.app = item.appInfo
        binding.nrd = item.record
        binding.timeFormatted = item.formattedTime
        binding.card.setOnClickListener {
            showOptionsMenuForRecordItem(binding.root, item)
        }
        binding.executePendingBindings()
    }

    private fun showOptionsMenuForRecordItem(rootView: View, model: NotificationRecordModel.Item) {
        val popupMenu = PopupMenu(rootView.context, rootView)
        popupMenu.inflate(R.menu.module_notification_recorder_nr_item)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_copy_content -> {
                    copyNotificationToClipboard(model)
                }
                R.id.action_reproduce -> {
                    reproduce(model)
                }
                R.id.action_view_zoom -> {
                    zoom(rootView, model)
                }
            }
            true
        }
        popupMenu.show()
    }

    private fun zoom(rootView: View, model: NotificationRecordModel.Item) {
        val holder = create(LinearLayout(rootView.context))
        holder.bind(model)
        holder.binding.card.setCardBackgroundColor(Color.TRANSPARENT)
        MaterialAlertDialogBuilder(rootView.context)
            .setView(holder.itemView)
            .show()
    }

    private fun reproduce(model: NotificationRecordModel.Item) {
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
    }

    private fun copyNotificationToClipboard(model: NotificationRecordModel.Item) {
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

    companion object Factory {
        fun create(parent: ViewGroup): ItemViewHolder =
            ItemViewHolder(
                ModuleNotificationRecorderItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
    }
}