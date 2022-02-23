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
    @Override public int getUidForPkgName(java.lang.String pkgName) throws android.os.RemoteException
    {
      return 0;
    }
    // ApplicationInfo

    @Override public java.util.List<github.tornaco.android.thanos.core.pm.AppInfo> getInstalledPkgs(int flags) throws android.os.RemoteException
    {
      return null;
    }
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
    @Override public void setComponentEnabledSetting(android.content.ComponentName componentName, int newState, int flags) throws android.os.RemoteException
    {
    }
    @Override public int getComponentEnabledSetting(android.content.ComponentName componentName) throws android.os.RemoteException
    {
      return 0;
    }
    @Override public boolean isComponentDisabledByThanox(android.content.ComponentName componentName) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean getApplicationEnableState(java.lang.String packageName) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setApplicationEnableState(java.lang.String packageName, boolean enable, boolean tmp) throws android.os.RemoteException
    {
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getActivities(java.lang.String packageName) throws android.os.RemoteException
    {
      return null;
    }
    @Override public int getActivitiesCount(java.lang.String packageName) throws android.os.RemoteException
    {
      return 0;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getActivitiesInBatch(java.lang.String packageName, int itemCountInEachBatch, int batchIndex) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getReceivers(java.lang.String packageName) throws android.os.RemoteException
    {
      return null;
    }
    @Override public int getReceiverCount(java.lang.String packageName) throws android.os.RemoteException
    {
      return 0;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getReceiversInBatch(java.lang.String packageName, int itemCountInEachBatch, int batchIndex) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getServices(java.lang.String packageName) throws android.os.RemoteException
    {
      return null;
    }
    @Override public int getServiceCount(java.lang.String packageName) throws android.os.RemoteException
    {
      return 0;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getServicesInBatch(java.lang.String packageName, int itemCountInEachBatch, int batchIndex) throws android.os.RemoteException
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
    @Override public void setPkgSmartFreezeEnabled(java.lang.String pkgName, boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isPkgSmartFreezeEnabled(java.lang.String pkgName) throws android.os.RemoteException
    {
      return false;
    }
    @Override public java.lang.String[] getSmartFreezePkgs() throws android.os.RemoteException
    {
      return null;
    }
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
    @Override public java.lang.String[] enableAllThanoxDisabledPackages(boolean removeFromSmartFreezeList) throws android.os.RemoteException
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
    @Override public github.tornaco.android.thanos.core.pm.PackageSet getPackageSetById(java.lang.String id, boolean withPackages) throws android.os.RemoteException
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
    @Override public void addToPackageSet(java.lang.String pkg, java.lang.String id) throws android.os.RemoteException
    {
    }
    @Override public void removeFromPackageSet(java.lang.String pkg, java.lang.String id) throws android.os.RemoteException
    {
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.pm.PackageSet> getPackageSetThatContainsPkg(java.lang.String pkg) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<java.lang.String> getPackageSetLabelsThatContainsPkg(java.lang.String pkg) throws android.os.RemoteException
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
    @Override public void setEnablePackageOnLaunchRequestEnabled(java.lang.String pkg, boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isEnablePackageOnLaunchRequestEnabled(java.lang.String pkg) throws android.os.RemoteException
    {
      return false;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getProviders(java.lang.String packageName) throws android.os.RemoteException
    {
      return null;
    }
    // Wrap api to skip permission check

    @Override public java.lang.String[] getPackagesForUid(int uid) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.lang.String mayEnableAppOnStartActivityIntent(android.content.Intent intent) throws android.os.RemoteException
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
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.pm.IPkgManager
  {
    private static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.pm.IPkgManager";
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
      switch (code)
      {
        case INTERFACE_TRANSACTION:
        {
          reply.writeString(descriptor);
          return true;
        }
        case TRANSACTION_getPkgNameForUid:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String[] _result = this.getPkgNameForUid(_arg0);
          reply.writeNoException();
          reply.writeStringArray(_result);
          return true;
        }
        case TRANSACTION_getUidForPkgName:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _result = this.getUidForPkgName(_arg0);
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        case TRANSACTION_getInstalledPkgs:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          java.util.List<github.tornaco.android.thanos.core.pm.AppInfo> _result = this.getInstalledPkgs(_arg0);
          reply.writeNoException();
          reply.writeTypedList(_result);
          return true;
        }
        case TRANSACTION_getAppInfo:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          github.tornaco.android.thanos.core.pm.AppInfo _result = this.getAppInfo(_arg0);
          reply.writeNoException();
          if ((_result!=null)) {
            reply.writeInt(1);
            _result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          }
          else {
            reply.writeInt(0);
          }
          return true;
        }
        case TRANSACTION_getWhiteListPkgs:
        {
          data.enforceInterface(descriptor);
          java.lang.String[] _result = this.getWhiteListPkgs();
          reply.writeNoException();
          reply.writeStringArray(_result);
          return true;
        }
        case TRANSACTION_isPkgInWhiteList:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.isPkgInWhiteList(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setComponentEnabledSetting:
        {
          data.enforceInterface(descriptor);
          android.content.ComponentName _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.content.ComponentName.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          int _arg1;
          _arg1 = data.readInt();
          int _arg2;
          _arg2 = data.readInt();
          this.setComponentEnabledSetting(_arg0, _arg1, _arg2);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_getComponentEnabledSetting:
        {
          data.enforceInterface(descriptor);
          android.content.ComponentName _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.content.ComponentName.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          int _result = this.getComponentEnabledSetting(_arg0);
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        case TRANSACTION_isComponentDisabledByThanox:
        {
          data.enforceInterface(descriptor);
          android.content.ComponentName _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.content.ComponentName.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          boolean _result = this.isComponentDisabledByThanox(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_getApplicationEnableState:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.getApplicationEnableState(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setApplicationEnableState:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          boolean _arg2;
          _arg2 = (0!=data.readInt());
          this.setApplicationEnableState(_arg0, _arg1, _arg2);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_getActivities:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> _result = this.getActivities(_arg0);
          reply.writeNoException();
          reply.writeTypedList(_result);
          return true;
        }
        case TRANSACTION_getActivitiesCount:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _result = this.getActivitiesCount(_arg0);
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        case TRANSACTION_getActivitiesInBatch:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          int _arg2;
          _arg2 = data.readInt();
          java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> _result = this.getActivitiesInBatch(_arg0, _arg1, _arg2);
          reply.writeNoException();
          reply.writeTypedList(_result);
          return true;
        }
        case TRANSACTION_getReceivers:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> _result = this.getReceivers(_arg0);
          reply.writeNoException();
          reply.writeTypedList(_result);
          return true;
        }
        case TRANSACTION_getReceiverCount:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _result = this.getReceiverCount(_arg0);
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        case TRANSACTION_getReceiversInBatch:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          int _arg2;
          _arg2 = data.readInt();
          java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> _result = this.getReceiversInBatch(_arg0, _arg1, _arg2);
          reply.writeNoException();
          reply.writeTypedList(_result);
          return true;
        }
        case TRANSACTION_getServices:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> _result = this.getServices(_arg0);
          reply.writeNoException();
          reply.writeTypedList(_result);
          return true;
        }
        case TRANSACTION_getServiceCount:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _result = this.getServiceCount(_arg0);
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        case TRANSACTION_getServicesInBatch:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          int _arg2;
          _arg2 = data.readInt();
          java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> _result = this.getServicesInBatch(_arg0, _arg1, _arg2);
          reply.writeNoException();
          reply.writeTypedList(_result);
          return true;
        }
        case TRANSACTION_setSmartFreezeEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setSmartFreezeEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isSmartFreezeEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.isSmartFreezeEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setPkgSmartFreezeEnabled:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setPkgSmartFreezeEnabled(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isPkgSmartFreezeEnabled:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.isPkgSmartFreezeEnabled(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_getSmartFreezePkgs:
        {
          data.enforceInterface(descriptor);
          java.lang.String[] _result = this.getSmartFreezePkgs();
          reply.writeNoException();
          reply.writeStringArray(_result);
          return true;
        }
        case TRANSACTION_launchSmartFreezePkg:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.launchSmartFreezePkg(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_setSmartFreezeScreenOffCheckEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setSmartFreezeScreenOffCheckEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isSmartFreezeScreenOffCheckEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.isSmartFreezeScreenOffCheckEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setSmartFreezeScreenOffCheckDelay:
        {
          data.enforceInterface(descriptor);
          long _arg0;
          _arg0 = data.readLong();
          this.setSmartFreezeScreenOffCheckDelay(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_getSmartFreezeScreenOffCheckDelay:
        {
          data.enforceInterface(descriptor);
          long _result = this.getSmartFreezeScreenOffCheckDelay();
          reply.writeNoException();
          reply.writeLong(_result);
          return true;
        }
        case TRANSACTION_queryLaunchIntentForPackage:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          android.content.Intent _result = this.queryLaunchIntentForPackage(_arg0);
          reply.writeNoException();
          if ((_result!=null)) {
            reply.writeInt(1);
            _result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          }
          else {
            reply.writeInt(0);
          }
          return true;
        }
        case TRANSACTION_enableAllThanoxDisabledPackages:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          java.lang.String[] _result = this.enableAllThanoxDisabledPackages(_arg0);
          reply.writeNoException();
          reply.writeStringArray(_result);
          return true;
        }
        case TRANSACTION_deviceHasGms:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.deviceHasGms();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_verifyBillingState:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.verifyBillingState();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_launchSmartFreezePkgThenKillOrigin:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.launchSmartFreezePkgThenKillOrigin(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isProtectedWhitelistEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.isProtectedWhitelistEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setProtectedWhitelistEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setProtectedWhitelistEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_addPlugin:
        {
          data.enforceInterface(descriptor);
          android.os.ParcelFileDescriptor _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.os.ParcelFileDescriptor.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          java.lang.String _arg1;
          _arg1 = data.readString();
          github.tornaco.android.thanos.core.pm.IAddPluginCallback _arg2;
          _arg2 = github.tornaco.android.thanos.core.pm.IAddPluginCallback.Stub.asInterface(data.readStrongBinder());
          this.addPlugin(_arg0, _arg1, _arg2);
          return true;
        }
        case TRANSACTION_removePlugin:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.removePlugin(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_hasPlugin:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.hasPlugin(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_isSmartFreezeHidePackageEventEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.isSmartFreezeHidePackageEventEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setSmartFreezeHidePackageEventEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setSmartFreezeHidePackageEventEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_dump:
        {
          data.enforceInterface(descriptor);
          github.tornaco.android.thanos.core.IPrinter _arg0;
          _arg0 = github.tornaco.android.thanos.core.IPrinter.Stub.asInterface(data.readStrongBinder());
          this.dump(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_setPackageBlockUninstallEnabled:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setPackageBlockUninstallEnabled(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isPackageBlockUninstallEnabled:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.isPackageBlockUninstallEnabled(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setPackageBlockClearDataEnabled:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setPackageBlockClearDataEnabled(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isPackageBlockClearDataEnabled:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.isPackageBlockClearDataEnabled(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_getInstalledPackagesCount:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          int _result = this.getInstalledPackagesCount(_arg0);
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        case TRANSACTION_createPackageSet:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          github.tornaco.android.thanos.core.pm.PackageSet _result = this.createPackageSet(_arg0);
          reply.writeNoException();
          if ((_result!=null)) {
            reply.writeInt(1);
            _result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          }
          else {
            reply.writeInt(0);
          }
          return true;
        }
        case TRANSACTION_removePackageSet:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.removePackageSet(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_getPackageSetById:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          github.tornaco.android.thanos.core.pm.PackageSet _result = this.getPackageSetById(_arg0, _arg1);
          reply.writeNoException();
          if ((_result!=null)) {
            reply.writeInt(1);
            _result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          }
          else {
            reply.writeInt(0);
          }
          return true;
        }
        case TRANSACTION_getAllPackageSets:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          java.util.List<github.tornaco.android.thanos.core.pm.PackageSet> _result = this.getAllPackageSets(_arg0);
          reply.writeNoException();
          reply.writeTypedList(_result);
          return true;
        }
        case TRANSACTION_getAllPackageSetIds:
        {
          data.enforceInterface(descriptor);
          java.util.List<java.lang.String> _result = this.getAllPackageSetIds();
          reply.writeNoException();
          reply.writeStringList(_result);
          return true;
        }
        case TRANSACTION_addToPackageSet:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.addToPackageSet(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_removeFromPackageSet:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.removeFromPackageSet(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_getPackageSetThatContainsPkg:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.util.List<github.tornaco.android.thanos.core.pm.PackageSet> _result = this.getPackageSetThatContainsPkg(_arg0);
          reply.writeNoException();
          reply.writeTypedList(_result);
          return true;
        }
        case TRANSACTION_getPackageSetLabelsThatContainsPkg:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.util.List<java.lang.String> _result = this.getPackageSetLabelsThatContainsPkg(_arg0);
          reply.writeNoException();
          reply.writeStringList(_result);
          return true;
        }
        case TRANSACTION_setFreezePkgWithSuspendEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setFreezePkgWithSuspendEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isFreezePkgWithSuspendEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.isFreezePkgWithSuspendEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_getInstalledPkgsByPackageSetId:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.util.List<github.tornaco.android.thanos.core.pm.AppInfo> _result = this.getInstalledPkgsByPackageSetId(_arg0);
          reply.writeNoException();
          reply.writeTypedList(_result);
          return true;
        }
        case TRANSACTION_registerPackageSetChangeListener:
        {
          data.enforceInterface(descriptor);
          github.tornaco.android.thanos.core.pm.IPackageSetChangeListener _arg0;
          _arg0 = github.tornaco.android.thanos.core.pm.IPackageSetChangeListener.Stub.asInterface(data.readStrongBinder());
          this.registerPackageSetChangeListener(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_unRegisterPackageSetChangeListener:
        {
          data.enforceInterface(descriptor);
          github.tornaco.android.thanos.core.pm.IPackageSetChangeListener _arg0;
          _arg0 = github.tornaco.android.thanos.core.pm.IPackageSetChangeListener.Stub.asInterface(data.readStrongBinder());
          this.unRegisterPackageSetChangeListener(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_setEnablePackageOnLaunchRequestEnabled:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setEnablePackageOnLaunchRequestEnabled(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isEnablePackageOnLaunchRequestEnabled:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.isEnablePackageOnLaunchRequestEnabled(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_getProviders:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> _result = this.getProviders(_arg0);
          reply.writeNoException();
          reply.writeTypedList(_result);
          return true;
        }
        case TRANSACTION_getPackagesForUid:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String[] _result = this.getPackagesForUid(_arg0);
          reply.writeNoException();
          reply.writeStringArray(_result);
          return true;
        }
        case TRANSACTION_mayEnableAppOnStartActivityIntent:
        {
          data.enforceInterface(descriptor);
          android.content.Intent _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.content.Intent.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          java.lang.String _result = this.mayEnableAppOnStartActivityIntent(_arg0);
          reply.writeNoException();
          reply.writeString(_result);
          return true;
        }
        case TRANSACTION_isEnablePkgOnLaunchByDefault:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.isEnablePkgOnLaunchByDefault();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setEnablePkgOnLaunchByDefaultEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setEnablePkgOnLaunchByDefaultEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
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
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getPkgNameForUid(uid);
          }
          _reply.readException();
          _result = _reply.createStringArray();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public int getUidForPkgName(java.lang.String pkgName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getUidForPkgName, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getUidForPkgName(pkgName);
          }
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
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getInstalledPkgs(flags);
          }
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.pm.AppInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public github.tornaco.android.thanos.core.pm.AppInfo getAppInfo(java.lang.String pkgName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.pm.AppInfo _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAppInfo, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getAppInfo(pkgName);
          }
          _reply.readException();
          if ((0!=_reply.readInt())) {
            _result = github.tornaco.android.thanos.core.pm.AppInfo.CREATOR.createFromParcel(_reply);
          }
          else {
            _result = null;
          }
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
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getWhiteListPkgs();
          }
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
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isPkgInWhiteList(pkg);
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setComponentEnabledSetting(android.content.ComponentName componentName, int newState, int flags) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((componentName!=null)) {
            _data.writeInt(1);
            componentName.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          _data.writeInt(newState);
          _data.writeInt(flags);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setComponentEnabledSetting, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setComponentEnabledSetting(componentName, newState, flags);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public int getComponentEnabledSetting(android.content.ComponentName componentName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((componentName!=null)) {
            _data.writeInt(1);
            componentName.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_getComponentEnabledSetting, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getComponentEnabledSetting(componentName);
          }
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean isComponentDisabledByThanox(android.content.ComponentName componentName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((componentName!=null)) {
            _data.writeInt(1);
            componentName.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_isComponentDisabledByThanox, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isComponentDisabledByThanox(componentName);
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean getApplicationEnableState(java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getApplicationEnableState, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getApplicationEnableState(packageName);
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setApplicationEnableState(java.lang.String packageName, boolean enable, boolean tmp) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          _data.writeInt(((enable)?(1):(0)));
          _data.writeInt(((tmp)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setApplicationEnableState, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setApplicationEnableState(packageName, enable, tmp);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getActivities(java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getActivities, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getActivities(packageName);
          }
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
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getActivitiesCount(packageName);
          }
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getActivitiesInBatch(java.lang.String packageName, int itemCountInEachBatch, int batchIndex) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          _data.writeInt(itemCountInEachBatch);
          _data.writeInt(batchIndex);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getActivitiesInBatch, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getActivitiesInBatch(packageName, itemCountInEachBatch, batchIndex);
          }
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.pm.ComponentInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getReceivers(java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getReceivers, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getReceivers(packageName);
          }
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
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getReceiverCount(packageName);
          }
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getReceiversInBatch(java.lang.String packageName, int itemCountInEachBatch, int batchIndex) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          _data.writeInt(itemCountInEachBatch);
          _data.writeInt(batchIndex);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getReceiversInBatch, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getReceiversInBatch(packageName, itemCountInEachBatch, batchIndex);
          }
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.pm.ComponentInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getServices(java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getServices, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getServices(packageName);
          }
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
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getServiceCount(packageName);
          }
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getServicesInBatch(java.lang.String packageName, int itemCountInEachBatch, int batchIndex) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          _data.writeInt(itemCountInEachBatch);
          _data.writeInt(batchIndex);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getServicesInBatch, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getServicesInBatch(packageName, itemCountInEachBatch, batchIndex);
          }
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
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setSmartFreezeEnabled(enable);
            return;
          }
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
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isSmartFreezeEnabled();
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setPkgSmartFreezeEnabled(java.lang.String pkgName, boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPkgSmartFreezeEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setPkgSmartFreezeEnabled(pkgName, enable);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isPkgSmartFreezeEnabled(java.lang.String pkgName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPkgSmartFreezeEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isPkgSmartFreezeEnabled(pkgName);
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.lang.String[] getSmartFreezePkgs() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getSmartFreezePkgs, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getSmartFreezePkgs();
          }
          _reply.readException();
          _result = _reply.createStringArray();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void launchSmartFreezePkg(java.lang.String pkgName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_launchSmartFreezePkg, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().launchSmartFreezePkg(pkgName);
            return;
          }
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
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setSmartFreezeScreenOffCheckEnabled(enable);
            return;
          }
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
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isSmartFreezeScreenOffCheckEnabled();
          }
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
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setSmartFreezeScreenOffCheckDelay(delay);
            return;
          }
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
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getSmartFreezeScreenOffCheckDelay();
          }
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
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().queryLaunchIntentForPackage(pkgName);
          }
          _reply.readException();
          if ((0!=_reply.readInt())) {
            _result = android.content.Intent.CREATOR.createFromParcel(_reply);
          }
          else {
            _result = null;
          }
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.lang.String[] enableAllThanoxDisabledPackages(boolean removeFromSmartFreezeList) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((removeFromSmartFreezeList)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_enableAllThanoxDisabledPackages, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().enableAllThanoxDisabledPackages(removeFromSmartFreezeList);
          }
          _reply.readException();
          _result = _reply.createStringArray();
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
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().deviceHasGms();
          }
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
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().verifyBillingState();
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void launchSmartFreezePkgThenKillOrigin(java.lang.String pkgName, java.lang.String origin) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          _data.writeString(origin);
          boolean _status = mRemote.transact(Stub.TRANSACTION_launchSmartFreezePkgThenKillOrigin, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().launchSmartFreezePkgThenKillOrigin(pkgName, origin);
            return;
          }
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
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isProtectedWhitelistEnabled();
          }
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
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setProtectedWhitelistEnabled(enable);
            return;
          }
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
          if ((pfd!=null)) {
            _data.writeInt(1);
            pfd.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          _data.writeString(pluginPackageName);
          _data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_addPlugin, _data, null, android.os.IBinder.FLAG_ONEWAY);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().addPlugin(pfd, pluginPackageName, callback);
            return;
          }
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
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().removePlugin(pluginPackageName);
            return;
          }
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
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().hasPlugin(pluginPackageName);
          }
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
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isSmartFreezeHidePackageEventEnabled();
          }
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
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setSmartFreezeHidePackageEventEnabled(enabled);
            return;
          }
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
          _data.writeStrongBinder((((p!=null))?(p.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_dump, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().dump(p);
            return;
          }
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
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setPackageBlockUninstallEnabled(pkgName, enable);
            return;
          }
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
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isPackageBlockUninstallEnabled(pkgName);
          }
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
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setPackageBlockClearDataEnabled(pkgName, enable);
            return;
          }
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
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isPackageBlockClearDataEnabled(pkgName);
          }
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
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getInstalledPackagesCount(appFlags);
          }
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
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().createPackageSet(label);
          }
          _reply.readException();
          if ((0!=_reply.readInt())) {
            _result = github.tornaco.android.thanos.core.pm.PackageSet.CREATOR.createFromParcel(_reply);
          }
          else {
            _result = null;
          }
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
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().removePackageSet(id);
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public github.tornaco.android.thanos.core.pm.PackageSet getPackageSetById(java.lang.String id, boolean withPackages) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.pm.PackageSet _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(id);
          _data.writeInt(((withPackages)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_getPackageSetById, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getPackageSetById(id, withPackages);
          }
          _reply.readException();
          if ((0!=_reply.readInt())) {
            _result = github.tornaco.android.thanos.core.pm.PackageSet.CREATOR.createFromParcel(_reply);
          }
          else {
            _result = null;
          }
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
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getAllPackageSets(withPackages);
          }
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
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getAllPackageSetIds();
          }
          _reply.readException();
          _result = _reply.createStringArrayList();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void addToPackageSet(java.lang.String pkg, java.lang.String id) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          _data.writeString(id);
          boolean _status = mRemote.transact(Stub.TRANSACTION_addToPackageSet, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().addToPackageSet(pkg, id);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void removeFromPackageSet(java.lang.String pkg, java.lang.String id) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          _data.writeString(id);
          boolean _status = mRemote.transact(Stub.TRANSACTION_removeFromPackageSet, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().removeFromPackageSet(pkg, id);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.pm.PackageSet> getPackageSetThatContainsPkg(java.lang.String pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.pm.PackageSet> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getPackageSetThatContainsPkg, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getPackageSetThatContainsPkg(pkg);
          }
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.pm.PackageSet.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<java.lang.String> getPackageSetLabelsThatContainsPkg(java.lang.String pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<java.lang.String> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getPackageSetLabelsThatContainsPkg, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getPackageSetLabelsThatContainsPkg(pkg);
          }
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
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setFreezePkgWithSuspendEnabled(enable);
            return;
          }
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
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isFreezePkgWithSuspendEnabled();
          }
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
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getInstalledPkgsByPackageSetId(pkgSetId);
          }
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
          _data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_registerPackageSetChangeListener, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().registerPackageSetChangeListener(listener);
            return;
          }
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
          _data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_unRegisterPackageSetChangeListener, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().unRegisterPackageSetChangeListener(listener);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setEnablePackageOnLaunchRequestEnabled(java.lang.String pkg, boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setEnablePackageOnLaunchRequestEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setEnablePackageOnLaunchRequestEnabled(pkg, enable);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isEnablePackageOnLaunchRequestEnabled(java.lang.String pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isEnablePackageOnLaunchRequestEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isEnablePackageOnLaunchRequestEnabled(pkg);
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getProviders(java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getProviders, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getProviders(packageName);
          }
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
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getPackagesForUid(uid);
          }
          _reply.readException();
          _result = _reply.createStringArray();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.lang.String mayEnableAppOnStartActivityIntent(android.content.Intent intent) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((intent!=null)) {
            _data.writeInt(1);
            intent.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_mayEnableAppOnStartActivityIntent, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().mayEnableAppOnStartActivityIntent(intent);
          }
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
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isEnablePkgOnLaunchByDefault();
          }
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
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setEnablePkgOnLaunchByDefaultEnabled(byDefault);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      public static github.tornaco.android.thanos.core.pm.IPkgManager sDefaultImpl;
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
    public static boolean setDefaultImpl(github.tornaco.android.thanos.core.pm.IPkgManager impl) {
      // Only one user of this interface can use this function
      // at a time. This is a heuristic to detect if two different
      // users in the same process use this function.
      if (Stub.Proxy.sDefaultImpl != null) {
        throw new IllegalStateException("setDefaultImpl() called twice");
      }
      if (impl != null) {
        Stub.Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static github.tornaco.android.thanos.core.pm.IPkgManager getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public java.lang.String[] getPkgNameForUid(int uid) throws android.os.RemoteException;
  public int getUidForPkgName(java.lang.String pkgName) throws android.os.RemoteException;
  // ApplicationInfo

  public java.util.List<github.tornaco.android.thanos.core.pm.AppInfo> getInstalledPkgs(int flags) throws android.os.RemoteException;
  public github.tornaco.android.thanos.core.pm.AppInfo getAppInfo(java.lang.String pkgName) throws android.os.RemoteException;
  public java.lang.String[] getWhiteListPkgs() throws android.os.RemoteException;
  public boolean isPkgInWhiteList(java.lang.String pkg) throws android.os.RemoteException;
  public void setComponentEnabledSetting(android.content.ComponentName componentName, int newState, int flags) throws android.os.RemoteException;
  public int getComponentEnabledSetting(android.content.ComponentName componentName) throws android.os.RemoteException;
  public boolean isComponentDisabledByThanox(android.content.ComponentName componentName) throws android.os.RemoteException;
  public boolean getApplicationEnableState(java.lang.String packageName) throws android.os.RemoteException;
  public void setApplicationEnableState(java.lang.String packageName, boolean enable, boolean tmp) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getActivities(java.lang.String packageName) throws android.os.RemoteException;
  public int getActivitiesCount(java.lang.String packageName) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getActivitiesInBatch(java.lang.String packageName, int itemCountInEachBatch, int batchIndex) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getReceivers(java.lang.String packageName) throws android.os.RemoteException;
  public int getReceiverCount(java.lang.String packageName) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getReceiversInBatch(java.lang.String packageName, int itemCountInEachBatch, int batchIndex) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getServices(java.lang.String packageName) throws android.os.RemoteException;
  public int getServiceCount(java.lang.String packageName) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getServicesInBatch(java.lang.String packageName, int itemCountInEachBatch, int batchIndex) throws android.os.RemoteException;
  public void setSmartFreezeEnabled(boolean enable) throws android.os.RemoteException;
  public boolean isSmartFreezeEnabled() throws android.os.RemoteException;
  public void setPkgSmartFreezeEnabled(java.lang.String pkgName, boolean enable) throws android.os.RemoteException;
  public boolean isPkgSmartFreezeEnabled(java.lang.String pkgName) throws android.os.RemoteException;
  public java.lang.String[] getSmartFreezePkgs() throws android.os.RemoteException;
  public void launchSmartFreezePkg(java.lang.String pkgName) throws android.os.RemoteException;
  public void setSmartFreezeScreenOffCheckEnabled(boolean enable) throws android.os.RemoteException;
  public boolean isSmartFreezeScreenOffCheckEnabled() throws android.os.RemoteException;
  public void setSmartFreezeScreenOffCheckDelay(long delay) throws android.os.RemoteException;
  public long getSmartFreezeScreenOffCheckDelay() throws android.os.RemoteException;
  public android.content.Intent queryLaunchIntentForPackage(java.lang.String pkgName) throws android.os.RemoteException;
  public java.lang.String[] enableAllThanoxDisabledPackages(boolean removeFromSmartFreezeList) throws android.os.RemoteException;
  public boolean deviceHasGms() throws android.os.RemoteException;
  public boolean verifyBillingState() throws android.os.RemoteException;
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
  public github.tornaco.android.thanos.core.pm.PackageSet getPackageSetById(java.lang.String id, boolean withPackages) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.pm.PackageSet> getAllPackageSets(boolean withPackages) throws android.os.RemoteException;
  public java.util.List<java.lang.String> getAllPackageSetIds() throws android.os.RemoteException;
  public void addToPackageSet(java.lang.String pkg, java.lang.String id) throws android.os.RemoteException;
  public void removeFromPackageSet(java.lang.String pkg, java.lang.String id) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.pm.PackageSet> getPackageSetThatContainsPkg(java.lang.String pkg) throws android.os.RemoteException;
  public java.util.List<java.lang.String> getPackageSetLabelsThatContainsPkg(java.lang.String pkg) throws android.os.RemoteException;
  public void setFreezePkgWithSuspendEnabled(boolean enable) throws android.os.RemoteException;
  public boolean isFreezePkgWithSuspendEnabled() throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.pm.AppInfo> getInstalledPkgsByPackageSetId(java.lang.String pkgSetId) throws android.os.RemoteException;
  public void registerPackageSetChangeListener(github.tornaco.android.thanos.core.pm.IPackageSetChangeListener listener) throws android.os.RemoteException;
  public void unRegisterPackageSetChangeListener(github.tornaco.android.thanos.core.pm.IPackageSetChangeListener listener) throws android.os.RemoteException;
  public void setEnablePackageOnLaunchRequestEnabled(java.lang.String pkg, boolean enable) throws android.os.RemoteException;
  public boolean isEnablePackageOnLaunchRequestEnabled(java.lang.String pkg) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.pm.ComponentInfo> getProviders(java.lang.String packageName) throws android.os.RemoteException;
  // Wrap api to skip permission check

  public java.lang.String[] getPackagesForUid(int uid) throws android.os.RemoteException;
  public java.lang.String mayEnableAppOnStartActivityIntent(android.content.Intent intent) throws android.os.RemoteException;
  public boolean isEnablePkgOnLaunchByDefault() throws android.os.RemoteException;
  public void setEnablePkgOnLaunchByDefaultEnabled(boolean byDefault) throws android.os.RemoteException;
}
