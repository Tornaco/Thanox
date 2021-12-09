package github.tornaco.android.thanos.core.pm;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ServiceInfo;
import android.os.ParcelFileDescriptor;

import android.os.RemoteException;
import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.core.annotation.NonNull;
import github.tornaco.android.thanos.core.annotation.Nullable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import util.EncryptUtils;

@AllArgsConstructor
public class PackageManager {

  private IPkgManager pm;

  public static String packageNameOfAndroid() {
    return "android";
  }

  public static String packageNameOfTelecom() {
    return "com.android.server.telecom";
  }

  public static String packageNameOfPhone() {
    return "com.android.phone";
  }

  public static String sharedUserIdOfMedia() {
    return "android.media";
  }

  public static String sharedUserIdOfPhone() {
    return "android.uid.phone";
  }

  public static String sharedUserIdOfSystem() {
    return "android.uid.system";
  }

  @SneakyThrows
  public String[] getPkgNameForUid(int uid) {
    return pm.getPkgNameForUid(uid);
  }

  @SneakyThrows
  public int getUidForPkgName(String pkgName) {
    return pm.getUidForPkgName(pkgName);
  }

  @SneakyThrows
  public List<AppInfo> getInstalledPkgs(int flags) {
    return pm.getInstalledPkgs(flags);
  }

  @SneakyThrows
  public AppInfo getAppInfo(String pkgName) {
    return pm.getAppInfo(pkgName);
  }

  @SneakyThrows
  public String[] getWhiteListPkgs() {
    return pm.getWhiteListPkgs();
  }

  @SneakyThrows
  public boolean isPkgInWhiteList(String pkg) {
    return pm.isPkgInWhiteList(pkg);
  }

  @SneakyThrows
  public void setComponentEnabledSetting(ComponentName componentName, int newState, int flags) {
    pm.setComponentEnabledSetting(componentName, newState, flags);
  }

  @SneakyThrows
  public int getComponentEnabledSetting(ComponentName componentName) {
    return pm.getComponentEnabledSetting(componentName);
  }

  @SneakyThrows
  public boolean getApplicationEnableState(String packageName) {
    return pm.getApplicationEnableState(packageName);
  }

  @SneakyThrows
  public void setApplicationEnableState(String packageName, boolean enable, boolean tmp) {
    pm.setApplicationEnableState(packageName, enable, tmp);
  }

  @SneakyThrows
  public List<ComponentInfo> getActivities(String packageName) {
    return pm.getActivities(packageName);
  }

  @SneakyThrows
  public List<ComponentInfo> getReceivers(String packageName) {
    return pm.getReceivers(packageName);
  }


  @SneakyThrows
  public List<ComponentInfo> getServices(String packageName) {
    return pm.getServices(packageName);
  }

  @SneakyThrows
  @Nullable
  public List<ComponentInfo> getActivitiesInBatch(String packageName, int itemCountInEachBatch,
                                                  int batchIndex) {
    return pm.getActivitiesInBatch(packageName, itemCountInEachBatch, batchIndex);
  }

  @SneakyThrows
  @Nullable
  public List<ComponentInfo> getReceiversInBatch(String packageName, int itemCountInEachBatch,
                                                 int batchIndex) {
    return pm.getReceiversInBatch(packageName, itemCountInEachBatch, batchIndex);
  }

  @SneakyThrows
  @Nullable
  public List<ComponentInfo> getServicesInBatch(String packageName, int itemCountInEachBatch,
                                                int batchIndex) {
    return pm.getServicesInBatch(packageName, itemCountInEachBatch, batchIndex);
  }

  @SneakyThrows
  public int getActivitiesCount(String packageName) {
    return pm.getActivitiesCount(packageName);
  }

  @SneakyThrows
  public int getReceiverCount(String packageName) {
    return pm.getReceiverCount(packageName);
  }

  @SneakyThrows
  public int getServiceCount(String packageName) {
    return pm.getServiceCount(packageName);
  }

  @SneakyThrows
  public boolean isComponentDisabledByThanox(ComponentName componentName) {
    return pm.isComponentDisabledByThanox(componentName);
  }

