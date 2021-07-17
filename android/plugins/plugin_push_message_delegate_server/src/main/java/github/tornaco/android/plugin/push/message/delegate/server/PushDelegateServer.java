package github.tornaco.android.plugin.push.message.delegate.server;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import github.tornaco.android.plugin.push.message.delegate.IPushDelegateManager;
import github.tornaco.android.plugin.push.message.delegate.Logging;
import github.tornaco.android.thanos.core.app.ThanosManagerNative;
import github.tornaco.android.thanos.core.app.activity.ITopPackageChangeListener;
import github.tornaco.android.thanos.core.n.INotificationObserver;
import github.tornaco.android.thanos.core.n.NotificationRecord;
import github.tornaco.android.thanos.core.pref.IPrefChangeListener;
import github.tornaco.android.thanos.core.push.IChannelHandler;
import github.tornaco.android.thanos.core.push.PushChannel;
import github.tornaco.android.thanos.core.util.AbstractSafeR;
import github.tornaco.android.thanos.core.util.HandlerUtils;
import lombok.Getter;

public class PushDelegateServer extends IPushDelegateManager.Stub {

    private static final String PREF_KEY_CHANNEL_ENABLE_PREFIX = "plugin.push.message.delegate.server.channel_enabled_";
    private static final String PREF_KEY_CHANNEL_SHOW_CONTENT_ENABLE_PREFIX = "plugin.push.message.delegate.server.channel_show_content_enabled_";
    private static final String PREF_KEY_CHANNEL_N_SOUND_ENABLE_PREFIX = "plugin.push.message.delegate.server.channel_n_sound_enabled_";
    private static final String PREF_KEY_CHANNEL_N_VIBRATE_ENABLE_PREFIX = "plugin.push.message.delegate.server.channel_n_vibrate_enabled_";
    private static final String PREF_KEY_CHANNEL_START_APP_ON_PUSH_ENABLE_PREFIX = "plugin.push.message.delegate.server.channel_start_app_on_push_enabled_";
    private static final String PREF_KEY_CHANNEL_SKIP_IF_RUNNING_ENABLE_PREFIX = "plugin.push.message.delegate.server.channel_skip_if_running_enabled_";

    @Getter
    private Context context;
    private Handler serverHandler;

    private WeChatPushNotificationHandler weChatPushNotificationHandler;

    private final IPrefChangeListener prefChangeListener = new IPrefChangeListener.Stub() {
        @Override
        public void onPrefChanged(String key) {
            Logging.logD("PushDelegate onPrefChanged: " + key);
        }
    };

    private final ITopPackageChangeListener topPackageChangeListener = new ITopPackageChangeListener.Stub() {
        @Override
        public void onChange(String from, String to) {
            Logging.logD("PushDelegate topPackageChangeListener: " + to);
            if (serverHandler != null) serverHandler.post(new AbstractSafeR() {
                @Override
                public void runSafety() {
                    weChatPushNotificationHandler.onTopPackageChanged(to);
                }
            });
        }
    };

    private final IChannelHandler.Stub fcmHandler = new IChannelHandler.Stub() {
        @Override
        public void onMessageArrive(Intent intent) {
            Logging.logD("PushDelegate onMessageArrive: " + intent);
            if (intent == null) return;
            if (serverHandler != null) serverHandler.post(new AbstractSafeR() {
                @Override
                public void runSafety() {
                    final String targetPkg = intent.getPackage();
                    weChatPushNotificationHandler.handleIncomingIntent(targetPkg, intent);
                }
            });
        }
    };

    private final IChannelHandler.Stub miPushHandler = new IChannelHandler.Stub() {
        @Override
        public void onMessageArrive(Intent intent) {
            Logging.logD("PushDelegate onMessageArrive: " + intent);
        }
    };

    private final INotificationObserver notificationObserver = new INotificationObserver.Stub() {
        @Override
        public void onNewNotification(NotificationRecord record) {
            if (serverHandler != null) {
                serverHandler.post(new AbstractSafeR() {
                    @Override
                    public void runSafety() {
                        weChatPushNotificationHandler.onNewNotification(record);
                    }
                });
            }
        }

        @Override
        public void onNotificationRemoved(NotificationRecord record) {
            // Noop.
        }
    };

    public void start(Context context) {
        Logging.logW("PushDelegateServer start with context: " + context);
        this.context = context;
        this.serverHandler = HandlerUtils.newHandlerOfNewThread("push_delegate");
    }

    public void systemReady() {
        Logging.logW("PushDelegateServer systemReady with context: " + context);
        if (context == null) {
            Logging.logE("PushDelegate Context is null...");
            return;
        }
        if (serverHandler == null) {
            Logging.logE("PushDelegate serverHandler is null...");
            return;
        }
        publish();
        registerReceivers();
    }

    public void shutdown() {
    }

    private void publish() {
        try {
            ThanosManagerNative.getDefault().getServiceManager().addService("push_delegate", asBinder());
        } catch (RemoteException e) {
            Logging.logE(Log.getStackTraceString(e));
        }
    }

