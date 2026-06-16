package github.tornaco.android.thanos.core.location;

import lombok.SneakyThrows;

public class PositionTravelManager {
    private final IPositionTravelManager manager;

    public PositionTravelManager(IPositionTravelManager manager) {
        this.manager = manager;
    }

    @SneakyThrows
    public boolean isEnabled() {
        return manager.isEnabled();
    }

    @SneakyThrows
    public void setEnabled(boolean enabled) {
        manager.setEnabled(enabled);
    }

    @SneakyThrows
    public double getLatitude() {
        return manager.getLatitude();
    }

    @SneakyThrows
    public double getLongitude() {
        return manager.getLongitude();
    }

    @SneakyThrows
    public void setCoordinates(double latitude, double longitude) {
        manager.setCoordinates(latitude, longitude);
    }
}