  @SneakyThrows
  public void setSmartFreezeEnabled(boolean enable) {
    pm.setSmartFreezeEnabled(enable);
  }

  @SneakyThrows
  public boolean isSmartFreezeEnabled() {
    return pm.isSmartFreezeEnabled();
  }

  @SneakyThrows
  public void setPkgSmartFreezeEnabled(String pkgName, boolean enable) {
    pm.setPkgSmartFreezeEnabled(pkgName, enable);
  }

  @SneakyThrows
  public String[] enableAllThanoxDisabledPackages(boolean removeFromSmartFreezeList) {
    return pm.enableAllThanoxDisabledPackages(removeFromSmartFreezeList);
  }

  @SneakyThrows
  public boolean isPkgSmartFreezeEnabled(String pkgName) {
    return pm.isPkgSmartFreezeEnabled(pkgName);
  }

  @SneakyThrows
  public String[] getSmartFreezePkgs() {
    String[] res = pm.getSmartFreezePkgs();
    if (res == null) {
      res = new String[0];
    }
    return res;
  }

  @SneakyThrows
  public void launchSmartFreezePkg(String pkgName) {
    pm.launchSmartFreezePkg(pkgName);
  }

  @SneakyThrows
  public void launchSmartFreezePkgThenKillOrigin(String pkgName, String origin) {
    pm.launchSmartFreezePkgThenKillOrigin(pkgName, origin);
  }

  @SneakyThrows
  public void setSmartFreezeScreenOffCheckEnabled(boolean enable) {
    pm.setSmartFreezeScreenOffCheckEnabled(enable);
  }

  @SneakyThrows
  public boolean isSmartFreezeHidePackageEventEnabled() {
    return pm.isSmartFreezeHidePackageEventEnabled();
  }

  @SneakyThrows
  public void setSmartFreezeHidePackageEventEnabled(boolean enabled) {
    pm.setSmartFreezeHidePackageEventEnabled(enabled);
  }

  @SneakyThrows
  public boolean isSmartFreezeScreenOffCheckEnabled() {
    return pm.isSmartFreezeScreenOffCheckEnabled();
  }

  @SneakyThrows
  public void setSmartFreezeScreenOffCheckDelay(long delay) {
    pm.setSmartFreezeScreenOffCheckDelay(delay);
  }

  @SneakyThrows
  public long getSmartFreezeScreenOffCheckDelay() {
    return pm.getSmartFreezeScreenOffCheckDelay();
  }

  @SneakyThrows
  public Intent queryLaunchIntentForPackage(String pkgName) {
    return pm.queryLaunchIntentForPackage(pkgName);
  }

  @Nullable
  public String resolveShortcutPackageName(String pkgName) {
    // Compat.
    if (!pkgName.endsWith("_thanox_shortcut_enc")) {
      return pkgName.replace(BuildProp.THANOS_SHORTCUT_PKG_NAME_PREFIX, "");
    }
    try {
      return EncryptUtils.decrypt(
          pkgName.replace(BuildProp.THANOS_SHORTCUT_PKG_NAME_PREFIX, "")
              .replace("_thanox_shortcut_enc", ""));
    } catch (Throwable e) {
      return null;
    }
  }

  @NonNull
  public String createShortcutStubPkgName(AppInfo appInfo) {
    try {
      return BuildProp.THANOS_SHORTCUT_PKG_NAME_PREFIX
          + EncryptUtils.encrypt(appInfo.getPkgName())
          + "_thanox_shortcut_enc";
    } catch (Throwable e) {
      return BuildProp.THANOS_SHORTCUT_PKG_NAME_PREFIX + appInfo.getPkgName();
    }
  }

  @SneakyThrows
  public boolean deviceHasGms() {
    return pm.deviceHasGms();
  }

  @SneakyThrows
  public boolean verifyBillingState() {
    return pm.verifyBillingState();
  }

  @SneakyThrows
  public boolean isProtectedWhitelistEnabled() {
    return pm.isProtectedWhitelistEnabled();
  }