    private void registerReceivers() {
        try {
            weChatPushNotificationHandler = new WeChatPushNotificationHandler(getContext());
            weChatPushNotificationHandler.setEnabled(isPushMessageHandlerEnabled(WeChatPushNotificationHandler.WECHAT_PKG_NAME));
            weChatPushNotificationHandler.setNotificationSoundEnabled(isPushMessageHandlerNotificationSoundEnabled(WeChatPushNotificationHandler.WECHAT_PKG_NAME));
            weChatPushNotificationHandler.setNotificationVibrateEnabled(isPushMessageHandlerNotificationVibrateEnabled(WeChatPushNotificationHandler.WECHAT_PKG_NAME));
            weChatPushNotificationHandler.setShowContentEnabled(isPushMessageHandlerShowContentEnabled(WeChatPushNotificationHandler.WECHAT_PKG_NAME));
            weChatPushNotificationHandler.setStartAppOnPushEnabled(isStartAppOnPushEnabled(WeChatPushNotificationHandler.WECHAT_PKG_NAME));
            weChatPushNotificationHandler.setSkipIfRunningEnabled(isSkipIfAppRunningEnabled(WeChatPushNotificationHandler.WECHAT_PKG_NAME));

            ThanosManagerNative.getDefault().getPushManager().registerChannel(PushChannel.FCM_GCM);
            ThanosManagerNative.getDefault().getPushManager().registerChannelHandler(PushChannel.FCM_GCM.getChannelId(), fcmHandler);

            ThanosManagerNative.getDefault().getPushManager().registerChannel(PushChannel.MIPUSH);
            ThanosManagerNative.getDefault().getPushManager().registerChannelHandler(PushChannel.MIPUSH.getChannelId(), miPushHandler);

            ThanosManagerNative.getDefault().getPrefManager().registerSettingsChangeListener(prefChangeListener);
            ThanosManagerNative.getDefault().getActivityStackSupervisor().registerTopPackageChangeListener(topPackageChangeListener);

            ThanosManagerNative.getDefault().getNotificationManager().registerObserver(notificationObserver);
        } catch (RemoteException e) {
            Logging.logE(Log.getStackTraceString(e));
        }
    }

    private boolean isPushMessageHandlerEnabled(String pkg) {
        try {
            return ThanosManagerNative
                    .getDefault()
                    .getPrefManager()
                    .getBoolean(PREF_KEY_CHANNEL_ENABLE_PREFIX + pkg, false);
        } catch (RemoteException e) {
            Logging.logE(Log.getStackTraceString(e));
            return false;
        }
    }

    private boolean isPushMessageHandlerShowContentEnabled(String pkg) {
        try {
            return ThanosManagerNative
                    .getDefault()
                    .getPrefManager()
                    .getBoolean(PREF_KEY_CHANNEL_SHOW_CONTENT_ENABLE_PREFIX + pkg, false);
        } catch (RemoteException e) {
            Logging.logE(Log.getStackTraceString(e));
            return false;
        }
    }

    private boolean isPushMessageHandlerNotificationSoundEnabled(String pkg) {
        try {
            return ThanosManagerNative
                    .getDefault()
                    .getPrefManager()
                    .getBoolean(PREF_KEY_CHANNEL_N_SOUND_ENABLE_PREFIX + pkg, false);
        } catch (RemoteException e) {
            Logging.logE(Log.getStackTraceString(e));
            return false;
        }
    }

    private boolean isPushMessageHandlerNotificationVibrateEnabled(String pkg) {
        try {
            return ThanosManagerNative
                    .getDefault()
                    .getPrefManager()
                    .getBoolean(PREF_KEY_CHANNEL_N_VIBRATE_ENABLE_PREFIX + pkg, false);
        } catch (RemoteException e) {
            Logging.logE(Log.getStackTraceString(e));
            return false;
        }
    }

    private boolean isSkipIfAppRunningEnabled(String pkg) {
        try {
            return ThanosManagerNative
                    .getDefault()
                    .getPrefManager()
                    .getBoolean(PREF_KEY_CHANNEL_SKIP_IF_RUNNING_ENABLE_PREFIX + pkg, false);
        } catch (RemoteException e) {
            Logging.logE(Log.getStackTraceString(e));
            return false;
        }
    }

    private boolean isStartAppOnPushEnabled(String pkg) {
        try {
            return ThanosManagerNative
                    .getDefault()
                    .getPrefManager()
                    .getBoolean(PREF_KEY_CHANNEL_START_APP_ON_PUSH_ENABLE_PREFIX + pkg, false);
        } catch (RemoteException e) {
            Logging.logE(Log.getStackTraceString(e));
            return false;
        }
    }

    @Override
    public boolean wechatEnabled() {
        Logging.logV("wechatEnabled? " + weChatPushNotificationHandler.isEnabled());
        return weChatPushNotificationHandler.isEnabled();
    }

