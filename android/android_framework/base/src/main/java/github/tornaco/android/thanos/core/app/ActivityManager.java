package github.tornaco.android.thanos.core.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.UserInfo;
import android.os.IBinder;
import android.os.ServiceManager;
import android.os.UserHandle;

import com.elvishew.xlog.XLog;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import github.tornaco.android.thanos.core.app.start.StartRecord;
import github.tornaco.android.thanos.core.app.usage.ProcessCpuUsageStats;
import github.tornaco.android.thanos.core.os.ProcessName;
import github.tornaco.android.thanos.core.os.SwapInfo;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.pm.IPkgManager;
import github.tornaco.android.thanos.core.pm.Pkg;
import github.tornaco.android.thanos.core.process.ProcessRecord;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import util.Singleton;

@AllArgsConstructor
public class ActivityManager {
    private static final Singleton<android.app.IActivityManager> IActivityManagerSingleton =
            new Singleton<android.app.IActivityManager>() {
                @Override
                protected android.app.IActivityManager create() {
                    final IBinder b = ServiceManager.getService(Context.ACTIVITY_SERVICE);
                    final android.app.IActivityManager am = android.app.IActivityManager.Stub.asInterface(b);
                    return am;
                }
            };

    public static final class ExcludeRecentSetting {
        public static final int NONE = 0;
        public static final int INCLUDE = 1;
        public static final int EXCLUDE = -1;
    }

    private IActivityManager server;
    private IPkgManager pkg;

    public static android.app.IActivityManager getAndroidService() {
        return IActivityManagerSingleton.get();
    }

    @SneakyThrows
    public String getCurrentFrontApp() {
        return server.getCurrentFrontApp();
    }

    /**
     * @deprecated User {{@link #forceStopPackage(Pkg, String)} instead}
     */
    @SneakyThrows
    @Deprecated
    public void forceStopPackage(String packageName) {
        server.forceStopPackage(Pkg.systemUserPkg(packageName), "Not defined.");
    }

    /**
     * @deprecated User {{@link #forceStopPackage(Pkg, String)} instead}
     */
    @SneakyThrows
    @Deprecated
    public void forceStopPackage(Pkg pkg) {
        server.forceStopPackage(pkg, "Not defined.");
    }

    @SneakyThrows
    public void forceStopPackage(Pkg pkg, String reason) {
        server.forceStopPackage(pkg, reason);
    }

    /**
     * @deprecated User {{@link #idlePackage(Pkg)} instead}
     */
    @SneakyThrows
    @Deprecated
    public void idlePackage(String packageName) {
        server.idlePackage(Pkg.systemUserPkg(packageName));
    }

    @SneakyThrows
    public void idlePackage(Pkg pkg) {
        server.idlePackage(pkg);
    }

    @SneakyThrows
    public ProcessRecord[] getRunningAppProcess() {
        return server.getRunningAppProcess();
    }

    @SneakyThrows
    public List<Pkg> getRunningAppPackages() {
        return server.getRunningAppPackages();
    }

    @SneakyThrows
    public List<android.app.ActivityManager.RunningServiceInfo> getRunningServiceLegacy(int max) {
        return server.getRunningServiceLegacy(max);
    }

    @SneakyThrows
    public List<RunningAppProcessInfoCompat> getRunningAppProcessLegacy() {
        return server.getRunningAppProcessLegacy();
    }

    @SneakyThrows
    public int getRunningAppsCount() {
        return server.getRunningAppsCount();
    }

    @SneakyThrows
    public List<ProcessRecord> getRunningAppProcessForPackage(Pkg pkg) {
        return server.getRunningAppProcessForPackage(pkg);
    }

    @SneakyThrows
    public List<ProcessRecord> getRunningAppProcessForPackage(String pkgName) {
        return server.getRunningAppProcessForPackage(Pkg.systemUserPkg(pkgName));
    }

    @SneakyThrows
    public List<ProcessRecord> getRunningAppProcessForPackage(String pkgName, int userId) {
        return server.getRunningAppProcessForPackage(new Pkg(pkgName, userId));
    }

    /**
     * @deprecated Use {@link #isPackageRunning(Pkg)} instead
     */
    @SneakyThrows
    @Deprecated
    public boolean isPackageRunning(String pkgName) {
        return server.isPackageRunning(Pkg.systemUserPkg(pkgName));
    }

    @SneakyThrows
    public boolean isPackageRunning(Pkg pkg) {
        return server.isPackageRunning(pkg);
    }

