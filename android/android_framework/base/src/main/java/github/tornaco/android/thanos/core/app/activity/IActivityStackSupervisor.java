/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /home/tornaco/Android/Sdk/build-tools/35.0.0/aidl -I/home/tornaco/Documents/Thanox/android/android_framework/base/src/main/java -p/home/tornaco/Documents/Thanox/android/android_sdk/framework.aidl -p/home/tornaco/Documents/Thanox/android/android_sdk/thanos.aidl /home/tornaco/Documents/Thanox/android/android_framework/base/src/main/java/github/tornaco/android/thanos/core/app/activity/IActivityStackSupervisor.aidl
 */
package github.tornaco.android.thanos.core.app.activity;
public interface IActivityStackSupervisor extends android.os.IInterface
{
  /** Default implementation for IActivityStackSupervisor. */
  public static class Default implements github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor
  {
    /** @deprecated use {@link #replaceActivityStartingIntent} instead */
    @Override public boolean checkActivity(android.content.ComponentName componentName, int userId, android.os.IBinder resultTo) throws android.os.RemoteException
    {
      return false;
    }
    @Override public android.content.Intent replaceActivityStartingIntent(android.content.Intent intent, int userId, android.os.IBinder resultTo, java.lang.String callingPkgName) throws android.os.RemoteException
    {
      return null;
    }
    @Override public boolean shouldVerifyActivityStarting(android.content.ComponentName componentName, java.lang.String pkg, java.lang.String source) throws android.os.RemoteException
    {
      return false;
    }
    @Override public java.lang.String getCurrentFrontApp() throws android.os.RemoteException
    {
      return null;
    }
    @Override public void setAppLockEnabled(boolean enabled) throws android.os.RemoteException
    {
    }
    @Override public boolean isAppLockEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean isPackageLocked(java.lang.String pkg) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setPackageLocked(java.lang.String pkg, boolean locked) throws android.os.RemoteException
    {
    }
    @Override public void setVerifyResult(int request, int result, int reason) throws android.os.RemoteException
    {
    }
    @Override public void addComponentReplacement(github.tornaco.android.thanos.core.app.component.ComponentReplacement replacement) throws android.os.RemoteException
    {
    }
    @Override public void removeComponentReplacement(github.tornaco.android.thanos.core.app.component.ComponentReplacement replacement) throws android.os.RemoteException
    {
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.app.component.ComponentReplacement> getComponentReplacements() throws android.os.RemoteException
    {
      return null;
    }
    @Override public void setActivityTrampolineEnabled(boolean enabled) throws android.os.RemoteException
    {
    }
    @Override public boolean isActivityTrampolineEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setShowCurrentComponentViewEnabled(boolean enabled) throws android.os.RemoteException
    {
    }
    @Override public boolean isShowCurrentComponentViewEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void registerTopPackageChangeListener(github.tornaco.android.thanos.core.app.activity.ITopPackageChangeListener listener) throws android.os.RemoteException
    {
    }
    @Override public void unRegisterTopPackageChangeListener(github.tornaco.android.thanos.core.app.activity.ITopPackageChangeListener listener) throws android.os.RemoteException
    {
    }
    @Override public boolean isVerifyOnScreenOffEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setVerifyOnScreenOffEnabled(boolean enabled) throws android.os.RemoteException
    {
    }
    @Override public boolean isVerifyOnAppSwitchEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setVerifyOnAppSwitchEnabled(boolean enabled) throws android.os.RemoteException
    {
    }
    @Override public boolean isVerifyOnTaskRemovedEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setVerifyOnTaskRemovedEnabled(boolean enabled) throws android.os.RemoteException
    {
    }
    // Bridge API to report app events.
    @Override public android.content.Intent reportOnStartActivity(java.lang.String callingPackage, android.content.Intent intent) throws android.os.RemoteException
    {
      return null;
    }
    @Override public void reportOnActivityStopped(android.os.IBinder token) throws android.os.RemoteException
    {
    }
    @Override public void reportOnActivityResumed(android.os.IBinder token) throws android.os.RemoteException
    {
    }
    @Override public void dump(github.tornaco.android.thanos.core.IPrinter p) throws android.os.RemoteException
    {
    }
    @Override public void registerActivityLifecycleListener(github.tornaco.android.thanos.core.app.activity.IActivityLifecycleListener listener) throws android.os.RemoteException
    {
    }
    @Override public void unRegisterActivityLifecycleListener(github.tornaco.android.thanos.core.app.activity.IActivityLifecycleListener listener) throws android.os.RemoteException
    {
    }
    @Override public void addAppLockWhiteListComponents(java.util.List<android.content.ComponentName> componentName) throws android.os.RemoteException
    {
    }
    @Override public void removeAppLockWhiteListComponents(java.util.List<android.content.ComponentName> componentName) throws android.os.RemoteException
    {
    }
    @Override public java.util.List<android.content.ComponentName> getAppLockWhiteListComponents() throws android.os.RemoteException
    {
      return null;
    }
    @Override public int getLaunchOtherAppSetting(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
      return 0;
    }
    @Override public void setLaunchOtherAppSetting(github.tornaco.android.thanos.core.pm.Pkg pkg, int setting) throws android.os.RemoteException
    {
    }
    @Override public boolean isLaunchOtherAppBlockerEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setLaunchOtherAppBlockerEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public void addLaunchOtherAppRule(java.lang.String rule) throws android.os.RemoteException
    {
    }
    @Override public void deleteLaunchOtherAppRule(java.lang.String rule) throws android.os.RemoteException
    {
    }
    @Override public java.lang.String[] getAllLaunchOtherAppRules() throws android.os.RemoteException
    {
      return null;
    }
    @Override public void removePkgFromLaunchOtherAppAllowList(github.tornaco.android.thanos.core.pm.Pkg pkg, github.tornaco.android.thanos.core.pm.Pkg pkgToRemove) throws android.os.RemoteException
    {
    }
    @Override public void addPkgToLaunchOtherAppAllowList(github.tornaco.android.thanos.core.pm.Pkg pkg, github.tornaco.android.thanos.core.pm.Pkg pkgToAdd) throws android.os.RemoteException
    {
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.pm.Pkg> getLaunchOtherAppAllowListOrNull(github.tornaco.android.thanos.core.pm.Pkg callerPkg) throws android.os.RemoteException
    {
      return null;
    }
    @Override public int getLockMethod() throws android.os.RemoteException
    {
      return 0;
    }
    @Override public void setLockMethod(int method) throws android.os.RemoteException
    {
    }
    @Override public void setLockPattern(java.lang.String pattern) throws android.os.RemoteException
    {
    }
    @Override public java.lang.String getLockPattern() throws android.os.RemoteException
    {
      return null;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor))) {
        return ((github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor)iin);
      }
      return new github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor.Stub.Proxy(obj);
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
      if (code == INTERFACE_TRANSACTION) {
        reply.writeString(descriptor);
        return true;
      }
      switch (code)
      {
        case TRANSACTION_checkActivity:
        {
          android.content.ComponentName _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.content.ComponentName.CREATOR);
          int _arg1;
          _arg1 = data.readInt();
          android.os.IBinder _arg2;
          _arg2 = data.readStrongBinder();
          boolean _result = this.checkActivity(_arg0, _arg1, _arg2);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_replaceActivityStartingIntent:
        {
          android.content.Intent _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.content.Intent.CREATOR);
          int _arg1;
          _arg1 = data.readInt();
          android.os.IBinder _arg2;
          _arg2 = data.readStrongBinder();
          java.lang.String _arg3;
          _arg3 = data.readString();
          android.content.Intent _result = this.replaceActivityStartingIntent(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_shouldVerifyActivityStarting:
        {
          android.content.ComponentName _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.content.ComponentName.CREATOR);
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.lang.String _arg2;
          _arg2 = data.readString();
          boolean _result = this.shouldVerifyActivityStarting(_arg0, _arg1, _arg2);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_getCurrentFrontApp:
        {
          java.lang.String _result = this.getCurrentFrontApp();
          reply.writeNoException();
          reply.writeString(_result);
          break;
        }
        case TRANSACTION_setAppLockEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setAppLockEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isAppLockEnabled:
        {
          boolean _result = this.isAppLockEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_isPackageLocked:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.isPackageLocked(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setPackageLocked:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setPackageLocked(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_setVerifyResult:
        {
          int _arg0;
          _arg0 = data.readInt();
          int _arg1;
          _arg1 = data.readInt();
          int _arg2;
          _arg2 = data.readInt();
          this.setVerifyResult(_arg0, _arg1, _arg2);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_addComponentReplacement:
        {
          github.tornaco.android.thanos.core.app.component.ComponentReplacement _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.app.component.ComponentReplacement.CREATOR);
          this.addComponentReplacement(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_removeComponentReplacement:
        {
          github.tornaco.android.thanos.core.app.component.ComponentReplacement _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.app.component.ComponentReplacement.CREATOR);
          this.removeComponentReplacement(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getComponentReplacements:
        {
          java.util.List<github.tornaco.android.thanos.core.app.component.ComponentReplacement> _result = this.getComponentReplacements();
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_setActivityTrampolineEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setActivityTrampolineEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isActivityTrampolineEnabled:
        {
          boolean _result = this.isActivityTrampolineEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setShowCurrentComponentViewEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setShowCurrentComponentViewEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isShowCurrentComponentViewEnabled:
        {
          boolean _result = this.isShowCurrentComponentViewEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_registerTopPackageChangeListener:
        {
          github.tornaco.android.thanos.core.app.activity.ITopPackageChangeListener _arg0;
          _arg0 = github.tornaco.android.thanos.core.app.activity.ITopPackageChangeListener.Stub.asInterface(data.readStrongBinder());
          this.registerTopPackageChangeListener(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_unRegisterTopPackageChangeListener:
        {
          github.tornaco.android.thanos.core.app.activity.ITopPackageChangeListener _arg0;
          _arg0 = github.tornaco.android.thanos.core.app.activity.ITopPackageChangeListener.Stub.asInterface(data.readStrongBinder());
          this.unRegisterTopPackageChangeListener(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isVerifyOnScreenOffEnabled:
        {
          boolean _result = this.isVerifyOnScreenOffEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setVerifyOnScreenOffEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setVerifyOnScreenOffEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isVerifyOnAppSwitchEnabled:
        {
          boolean _result = this.isVerifyOnAppSwitchEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setVerifyOnAppSwitchEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setVerifyOnAppSwitchEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isVerifyOnTaskRemovedEnabled:
        {
          boolean _result = this.isVerifyOnTaskRemovedEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setVerifyOnTaskRemovedEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setVerifyOnTaskRemovedEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_reportOnStartActivity:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          android.content.Intent _arg1;
          _arg1 = _Parcel.readTypedObject(data, android.content.Intent.CREATOR);
          android.content.Intent _result = this.reportOnStartActivity(_arg0, _arg1);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_reportOnActivityStopped:
        {
          android.os.IBinder _arg0;
          _arg0 = data.readStrongBinder();
          this.reportOnActivityStopped(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_reportOnActivityResumed:
        {
          android.os.IBinder _arg0;
          _arg0 = data.readStrongBinder();
          this.reportOnActivityResumed(_arg0);
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
        case TRANSACTION_registerActivityLifecycleListener:
        {
          github.tornaco.android.thanos.core.app.activity.IActivityLifecycleListener _arg0;
          _arg0 = github.tornaco.android.thanos.core.app.activity.IActivityLifecycleListener.Stub.asInterface(data.readStrongBinder());
          this.registerActivityLifecycleListener(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_unRegisterActivityLifecycleListener:
        {
          github.tornaco.android.thanos.core.app.activity.IActivityLifecycleListener _arg0;
          _arg0 = github.tornaco.android.thanos.core.app.activity.IActivityLifecycleListener.Stub.asInterface(data.readStrongBinder());
          this.unRegisterActivityLifecycleListener(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_addAppLockWhiteListComponents:
        {
          java.util.List<android.content.ComponentName> _arg0;
          _arg0 = data.createTypedArrayList(android.content.ComponentName.CREATOR);
          this.addAppLockWhiteListComponents(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_removeAppLockWhiteListComponents:
        {
          java.util.List<android.content.ComponentName> _arg0;
          _arg0 = data.createTypedArrayList(android.content.ComponentName.CREATOR);
          this.removeAppLockWhiteListComponents(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getAppLockWhiteListComponents:
        {
          java.util.List<android.content.ComponentName> _result = this.getAppLockWhiteListComponents();
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getLaunchOtherAppSetting:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          int _result = this.getLaunchOtherAppSetting(_arg0);
          reply.writeNoException();
          reply.writeInt(_result);
          break;
        }
        case TRANSACTION_setLaunchOtherAppSetting:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          int _arg1;
          _arg1 = data.readInt();
          this.setLaunchOtherAppSetting(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isLaunchOtherAppBlockerEnabled:
        {
          boolean _result = this.isLaunchOtherAppBlockerEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setLaunchOtherAppBlockerEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setLaunchOtherAppBlockerEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_addLaunchOtherAppRule:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.addLaunchOtherAppRule(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_deleteLaunchOtherAppRule:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.deleteLaunchOtherAppRule(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getAllLaunchOtherAppRules:
        {
          java.lang.String[] _result = this.getAllLaunchOtherAppRules();
          reply.writeNoException();
          reply.writeStringArray(_result);
          break;
        }
        case TRANSACTION_removePkgFromLaunchOtherAppAllowList:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          github.tornaco.android.thanos.core.pm.Pkg _arg1;
          _arg1 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          this.removePkgFromLaunchOtherAppAllowList(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_addPkgToLaunchOtherAppAllowList:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          github.tornaco.android.thanos.core.pm.Pkg _arg1;
          _arg1 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          this.addPkgToLaunchOtherAppAllowList(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getLaunchOtherAppAllowListOrNull:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          java.util.List<github.tornaco.android.thanos.core.pm.Pkg> _result = this.getLaunchOtherAppAllowListOrNull(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getLockMethod:
        {
          int _result = this.getLockMethod();
          reply.writeNoException();
          reply.writeInt(_result);
          break;
        }
        case TRANSACTION_setLockMethod:
        {
          int _arg0;
          _arg0 = data.readInt();
          this.setLockMethod(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_setLockPattern:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.setLockPattern(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getLockPattern:
        {
          java.lang.String _result = this.getLockPattern();
          reply.writeNoException();
          reply.writeString(_result);
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static class Proxy implements github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor
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
      /** @deprecated use {@link #replaceActivityStartingIntent} instead */
      @Override public boolean checkActivity(android.content.ComponentName componentName, int userId, android.os.IBinder resultTo) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, componentName, 0);
          _data.writeInt(userId);
          _data.writeStrongBinder(resultTo);
          boolean _status = mRemote.transact(Stub.TRANSACTION_checkActivity, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public android.content.Intent replaceActivityStartingIntent(android.content.Intent intent, int userId, android.os.IBinder resultTo, java.lang.String callingPkgName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.content.Intent _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, intent, 0);
          _data.writeInt(userId);
          _data.writeStrongBinder(resultTo);
          _data.writeString(callingPkgName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_replaceActivityStartingIntent, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, android.content.Intent.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean shouldVerifyActivityStarting(android.content.ComponentName componentName, java.lang.String pkg, java.lang.String source) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, componentName, 0);
          _data.writeString(pkg);
          _data.writeString(source);
          boolean _status = mRemote.transact(Stub.TRANSACTION_shouldVerifyActivityStarting, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.lang.String getCurrentFrontApp() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getCurrentFrontApp, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readString();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setAppLockEnabled(boolean enabled) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enabled)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setAppLockEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isAppLockEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isAppLockEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean isPackageLocked(java.lang.String pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPackageLocked, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setPackageLocked(java.lang.String pkg, boolean locked) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          _data.writeInt(((locked)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPackageLocked, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setVerifyResult(int request, int result, int reason) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(request);
          _data.writeInt(result);
          _data.writeInt(reason);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setVerifyResult, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void addComponentReplacement(github.tornaco.android.thanos.core.app.component.ComponentReplacement replacement) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, replacement, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_addComponentReplacement, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void removeComponentReplacement(github.tornaco.android.thanos.core.app.component.ComponentReplacement replacement) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, replacement, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_removeComponentReplacement, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.app.component.ComponentReplacement> getComponentReplacements() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.app.component.ComponentReplacement> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getComponentReplacements, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.app.component.ComponentReplacement.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setActivityTrampolineEnabled(boolean enabled) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enabled)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setActivityTrampolineEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isActivityTrampolineEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isActivityTrampolineEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setShowCurrentComponentViewEnabled(boolean enabled) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enabled)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setShowCurrentComponentViewEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isShowCurrentComponentViewEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isShowCurrentComponentViewEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void registerTopPackageChangeListener(github.tornaco.android.thanos.core.app.activity.ITopPackageChangeListener listener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(listener);
          boolean _status = mRemote.transact(Stub.TRANSACTION_registerTopPackageChangeListener, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void unRegisterTopPackageChangeListener(github.tornaco.android.thanos.core.app.activity.ITopPackageChangeListener listener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(listener);
          boolean _status = mRemote.transact(Stub.TRANSACTION_unRegisterTopPackageChangeListener, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isVerifyOnScreenOffEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isVerifyOnScreenOffEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setVerifyOnScreenOffEnabled(boolean enabled) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enabled)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setVerifyOnScreenOffEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isVerifyOnAppSwitchEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isVerifyOnAppSwitchEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setVerifyOnAppSwitchEnabled(boolean enabled) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enabled)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setVerifyOnAppSwitchEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isVerifyOnTaskRemovedEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isVerifyOnTaskRemovedEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setVerifyOnTaskRemovedEnabled(boolean enabled) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enabled)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setVerifyOnTaskRemovedEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      // Bridge API to report app events.
      @Override public android.content.Intent reportOnStartActivity(java.lang.String callingPackage, android.content.Intent intent) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.content.Intent _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(callingPackage);
          _Parcel.writeTypedObject(_data, intent, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_reportOnStartActivity, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, android.content.Intent.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void reportOnActivityStopped(android.os.IBinder token) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongBinder(token);
          boolean _status = mRemote.transact(Stub.TRANSACTION_reportOnActivityStopped, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void reportOnActivityResumed(android.os.IBinder token) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongBinder(token);
          boolean _status = mRemote.transact(Stub.TRANSACTION_reportOnActivityResumed, _data, _reply, 0);
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
      @Override public void registerActivityLifecycleListener(github.tornaco.android.thanos.core.app.activity.IActivityLifecycleListener listener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(listener);
          boolean _status = mRemote.transact(Stub.TRANSACTION_registerActivityLifecycleListener, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void unRegisterActivityLifecycleListener(github.tornaco.android.thanos.core.app.activity.IActivityLifecycleListener listener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(listener);
          boolean _status = mRemote.transact(Stub.TRANSACTION_unRegisterActivityLifecycleListener, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void addAppLockWhiteListComponents(java.util.List<android.content.ComponentName> componentName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedList(_data, componentName, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_addAppLockWhiteListComponents, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void removeAppLockWhiteListComponents(java.util.List<android.content.ComponentName> componentName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedList(_data, componentName, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_removeAppLockWhiteListComponents, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.util.List<android.content.ComponentName> getAppLockWhiteListComponents() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<android.content.ComponentName> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAppLockWhiteListComponents, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(android.content.ComponentName.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public int getLaunchOtherAppSetting(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getLaunchOtherAppSetting, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setLaunchOtherAppSetting(github.tornaco.android.thanos.core.pm.Pkg pkg, int setting) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          _data.writeInt(setting);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setLaunchOtherAppSetting, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isLaunchOtherAppBlockerEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isLaunchOtherAppBlockerEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setLaunchOtherAppBlockerEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setLaunchOtherAppBlockerEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void addLaunchOtherAppRule(java.lang.String rule) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(rule);
          boolean _status = mRemote.transact(Stub.TRANSACTION_addLaunchOtherAppRule, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void deleteLaunchOtherAppRule(java.lang.String rule) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(rule);
          boolean _status = mRemote.transact(Stub.TRANSACTION_deleteLaunchOtherAppRule, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.lang.String[] getAllLaunchOtherAppRules() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAllLaunchOtherAppRules, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createStringArray();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void removePkgFromLaunchOtherAppAllowList(github.tornaco.android.thanos.core.pm.Pkg pkg, github.tornaco.android.thanos.core.pm.Pkg pkgToRemove) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          _Parcel.writeTypedObject(_data, pkgToRemove, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_removePkgFromLaunchOtherAppAllowList, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void addPkgToLaunchOtherAppAllowList(github.tornaco.android.thanos.core.pm.Pkg pkg, github.tornaco.android.thanos.core.pm.Pkg pkgToAdd) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          _Parcel.writeTypedObject(_data, pkgToAdd, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_addPkgToLaunchOtherAppAllowList, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.pm.Pkg> getLaunchOtherAppAllowListOrNull(github.tornaco.android.thanos.core.pm.Pkg callerPkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.pm.Pkg> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, callerPkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getLaunchOtherAppAllowListOrNull, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public int getLockMethod() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getLockMethod, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setLockMethod(int method) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(method);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setLockMethod, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setLockPattern(java.lang.String pattern) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pattern);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setLockPattern, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.lang.String getLockPattern() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getLockPattern, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readString();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
    }
    static final int TRANSACTION_checkActivity = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_replaceActivityStartingIntent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_shouldVerifyActivityStarting = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_getCurrentFrontApp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_setAppLockEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_isAppLockEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_isPackageLocked = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_setPackageLocked = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    static final int TRANSACTION_setVerifyResult = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
    static final int TRANSACTION_addComponentReplacement = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
    static final int TRANSACTION_removeComponentReplacement = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
    static final int TRANSACTION_getComponentReplacements = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
    static final int TRANSACTION_setActivityTrampolineEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
    static final int TRANSACTION_isActivityTrampolineEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
    static final int TRANSACTION_setShowCurrentComponentViewEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
    static final int TRANSACTION_isShowCurrentComponentViewEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
    static final int TRANSACTION_registerTopPackageChangeListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
    static final int TRANSACTION_unRegisterTopPackageChangeListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
    static final int TRANSACTION_isVerifyOnScreenOffEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
    static final int TRANSACTION_setVerifyOnScreenOffEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
    static final int TRANSACTION_isVerifyOnAppSwitchEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
    static final int TRANSACTION_setVerifyOnAppSwitchEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 21);
    static final int TRANSACTION_isVerifyOnTaskRemovedEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 22);
    static final int TRANSACTION_setVerifyOnTaskRemovedEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 23);
    static final int TRANSACTION_reportOnStartActivity = (android.os.IBinder.FIRST_CALL_TRANSACTION + 24);
    static final int TRANSACTION_reportOnActivityStopped = (android.os.IBinder.FIRST_CALL_TRANSACTION + 25);
    static final int TRANSACTION_reportOnActivityResumed = (android.os.IBinder.FIRST_CALL_TRANSACTION + 26);
    static final int TRANSACTION_dump = (android.os.IBinder.FIRST_CALL_TRANSACTION + 27);
    static final int TRANSACTION_registerActivityLifecycleListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 28);
    static final int TRANSACTION_unRegisterActivityLifecycleListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 29);
    static final int TRANSACTION_addAppLockWhiteListComponents = (android.os.IBinder.FIRST_CALL_TRANSACTION + 30);
    static final int TRANSACTION_removeAppLockWhiteListComponents = (android.os.IBinder.FIRST_CALL_TRANSACTION + 31);
    static final int TRANSACTION_getAppLockWhiteListComponents = (android.os.IBinder.FIRST_CALL_TRANSACTION + 32);
    static final int TRANSACTION_getLaunchOtherAppSetting = (android.os.IBinder.FIRST_CALL_TRANSACTION + 33);
    static final int TRANSACTION_setLaunchOtherAppSetting = (android.os.IBinder.FIRST_CALL_TRANSACTION + 34);
    static final int TRANSACTION_isLaunchOtherAppBlockerEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 35);
    static final int TRANSACTION_setLaunchOtherAppBlockerEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 36);
    static final int TRANSACTION_addLaunchOtherAppRule = (android.os.IBinder.FIRST_CALL_TRANSACTION + 37);
    static final int TRANSACTION_deleteLaunchOtherAppRule = (android.os.IBinder.FIRST_CALL_TRANSACTION + 38);
    static final int TRANSACTION_getAllLaunchOtherAppRules = (android.os.IBinder.FIRST_CALL_TRANSACTION + 39);
    static final int TRANSACTION_removePkgFromLaunchOtherAppAllowList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 40);
    static final int TRANSACTION_addPkgToLaunchOtherAppAllowList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 41);
    static final int TRANSACTION_getLaunchOtherAppAllowListOrNull = (android.os.IBinder.FIRST_CALL_TRANSACTION + 42);
    static final int TRANSACTION_getLockMethod = (android.os.IBinder.FIRST_CALL_TRANSACTION + 43);
    static final int TRANSACTION_setLockMethod = (android.os.IBinder.FIRST_CALL_TRANSACTION + 44);
    static final int TRANSACTION_setLockPattern = (android.os.IBinder.FIRST_CALL_TRANSACTION + 45);
    static final int TRANSACTION_getLockPattern = (android.os.IBinder.FIRST_CALL_TRANSACTION + 46);
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor";
  /** @deprecated use {@link #replaceActivityStartingIntent} instead */
  @Deprecated
  public boolean checkActivity(android.content.ComponentName componentName, int userId, android.os.IBinder resultTo) throws android.os.RemoteException;
  public android.content.Intent replaceActivityStartingIntent(android.content.Intent intent, int userId, android.os.IBinder resultTo, java.lang.String callingPkgName) throws android.os.RemoteException;
  public boolean shouldVerifyActivityStarting(android.content.ComponentName componentName, java.lang.String pkg, java.lang.String source) throws android.os.RemoteException;
  public java.lang.String getCurrentFrontApp() throws android.os.RemoteException;
  public void setAppLockEnabled(boolean enabled) throws android.os.RemoteException;
  public boolean isAppLockEnabled() throws android.os.RemoteException;
  public boolean isPackageLocked(java.lang.String pkg) throws android.os.RemoteException;
  public void setPackageLocked(java.lang.String pkg, boolean locked) throws android.os.RemoteException;
  public void setVerifyResult(int request, int result, int reason) throws android.os.RemoteException;
  public void addComponentReplacement(github.tornaco.android.thanos.core.app.component.ComponentReplacement replacement) throws android.os.RemoteException;
  public void removeComponentReplacement(github.tornaco.android.thanos.core.app.component.ComponentReplacement replacement) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.app.component.ComponentReplacement> getComponentReplacements() throws android.os.RemoteException;
  public void setActivityTrampolineEnabled(boolean enabled) throws android.os.RemoteException;
  public boolean isActivityTrampolineEnabled() throws android.os.RemoteException;
  public void setShowCurrentComponentViewEnabled(boolean enabled) throws android.os.RemoteException;
  public boolean isShowCurrentComponentViewEnabled() throws android.os.RemoteException;
  public void registerTopPackageChangeListener(github.tornaco.android.thanos.core.app.activity.ITopPackageChangeListener listener) throws android.os.RemoteException;
  public void unRegisterTopPackageChangeListener(github.tornaco.android.thanos.core.app.activity.ITopPackageChangeListener listener) throws android.os.RemoteException;
  public boolean isVerifyOnScreenOffEnabled() throws android.os.RemoteException;
  public void setVerifyOnScreenOffEnabled(boolean enabled) throws android.os.RemoteException;
  public boolean isVerifyOnAppSwitchEnabled() throws android.os.RemoteException;
  public void setVerifyOnAppSwitchEnabled(boolean enabled) throws android.os.RemoteException;
  public boolean isVerifyOnTaskRemovedEnabled() throws android.os.RemoteException;
  public void setVerifyOnTaskRemovedEnabled(boolean enabled) throws android.os.RemoteException;
  // Bridge API to report app events.
  public android.content.Intent reportOnStartActivity(java.lang.String callingPackage, android.content.Intent intent) throws android.os.RemoteException;
  public void reportOnActivityStopped(android.os.IBinder token) throws android.os.RemoteException;
  public void reportOnActivityResumed(android.os.IBinder token) throws android.os.RemoteException;
  public void dump(github.tornaco.android.thanos.core.IPrinter p) throws android.os.RemoteException;
  public void registerActivityLifecycleListener(github.tornaco.android.thanos.core.app.activity.IActivityLifecycleListener listener) throws android.os.RemoteException;
  public void unRegisterActivityLifecycleListener(github.tornaco.android.thanos.core.app.activity.IActivityLifecycleListener listener) throws android.os.RemoteException;
  public void addAppLockWhiteListComponents(java.util.List<android.content.ComponentName> componentName) throws android.os.RemoteException;
  public void removeAppLockWhiteListComponents(java.util.List<android.content.ComponentName> componentName) throws android.os.RemoteException;
  public java.util.List<android.content.ComponentName> getAppLockWhiteListComponents() throws android.os.RemoteException;
  public int getLaunchOtherAppSetting(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  public void setLaunchOtherAppSetting(github.tornaco.android.thanos.core.pm.Pkg pkg, int setting) throws android.os.RemoteException;
  public boolean isLaunchOtherAppBlockerEnabled() throws android.os.RemoteException;
  public void setLaunchOtherAppBlockerEnabled(boolean enable) throws android.os.RemoteException;
  public void addLaunchOtherAppRule(java.lang.String rule) throws android.os.RemoteException;
  public void deleteLaunchOtherAppRule(java.lang.String rule) throws android.os.RemoteException;
  public java.lang.String[] getAllLaunchOtherAppRules() throws android.os.RemoteException;
  public void removePkgFromLaunchOtherAppAllowList(github.tornaco.android.thanos.core.pm.Pkg pkg, github.tornaco.android.thanos.core.pm.Pkg pkgToRemove) throws android.os.RemoteException;
  public void addPkgToLaunchOtherAppAllowList(github.tornaco.android.thanos.core.pm.Pkg pkg, github.tornaco.android.thanos.core.pm.Pkg pkgToAdd) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.pm.Pkg> getLaunchOtherAppAllowListOrNull(github.tornaco.android.thanos.core.pm.Pkg callerPkg) throws android.os.RemoteException;
  public int getLockMethod() throws android.os.RemoteException;
  public void setLockMethod(int method) throws android.os.RemoteException;
  public void setLockPattern(java.lang.String pattern) throws android.os.RemoteException;
  public java.lang.String getLockPattern() throws android.os.RemoteException;
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
