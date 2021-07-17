package github.tornaco.android.plugin.push.message.delegate.server;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import github.tornaco.android.plugin.push.message.delegate.Logging;
import github.tornaco.android.thanos.core.Res;
import github.tornaco.android.thanos.core.app.ThanosManagerNative;
import github.tornaco.android.thanos.core.n.NotificationRecord;
import github.tornaco.android.thanos.core.push.PushMessage;

/**
 * Created by Tornaco on 2018/4/10 17:30.
 * God bless no bug!
 */
public class WeChatPushNotificationHandler extends BasePushNotificationHandler {

    public static final String WECHAT_PKG_NAME = "com.tencent.mm";

    private static final String NOTIFICATION_CHANNEL_ID_WECHAT = "dev.tornaco.notification.channel.id.thanox-wechat";
    private static final String NOTIFICATION_CHANNEL_NAME_WECHAT = "WeChat";

    private static final String WECHAT_INTENT_KEY_ALERT = "alert";
    private static final String WECHAT_INTENT_KEY_BADGE = "badge";
    private static final String WECHAT_INTENT_KEY_FROM = "from";

    public WeChatPushNotificationHandler(Context context) {
        super(context);
    }

    public static boolean launchNotificationChannelSettingsForOreo(Context context,
                                                                   boolean android/*App layer or FW layer*/) {
        try {
            Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                    .putExtra(Settings.EXTRA_APP_PACKAGE, android ? "android" : context.getPackageName())
                    .putExtra(Settings.EXTRA_CHANNEL_ID, NOTIFICATION_CHANNEL_ID_WECHAT)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Example. Assets/wechat_intent_dump
    @Override
    public boolean handleIncomingIntent(String targetPackage, Intent intent) {
        if (!isEnabled()) {
            Logging.logV("WeChatPushNotificationHandler not enabled");
            return false;
        }
        Logging.logV("handleIncomingIntent:" + intent);
        if (!WECHAT_PKG_NAME.equals(targetPackage)) {
            return false;
        }

        boolean isRunning = false;
        try {
            isRunning = ThanosManagerNative.getDefault().getActivityManager().isPackageRunning(WECHAT_PKG_NAME);
        } catch (RemoteException e) {
            Logging.logE(Log.getStackTraceString(e));
        }

        if (isTargetPackageRunningOnTop()) {
            // Reset all when package is in front.
            Logging.logV("WeChatPushNotificationHandler target is running on top");
            clearBadge();
            return true;
        }

        if (isSkipIfRunningEnabled() && isRunning) {
            Logging.logV("WeChatPushNotificationHandler skip when app running");
            clearBadge();
            return true;
        }

        try {
            postNotification(resolveWeChatPushIntent(intent));

            if (isStartAppOnPushEnabled()) {
                if (!isRunning) {
                    // Defrost first.
                    ThanosManagerNative.getDefault().getPkgManager().setApplicationEnableState(
                            WECHAT_PKG_NAME,
                            true,
                            false);
                    ThanosManagerNative.getDefault().getActivityManager().addApp(WECHAT_PKG_NAME);
                }
            }
            return true;
        } catch (RemoteException e) {
            Logging.logE(Log.getStackTraceString(e));
            return false;
        }
    }

    @Override
    public void onNewNotification(NotificationRecord record) {
        if (!isEnabled()) {
            Logging.logV("onNewNotification WeChatPushNotificationHandler not enabled");
            return;
        }
        if (!WECHAT_PKG_NAME.equals(record.getPkgName())) {
            return;
        }
        clearBadge();
        cancelPendingCancelNotifications();
        Logging.logV("onNewNotification, clear and cancel.");
    }

    @Override
    public String getTargetPackageName() {
        return WECHAT_PKG_NAME;
    }

    private PushMessage resolveWeChatPushIntent(Intent intent) {
        try {

            // If this is a test.
            boolean isTestMessage = intent.hasExtra(KEY_MOCK_MESSAGE);
            if (isTestMessage) {
                return createAlertMessage("Thanox", intent.getStringExtra(KEY_MOCK_MESSAGE));
            }

            String from = intent.getStringExtra(WECHAT_INTENT_KEY_FROM);

            if (from == null) {
                return createDefaultPushMessage();
            }

            // Increase message count.
            updateBadge(from);

            if (!isShowContentEnabled()) {
                return createSecretPushMessage(from);
            }

            Logging.logW("ShowContentEnabled...");

            String alert = intent.getStringExtra(WECHAT_INTENT_KEY_ALERT);


            if (alert == null) {
                Logging.logW("alert == null...");
                return createSecretPushMessage(from);
            }

            return createAlertMessage(from, alert);
        } catch (Throwable e) {
            Logging.logW("Fail resolveWeChatPushIntent, use default: " + Log.getStackTraceString(e));
            return createDefaultPushMessage();
        }
    }

    private PushMessage createAlertMessage(String from, String alert) {
        return PushMessage.builder()
                .channelId(NOTIFICATION_CHANNEL_ID_WECHAT)
                .channelName(NOTIFICATION_CHANNEL_NAME_WECHAT)
                .smallIconResName(Res.Drawables.DRAWABLE_WECHAT_SMALL)
                .largeIconResName(Res.Drawables.DRAWABLE_WECHAT_LARGE)
                .title(String.format("%s条消息", getBadgeFrom(from)))
                .message(alert)
                // Diff sender with diff id.
                .from(MessageIdWrapper.id(from))
                .targetPackageName(getTargetPackageName())
                .build();
    }

    // No content.
    private PushMessage createSecretPushMessage(String from) {
        Logging.logW("createSecretPushMessage...");
        return PushMessage.builder()
                .channelId(NOTIFICATION_CHANNEL_ID_WECHAT)
                .channelName(NOTIFICATION_CHANNEL_NAME_WECHAT)
                .smallIconResName(Res.Drawables.DRAWABLE_WECHAT_SMALL)
                .largeIconResName(Res.Drawables.DRAWABLE_WECHAT_LARGE)
                .title("微信")
                .message(String.format("%s个联系人发来%s条消息", getFromCount(), getAllBadge()))
                .from(MessageIdWrapper.id(WECHAT_PKG_NAME)) // Do not split for diff sender...
                .targetPackageName(getTargetPackageName())
                .build();
    }


    // Some err occurred, we post this message.
    private PushMessage createDefaultPushMessage() {
        return PushMessage.builder()
                .channelId(NOTIFICATION_CHANNEL_ID_WECHAT)
                .channelName(NOTIFICATION_CHANNEL_NAME_WECHAT)
                .smallIconResName(Res.Drawables.DRAWABLE_WECHAT_SMALL)
                .largeIconResName(Res.Drawables.DRAWABLE_WECHAT_LARGE)
                .message("你收到了一条新消息")
                .title("微信")
                .from(MessageIdWrapper.id(WECHAT_PKG_NAME)) // Do not split for diff sender...
                .targetPackageName(getTargetPackageName())
                .build();
    }

    static class MessageIdWrapper {
        static final Map<String, Integer> idMap = new HashMap<>();

        static int id(String messageIdString) {
            Integer cache = idMap.get(messageIdString);
            if (cache != null) return cache;
            int idNew = 0;
            try {
                idNew = ThanosManagerNative.getDefault().getNotificationManager().nextNotificationId();
            } catch (RemoteException e) {
                Logging.logE(Log.getStackTraceString(e));
            }
            idMap.put(messageIdString, idNew);
            return idNew;
        }
    }
}
