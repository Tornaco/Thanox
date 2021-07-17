package github.tornaco.android.plugin.push.message.delegate.server;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;

import github.tornaco.android.thanos.core.compat.NotificationCompat;

/**
 * Created by Tornaco on 2018/5/24 14:48.
 * God bless no bug!
 */
public abstract class SystemUI {
    public static final String EXTRA_SUBSTITUTE_APP_NAME = "android.substName";

    public static void overrideNotificationAppName(Context context, Notification.Builder n, String name) {
        final Bundle extras = new Bundle();
        extras.putString(EXTRA_SUBSTITUTE_APP_NAME,
                name == null ? context.getString(com.android.internal.R.string.android_system_label)
                        : name);

        n.addExtras(extras);
    }

    public static void overrideNotificationAppName(Context context, NotificationCompat.Builder n, String name) {
        final Bundle extras = new Bundle();
        extras.putString(EXTRA_SUBSTITUTE_APP_NAME,
                name == null ? context.getString(com.android.internal.R.string.android_system_label)
                        : name);

        n.addExtras(extras);
    }
}
