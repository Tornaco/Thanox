/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /Users/haoguo/Library/Android/sdk/build-tools/35.0.0/aidl -I/Users/haoguo/Documents/workspace/Thanox/android/android_framework/base/src/main/java -p/Users/haoguo/Documents/workspace/Thanox/android/android_sdk/framework.aidl -p/Users/haoguo/Documents/workspace/Thanox/android/android_sdk/thanos.aidl /Users/haoguo/Documents/workspace/Thanox/android/android_framework/base/src/main/java/github/tornaco/android/thanos/core/plus/ICallback.aidl
 */
package github.tornaco.android.thanos.core.plus;
public interface ICallback extends android.os.IInterface
{
  /** Default implementation for ICallback. */
  public static class Default implements github.tornaco.android.thanos.core.plus.ICallback
  {
    @Override public void onRes(github.tornaco.android.thanos.core.plus.RR res) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.plus.ICallback
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.plus.ICallback interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.plus.ICallback asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.plus.ICallback))) {
        return ((github.tornaco.android.thanos.core.plus.ICallback)iin);
      }
      return new github.tornaco.android.thanos.core.plus.ICallback.Stub.Proxy(obj);
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
        case TRANSACTION_onRes:
        {
          github.tornaco.android.thanos.core.plus.RR _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.plus.RR.CREATOR);
          this.onRes(_arg0);
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static class Proxy implements github.tornaco.android.thanos.core.plus.ICallback
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
      @Override public void onRes(github.tornaco.android.thanos.core.plus.RR res) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, res, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onRes, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
    }
    static final int TRANSACTION_onRes = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.plus.ICallback";
  public void onRes(github.tornaco.android.thanos.core.plus.RR res) throws android.os.RemoteException;
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