    @Override
    public void setWeChatEnabled(boolean enabled) {
        weChatPushNotificationHandler.setEnabled(enabled);
        try {
            ThanosManagerNative
                    .getDefault()
                    .getPrefManager()
                    .putBoolean(PREF_KEY_CHANNEL_ENABLE_PREFIX + WeChatPushNotificationHandler.WECHAT_PKG_NAME, enabled);
        } catch (RemoteException e) {
            Logging.logE(Log.getStackTraceString(e));
        }
    }

    @Override
    public boolean wechatSoundEnabled() {
        return weChatPushNotificationHandler.isNotificationSoundEnabled();
    }

    @Override
    public void setWechatSoundEnabled(boolean enabled) {
        weChatPushNotificationHandler.setNotificationSoundEnabled(enabled);
        try {
            ThanosManagerNative
                    .getDefault()
                    .getPrefManager()
                    .putBoolean(PREF_KEY_CHANNEL_N_SOUND_ENABLE_PREFIX + WeChatPushNotificationHandler.WECHAT_PKG_NAME, enabled);
        } catch (RemoteException e) {
            Logging.logE(Log.getStackTraceString(e));
        }
    }

    @Override
    public boolean wechatContentEnabled() {
        return weChatPushNotificationHandler.isShowContentEnabled();
    }

    @Override
    public void setWechatContentEnabled(boolean enabled) {
        weChatPushNotificationHandler.setShowContentEnabled(enabled);
        try {
            ThanosManagerNative
                    .getDefault()
                    .getPrefManager()
                    .putBoolean(PREF_KEY_CHANNEL_SHOW_CONTENT_ENABLE_PREFIX + WeChatPushNotificationHandler.WECHAT_PKG_NAME, enabled);
        } catch (RemoteException e) {
            Logging.logE(Log.getStackTraceString(e));
        }
    }

    @Override
    public boolean wechatVibrateEnabled() {
        return weChatPushNotificationHandler.isNotificationVibrateEnabled();
    }

    @Override
    public void setWechatVibrateEnabled(boolean enabled) {
        weChatPushNotificationHandler.setNotificationVibrateEnabled(enabled);
        try {
            ThanosManagerNative
                    .getDefault()
                    .getPrefManager()
                    .putBoolean(PREF_KEY_CHANNEL_N_VIBRATE_ENABLE_PREFIX + WeChatPushNotificationHandler.WECHAT_PKG_NAME, enabled);
        } catch (RemoteException e) {
            Logging.logE(Log.getStackTraceString(e));
        }
    }

    @Override
    public void mockWechatMessage() {
        serverHandler.post(new AbstractSafeR() {
            @Override
            public void runSafety() {
                Intent dummy = new Intent();
                dummy.putExtra(PushNotificationHandler.KEY_MOCK_MESSAGE, "Hello world!");
                weChatPushNotificationHandler.handleIncomingIntent(WeChatPushNotificationHandler.WECHAT_PKG_NAME, dummy);
            }
        });
    }

    @Override
    public boolean startWechatOnPushEnabled() {
        return weChatPushNotificationHandler.isStartAppOnPushEnabled();
    }

    @Override
    public void setStartWechatOnPushEnabled(boolean enabled) {
        weChatPushNotificationHandler.setStartAppOnPushEnabled(enabled);
        try {
            ThanosManagerNative
                    .getDefault()
                    .getPrefManager()
                    .putBoolean(PREF_KEY_CHANNEL_START_APP_ON_PUSH_ENABLE_PREFIX + WeChatPushNotificationHandler.WECHAT_PKG_NAME, enabled);
        } catch (RemoteException e) {
            Logging.logE(Log.getStackTraceString(e));
        }
    }

    @Override
    public boolean skipIfWeChatAppRunningEnabled() {
        return weChatPushNotificationHandler.isSkipIfRunningEnabled();
    }

    @Override
    public void setSkipIfWeChatAppRunningEnabled(boolean enabled) {
        weChatPushNotificationHandler.setSkipIfRunningEnabled(enabled);
        try {
            ThanosManagerNative
                    .getDefault()
                    .getPrefManager()
                    .putBoolean(PREF_KEY_CHANNEL_SKIP_IF_RUNNING_ENABLE_PREFIX + WeChatPushNotificationHandler.WECHAT_PKG_NAME, enabled);
        } catch (RemoteException e) {
            Logging.logE(Log.getStackTraceString(e));
        }
    }

    public boolean shouldHookBroadcastPerformResult() {
        return weChatPushNotificationHandler.isEnabled();
    }

    public int onHookBroadcastPerformResult(Intent intent, int resultCode) {
        if (intent == null) {
            return resultCode;
        }
        // Check if GCM intent.
        if (!PushMessageAppStateHelper.isPushIntent(intent)) {
            return resultCode;
        }
        // Check if PMH for this pkg is enabled.
        String pkgName = intent.getPackage();
        if (TextUtils.isEmpty(pkgName)) {
            return resultCode;
        }

        if (!isPushMessageHandlerEnabled(pkgName)) {
            return resultCode;
        }

        // Hooked!
        Logging.logW("onHookBroadcastPerformResult, returning OK for " + pkgName);
        return Activity.RESULT_OK;
    }
}
