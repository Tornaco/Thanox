package github.tornaco.android.thanos.core.app;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.UserInfo;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.List;

import github.tornaco.android.thanos.core.app.start.StartRecord;
import github.tornaco.android.thanos.core.pm.Pkg;
import github.tornaco.android.thanos.core.process.ProcessRecord;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class ActivityManager {
  public static final class ExcludeRecentSetting {
    public static final int NONE = 0;
    public static final int INCLUDE = 1;
    public static final int EXCLUDE = -1;
  }

  private IActivityManager server;

  @SneakyThrows
  public String getCurrentFrontApp() {
    return server.getCurrentFrontApp();
  }

  @SneakyThrows
  public void forceStopPackage(String packageName) {
    server.forceStopPackage(packageName);
  }

  @SneakyThrows
  public void idlePackage(String packageName) {
    server.idlePackage(packageName);
  }

  @SneakyThrows
  public boolean checkBroadcastingIntent(Intent intent) {
    return server.checkBroadcastingIntent(intent);
  }

  @SneakyThrows
  public boolean checkService(Intent intent, ComponentName service, int callerUid) {
    return server.checkService(intent, service, callerUid);
  }

  @SneakyThrows
  public boolean checkRestartService(String packageName, ComponentName componentName) {
    return server.checkRestartService(packageName, componentName);
  }

  @SneakyThrows
  public boolean checkBroadcast(Intent intent, int receiverUid, int callerUid) {
    return server.checkBroadcast(intent, receiverUid, callerUid);
  }

  @SneakyThrows
  public boolean checkStartProcess(
      ApplicationInfo applicationInfo, String hostType, String hostName) {
    return server.checkStartProcess(applicationInfo, hostType, hostName);
  }

  @SneakyThrows
  public void onStartProcessLocked(ApplicationInfo applicationInfo) {
    server.onStartProcessLocked(applicationInfo);
  }

  @SneakyThrows
  public ProcessRecord[] getRunningAppProcess() {
    return server.getRunningAppProcess();
  }

  @SneakyThrows
  public String[] getRunningAppPackages() {
    return server.getRunningAppPackages();
  }

  @SneakyThrows
  public List<android.app.ActivityManager.RunningServiceInfo> getRunningServiceLegacy(int max) {
    return server.getRunningServiceLegacy(max);
  }

  @SneakyThrows
  public List<android.app.ActivityManager.RunningAppProcessInfo> getRunningAppProcessLegacy() {
    return server.getRunningAppProcessLegacy();
  }

  @SneakyThrows
  public int getRunningAppsCount() {
    return server.getRunningAppsCount();
  }

  @SneakyThrows
  public ProcessRecord[] getRunningAppProcessForPackage(Pkg pkg) {
    return server.getRunningAppProcessForPackage(pkg);
  }

  @SneakyThrows
  public boolean isPackageRunning(String pkgName) {
    return server.isPackageRunning(pkgName);
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

  @SneakyThrows
  public void setPkgStartBlockEnabled(String pkgName, boolean enable) {
    server.setPkgStartBlockEnabled(pkgName, enable);
  }

  @SneakyThrows
  public boolean isPkgStartBlocking(String pkgName) {
    return server.isPkgStartBlocking(pkgName);
  }

  @SneakyThrows
  public void setPkgBgRestrictEnabled(String pkgName, boolean enable) {
    server.setPkgBgRestrictEnabled(pkgName, enable);
  }

  @SneakyThrows
  public boolean isPkgBgRestricted(String pkgName) {
    return server.isPkgBgRestricted(pkgName);
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
  public void setPkgCleanUpOnTaskRemovalEnabled(String pkgName, boolean enable) {
    server.setPkgCleanUpOnTaskRemovalEnabled(pkgName, enable);
  }

  @SneakyThrows
  public boolean isPkgCleanUpOnTaskRemovalEnabled(String pkgName) {
    return server.isPkgCleanUpOnTaskRemovalEnabled(pkgName);
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
  public void setPkgRecentTaskBlurEnabled(String pkgName, boolean enable) {
    server.setPkgRecentTaskBlurEnabled(pkgName, enable);
  }

  @SneakyThrows
  public boolean isPkgRecentTaskBlurEnabled(String pkgName) {
    return server.isPkgRecentTaskBlurEnabled(pkgName);
  }

  @SneakyThrows
  public boolean isPackageIdle(String packageName) {
    return server.isPackageIdle(packageName);
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
  public void setPkgSmartStandByEnabled(String pkgName, boolean enable) {
    server.setPkgSmartStandByEnabled(pkgName, enable);
  }

  @SneakyThrows
  public boolean isPkgSmartStandByEnabled(String pkgName) {
    return server.isPkgSmartStandByEnabled(pkgName);
  }

  @SneakyThrows
  public String[] getLastRecentUsedPackages(int count) {
    return server.getLastRecentUsedPackages(count);
  }

  @SneakyThrows
  public int getRecentTaskExcludeSettingForPackage(String pkgName) {
    return server.getRecentTaskExcludeSettingForPackage(pkgName);
  }

  @SneakyThrows
  public void setRecentTaskExcludeSettingForPackage(String pkgName, int setting) {
    server.setRecentTaskExcludeSettingForPackage(pkgName, setting);
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

  @SneakyThrows
  public void addApp(String targetPkg) {
    server.addApp(targetPkg);
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
  public void stopService(Intent intent) {
    server.stopService(intent);
  }

  @SneakyThrows
  public void killBackgroundProcesses(String packageName) {
    server.killBackgroundProcesses(Pkg.systemUserPkg(packageName));
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

  @SneakyThrows
  public boolean checkGetContentProvider(String callerPkg, String name) {
    return server.checkGetContentProvider(callerPkg, name);
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

  public IBinder asBinder() {
    return server.asBinder();
  }
}