    @SneakyThrows
    public List<StartRecord> getStartRecordsByPackageName(String pkgName) {
        return server.getStartRecordsByPackageName(pkgName);
    }

    @SneakyThrows
    public List<String> getStartRecordBlockedPackages() {
        return server.getStartRecordBlockedPackages();
    }

    @SneakyThrows
    public void resetStartRecordsBlocked() {
        server.resetStartRecordsBlocked();
    }

    @SneakyThrows
    public void resetStartRecordsAllowed() {
        server.resetStartRecordsAllowed();
    }

    @SneakyThrows
    public long getStartRecordBlockedCountByPackageName(String pkgName) {
        return server.getStartRecordBlockedCountByPackageName(pkgName);
    }

    @SneakyThrows
    public long getStartRecordsBlockedCount() {
        return server.getStartRecordsBlockedCount();
    }

    @SneakyThrows
    public List<String> getStartRecordAllowedPackages() {
        return server.getStartRecordAllowedPackages();
    }

    @SneakyThrows
    public long getStartRecordsAllowedCount() {
        return server.getStartRecordsAllowedCount();
    }

    @SneakyThrows
    public long getStartRecordAllowedCountByPackageName(String pkgName) {
        return server.getStartRecordAllowedCountByPackageName(pkgName);
    }

    @SneakyThrows
    public List<StartRecord> getStartRecordsAllowedByPackageName(String pkgName) {
        return server.getStartRecordsAllowedByPackageName(pkgName);
    }

    @SneakyThrows
    public List<StartRecord> getStartRecordsBlockedByPackageName(String pkgName) {
        return server.getStartRecordsBlockedByPackageName(pkgName);
    }

    @SneakyThrows
    public List<StartRecord> getAllStartRecords(int appFlags) {
        return server.getAllStartRecords(appFlags);
    }

    @SneakyThrows
    public List<StartRecord> getAllStartRecordsWithRes(
            int appFlags, boolean allowed, boolean blocked) {
        return server.getAllStartRecordsWithRes(appFlags, allowed, blocked);
    }

    @SneakyThrows
    public List<StartRecord> getAllStartRecordsForPackageSetWithRes(String pkgSetId, boolean allowed, boolean blocked) {
        return server.getAllStartRecordsForPackageSetWithRes(pkgSetId, allowed, blocked);
    }

    /**
     * @deprecated Use {{@link #setPkgStartBlockEnabled(Pkg, boolean)}} instead
     */
    @SneakyThrows
    @Deprecated
    public void setPkgStartBlockEnabled(String pkgName, boolean enable) {
        server.setPkgStartBlockEnabled(Pkg.systemUserPkg(pkgName), enable);
    }

    @SneakyThrows
    public void setPkgStartBlockEnabled(Pkg pkg, boolean enable) {
        server.setPkgStartBlockEnabled(pkg, enable);
    }

    /**
     * @deprecated Use {{@link #isPkgStartBlocking(Pkg)}} instead
     */
    @SneakyThrows
    @Deprecated
    public boolean isPkgStartBlocking(String pkgName) {
        return server.isPkgStartBlocking(Pkg.systemUserPkg(pkgName));
    }

    @SneakyThrows
    public boolean isPkgStartBlocking(Pkg pkg) {
        return server.isPkgStartBlocking(pkg);
    }

    @SneakyThrows
    @Deprecated
    public void setPkgBgRestrictEnabled(String pkgName, boolean enable) {
        server.setPkgBgRestrictEnabled(Pkg.systemUserPkg(pkgName), enable);
    }

    @SneakyThrows
    public void setPkgBgRestrictEnabled(Pkg pkg, boolean enable) {
        server.setPkgBgRestrictEnabled(pkg, enable);
    }

    @SneakyThrows
    public void setPkgBgRestrictEnabled(AppInfo appInfo, boolean enable) {
        server.setPkgBgRestrictEnabled(Pkg.fromAppInfo(appInfo), enable);
    }

    @SneakyThrows
    @Deprecated
    public boolean isPkgBgRestricted(String pkgName) {
        return server.isPkgBgRestricted(Pkg.systemUserPkg(pkgName));
    }

    @SneakyThrows
    public boolean isPkgBgRestricted(Pkg pkg) {
        return server.isPkgBgRestricted(pkg);
    }

    @SneakyThrows
    public boolean isPkgBgRestricted(AppInfo appInfo) {
        return server.isPkgBgRestricted(Pkg.fromAppInfo(appInfo));
    }

