package github.tornaco.android.thanos.core.app;

import github.tornaco.android.thanos.core.app.RunningServiceInfoCompat;
import github.tornaco.android.thanos.core.app.ActivityAssistInfo;
import android.content.pm.UserInfo;
import github.tornaco.android.thanos.core.IPrinter;
import github.tornaco.android.thanos.core.app.RunningAppProcessInfoCompat;
import github.tornaco.android.thanos.core.app.usage.ProcessCpuUsageStats;
import github.tornaco.android.thanos.core.os.SwapInfo;

interface IActivityManager {
    String getCurrentFrontApp();

    void forceStopPackage(in Pkg pkg, String reason);

    void idlePackage(in Pkg pkg);
    boolean isPackageIdle(in Pkg pkg);

    boolean checkBroadcastingIntent(in Intent intent);

    boolean checkService(in Intent intent, in ComponentName service, int callerUid, int userId);

    boolean checkRestartService(String packageName, in ComponentName componentName);

    boolean checkBroadcast(in Intent intent, int receiverUid, int callerUid);

    boolean checkStartProcess(String processName, in ApplicationInfo applicationInfo, String hostType, String hostName);

    void onStartProcessLocked(in ApplicationInfo appInfo);

    ProcessRecord[] getRunningAppProcess();
    List<Pkg> getRunningAppPackages();

    List<RunningServiceInfo> getRunningServiceLegacy(int max);
    List<RunningAppProcessInfoCompat> getRunningAppProcessLegacy();

    int getRunningAppsCount();

    List<ProcessRecord> getRunningAppProcessForPackage(in Pkg pkg);
    boolean isPackageRunning(in Pkg pkg);

    List<StartRecord> getStartRecordsByPackageName(String pkgName);

    List<String> getStartRecordBlockedPackages();
    long getStartRecordsBlockedCount();
    long getStartRecordBlockedCountByPackageName(String pkgName);

    // 启动管理设置
    boolean isStartBlockEnabled();
    void setStartBlockEnabled(boolean enable);
    void setPkgStartBlockEnabled(in Pkg pkg, boolean enable);
    boolean isPkgStartBlocking(in Pkg pkg);

    // Task removal
    boolean isCleanUpOnTaskRemovalEnabled();
    void setCleanUpOnTaskRemovalEnabled(boolean enable);
    void setPkgCleanUpOnTaskRemovalEnabled(in Pkg pkg, boolean enable);
    boolean isPkgCleanUpOnTaskRemovalEnabled(in Pkg pkg);

    // 后台运行设置
    boolean isBgRestrictEnabled();
    void setBgRestrictEnabled(boolean enable);
    void setPkgBgRestrictEnabled(in Pkg pkg, boolean enable);
    boolean isPkgBgRestricted(in Pkg pkg);

    void setBgRestrictNotificationEnabled(boolean enabled);
    boolean isBgRestrictNotificationEnabled();

    // Task blur
    boolean isRecentTaskBlurEnabled();
    void setRecentTaskBlurEnabled(boolean enable);
    void setPkgRecentTaskBlurEnabled(in Pkg pkg, boolean enable);
    boolean isPkgRecentTaskBlurEnabled(in Pkg pkg);

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
    void setPkgSmartStandByEnabled(in Pkg pkg, boolean enable);
    boolean isPkgSmartStandByEnabled(in Pkg pkg);

    List<Pkg> getLastRecentUsedPackages(int count);

    int getRecentTaskExcludeSettingForPackage(in Pkg pkg);
    void setRecentTaskExcludeSettingForPackage(in Pkg pkg, int setting);

    // Keep when has recent task.
    boolean isBgTaskCleanUpSkipWhenHasRecentTaskEnabled();
    void setBgTaskCleanUpSkipWhenHasRecentTaskEnabled(boolean enable);

    void launchAppDetailsActivity(String pkgName);

    void resetStartRecordsBlocked();

    void addApp(in Pkg pkg);

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

    RunningServiceInfoCompat[] getRunningAppServiceForPackage(String pkgName, int userId);
    boolean hasRunningServiceForPackage(String pkgName, int userId);

    UserInfo getUserInfo(int userHandle);

    boolean stopService(in Intent intent);
    boolean killBackgroundProcesses(in Pkg pkg);

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

    boolean checkGetContentProvider(String callerPkg, String name, int userId);

    List<StartRecord> getAllStartRecordsForPackageSetWithRes(String pkgSetId, boolean allowed, boolean blocked);

    // ******************************************************************
    // CAF API
    // https://source.android.com/devices/tech/perf/cached-apps-freezer
    //
    // ******************************************************************
    boolean isCachedAppsFreezerSupported();
    void freezeApp(in Pkg pkg);
    void unfreezeApp(in Pkg pkg);
    void freezeAppProcess(long pid);
    void unfreezeAppProcess(long pid);

    void updateProcessCpuUsageStats();
    List<ProcessCpuUsageStats> queryProcessCpuUsageStats(in long[] pids, boolean update);
    float queryCpuUsageRatio(in long[] pids, boolean update);


    boolean killProcess(long pid);

    SwapInfo getSwapInfo();

    // ******************************************************************
    // Block API
    // Block Receiver/Service/Provider all the time, event the package is running at foreground,
    // may cause the app crash.
    // ******************************************************************
    void setBlockAllReceiver(in Pkg pkg, boolean block);
    boolean isBlockAllReceiver(in Pkg pkg);

    void setBlockAllService(in Pkg pkg, boolean block);
    boolean isBlockAllService(in Pkg pkg);

    void setBlockAllProvider(in Pkg pkg, boolean block);
    boolean isBlockAllProvider(in Pkg pkg);

    // Return 0 if it fail
    long getProcessStartTime(int pid);

    boolean isAppForeground(in Pkg pkg);
    boolean hasRunningForegroundService(in Pkg pkg, int foregroundServicetype);

    List<ActivityAssistInfo> getTopVisibleActivities();

    boolean checkStartActivity(in Intent intent, int callerUid);

    boolean isBgTaskCleanUpSkipForegroundEnabled();
    void setBgTaskCleanUpSkipForegroundEnabled(boolean enable);

    /* return the pid of process. or -1 if no process found */
    int getPid(in ProcessName processName);
    /* return the pid of killed process. or -1 if no process found */
    int killProcessByName(in ProcessName processName);
    void killProcessByNames(in List<ProcessName> processNames);

    boolean dumpHeap(String process);

    Pkg getCurrentFrontPkg();

    boolean isSmartStandByByPassIfHasVisibleWindows();
    void setSmartStandByByPassIfHasVisibleWindowsEnabled(boolean enable);

    float getTotalCpuPercent(boolean update);

    List<PkgCpuUsageStats> getTopNCpuUsagePackages(int n, boolean update);

    boolean isPkgResident(in Pkg pkg);
    void setPkgResident(in Pkg pkg, boolean resident);

    void setOneKeyBoostSetting(String setting);
    String getOneKeyBoostSetting();
}