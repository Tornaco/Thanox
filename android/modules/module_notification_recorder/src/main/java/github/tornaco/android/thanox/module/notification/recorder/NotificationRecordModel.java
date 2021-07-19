package github.tornaco.android.thanox.module.notification.recorder;

import androidx.annotation.Nullable;

import github.tornaco.android.thanos.core.n.NotificationRecord;
import github.tornaco.android.thanos.core.pm.AppInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationRecordModel {
    private NotificationRecord record;
    @Nullable
    private AppInfo appInfo;
    private String formattedTime;
}