    @SneakyThrows
    public void setBgTaskCleanUpDelayTimeMills(long delayMills) {
        server.setBgTaskCleanUpDelayTimeMills(delayMills);
    }

    @SneakyThrows
    public long getBgTaskCleanUpDelayTimeMills() {
        return server.getBgTaskCleanUpDelayTimeMills();
    }

    @SneakyThrows
    public void notifyTaskCreated(int taskId, ComponentName componentName) {
        server.notifyTaskCreated(taskId, componentName);
    }

    @SneakyThrows
    public android.app.ActivityManager.MemoryInfo getMemoryInfo() {
        return server.getMemoryInfo();
    }

    @SneakyThrows
    public long[] getProcessPss(int[] pids) {
        return server.getProcessPss(pids);
    }

    @SneakyThrows
    public boolean isStartBlockEnabled() {
        return server.isStartBlockEnabled();
    }

    @SneakyThrows
    public void setStartBlockEnabled(boolean enable) {
        server.setStartBlockEnabled(enable);
    }

    @SneakyThrows
    public boolean isBgRestrictEnabled() {
        return server.isBgRestrictEnabled();
    }

    @SneakyThrows
    public void setBgRestrictEnabled(boolean enable) {
        server.setBgRestrictEnabled(enable);
    }

    @SneakyThrows
    public boolean isCleanUpOnTaskRemovalEnabled() {
        return server.isCleanUpOnTaskRemovalEnabled();
    }

    @SneakyThrows
    public void setCleanUpOnTaskRemovalEnabled(boolean enable) {
        server.setCleanUpOnTaskRemovalEnabled(enable);
    }

    @SneakyThrows
    @Deprecated
    public void setPkgCleanUpOnTaskRemovalEnabled(String pkgName, boolean enable) {
        server.setPkgCleanUpOnTaskRemovalEnabled(Pkg.systemUserPkg(pkgName), enable);
    }

    @SneakyThrows
    public void setPkgCleanUpOnTaskRemovalEnabled(Pkg pkg, boolean enable) {
        server.setPkgCleanUpOnTaskRemovalEnabled(pkg, enable);
    }

    @SneakyThrows
    @Deprecated
    public boolean isPkgCleanUpOnTaskRemovalEnabled(String pkgName) {
        return server.isPkgCleanUpOnTaskRemovalEnabled(Pkg.systemUserPkg(pkgName));
    }

    @SneakyThrows
    public boolean isPkgCleanUpOnTaskRemovalEnabled(Pkg pkg) {
        return server.isPkgCleanUpOnTaskRemovalEnabled(pkg);
    }

    @SneakyThrows
    public boolean isBgTaskCleanUpSkipForegroundEnabled() {
        return server.isBgTaskCleanUpSkipForegroundEnabled();
    }

    @SneakyThrows
    public void setBgTaskCleanUpSkipForegroundEnabled(boolean enable) {
        server.setBgTaskCleanUpSkipForegroundEnabled(enable);
    }

    @SneakyThrows
    public boolean isBgTaskCleanUpSkipAudioFocusedAppEnabled() {
        return server.isBgTaskCleanUpSkipAudioFocusedAppEnabled();
    }

    @SneakyThrows
    public void setBgTaskCleanUpSkipAudioFocusedAppEnabled(boolean enable) {
        server.setBgTaskCleanUpSkipAudioFocusedAppEnabled(enable);
    }

    @SneakyThrows
    public boolean isBgTaskCleanUpSkipWhichHasNotificationEnabled() {
        return server.isBgTaskCleanUpSkipWhichHasNotificationEnabled();
    }

    @SneakyThrows
    public void setBgTaskCleanUpSkipWhichHasNotificationEnabled(boolean enable) {
        server.setBgTaskCleanUpSkipWhichHasNotificationEnabled(enable);
    }

    @SneakyThrows
    public boolean isRecentTaskBlurEnabled() {
        return server.isRecentTaskBlurEnabled();
    }

    @SneakyThrows
    public void setRecentTaskBlurEnabled(boolean enable) {
        server.setRecentTaskBlurEnabled(enable);
    }

    @SneakyThrows
    @Deprecated
    public void setPkgRecentTaskBlurEnabled(String pkgName, boolean enable) {
        server.setPkgRecentTaskBlurEnabled(Pkg.systemUserPkg(pkgName), enable);
    }

    @SneakyThrows
    public void setPkgRecentTaskBlurEnabled(Pkg pkg, boolean enable) {
        server.setPkgRecentTaskBlurEnabled(pkg, enable);
    }

