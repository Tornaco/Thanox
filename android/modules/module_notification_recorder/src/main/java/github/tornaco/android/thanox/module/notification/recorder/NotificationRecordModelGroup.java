package github.tornaco.android.thanox.module.notification.recorder;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationRecordModelGroup {
    private String dateDesc;
    private List<NotificationRecordModel> models;
}
