/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package github.tornaco.android.thanos.core.compat;

import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import github.tornaco.android.thanos.core.annotation.GuardedBy;
import github.tornaco.android.thanos.core.annotation.NonNull;
import github.tornaco.android.thanos.core.annotation.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Compatibility library for NotificationManager with fallbacks for older platforms.
 *
 * <p>To use this class, call the static function {@link #from} to get a
 * {@link NotificationManagerCompat} object, and then call one of its
 * methods to post or cancel notifications.
 */
@SuppressWarnings("WeakerAccess")
public final class NotificationManagerCompat {
    private static final String TAG = "NotifManCompat";
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    /**
     * Notification extras key: if set to true, the posted notification should use
     * the side channel for delivery instead of using notification manager.
     */
    public static final String EXTRA_USE_SIDE_CHANNEL = "android.support.useSideChannel";

    /**
     * Intent action to register for on a service to receive side channel
     * notifications. The listening service must be in the same package as an enabled
     * {@link android.service.notification.NotificationListenerService}.
     */
    public static final String ACTION_BIND_SIDE_CHANNEL =
            "android.support.BIND_NOTIFICATION_SIDE_CHANNEL";

    /**
     * Maximum sdk build version which needs support for side channeled notifications.
     * Currently the only needed use is for side channeling group children before KITKAT_WATCH.
     */
    static final int MAX_SIDE_CHANNEL_SDK_VERSION = 19;

    /**
     * Base time delay for a side channel listener queue retry.
     */
    private static final int SIDE_CHANNEL_RETRY_BASE_INTERVAL_MS = 1000;
    /**
     * Maximum retries for a side channel listener before dropping tasks.
     */
    private static final int SIDE_CHANNEL_RETRY_MAX_COUNT = 6;
    /**
     * Hidden field Settings.Secure.ENABLED_NOTIFICATION_LISTENERS
     */
    private static final String SETTING_ENABLED_NOTIFICATION_LISTENERS =
            "enabled_notification_listeners";

    /**
     * Cache of enabled notification listener components
     */
    private static final Object sEnabledNotificationListenersLock = new Object();
    @GuardedBy("sEnabledNotificationListenersLock")
    private static String sEnabledNotificationListeners;
    @GuardedBy("sEnabledNotificationListenersLock")
    private static Set<String> sEnabledNotificationListenerPackages = new HashSet<String>();

    private final Context mContext;
    private final NotificationManager mNotificationManager;
    /**
     * Lock for mutable static fields
     */
    private static final Object sLock = new Object();

    /**
     * Value signifying that the user has not expressed an importance.
     * <p>
     * This value is for persisting preferences, and should never be associated with
     * an actual notification.
     */
    public static final int IMPORTANCE_UNSPECIFIED = -1000;

    /**
     * A notification with no importance: shows nowhere, is blocked.
     */
    public static final int IMPORTANCE_NONE = 0;

    /**
     * Min notification importance: only shows in the shade, below the fold.
     */
    public static final int IMPORTANCE_MIN = 1;

    /**
     * Low notification importance: shows everywhere, but is not intrusive.
     */
    public static final int IMPORTANCE_LOW = 2;

    /**
     * Default notification importance: shows everywhere, allowed to makes noise,
     * but does not visually intrude.
     */
    public static final int IMPORTANCE_DEFAULT = 3;

    /**
     * Higher notification importance: shows everywhere, allowed to makes noise and peek.
     */
    public static final int IMPORTANCE_HIGH = 4;

    /**
     * Highest notification importance: shows everywhere, allowed to makes noise, peek, and
     * use full screen intents.
     */
    public static final int IMPORTANCE_MAX = 5;

    /**
     * Get a {@link NotificationManagerCompat} instance for a provided context.
     */
    @NonNull
    public static NotificationManagerCompat from(@NonNull Context context) {
        return new NotificationManagerCompat(context);
    }

    private NotificationManagerCompat(Context context) {
        mContext = context;
        mNotificationManager = (NotificationManager) mContext.getSystemService(
                Context.NOTIFICATION_SERVICE);
    }

    /**
     * Cancel a previously shown notification.
     *
     * @param id the ID of the notification
     */
    public void cancel(int id) {
        cancel(null, id);
    }

    /**
     * Cancel a previously shown notification.
     *
     * @param tag the string identifier of the notification.
     * @param id  the ID of the notification
     */
    public void cancel(@Nullable String tag, int id) {
        mNotificationManager.cancel(tag, id);
    }

    /**
     * Cancel all previously shown notifications.
     */
    public void cancelAll() {
        mNotificationManager.cancelAll();
    }

    /**
     * Post a notification to be shown in the status bar, stream, etc.
     *
     * @param id           the ID of the notification
     * @param notification the notification to post to the system
     */
    public void notify(int id, @NonNull Notification notification) {
        notify(null, id, notification);
    }

    /**
     * Post a notification to be shown in the status bar, stream, etc.
     *
     * @param tag          the string identifier for a notification. Can be {@code null}.
     * @param id           the ID of the notification. The pair (tag, id) must be unique within your app.
     * @param notification the notification to post to the system
     */
    public void notify(@Nullable String tag, int id, @NonNull Notification notification) {
        if (useSideChannelForNotification(notification)) {
            // Cancel this notification in notification manager if it just transitioned to being
            // side channelled.
            mNotificationManager.cancel(tag, id);
        } else {
            mNotificationManager.notify(tag, id, notification);
        }
    }

    /**
     * Returns whether notifications from the calling package are not blocked.
     */
    public boolean areNotificationsEnabled() {
        if (Build.VERSION.SDK_INT >= 24) {
            return mNotificationManager.areNotificationsEnabled();
        } else {
            AppOpsManager appOps =
                    (AppOpsManager) mContext.getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = mContext.getApplicationInfo();
            String pkg = mContext.getApplicationContext().getPackageName();
            int uid = appInfo.uid;
            try {
                Class<?> appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE,
                        Integer.TYPE, String.class);
                Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
                int value = (int) opPostNotificationValue.get(Integer.class);
                return ((int) checkOpNoThrowMethod.invoke(appOps, value, uid, pkg)
                        == AppOpsManager.MODE_ALLOWED);
            } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException
                    | InvocationTargetException | IllegalAccessException | RuntimeException e) {
                return true;
            }
        }
    }

    /**
     * Returns the user specified importance for notifications from the calling package.
     *
     * @return An importance level, such as {@link #IMPORTANCE_DEFAULT}.
     */
    public int getImportance() {
        if (Build.VERSION.SDK_INT >= 24) {
            return mNotificationManager.getImportance();
        } else {
            return IMPORTANCE_UNSPECIFIED;
        }
    }

    /**
     * Get the set of packages that have an enabled notification listener component within them.
     */
    @NonNull
    public static Set<String> getEnabledListenerPackages(@NonNull Context context) {
        final String enabledNotificationListeners = Settings.Secure.getString(
                context.getContentResolver(),
                SETTING_ENABLED_NOTIFICATION_LISTENERS);
        synchronized (sEnabledNotificationListenersLock) {
            // Parse the string again if it is different from the last time this method was called.
            if (enabledNotificationListeners != null
                    && !enabledNotificationListeners.equals(sEnabledNotificationListeners)) {
                final String[] components = enabledNotificationListeners.split(":", -1);
                Set<String> packageNames = new HashSet<String>(components.length);
                for (String component : components) {
                    ComponentName componentName = ComponentName.unflattenFromString(component);
                    if (componentName != null) {
                        packageNames.add(componentName.getPackageName());
                    }
                }
                sEnabledNotificationListenerPackages = packageNames;
                sEnabledNotificationListeners = enabledNotificationListeners;
            }
            return sEnabledNotificationListenerPackages;
        }
    }

    /**
     * Returns true if this notification should use the side channel for delivery.
     */
    private static boolean useSideChannelForNotification(Notification notification) {
        Bundle extras = NotificationCompat.getExtras(notification);
        return extras != null && extras.getBoolean(EXTRA_USE_SIDE_CHANNEL);
    }
}