    @SneakyThrows
    @Deprecated
    public boolean isPkgRecentTaskBlurEnabled(String pkgName) {
        return server.isPkgRecentTaskBlurEnabled(Pkg.systemUserPkg(pkgName));
    }

    @SneakyThrows
    public boolean isPkgRecentTaskBlurEnabled(Pkg pkg) {
        return server.isPkgRecentTaskBlurEnabled(pkg);
    }

    @SneakyThrows
    @Deprecated
    /**
     * @deprecated User {{@link #isPackageIdle(Pkg)} instead}
     */
    public boolean isPackageIdle(String packageName) {
        return server.isPackageIdle(Pkg.systemUserPkg(packageName));
    }

    @SneakyThrows
    public boolean isPackageIdle(Pkg pkg) {
        return server.isPackageIdle(pkg);
    }

    @SneakyThrows
    public int isPlatformAppIdleEnabled() {
        return server.isPlatformAppIdleEnabled();
    }

    @SneakyThrows
    public boolean isSmartStandByEnabled() {
        return server.isSmartStandByEnabled();
    }

    @SneakyThrows
    public void setSmartStandByEnabled(boolean enable) {
        server.setSmartStandByEnabled(enable);
    }

    @SneakyThrows
    @Deprecated
    /**
     * @deprecated Use {{@link #setPkgSmartStandByEnabled(Pkg, boolean)} instead}
     */
    public void setPkgSmartStandByEnabled(String pkgName, boolean enable) {
        server.setPkgSmartStandByEnabled(Pkg.systemUserPkg(pkgName), enable);
    }

    @SneakyThrows
    public void setPkgSmartStandByEnabled(Pkg pkg, boolean enable) {
        server.setPkgSmartStandByEnabled(pkg, enable);
    }

    /**
     * @deprecated Use {{@link #isPkgSmartStandByEnabled(Pkg)} instead}
     */
    @SneakyThrows
    public boolean isPkgSmartStandByEnabled(String pkgName) {
        return server.isPkgSmartStandByEnabled(Pkg.systemUserPkg(pkgName));
    }

    @SneakyThrows
    public boolean isPkgSmartStandByEnabled(Pkg pkg) {
        return server.isPkgSmartStandByEnabled(pkg);
    }

    @SneakyThrows
    public List<Pkg> getLastRecentUsedPackages(int count) {
        return server.getLastRecentUsedPackages(count);
    }

    @SneakyThrows
    @Deprecated
    public int getRecentTaskExcludeSettingForPackage(String pkgName) {
        return server.getRecentTaskExcludeSettingForPackage(Pkg.systemUserPkg(pkgName));
    }

    @SneakyThrows
    public int getRecentTaskExcludeSettingForPackage(Pkg pkg) {
        return server.getRecentTaskExcludeSettingForPackage(pkg);
    }

    @SneakyThrows
    @Deprecated
    public void setRecentTaskExcludeSettingForPackage(String pkgName, int setting) {
        server.setRecentTaskExcludeSettingForPackage(Pkg.systemUserPkg(pkgName), setting);
    }

    @SneakyThrows
    public void setRecentTaskExcludeSettingForPackage(Pkg pkg, int setting) {
        server.setRecentTaskExcludeSettingForPackage(pkg, setting);
    }

    @SneakyThrows
    public boolean isBgTaskCleanUpSkipWhenHasRecentTaskEnabled() {
        return server.isBgTaskCleanUpSkipWhenHasRecentTaskEnabled();
    }

    @SneakyThrows
    public void setBgTaskCleanUpSkipWhenHasRecentTaskEnabled(boolean enable) {
        server.setBgTaskCleanUpSkipWhenHasRecentTaskEnabled(enable);
    }

    @SneakyThrows
    public void launchAppDetailsActivity(String pkgName) {
        server.launchAppDetailsActivity(pkgName);
    }

    @SneakyThrows
    public void setBgRestrictNotificationEnabled(boolean enabled) {
        server.setBgRestrictNotificationEnabled(enabled);
    }

    @SneakyThrows
    public boolean isBgRestrictNotificationEnabled() {
        return server.isBgRestrictNotificationEnabled();
    }

    /**
     * @deprecated User {{@link #addApp(Pkg)} instead}
     */
    @SneakyThrows
    @Deprecated
    public void addApp(String targetPkg) {
        server.addApp(Pkg.systemUserPkg(targetPkg));
    }

    @SneakyThrows
    public void addApp(Pkg pkg) {
        server.addApp(pkg);
    }

