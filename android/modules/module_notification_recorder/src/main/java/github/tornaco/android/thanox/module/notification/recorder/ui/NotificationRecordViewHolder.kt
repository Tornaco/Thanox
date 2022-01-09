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
            showOptionsMenuForRecordItem(binding.root, model)
        }
        binding.executePendingBindings()
    }

    private fun showOptionsMenuForRecordItem(rootView: View, model: NotificationRecordModel) {
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

    private fun zoom(rootView: View, model: NotificationRecordModel) {
        val holder = create(LinearLayout(rootView.context))
        holder.bind(model)
        holder.binding.card.setCardBackgroundColor(Color.TRANSPARENT)
        MaterialAlertDialogBuilder(rootView.context)
            .setView(holder.itemView)
            .show()
    }

    private fun reproduce(model: NotificationRecordModel) {
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

    private fun copyNotificationToClipboard(model: NotificationRecordModel) {
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
        fun create(parent: ViewGroup): NotificationRecordViewHolder =
            NotificationRecordViewHolder(
                ModuleNotificationRecorderItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
    }
}