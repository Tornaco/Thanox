package github.tornaco.android.thanos.core.power;

import github.tornaco.android.thanos.core.IPrinter;

interface IPowerManager {
    void reboot();
    void softReboot();

    void goToSleep(long delay);

    void setPowerSaveModeEnabled(boolean enable);
    boolean isPowerSaveModeEnabled();

    List<SeenWakeLock> getSeenWakeLocks();
    List<SeenWakeLock> getSeenWakeLocksByPackageName(String packageName);

    void dump(in IPrinter p);

    void wakeUp(long delay);

    void setBrightness(int level);

    int getBrightness();

    void setAutoBrightnessEnabled(boolean enable);
    boolean isAutoBrightnessEnabled();
}