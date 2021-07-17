package github.tornaco.android.plugin.push.message.delegate.server;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.core.app.AppResources;
import github.tornaco.android.thanos.core.app.ThanosManagerNative;
import github.tornaco.android.thanos.core.compat.NotificationCompat;
import github.tornaco.android.thanos.core.compat.NotificationManagerCompat;
import github.tornaco.android.thanos.core.push.PushMessage;
import github.tornaco.android.thanos.core.util.OsUtils;
import github.tornaco.android.thanos.core.util.PkgUtils;
import github.tornaco.android.thanos.services.xposed.XposedLogger;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Tornaco on 2018/4/10 17:31.
 * God bless no bug!
 */

@SuppressWarnings("WeakerAccess")
// Both used at App and Framework.
public abstract class BasePushNotificationHandler implements PushNotificationHandler {

    @Setter
    @Getter
    private Context context;

    // This is used for app to disable custom tone.
    @Setter
    @Getter
    private boolean customRingtoneEnabled = true;

    private Intent launchIntent;

    private Map<String, Integer> mBadgeMap = new HashMap<>();

    private Set<Integer> mPendingCancelNotifications = new HashSet<>();

    public BasePushNotificationHandler(Context context) {
        this.context = context;
    }

    @Setter
    @Getter
    private boolean enabled,
            startAppOnPushEnabled,
            skipIfRunningEnabled,
            showContentEnabled,
            notificationSoundEnabled,
            notificationVibrateEnabled,
            notificationPostByAppEnabled;

    @Getter
    @Setter
    private String topPackage;

    @Override
    public void onTopPackageChanged(String who) {
        setTopPackage(who);
        if (getTargetPackageName().equals(who)) {
            Log.d(XposedLogger.LOG_PREFIX, String.format("onTopPackageChanged: %s", getClass()));
            // Reset badge.
            clearBadge();
            cancelPendingCancelNotifications();
        }
    }

    protected void clearBadge() {
        mBadgeMap.clear();
    }

    protected void updateBadge(String from) {
        Integer count = mBadgeMap.get(from);
        if (count == null) {
            count = 0;
        }
        mBadgeMap.put(from, count + 1);
    }

    protected int getBadgeFrom(String from) {
        Integer count = mBadgeMap.get(from);
        return count == null ? 0 : count;
    }

    protected int getFromCount() {
        return mBadgeMap.size();
    }

    protected int getAllBadge() {
        int res = 0;
        Map<String, Integer> tmp = new HashMap<>(mBadgeMap);
        for (String key : tmp.keySet()) {
            Integer count = tmp.get(key);
            if (count == null) {
                count = 0;
            }
            res = res + count;
        }
        return res;
    }

    protected boolean isTargetPackageRunningOnTop() {
        return getTargetPackageName().equals(getTopPackage());
    }

    private void addToPendingCancelNotifications(int id) {
        mPendingCancelNotifications.add(id);
    }

    private void clearPendingCancelNotifications() {
        mPendingCancelNotifications.clear();
    }

    protected void cancelPendingCancelNotifications() {
        try {
            Set<Integer> temp = new HashSet<>(mPendingCancelNotifications);
            NotificationManagerCompat nm = NotificationManagerCompat.from(getContext());
            for (Integer id : temp) {
                nm.cancel(id);
            }
            clearPendingCancelNotifications();
        } catch (Throwable e) {
            // We tried...
        }
    }

    protected Intent getLaunchIntent() {
        ensureLauncherIntent();
        return launchIntent;
    }

    private void ensureLauncherIntent() {
        synchronized (this) {
            if (launchIntent == null) {
                launchIntent = getContext().getPackageManager().getLaunchIntentForPackage(getTargetPackageName());
                Log.d(XposedLogger.LOG_PREFIX, "BasePushNotificationHandler, launchIntent=" + launchIntent);
            }
        }
    }

    public void postNotification(PushMessage pushMessage) throws RemoteException {

        createPushNotificationChannelForO(pushMessage.getChannelId(), pushMessage.getChannelName());

        Intent launchIntent = getLaunchIntent();
        if (launchIntent == null) {
            Log.e(XposedLogger.LOG_PREFIX, "Fail retrieve launch intent when postNotification!");
            return;
        }

        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        int nId = ThanosManagerNative.getDefault().getNotificationManager().nextNotificationId();
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), nId, launchIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), pushMessage.getChannelId());

        NotificationCompat.BigTextStyle style =
                new NotificationCompat.BigTextStyle();
        style.bigText(pushMessage.getMessage());
        style.setBigContentTitle(pushMessage.getTitle());

        Uri curSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        try {
            SystemUI.overrideNotificationAppName(getContext(), builder, "微信推送");
        } catch (Throwable ignored) {
        }

        Notification n = builder
                .setContentIntent(pendingIntent)
                .setContentTitle(pushMessage.getTitle())
                .setContentText(pushMessage.getMessage())
                .setAutoCancel(true)
                .setStyle(style)
                .setLargeIcon(new AppResources(getContext(), dynamicThanoxPkgName()).getBitmap(pushMessage.getLargeIconResName()))
                .setVibrate(isNotificationVibrateEnabled() ? new long[]{200, 200, 100, 100} : new long[]{0})
                .setSound(isNotificationSoundEnabled() ? curSoundUri : null)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setPriority(Notification.PRIORITY_HIGH)
                .build();

        n.setSmallIcon(new AppResources(getContext(), dynamicThanoxPkgName()).getIcon(pushMessage.getSmallIconResName()));

        addToPendingCancelNotifications(pushMessage.getFrom());

        NotificationManagerCompat.from(getContext())
                .notify(pushMessage.getFrom(), n);
    }

    private void createPushNotificationChannelForO(String channelId, String channelName) {
        if (OsUtils.isOOrAbove()) {
            NotificationManager notificationManager = (NotificationManager)
                    getContext().getSystemService(
                            Context.NOTIFICATION_SERVICE);
            NotificationChannel nc = null;
            if (notificationManager != null) {
                nc = notificationManager.getNotificationChannel(channelId);
            }
            if (nc != null) {
                // Setup.
                return;
            }
            NotificationChannel notificationChannel;
            notificationChannel = new NotificationChannel(channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{200, 200, 100, 100});
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
    }

    @Override
    public void systemReady() {

    }

    private String dynamicThanoxPkgName() {
        if (PkgUtils.isPkgInstalled(context, BuildProp.THANOS_APP_PKG_NAME_PREFIX)) {
            return BuildProp.THANOS_APP_PKG_NAME_PREFIX;
        }
        if (PkgUtils.isPkgInstalled(context, BuildProp.THANOS_APP_PKG_NAME_PREFIX + ".pro")) {
            return BuildProp.THANOS_APP_PKG_NAME_PREFIX + ".pro";
        }
        // Res package.
        return BuildProp.THANOS_APP_PKG_NAME_PREFIX + ".res";
    }
}
