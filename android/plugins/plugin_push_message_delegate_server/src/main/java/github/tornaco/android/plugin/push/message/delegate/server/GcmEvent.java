package github.tornaco.android.plugin.push.message.delegate.server;

import lombok.Data;

/**
 * Created by Tornaco on 2018/4/29 12:21.
 * God bless no bug!
 */
@Data
public class GcmEvent {
    private long eventTime;
    private String eventPackage;
}