  @SneakyThrows
  public void setProtectedWhitelistEnabled(boolean enable) {
    pm.setProtectedWhitelistEnabled(enable);
  }

  @SneakyThrows
  public void addPlugin(ParcelFileDescriptor pfd, String pluginPackageName,
      AddPluginCallback callback) {
    pm.addPlugin(pfd, pluginPackageName, callback.getStub());
  }

  @SneakyThrows
  public boolean hasPlugin(String pluginPackageName) {
    return pm.hasPlugin(pluginPackageName);
  }

  @SneakyThrows
  public void removePlugin(String pluginPackageName) {
    pm.removePlugin(pluginPackageName);
  }

  @SneakyThrows
  public void setPackageBlockUninstallEnabled(String pkgName, boolean enable) {
    pm.setPackageBlockUninstallEnabled(pkgName, enable);
  }

  @SneakyThrows
  public boolean isPackageBlockUninstallEnabled(String pkgName) {
    return pm.isPackageBlockUninstallEnabled(pkgName);
  }

  @SneakyThrows
  public void setPackageBlockClearDataEnabled(String pkgName, boolean enable) {
    pm.setPackageBlockClearDataEnabled(pkgName, enable);
  }

  @SneakyThrows
  public boolean isPackageBlockClearDataEnabled(String pkgName) {
    return pm.isPackageBlockClearDataEnabled(pkgName);
  }

  @SneakyThrows
  public int getInstalledPackagesCount(int appFlags) {
    return pm.getInstalledPackagesCount(appFlags);
  }

  @SneakyThrows
  public PackageSet createPackageSet(String label) {
    return pm.createPackageSet(label);
  }

  @SneakyThrows
  public boolean removePackageSet(String id) {
    return pm.removePackageSet(id);
  }

  @SneakyThrows
  public PackageSet getPackageSetById(String id, boolean withPackages) {
    return pm.getPackageSetById(id, withPackages);
  }

  @SneakyThrows
  public List<PackageSet> getAllPackageSets(boolean withPackages) {
    return pm.getAllPackageSets(withPackages);
  }

  @SneakyThrows
  public List<String> getAllPackageSetIds() {
    return pm.getAllPackageSetIds();
  }

  @SneakyThrows
  public void addToPackageSet(String pkg, String id) {
    pm.addToPackageSet(pkg, id);
  }

  @SneakyThrows
  public void removeFromPackageSet(String pkg, String id) {
    pm.removeFromPackageSet(pkg, id);
  }

  @SneakyThrows
  public List<PackageSet> getPackageSetThatContainsPkg(String pkg) {
    return pm.getPackageSetThatContainsPkg(pkg);
  }

  @SneakyThrows
  public List<String> getPackageSetLabelsThatContainsPkg(String pkg) {
    return pm.getPackageSetLabelsThatContainsPkg(pkg);
  }

  @SneakyThrows
  public void setFreezePkgWithSuspendEnabled(boolean enable) {
    pm.setFreezePkgWithSuspendEnabled(enable);
  }

  @SneakyThrows
  public boolean isFreezePkgWithSuspendEnabled() {
    return pm.isFreezePkgWithSuspendEnabled();
  }

  @SneakyThrows
  public List<AppInfo> getInstalledPkgsByPackageSetId(String pkgSetId) {
    return pm.getInstalledPkgsByPackageSetId(pkgSetId);
  }

  @SneakyThrows
  public void registerPackageSetChangeListener(PackageSetChangeListener listener) {
    pm.registerPackageSetChangeListener(listener.stub);
  }

  @SneakyThrows
  public void unRegisterPackageSetChangeListener(PackageSetChangeListener listener) {
    pm.unRegisterPackageSetChangeListener(listener.stub);
  }

  @SneakyThrows
  public void setEnablePackageOnLaunchRequestEnabled(String pkg, boolean enable) {
    pm.setEnablePackageOnLaunchRequestEnabled(pkg, enable);
  }

  @SneakyThrows
  public boolean isEnablePackageOnLaunchRequestEnabled(String pkg) {
    return pm.isEnablePackageOnLaunchRequestEnabled(pkg);
  }
}