    @SneakyThrows
    public boolean isStartRuleEnabled() {
        return server.isStartRuleEnabled();
    }

    @SneakyThrows
    public void setStartRuleEnabled(boolean enable) {
        server.setStartRuleEnabled(enable);
    }

    @SneakyThrows
    public void addStartRule(String rule) {
        server.addStartRule(rule);
    }

    @SneakyThrows
    public void deleteStartRule(String rule) {
        server.deleteStartRule(rule);
    }

    @SneakyThrows
    public String[] getAllStartRules() {
        return server.getAllStartRules();
    }

    @SneakyThrows
    public void addStandbyRule(String rule) {
        server.addStandbyRule(rule);
    }

    @SneakyThrows
    public void deleteStandbyRule(String rule) {
        server.deleteStandbyRule(rule);
    }

    @SneakyThrows
    public String[] getAllStandbyRules() {
        return server.getAllStandbyRules();
    }

    @SneakyThrows
    public boolean isStandbyRuleEnabled() {
        return server.isStandbyRuleEnabled();
    }

    @SneakyThrows
    public void setStandbyRuleEnabled(boolean enable) {
        server.setStandbyRuleEnabled(enable);
    }

    @SneakyThrows
    public RunningServiceInfoCompat[] getRunningAppServiceForPackage(String pkgName) {
        return server.getRunningAppServiceForPackage(pkgName);
    }

    @SneakyThrows
    public boolean hasRunningServiceForPackage(String pkgName) {
        return server.hasRunningServiceForPackage(pkgName);
    }

    @SneakyThrows
    public UserInfo getUserInfo(int userHandle) {
        return server.getUserInfo(userHandle);
    }

    @SneakyThrows
    public boolean stopService(Intent intent) {
        return server.stopService(intent);
    }

    @SneakyThrows
    public void killBackgroundProcesses(String packageName) {
        server.killBackgroundProcesses(Pkg.systemUserPkg(packageName));
    }

    @SneakyThrows
    public boolean killProcess(long pid) {
        return server.killProcess(pid);
    }

    /**
     * @deprecated User {{@link #getPid(ProcessName)} instead}
     */
    @SneakyThrows
    @Deprecated
    public int getPid(String processName) {
        return server.getPid(ProcessName.systemUserProcess(processName));
    }

    @SneakyThrows
    public int getPid(ProcessName processName) {
        return server.getPid(processName);
    }

    /**
     * @deprecated User {{@link #killProcessByName(ProcessName)} instead}
     */
    @SneakyThrows
    @Deprecated
    public int killProcessByName(String processName) {
        return server.killProcessByName(ProcessName.systemUserProcess(processName));
    }

    @SneakyThrows
    public int killProcessByName(ProcessName processName) {
        return server.killProcessByName(processName);
    }

    /**
     * @deprecated User {{@link #killProcessByNames2(List<ProcessName>)} instead}
     */
    @SneakyThrows
    @Deprecated
    public void killProcessByNames(List<String> processNames) {
        server.killProcessByNames(processNames.stream().map(ProcessName::systemUserProcess).collect(Collectors.toList()));
    }

    @SneakyThrows
    public void killProcessByNames2(List<ProcessName> processNames) {
        server.killProcessByNames(processNames);
    }

    @SneakyThrows
    public String getPackageNameForTaskId(int taskId) {
        return server.getPackageNameForTaskId(taskId);
    }

    @SneakyThrows
    public boolean isSmartStandByStopServiceEnabled() {
        return server.isSmartStandByStopServiceEnabled();
    }

    @SneakyThrows
    public void setSmartStandByStopServiceEnabled(boolean enable) {
        server.setSmartStandByStopServiceEnabled(enable);
    }

    @SneakyThrows
    public boolean isSmartStandByInactiveEnabled() {
        return server.isSmartStandByInactiveEnabled();
    }

    @SneakyThrows
    public void setSmartStandByInactiveEnabled(boolean enable) {
        server.setSmartStandByInactiveEnabled(enable);
    }

    @SneakyThrows
    public boolean isSmartStandByByPassIfHasNotificationEnabled() {
        return server.isSmartStandByByPassIfHasNotificationEnabled();
    }

    @SneakyThrows
    public void setSmartStandByByPassIfHasNotificationEnabled(boolean enable) {
        server.setSmartStandByByPassIfHasNotificationEnabled(enable);
    }

    @SneakyThrows
    public boolean isSmartStandByByPassIfHasVisibleWindows() {
        return server.isSmartStandByByPassIfHasVisibleWindows();
    }

