package com.android.server.firewall;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Handler;

public class IntentFirewall {

  public IntentFirewall(IntentFirewall.AMSInterface ams, Handler handler) {}

  public boolean checkStartActivity(
      Intent intent,
      int callerUid,
      int callerPid,
      String resolvedType,
      ApplicationInfo resolvedApp) {
    return false;
  }

  public boolean checkService(
      ComponentName resolvedService,
      Intent intent,
      int callerUid,
      int callerPid,
      String resolvedType,
      ApplicationInfo resolvedApp) {
    return false;
  }

  public boolean checkBroadcast(
      Intent intent, int callerUid, int callerPid, String resolvedType, int receivingUid) {
    return false;
  }

  public interface AMSInterface {}
}
