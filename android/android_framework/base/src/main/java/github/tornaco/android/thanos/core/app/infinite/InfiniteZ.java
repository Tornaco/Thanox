/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.app.infinite;
public interface InfiniteZ extends android.os.IInterface
{
  /** Default implementation for InfiniteZ. */
  public static class Default implements github.tornaco.android.thanos.core.app.infinite.InfiniteZ
  {
    @Override public void setEnabled(boolean enable, github.tornaco.android.thanos.core.app.infinite.IEnableCallback callback) throws android.os.RemoteException
    {
    }
    @Override public boolean isEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void addPackage(java.lang.String pkgName, github.tornaco.android.thanos.core.app.infinite.IAddPackageCallback callback) throws android.os.RemoteException
    {
    }
    @Override public void removePackage(java.lang.String pkgName, github.tornaco.android.thanos.core.app.infinite.IRemovePackageCallback callback) throws android.os.RemoteException
    {
    }
    @Override public void launchPackage(java.lang.String pkgName, github.tornaco.android.thanos.core.app.infinite.ILaunchPackageCallback callback) throws android.os.RemoteException
    {
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.pm.AppInfo> getInstalledPackages() throws android.os.RemoteException
    {
      return null;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.app.infinite.InfiniteZ
  {
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.app.infinite.InfiniteZ interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.app.infinite.InfiniteZ asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.app.infinite.InfiniteZ))) {
        return ((github.tornaco.android.thanos.core.app.infinite.InfiniteZ)iin);
      }
      return new github.tornaco.android.thanos.core.app.infinite.InfiniteZ.Stub.Proxy(obj);
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
        case TRANSACTION_setEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          github.tornaco.android.thanos.core.app.infinite.IEnableCallback _arg1;
          _arg1 = github.tornaco.android.thanos.core.app.infinite.IEnableCallback.Stub.asInterface(data.readStrongBinder());
          this.setEnabled(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isEnabled:
        {
          boolean _result = this.isEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_addPackage:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          github.tornaco.android.thanos.core.app.infinite.IAddPackageCallback _arg1;
          _arg1 = github.tornaco.android.thanos.core.app.infinite.IAddPackageCallback.Stub.asInterface(data.readStrongBinder());
          this.addPackage(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_removePackage:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          github.tornaco.android.thanos.core.app.infinite.IRemovePackageCallback _arg1;
          _arg1 = github.tornaco.android.thanos.core.app.infinite.IRemovePackageCallback.Stub.asInterface(data.readStrongBinder());
          this.removePackage(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_launchPackage:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          github.tornaco.android.thanos.core.app.infinite.ILaunchPackageCallback _arg1;
          _arg1 = github.tornaco.android.thanos.core.app.infinite.ILaunchPackageCallback.Stub.asInterface(data.readStrongBinder());
          this.launchPackage(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getInstalledPackages:
        {
          java.util.List<github.tornaco.android.thanos.core.pm.AppInfo> _result = this.getInstalledPackages();
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static class Proxy implements github.tornaco.android.thanos.core.app.infinite.InfiniteZ
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
      @Override public void setEnabled(boolean enable, github.tornaco.android.thanos.core.app.infinite.IEnableCallback callback) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          _data.writeStrongInterface(callback);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void addPackage(java.lang.String pkgName, github.tornaco.android.thanos.core.app.infinite.IAddPackageCallback callback) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          _data.writeStrongInterface(callback);
          boolean _status = mRemote.transact(Stub.TRANSACTION_addPackage, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void removePackage(java.lang.String pkgName, github.tornaco.android.thanos.core.app.infinite.IRemovePackageCallback callback) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          _data.writeStrongInterface(callback);
          boolean _status = mRemote.transact(Stub.TRANSACTION_removePackage, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void launchPackage(java.lang.String pkgName, github.tornaco.android.thanos.core.app.infinite.ILaunchPackageCallback callback) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          _data.writeStrongInterface(callback);
          boolean _status = mRemote.transact(Stub.TRANSACTION_launchPackage, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.pm.AppInfo> getInstalledPackages() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.pm.AppInfo> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getInstalledPackages, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.pm.AppInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
    }
    static final int TRANSACTION_setEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_isEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_addPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_removePackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_launchPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_getInstalledPackages = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
  }
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.app.infinite.InfiniteZ";
  public void setEnabled(boolean enable, github.tornaco.android.thanos.core.app.infinite.IEnableCallback callback) throws android.os.RemoteException;
  public boolean isEnabled() throws android.os.RemoteException;
  public void addPackage(java.lang.String pkgName, github.tornaco.android.thanos.core.app.infinite.IAddPackageCallback callback) throws android.os.RemoteException;
  public void removePackage(java.lang.String pkgName, github.tornaco.android.thanos.core.app.infinite.IRemovePackageCallback callback) throws android.os.RemoteException;
  public void launchPackage(java.lang.String pkgName, github.tornaco.android.thanos.core.app.infinite.ILaunchPackageCallback callback) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.pm.AppInfo> getInstalledPackages() throws android.os.RemoteException;
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
