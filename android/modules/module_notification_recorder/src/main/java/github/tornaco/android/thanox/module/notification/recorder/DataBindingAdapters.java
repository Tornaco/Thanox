package github.tornaco.android.thanox.module.notification.recorder;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import util.Consumer;

public class DataBindingAdapters {


    @BindingAdapter("android:notificationRecordModels")
    public static void setNotificationRecordModels(RecyclerView recyclerView, List<NotificationRecordModelGroup> models) {
        @SuppressWarnings("unchecked")
        Consumer<List<NotificationRecordModelGroup>> consumer = (Consumer<List<NotificationRecordModelGroup>>) recyclerView.getAdapter();
        Objects.requireNonNull(consumer).accept(models);
    }
}
