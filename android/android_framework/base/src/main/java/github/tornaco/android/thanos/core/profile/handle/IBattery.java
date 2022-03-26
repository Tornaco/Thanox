package github.tornaco.android.thanos.core.profile.handle;

import github.tornaco.android.thanos.core.profile.state.BatteryState;

/**
 * Battery API
 *
 * @Since Thanox-v3.9.4
 */
@HandlerName("battery")
public interface IBattery {
    BatteryState getBatteryState();
}
