package github.tornaco.android.thanox.module.notification.recorder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.thanos.core.compat.NotificationCompat;
import github.tornaco.android.thanos.core.util.ClipboardUtils;
import github.tornaco.android.thanos.core.util.OsUtils;
import github.tornaco.android.thanos.widget.section.SectioningAdapter;
import github.tornaco.android.thanox.module.notification.recorder.databinding.ModuleNotificationRecorderItemBinding;
import github.tornaco.android.thanox.module.notification.recorder.databinding.ModuleNotificationRecorderItemFooterBinding;
import github.tornaco.android.thanox.module.notification.recorder.databinding.ModuleNotificationRecorderItemHeaderBinding;
import util.Consumer;

public class NRListAdapter extends SectioningAdapter implements Consumer<List<NotificationRecordModelGroup>> {

    private final List<NotificationRecordModelGroup> nrdList = new ArrayList<>();

    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemUserType) {
        return new IVH(ModuleNotificationRecorderItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerUserType) {
        return new HVH(ModuleNotificationRecorderItemHeaderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public FooterViewHolder onCreateFooterViewHolder(ViewGroup parent, int footerUserType) {
        return new FVH(ModuleNotificationRecorderItemFooterBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder viewHolder, int sectionIndex, int headerUserType) {
        super.onBindHeaderViewHolder(viewHolder, sectionIndex, headerUserType);
        HVH hvh = (HVH) viewHolder;
        hvh.binding.setTitle(nrdList.get(sectionIndex).getDateDesc() + String.format("(%s)", nrdList.get(sectionIndex).getModels().size()));
        hvh.binding.executePendingBindings();
    }

    @Override
    public void onBindItemViewHolder(ItemViewHolder viewHolder, int sectionIndex, int itemIndex, int itemUserType) {
        super.onBindItemViewHolder(viewHolder, sectionIndex, itemIndex, itemUserType);
        NotificationRecordModelGroup group = nrdList.get(sectionIndex);
        NotificationRecordModel model = group.getModels().get(itemIndex);
        IVH ivh = (IVH) viewHolder;
        ivh.binding.setApp(model.getAppInfo());
        ivh.binding.setNrd(model.getRecord());
        ivh.binding.setTimeFormatted(model.getFormattedTime());
        ivh.binding.card.setOnClickListener(v -> {
            ClipboardUtils.copyToClipboard(ivh.itemView.getContext(), "thanox.notification.content", model.getRecord().getContent());
            Toast.makeText(
                    ivh.itemView.getContext(),
                    R.string.common_toast_copied_to_clipboard,
                    Toast.LENGTH_SHORT).show();
        });
        ivh.binding.card.setOnLongClickListener(v -> {
            if (model.getRecord().isToast()) {
                mockToast(ivh.itemView.getContext(), model.getRecord().getContent());
            } else {
                mockNotification(ivh.itemView.getContext(),
                        model.getRecord().getTitle(),
                        model.getRecord().getContent(),
                        model.getRecord().getWhen());
            }
            return true;
        });
        ivh.binding.executePendingBindings();
    }

    private void mockToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_LONG).show();
    }

    private void mockNotification(Context context, String title, String content, long when) {
        createNotificationChannelIfNeed(context);
        Notification n = new NotificationCompat.Builder(context, "NR-Mock")
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.module_notification_recorder_ic_chat_3_line)
                .setWhen(when)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                .setFullScreenIntent(PendingIntent.getBroadcast(
                        context,
                        String.valueOf(System.currentTimeMillis()).hashCode(),
                        new Intent("thanox.action.mock.notification"),
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE),
                        true)
                .build();
        NotificationManagerCompat.from(context)
                .notify(String.valueOf(System.currentTimeMillis()).hashCode(), n);
    }

    private void createNotificationChannelIfNeed(Context context) {
        if (!OsUtils.isOOrAbove()) {
            return;
        }
        if (NotificationManagerCompat.from(context).getNotificationChannel("NR-Mock") != null) {
            return;
        }
        NotificationManagerCompat.from(context)
                .createNotificationChannel(new NotificationChannel("NR-Mock", "NR-Mock", NotificationManager.IMPORTANCE_HIGH));
    }

    @Override
    public void onBindFooterViewHolder(FooterViewHolder viewHolder, int sectionIndex, int footerUserType) {
        super.onBindFooterViewHolder(viewHolder, sectionIndex, footerUserType);
    }

    @Override
    public int getNumberOfItemsInSection(int sectionIndex) {
        return nrdList.get(sectionIndex).getModels().size();
    }

    @Override
    public int getNumberOfSections() {
        return nrdList.size();
    }

    @Override
    public boolean doesSectionHaveHeader(int sectionIndex) {
        return true;
    }

    @Override
    public boolean doesSectionHaveFooter(int sectionIndex) {
        return true;
    }

    @Override
    public void accept(List<NotificationRecordModelGroup> nrds) {
        this.nrdList.clear();
        if (nrds != null) {
            this.nrdList.addAll(nrds);
        }
        notifyAllSectionsDataSetChanged();
    }

    final static class HVH extends HeaderViewHolder {
        private ModuleNotificationRecorderItemHeaderBinding binding;

        HVH(ModuleNotificationRecorderItemHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public github.tornaco.android.thanox.module.notification.recorder.databinding.ModuleNotificationRecorderItemHeaderBinding getBinding() {
            return this.binding;
        }
    }

    final static class IVH extends ItemViewHolder {
        private ModuleNotificationRecorderItemBinding binding;

        IVH(ModuleNotificationRecorderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public github.tornaco.android.thanox.module.notification.recorder.databinding.ModuleNotificationRecorderItemBinding getBinding() {
            return this.binding;
        }
    }

    final static class FVH extends FooterViewHolder {
        private ModuleNotificationRecorderItemFooterBinding binding;

        FVH(ModuleNotificationRecorderItemFooterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public github.tornaco.android.thanox.module.notification.recorder.databinding.ModuleNotificationRecorderItemFooterBinding getBinding() {
            return this.binding;
        }
    }
}
