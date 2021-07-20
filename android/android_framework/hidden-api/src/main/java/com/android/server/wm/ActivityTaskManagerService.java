package com.android.server.wm;

import android.app.IApplicationThread;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;

import com.android.server.SystemService;
import com.android.server.am.PendingIntentController;
import com.android.server.firewall.IntentFirewall;

public class ActivityTaskManagerService {
  public ActivityTaskManagerService(Context context) {}

  public void initialize(
      IntentFirewall intentFirewall, PendingIntentController intentController, Looper looper) {}

  public boolean removeTask(int taskId) {
    return false;
  }

  protected RecentTasks createRecentTasks() {
    return null;
  }

  protected ActivityStackSupervisor createStackSupervisor() {
    return null;
  }

  public void moveTaskToFront(
      IApplicationThread appThread,
      String callingPackage,
      int taskId,
      int flags,
      Bundle bOptions) {}

  public static final class Lifecycle extends SystemService {
    @Override
    public void onStart() {}
  }

  // class com.android.server.wm.ActivityTaskManagerInternal
  // com.android.server.wm.ActivityTaskManagerService$LocalService@73f2c51
  abstract /* From final*/ class LocalService extends ActivityTaskManagerInternal {}
}
