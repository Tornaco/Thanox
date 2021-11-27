package github.tornaco.android.thanos.core;

import github.tornaco.android.thanos.core.os.IServiceManager;
import github.tornaco.android.thanos.core.pref.IPrefManager;
import github.tornaco.android.thanos.core.app.event.IEventSubscriber;
import github.tornaco.android.thanos.core.app.usage.IUsageStatsManager;
import github.tornaco.android.thanos.core.app.IActivityManager;
import github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor;
import github.tornaco.android.thanos.core.pm.IPkgManager;
import github.tornaco.android.thanos.core.secure.IPrivacyManager;
import github.tornaco.android.thanos.core.secure.ops.IAppOpsService;
import github.tornaco.android.thanos.core.push.IPushManager;
import github.tornaco.android.thanos.core.n.INotificationManager;
import github.tornaco.android.thanos.core.audio.IAudioManager;
import github.tornaco.android.thanos.core.profile.IProfileManager;
import github.tornaco.android.thanos.core.backup.IBackupAgent;
import github.tornaco.android.thanos.core.wm.IWindowManager;
import github.tornaco.android.thanos.core.power.IPowerManager;
import github.tornaco.android.thanos.core.input.IInputManager;
import github.tornaco.android.thanos.core.plus.IRS;
import github.tornaco.android.thanos.core.IPluginLogger;
import github.tornaco.android.thanos.core.app.infinite.InfiniteZ;

import android.content.IntentFilter;

// DO NOT CHANGE ORDER.
interface IThanos {

    IServiceManager getServiceManager();
    IPrefManager getPrefManager();
    IActivityManager getActivityManager();
    IPkgManager getPkgManager();
    IActivityStackSupervisor getActivityStackSupervisor();
    IPrivacyManager getPrivacyManager();
    IAppOpsService getAppOpsService();
    IPushManager getPushManager();
    INotificationManager getNotificationManager();
    IAudioManager getAudioManager();
    IProfileManager getProfileManager();
    IBackupAgent getBackupAgent();
    IWindowManager getWindowManager();
    IPowerManager getPowerManager();
    IInputManager getInputManager();

    void registerEventSubscriber(in IntentFilter filter, in IEventSubscriber subscriber);
    void unRegisterEventSubscriber(in IEventSubscriber subscriber);

    String fingerPrint();
    String getVersionName();

    String whoAreYou();

    boolean isLoggingEnabled();
    void setLoggingEnabled(boolean enable);

    boolean hasFeature(String feature);
    boolean hasFrameworkInitializeError();

    IPluginLogger getPluginLogger(String pluginAlias);

    InfiniteZ getInfiniteZ();

    String getPatchingSource();

    IRS getRS();

    IUsageStatsManager getUsageStatsManager();
}