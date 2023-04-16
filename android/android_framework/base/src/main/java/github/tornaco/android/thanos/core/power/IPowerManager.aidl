package github.tornaco.android.thanos.core.power;

import github.tornaco.android.thanos.core.IPrinter;
import github.tornaco.android.thanos.core.pm.Pkg;
import github.tornaco.android.thanos.core.power.WakeLockStats;

interface IPowerManager {
    void reboot();
    void softReboot();

    void goToSleep(long delay);

    void setPowerSaveModeEnabled(boolean enable);
    boolean isPowerSaveModeEnabled();

    List<SeenWakeLock> getSeenWakeLocks(boolean includeHistory);
    List<SeenWakeLock> getSeenWakeLocksForPkg(in Pkg pkg, boolean includeHistory);
    List<WakeLockStats> getSeenWakeLocksStats(boolean includeHistory, boolean heldOnly);
    boolean isWakeLockHeld(in SeenWakeLock wakelock);

    void dump(in IPrinter p);

    void wakeUp(long delay);

    void setBrightness(int level);

    int getBrightness();

    void setAutoBrightnessEnabled(boolean enable);
    boolean isAutoBrightnessEnabled();

    boolean isWakeLockBlockerEnabled();
    void setWakeLockBlockerEnabled(boolean enable);

    void setBlockWakeLock(in SeenWakeLock wl, boolean block);
}