    @SneakyThrows
    public void setSmartStandByByPassIfHasVisibleWindowsEnabled(boolean enable) {
        server.setSmartStandByByPassIfHasVisibleWindowsEnabled(enable);
    }

    @SneakyThrows
    public boolean isSmartStandByBlockBgServiceStartEnabled() {
        return server.isSmartStandByBlockBgServiceStartEnabled();
    }

    @SneakyThrows
    public void setSmartStandByBlockBgServiceStartEnabled(boolean enable) {
        server.setSmartStandByBlockBgServiceStartEnabled(enable);
    }

    @SneakyThrows
    public void setNetStatTrackerEnabled(boolean enabled) {
        server.setNetStatTrackerEnabled(enabled);
    }

    @SneakyThrows
    public boolean isNetStatTrackerEnabled() {
        return server.isNetStatTrackerEnabled();
    }


    // ******************************************************************
    // CAF API
    // https://source.android.com/devices/tech/perf/cached-apps-freezer
    //
    // ******************************************************************

    @SneakyThrows
    public boolean isCachedAppsFreezerSupported() {
        return server.isCachedAppsFreezerSupported();
    }

    @SneakyThrows
    public void freezeApp(String pkgName) {
        server.freezeApp(Pkg.systemUserPkg(pkgName));
    }

    @SneakyThrows
    public void freezeApp(Pkg pkg) {
        server.freezeApp(pkg);
    }

    @SneakyThrows
    public void freezeAppProcess(long pid) {
        server.freezeAppProcess(pid);
    }

    @SneakyThrows
    public void unfreezeApp(Pkg pkg) {
        server.unfreezeApp(pkg);
    }

    @SneakyThrows
    public void unfreezeApp(String pkgName) {
        server.unfreezeApp(Pkg.systemUserPkg(pkgName));
    }

    @SneakyThrows
    public void unfreezeAppProcess(long pid) {
        server.unfreezeAppProcess(pid);
    }

    @SneakyThrows
    public void updateProcessCpuUsageStats() {
        server.updateProcessCpuUsageStats();
    }

    @SneakyThrows
    public float queryCpuUsageRatio(long[] pids, boolean update) {
        return server.queryCpuUsageRatio(pids, update);
    }

    @SneakyThrows
    public List<ProcessCpuUsageStats> queryProcessCpuUsageStats(long[] pids, boolean update) {
        return server.queryProcessCpuUsageStats(pids, update);
    }

    @SneakyThrows
    public SwapInfo getSwapInfo() {
        return server.getSwapInfo();
    }

    // ******************************************************************
    // Block API
    //
    // ******************************************************************
    @SneakyThrows
    public void setBlockAllReceiver(Pkg pkg, boolean block) {
        server.setBlockAllReceiver(pkg, block);
    }

    @SneakyThrows
    public boolean isBlockAllReceiver(Pkg pkg) {
        return server.isBlockAllReceiver(pkg);
    }

    @SneakyThrows
    public void setBlockAllReceiver(String pkgName, boolean block) {
        setBlockAllReceiver(Pkg.currentUserPkg(pkgName), block);
    }

    @SneakyThrows
    public boolean isBlockAllReceiver(String pkgName) {
        return isBlockAllReceiver(Pkg.currentUserPkg(pkgName));
    }

    @SneakyThrows
    public void setBlockAllService(Pkg pkg, boolean block) {
        server.setBlockAllService(pkg, block);
    }

    @SneakyThrows
    public boolean isBlockAllService(Pkg pkg) {
        return server.isBlockAllService(pkg);
    }

    @SneakyThrows
    public void setBlockAllService(String pkgName, boolean block) {
        setBlockAllService(Pkg.currentUserPkg(pkgName), block);
    }

    @SneakyThrows
    public boolean isBlockAllService(String pkgName) {
        return isBlockAllService(Pkg.currentUserPkg(pkgName));
    }

    @SneakyThrows
    public void setBlockAllProvider(Pkg pkg, boolean block) {
        server.setBlockAllProvider(pkg, block);
    }

    @SneakyThrows
    public boolean isBlockAllProvider(Pkg pkg) {
        return server.isBlockAllProvider(pkg);
    }

    @SneakyThrows
    public void setBlockAllProvider(String pkgName, boolean block) {
        setBlockAllProvider(Pkg.currentUserPkg(pkgName), block);
    }

