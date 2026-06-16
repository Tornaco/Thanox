package github.tornaco.android.thanos.core.location;

/**
 * In-memory static holder for position travel state.
 * Shared between service (system_server IPC) and Xposed hooks (same process).
 * Auto-resets to OFF on every reboot (no persistence).
 */
public final class PositionTravelState {

    private static volatile boolean sEnabled = false;
    private static volatile double sLatitude = 39.9042;  // Beijing
    private static volatile double sLongitude = 116.4074; // Beijing

    private PositionTravelState() {
    }

    public static boolean isEnabled() {
        return sEnabled;
    }

    public static void setEnabled(boolean enabled) {
        sEnabled = enabled;
    }

    public static double getLatitude() {
        return sLatitude;
    }

    public static double getLongitude() {
        return sLongitude;
    }

    public static void setCoordinates(double latitude, double longitude) {
        sLatitude = latitude;
        sLongitude = longitude;
    }
}
