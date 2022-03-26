package github.tornaco.android.thanos.core.profile.handle;

@HandlerName("data")
public
interface IData {

    void setDataEnabled(boolean enable);

    void setDataEnabled(int subId, boolean enable);

    void setDataEnabledForSlot(int slotId, boolean enable);

    boolean isDataEnabled();

    boolean isDataEnabledForSlot(int slotId);

    /**
     * Network type enums, primarily used by android/telephony/TelephonyManager.java.
     * Do not add negative types.
     * enum NetworkTypeEnum {
     * NETWORK_TYPE_UNKNOWN = 0;
     * NETWORK_TYPE_GPRS = 1;
     * NETWORK_TYPE_EDGE = 2;
     * NETWORK_TYPE_UMTS = 3;
     * NETWORK_TYPE_CDMA = 4;
     * NETWORK_TYPE_EVDO_0 = 5;
     * NETWORK_TYPE_EVDO_A = 6;
     * NETWORK_TYPE_1XRTT = 7;
     * NETWORK_TYPE_HSDPA = 8;
     * NETWORK_TYPE_HSUPA = 9;
     * NETWORK_TYPE_HSPA = 10;
     * NETWORK_TYPE_IDEN = 11;
     * NETWORK_TYPE_EVDO_B = 12;
     * NETWORK_TYPE_LTE = 13;
     * NETWORK_TYPE_EHRPD = 14;
     * NETWORK_TYPE_HSPAP = 15;
     * NETWORK_TYPE_GSM = 16;
     * NETWORK_TYPE_TD_SCDMA = 17;
     * NETWORK_TYPE_IWLAN = 18;
     * NETWORK_TYPE_LTE_CA = 19;
     * NETWORK_TYPE_NR = 20;
     * }
     */
    void setDataNetworkType(int type);

    void setDataNetworkTypeForPhone(int phoneId, int type);

    void setDataNetworkTypeForSlot(int slotId, int type);

    int getCurrentPreferredNetworkMode();

    int getCurrentPreferredNetworkMode(int subId);

    int getCurrentPreferredNetworkModeForSlot(int slotId);

    boolean isAirplaneModeEnabled();
}
