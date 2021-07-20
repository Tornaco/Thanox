package com.android.server.appop;

import android.os.Handler;
import android.os.IBinder;

import java.io.File;

public class AppOpsService {
  public AppOpsService(File storagePath, Handler handler) {}

  public int checkOperation(int code, int uid, String packageName) {
    return 0;
  }

  public int noteOperation(int code, int uid, String packageName) {
    return 0;
  }

  public int noteProxyOperation(
      int code, int proxyUid, String proxyPackageName, int proxiedUid, String proxiedPackageName) {
    return 0;
  }

  public int startOperation(
      IBinder token, int code, int uid, String packageName, boolean startIfModeDefault) {
    return 0;
  }

  public void finishOperation(IBinder token, int code, int uid, String packageName) {}
}
