package github.tornaco.android.thanos.core.pm;

import github.tornaco.android.thanos.core.pm.IAddPluginCallback;
import github.tornaco.android.thanos.core.pm.IPackageSetChangeListener;
import github.tornaco.android.thanos.core.pm.IPackageEnableStateChangeListener;
import github.tornaco.android.thanos.core.pm.ComponentInfo;
import github.tornaco.android.thanos.core.pm.Pkg;
import github.tornaco.android.thanos.core.IPrinter;

interface IPkgManager {
    String[] getPkgNameForUid(int uid);
    int getUidForPkgName(in Pkg pkg);

    // ApplicationInfo
    List<AppInfo> getInstalledPkgs(int flags);
    /** @deprecated use {@link #getAppInfoForUser} instead */
    AppInfo getAppInfo(String pkgName);

    String[] getWhiteListPkgs();
    boolean isPkgInWhiteList(String pkg);

    void setComponentEnabledSetting(int userId, in ComponentName componentName, int newState, int flags);
    int getComponentEnabledSetting(int userId, in ComponentName componentName);
    boolean isComponentDisabledByThanox(int userId, in ComponentName componentName);

    boolean getApplicationEnableState(in Pkg pkg);
    void setApplicationEnableState(in Pkg pkg, boolean enable, boolean tmp);

    List<ComponentInfo> getActivities(int userId, String packageName);
    int getActivitiesCount(String packageName);
    List<ComponentInfo> getActivitiesInBatch(int userId, String packageName, int itemCountInEachBatch, int batchIndex);

    List<ComponentInfo> getReceivers(int userId, String packageName);
    int getReceiverCount(String packageName);
    List<ComponentInfo> getReceiversInBatch(int userId, String packageName, int itemCountInEachBatch, int batchIndex);

    List<ComponentInfo> getServices(int userId, String packageName);
    int getServiceCount(String packageName);
    List<ComponentInfo> getServicesInBatch(int userId, String packageName, int itemCountInEachBatch, int batchIndex);

    void setSmartFreezeEnabled(boolean enable);
    boolean isSmartFreezeEnabled();
    void setPkgSmartFreezeEnabled(in Pkg pkgName, boolean enable);
    boolean isPkgSmartFreezeEnabled(in Pkg pkgName);
    List<Pkg> getSmartFreezePkgs();
    /** @deprecated use {@link #launchSmartFreezePkgForUser} instead */
    void launchSmartFreezePkg(String pkgName);

    void setSmartFreezeScreenOffCheckEnabled(boolean enable);
    boolean isSmartFreezeScreenOffCheckEnabled();
    void setSmartFreezeScreenOffCheckDelay(long delay);
    long getSmartFreezeScreenOffCheckDelay();

    Intent queryLaunchIntentForPackage(String pkgName);

    List<Pkg> enableAllThanoxDisabledPackages(boolean removeFromSmartFreezeList);

    boolean deviceHasGms();
    boolean verifyBillingState();

     /** @deprecated use {@link #launchSmartFreezePkgThenKillOriginForUser} instead */
    void launchSmartFreezePkgThenKillOrigin(String pkgName, String origin);

    boolean isProtectedWhitelistEnabled();
    void setProtectedWhitelistEnabled(boolean enable);

    oneway void addPlugin(in ParcelFileDescriptor pfd, String pluginPackageName,in IAddPluginCallback callback);
    void removePlugin(String pluginPackageName);
    boolean hasPlugin(String pluginPackageName);

    boolean isSmartFreezeHidePackageEventEnabled();
    void setSmartFreezeHidePackageEventEnabled(boolean enabled);

    void dump(in IPrinter p);

    void setPackageBlockUninstallEnabled(String pkgName, boolean enable);
    boolean isPackageBlockUninstallEnabled(String pkgName);

    void setPackageBlockClearDataEnabled(String pkgName, boolean enable);
    boolean isPackageBlockClearDataEnabled(String pkgName);

    int getInstalledPackagesCount(int appFlags);

    PackageSet createPackageSet(String label);
    boolean removePackageSet(String id);
    PackageSet getPackageSetById(String id, boolean withPackages, boolean shouldFilterUserWhiteList);
    List<PackageSet> getAllPackageSets(boolean withPackages);
    List<String> getAllPackageSetIds();
    void addToPackageSet(in Pkg pkg, String id);
    void removeFromPackageSet(in Pkg pkg, String id);

    List<PackageSet> getPackageSetThatContainsPkg(in Pkg pkg);
    List<String> getPackageSetLabelsThatContainsPkg(in Pkg pkg);

    void setFreezePkgWithSuspendEnabled(boolean enable);
    boolean isFreezePkgWithSuspendEnabled();

    List<AppInfo> getInstalledPkgsByPackageSetId(String pkgSetId);

    void registerPackageSetChangeListener(in IPackageSetChangeListener listener);
    void unRegisterPackageSetChangeListener(in IPackageSetChangeListener listener);

    void setEnablePackageOnLaunchRequestEnabled(in Pkg pkg, boolean enable);
    boolean isEnablePackageOnLaunchRequestEnabled(in Pkg pkg);

    List<ComponentInfo> getProviders(int userId, String packageName);

    // Wrap api to skip permission check
    String[] getPackagesForUid(int uid);

    String mayEnableAppOnStartActivityIntent(in Intent intent, int userId);

    boolean isEnablePkgOnLaunchByDefault();
    void setEnablePkgOnLaunchByDefaultEnabled(boolean byDefault);

    AppInfo getAppInfoForUser(String pkgName, int userId);
    void launchSmartFreezePkgForUser(in Pkg pkg);

    void launchSmartFreezePkgThenKillOriginForUser(in Pkg targetPkg, String origin);

    void setDOLTipsEnabled(boolean enable);
    boolean isDOLTipsEnabled();

    void updatePackageSetLabel(String newLabel, String id);

    void freezeAllSmartFreezePackages(in IPackageEnableStateChangeListener listener);
    void freezeSmartFreezePackages(in List<Pkg> packages, in IPackageEnableStateChangeListener listener);

    boolean hasFreezedPackageInUserWhiteListPkgSet();

    List<ComponentName> getAllDisabledComponentsForPackage(in Pkg pkg, int itemCountInEachBatch, int batchIndex);

    void setFreezeTipEnabled(boolean enable);
    boolean isFreezeTipEnabled();

    void restoreAllAppComponentSettings();
}