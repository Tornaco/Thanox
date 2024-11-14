/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /home/tornaco/Android/Sdk/build-tools/35.0.0/aidl -I/home/tornaco/Documents/Thanox/android/android_framework/base/src/main/java -p/home/tornaco/Documents/Thanox/android/android_sdk/framework.aidl -p/home/tornaco/Documents/Thanox/android/android_sdk/thanos.aidl /home/tornaco/Documents/Thanox/android/android_framework/base/src/main/java/github/tornaco/android/thanos/core/secure/ops/IAppOpsService.aidl
 */
package github.tornaco.android.thanos.core.secure.ops;
public interface IAppOpsService extends android.os.IInterface
{
  /** Default implementation for IAppOpsService. */
  public static class Default implements github.tornaco.android.thanos.core.secure.ops.IAppOpsService
  {
    @Override public void setMode(int code, int uid, java.lang.String packageName, int mode) throws android.os.RemoteException
    {
    }
    @Override public void resetAllModes(java.lang.String reqPackageName) throws android.os.RemoteException
    {
    }
    @Override public int checkOperation(int code, int uid, java.lang.String packageName) throws android.os.RemoteException
    {
      return 0;
    }
    @Override public boolean isOpsEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setOpsEnabled(boolean enabled) throws android.os.RemoteException
    {
    }
    @Override public void onStartOp(android.os.IBinder token, int code, int uid, java.lang.String packageName) throws android.os.RemoteException
    {
    }
    @Override public void onFinishOp(android.os.IBinder token, int code, int uid, java.lang.String packageName) throws android.os.RemoteException
    {
    }
    @Override public void setOpRemindEnable(int code, boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isOpRemindEnabled(int code) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setPkgOpRemindEnable(java.lang.String pkg, boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isPkgOpRemindEnable(java.lang.String pkg) throws android.os.RemoteException
    {
      return false;
    }
    @Override public int checkOperationNonCheck(int code, int uid, java.lang.String packageName) throws android.os.RemoteException
    {
      return 0;
    }
    @Override public void dump(github.tornaco.android.thanos.core.IPrinter p) throws android.os.RemoteException
    {
    }
    @Override public void onSettingsGetString(java.lang.String name, java.lang.String value, java.lang.String callerPackageName) throws android.os.RemoteException
    {
    }
    @Override public void onSettingsPutString(java.lang.String name, java.lang.String value, java.lang.String callerPackageName) throws android.os.RemoteException
    {
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.secure.ops.SettingsAccessRecord> getSettingsReadRecords(java.lang.String filterCallerPackageName) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.secure.ops.SettingsAccessRecord> getSettingsWriteRecords(java.lang.String filterCallerPackageName) throws android.os.RemoteException
    {
      return null;
    }
    @Override public void clearSettingsReadRecords() throws android.os.RemoteException
    {
    }
    @Override public void clearSettingsWriteRecords() throws android.os.RemoteException
    {
    }
    @Override public void setSettingsRecordEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isSettingsRecordEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.secure.ops.IAppOpsService
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.secure.ops.IAppOpsService interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.secure.ops.IAppOpsService asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.secure.ops.IAppOpsService))) {
        return ((github.tornaco.android.thanos.core.secure.ops.IAppOpsService)iin);
      }
      return new github.tornaco.android.thanos.core.secure.ops.IAppOpsService.Stub.Proxy(obj);
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
        case TRANSACTION_setMode:
        {
          int _arg0;
          _arg0 = data.readInt();
          int _arg1;
          _arg1 = data.readInt();
          java.lang.String _arg2;
          _arg2 = data.readString();
          int _arg3;
          _arg3 = data.readInt();
          this.setMode(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_resetAllModes:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.resetAllModes(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_checkOperation:
        {
          int _arg0;
          _arg0 = data.readInt();
          int _arg1;
          _arg1 = data.readInt();
          java.lang.String _arg2;
          _arg2 = data.readString();
          int _result = this.checkOperation(_arg0, _arg1, _arg2);
          reply.writeNoException();
          reply.writeInt(_result);
          break;
        }
        case TRANSACTION_isOpsEnabled:
        {
          boolean _result = this.isOpsEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setOpsEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setOpsEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_onStartOp:
        {
          android.os.IBinder _arg0;
          _arg0 = data.readStrongBinder();
          int _arg1;
          _arg1 = data.readInt();
          int _arg2;
          _arg2 = data.readInt();
          java.lang.String _arg3;
          _arg3 = data.readString();
          this.onStartOp(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_onFinishOp:
        {
          android.os.IBinder _arg0;
          _arg0 = data.readStrongBinder();
          int _arg1;
          _arg1 = data.readInt();
          int _arg2;
          _arg2 = data.readInt();
          java.lang.String _arg3;
          _arg3 = data.readString();
          this.onFinishOp(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_setOpRemindEnable:
        {
          int _arg0;
          _arg0 = data.readInt();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setOpRemindEnable(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isOpRemindEnabled:
        {
          int _arg0;
          _arg0 = data.readInt();
          boolean _result = this.isOpRemindEnabled(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setPkgOpRemindEnable:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setPkgOpRemindEnable(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isPkgOpRemindEnable:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.isPkgOpRemindEnable(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_checkOperationNonCheck:
        {
          int _arg0;
          _arg0 = data.readInt();
          int _arg1;
          _arg1 = data.readInt();
          java.lang.String _arg2;
          _arg2 = data.readString();
          int _result = this.checkOperationNonCheck(_arg0, _arg1, _arg2);
          reply.writeNoException();
          reply.writeInt(_result);
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
        case TRANSACTION_onSettingsGetString:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.lang.String _arg2;
          _arg2 = data.readString();
          this.onSettingsGetString(_arg0, _arg1, _arg2);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_onSettingsPutString:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.lang.String _arg2;
          _arg2 = data.readString();
          this.onSettingsPutString(_arg0, _arg1, _arg2);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getSettingsReadRecords:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.util.List<github.tornaco.android.thanos.core.secure.ops.SettingsAccessRecord> _result = this.getSettingsReadRecords(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getSettingsWriteRecords:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.util.List<github.tornaco.android.thanos.core.secure.ops.SettingsAccessRecord> _result = this.getSettingsWriteRecords(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_clearSettingsReadRecords:
        {
          this.clearSettingsReadRecords();
          reply.writeNoException();
          break;
        }
        case TRANSACTION_clearSettingsWriteRecords:
        {
          this.clearSettingsWriteRecords();
          reply.writeNoException();
          break;
        }
        case TRANSACTION_setSettingsRecordEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setSettingsRecordEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isSettingsRecordEnabled:
        {
          boolean _result = this.isSettingsRecordEnabled();
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
    private static class Proxy implements github.tornaco.android.thanos.core.secure.ops.IAppOpsService
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
      @Override public void setMode(int code, int uid, java.lang.String packageName, int mode) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(code);
          _data.writeInt(uid);
          _data.writeString(packageName);
          _data.writeInt(mode);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setMode, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void resetAllModes(java.lang.String reqPackageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(reqPackageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_resetAllModes, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public int checkOperation(int code, int uid, java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(code);
          _data.writeInt(uid);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_checkOperation, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean isOpsEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isOpsEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setOpsEnabled(boolean enabled) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enabled)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setOpsEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void onStartOp(android.os.IBinder token, int code, int uid, java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongBinder(token);
          _data.writeInt(code);
          _data.writeInt(uid);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onStartOp, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void onFinishOp(android.os.IBinder token, int code, int uid, java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongBinder(token);
          _data.writeInt(code);
          _data.writeInt(uid);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onFinishOp, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setOpRemindEnable(int code, boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(code);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setOpRemindEnable, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isOpRemindEnabled(int code) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(code);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isOpRemindEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setPkgOpRemindEnable(java.lang.String pkg, boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPkgOpRemindEnable, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isPkgOpRemindEnable(java.lang.String pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPkgOpRemindEnable, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public int checkOperationNonCheck(int code, int uid, java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(code);
          _data.writeInt(uid);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_checkOperationNonCheck, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
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
      @Override public void onSettingsGetString(java.lang.String name, java.lang.String value, java.lang.String callerPackageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(name);
          _data.writeString(value);
          _data.writeString(callerPackageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onSettingsGetString, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void onSettingsPutString(java.lang.String name, java.lang.String value, java.lang.String callerPackageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(name);
          _data.writeString(value);
          _data.writeString(callerPackageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onSettingsPutString, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.secure.ops.SettingsAccessRecord> getSettingsReadRecords(java.lang.String filterCallerPackageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.secure.ops.SettingsAccessRecord> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(filterCallerPackageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getSettingsReadRecords, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.secure.ops.SettingsAccessRecord.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.secure.ops.SettingsAccessRecord> getSettingsWriteRecords(java.lang.String filterCallerPackageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.secure.ops.SettingsAccessRecord> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(filterCallerPackageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getSettingsWriteRecords, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.secure.ops.SettingsAccessRecord.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void clearSettingsReadRecords() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_clearSettingsReadRecords, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void clearSettingsWriteRecords() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_clearSettingsWriteRecords, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setSettingsRecordEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setSettingsRecordEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isSettingsRecordEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isSettingsRecordEnabled, _data, _reply, 0);
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
    static final int TRANSACTION_setMode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_resetAllModes = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_checkOperation = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_isOpsEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_setOpsEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_onStartOp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_onFinishOp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_setOpRemindEnable = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    static final int TRANSACTION_isOpRemindEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
    static final int TRANSACTION_setPkgOpRemindEnable = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
    static final int TRANSACTION_isPkgOpRemindEnable = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
    static final int TRANSACTION_checkOperationNonCheck = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
    static final int TRANSACTION_dump = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
    static final int TRANSACTION_onSettingsGetString = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
    static final int TRANSACTION_onSettingsPutString = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
    static final int TRANSACTION_getSettingsReadRecords = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
    static final int TRANSACTION_getSettingsWriteRecords = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
    static final int TRANSACTION_clearSettingsReadRecords = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
    static final int TRANSACTION_clearSettingsWriteRecords = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
    static final int TRANSACTION_setSettingsRecordEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
    static final int TRANSACTION_isSettingsRecordEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.secure.ops.IAppOpsService";
  public void setMode(int code, int uid, java.lang.String packageName, int mode) throws android.os.RemoteException;
  public void resetAllModes(java.lang.String reqPackageName) throws android.os.RemoteException;
  public int checkOperation(int code, int uid, java.lang.String packageName) throws android.os.RemoteException;
  public boolean isOpsEnabled() throws android.os.RemoteException;
  public void setOpsEnabled(boolean enabled) throws android.os.RemoteException;
  public void onStartOp(android.os.IBinder token, int code, int uid, java.lang.String packageName) throws android.os.RemoteException;
  public void onFinishOp(android.os.IBinder token, int code, int uid, java.lang.String packageName) throws android.os.RemoteException;
  public void setOpRemindEnable(int code, boolean enable) throws android.os.RemoteException;
  public boolean isOpRemindEnabled(int code) throws android.os.RemoteException;
  public void setPkgOpRemindEnable(java.lang.String pkg, boolean enable) throws android.os.RemoteException;
  public boolean isPkgOpRemindEnable(java.lang.String pkg) throws android.os.RemoteException;
  public int checkOperationNonCheck(int code, int uid, java.lang.String packageName) throws android.os.RemoteException;
  public void dump(github.tornaco.android.thanos.core.IPrinter p) throws android.os.RemoteException;
  public void onSettingsGetString(java.lang.String name, java.lang.String value, java.lang.String callerPackageName) throws android.os.RemoteException;
  public void onSettingsPutString(java.lang.String name, java.lang.String value, java.lang.String callerPackageName) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.secure.ops.SettingsAccessRecord> getSettingsReadRecords(java.lang.String filterCallerPackageName) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.secure.ops.SettingsAccessRecord> getSettingsWriteRecords(java.lang.String filterCallerPackageName) throws android.os.RemoteException;
  public void clearSettingsReadRecords() throws android.os.RemoteException;
  public void clearSettingsWriteRecords() throws android.os.RemoteException;
  public void setSettingsRecordEnabled(boolean enable) throws android.os.RemoteException;
  public boolean isSettingsRecordEnabled() throws android.os.RemoteException;
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
