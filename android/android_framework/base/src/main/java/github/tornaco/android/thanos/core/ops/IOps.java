/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /Users/haoguo/Library/Android/sdk/build-tools/35.0.0/aidl -I/Users/haoguo/Documents/workspace/Thanox/android/android_framework/base/src/main/java -p/Users/haoguo/Documents/workspace/Thanox/android/android_sdk/framework.aidl -p/Users/haoguo/Documents/workspace/Thanox/android/android_sdk/thanos.aidl /Users/haoguo/Documents/workspace/Thanox/android/android_framework/base/src/main/java/github/tornaco/android/thanos/core/ops/IOps.aidl
 */
package github.tornaco.android.thanos.core.ops;
public interface IOps extends android.os.IInterface
{
  /** Default implementation for IOps. */
  public static class Default implements github.tornaco.android.thanos.core.ops.IOps
  {
    @Override public void setMode(int code, github.tornaco.android.thanos.core.pm.Pkg pkg, java.lang.String permStateName) throws android.os.RemoteException
    {
    }
    @Override public github.tornaco.android.thanos.core.ops.PermInfo getPackagePermInfo(int code, github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.lang.String opToName(int code) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.lang.String opToPermission(int code) throws android.os.RemoteException
    {
      return null;
    }
    @Override public int getPermissionFlags(java.lang.String permName, github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
      return 0;
    }
    @Override public java.lang.String permissionFlagToString(int flag) throws android.os.RemoteException
    {
      return null;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.ops.IOps
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.ops.IOps interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.ops.IOps asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.ops.IOps))) {
        return ((github.tornaco.android.thanos.core.ops.IOps)iin);
      }
      return new github.tornaco.android.thanos.core.ops.IOps.Stub.Proxy(obj);
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
          github.tornaco.android.thanos.core.pm.Pkg _arg1;
          _arg1 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          java.lang.String _arg2;
          _arg2 = data.readString();
          this.setMode(_arg0, _arg1, _arg2);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getPackagePermInfo:
        {
          int _arg0;
          _arg0 = data.readInt();
          github.tornaco.android.thanos.core.pm.Pkg _arg1;
          _arg1 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          github.tornaco.android.thanos.core.ops.PermInfo _result = this.getPackagePermInfo(_arg0, _arg1);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_opToName:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _result = this.opToName(_arg0);
          reply.writeNoException();
          reply.writeString(_result);
          break;
        }
        case TRANSACTION_opToPermission:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _result = this.opToPermission(_arg0);
          reply.writeNoException();
          reply.writeString(_result);
          break;
        }
        case TRANSACTION_getPermissionFlags:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          github.tornaco.android.thanos.core.pm.Pkg _arg1;
          _arg1 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          int _result = this.getPermissionFlags(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(_result);
          break;
        }
        case TRANSACTION_permissionFlagToString:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _result = this.permissionFlagToString(_arg0);
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
    private static class Proxy implements github.tornaco.android.thanos.core.ops.IOps
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
      @Override public void setMode(int code, github.tornaco.android.thanos.core.pm.Pkg pkg, java.lang.String permStateName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(code);
          _Parcel.writeTypedObject(_data, pkg, 0);
          _data.writeString(permStateName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setMode, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public github.tornaco.android.thanos.core.ops.PermInfo getPackagePermInfo(int code, github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.ops.PermInfo _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(code);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getPackagePermInfo, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, github.tornaco.android.thanos.core.ops.PermInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.lang.String opToName(int code) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(code);
          boolean _status = mRemote.transact(Stub.TRANSACTION_opToName, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readString();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.lang.String opToPermission(int code) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(code);
          boolean _status = mRemote.transact(Stub.TRANSACTION_opToPermission, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readString();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public int getPermissionFlags(java.lang.String permName, github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(permName);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getPermissionFlags, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.lang.String permissionFlagToString(int flag) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(flag);
          boolean _status = mRemote.transact(Stub.TRANSACTION_permissionFlagToString, _data, _reply, 0);
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
    static final int TRANSACTION_setMode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_getPackagePermInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_opToName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_opToPermission = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_getPermissionFlags = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_permissionFlagToString = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.ops.IOps";
  public void setMode(int code, github.tornaco.android.thanos.core.pm.Pkg pkg, java.lang.String permStateName) throws android.os.RemoteException;
  public github.tornaco.android.thanos.core.ops.PermInfo getPackagePermInfo(int code, github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  public java.lang.String opToName(int code) throws android.os.RemoteException;
  public java.lang.String opToPermission(int code) throws android.os.RemoteException;
  public int getPermissionFlags(java.lang.String permName, github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  public java.lang.String permissionFlagToString(int flag) throws android.os.RemoteException;
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
  }
}
