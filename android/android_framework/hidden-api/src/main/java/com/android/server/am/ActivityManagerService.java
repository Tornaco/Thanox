package com.android.server.am;

import android.app.ApplicationErrorReport;
import android.app.IApplicationThread;
import android.app.IServiceConnection;
import android.app.ProfilerInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.IIntentReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.TransactionTooLargeException;
import android.util.TimingsTraceLog;

import com.android.server.SystemService;
import com.android.server.firewall.IntentFirewall;
import com.android.server.wm.ActivityTaskManagerService;

public abstract class ActivityManagerService extends Binder {
  public /*final*/ IntentFirewall mIntentFirewall;

  public ActivityManagerService(Context systemContext, ActivityTaskManagerService atm) {}

  public void setSystemProcess() {}

  public boolean removeTask(int taskId) {
    return false;
  }

  public void systemReady(final Runnable goingCallback, TimingsTraceLog traceLog) {}

  public ComponentName startService(
      IApplicationThread caller,
      Intent service,
      String resolvedType,
      boolean requireForeground,
      String callingPackage,
      int userId)
      throws TransactionTooLargeException {
    return null;
  }

  public Intent registerReceiver(
      IApplicationThread caller,
      String callerPackage,
      IIntentReceiver receiver,
      IntentFilter filter,
      String permission,
      int userId,
      int flags) {
    return null;
  }

  public void startProcess(
      String processName,
      ApplicationInfo info,
      boolean knownToBeDead,
      boolean isTop,
      String hostingType,
      ComponentName hostingName) {}

  public int bindIsolatedService(
      IApplicationThread caller,
      IBinder token,
      Intent service,
      String resolvedType,
      IServiceConnection connection,
      int flags,
      String instanceName,
      String callingPackage,
      int userId)
      throws TransactionTooLargeException {
    return 0;
  }

  @Deprecated
  public int startActivity(
      IApplicationThread caller,
      String callingPackage,
      Intent intent,
      String resolvedType,
      IBinder resultTo,
      String resultWho,
      int requestCode,
      int startFlags,
      ProfilerInfo profilerInfo,
      Bundle bOptions) {
    return 0;
  }

  public int startActivityWithFeature(
      IApplicationThread caller,
      String callingPackage,
      String callingFeatureId,
      Intent intent,
      String resolvedType,
      IBinder resultTo,
      String resultWho,
      int requestCode,
      int startFlags,
      ProfilerInfo profilerInfo,
      Bundle bOptions) {
    return 0;
  }

  public void handleApplicationCrash(
      IBinder app, ApplicationErrorReport.ParcelableCrashInfo crashInfo) {}

  private ProcessRecord findAppProcess(IBinder app, String reason) {
    return null;
  }

  public static final class Lifecycle extends SystemService {
    @Override
    public void onStart() {}
  }
}
