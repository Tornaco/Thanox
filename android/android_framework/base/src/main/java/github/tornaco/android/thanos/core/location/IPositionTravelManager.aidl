package github.tornaco.android.thanos.core.location;

interface IPositionTravelManager {
    boolean isEnabled();
    void setEnabled(boolean enabled);
    double getLatitude();
    double getLongitude();
    void setCoordinates(double latitude, double longitude);
}
