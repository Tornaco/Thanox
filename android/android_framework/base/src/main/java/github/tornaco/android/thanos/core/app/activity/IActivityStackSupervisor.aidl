package github.tornaco.android.thanos.core.app.activity;

import github.tornaco.android.thanos.core.app.activity.IVerifyCallback;
import github.tornaco.android.thanos.core.app.activity.ITopPackageChangeListener;
import github.tornaco.android.thanos.core.app.activity.IActivityLifecycleListener;
import github.tornaco.android.thanos.core.app.component.ComponentReplacement;
import github.tornaco.android.thanos.core.IPrinter;

interface IActivityStackSupervisor {
    /** @deprecated use {@link #replaceActivityStartingIntent} instead */
    boolean checkActivity(in ComponentName componentName, int userId, in IBinder resultTo);

    Intent replaceActivityStartingIntent(in Intent intent, int userId, in IBinder resultTo);

    boolean shouldVerifyActivityStarting(in ComponentName componentName, String pkg, String source);

    String getCurrentFrontApp();

    void setAppLockEnabled(boolean enabled);
    boolean isAppLockEnabled();

    boolean isPackageLocked(String pkg);
    void setPackageLocked(String pkg, boolean locked);

    void setVerifyResult(int request, int result, int reason);

    void addComponentReplacement(in ComponentReplacement replacement);
    void removeComponentReplacement(in ComponentReplacement replacement);

    List<ComponentReplacement> getComponentReplacements();

    void setActivityTrampolineEnabled(boolean enabled);
    boolean isActivityTrampolineEnabled();

    void setShowCurrentComponentViewEnabled(boolean enabled);
    boolean isShowCurrentComponentViewEnabled();

    void registerTopPackageChangeListener(in ITopPackageChangeListener listener);
    void unRegisterTopPackageChangeListener(in ITopPackageChangeListener listener);

    boolean isVerifyOnScreenOffEnabled();
    void setVerifyOnScreenOffEnabled(boolean enabled);

    boolean isVerifyOnAppSwitchEnabled();
    void setVerifyOnAppSwitchEnabled(boolean enabled);

    boolean isVerifyOnTaskRemovedEnabled();
    void setVerifyOnTaskRemovedEnabled(boolean enabled);

    // Bridge API to report app events.
    Intent reportOnStartActivity(String callingPackage, in Intent intent);
    void reportOnActivityStopped(in IBinder token);
    void reportOnActivityResumed(in IBinder token);

    void dump(in IPrinter p);

    void registerActivityLifecycleListener(in IActivityLifecycleListener listener);
    void unRegisterActivityLifecycleListener(in IActivityLifecycleListener listener);

    void addAppLockWhiteListComponents(in List<ComponentName> componentName);
    void removeAppLockWhiteListComponents(in List<ComponentName> componentName);
    List<ComponentName> getAppLockWhiteListComponents();

}