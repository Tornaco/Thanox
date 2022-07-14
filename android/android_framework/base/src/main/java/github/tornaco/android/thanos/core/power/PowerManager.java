package github.tornaco.android.thanos.core.power;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class PowerManager {

    private final IPowerManager server;

    @SneakyThrows
    public void reboot() {
        server.reboot();
    }

    @SneakyThrows
    public void softReboot() {
        server.softReboot();
    }

    @SneakyThrows
    public void goToSleep(long delay) {
        server.goToSleep(delay);
    }

    @SneakyThrows
    public void setPowerSaveModeEnabled(boolean enable) {
        server.setPowerSaveModeEnabled(enable);
    }

    @SneakyThrows
    public boolean isPowerSaveModeEnabled() {
        return server.isPowerSaveModeEnabled();
    }

    @SneakyThrows
    public List<SeenWakeLock> getSeenWakeLocks(boolean includeHistory) {
        return server.getSeenWakeLocks(includeHistory);
    }

    @SneakyThrows
    public List<SeenWakeLock> getSeenWakeLocksByPackageName(String packageName, boolean includeHistory) {
        return server.getSeenWakeLocksByPackageName(packageName, includeHistory);
    }

    @SneakyThrows
    public boolean isWakeLockHeld(SeenWakeLock wakelock) {
        return server.isWakeLockHeld(wakelock);
    }

    @SneakyThrows
    public boolean isWakeLockBlockerEnabled() {
        return server.isWakeLockBlockerEnabled();
    }

    @SneakyThrows
    public void setWakeLockBlockerEnabled(boolean enable) {
        server.setWakeLockBlockerEnabled(enable);
    }

    @SneakyThrows
    public void setBlockWakeLock(SeenWakeLock wl, boolean block) {
        server.setBlockWakeLock(wl, block);
    }
}
