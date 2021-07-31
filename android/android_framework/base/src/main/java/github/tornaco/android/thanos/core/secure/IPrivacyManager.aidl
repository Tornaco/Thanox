package github.tornaco.android.thanos.core.secure;

import github.tornaco.android.thanos.core.secure.PrivacyCheatRecord;
import github.tornaco.android.thanos.core.secure.field.Fields;


interface IPrivacyManager {
    boolean isPrivacyEnabled();
    void setPrivacyEnabled(boolean enabled);

    int getPrivacyDataCheatPkgCount();
    long getPrivacyDataCheatRequestCount();

    String getOriginalDeviceId();
    String getOriginalLine1Number();
    String getOriginalSimSerialNumber();
    String getOriginalAndroidId();
    String getOriginalImei(int slotIndex);
    String getOriginalMeid(int slotIndex);

    int getPhoneCount();
    SubscriptionInfo[] getAccessibleSubscriptionInfoList();

    PrivacyCheatRecord[] getPrivacyCheatRecords();
    void clearPrivacyCheatRecords();

    boolean addOrUpdateFieldsProfile(in Fields f);
    boolean deleteFieldsProfile(in Fields f);
    boolean deleteFieldsProfileById(in String id);

    List<Fields> getAllFieldsProfiles();

    void selectFieldsProfileForPackage(String pkg, String profileId);
    String getSelectedFieldsProfileIdForPackage(String pkg);
    Fields getSelectedFieldsProfileForPackage(String pkg, int checkingOp);
    Fields getFieldsProfileById(String id);
    boolean isUidFieldsProfileSelected(int uid);
    boolean isPackageFieldsProfileSelected(String pkg);

    int getUsageForFieldsProfile(String id);
    List<String> getUsagePackagesForFieldsProfile(String id);

    String getOriginalSimCountryIso();
    String getOriginalSimOp(int subId);
    String getOriginalSimOpName(int subId);

    String getOriginalNetworkCountryIso();
    String getOriginalNetworkOp(int subId);
    String getOriginalNetworkOpName(int subId);

}