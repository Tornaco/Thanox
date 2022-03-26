package github.tornaco.android.thanos.core.profile.handle;

@HandlerName("hw")
public
interface IHW {

    boolean enableWifi();

    boolean disableWifi();

    boolean isWifiEnabled();

    boolean enableLocation();

    boolean disableLocation();

    boolean isLocationEnabled();

    boolean enableBT();

    boolean disableBT();

    boolean isBTEnabled();

    boolean enableNfc();

    boolean disableNfc();

    boolean isNfcEnabled();

    boolean enabledFlashlight();

    boolean disableFlashlight();

    boolean isFlashlightEnabled();

    boolean isFlashlightAvailable();
}
