package github.tornaco.android.thanox.module.notification.recorder.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import github.tornaco.android.thanos.databinding.ModuleNotificationRecorderItemHeaderBinding
import github.tornaco.android.thanox.module.notification.recorder.model.NotificationRecordModel

class HeaderViewHolder(private val binding: ModuleNotificationRecorderItemHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: NotificationRecordModel.Separator?) {
        if (model == null) {
            binding.root.visibility = View.INVISIBLE
            return
        }

        bindSeparator(model)
    }

    private fun bindSeparator(model: NotificationRecordModel.Separator) {
        binding.title = model.formattedTime
        binding.executePendingBindings()
    }

    companion object Factory {
        fun create(parent: ViewGroup): HeaderViewHolder =
            HeaderViewHolder(
                ModuleNotificationRecorderItemHeaderBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
    }
}