/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /Users/haoguo/Library/Android/sdk/build-tools/35.0.0/aidl -I/Users/haoguo/Documents/workspace/Thanox/android/android_framework/base/src/main/java -p/Users/haoguo/Documents/workspace/Thanox/android/android_sdk/framework.aidl -p/Users/haoguo/Documents/workspace/Thanox/android/android_sdk/thanos.aidl /Users/haoguo/Documents/workspace/Thanox/android/android_framework/base/src/main/java/github/tornaco/android/thanos/core/app/activity/ITopPackageChangeListener.aidl
 */
package github.tornaco.android.thanos.core.app.activity;
public interface ITopPackageChangeListener extends android.os.IInterface
{
  /** Default implementation for ITopPackageChangeListener. */
  public static class Default implements github.tornaco.android.thanos.core.app.activity.ITopPackageChangeListener
  {
    @Override public void onChange(github.tornaco.android.thanos.core.pm.Pkg from, github.tornaco.android.thanos.core.pm.Pkg to) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.app.activity.ITopPackageChangeListener
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.app.activity.ITopPackageChangeListener interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.app.activity.ITopPackageChangeListener asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.app.activity.ITopPackageChangeListener))) {
        return ((github.tornaco.android.thanos.core.app.activity.ITopPackageChangeListener)iin);
      }
      return new github.tornaco.android.thanos.core.app.activity.ITopPackageChangeListener.Stub.Proxy(obj);
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
        case TRANSACTION_onChange:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          github.tornaco.android.thanos.core.pm.Pkg _arg1;
          _arg1 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          this.onChange(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static class Proxy implements github.tornaco.android.thanos.core.app.activity.ITopPackageChangeListener
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
      @Override public void onChange(github.tornaco.android.thanos.core.pm.Pkg from, github.tornaco.android.thanos.core.pm.Pkg to) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, from, 0);
          _Parcel.writeTypedObject(_data, to, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onChange, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
    }
    static final int TRANSACTION_onChange = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.app.activity.ITopPackageChangeListener";
  public void onChange(github.tornaco.android.thanos.core.pm.Pkg from, github.tornaco.android.thanos.core.pm.Pkg to) throws android.os.RemoteException;
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
