package github.tornaco.android.thanos.core.app;

import github.tornaco.android.thanos.core.app.RunningServiceInfoCompat;
import android.content.pm.UserInfo;
import github.tornaco.android.thanos.core.IPrinter;

interface IActivityManager {
    String getCurrentFrontApp();

    void forceStopPackage(String packageName);

    void idlePackage(String packageName);
    boolean isPackageIdle(String packageName);

    boolean checkBroadcastingIntent(in Intent intent);

    boolean checkService(in Intent intent, in ComponentName service, int callerUid);

    boolean checkRestartService(String packageName, in ComponentName componentName);

    boolean checkBroadcast(in Intent intent, int receiverUid, int callerUid);

    boolean checkStartProcess(in ApplicationInfo applicationInfo, String hostType, String hostName);

    void onStartProcessLocked(in ApplicationInfo appInfo);

    ProcessRecord[] getRunningAppProcess();
    String[] getRunningAppPackages();

    List<RunningServiceInfo> getRunningServiceLegacy(int max);
    List<RunningAppProcessInfo> getRunningAppProcessLegacy();

    int getRunningAppsCount();

    ProcessRecord[] getRunningAppProcessForPackage(String pkgName);
    boolean isPackageRunning(String pkgName);

    List<StartRecord> getStartRecordsByPackageName(String pkgName);

    List<String> getStartRecordBlockedPackages();
    long getStartRecordsBlockedCount();
    long getStartRecordBlockedCountByPackageName(String pkgName);

    // 启动管理设置
    boolean isStartBlockEnabled();
    void setStartBlockEnabled(boolean enable);
    void setPkgStartBlockEnabled(String pkgName, boolean enable);
    boolean isPkgStartBlocking(String pkgName);

    // Task removal
    boolean isCleanUpOnTaskRemovalEnabled();
    void setCleanUpOnTaskRemovalEnabled(boolean enable);
    void setPkgCleanUpOnTaskRemovalEnabled(String pkgName, boolean enable);
    boolean isPkgCleanUpOnTaskRemovalEnabled(String pkgName);

    // 后台运行设置
    boolean isBgRestrictEnabled();
    void setBgRestrictEnabled(boolean enable);
    void setPkgBgRestrictEnabled(String pkgName, boolean enable);
    boolean isPkgBgRestricted(String pkgName);

    void setBgRestrictNotificationEnabled(boolean enabled);
    boolean isBgRestrictNotificationEnabled();

    // Task blur
    boolean isRecentTaskBlurEnabled();
    void setRecentTaskBlurEnabled(boolean enable);
    void setPkgRecentTaskBlurEnabled(String pkgName, boolean enable);
    boolean isPkgRecentTaskBlurEnabled(String pkgName);

    // Audio focused app.
    boolean isBgTaskCleanUpSkipAudioFocusedAppEnabled();
    void setBgTaskCleanUpSkipAudioFocusedAppEnabled(boolean enable);

    // Notification record app.
    boolean isBgTaskCleanUpSkipWhichHasNotificationEnabled();
    void setBgTaskCleanUpSkipWhichHasNotificationEnabled(boolean enable);

    // 后台运行锁屏清理延迟
    void setBgTaskCleanUpDelayTimeMills(long delayMills);
    long getBgTaskCleanUpDelayTimeMills();

    void notifyTaskCreated(int taskId, in ComponentName componentName);

    MemoryInfo getMemoryInfo();
    long[] getProcessPss(in int[] pids);

    void onApplicationCrashing(String eventType, String processName, in ProcessRecord process, String stackTrace);

    String getPackageNameForTaskId(int taskId);

    int isPlatformAppIdleEnabled();

    boolean isSmartStandByEnabled();
    void setSmartStandByEnabled(boolean enable);
    void setPkgSmartStandByEnabled(String pkgName, boolean enable);
    boolean isPkgSmartStandByEnabled(String pkgName);

    String[] getLastRecentUsedPackages(int count);

    int getRecentTaskExcludeSettingForPackage(String pkgName);
    void setRecentTaskExcludeSettingForPackage(String pkgName, int setting);

    // Keep when has recent task.
    boolean isBgTaskCleanUpSkipWhenHasRecentTaskEnabled();
    void setBgTaskCleanUpSkipWhenHasRecentTaskEnabled(boolean enable);

    void launchAppDetailsActivity(String pkgName);

    void resetStartRecordsBlocked();

    void addApp(String targetPkg);

    boolean isStartRuleEnabled();
    void setStartRuleEnabled(boolean enable);

    void addStartRule(String rule);
    void deleteStartRule(String rule);
    String[] getAllStartRules();

    boolean isStandbyRuleEnabled();
    void setStandbyRuleEnabled(boolean enable);

    void addStandbyRule(String rule);
    void deleteStandbyRule(String rule);
    String[] getAllStandbyRules();

    RunningServiceInfoCompat[] getRunningAppServiceForPackage(String pkgName);
    boolean hasRunningServiceForPackage(String pkgName);

    UserInfo getUserInfo(int userHandle);

    void stopService(in Intent intent);
    void killBackgroundProcesses(String packageName);

    boolean isSmartStandByStopServiceEnabled();
    void setSmartStandByStopServiceEnabled(boolean enable);

    boolean isSmartStandByInactiveEnabled();
    void setSmartStandByInactiveEnabled(boolean enable);

    boolean isSmartStandByByPassIfHasNotificationEnabled();
    void setSmartStandByByPassIfHasNotificationEnabled(boolean enable);

    boolean isSmartStandByBlockBgServiceStartEnabled();
    void setSmartStandByBlockBgServiceStartEnabled(boolean enable);

    List<String> getStartRecordAllowedPackages();
    long getStartRecordsAllowedCount();
    long getStartRecordAllowedCountByPackageName(String pkgName);

    List<StartRecord> getStartRecordsAllowedByPackageName(String pkgName);
    List<StartRecord> getStartRecordsBlockedByPackageName(String pkgName);

    void resetStartRecordsAllowed();

    List<StartRecord> getAllStartRecordsWithRes(int appFlags, boolean allowed, boolean blocked);
    List<StartRecord> getAllStartRecords(int appFlags);

    void dump(in IPrinter p);

    void dumpCpu(in IPrinter p);

    void setNetStatTrackerEnabled(boolean enabled);
    boolean isNetStatTrackerEnabled();

    boolean checkGetContentProvider(String callerPkg, String name);
}