    @SneakyThrows
    public boolean isBlockAllProvider(String pkgName) {
        return isBlockAllProvider(Pkg.currentUserPkg(pkgName));
    }

    @SneakyThrows
    public long getProcessStartTime(int pid) {
        return server.getProcessStartTime(pid);
    }

    @SneakyThrows
    public boolean isAppForeground(Pkg pkg) {
        return server.isAppForeground(pkg);
    }

    @SneakyThrows
    public boolean isAppForeground(String pkgName) {
        return server.isAppForeground(Pkg.currentUserPkg(pkgName));
    }

    @SneakyThrows
    public List<AppInfo> getAllForegroundApps() {
        List<AppInfo> res = new ArrayList<>();
        for (Pkg runningPkg : server.getRunningAppPackages()) {
            if (isAppForeground(runningPkg)) {
                AppInfo appInfo = pkg.getAppInfoForUser(runningPkg.getPkgName(), runningPkg.getUserId());
                if (appInfo != null) {
                    res.add(appInfo);
                }
            }
        }
        return res;
    }

    @SneakyThrows
    public List<Pkg> getAllForegroundPkgs() {
        List<Pkg> res = new ArrayList<>();
        for (Pkg runningPkg : server.getRunningAppPackages()) {
            if (isAppForeground(runningPkg)) {
                res.add(runningPkg);
            }
        }
        return res;
    }

    /**
     * Returns the top activity from each of the currently visible root tasks, and the related uid.
     * The first entry will be the focused activity.
     * <p>
     * https://cs.android.com/android/platform/superproject/+/master:frameworks/base/services/core/java/com/android/server/wm/ActivityTaskManagerInternal.java
     */
    @SneakyThrows
    public List<ActivityAssistInfo> getTopVisibleActivities() {
        return server.getTopVisibleActivities();
    }

    @SneakyThrows
    public boolean hasTopVisibleActivities(String pkgName) {
        return getTopVisibleActivities()
                .stream()
                .anyMatch(activityAssistInfo -> activityAssistInfo.name.getPackageName().equals(pkgName));
    }

    @SneakyThrows
    public boolean hasTopVisibleActivities(Pkg pkg) {
        return getTopVisibleActivities()
                .stream()
                .anyMatch(activityAssistInfo -> activityAssistInfo.name.getPackageName().equals(pkg.getPkgName())
                        && UserHandle.getUserId(activityAssistInfo.uid) == pkg.getUserId());
    }

    @SneakyThrows
    public boolean dumpHeap(String process) {
        return server.dumpHeap(process);
    }

    public IBinder asBinder() {
        return server.asBinder();
    }

    public static String procStateToString(int procState) {
        XLog.w("procStateToString: " + procState);
        final String procStateStr;
        switch (procState) {
            case ProcessStateEnum.PROCESS_STATE_PERSISTENT:
                procStateStr = "PER";
                break;
            case ProcessStateEnum.PROCESS_STATE_PERSISTENT_UI:
                procStateStr = "PERU";
                break;
            case ProcessStateEnum.PROCESS_STATE_TOP:
                procStateStr = "TOP";
                break;
            case ProcessStateEnum.PROCESS_STATE_BOUND_TOP:
                procStateStr = "BTOP";
                break;
            case ProcessStateEnum.PROCESS_STATE_FOREGROUND_SERVICE:
                procStateStr = "FGS";
                break;
            case ProcessStateEnum.PROCESS_STATE_BOUND_FOREGROUND_SERVICE:
                procStateStr = "BFGS";
                break;
            case ProcessStateEnum.PROCESS_STATE_IMPORTANT_FOREGROUND:
                procStateStr = "IMPF";
                break;
            case ProcessStateEnum.PROCESS_STATE_IMPORTANT_BACKGROUND:
                procStateStr = "IMPB";
                break;
            case ProcessStateEnum.PROCESS_STATE_TRANSIENT_BACKGROUND:
                procStateStr = "TRNB";
                break;
            case ProcessStateEnum.PROCESS_STATE_BACKUP:
                procStateStr = "BKUP";
                break;
            case ProcessStateEnum.PROCESS_STATE_SERVICE:
                procStateStr = "SVC";
                break;
            case ProcessStateEnum.PROCESS_STATE_RECEIVER:
                procStateStr = "RCVR";
                break;
            case ProcessStateEnum.PROCESS_STATE_TOP_SLEEPING:
                procStateStr = "TPSL";
                break;
            case ProcessStateEnum.PROCESS_STATE_HEAVY_WEIGHT:
                procStateStr = "HVY ";
                break;
            case ProcessStateEnum.PROCESS_STATE_HOME:
                procStateStr = "HOME";
                break;
            case ProcessStateEnum.PROCESS_STATE_LAST_ACTIVITY:
                procStateStr = "LAST";
                break;
            case ProcessStateEnum.PROCESS_STATE_CACHED_ACTIVITY:
                procStateStr = "CAC";
                break;
            case ProcessStateEnum.PROCESS_STATE_CACHED_ACTIVITY_CLIENT:
                procStateStr = "CACC";
                break;
            case ProcessStateEnum.PROCESS_STATE_CACHED_RECENT:
                procStateStr = "CRE ";
                break;
            case ProcessStateEnum.PROCESS_STATE_CACHED_EMPTY:
                procStateStr = "CEM";
                break;
            case ProcessStateEnum.PROCESS_STATE_NONEXISTENT:
                procStateStr = "NONE";
                break;
            default:
                procStateStr = "??";
                break;
        }
        return procStateStr;
    }

