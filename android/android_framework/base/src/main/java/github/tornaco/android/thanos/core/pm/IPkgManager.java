/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.pm;
public interface IPkgManager extends android.os.IInterface
{
  /** Default implementation for IPkgManager. */
  public static class Default implements github.tornaco.android.thanos.core.pm.IPkgManager
  {
    @Override public java.lang.String[] getPkgNameForUid(int uid) throws android.os.RemoteException
    {
      return null;
    }
    @Override public int getUidForPkgName(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
      return 0;
    }
    // ApplicationInfo
    @Override public java.util.List<github.tornaco.android.thanos.core.pm.AppInfo> getInstalledPkgs(int flags) throws android.os.RemoteException
    {
      return null;
    }
    /** @deprecated use {@link #getAppInfoForUser} instead */
    @Override public github.tornaco.android.thanos.core.pm.AppInfo getAppInfo(java.lang.String pkgName) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.lang.String[] getWhiteListPkgs() throws android.os.RemoteException
    {
      return null;
    }
    @Override public boolean isPkgInWhiteList(java.lang.String pkg) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setComponentEnabledSetting(int userId, android.content.ComponentName componentName, int newState, int flags) throws android.os.RemoteException
    {
    }
    @Override public int getComponentEnabledSetting(int userId, android.content.ComponentName componentName) throws android.os.RemoteException
    {
      return 0;
    }
    @Override public boolean isComponentDisabledByThanox(int userId, android.content.ComponentName componentName) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean getApplicationEnableState(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setApplicationEnableState(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean enable, boolean tmp) throws android.os.RemoteException
    {
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getActivities(int userId, java.lang.String packageName) throws android.os.RemoteException
    {
      return null;
    }
    @Override public int getActivitiesCount(java.lang.String packageName) throws android.os.RemoteException
    {
      return 0;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getActivitiesInBatch(int userId, java.lang.String packageName, int itemCountInEachBatch, int batchIndex) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getReceivers(int userId, java.lang.String packageName) throws android.os.RemoteException
    {
      return null;
    }
    @Override public int getReceiverCount(java.lang.String packageName) throws android.os.RemoteException
    {
      return 0;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getReceiversInBatch(int userId, java.lang.String packageName, int itemCountInEachBatch, int batchIndex) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getServices(int userId, java.lang.String packageName) throws android.os.RemoteException
    {
      return null;
    }
    @Override public int getServiceCount(java.lang.String packageName) throws android.os.RemoteException
    {
      return 0;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getServicesInBatch(int userId, java.lang.String packageName, int itemCountInEachBatch, int batchIndex) throws android.os.RemoteException
    {
      return null;
    }
    @Override public void setSmartFreezeEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isSmartFreezeEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setPkgSmartFreezeEnabled(github.tornaco.android.thanos.core.pm.Pkg pkgName, boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isPkgSmartFreezeEnabled(github.tornaco.android.thanos.core.pm.Pkg pkgName) throws android.os.RemoteException
    {
      return false;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.pm.Pkg> getSmartFreezePkgs() throws android.os.RemoteException
    {
      return null;
    }
    /** @deprecated use {@link #launchSmartFreezePkgForUser} instead */
    @Override public void launchSmartFreezePkg(java.lang.String pkgName) throws android.os.RemoteException
    {
    }
    @Override public void setSmartFreezeScreenOffCheckEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isSmartFreezeScreenOffCheckEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setSmartFreezeScreenOffCheckDelay(long delay) throws android.os.RemoteException
    {
    }
    @Override public long getSmartFreezeScreenOffCheckDelay() throws android.os.RemoteException
    {
      return 0L;
    }
    @Override public android.content.Intent queryLaunchIntentForPackage(java.lang.String pkgName) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.pm.Pkg> enableAllThanoxDisabledPackages(boolean removeFromSmartFreezeList) throws android.os.RemoteException
    {
      return null;
    }
    @Override public boolean deviceHasGms() throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean verifyBillingState() throws android.os.RemoteException
    {
      return false;
    }
    /** @deprecated use {@link #launchSmartFreezePkgThenKillOriginForUser} instead */
    @Override public void launchSmartFreezePkgThenKillOrigin(java.lang.String pkgName, java.lang.String origin) throws android.os.RemoteException
    {
    }
    @Override public boolean isProtectedWhitelistEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setProtectedWhitelistEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public void addPlugin(android.os.ParcelFileDescriptor pfd, java.lang.String pluginPackageName, github.tornaco.android.thanos.core.pm.IAddPluginCallback callback) throws android.os.RemoteException
    {
    }
    @Override public void removePlugin(java.lang.String pluginPackageName) throws android.os.RemoteException
    {
    }
    @Override public boolean hasPlugin(java.lang.String pluginPackageName) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean isSmartFreezeHidePackageEventEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setSmartFreezeHidePackageEventEnabled(boolean enabled) throws android.os.RemoteException
    {
    }
    @Override public void dump(github.tornaco.android.thanos.core.IPrinter p) throws android.os.RemoteException
    {
    }
    @Override public void setPackageBlockUninstallEnabled(java.lang.String pkgName, boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isPackageBlockUninstallEnabled(java.lang.String pkgName) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setPackageBlockClearDataEnabled(java.lang.String pkgName, boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isPackageBlockClearDataEnabled(java.lang.String pkgName) throws android.os.RemoteException
    {
      return false;
    }
    @Override public int getInstalledPackagesCount(int appFlags) throws android.os.RemoteException
    {
      return 0;
    }
    @Override public github.tornaco.android.thanos.core.pm.PackageSet createPackageSet(java.lang.String label) throws android.os.RemoteException
    {
      return null;
    }
    @Override public boolean removePackageSet(java.lang.String id) throws android.os.RemoteException
    {
      return false;
    }
    @Override public github.tornaco.android.thanos.core.pm.PackageSet getPackageSetById(java.lang.String id, boolean withPackages, boolean shouldFilterUserWhiteList) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.pm.PackageSet> getAllPackageSets(boolean withPackages) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<java.lang.String> getAllPackageSetIds() throws android.os.RemoteException
    {
      return null;
    }
    @Override public void addToPackageSet(github.tornaco.android.thanos.core.pm.Pkg pkg, java.lang.String id) throws android.os.RemoteException
    {
    }
    @Override public void removeFromPackageSet(github.tornaco.android.thanos.core.pm.Pkg pkg, java.lang.String id) throws android.os.RemoteException
    {
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.pm.PackageSet> getPackageSetThatContainsPkg(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<java.lang.String> getPackageSetLabelsThatContainsPkg(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
      return null;
    }
    @Override public void setFreezePkgWithSuspendEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isFreezePkgWithSuspendEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.pm.AppInfo> getInstalledPkgsByPackageSetId(java.lang.String pkgSetId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public void registerPackageSetChangeListener(github.tornaco.android.thanos.core.pm.IPackageSetChangeListener listener) throws android.os.RemoteException
    {
    }
    @Override public void unRegisterPackageSetChangeListener(github.tornaco.android.thanos.core.pm.IPackageSetChangeListener listener) throws android.os.RemoteException
    {
    }
    @Override public void setEnablePackageOnLaunchRequestEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isEnablePackageOnLaunchRequestEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
      return false;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getProviders(int userId, java.lang.String packageName) throws android.os.RemoteException
    {
      return null;
    }
    // Wrap api to skip permission check
    @Override public java.lang.String[] getPackagesForUid(int uid) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.lang.String mayEnableAppOnStartActivityIntent(android.content.Intent intent, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public boolean isEnablePkgOnLaunchByDefault() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setEnablePkgOnLaunchByDefaultEnabled(boolean byDefault) throws android.os.RemoteException
    {
    }
    @Override public github.tornaco.android.thanos.core.pm.AppInfo getAppInfoForUser(java.lang.String pkgName, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public void launchSmartFreezePkgForUser(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
    }
    @Override public void launchSmartFreezePkgThenKillOriginForUser(github.tornaco.android.thanos.core.pm.Pkg targetPkg, java.lang.String origin) throws android.os.RemoteException
    {
    }
    @Override public void setDOLTipsEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isDOLTipsEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void updatePackageSetLabel(java.lang.String newLabel, java.lang.String id) throws android.os.RemoteException
    {
    }
    @Override public void freezeAllSmartFreezePackages(github.tornaco.android.thanos.core.pm.IPackageEnableStateChangeListener listener) throws android.os.RemoteException
    {
    }
    @Override public void freezeSmartFreezePackages(java.util.List<github.tornaco.android.thanos.core.pm.Pkg> packages, github.tornaco.android.thanos.core.pm.IPackageEnableStateChangeListener listener) throws android.os.RemoteException
    {
    }
    @Override public boolean hasFreezedPackageInUserWhiteListPkgSet() throws android.os.RemoteException
    {
      return false;
    }
    @Override public java.util.List<android.content.ComponentName> getAllDisabledComponentsForPackage(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
      return null;
    }
    @Override public void setFreezeTipEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isFreezeTipEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.pm.IPkgManager
  {
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.pm.IPkgManager interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.pm.IPkgManager asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.pm.IPkgManager))) {
        return ((github.tornaco.android.thanos.core.pm.IPkgManager)iin);
      }
      return new github.tornaco.android.thanos.core.pm.IPkgManager.Stub.Proxy(obj);
    }
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      java.lang.String descriptor = DESCRIPTOR;
      if (code >= android.os.IBinder.FIRST_CALL_TRANSACTION && code <= android.os.IBinder.LAST_CALL_TRANSACTION) {
        data.enforceInterface(descriptor);
      }
      switch (code)
      {
        case INTERFACE_TRANSACTION:
        {
          reply.writeString(descriptor);
          return true;
        }
      }
      switch (code)
      {
        case TRANSACTION_getPkgNameForUid:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String[] _result = this.getPkgNameForUid(_arg0);
          reply.writeNoException();
          reply.writeStringArray(_result);
          break;
        }
        case TRANSACTION_getUidForPkgName:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          int _result = this.getUidForPkgName(_arg0);
          reply.writeNoException();
          reply.writeInt(_result);
          break;
        }
        case TRANSACTION_getInstalledPkgs:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.util.List<github.tornaco.android.thanos.core.pm.AppInfo> _result = this.getInstalledPkgs(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getAppInfo:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          github.tornaco.android.thanos.core.pm.AppInfo _result = this.getAppInfo(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getWhiteListPkgs:
        {
          java.lang.String[] _result = this.getWhiteListPkgs();
          reply.writeNoException();
          reply.writeStringArray(_result);
          break;
        }
        case TRANSACTION_isPkgInWhiteList:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.isPkgInWhiteList(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setComponentEnabledSetting:
        {
          int _arg0;
          _arg0 = data.readInt();
          android.content.ComponentName _arg1;
          _arg1 = _Parcel.readTypedObject(data, android.content.ComponentName.CREATOR);
          int _arg2;
          _arg2 = data.readInt();
          int _arg3;
          _arg3 = data.readInt();
          this.setComponentEnabledSetting(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getComponentEnabledSetting:
        {
          int _arg0;
          _arg0 = data.readInt();
          android.content.ComponentName _arg1;
          _arg1 = _Parcel.readTypedObject(data, android.content.ComponentName.CREATOR);
          int _result = this.getComponentEnabledSetting(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(_result);
          break;
        }
        case TRANSACTION_isComponentDisabledByThanox:
        {
          int _arg0;
          _arg0 = data.readInt();
          android.content.ComponentName _arg1;
          _arg1 = _Parcel.readTypedObject(data, android.content.ComponentName.CREATOR);
          boolean _result = this.isComponentDisabledByThanox(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_getApplicationEnableState:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _result = this.getApplicationEnableState(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setApplicationEnableState:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          boolean _arg2;
          _arg2 = (0!=data.readInt());
          this.setApplicationEnableState(_arg0, _arg1, _arg2);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getActivities:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> _result = this.getActivities(_arg0, _arg1);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getActivitiesCount:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _result = this.getActivitiesCount(_arg0);
          reply.writeNoException();
          reply.writeInt(_result);
          break;
        }
        case TRANSACTION_getActivitiesInBatch:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          int _arg2;
          _arg2 = data.readInt();
          int _arg3;
          _arg3 = data.readInt();
          java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> _result = this.getActivitiesInBatch(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getReceivers:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> _result = this.getReceivers(_arg0, _arg1);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getReceiverCount:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _result = this.getReceiverCount(_arg0);
          reply.writeNoException();
          reply.writeInt(_result);
          break;
        }
        case TRANSACTION_getReceiversInBatch:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          int _arg2;
          _arg2 = data.readInt();
          int _arg3;
          _arg3 = data.readInt();
          java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> _result = this.getReceiversInBatch(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getServices:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> _result = this.getServices(_arg0, _arg1);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getServiceCount:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _result = this.getServiceCount(_arg0);
          reply.writeNoException();
          reply.writeInt(_result);
          break;
        }
        case TRANSACTION_getServicesInBatch:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          int _arg2;
          _arg2 = data.readInt();
          int _arg3;
          _arg3 = data.readInt();
          java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> _result = this.getServicesInBatch(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_setSmartFreezeEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setSmartFreezeEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isSmartFreezeEnabled:
        {
          boolean _result = this.isSmartFreezeEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setPkgSmartFreezeEnabled:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setPkgSmartFreezeEnabled(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isPkgSmartFreezeEnabled:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _result = this.isPkgSmartFreezeEnabled(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_getSmartFreezePkgs:
        {
          java.util.List<github.tornaco.android.thanos.core.pm.Pkg> _result = this.getSmartFreezePkgs();
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_launchSmartFreezePkg:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.launchSmartFreezePkg(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_setSmartFreezeScreenOffCheckEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setSmartFreezeScreenOffCheckEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isSmartFreezeScreenOffCheckEnabled:
        {
          boolean _result = this.isSmartFreezeScreenOffCheckEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setSmartFreezeScreenOffCheckDelay:
        {
          long _arg0;
          _arg0 = data.readLong();
          this.setSmartFreezeScreenOffCheckDelay(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getSmartFreezeScreenOffCheckDelay:
        {
          long _result = this.getSmartFreezeScreenOffCheckDelay();
          reply.writeNoException();
          reply.writeLong(_result);
          break;
        }
        case TRANSACTION_queryLaunchIntentForPackage:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          android.content.Intent _result = this.queryLaunchIntentForPackage(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_enableAllThanoxDisabledPackages:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          java.util.List<github.tornaco.android.thanos.core.pm.Pkg> _result = this.enableAllThanoxDisabledPackages(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_deviceHasGms:
        {
          boolean _result = this.deviceHasGms();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_verifyBillingState:
        {
          boolean _result = this.verifyBillingState();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_launchSmartFreezePkgThenKillOrigin:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.launchSmartFreezePkgThenKillOrigin(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isProtectedWhitelistEnabled:
        {
          boolean _result = this.isProtectedWhitelistEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setProtectedWhitelistEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setProtectedWhitelistEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_addPlugin:
        {
          android.os.ParcelFileDescriptor _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.os.ParcelFileDescriptor.CREATOR);
          java.lang.String _arg1;
          _arg1 = data.readString();
          github.tornaco.android.thanos.core.pm.IAddPluginCallback _arg2;
          _arg2 = github.tornaco.android.thanos.core.pm.IAddPluginCallback.Stub.asInterface(data.readStrongBinder());
          this.addPlugin(_arg0, _arg1, _arg2);
          break;
        }
        case TRANSACTION_removePlugin:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.removePlugin(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_hasPlugin:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.hasPlugin(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_isSmartFreezeHidePackageEventEnabled:
        {
          boolean _result = this.isSmartFreezeHidePackageEventEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setSmartFreezeHidePackageEventEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setSmartFreezeHidePackageEventEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_dump:
        {
          github.tornaco.android.thanos.core.IPrinter _arg0;
          _arg0 = github.tornaco.android.thanos.core.IPrinter.Stub.asInterface(data.readStrongBinder());
          this.dump(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_setPackageBlockUninstallEnabled:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setPackageBlockUninstallEnabled(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isPackageBlockUninstallEnabled:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.isPackageBlockUninstallEnabled(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setPackageBlockClearDataEnabled:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setPackageBlockClearDataEnabled(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isPackageBlockClearDataEnabled:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.isPackageBlockClearDataEnabled(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_getInstalledPackagesCount:
        {
          int _arg0;
          _arg0 = data.readInt();
          int _result = this.getInstalledPackagesCount(_arg0);
          reply.writeNoException();
          reply.writeInt(_result);
          break;
        }
        case TRANSACTION_createPackageSet:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          github.tornaco.android.thanos.core.pm.PackageSet _result = this.createPackageSet(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_removePackageSet:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.removePackageSet(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_getPackageSetById:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          boolean _arg2;
          _arg2 = (0!=data.readInt());
          github.tornaco.android.thanos.core.pm.PackageSet _result = this.getPackageSetById(_arg0, _arg1, _arg2);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getAllPackageSets:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          java.util.List<github.tornaco.android.thanos.core.pm.PackageSet> _result = this.getAllPackageSets(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getAllPackageSetIds:
        {
          java.util.List<java.lang.String> _result = this.getAllPackageSetIds();
          reply.writeNoException();
          reply.writeStringList(_result);
          break;
        }
        case TRANSACTION_addToPackageSet:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.addToPackageSet(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_removeFromPackageSet:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.removeFromPackageSet(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getPackageSetThatContainsPkg:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          java.util.List<github.tornaco.android.thanos.core.pm.PackageSet> _result = this.getPackageSetThatContainsPkg(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getPackageSetLabelsThatContainsPkg:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          java.util.List<java.lang.String> _result = this.getPackageSetLabelsThatContainsPkg(_arg0);
          reply.writeNoException();
          reply.writeStringList(_result);
          break;
        }
        case TRANSACTION_setFreezePkgWithSuspendEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setFreezePkgWithSuspendEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isFreezePkgWithSuspendEnabled:
        {
          boolean _result = this.isFreezePkgWithSuspendEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_getInstalledPkgsByPackageSetId:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.util.List<github.tornaco.android.thanos.core.pm.AppInfo> _result = this.getInstalledPkgsByPackageSetId(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_registerPackageSetChangeListener:
        {
          github.tornaco.android.thanos.core.pm.IPackageSetChangeListener _arg0;
          _arg0 = github.tornaco.android.thanos.core.pm.IPackageSetChangeListener.Stub.asInterface(data.readStrongBinder());
          this.registerPackageSetChangeListener(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_unRegisterPackageSetChangeListener:
        {
          github.tornaco.android.thanos.core.pm.IPackageSetChangeListener _arg0;
          _arg0 = github.tornaco.android.thanos.core.pm.IPackageSetChangeListener.Stub.asInterface(data.readStrongBinder());
          this.unRegisterPackageSetChangeListener(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_setEnablePackageOnLaunchRequestEnabled:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setEnablePackageOnLaunchRequestEnabled(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isEnablePackageOnLaunchRequestEnabled:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _result = this.isEnablePackageOnLaunchRequestEnabled(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_getProviders:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> _result = this.getProviders(_arg0, _arg1);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getPackagesForUid:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String[] _result = this.getPackagesForUid(_arg0);
          reply.writeNoException();
          reply.writeStringArray(_result);
          break;
        }
        case TRANSACTION_mayEnableAppOnStartActivityIntent:
        {
          android.content.Intent _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.content.Intent.CREATOR);
          int _arg1;
          _arg1 = data.readInt();
          java.lang.String _result = this.mayEnableAppOnStartActivityIntent(_arg0, _arg1);
          reply.writeNoException();
          reply.writeString(_result);
          break;
        }
        case TRANSACTION_isEnablePkgOnLaunchByDefault:
        {
          boolean _result = this.isEnablePkgOnLaunchByDefault();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setEnablePkgOnLaunchByDefaultEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setEnablePkgOnLaunchByDefaultEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getAppInfoForUser:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          github.tornaco.android.thanos.core.pm.AppInfo _result = this.getAppInfoForUser(_arg0, _arg1);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_launchSmartFreezePkgForUser:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          this.launchSmartFreezePkgForUser(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_launchSmartFreezePkgThenKillOriginForUser:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.launchSmartFreezePkgThenKillOriginForUser(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_setDOLTipsEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setDOLTipsEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isDOLTipsEnabled:
        {
          boolean _result = this.isDOLTipsEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_updatePackageSetLabel:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.updatePackageSetLabel(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_freezeAllSmartFreezePackages:
        {
          github.tornaco.android.thanos.core.pm.IPackageEnableStateChangeListener _arg0;
          _arg0 = github.tornaco.android.thanos.core.pm.IPackageEnableStateChangeListener.Stub.asInterface(data.readStrongBinder());
          this.freezeAllSmartFreezePackages(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_freezeSmartFreezePackages:
        {
          java.util.List<github.tornaco.android.thanos.core.pm.Pkg> _arg0;
          _arg0 = data.createTypedArrayList(github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          github.tornaco.android.thanos.core.pm.IPackageEnableStateChangeListener _arg1;
          _arg1 = github.tornaco.android.thanos.core.pm.IPackageEnableStateChangeListener.Stub.asInterface(data.readStrongBinder());
          this.freezeSmartFreezePackages(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_hasFreezedPackageInUserWhiteListPkgSet:
        {
          boolean _result = this.hasFreezedPackageInUserWhiteListPkgSet();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_getAllDisabledComponentsForPackage:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          java.util.List<android.content.ComponentName> _result = this.getAllDisabledComponentsForPackage(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_setFreezeTipEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setFreezeTipEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isFreezeTipEnabled:
        {
          boolean _result = this.isFreezeTipEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static class Proxy implements github.tornaco.android.thanos.core.pm.IPkgManager
    {
      private android.os.IBinder mRemote;
      Proxy(android.os.IBinder remote)
      {
        mRemote = remote;
      }
      @Override public android.os.IBinder asBinder()
      {
        return mRemote;
      }
      public java.lang.String getInterfaceDescriptor()
      {
        return DESCRIPTOR;
      }
      @Override public java.lang.String[] getPkgNameForUid(int uid) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(uid);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getPkgNameForUid, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createStringArray();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public int getUidForPkgName(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getUidForPkgName, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      // ApplicationInfo
      @Override public java.util.List<github.tornaco.android.thanos.core.pm.AppInfo> getInstalledPkgs(int flags) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.pm.AppInfo> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(flags);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getInstalledPkgs, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.pm.AppInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      /** @deprecated use {@link #getAppInfoForUser} instead */
      @Override public github.tornaco.android.thanos.core.pm.AppInfo getAppInfo(java.lang.String pkgName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.pm.AppInfo _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAppInfo, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, github.tornaco.android.thanos.core.pm.AppInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.lang.String[] getWhiteListPkgs() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getWhiteListPkgs, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createStringArray();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean isPkgInWhiteList(java.lang.String pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPkgInWhiteList, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setComponentEnabledSetting(int userId, android.content.ComponentName componentName, int newState, int flags) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(userId);
          _Parcel.writeTypedObject(_data, componentName, 0);
          _data.writeInt(newState);
          _data.writeInt(flags);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setComponentEnabledSetting, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public int getComponentEnabledSetting(int userId, android.content.ComponentName componentName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(userId);
          _Parcel.writeTypedObject(_data, componentName, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getComponentEnabledSetting, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean isComponentDisabledByThanox(int userId, android.content.ComponentName componentName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(userId);
          _Parcel.writeTypedObject(_data, componentName, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isComponentDisabledByThanox, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean getApplicationEnableState(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getApplicationEnableState, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setApplicationEnableState(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean enable, boolean tmp) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          _data.writeInt(((enable)?(1):(0)));
          _data.writeInt(((tmp)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setApplicationEnableState, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getActivities(int userId, java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(userId);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getActivities, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.pm.ComponentInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public int getActivitiesCount(java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getActivitiesCount, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getActivitiesInBatch(int userId, java.lang.String packageName, int itemCountInEachBatch, int batchIndex) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(userId);
          _data.writeString(packageName);
          _data.writeInt(itemCountInEachBatch);
          _data.writeInt(batchIndex);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getActivitiesInBatch, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.pm.ComponentInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getReceivers(int userId, java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(userId);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getReceivers, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.pm.ComponentInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public int getReceiverCount(java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getReceiverCount, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getReceiversInBatch(int userId, java.lang.String packageName, int itemCountInEachBatch, int batchIndex) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(userId);
          _data.writeString(packageName);
          _data.writeInt(itemCountInEachBatch);
          _data.writeInt(batchIndex);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getReceiversInBatch, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.pm.ComponentInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getServices(int userId, java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(userId);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getServices, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.pm.ComponentInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public int getServiceCount(java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getServiceCount, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getServicesInBatch(int userId, java.lang.String packageName, int itemCountInEachBatch, int batchIndex) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(userId);
          _data.writeString(packageName);
          _data.writeInt(itemCountInEachBatch);
          _data.writeInt(batchIndex);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getServicesInBatch, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.pm.ComponentInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setSmartFreezeEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setSmartFreezeEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isSmartFreezeEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isSmartFreezeEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setPkgSmartFreezeEnabled(github.tornaco.android.thanos.core.pm.Pkg pkgName, boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkgName, 0);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPkgSmartFreezeEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isPkgSmartFreezeEnabled(github.tornaco.android.thanos.core.pm.Pkg pkgName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkgName, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPkgSmartFreezeEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.pm.Pkg> getSmartFreezePkgs() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.pm.Pkg> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getSmartFreezePkgs, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      /** @deprecated use {@link #launchSmartFreezePkgForUser} instead */
      @Override public void launchSmartFreezePkg(java.lang.String pkgName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_launchSmartFreezePkg, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setSmartFreezeScreenOffCheckEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setSmartFreezeScreenOffCheckEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isSmartFreezeScreenOffCheckEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isSmartFreezeScreenOffCheckEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setSmartFreezeScreenOffCheckDelay(long delay) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeLong(delay);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setSmartFreezeScreenOffCheckDelay, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public long getSmartFreezeScreenOffCheckDelay() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        long _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getSmartFreezeScreenOffCheckDelay, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readLong();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public android.content.Intent queryLaunchIntentForPackage(java.lang.String pkgName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.content.Intent _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_queryLaunchIntentForPackage, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, android.content.Intent.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.pm.Pkg> enableAllThanoxDisabledPackages(boolean removeFromSmartFreezeList) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.pm.Pkg> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((removeFromSmartFreezeList)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_enableAllThanoxDisabledPackages, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean deviceHasGms() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_deviceHasGms, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean verifyBillingState() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_verifyBillingState, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      /** @deprecated use {@link #launchSmartFreezePkgThenKillOriginForUser} instead */
      @Override public void launchSmartFreezePkgThenKillOrigin(java.lang.String pkgName, java.lang.String origin) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          _data.writeString(origin);
          boolean _status = mRemote.transact(Stub.TRANSACTION_launchSmartFreezePkgThenKillOrigin, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isProtectedWhitelistEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isProtectedWhitelistEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setProtectedWhitelistEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setProtectedWhitelistEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void addPlugin(android.os.ParcelFileDescriptor pfd, java.lang.String pluginPackageName, github.tornaco.android.thanos.core.pm.IAddPluginCallback callback) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pfd, 0);
          _data.writeString(pluginPackageName);
          _data.writeStrongInterface(callback);
          boolean _status = mRemote.transact(Stub.TRANSACTION_addPlugin, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      @Override public void removePlugin(java.lang.String pluginPackageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pluginPackageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_removePlugin, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean hasPlugin(java.lang.String pluginPackageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pluginPackageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_hasPlugin, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean isSmartFreezeHidePackageEventEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isSmartFreezeHidePackageEventEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setSmartFreezeHidePackageEventEnabled(boolean enabled) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enabled)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setSmartFreezeHidePackageEventEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void dump(github.tornaco.android.thanos.core.IPrinter p) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(p);
          boolean _status = mRemote.transact(Stub.TRANSACTION_dump, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setPackageBlockUninstallEnabled(java.lang.String pkgName, boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPackageBlockUninstallEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isPackageBlockUninstallEnabled(java.lang.String pkgName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPackageBlockUninstallEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setPackageBlockClearDataEnabled(java.lang.String pkgName, boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPackageBlockClearDataEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isPackageBlockClearDataEnabled(java.lang.String pkgName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPackageBlockClearDataEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public int getInstalledPackagesCount(int appFlags) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(appFlags);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getInstalledPackagesCount, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public github.tornaco.android.thanos.core.pm.PackageSet createPackageSet(java.lang.String label) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.pm.PackageSet _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(label);
          boolean _status = mRemote.transact(Stub.TRANSACTION_createPackageSet, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, github.tornaco.android.thanos.core.pm.PackageSet.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean removePackageSet(java.lang.String id) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(id);
          boolean _status = mRemote.transact(Stub.TRANSACTION_removePackageSet, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public github.tornaco.android.thanos.core.pm.PackageSet getPackageSetById(java.lang.String id, boolean withPackages, boolean shouldFilterUserWhiteList) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.pm.PackageSet _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(id);
          _data.writeInt(((withPackages)?(1):(0)));
          _data.writeInt(((shouldFilterUserWhiteList)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_getPackageSetById, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, github.tornaco.android.thanos.core.pm.PackageSet.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.pm.PackageSet> getAllPackageSets(boolean withPackages) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.pm.PackageSet> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((withPackages)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAllPackageSets, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.pm.PackageSet.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<java.lang.String> getAllPackageSetIds() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<java.lang.String> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAllPackageSetIds, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createStringArrayList();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void addToPackageSet(github.tornaco.android.thanos.core.pm.Pkg pkg, java.lang.String id) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          _data.writeString(id);
          boolean _status = mRemote.transact(Stub.TRANSACTION_addToPackageSet, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void removeFromPackageSet(github.tornaco.android.thanos.core.pm.Pkg pkg, java.lang.String id) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          _data.writeString(id);
          boolean _status = mRemote.transact(Stub.TRANSACTION_removeFromPackageSet, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.pm.PackageSet> getPackageSetThatContainsPkg(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.pm.PackageSet> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getPackageSetThatContainsPkg, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.pm.PackageSet.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<java.lang.String> getPackageSetLabelsThatContainsPkg(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<java.lang.String> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getPackageSetLabelsThatContainsPkg, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createStringArrayList();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setFreezePkgWithSuspendEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setFreezePkgWithSuspendEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isFreezePkgWithSuspendEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isFreezePkgWithSuspendEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.pm.AppInfo> getInstalledPkgsByPackageSetId(java.lang.String pkgSetId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.pm.AppInfo> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgSetId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getInstalledPkgsByPackageSetId, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.pm.AppInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void registerPackageSetChangeListener(github.tornaco.android.thanos.core.pm.IPackageSetChangeListener listener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(listener);
          boolean _status = mRemote.transact(Stub.TRANSACTION_registerPackageSetChangeListener, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void unRegisterPackageSetChangeListener(github.tornaco.android.thanos.core.pm.IPackageSetChangeListener listener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(listener);
          boolean _status = mRemote.transact(Stub.TRANSACTION_unRegisterPackageSetChangeListener, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setEnablePackageOnLaunchRequestEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setEnablePackageOnLaunchRequestEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isEnablePackageOnLaunchRequestEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isEnablePackageOnLaunchRequestEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getProviders(int userId, java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(userId);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getProviders, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.pm.ComponentInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      // Wrap api to skip permission check
      @Override public java.lang.String[] getPackagesForUid(int uid) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(uid);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getPackagesForUid, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createStringArray();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.lang.String mayEnableAppOnStartActivityIntent(android.content.Intent intent, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, intent, 0);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_mayEnableAppOnStartActivityIntent, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readString();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean isEnablePkgOnLaunchByDefault() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isEnablePkgOnLaunchByDefault, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setEnablePkgOnLaunchByDefaultEnabled(boolean byDefault) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((byDefault)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setEnablePkgOnLaunchByDefaultEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public github.tornaco.android.thanos.core.pm.AppInfo getAppInfoForUser(java.lang.String pkgName, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.pm.AppInfo _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAppInfoForUser, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, github.tornaco.android.thanos.core.pm.AppInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void launchSmartFreezePkgForUser(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_launchSmartFreezePkgForUser, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void launchSmartFreezePkgThenKillOriginForUser(github.tornaco.android.thanos.core.pm.Pkg targetPkg, java.lang.String origin) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, targetPkg, 0);
          _data.writeString(origin);
          boolean _status = mRemote.transact(Stub.TRANSACTION_launchSmartFreezePkgThenKillOriginForUser, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setDOLTipsEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setDOLTipsEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isDOLTipsEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isDOLTipsEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void updatePackageSetLabel(java.lang.String newLabel, java.lang.String id) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(newLabel);
          _data.writeString(id);
          boolean _status = mRemote.transact(Stub.TRANSACTION_updatePackageSetLabel, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void freezeAllSmartFreezePackages(github.tornaco.android.thanos.core.pm.IPackageEnableStateChangeListener listener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(listener);
          boolean _status = mRemote.transact(Stub.TRANSACTION_freezeAllSmartFreezePackages, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void freezeSmartFreezePackages(java.util.List<github.tornaco.android.thanos.core.pm.Pkg> packages, github.tornaco.android.thanos.core.pm.IPackageEnableStateChangeListener listener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedList(_data, packages, 0);
          _data.writeStrongInterface(listener);
          boolean _status = mRemote.transact(Stub.TRANSACTION_freezeSmartFreezePackages, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean hasFreezedPackageInUserWhiteListPkgSet() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_hasFreezedPackageInUserWhiteListPkgSet, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<android.content.ComponentName> getAllDisabledComponentsForPackage(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<android.content.ComponentName> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAllDisabledComponentsForPackage, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(android.content.ComponentName.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setFreezeTipEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setFreezeTipEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isFreezeTipEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isFreezeTipEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
    }
    static final int TRANSACTION_getPkgNameForUid = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_getUidForPkgName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_getInstalledPkgs = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_getAppInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_getWhiteListPkgs = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_isPkgInWhiteList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_setComponentEnabledSetting = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_getComponentEnabledSetting = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    static final int TRANSACTION_isComponentDisabledByThanox = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
    static final int TRANSACTION_getApplicationEnableState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
    static final int TRANSACTION_setApplicationEnableState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
    static final int TRANSACTION_getActivities = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
    static final int TRANSACTION_getActivitiesCount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
    static final int TRANSACTION_getActivitiesInBatch = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
    static final int TRANSACTION_getReceivers = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
    static final int TRANSACTION_getReceiverCount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
    static final int TRANSACTION_getReceiversInBatch = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
    static final int TRANSACTION_getServices = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
    static final int TRANSACTION_getServiceCount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
    static final int TRANSACTION_getServicesInBatch = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
    static final int TRANSACTION_setSmartFreezeEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
    static final int TRANSACTION_isSmartFreezeEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 21);
    static final int TRANSACTION_setPkgSmartFreezeEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 22);
    static final int TRANSACTION_isPkgSmartFreezeEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 23);
    static final int TRANSACTION_getSmartFreezePkgs = (android.os.IBinder.FIRST_CALL_TRANSACTION + 24);
    static final int TRANSACTION_launchSmartFreezePkg = (android.os.IBinder.FIRST_CALL_TRANSACTION + 25);
    static final int TRANSACTION_setSmartFreezeScreenOffCheckEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 26);
    static final int TRANSACTION_isSmartFreezeScreenOffCheckEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 27);
    static final int TRANSACTION_setSmartFreezeScreenOffCheckDelay = (android.os.IBinder.FIRST_CALL_TRANSACTION + 28);
    static final int TRANSACTION_getSmartFreezeScreenOffCheckDelay = (android.os.IBinder.FIRST_CALL_TRANSACTION + 29);
    static final int TRANSACTION_queryLaunchIntentForPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 30);
    static final int TRANSACTION_enableAllThanoxDisabledPackages = (android.os.IBinder.FIRST_CALL_TRANSACTION + 31);
    static final int TRANSACTION_deviceHasGms = (android.os.IBinder.FIRST_CALL_TRANSACTION + 32);
    static final int TRANSACTION_verifyBillingState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 33);
    static final int TRANSACTION_launchSmartFreezePkgThenKillOrigin = (android.os.IBinder.FIRST_CALL_TRANSACTION + 34);
    static final int TRANSACTION_isProtectedWhitelistEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 35);
    static final int TRANSACTION_setProtectedWhitelistEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 36);
    static final int TRANSACTION_addPlugin = (android.os.IBinder.FIRST_CALL_TRANSACTION + 37);
    static final int TRANSACTION_removePlugin = (android.os.IBinder.FIRST_CALL_TRANSACTION + 38);
    static final int TRANSACTION_hasPlugin = (android.os.IBinder.FIRST_CALL_TRANSACTION + 39);
    static final int TRANSACTION_isSmartFreezeHidePackageEventEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 40);
    static final int TRANSACTION_setSmartFreezeHidePackageEventEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 41);
    static final int TRANSACTION_dump = (android.os.IBinder.FIRST_CALL_TRANSACTION + 42);
    static final int TRANSACTION_setPackageBlockUninstallEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 43);
    static final int TRANSACTION_isPackageBlockUninstallEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 44);
    static final int TRANSACTION_setPackageBlockClearDataEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 45);
    static final int TRANSACTION_isPackageBlockClearDataEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 46);
    static final int TRANSACTION_getInstalledPackagesCount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 47);
    static final int TRANSACTION_createPackageSet = (android.os.IBinder.FIRST_CALL_TRANSACTION + 48);
    static final int TRANSACTION_removePackageSet = (android.os.IBinder.FIRST_CALL_TRANSACTION + 49);
    static final int TRANSACTION_getPackageSetById = (android.os.IBinder.FIRST_CALL_TRANSACTION + 50);
    static final int TRANSACTION_getAllPackageSets = (android.os.IBinder.FIRST_CALL_TRANSACTION + 51);
    static final int TRANSACTION_getAllPackageSetIds = (android.os.IBinder.FIRST_CALL_TRANSACTION + 52);
    static final int TRANSACTION_addToPackageSet = (android.os.IBinder.FIRST_CALL_TRANSACTION + 53);
    static final int TRANSACTION_removeFromPackageSet = (android.os.IBinder.FIRST_CALL_TRANSACTION + 54);
    static final int TRANSACTION_getPackageSetThatContainsPkg = (android.os.IBinder.FIRST_CALL_TRANSACTION + 55);
    static final int TRANSACTION_getPackageSetLabelsThatContainsPkg = (android.os.IBinder.FIRST_CALL_TRANSACTION + 56);
    static final int TRANSACTION_setFreezePkgWithSuspendEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 57);
    static final int TRANSACTION_isFreezePkgWithSuspendEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 58);
    static final int TRANSACTION_getInstalledPkgsByPackageSetId = (android.os.IBinder.FIRST_CALL_TRANSACTION + 59);
    static final int TRANSACTION_registerPackageSetChangeListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 60);
    static final int TRANSACTION_unRegisterPackageSetChangeListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 61);
    static final int TRANSACTION_setEnablePackageOnLaunchRequestEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 62);
    static final int TRANSACTION_isEnablePackageOnLaunchRequestEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 63);
    static final int TRANSACTION_getProviders = (android.os.IBinder.FIRST_CALL_TRANSACTION + 64);
    static final int TRANSACTION_getPackagesForUid = (android.os.IBinder.FIRST_CALL_TRANSACTION + 65);
    static final int TRANSACTION_mayEnableAppOnStartActivityIntent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 66);
    static final int TRANSACTION_isEnablePkgOnLaunchByDefault = (android.os.IBinder.FIRST_CALL_TRANSACTION + 67);
    static final int TRANSACTION_setEnablePkgOnLaunchByDefaultEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 68);
    static final int TRANSACTION_getAppInfoForUser = (android.os.IBinder.FIRST_CALL_TRANSACTION + 69);
    static final int TRANSACTION_launchSmartFreezePkgForUser = (android.os.IBinder.FIRST_CALL_TRANSACTION + 70);
    static final int TRANSACTION_launchSmartFreezePkgThenKillOriginForUser = (android.os.IBinder.FIRST_CALL_TRANSACTION + 71);
    static final int TRANSACTION_setDOLTipsEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 72);
    static final int TRANSACTION_isDOLTipsEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 73);
    static final int TRANSACTION_updatePackageSetLabel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 74);
    static final int TRANSACTION_freezeAllSmartFreezePackages = (android.os.IBinder.FIRST_CALL_TRANSACTION + 75);
    static final int TRANSACTION_freezeSmartFreezePackages = (android.os.IBinder.FIRST_CALL_TRANSACTION + 76);
    static final int TRANSACTION_hasFreezedPackageInUserWhiteListPkgSet = (android.os.IBinder.FIRST_CALL_TRANSACTION + 77);
    static final int TRANSACTION_getAllDisabledComponentsForPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 78);
    static final int TRANSACTION_setFreezeTipEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 79);
    static final int TRANSACTION_isFreezeTipEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 80);
  }
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.pm.IPkgManager";
  public java.lang.String[] getPkgNameForUid(int uid) throws android.os.RemoteException;
  public int getUidForPkgName(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  // ApplicationInfo
  public java.util.List<github.tornaco.android.thanos.core.pm.AppInfo> getInstalledPkgs(int flags) throws android.os.RemoteException;
  /** @deprecated use {@link #getAppInfoForUser} instead */
  @Deprecated
  public github.tornaco.android.thanos.core.pm.AppInfo getAppInfo(java.lang.String pkgName) throws android.os.RemoteException;
  public java.lang.String[] getWhiteListPkgs() throws android.os.RemoteException;
  public boolean isPkgInWhiteList(java.lang.String pkg) throws android.os.RemoteException;
  public void setComponentEnabledSetting(int userId, android.content.ComponentName componentName, int newState, int flags) throws android.os.RemoteException;
  public int getComponentEnabledSetting(int userId, android.content.ComponentName componentName) throws android.os.RemoteException;
  public boolean isComponentDisabledByThanox(int userId, android.content.ComponentName componentName) throws android.os.RemoteException;
  public boolean getApplicationEnableState(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  public void setApplicationEnableState(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean enable, boolean tmp) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getActivities(int userId, java.lang.String packageName) throws android.os.RemoteException;
  public int getActivitiesCount(java.lang.String packageName) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getActivitiesInBatch(int userId, java.lang.String packageName, int itemCountInEachBatch, int batchIndex) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getReceivers(int userId, java.lang.String packageName) throws android.os.RemoteException;
  public int getReceiverCount(java.lang.String packageName) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getReceiversInBatch(int userId, java.lang.String packageName, int itemCountInEachBatch, int batchIndex) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getServices(int userId, java.lang.String packageName) throws android.os.RemoteException;
  public int getServiceCount(java.lang.String packageName) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getServicesInBatch(int userId, java.lang.String packageName, int itemCountInEachBatch, int batchIndex) throws android.os.RemoteException;
  public void setSmartFreezeEnabled(boolean enable) throws android.os.RemoteException;
  public boolean isSmartFreezeEnabled() throws android.os.RemoteException;
  public void setPkgSmartFreezeEnabled(github.tornaco.android.thanos.core.pm.Pkg pkgName, boolean enable) throws android.os.RemoteException;
  public boolean isPkgSmartFreezeEnabled(github.tornaco.android.thanos.core.pm.Pkg pkgName) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.pm.Pkg> getSmartFreezePkgs() throws android.os.RemoteException;
  /** @deprecated use {@link #launchSmartFreezePkgForUser} instead */
  @Deprecated
  public void launchSmartFreezePkg(java.lang.String pkgName) throws android.os.RemoteException;
  public void setSmartFreezeScreenOffCheckEnabled(boolean enable) throws android.os.RemoteException;
  public boolean isSmartFreezeScreenOffCheckEnabled() throws android.os.RemoteException;
  public void setSmartFreezeScreenOffCheckDelay(long delay) throws android.os.RemoteException;
  public long getSmartFreezeScreenOffCheckDelay() throws android.os.RemoteException;
  public android.content.Intent queryLaunchIntentForPackage(java.lang.String pkgName) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.pm.Pkg> enableAllThanoxDisabledPackages(boolean removeFromSmartFreezeList) throws android.os.RemoteException;
  public boolean deviceHasGms() throws android.os.RemoteException;
  public boolean verifyBillingState() throws android.os.RemoteException;
  /** @deprecated use {@link #launchSmartFreezePkgThenKillOriginForUser} instead */
  @Deprecated
  public void launchSmartFreezePkgThenKillOrigin(java.lang.String pkgName, java.lang.String origin) throws android.os.RemoteException;
  public boolean isProtectedWhitelistEnabled() throws android.os.RemoteException;
  public void setProtectedWhitelistEnabled(boolean enable) throws android.os.RemoteException;
  public void addPlugin(android.os.ParcelFileDescriptor pfd, java.lang.String pluginPackageName, github.tornaco.android.thanos.core.pm.IAddPluginCallback callback) throws android.os.RemoteException;
  public void removePlugin(java.lang.String pluginPackageName) throws android.os.RemoteException;
  public boolean hasPlugin(java.lang.String pluginPackageName) throws android.os.RemoteException;
  public boolean isSmartFreezeHidePackageEventEnabled() throws android.os.RemoteException;
  public void setSmartFreezeHidePackageEventEnabled(boolean enabled) throws android.os.RemoteException;
  public void dump(github.tornaco.android.thanos.core.IPrinter p) throws android.os.RemoteException;
  public void setPackageBlockUninstallEnabled(java.lang.String pkgName, boolean enable) throws android.os.RemoteException;
  public boolean isPackageBlockUninstallEnabled(java.lang.String pkgName) throws android.os.RemoteException;
  public void setPackageBlockClearDataEnabled(java.lang.String pkgName, boolean enable) throws android.os.RemoteException;
  public boolean isPackageBlockClearDataEnabled(java.lang.String pkgName) throws android.os.RemoteException;
  public int getInstalledPackagesCount(int appFlags) throws android.os.RemoteException;
  public github.tornaco.android.thanos.core.pm.PackageSet createPackageSet(java.lang.String label) throws android.os.RemoteException;
  public boolean removePackageSet(java.lang.String id) throws android.os.RemoteException;
  public github.tornaco.android.thanos.core.pm.PackageSet getPackageSetById(java.lang.String id, boolean withPackages, boolean shouldFilterUserWhiteList) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.pm.PackageSet> getAllPackageSets(boolean withPackages) throws android.os.RemoteException;
  public java.util.List<java.lang.String> getAllPackageSetIds() throws android.os.RemoteException;
  public void addToPackageSet(github.tornaco.android.thanos.core.pm.Pkg pkg, java.lang.String id) throws android.os.RemoteException;
  public void removeFromPackageSet(github.tornaco.android.thanos.core.pm.Pkg pkg, java.lang.String id) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.pm.PackageSet> getPackageSetThatContainsPkg(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  public java.util.List<java.lang.String> getPackageSetLabelsThatContainsPkg(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  public void setFreezePkgWithSuspendEnabled(boolean enable) throws android.os.RemoteException;
  public boolean isFreezePkgWithSuspendEnabled() throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.pm.AppInfo> getInstalledPkgsByPackageSetId(java.lang.String pkgSetId) throws android.os.RemoteException;
  public void registerPackageSetChangeListener(github.tornaco.android.thanos.core.pm.IPackageSetChangeListener listener) throws android.os.RemoteException;
  public void unRegisterPackageSetChangeListener(github.tornaco.android.thanos.core.pm.IPackageSetChangeListener listener) throws android.os.RemoteException;
  public void setEnablePackageOnLaunchRequestEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean enable) throws android.os.RemoteException;
  public boolean isEnablePackageOnLaunchRequestEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getProviders(int userId, java.lang.String packageName) throws android.os.RemoteException;
  // Wrap api to skip permission check
  public java.lang.String[] getPackagesForUid(int uid) throws android.os.RemoteException;
  public java.lang.String mayEnableAppOnStartActivityIntent(android.content.Intent intent, int userId) throws android.os.RemoteException;
  public boolean isEnablePkgOnLaunchByDefault() throws android.os.RemoteException;
  public void setEnablePkgOnLaunchByDefaultEnabled(boolean byDefault) throws android.os.RemoteException;
  public github.tornaco.android.thanos.core.pm.AppInfo getAppInfoForUser(java.lang.String pkgName, int userId) throws android.os.RemoteException;
  public void launchSmartFreezePkgForUser(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  public void launchSmartFreezePkgThenKillOriginForUser(github.tornaco.android.thanos.core.pm.Pkg targetPkg, java.lang.String origin) throws android.os.RemoteException;
  public void setDOLTipsEnabled(boolean enable) throws android.os.RemoteException;
  public boolean isDOLTipsEnabled() throws android.os.RemoteException;
  public void updatePackageSetLabel(java.lang.String newLabel, java.lang.String id) throws android.os.RemoteException;
  public void freezeAllSmartFreezePackages(github.tornaco.android.thanos.core.pm.IPackageEnableStateChangeListener listener) throws android.os.RemoteException;
  public void freezeSmartFreezePackages(java.util.List<github.tornaco.android.thanos.core.pm.Pkg> packages, github.tornaco.android.thanos.core.pm.IPackageEnableStateChangeListener listener) throws android.os.RemoteException;
  public boolean hasFreezedPackageInUserWhiteListPkgSet() throws android.os.RemoteException;
  public java.util.List<android.content.ComponentName> getAllDisabledComponentsForPackage(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  public void setFreezeTipEnabled(boolean enable) throws android.os.RemoteException;
  public boolean isFreezeTipEnabled() throws android.os.RemoteException;
  /** @hide */
  static class _Parcel {
    static private <T> T readTypedObject(
        android.os.Parcel parcel,
        android.os.Parcelable.Creator<T> c) {
      if (parcel.readInt() != 0) {
          return c.createFromParcel(parcel);
      } else {
          return null;
      }
    }
    static private <T extends android.os.Parcelable> void writeTypedObject(
        android.os.Parcel parcel, T value, int parcelableFlags) {
      if (value != null) {
        parcel.writeInt(1);
        value.writeToParcel(parcel, parcelableFlags);
      } else {
        parcel.writeInt(0);
      }
    }
    static private <T extends android.os.Parcelable> void writeTypedList(
        android.os.Parcel parcel, java.util.List<T> value, int parcelableFlags) {
      if (value == null) {
        parcel.writeInt(-1);
      } else {
        int N = value.size();
        int i = 0;
        parcel.writeInt(N);
        while (i < N) {
    writeTypedObject(parcel, value.get(i), parcelableFlags);
          i++;
        }
      }
    }
  }
}
