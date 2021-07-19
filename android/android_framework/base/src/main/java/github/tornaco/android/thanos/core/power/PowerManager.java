package github.tornaco.android.thanos.core.power;

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
}