    interface ProcessStateEnum {
        // Unlike the ActivityManager PROCESS_STATE values, the ordering and numerical values
        // here are completely fixed and arbitrary. Order is irrelevant.
        // No attempt need be made to keep them in sync.
        // The values here must not be modified. Any new process states can be appended to the end.

        // Process state that is unknown to this proto file (i.e. is not mapped
        // by ActivityManager.processStateAmToProto()). Can only happen if there's a bug in the mapping.
        int PROCESS_STATE_UNKNOWN_TO_PROTO = 998;
        // Not a real process state.
        int PROCESS_STATE_UNKNOWN = 999;
        // Process is a persistent system process.
        int PROCESS_STATE_PERSISTENT = 1000;
        // Process is a persistent system process and is doing UI.
        int PROCESS_STATE_PERSISTENT_UI = 1001;
        // Process is hosting the current top activities. Note that this covers
        // all activities that are visible to the user.
        int PROCESS_STATE_TOP = 1002;
        // Process is bound to a TOP app.
        int PROCESS_STATE_BOUND_TOP = 1020;
        // Process is hosting a foreground service.
        int PROCESS_STATE_FOREGROUND_SERVICE = 1003;
        // Process is hosting a service bound by the system or another foreground app.
        int PROCESS_STATE_BOUND_FOREGROUND_SERVICE = 1004;
        // Process is important to the user, and something they are aware of.
        int PROCESS_STATE_IMPORTANT_FOREGROUND = 1005;
        // Process is important to the user, but not something they are aware of.
        int PROCESS_STATE_IMPORTANT_BACKGROUND = 1006;
        // Process is in the background transient so we will try to keep running.
        int PROCESS_STATE_TRANSIENT_BACKGROUND = 1007;
        // Process is in the background running a backup/restore operation.
        int PROCESS_STATE_BACKUP = 1008;
        // Process is in the background running a service. Unlike oom_adj, this
        // level is used for both the normal running in background state and the
        // executing operations state.
        int PROCESS_STATE_SERVICE = 1009;
        // Process is in the background running a receiver. Note that from the
        // perspective of oom_adj, receivers run at a higher foreground level, but
        // for our prioritization here that is not necessary and putting them
        // below services means many fewer changes in some process states as they
        // receive broadcasts.
        int PROCESS_STATE_RECEIVER = 1010;
        // Same as PROCESS_STATE_TOP but while device is sleeping.
        int PROCESS_STATE_TOP_SLEEPING = 1011;
        // Process is in the background, but it can't restore its state so we want
        // to try to avoid killing it.
        int PROCESS_STATE_HEAVY_WEIGHT = 1012;
        // Process is in the background but hosts the home activity.
        int PROCESS_STATE_HOME = 1013;
        // Process is in the background but hosts the last shown activity.
        int PROCESS_STATE_LAST_ACTIVITY = 1014;
        // Process is being cached for later use and contains activities.
        int PROCESS_STATE_CACHED_ACTIVITY = 1015;
        // Process is being cached for later use and is a client of another cached
        // process that contains activities.
        int PROCESS_STATE_CACHED_ACTIVITY_CLIENT = 1016;
        // Process is being cached for later use and has an activity that corresponds
        // to an existing recent task.
        int PROCESS_STATE_CACHED_RECENT = 1017;
        // Process is being cached for later use and is empty.
        int PROCESS_STATE_CACHED_EMPTY = 1018;
        // Process does not exist.
        int PROCESS_STATE_NONEXISTENT = 1019;
    }
}
