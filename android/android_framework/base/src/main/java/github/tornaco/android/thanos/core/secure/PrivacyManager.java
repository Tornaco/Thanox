package github.tornaco.android.thanos.core.secure;

import android.os.RemoteException;
import android.telephony.SubscriptionInfo;

import java.util.List;

import github.tornaco.android.thanos.core.secure.field.Fields;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class PrivacyManager {

  public static class PrivacyOp {

    public static final int OP_NO_OP = -1;

    public static final int OP_ANDROID_ID = 0x1;
    public static final int OP_DEVICE_ID = 0x2;
    public static final int OP_LINE1NUM = 0x3;
    public static final int OP_SIM_SERIAL = 0x4;
    public static final int OP_IMEI = 0x5;
    public static final int OP_MEID = 0x6;

    // SIM
    public static final int OP_SIM_CONTRY_ISO = 0x7;
    public static final int OP_SIM_OPERATOR_NAME = 0x8;
    public static final int OP_SIM_OPERATOR = 0x9;

    public static final int OP_NETWORK_CONTRY_ISO = 0x10;
    public static final int OP_NETWORK_OPERATOR_NAME = 0x11;
    public static final int OP_NETWORK_OPERATOR = 0x12;
  }

  private final IPrivacyManager server;

  @SneakyThrows
  public boolean isPrivacyEnabled() {
    return server.isPrivacyEnabled();
  }

  @SneakyThrows
  public void setPrivacyEnabled(boolean enabled) {
    server.setPrivacyEnabled(enabled);
  }

  @SneakyThrows
  public int getPrivacyDataCheatPkgCount() {
    return server.getPrivacyDataCheatPkgCount();
  }

  @SneakyThrows
  public long getPrivacyDataCheatRequestCount() {
    return server.getPrivacyDataCheatRequestCount();
  }

  @SneakyThrows
  public String getOriginalDeviceId() {
    return server.getOriginalDeviceId();
  }

  @SneakyThrows
  public String getOriginalLine1Number() {
    return server.getOriginalLine1Number();
  }

  @SneakyThrows
  public String getOriginalSimSerialNumber() {
    return server.getOriginalSimSerialNumber();
  }

  @SneakyThrows
  public String getOriginalAndroidId() {
    return server.getOriginalAndroidId();
  }

  @SneakyThrows
  public String getOriginalImei(int slotIndex) {
    return server.getOriginalImei(slotIndex);
  }

  @SneakyThrows
  public String getOriginalMeid(int slotIndex) {
    return server.getOriginalMeid(slotIndex);
  }

  @SneakyThrows
  public int getPhoneCount() {
    return server.getPhoneCount();
  }

  @SneakyThrows
  public SubscriptionInfo[] getAccessibleSubscriptionInfoList() {
    return server.getAccessibleSubscriptionInfoList();
  }

  @SneakyThrows
  public PrivacyCheatRecord[] getPrivacyCheatRecords() {
    return server.getPrivacyCheatRecords();
  }

  @SneakyThrows
  public void clearPrivacyCheatRecords() {
    server.clearPrivacyCheatRecords();
  }

  @SneakyThrows
  public boolean addOrUpdateFieldsProfile(Fields f) {
    return server.addOrUpdateFieldsProfile(f);
  }

  @SneakyThrows
  public boolean deleteFieldsProfile(Fields f) {
    return server.deleteFieldsProfile(f);
  }

  @SneakyThrows
  public boolean deleteFieldsProfileById(String id) {
    return server.deleteFieldsProfileById(id);
  }

  @SneakyThrows
  public List<Fields> getAllFieldsProfiles() {
    return server.getAllFieldsProfiles();
  }

  @SneakyThrows
  public void selectFieldsProfileForPackage(String pkg, String profileId) {
    server.selectFieldsProfileForPackage(pkg, profileId);
  }

  @SneakyThrows
  public String getSelectedFieldsProfileIdForPackage(String pkg) {
    return server.getSelectedFieldsProfileIdForPackage(pkg);
  }

  @SneakyThrows
  public Fields getSelectedFieldsProfileForPackage(String pkg, int checkingOp) {
    return server.getSelectedFieldsProfileForPackage(pkg, checkingOp);
  }

  @SneakyThrows
  public Fields getFieldsProfileById(String id) {
    return server.getFieldsProfileById(id);
  }

  @SneakyThrows
  public boolean isUidFieldsProfileSelected(int uid) {
    return server.isUidFieldsProfileSelected(uid);
  }

  @SneakyThrows
  public boolean isPackageFieldsProfileSelected(String pkg) {
    return server.isPackageFieldsProfileSelected(pkg);
  }

  @SneakyThrows
  public int getUsageForFieldsProfile(String id) {
    return server.getUsageForFieldsProfile(id);
  }

  @SneakyThrows
  public List<String> getUsagePackagesForFieldsProfile(String id) {
    return server.getUsagePackagesForFieldsProfile(id);
  }

  @SneakyThrows
  public String getOriginalSimCountryIso() {
    return server.getOriginalSimCountryIso();
  }

  @SneakyThrows
  public String getOriginalNetworkOp(int subId) {
    return server.getOriginalNetworkOp(subId);
  }

  @SneakyThrows
  public String getOriginalNetworkOpName(int subId) {
    return server.getOriginalNetworkOpName(subId);
  }

  @SneakyThrows
  public String getOriginalSimOp(int subId) {
    return server.getOriginalSimOp(subId);
  }

  @SneakyThrows
  public String getOriginalSimOpName(int subId) {
    return server.getOriginalSimOpName(subId);
  }

  @SneakyThrows
  public String getOriginalNetworkCountryIso() {
    return server.getOriginalNetworkCountryIso();
  }
}
