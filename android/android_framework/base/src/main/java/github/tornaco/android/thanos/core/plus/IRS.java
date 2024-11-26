/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /home/tornaco/Android/Sdk/build-tools/35.0.0/aidl -I/home/tornaco/Documents/Thanox/android/android_framework/base/src/main/java -p/home/tornaco/Documents/Thanox/android/android_sdk/framework.aidl -p/home/tornaco/Documents/Thanox/android/android_sdk/thanos.aidl /home/tornaco/Documents/Thanox/android/android_framework/base/src/main/java/github/tornaco/android/thanos/core/plus/IRS.aidl
 */
package github.tornaco.android.thanos.core.plus;
// Remote Server
public interface IRS extends android.os.IInterface
{
  /** Default implementation for IRS. */
  public static class Default implements github.tornaco.android.thanos.core.plus.IRS
  {
    // Bind code.
    @Override public void bc(java.lang.String code, java.lang.String deviceId, github.tornaco.android.thanos.core.plus.ICallback cb) throws android.os.RemoteException
    {
    }
    // Verify binding.
    @Override public void vb(java.lang.String code, java.lang.String deviceId, github.tornaco.android.thanos.core.plus.ICallback cb) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.plus.IRS
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.plus.IRS interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.plus.IRS asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.plus.IRS))) {
        return ((github.tornaco.android.thanos.core.plus.IRS)iin);
      }
      return new github.tornaco.android.thanos.core.plus.IRS.Stub.Proxy(obj);
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
        case TRANSACTION_bc:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          github.tornaco.android.thanos.core.plus.ICallback _arg2;
          _arg2 = github.tornaco.android.thanos.core.plus.ICallback.Stub.asInterface(data.readStrongBinder());
          this.bc(_arg0, _arg1, _arg2);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_vb:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          github.tornaco.android.thanos.core.plus.ICallback _arg2;
          _arg2 = github.tornaco.android.thanos.core.plus.ICallback.Stub.asInterface(data.readStrongBinder());
          this.vb(_arg0, _arg1, _arg2);
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
    private static class Proxy implements github.tornaco.android.thanos.core.plus.IRS
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
      // Bind code.
      @Override public void bc(java.lang.String code, java.lang.String deviceId, github.tornaco.android.thanos.core.plus.ICallback cb) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(code);
          _data.writeString(deviceId);
          _data.writeStrongInterface(cb);
          boolean _status = mRemote.transact(Stub.TRANSACTION_bc, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      // Verify binding.
      @Override public void vb(java.lang.String code, java.lang.String deviceId, github.tornaco.android.thanos.core.plus.ICallback cb) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(code);
          _data.writeString(deviceId);
          _data.writeStrongInterface(cb);
          boolean _status = mRemote.transact(Stub.TRANSACTION_vb, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
    }
    static final int TRANSACTION_bc = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_vb = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.plus.IRS";
  // Bind code.
  public void bc(java.lang.String code, java.lang.String deviceId, github.tornaco.android.thanos.core.plus.ICallback cb) throws android.os.RemoteException;
  // Verify binding.
  public void vb(java.lang.String code, java.lang.String deviceId, github.tornaco.android.thanos.core.plus.ICallback cb) throws android.os.RemoteException;
}
