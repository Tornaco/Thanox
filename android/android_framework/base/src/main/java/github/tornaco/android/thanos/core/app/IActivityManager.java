/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /home/tornaco/Android/Sdk/build-tools/35.0.0/aidl -I/home/tornaco/Documents/Thanox/android/android_framework/base/src/main/java -p/home/tornaco/Documents/Thanox/android/android_sdk/framework.aidl -p/home/tornaco/Documents/Thanox/android/android_sdk/thanos.aidl /home/tornaco/Documents/Thanox/android/android_framework/base/src/main/java/github/tornaco/android/thanos/core/app/IActivityManager.aidl
 */
package github.tornaco.android.thanos.core.app;
public interface IActivityManager extends android.os.IInterface
{
  /** Default implementation for IActivityManager. */
  public static class Default implements github.tornaco.android.thanos.core.app.IActivityManager
  {
    @Override public java.lang.String getCurrentFrontApp() throws android.os.RemoteException
    {
      return null;
    }
    @Override public void forceStopPackage(github.tornaco.android.thanos.core.pm.Pkg pkg, java.lang.String reason) throws android.os.RemoteException
    {
    }
    @Override public void idlePackage(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
    }
    @Override public boolean isPackageIdle(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean checkBroadcastingIntent(android.content.Intent intent) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean checkService(android.content.Intent intent, android.content.ComponentName service, int callerUid, int userId) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean checkRestartService(java.lang.String packageName, android.content.ComponentName componentName) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean checkBroadcast(android.content.Intent intent, int receiverUid, int callerUid) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean checkStartProcess(java.lang.String processName, android.content.pm.ApplicationInfo applicationInfo, java.lang.String hostType, java.lang.String hostName) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void onStartProcessLocked(android.content.pm.ApplicationInfo appInfo) throws android.os.RemoteException
    {
    }
    @Override public github.tornaco.android.thanos.core.process.ProcessRecord[] getRunningAppProcess() throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.pm.Pkg> getRunningAppPackages() throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<android.app.ActivityManager.RunningServiceInfo> getRunningServiceLegacy(int max) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.app.RunningAppProcessInfoCompat> getRunningAppProcessLegacy() throws android.os.RemoteException
    {
      return null;
    }
    @Override public int getRunningAppsCount() throws android.os.RemoteException
    {
      return 0;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.process.ProcessRecord> getRunningAppProcessForPackage(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
      return null;
    }
    @Override public boolean isPackageRunning(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
      return false;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.app.start.StartRecord> getStartRecordsByPackageName(java.lang.String pkgName) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<java.lang.String> getStartRecordBlockedPackages() throws android.os.RemoteException
    {
      return null;
    }
    @Override public long getStartRecordsBlockedCount() throws android.os.RemoteException
    {
      return 0L;
    }
    @Override public long getStartRecordBlockedCountByPackageName(java.lang.String pkgName) throws android.os.RemoteException
    {
      return 0L;
    }
    // 启动管理设置
    @Override public boolean isStartBlockEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setStartBlockEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public void setPkgStartBlockEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isPkgStartBlocking(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
      return false;
    }
    // Task removal
    @Override public boolean isCleanUpOnTaskRemovalEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setCleanUpOnTaskRemovalEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public void setPkgCleanUpOnTaskRemovalEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isPkgCleanUpOnTaskRemovalEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
      return false;
    }
    // 后台运行设置
    @Override public boolean isBgRestrictEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setBgRestrictEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public void setPkgBgRestrictEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isPkgBgRestricted(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setBgRestrictNotificationEnabled(boolean enabled) throws android.os.RemoteException
    {
    }
    @Override public boolean isBgRestrictNotificationEnabled() throws android.os.RemoteException
    {
      return false;
    }
    // Task blur
    @Override public boolean isRecentTaskBlurEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setRecentTaskBlurEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public void setPkgRecentTaskBlurEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isPkgRecentTaskBlurEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
      return false;
    }
    // Audio focused app.
    @Override public boolean isBgTaskCleanUpSkipAudioFocusedAppEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setBgTaskCleanUpSkipAudioFocusedAppEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    // Notification record app.
    @Override public boolean isBgTaskCleanUpSkipWhichHasNotificationEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setBgTaskCleanUpSkipWhichHasNotificationEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    // 后台运行锁屏清理延迟
    @Override public void setBgTaskCleanUpDelayTimeMills(long delayMills) throws android.os.RemoteException
    {
    }
    @Override public long getBgTaskCleanUpDelayTimeMills() throws android.os.RemoteException
    {
      return 0L;
    }
    @Override public void notifyTaskCreated(int taskId, android.content.ComponentName componentName) throws android.os.RemoteException
    {
    }
    @Override public android.app.ActivityManager.MemoryInfo getMemoryInfo() throws android.os.RemoteException
    {
      return null;
    }
    @Override public long[] getProcessPss(int[] pids) throws android.os.RemoteException
    {
      return null;
    }
    @Override public void onApplicationCrashing(java.lang.String eventType, java.lang.String processName, github.tornaco.android.thanos.core.process.ProcessRecord process, java.lang.String stackTrace) throws android.os.RemoteException
    {
    }
    @Override public java.lang.String getPackageNameForTaskId(int taskId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public int isPlatformAppIdleEnabled() throws android.os.RemoteException
    {
      return 0;
    }
    @Override public boolean isSmartStandByEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setSmartStandByEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public void setPkgSmartStandByEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isPkgSmartStandByEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
      return false;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.pm.Pkg> getLastRecentUsedPackages(int count) throws android.os.RemoteException
    {
      return null;
    }
    @Override public int getRecentTaskExcludeSettingForPackage(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
      return 0;
    }
    @Override public void setRecentTaskExcludeSettingForPackage(github.tornaco.android.thanos.core.pm.Pkg pkg, int setting) throws android.os.RemoteException
    {
    }
    // Keep when has recent task.
    @Override public boolean isBgTaskCleanUpSkipWhenHasRecentTaskEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setBgTaskCleanUpSkipWhenHasRecentTaskEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public void launchAppDetailsActivity(java.lang.String pkgName) throws android.os.RemoteException
    {
    }
    @Override public void resetStartRecordsBlocked() throws android.os.RemoteException
    {
    }
    @Override public void addApp(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
    }
    @Override public boolean isStartRuleEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setStartRuleEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public void addStartRule(java.lang.String rule) throws android.os.RemoteException
    {
    }
    @Override public void deleteStartRule(java.lang.String rule) throws android.os.RemoteException
    {
    }
    @Override public java.lang.String[] getAllStartRules() throws android.os.RemoteException
    {
      return null;
    }
    @Override public boolean isStandbyRuleEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setStandbyRuleEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public void addStandbyRule(java.lang.String rule) throws android.os.RemoteException
    {
    }
    @Override public void deleteStandbyRule(java.lang.String rule) throws android.os.RemoteException
    {
    }
    @Override public java.lang.String[] getAllStandbyRules() throws android.os.RemoteException
    {
      return null;
    }
    @Override public github.tornaco.android.thanos.core.app.RunningServiceInfoCompat[] getRunningAppServiceForPackage(java.lang.String pkgName, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public boolean hasRunningServiceForPackage(java.lang.String pkgName, int userId) throws android.os.RemoteException
    {
      return false;
    }
    @Override public android.content.pm.UserInfo getUserInfo(int userHandle) throws android.os.RemoteException
    {
      return null;
    }
    @Override public boolean stopService(android.content.Intent intent) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean killBackgroundProcesses(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean isSmartStandByStopServiceEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setSmartStandByStopServiceEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isSmartStandByInactiveEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setSmartStandByInactiveEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isSmartStandByByPassIfHasNotificationEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setSmartStandByByPassIfHasNotificationEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isSmartStandByBlockBgServiceStartEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setSmartStandByBlockBgServiceStartEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public java.util.List<java.lang.String> getStartRecordAllowedPackages() throws android.os.RemoteException
    {
      return null;
    }
    @Override public long getStartRecordsAllowedCount() throws android.os.RemoteException
    {
      return 0L;
    }
    @Override public long getStartRecordAllowedCountByPackageName(java.lang.String pkgName) throws android.os.RemoteException
    {
      return 0L;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.app.start.StartRecord> getStartRecordsAllowedByPackageName(java.lang.String pkgName) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.app.start.StartRecord> getStartRecordsBlockedByPackageName(java.lang.String pkgName) throws android.os.RemoteException
    {
      return null;
    }
    @Override public void resetStartRecordsAllowed() throws android.os.RemoteException
    {
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.app.start.StartRecord> getAllStartRecordsWithRes(int appFlags, boolean allowed, boolean blocked) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.app.start.StartRecord> getAllStartRecords(int appFlags) throws android.os.RemoteException
    {
      return null;
    }
    @Override public void dump(github.tornaco.android.thanos.core.IPrinter p) throws android.os.RemoteException
    {
    }
    @Override public void dumpCpu(github.tornaco.android.thanos.core.IPrinter p) throws android.os.RemoteException
    {
    }
    @Override public void setNetStatTrackerEnabled(boolean enabled) throws android.os.RemoteException
    {
    }
    @Override public boolean isNetStatTrackerEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean checkGetContentProvider(java.lang.String callerPkg, java.lang.String name, int userId) throws android.os.RemoteException
    {
      return false;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.app.start.StartRecord> getAllStartRecordsForPackageSetWithRes(java.lang.String pkgSetId, boolean allowed, boolean blocked) throws android.os.RemoteException
    {
      return null;
    }
    // ******************************************************************
    // CAF API
    // https://source.android.com/devices/tech/perf/cached-apps-freezer
    //
    // ******************************************************************
    @Override public boolean isCachedAppsFreezerSupported() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void freezeApp(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
    }
    @Override public void unfreezeApp(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
    }
    @Override public void freezeAppProcess(long pid) throws android.os.RemoteException
    {
    }
    @Override public void unfreezeAppProcess(long pid) throws android.os.RemoteException
    {
    }
    @Override public void updateProcessCpuUsageStats() throws android.os.RemoteException
    {
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.app.usage.ProcessCpuUsageStats> queryProcessCpuUsageStats(long[] pids, boolean update) throws android.os.RemoteException
    {
      return null;
    }
    @Override public float queryCpuUsageRatio(long[] pids, boolean update) throws android.os.RemoteException
    {
      return 0.0f;
    }
    @Override public boolean killProcess(long pid) throws android.os.RemoteException
    {
      return false;
    }
    @Override public github.tornaco.android.thanos.core.os.SwapInfo getSwapInfo() throws android.os.RemoteException
    {
      return null;
    }
    // ******************************************************************
    // Block API
    // Block Receiver/Service/Provider all the time, event the package is running at foreground,
    // may cause the app crash.
    // ******************************************************************
    @Override public void setBlockAllReceiver(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean block) throws android.os.RemoteException
    {
    }
    @Override public boolean isBlockAllReceiver(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setBlockAllService(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean block) throws android.os.RemoteException
    {
    }
    @Override public boolean isBlockAllService(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setBlockAllProvider(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean block) throws android.os.RemoteException
    {
    }
    @Override public boolean isBlockAllProvider(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
      return false;
    }
    // Return 0 if it fail
    @Override public long getProcessStartTime(int pid) throws android.os.RemoteException
    {
      return 0L;
    }
    @Override public boolean isAppForeground(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean hasRunningForegroundService(github.tornaco.android.thanos.core.pm.Pkg pkg, int foregroundServicetype) throws android.os.RemoteException
    {
      return false;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.app.ActivityAssistInfo> getTopVisibleActivities() throws android.os.RemoteException
    {
      return null;
    }
    @Override public boolean checkStartActivity(android.content.Intent intent, int callerUid) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean isBgTaskCleanUpSkipForegroundEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setBgTaskCleanUpSkipForegroundEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    /** return the pid of process. or -1 if no process found */
    @Override public int getPid(github.tornaco.android.thanos.core.os.ProcessName processName) throws android.os.RemoteException
    {
      return 0;
    }
    /** return the pid of killed process. or -1 if no process found */
    @Override public int killProcessByName(github.tornaco.android.thanos.core.os.ProcessName processName) throws android.os.RemoteException
    {
      return 0;
    }
    @Override public void killProcessByNames(java.util.List<github.tornaco.android.thanos.core.os.ProcessName> processNames) throws android.os.RemoteException
    {
    }
    @Override public boolean dumpHeap(java.lang.String process) throws android.os.RemoteException
    {
      return false;
    }
    @Override public github.tornaco.android.thanos.core.pm.Pkg getCurrentFrontPkg() throws android.os.RemoteException
    {
      return null;
    }
    @Override public boolean isSmartStandByByPassIfHasVisibleWindows() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setSmartStandByByPassIfHasVisibleWindowsEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public float getTotalCpuPercent(boolean update) throws android.os.RemoteException
    {
      return 0.0f;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.app.usage.PkgCpuUsageStats> getTopNCpuUsagePackages(int n, boolean update) throws android.os.RemoteException
    {
      return null;
    }
    @Override public boolean isPkgResident(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setPkgResident(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean resident) throws android.os.RemoteException
    {
    }
    @Override public void setPkgRecentTaskBlurMode(github.tornaco.android.thanos.core.pm.Pkg pkg, int mode) throws android.os.RemoteException
    {
    }
    @Override public int getPkgRecentTaskBlurMode(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
      return 0;
    }
    @Override public boolean isSmartStandByUnbindServiceEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setSmartStandByUnbindServiceEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.app.IActivityManager
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.app.IActivityManager interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.app.IActivityManager asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.app.IActivityManager))) {
        return ((github.tornaco.android.thanos.core.app.IActivityManager)iin);
      }
      return new github.tornaco.android.thanos.core.app.IActivityManager.Stub.Proxy(obj);
    }
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      java.lang.String descriptor = DESCRIPTOR;
      if (code >= android.os.IBinder.FIRST_CALL_TRANSACTION && code <= android.os.IBinder.LAST_CALL_TRANSACTION) {
        data.enforceInterface(descriptor);
      }
      if (code == INTERFACE_TRANSACTION) {
        reply.writeString(descriptor);
        return true;
      }
      switch (code)
      {
        case TRANSACTION_getCurrentFrontApp:
        {
          java.lang.String _result = this.getCurrentFrontApp();
          reply.writeNoException();
          reply.writeString(_result);
          break;
        }
        case TRANSACTION_forceStopPackage:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.forceStopPackage(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_idlePackage:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          this.idlePackage(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isPackageIdle:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _result = this.isPackageIdle(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_checkBroadcastingIntent:
        {
          android.content.Intent _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.content.Intent.CREATOR);
          boolean _result = this.checkBroadcastingIntent(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_checkService:
        {
          android.content.Intent _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.content.Intent.CREATOR);
          android.content.ComponentName _arg1;
          _arg1 = _Parcel.readTypedObject(data, android.content.ComponentName.CREATOR);
          int _arg2;
          _arg2 = data.readInt();
          int _arg3;
          _arg3 = data.readInt();
          boolean _result = this.checkService(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_checkRestartService:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          android.content.ComponentName _arg1;
          _arg1 = _Parcel.readTypedObject(data, android.content.ComponentName.CREATOR);
          boolean _result = this.checkRestartService(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_checkBroadcast:
        {
          android.content.Intent _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.content.Intent.CREATOR);
          int _arg1;
          _arg1 = data.readInt();
          int _arg2;
          _arg2 = data.readInt();
          boolean _result = this.checkBroadcast(_arg0, _arg1, _arg2);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_checkStartProcess:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          android.content.pm.ApplicationInfo _arg1;
          _arg1 = _Parcel.readTypedObject(data, android.content.pm.ApplicationInfo.CREATOR);
          java.lang.String _arg2;
          _arg2 = data.readString();
          java.lang.String _arg3;
          _arg3 = data.readString();
          boolean _result = this.checkStartProcess(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_onStartProcessLocked:
        {
          android.content.pm.ApplicationInfo _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.content.pm.ApplicationInfo.CREATOR);
          this.onStartProcessLocked(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getRunningAppProcess:
        {
          github.tornaco.android.thanos.core.process.ProcessRecord[] _result = this.getRunningAppProcess();
          reply.writeNoException();
          reply.writeTypedArray(_result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getRunningAppPackages:
        {
          java.util.List<github.tornaco.android.thanos.core.pm.Pkg> _result = this.getRunningAppPackages();
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getRunningServiceLegacy:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.util.List<android.app.ActivityManager.RunningServiceInfo> _result = this.getRunningServiceLegacy(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getRunningAppProcessLegacy:
        {
          java.util.List<github.tornaco.android.thanos.core.app.RunningAppProcessInfoCompat> _result = this.getRunningAppProcessLegacy();
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getRunningAppsCount:
        {
          int _result = this.getRunningAppsCount();
          reply.writeNoException();
          reply.writeInt(_result);
          break;
        }
        case TRANSACTION_getRunningAppProcessForPackage:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          java.util.List<github.tornaco.android.thanos.core.process.ProcessRecord> _result = this.getRunningAppProcessForPackage(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_isPackageRunning:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _result = this.isPackageRunning(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_getStartRecordsByPackageName:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.util.List<github.tornaco.android.thanos.core.app.start.StartRecord> _result = this.getStartRecordsByPackageName(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getStartRecordBlockedPackages:
        {
          java.util.List<java.lang.String> _result = this.getStartRecordBlockedPackages();
          reply.writeNoException();
          reply.writeStringList(_result);
          break;
        }
        case TRANSACTION_getStartRecordsBlockedCount:
        {
          long _result = this.getStartRecordsBlockedCount();
          reply.writeNoException();
          reply.writeLong(_result);
          break;
        }
        case TRANSACTION_getStartRecordBlockedCountByPackageName:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          long _result = this.getStartRecordBlockedCountByPackageName(_arg0);
          reply.writeNoException();
          reply.writeLong(_result);
          break;
        }
        case TRANSACTION_isStartBlockEnabled:
        {
          boolean _result = this.isStartBlockEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setStartBlockEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setStartBlockEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_setPkgStartBlockEnabled:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setPkgStartBlockEnabled(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isPkgStartBlocking:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _result = this.isPkgStartBlocking(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_isCleanUpOnTaskRemovalEnabled:
        {
          boolean _result = this.isCleanUpOnTaskRemovalEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setCleanUpOnTaskRemovalEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setCleanUpOnTaskRemovalEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_setPkgCleanUpOnTaskRemovalEnabled:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setPkgCleanUpOnTaskRemovalEnabled(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isPkgCleanUpOnTaskRemovalEnabled:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _result = this.isPkgCleanUpOnTaskRemovalEnabled(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_isBgRestrictEnabled:
        {
          boolean _result = this.isBgRestrictEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setBgRestrictEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setBgRestrictEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_setPkgBgRestrictEnabled:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setPkgBgRestrictEnabled(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isPkgBgRestricted:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _result = this.isPkgBgRestricted(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setBgRestrictNotificationEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setBgRestrictNotificationEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isBgRestrictNotificationEnabled:
        {
          boolean _result = this.isBgRestrictNotificationEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_isRecentTaskBlurEnabled:
        {
          boolean _result = this.isRecentTaskBlurEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setRecentTaskBlurEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setRecentTaskBlurEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_setPkgRecentTaskBlurEnabled:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setPkgRecentTaskBlurEnabled(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isPkgRecentTaskBlurEnabled:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _result = this.isPkgRecentTaskBlurEnabled(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_isBgTaskCleanUpSkipAudioFocusedAppEnabled:
        {
          boolean _result = this.isBgTaskCleanUpSkipAudioFocusedAppEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setBgTaskCleanUpSkipAudioFocusedAppEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setBgTaskCleanUpSkipAudioFocusedAppEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isBgTaskCleanUpSkipWhichHasNotificationEnabled:
        {
          boolean _result = this.isBgTaskCleanUpSkipWhichHasNotificationEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setBgTaskCleanUpSkipWhichHasNotificationEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setBgTaskCleanUpSkipWhichHasNotificationEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_setBgTaskCleanUpDelayTimeMills:
        {
          long _arg0;
          _arg0 = data.readLong();
          this.setBgTaskCleanUpDelayTimeMills(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getBgTaskCleanUpDelayTimeMills:
        {
          long _result = this.getBgTaskCleanUpDelayTimeMills();
          reply.writeNoException();
          reply.writeLong(_result);
          break;
        }
        case TRANSACTION_notifyTaskCreated:
        {
          int _arg0;
          _arg0 = data.readInt();
          android.content.ComponentName _arg1;
          _arg1 = _Parcel.readTypedObject(data, android.content.ComponentName.CREATOR);
          this.notifyTaskCreated(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getMemoryInfo:
        {
          android.app.ActivityManager.MemoryInfo _result = this.getMemoryInfo();
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getProcessPss:
        {
          int[] _arg0;
          _arg0 = data.createIntArray();
          long[] _result = this.getProcessPss(_arg0);
          reply.writeNoException();
          reply.writeLongArray(_result);
          break;
        }
        case TRANSACTION_onApplicationCrashing:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          github.tornaco.android.thanos.core.process.ProcessRecord _arg2;
          _arg2 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.process.ProcessRecord.CREATOR);
          java.lang.String _arg3;
          _arg3 = data.readString();
          this.onApplicationCrashing(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getPackageNameForTaskId:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _result = this.getPackageNameForTaskId(_arg0);
          reply.writeNoException();
          reply.writeString(_result);
          break;
        }
        case TRANSACTION_isPlatformAppIdleEnabled:
        {
          int _result = this.isPlatformAppIdleEnabled();
          reply.writeNoException();
          reply.writeInt(_result);
          break;
        }
        case TRANSACTION_isSmartStandByEnabled:
        {
          boolean _result = this.isSmartStandByEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setSmartStandByEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setSmartStandByEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_setPkgSmartStandByEnabled:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setPkgSmartStandByEnabled(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isPkgSmartStandByEnabled:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _result = this.isPkgSmartStandByEnabled(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_getLastRecentUsedPackages:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.util.List<github.tornaco.android.thanos.core.pm.Pkg> _result = this.getLastRecentUsedPackages(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getRecentTaskExcludeSettingForPackage:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          int _result = this.getRecentTaskExcludeSettingForPackage(_arg0);
          reply.writeNoException();
          reply.writeInt(_result);
          break;
        }
        case TRANSACTION_setRecentTaskExcludeSettingForPackage:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          int _arg1;
          _arg1 = data.readInt();
          this.setRecentTaskExcludeSettingForPackage(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isBgTaskCleanUpSkipWhenHasRecentTaskEnabled:
        {
          boolean _result = this.isBgTaskCleanUpSkipWhenHasRecentTaskEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setBgTaskCleanUpSkipWhenHasRecentTaskEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setBgTaskCleanUpSkipWhenHasRecentTaskEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_launchAppDetailsActivity:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.launchAppDetailsActivity(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_resetStartRecordsBlocked:
        {
          this.resetStartRecordsBlocked();
          reply.writeNoException();
          break;
        }
        case TRANSACTION_addApp:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          this.addApp(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isStartRuleEnabled:
        {
          boolean _result = this.isStartRuleEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setStartRuleEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setStartRuleEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_addStartRule:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.addStartRule(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_deleteStartRule:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.deleteStartRule(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getAllStartRules:
        {
          java.lang.String[] _result = this.getAllStartRules();
          reply.writeNoException();
          reply.writeStringArray(_result);
          break;
        }
        case TRANSACTION_isStandbyRuleEnabled:
        {
          boolean _result = this.isStandbyRuleEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setStandbyRuleEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setStandbyRuleEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_addStandbyRule:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.addStandbyRule(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_deleteStandbyRule:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.deleteStandbyRule(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getAllStandbyRules:
        {
          java.lang.String[] _result = this.getAllStandbyRules();
          reply.writeNoException();
          reply.writeStringArray(_result);
          break;
        }
        case TRANSACTION_getRunningAppServiceForPackage:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          github.tornaco.android.thanos.core.app.RunningServiceInfoCompat[] _result = this.getRunningAppServiceForPackage(_arg0, _arg1);
          reply.writeNoException();
          reply.writeTypedArray(_result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_hasRunningServiceForPackage:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          boolean _result = this.hasRunningServiceForPackage(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_getUserInfo:
        {
          int _arg0;
          _arg0 = data.readInt();
          android.content.pm.UserInfo _result = this.getUserInfo(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_stopService:
        {
          android.content.Intent _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.content.Intent.CREATOR);
          boolean _result = this.stopService(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_killBackgroundProcesses:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _result = this.killBackgroundProcesses(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_isSmartStandByStopServiceEnabled:
        {
          boolean _result = this.isSmartStandByStopServiceEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setSmartStandByStopServiceEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setSmartStandByStopServiceEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isSmartStandByInactiveEnabled:
        {
          boolean _result = this.isSmartStandByInactiveEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setSmartStandByInactiveEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setSmartStandByInactiveEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isSmartStandByByPassIfHasNotificationEnabled:
        {
          boolean _result = this.isSmartStandByByPassIfHasNotificationEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setSmartStandByByPassIfHasNotificationEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setSmartStandByByPassIfHasNotificationEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isSmartStandByBlockBgServiceStartEnabled:
        {
          boolean _result = this.isSmartStandByBlockBgServiceStartEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setSmartStandByBlockBgServiceStartEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setSmartStandByBlockBgServiceStartEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getStartRecordAllowedPackages:
        {
          java.util.List<java.lang.String> _result = this.getStartRecordAllowedPackages();
          reply.writeNoException();
          reply.writeStringList(_result);
          break;
        }
        case TRANSACTION_getStartRecordsAllowedCount:
        {
          long _result = this.getStartRecordsAllowedCount();
          reply.writeNoException();
          reply.writeLong(_result);
          break;
        }
        case TRANSACTION_getStartRecordAllowedCountByPackageName:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          long _result = this.getStartRecordAllowedCountByPackageName(_arg0);
          reply.writeNoException();
          reply.writeLong(_result);
          break;
        }
        case TRANSACTION_getStartRecordsAllowedByPackageName:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.util.List<github.tornaco.android.thanos.core.app.start.StartRecord> _result = this.getStartRecordsAllowedByPackageName(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getStartRecordsBlockedByPackageName:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.util.List<github.tornaco.android.thanos.core.app.start.StartRecord> _result = this.getStartRecordsBlockedByPackageName(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_resetStartRecordsAllowed:
        {
          this.resetStartRecordsAllowed();
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getAllStartRecordsWithRes:
        {
          int _arg0;
          _arg0 = data.readInt();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          boolean _arg2;
          _arg2 = (0!=data.readInt());
          java.util.List<github.tornaco.android.thanos.core.app.start.StartRecord> _result = this.getAllStartRecordsWithRes(_arg0, _arg1, _arg2);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getAllStartRecords:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.util.List<github.tornaco.android.thanos.core.app.start.StartRecord> _result = this.getAllStartRecords(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_dump:
        {
          github.tornaco.android.thanos.core.IPrinter _arg0;
          _arg0 = github.tornaco.android.thanos.core.IPrinter.Stub.asInterface(data.readStrongBinder());
          this.dump(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_dumpCpu:
        {
          github.tornaco.android.thanos.core.IPrinter _arg0;
          _arg0 = github.tornaco.android.thanos.core.IPrinter.Stub.asInterface(data.readStrongBinder());
          this.dumpCpu(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_setNetStatTrackerEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setNetStatTrackerEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isNetStatTrackerEnabled:
        {
          boolean _result = this.isNetStatTrackerEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_checkGetContentProvider:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          int _arg2;
          _arg2 = data.readInt();
          boolean _result = this.checkGetContentProvider(_arg0, _arg1, _arg2);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_getAllStartRecordsForPackageSetWithRes:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          boolean _arg2;
          _arg2 = (0!=data.readInt());
          java.util.List<github.tornaco.android.thanos.core.app.start.StartRecord> _result = this.getAllStartRecordsForPackageSetWithRes(_arg0, _arg1, _arg2);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_isCachedAppsFreezerSupported:
        {
          boolean _result = this.isCachedAppsFreezerSupported();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_freezeApp:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          this.freezeApp(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_unfreezeApp:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          this.unfreezeApp(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_freezeAppProcess:
        {
          long _arg0;
          _arg0 = data.readLong();
          this.freezeAppProcess(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_unfreezeAppProcess:
        {
          long _arg0;
          _arg0 = data.readLong();
          this.unfreezeAppProcess(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_updateProcessCpuUsageStats:
        {
          this.updateProcessCpuUsageStats();
          reply.writeNoException();
          break;
        }
        case TRANSACTION_queryProcessCpuUsageStats:
        {
          long[] _arg0;
          _arg0 = data.createLongArray();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          java.util.List<github.tornaco.android.thanos.core.app.usage.ProcessCpuUsageStats> _result = this.queryProcessCpuUsageStats(_arg0, _arg1);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_queryCpuUsageRatio:
        {
          long[] _arg0;
          _arg0 = data.createLongArray();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          float _result = this.queryCpuUsageRatio(_arg0, _arg1);
          reply.writeNoException();
          reply.writeFloat(_result);
          break;
        }
        case TRANSACTION_killProcess:
        {
          long _arg0;
          _arg0 = data.readLong();
          boolean _result = this.killProcess(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_getSwapInfo:
        {
          github.tornaco.android.thanos.core.os.SwapInfo _result = this.getSwapInfo();
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_setBlockAllReceiver:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setBlockAllReceiver(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isBlockAllReceiver:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _result = this.isBlockAllReceiver(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setBlockAllService:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setBlockAllService(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isBlockAllService:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _result = this.isBlockAllService(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setBlockAllProvider:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setBlockAllProvider(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isBlockAllProvider:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _result = this.isBlockAllProvider(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_getProcessStartTime:
        {
          int _arg0;
          _arg0 = data.readInt();
          long _result = this.getProcessStartTime(_arg0);
          reply.writeNoException();
          reply.writeLong(_result);
          break;
        }
        case TRANSACTION_isAppForeground:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _result = this.isAppForeground(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_hasRunningForegroundService:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          int _arg1;
          _arg1 = data.readInt();
          boolean _result = this.hasRunningForegroundService(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_getTopVisibleActivities:
        {
          java.util.List<github.tornaco.android.thanos.core.app.ActivityAssistInfo> _result = this.getTopVisibleActivities();
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_checkStartActivity:
        {
          android.content.Intent _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.content.Intent.CREATOR);
          int _arg1;
          _arg1 = data.readInt();
          boolean _result = this.checkStartActivity(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_isBgTaskCleanUpSkipForegroundEnabled:
        {
          boolean _result = this.isBgTaskCleanUpSkipForegroundEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setBgTaskCleanUpSkipForegroundEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setBgTaskCleanUpSkipForegroundEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getPid:
        {
          github.tornaco.android.thanos.core.os.ProcessName _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.os.ProcessName.CREATOR);
          int _result = this.getPid(_arg0);
          reply.writeNoException();
          reply.writeInt(_result);
          break;
        }
        case TRANSACTION_killProcessByName:
        {
          github.tornaco.android.thanos.core.os.ProcessName _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.os.ProcessName.CREATOR);
          int _result = this.killProcessByName(_arg0);
          reply.writeNoException();
          reply.writeInt(_result);
          break;
        }
        case TRANSACTION_killProcessByNames:
        {
          java.util.List<github.tornaco.android.thanos.core.os.ProcessName> _arg0;
          _arg0 = data.createTypedArrayList(github.tornaco.android.thanos.core.os.ProcessName.CREATOR);
          this.killProcessByNames(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_dumpHeap:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.dumpHeap(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_getCurrentFrontPkg:
        {
          github.tornaco.android.thanos.core.pm.Pkg _result = this.getCurrentFrontPkg();
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_isSmartStandByByPassIfHasVisibleWindows:
        {
          boolean _result = this.isSmartStandByByPassIfHasVisibleWindows();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setSmartStandByByPassIfHasVisibleWindowsEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setSmartStandByByPassIfHasVisibleWindowsEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getTotalCpuPercent:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          float _result = this.getTotalCpuPercent(_arg0);
          reply.writeNoException();
          reply.writeFloat(_result);
          break;
        }
        case TRANSACTION_getTopNCpuUsagePackages:
        {
          int _arg0;
          _arg0 = data.readInt();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          java.util.List<github.tornaco.android.thanos.core.app.usage.PkgCpuUsageStats> _result = this.getTopNCpuUsagePackages(_arg0, _arg1);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_isPkgResident:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _result = this.isPkgResident(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setPkgResident:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setPkgResident(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_setPkgRecentTaskBlurMode:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          int _arg1;
          _arg1 = data.readInt();
          this.setPkgRecentTaskBlurMode(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getPkgRecentTaskBlurMode:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          int _result = this.getPkgRecentTaskBlurMode(_arg0);
          reply.writeNoException();
          reply.writeInt(_result);
          break;
        }
        case TRANSACTION_isSmartStandByUnbindServiceEnabled:
        {
          boolean _result = this.isSmartStandByUnbindServiceEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setSmartStandByUnbindServiceEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setSmartStandByUnbindServiceEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static class Proxy implements github.tornaco.android.thanos.core.app.IActivityManager
    {
      private android.os.IBinder mRemote;
      Proxy(android.os.IBinder remote)
      {
        mRemote = remote;
      }
      @Override public android.os.IBinder asBinder()
      {
        return mRemote;
      }
      public java.lang.String getInterfaceDescriptor()
      {
        return DESCRIPTOR;
      }
      @Override public java.lang.String getCurrentFrontApp() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getCurrentFrontApp, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readString();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void forceStopPackage(github.tornaco.android.thanos.core.pm.Pkg pkg, java.lang.String reason) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          _data.writeString(reason);
          boolean _status = mRemote.transact(Stub.TRANSACTION_forceStopPackage, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void idlePackage(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_idlePackage, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isPackageIdle(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPackageIdle, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean checkBroadcastingIntent(android.content.Intent intent) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, intent, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_checkBroadcastingIntent, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean checkService(android.content.Intent intent, android.content.ComponentName service, int callerUid, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, intent, 0);
          _Parcel.writeTypedObject(_data, service, 0);
          _data.writeInt(callerUid);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_checkService, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean checkRestartService(java.lang.String packageName, android.content.ComponentName componentName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          _Parcel.writeTypedObject(_data, componentName, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_checkRestartService, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean checkBroadcast(android.content.Intent intent, int receiverUid, int callerUid) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, intent, 0);
          _data.writeInt(receiverUid);
          _data.writeInt(callerUid);
          boolean _status = mRemote.transact(Stub.TRANSACTION_checkBroadcast, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean checkStartProcess(java.lang.String processName, android.content.pm.ApplicationInfo applicationInfo, java.lang.String hostType, java.lang.String hostName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(processName);
          _Parcel.writeTypedObject(_data, applicationInfo, 0);
          _data.writeString(hostType);
          _data.writeString(hostName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_checkStartProcess, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void onStartProcessLocked(android.content.pm.ApplicationInfo appInfo) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, appInfo, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onStartProcessLocked, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public github.tornaco.android.thanos.core.process.ProcessRecord[] getRunningAppProcess() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.process.ProcessRecord[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getRunningAppProcess, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArray(github.tornaco.android.thanos.core.process.ProcessRecord.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.pm.Pkg> getRunningAppPackages() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.pm.Pkg> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getRunningAppPackages, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<android.app.ActivityManager.RunningServiceInfo> getRunningServiceLegacy(int max) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<android.app.ActivityManager.RunningServiceInfo> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(max);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getRunningServiceLegacy, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(android.app.ActivityManager.RunningServiceInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.app.RunningAppProcessInfoCompat> getRunningAppProcessLegacy() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.app.RunningAppProcessInfoCompat> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getRunningAppProcessLegacy, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.app.RunningAppProcessInfoCompat.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public int getRunningAppsCount() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getRunningAppsCount, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.process.ProcessRecord> getRunningAppProcessForPackage(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.process.ProcessRecord> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getRunningAppProcessForPackage, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.process.ProcessRecord.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean isPackageRunning(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPackageRunning, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.app.start.StartRecord> getStartRecordsByPackageName(java.lang.String pkgName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.app.start.StartRecord> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getStartRecordsByPackageName, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.app.start.StartRecord.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<java.lang.String> getStartRecordBlockedPackages() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<java.lang.String> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getStartRecordBlockedPackages, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createStringArrayList();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public long getStartRecordsBlockedCount() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        long _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getStartRecordsBlockedCount, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readLong();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public long getStartRecordBlockedCountByPackageName(java.lang.String pkgName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        long _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getStartRecordBlockedCountByPackageName, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readLong();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      // 启动管理设置
      @Override public boolean isStartBlockEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isStartBlockEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setStartBlockEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setStartBlockEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setPkgStartBlockEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPkgStartBlockEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isPkgStartBlocking(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPkgStartBlocking, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      // Task removal
      @Override public boolean isCleanUpOnTaskRemovalEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isCleanUpOnTaskRemovalEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setCleanUpOnTaskRemovalEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setCleanUpOnTaskRemovalEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setPkgCleanUpOnTaskRemovalEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPkgCleanUpOnTaskRemovalEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isPkgCleanUpOnTaskRemovalEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPkgCleanUpOnTaskRemovalEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      // 后台运行设置
      @Override public boolean isBgRestrictEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isBgRestrictEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setBgRestrictEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setBgRestrictEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setPkgBgRestrictEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPkgBgRestrictEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isPkgBgRestricted(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPkgBgRestricted, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setBgRestrictNotificationEnabled(boolean enabled) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enabled)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setBgRestrictNotificationEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isBgRestrictNotificationEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isBgRestrictNotificationEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      // Task blur
      @Override public boolean isRecentTaskBlurEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isRecentTaskBlurEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setRecentTaskBlurEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setRecentTaskBlurEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setPkgRecentTaskBlurEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPkgRecentTaskBlurEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isPkgRecentTaskBlurEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPkgRecentTaskBlurEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      // Audio focused app.
      @Override public boolean isBgTaskCleanUpSkipAudioFocusedAppEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isBgTaskCleanUpSkipAudioFocusedAppEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setBgTaskCleanUpSkipAudioFocusedAppEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setBgTaskCleanUpSkipAudioFocusedAppEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      // Notification record app.
      @Override public boolean isBgTaskCleanUpSkipWhichHasNotificationEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isBgTaskCleanUpSkipWhichHasNotificationEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setBgTaskCleanUpSkipWhichHasNotificationEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setBgTaskCleanUpSkipWhichHasNotificationEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      // 后台运行锁屏清理延迟
      @Override public void setBgTaskCleanUpDelayTimeMills(long delayMills) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeLong(delayMills);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setBgTaskCleanUpDelayTimeMills, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public long getBgTaskCleanUpDelayTimeMills() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        long _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getBgTaskCleanUpDelayTimeMills, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readLong();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void notifyTaskCreated(int taskId, android.content.ComponentName componentName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(taskId);
          _Parcel.writeTypedObject(_data, componentName, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_notifyTaskCreated, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public android.app.ActivityManager.MemoryInfo getMemoryInfo() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.app.ActivityManager.MemoryInfo _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getMemoryInfo, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, android.app.ActivityManager.MemoryInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public long[] getProcessPss(int[] pids) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        long[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeIntArray(pids);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getProcessPss, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createLongArray();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void onApplicationCrashing(java.lang.String eventType, java.lang.String processName, github.tornaco.android.thanos.core.process.ProcessRecord process, java.lang.String stackTrace) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(eventType);
          _data.writeString(processName);
          _Parcel.writeTypedObject(_data, process, 0);
          _data.writeString(stackTrace);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onApplicationCrashing, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.lang.String getPackageNameForTaskId(int taskId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(taskId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getPackageNameForTaskId, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readString();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public int isPlatformAppIdleEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPlatformAppIdleEnabled, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean isSmartStandByEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isSmartStandByEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setSmartStandByEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setSmartStandByEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setPkgSmartStandByEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPkgSmartStandByEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isPkgSmartStandByEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPkgSmartStandByEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.pm.Pkg> getLastRecentUsedPackages(int count) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.pm.Pkg> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(count);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getLastRecentUsedPackages, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public int getRecentTaskExcludeSettingForPackage(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getRecentTaskExcludeSettingForPackage, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setRecentTaskExcludeSettingForPackage(github.tornaco.android.thanos.core.pm.Pkg pkg, int setting) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          _data.writeInt(setting);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setRecentTaskExcludeSettingForPackage, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      // Keep when has recent task.
      @Override public boolean isBgTaskCleanUpSkipWhenHasRecentTaskEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isBgTaskCleanUpSkipWhenHasRecentTaskEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setBgTaskCleanUpSkipWhenHasRecentTaskEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setBgTaskCleanUpSkipWhenHasRecentTaskEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void launchAppDetailsActivity(java.lang.String pkgName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_launchAppDetailsActivity, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void resetStartRecordsBlocked() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_resetStartRecordsBlocked, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void addApp(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_addApp, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isStartRuleEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isStartRuleEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setStartRuleEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setStartRuleEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void addStartRule(java.lang.String rule) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(rule);
          boolean _status = mRemote.transact(Stub.TRANSACTION_addStartRule, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void deleteStartRule(java.lang.String rule) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(rule);
          boolean _status = mRemote.transact(Stub.TRANSACTION_deleteStartRule, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.lang.String[] getAllStartRules() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAllStartRules, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createStringArray();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean isStandbyRuleEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isStandbyRuleEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setStandbyRuleEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setStandbyRuleEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void addStandbyRule(java.lang.String rule) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(rule);
          boolean _status = mRemote.transact(Stub.TRANSACTION_addStandbyRule, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void deleteStandbyRule(java.lang.String rule) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(rule);
          boolean _status = mRemote.transact(Stub.TRANSACTION_deleteStandbyRule, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.lang.String[] getAllStandbyRules() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAllStandbyRules, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createStringArray();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public github.tornaco.android.thanos.core.app.RunningServiceInfoCompat[] getRunningAppServiceForPackage(java.lang.String pkgName, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.app.RunningServiceInfoCompat[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getRunningAppServiceForPackage, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArray(github.tornaco.android.thanos.core.app.RunningServiceInfoCompat.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean hasRunningServiceForPackage(java.lang.String pkgName, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_hasRunningServiceForPackage, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public android.content.pm.UserInfo getUserInfo(int userHandle) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.content.pm.UserInfo _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(userHandle);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getUserInfo, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, android.content.pm.UserInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean stopService(android.content.Intent intent) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, intent, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_stopService, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean killBackgroundProcesses(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_killBackgroundProcesses, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean isSmartStandByStopServiceEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isSmartStandByStopServiceEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setSmartStandByStopServiceEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setSmartStandByStopServiceEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isSmartStandByInactiveEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isSmartStandByInactiveEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setSmartStandByInactiveEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setSmartStandByInactiveEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isSmartStandByByPassIfHasNotificationEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isSmartStandByByPassIfHasNotificationEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setSmartStandByByPassIfHasNotificationEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setSmartStandByByPassIfHasNotificationEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isSmartStandByBlockBgServiceStartEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isSmartStandByBlockBgServiceStartEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setSmartStandByBlockBgServiceStartEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setSmartStandByBlockBgServiceStartEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.util.List<java.lang.String> getStartRecordAllowedPackages() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<java.lang.String> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getStartRecordAllowedPackages, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createStringArrayList();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public long getStartRecordsAllowedCount() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        long _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getStartRecordsAllowedCount, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readLong();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public long getStartRecordAllowedCountByPackageName(java.lang.String pkgName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        long _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getStartRecordAllowedCountByPackageName, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readLong();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.app.start.StartRecord> getStartRecordsAllowedByPackageName(java.lang.String pkgName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.app.start.StartRecord> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getStartRecordsAllowedByPackageName, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.app.start.StartRecord.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.app.start.StartRecord> getStartRecordsBlockedByPackageName(java.lang.String pkgName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.app.start.StartRecord> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getStartRecordsBlockedByPackageName, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.app.start.StartRecord.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void resetStartRecordsAllowed() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_resetStartRecordsAllowed, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.app.start.StartRecord> getAllStartRecordsWithRes(int appFlags, boolean allowed, boolean blocked) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.app.start.StartRecord> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(appFlags);
          _data.writeInt(((allowed)?(1):(0)));
          _data.writeInt(((blocked)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAllStartRecordsWithRes, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.app.start.StartRecord.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.app.start.StartRecord> getAllStartRecords(int appFlags) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.app.start.StartRecord> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(appFlags);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAllStartRecords, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.app.start.StartRecord.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void dump(github.tornaco.android.thanos.core.IPrinter p) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(p);
          boolean _status = mRemote.transact(Stub.TRANSACTION_dump, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void dumpCpu(github.tornaco.android.thanos.core.IPrinter p) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(p);
          boolean _status = mRemote.transact(Stub.TRANSACTION_dumpCpu, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setNetStatTrackerEnabled(boolean enabled) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enabled)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setNetStatTrackerEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isNetStatTrackerEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isNetStatTrackerEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean checkGetContentProvider(java.lang.String callerPkg, java.lang.String name, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(callerPkg);
          _data.writeString(name);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_checkGetContentProvider, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.app.start.StartRecord> getAllStartRecordsForPackageSetWithRes(java.lang.String pkgSetId, boolean allowed, boolean blocked) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.app.start.StartRecord> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgSetId);
          _data.writeInt(((allowed)?(1):(0)));
          _data.writeInt(((blocked)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAllStartRecordsForPackageSetWithRes, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.app.start.StartRecord.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      // ******************************************************************
      // CAF API
      // https://source.android.com/devices/tech/perf/cached-apps-freezer
      //
      // ******************************************************************
      @Override public boolean isCachedAppsFreezerSupported() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isCachedAppsFreezerSupported, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void freezeApp(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_freezeApp, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void unfreezeApp(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_unfreezeApp, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void freezeAppProcess(long pid) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeLong(pid);
          boolean _status = mRemote.transact(Stub.TRANSACTION_freezeAppProcess, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void unfreezeAppProcess(long pid) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeLong(pid);
          boolean _status = mRemote.transact(Stub.TRANSACTION_unfreezeAppProcess, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void updateProcessCpuUsageStats() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_updateProcessCpuUsageStats, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.app.usage.ProcessCpuUsageStats> queryProcessCpuUsageStats(long[] pids, boolean update) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.app.usage.ProcessCpuUsageStats> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeLongArray(pids);
          _data.writeInt(((update)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_queryProcessCpuUsageStats, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.app.usage.ProcessCpuUsageStats.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public float queryCpuUsageRatio(long[] pids, boolean update) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        float _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeLongArray(pids);
          _data.writeInt(((update)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_queryCpuUsageRatio, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readFloat();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean killProcess(long pid) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeLong(pid);
          boolean _status = mRemote.transact(Stub.TRANSACTION_killProcess, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public github.tornaco.android.thanos.core.os.SwapInfo getSwapInfo() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.os.SwapInfo _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getSwapInfo, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, github.tornaco.android.thanos.core.os.SwapInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      // ******************************************************************
      // Block API
      // Block Receiver/Service/Provider all the time, event the package is running at foreground,
      // may cause the app crash.
      // ******************************************************************
      @Override public void setBlockAllReceiver(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean block) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          _data.writeInt(((block)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setBlockAllReceiver, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isBlockAllReceiver(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isBlockAllReceiver, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setBlockAllService(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean block) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          _data.writeInt(((block)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setBlockAllService, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isBlockAllService(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isBlockAllService, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setBlockAllProvider(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean block) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          _data.writeInt(((block)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setBlockAllProvider, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isBlockAllProvider(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isBlockAllProvider, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      // Return 0 if it fail
      @Override public long getProcessStartTime(int pid) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        long _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(pid);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getProcessStartTime, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readLong();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean isAppForeground(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isAppForeground, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean hasRunningForegroundService(github.tornaco.android.thanos.core.pm.Pkg pkg, int foregroundServicetype) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          _data.writeInt(foregroundServicetype);
          boolean _status = mRemote.transact(Stub.TRANSACTION_hasRunningForegroundService, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.app.ActivityAssistInfo> getTopVisibleActivities() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.app.ActivityAssistInfo> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getTopVisibleActivities, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.app.ActivityAssistInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean checkStartActivity(android.content.Intent intent, int callerUid) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, intent, 0);
          _data.writeInt(callerUid);
          boolean _status = mRemote.transact(Stub.TRANSACTION_checkStartActivity, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean isBgTaskCleanUpSkipForegroundEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isBgTaskCleanUpSkipForegroundEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setBgTaskCleanUpSkipForegroundEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setBgTaskCleanUpSkipForegroundEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      /** return the pid of process. or -1 if no process found */
      @Override public int getPid(github.tornaco.android.thanos.core.os.ProcessName processName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, processName, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getPid, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      /** return the pid of killed process. or -1 if no process found */
      @Override public int killProcessByName(github.tornaco.android.thanos.core.os.ProcessName processName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, processName, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_killProcessByName, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void killProcessByNames(java.util.List<github.tornaco.android.thanos.core.os.ProcessName> processNames) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedList(_data, processNames, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_killProcessByNames, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean dumpHeap(java.lang.String process) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(process);
          boolean _status = mRemote.transact(Stub.TRANSACTION_dumpHeap, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public github.tornaco.android.thanos.core.pm.Pkg getCurrentFrontPkg() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.pm.Pkg _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getCurrentFrontPkg, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean isSmartStandByByPassIfHasVisibleWindows() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isSmartStandByByPassIfHasVisibleWindows, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setSmartStandByByPassIfHasVisibleWindowsEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setSmartStandByByPassIfHasVisibleWindowsEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public float getTotalCpuPercent(boolean update) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        float _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((update)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_getTotalCpuPercent, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readFloat();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.app.usage.PkgCpuUsageStats> getTopNCpuUsagePackages(int n, boolean update) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.app.usage.PkgCpuUsageStats> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(n);
          _data.writeInt(((update)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_getTopNCpuUsagePackages, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.app.usage.PkgCpuUsageStats.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean isPkgResident(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPkgResident, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setPkgResident(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean resident) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          _data.writeInt(((resident)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPkgResident, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setPkgRecentTaskBlurMode(github.tornaco.android.thanos.core.pm.Pkg pkg, int mode) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          _data.writeInt(mode);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPkgRecentTaskBlurMode, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public int getPkgRecentTaskBlurMode(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getPkgRecentTaskBlurMode, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean isSmartStandByUnbindServiceEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isSmartStandByUnbindServiceEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setSmartStandByUnbindServiceEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setSmartStandByUnbindServiceEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
    }
    static final int TRANSACTION_getCurrentFrontApp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_forceStopPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_idlePackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_isPackageIdle = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_checkBroadcastingIntent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_checkService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_checkRestartService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_checkBroadcast = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    static final int TRANSACTION_checkStartProcess = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
    static final int TRANSACTION_onStartProcessLocked = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
    static final int TRANSACTION_getRunningAppProcess = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
    static final int TRANSACTION_getRunningAppPackages = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
    static final int TRANSACTION_getRunningServiceLegacy = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
    static final int TRANSACTION_getRunningAppProcessLegacy = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
    static final int TRANSACTION_getRunningAppsCount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
    static final int TRANSACTION_getRunningAppProcessForPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
    static final int TRANSACTION_isPackageRunning = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
    static final int TRANSACTION_getStartRecordsByPackageName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
    static final int TRANSACTION_getStartRecordBlockedPackages = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
    static final int TRANSACTION_getStartRecordsBlockedCount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
    static final int TRANSACTION_getStartRecordBlockedCountByPackageName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
    static final int TRANSACTION_isStartBlockEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 21);
    static final int TRANSACTION_setStartBlockEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 22);
    static final int TRANSACTION_setPkgStartBlockEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 23);
    static final int TRANSACTION_isPkgStartBlocking = (android.os.IBinder.FIRST_CALL_TRANSACTION + 24);
    static final int TRANSACTION_isCleanUpOnTaskRemovalEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 25);
    static final int TRANSACTION_setCleanUpOnTaskRemovalEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 26);
    static final int TRANSACTION_setPkgCleanUpOnTaskRemovalEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 27);
    static final int TRANSACTION_isPkgCleanUpOnTaskRemovalEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 28);
    static final int TRANSACTION_isBgRestrictEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 29);
    static final int TRANSACTION_setBgRestrictEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 30);
    static final int TRANSACTION_setPkgBgRestrictEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 31);
    static final int TRANSACTION_isPkgBgRestricted = (android.os.IBinder.FIRST_CALL_TRANSACTION + 32);
    static final int TRANSACTION_setBgRestrictNotificationEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 33);
    static final int TRANSACTION_isBgRestrictNotificationEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 34);
    static final int TRANSACTION_isRecentTaskBlurEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 35);
    static final int TRANSACTION_setRecentTaskBlurEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 36);
    static final int TRANSACTION_setPkgRecentTaskBlurEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 37);
    static final int TRANSACTION_isPkgRecentTaskBlurEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 38);
    static final int TRANSACTION_isBgTaskCleanUpSkipAudioFocusedAppEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 39);
    static final int TRANSACTION_setBgTaskCleanUpSkipAudioFocusedAppEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 40);
    static final int TRANSACTION_isBgTaskCleanUpSkipWhichHasNotificationEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 41);
    static final int TRANSACTION_setBgTaskCleanUpSkipWhichHasNotificationEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 42);
    static final int TRANSACTION_setBgTaskCleanUpDelayTimeMills = (android.os.IBinder.FIRST_CALL_TRANSACTION + 43);
    static final int TRANSACTION_getBgTaskCleanUpDelayTimeMills = (android.os.IBinder.FIRST_CALL_TRANSACTION + 44);
    static final int TRANSACTION_notifyTaskCreated = (android.os.IBinder.FIRST_CALL_TRANSACTION + 45);
    static final int TRANSACTION_getMemoryInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 46);
    static final int TRANSACTION_getProcessPss = (android.os.IBinder.FIRST_CALL_TRANSACTION + 47);
    static final int TRANSACTION_onApplicationCrashing = (android.os.IBinder.FIRST_CALL_TRANSACTION + 48);
    static final int TRANSACTION_getPackageNameForTaskId = (android.os.IBinder.FIRST_CALL_TRANSACTION + 49);
    static final int TRANSACTION_isPlatformAppIdleEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 50);
    static final int TRANSACTION_isSmartStandByEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 51);
    static final int TRANSACTION_setSmartStandByEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 52);
    static final int TRANSACTION_setPkgSmartStandByEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 53);
    static final int TRANSACTION_isPkgSmartStandByEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 54);
    static final int TRANSACTION_getLastRecentUsedPackages = (android.os.IBinder.FIRST_CALL_TRANSACTION + 55);
    static final int TRANSACTION_getRecentTaskExcludeSettingForPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 56);
    static final int TRANSACTION_setRecentTaskExcludeSettingForPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 57);
    static final int TRANSACTION_isBgTaskCleanUpSkipWhenHasRecentTaskEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 58);
    static final int TRANSACTION_setBgTaskCleanUpSkipWhenHasRecentTaskEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 59);
    static final int TRANSACTION_launchAppDetailsActivity = (android.os.IBinder.FIRST_CALL_TRANSACTION + 60);
    static final int TRANSACTION_resetStartRecordsBlocked = (android.os.IBinder.FIRST_CALL_TRANSACTION + 61);
    static final int TRANSACTION_addApp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 62);
    static final int TRANSACTION_isStartRuleEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 63);
    static final int TRANSACTION_setStartRuleEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 64);
    static final int TRANSACTION_addStartRule = (android.os.IBinder.FIRST_CALL_TRANSACTION + 65);
    static final int TRANSACTION_deleteStartRule = (android.os.IBinder.FIRST_CALL_TRANSACTION + 66);
    static final int TRANSACTION_getAllStartRules = (android.os.IBinder.FIRST_CALL_TRANSACTION + 67);
    static final int TRANSACTION_isStandbyRuleEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 68);
    static final int TRANSACTION_setStandbyRuleEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 69);
    static final int TRANSACTION_addStandbyRule = (android.os.IBinder.FIRST_CALL_TRANSACTION + 70);
    static final int TRANSACTION_deleteStandbyRule = (android.os.IBinder.FIRST_CALL_TRANSACTION + 71);
    static final int TRANSACTION_getAllStandbyRules = (android.os.IBinder.FIRST_CALL_TRANSACTION + 72);
    static final int TRANSACTION_getRunningAppServiceForPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 73);
    static final int TRANSACTION_hasRunningServiceForPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 74);
    static final int TRANSACTION_getUserInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 75);
    static final int TRANSACTION_stopService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 76);
    static final int TRANSACTION_killBackgroundProcesses = (android.os.IBinder.FIRST_CALL_TRANSACTION + 77);
    static final int TRANSACTION_isSmartStandByStopServiceEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 78);
    static final int TRANSACTION_setSmartStandByStopServiceEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 79);
    static final int TRANSACTION_isSmartStandByInactiveEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 80);
    static final int TRANSACTION_setSmartStandByInactiveEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 81);
    static final int TRANSACTION_isSmartStandByByPassIfHasNotificationEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 82);
    static final int TRANSACTION_setSmartStandByByPassIfHasNotificationEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 83);
    static final int TRANSACTION_isSmartStandByBlockBgServiceStartEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 84);
    static final int TRANSACTION_setSmartStandByBlockBgServiceStartEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 85);
    static final int TRANSACTION_getStartRecordAllowedPackages = (android.os.IBinder.FIRST_CALL_TRANSACTION + 86);
    static final int TRANSACTION_getStartRecordsAllowedCount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 87);
    static final int TRANSACTION_getStartRecordAllowedCountByPackageName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 88);
    static final int TRANSACTION_getStartRecordsAllowedByPackageName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 89);
    static final int TRANSACTION_getStartRecordsBlockedByPackageName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 90);
    static final int TRANSACTION_resetStartRecordsAllowed = (android.os.IBinder.FIRST_CALL_TRANSACTION + 91);
    static final int TRANSACTION_getAllStartRecordsWithRes = (android.os.IBinder.FIRST_CALL_TRANSACTION + 92);
    static final int TRANSACTION_getAllStartRecords = (android.os.IBinder.FIRST_CALL_TRANSACTION + 93);
    static final int TRANSACTION_dump = (android.os.IBinder.FIRST_CALL_TRANSACTION + 94);
    static final int TRANSACTION_dumpCpu = (android.os.IBinder.FIRST_CALL_TRANSACTION + 95);
    static final int TRANSACTION_setNetStatTrackerEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 96);
    static final int TRANSACTION_isNetStatTrackerEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 97);
    static final int TRANSACTION_checkGetContentProvider = (android.os.IBinder.FIRST_CALL_TRANSACTION + 98);
    static final int TRANSACTION_getAllStartRecordsForPackageSetWithRes = (android.os.IBinder.FIRST_CALL_TRANSACTION + 99);
    static final int TRANSACTION_isCachedAppsFreezerSupported = (android.os.IBinder.FIRST_CALL_TRANSACTION + 100);
    static final int TRANSACTION_freezeApp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 101);
    static final int TRANSACTION_unfreezeApp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 102);
    static final int TRANSACTION_freezeAppProcess = (android.os.IBinder.FIRST_CALL_TRANSACTION + 103);
    static final int TRANSACTION_unfreezeAppProcess = (android.os.IBinder.FIRST_CALL_TRANSACTION + 104);
    static final int TRANSACTION_updateProcessCpuUsageStats = (android.os.IBinder.FIRST_CALL_TRANSACTION + 105);
    static final int TRANSACTION_queryProcessCpuUsageStats = (android.os.IBinder.FIRST_CALL_TRANSACTION + 106);
    static final int TRANSACTION_queryCpuUsageRatio = (android.os.IBinder.FIRST_CALL_TRANSACTION + 107);
    static final int TRANSACTION_killProcess = (android.os.IBinder.FIRST_CALL_TRANSACTION + 108);
    static final int TRANSACTION_getSwapInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 109);
    static final int TRANSACTION_setBlockAllReceiver = (android.os.IBinder.FIRST_CALL_TRANSACTION + 110);
    static final int TRANSACTION_isBlockAllReceiver = (android.os.IBinder.FIRST_CALL_TRANSACTION + 111);
    static final int TRANSACTION_setBlockAllService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 112);
    static final int TRANSACTION_isBlockAllService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 113);
    static final int TRANSACTION_setBlockAllProvider = (android.os.IBinder.FIRST_CALL_TRANSACTION + 114);
    static final int TRANSACTION_isBlockAllProvider = (android.os.IBinder.FIRST_CALL_TRANSACTION + 115);
    static final int TRANSACTION_getProcessStartTime = (android.os.IBinder.FIRST_CALL_TRANSACTION + 116);
    static final int TRANSACTION_isAppForeground = (android.os.IBinder.FIRST_CALL_TRANSACTION + 117);
    static final int TRANSACTION_hasRunningForegroundService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 118);
    static final int TRANSACTION_getTopVisibleActivities = (android.os.IBinder.FIRST_CALL_TRANSACTION + 119);
    static final int TRANSACTION_checkStartActivity = (android.os.IBinder.FIRST_CALL_TRANSACTION + 120);
    static final int TRANSACTION_isBgTaskCleanUpSkipForegroundEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 121);
    static final int TRANSACTION_setBgTaskCleanUpSkipForegroundEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 122);
    static final int TRANSACTION_getPid = (android.os.IBinder.FIRST_CALL_TRANSACTION + 123);
    static final int TRANSACTION_killProcessByName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 124);
    static final int TRANSACTION_killProcessByNames = (android.os.IBinder.FIRST_CALL_TRANSACTION + 125);
    static final int TRANSACTION_dumpHeap = (android.os.IBinder.FIRST_CALL_TRANSACTION + 126);
    static final int TRANSACTION_getCurrentFrontPkg = (android.os.IBinder.FIRST_CALL_TRANSACTION + 127);
    static final int TRANSACTION_isSmartStandByByPassIfHasVisibleWindows = (android.os.IBinder.FIRST_CALL_TRANSACTION + 128);
    static final int TRANSACTION_setSmartStandByByPassIfHasVisibleWindowsEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 129);
    static final int TRANSACTION_getTotalCpuPercent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 130);
    static final int TRANSACTION_getTopNCpuUsagePackages = (android.os.IBinder.FIRST_CALL_TRANSACTION + 131);
    static final int TRANSACTION_isPkgResident = (android.os.IBinder.FIRST_CALL_TRANSACTION + 132);
    static final int TRANSACTION_setPkgResident = (android.os.IBinder.FIRST_CALL_TRANSACTION + 133);
    static final int TRANSACTION_setPkgRecentTaskBlurMode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 134);
    static final int TRANSACTION_getPkgRecentTaskBlurMode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 135);
    static final int TRANSACTION_isSmartStandByUnbindServiceEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 136);
    static final int TRANSACTION_setSmartStandByUnbindServiceEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 137);
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.app.IActivityManager";
  public java.lang.String getCurrentFrontApp() throws android.os.RemoteException;
  public void forceStopPackage(github.tornaco.android.thanos.core.pm.Pkg pkg, java.lang.String reason) throws android.os.RemoteException;
  public void idlePackage(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  public boolean isPackageIdle(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  public boolean checkBroadcastingIntent(android.content.Intent intent) throws android.os.RemoteException;
  public boolean checkService(android.content.Intent intent, android.content.ComponentName service, int callerUid, int userId) throws android.os.RemoteException;
  public boolean checkRestartService(java.lang.String packageName, android.content.ComponentName componentName) throws android.os.RemoteException;
  public boolean checkBroadcast(android.content.Intent intent, int receiverUid, int callerUid) throws android.os.RemoteException;
  public boolean checkStartProcess(java.lang.String processName, android.content.pm.ApplicationInfo applicationInfo, java.lang.String hostType, java.lang.String hostName) throws android.os.RemoteException;
  public void onStartProcessLocked(android.content.pm.ApplicationInfo appInfo) throws android.os.RemoteException;
  public github.tornaco.android.thanos.core.process.ProcessRecord[] getRunningAppProcess() throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.pm.Pkg> getRunningAppPackages() throws android.os.RemoteException;
  public java.util.List<android.app.ActivityManager.RunningServiceInfo> getRunningServiceLegacy(int max) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.app.RunningAppProcessInfoCompat> getRunningAppProcessLegacy() throws android.os.RemoteException;
  public int getRunningAppsCount() throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.process.ProcessRecord> getRunningAppProcessForPackage(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  public boolean isPackageRunning(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.app.start.StartRecord> getStartRecordsByPackageName(java.lang.String pkgName) throws android.os.RemoteException;
  public java.util.List<java.lang.String> getStartRecordBlockedPackages() throws android.os.RemoteException;
  public long getStartRecordsBlockedCount() throws android.os.RemoteException;
  public long getStartRecordBlockedCountByPackageName(java.lang.String pkgName) throws android.os.RemoteException;
  // 启动管理设置
  public boolean isStartBlockEnabled() throws android.os.RemoteException;
  public void setStartBlockEnabled(boolean enable) throws android.os.RemoteException;
  public void setPkgStartBlockEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean enable) throws android.os.RemoteException;
  public boolean isPkgStartBlocking(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  // Task removal
  public boolean isCleanUpOnTaskRemovalEnabled() throws android.os.RemoteException;
  public void setCleanUpOnTaskRemovalEnabled(boolean enable) throws android.os.RemoteException;
  public void setPkgCleanUpOnTaskRemovalEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean enable) throws android.os.RemoteException;
  public boolean isPkgCleanUpOnTaskRemovalEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  // 后台运行设置
  public boolean isBgRestrictEnabled() throws android.os.RemoteException;
  public void setBgRestrictEnabled(boolean enable) throws android.os.RemoteException;
  public void setPkgBgRestrictEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean enable) throws android.os.RemoteException;
  public boolean isPkgBgRestricted(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  public void setBgRestrictNotificationEnabled(boolean enabled) throws android.os.RemoteException;
  public boolean isBgRestrictNotificationEnabled() throws android.os.RemoteException;
  // Task blur
  public boolean isRecentTaskBlurEnabled() throws android.os.RemoteException;
  public void setRecentTaskBlurEnabled(boolean enable) throws android.os.RemoteException;
  public void setPkgRecentTaskBlurEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean enable) throws android.os.RemoteException;
  public boolean isPkgRecentTaskBlurEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  // Audio focused app.
  public boolean isBgTaskCleanUpSkipAudioFocusedAppEnabled() throws android.os.RemoteException;
  public void setBgTaskCleanUpSkipAudioFocusedAppEnabled(boolean enable) throws android.os.RemoteException;
  // Notification record app.
  public boolean isBgTaskCleanUpSkipWhichHasNotificationEnabled() throws android.os.RemoteException;
  public void setBgTaskCleanUpSkipWhichHasNotificationEnabled(boolean enable) throws android.os.RemoteException;
  // 后台运行锁屏清理延迟
  public void setBgTaskCleanUpDelayTimeMills(long delayMills) throws android.os.RemoteException;
  public long getBgTaskCleanUpDelayTimeMills() throws android.os.RemoteException;
  public void notifyTaskCreated(int taskId, android.content.ComponentName componentName) throws android.os.RemoteException;
  public android.app.ActivityManager.MemoryInfo getMemoryInfo() throws android.os.RemoteException;
  public long[] getProcessPss(int[] pids) throws android.os.RemoteException;
  public void onApplicationCrashing(java.lang.String eventType, java.lang.String processName, github.tornaco.android.thanos.core.process.ProcessRecord process, java.lang.String stackTrace) throws android.os.RemoteException;
  public java.lang.String getPackageNameForTaskId(int taskId) throws android.os.RemoteException;
  public int isPlatformAppIdleEnabled() throws android.os.RemoteException;
  public boolean isSmartStandByEnabled() throws android.os.RemoteException;
  public void setSmartStandByEnabled(boolean enable) throws android.os.RemoteException;
  public void setPkgSmartStandByEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean enable) throws android.os.RemoteException;
  public boolean isPkgSmartStandByEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.pm.Pkg> getLastRecentUsedPackages(int count) throws android.os.RemoteException;
  public int getRecentTaskExcludeSettingForPackage(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  public void setRecentTaskExcludeSettingForPackage(github.tornaco.android.thanos.core.pm.Pkg pkg, int setting) throws android.os.RemoteException;
  // Keep when has recent task.
  public boolean isBgTaskCleanUpSkipWhenHasRecentTaskEnabled() throws android.os.RemoteException;
  public void setBgTaskCleanUpSkipWhenHasRecentTaskEnabled(boolean enable) throws android.os.RemoteException;
  public void launchAppDetailsActivity(java.lang.String pkgName) throws android.os.RemoteException;
  public void resetStartRecordsBlocked() throws android.os.RemoteException;
  public void addApp(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  public boolean isStartRuleEnabled() throws android.os.RemoteException;
  public void setStartRuleEnabled(boolean enable) throws android.os.RemoteException;
  public void addStartRule(java.lang.String rule) throws android.os.RemoteException;
  public void deleteStartRule(java.lang.String rule) throws android.os.RemoteException;
  public java.lang.String[] getAllStartRules() throws android.os.RemoteException;
  public boolean isStandbyRuleEnabled() throws android.os.RemoteException;
  public void setStandbyRuleEnabled(boolean enable) throws android.os.RemoteException;
  public void addStandbyRule(java.lang.String rule) throws android.os.RemoteException;
  public void deleteStandbyRule(java.lang.String rule) throws android.os.RemoteException;
  public java.lang.String[] getAllStandbyRules() throws android.os.RemoteException;
  public github.tornaco.android.thanos.core.app.RunningServiceInfoCompat[] getRunningAppServiceForPackage(java.lang.String pkgName, int userId) throws android.os.RemoteException;
  public boolean hasRunningServiceForPackage(java.lang.String pkgName, int userId) throws android.os.RemoteException;
  public android.content.pm.UserInfo getUserInfo(int userHandle) throws android.os.RemoteException;
  public boolean stopService(android.content.Intent intent) throws android.os.RemoteException;
  public boolean killBackgroundProcesses(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  public boolean isSmartStandByStopServiceEnabled() throws android.os.RemoteException;
  public void setSmartStandByStopServiceEnabled(boolean enable) throws android.os.RemoteException;
  public boolean isSmartStandByInactiveEnabled() throws android.os.RemoteException;
  public void setSmartStandByInactiveEnabled(boolean enable) throws android.os.RemoteException;
  public boolean isSmartStandByByPassIfHasNotificationEnabled() throws android.os.RemoteException;
  public void setSmartStandByByPassIfHasNotificationEnabled(boolean enable) throws android.os.RemoteException;
  public boolean isSmartStandByBlockBgServiceStartEnabled() throws android.os.RemoteException;
  public void setSmartStandByBlockBgServiceStartEnabled(boolean enable) throws android.os.RemoteException;
  public java.util.List<java.lang.String> getStartRecordAllowedPackages() throws android.os.RemoteException;
  public long getStartRecordsAllowedCount() throws android.os.RemoteException;
  public long getStartRecordAllowedCountByPackageName(java.lang.String pkgName) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.app.start.StartRecord> getStartRecordsAllowedByPackageName(java.lang.String pkgName) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.app.start.StartRecord> getStartRecordsBlockedByPackageName(java.lang.String pkgName) throws android.os.RemoteException;
  public void resetStartRecordsAllowed() throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.app.start.StartRecord> getAllStartRecordsWithRes(int appFlags, boolean allowed, boolean blocked) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.app.start.StartRecord> getAllStartRecords(int appFlags) throws android.os.RemoteException;
  public void dump(github.tornaco.android.thanos.core.IPrinter p) throws android.os.RemoteException;
  public void dumpCpu(github.tornaco.android.thanos.core.IPrinter p) throws android.os.RemoteException;
  public void setNetStatTrackerEnabled(boolean enabled) throws android.os.RemoteException;
  public boolean isNetStatTrackerEnabled() throws android.os.RemoteException;
  public boolean checkGetContentProvider(java.lang.String callerPkg, java.lang.String name, int userId) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.app.start.StartRecord> getAllStartRecordsForPackageSetWithRes(java.lang.String pkgSetId, boolean allowed, boolean blocked) throws android.os.RemoteException;
  // ******************************************************************
  // CAF API
  // https://source.android.com/devices/tech/perf/cached-apps-freezer
  //
  // ******************************************************************
  public boolean isCachedAppsFreezerSupported() throws android.os.RemoteException;
  public void freezeApp(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  public void unfreezeApp(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  public void freezeAppProcess(long pid) throws android.os.RemoteException;
  public void unfreezeAppProcess(long pid) throws android.os.RemoteException;
  public void updateProcessCpuUsageStats() throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.app.usage.ProcessCpuUsageStats> queryProcessCpuUsageStats(long[] pids, boolean update) throws android.os.RemoteException;
  public float queryCpuUsageRatio(long[] pids, boolean update) throws android.os.RemoteException;
  public boolean killProcess(long pid) throws android.os.RemoteException;
  public github.tornaco.android.thanos.core.os.SwapInfo getSwapInfo() throws android.os.RemoteException;
  // ******************************************************************
  // Block API
  // Block Receiver/Service/Provider all the time, event the package is running at foreground,
  // may cause the app crash.
  // ******************************************************************
  public void setBlockAllReceiver(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean block) throws android.os.RemoteException;
  public boolean isBlockAllReceiver(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  public void setBlockAllService(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean block) throws android.os.RemoteException;
  public boolean isBlockAllService(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  public void setBlockAllProvider(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean block) throws android.os.RemoteException;
  public boolean isBlockAllProvider(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  // Return 0 if it fail
  public long getProcessStartTime(int pid) throws android.os.RemoteException;
  public boolean isAppForeground(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  public boolean hasRunningForegroundService(github.tornaco.android.thanos.core.pm.Pkg pkg, int foregroundServicetype) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.app.ActivityAssistInfo> getTopVisibleActivities() throws android.os.RemoteException;
  public boolean checkStartActivity(android.content.Intent intent, int callerUid) throws android.os.RemoteException;
  public boolean isBgTaskCleanUpSkipForegroundEnabled() throws android.os.RemoteException;
  public void setBgTaskCleanUpSkipForegroundEnabled(boolean enable) throws android.os.RemoteException;
  /** return the pid of process. or -1 if no process found */
  public int getPid(github.tornaco.android.thanos.core.os.ProcessName processName) throws android.os.RemoteException;
  /** return the pid of killed process. or -1 if no process found */
  public int killProcessByName(github.tornaco.android.thanos.core.os.ProcessName processName) throws android.os.RemoteException;
  public void killProcessByNames(java.util.List<github.tornaco.android.thanos.core.os.ProcessName> processNames) throws android.os.RemoteException;
  public boolean dumpHeap(java.lang.String process) throws android.os.RemoteException;
  public github.tornaco.android.thanos.core.pm.Pkg getCurrentFrontPkg() throws android.os.RemoteException;
  public boolean isSmartStandByByPassIfHasVisibleWindows() throws android.os.RemoteException;
  public void setSmartStandByByPassIfHasVisibleWindowsEnabled(boolean enable) throws android.os.RemoteException;
  public float getTotalCpuPercent(boolean update) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.app.usage.PkgCpuUsageStats> getTopNCpuUsagePackages(int n, boolean update) throws android.os.RemoteException;
  public boolean isPkgResident(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  public void setPkgResident(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean resident) throws android.os.RemoteException;
  public void setPkgRecentTaskBlurMode(github.tornaco.android.thanos.core.pm.Pkg pkg, int mode) throws android.os.RemoteException;
  public int getPkgRecentTaskBlurMode(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  public boolean isSmartStandByUnbindServiceEnabled() throws android.os.RemoteException;
  public void setSmartStandByUnbindServiceEnabled(boolean enable) throws android.os.RemoteException;
  /** @hide */
  static class _Parcel {
    static private <T> T readTypedObject(
        android.os.Parcel parcel,
        android.os.Parcelable.Creator<T> c) {
      if (parcel.readInt() != 0) {
          return c.createFromParcel(parcel);
      } else {
          return null;
      }
    }
    static private <T extends android.os.Parcelable> void writeTypedObject(
        android.os.Parcel parcel, T value, int parcelableFlags) {
      if (value != null) {
        parcel.writeInt(1);
        value.writeToParcel(parcel, parcelableFlags);
      } else {
        parcel.writeInt(0);
      }
    }
    static private <T extends android.os.Parcelable> void writeTypedList(
        android.os.Parcel parcel, java.util.List<T> value, int parcelableFlags) {
      if (value == null) {
        parcel.writeInt(-1);
      } else {
        int N = value.size();
        int i = 0;
        parcel.writeInt(N);
        while (i < N) {
    writeTypedObject(parcel, value.get(i), parcelableFlags);
          i++;
        }
      }
    }
  }
}
