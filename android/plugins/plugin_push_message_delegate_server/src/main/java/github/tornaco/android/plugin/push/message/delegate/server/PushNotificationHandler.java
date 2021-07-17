package github.tornaco.android.plugin.push.message.delegate.server;

import android.content.Intent;

import github.tornaco.android.thanos.core.n.NotificationRecord;

/**
 * Created by Tornaco on 2018/4/10 17:24.
 * God bless no bug!
 */
public interface PushNotificationHandler {

    String KEY_MOCK_MESSAGE = "MOCK_MESSAGE_APM";

    boolean handleIncomingIntent(String targetPackage, Intent intent);

    void onTopPackageChanged(String who);

    String getTargetPackageName();

    void onNewNotification(NotificationRecord record);

    void systemReady();
}
