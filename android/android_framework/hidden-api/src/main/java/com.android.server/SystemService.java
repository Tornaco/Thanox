package com.android.server;

public abstract class SystemService {
  public static final int PHASE_WAIT_FOR_DEFAULT_DISPLAY = 100;
  public static final int PHASE_LOCK_SETTINGS_READY = 480;
  public static final int PHASE_SYSTEM_SERVICES_READY = 500;
  public static final int PHASE_ACTIVITY_MANAGER_READY = 550;
  public static final int PHASE_THIRD_PARTY_APPS_CAN_START = 600;
  public static final int PHASE_BOOT_COMPLETED = 1000;

  public abstract void onStart();

  public void onBootPhase(int phase) {}

  public void onStartUser(int userHandle) {}

  public void onUnlockUser(int userHandle) {}

  public void onSwitchUser(int userHandle) {}

  public void onStopUser(int userHandle) {}

  public void onCleanupUser(int userHandle) {}
}
