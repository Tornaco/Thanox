/*
 * This file is auto-generated.  DO NOT MODIFY.
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
    private static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.plus.IRS";
    /** Construct the stub at attach it to the interface. */
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
      switch (code)
      {
        case INTERFACE_TRANSACTION:
        {
          reply.writeString(descriptor);
          return true;
        }
        case TRANSACTION_bc:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          github.tornaco.android.thanos.core.plus.ICallback _arg2;
          _arg2 = github.tornaco.android.thanos.core.plus.ICallback.Stub.asInterface(data.readStrongBinder());
          this.bc(_arg0, _arg1, _arg2);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_vb:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          github.tornaco.android.thanos.core.plus.ICallback _arg2;
          _arg2 = github.tornaco.android.thanos.core.plus.ICallback.Stub.asInterface(data.readStrongBinder());
          this.vb(_arg0, _arg1, _arg2);
          reply.writeNoException();
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
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
          _data.writeStrongBinder((((cb!=null))?(cb.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_bc, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().bc(code, deviceId, cb);
            return;
          }
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
          _data.writeStrongBinder((((cb!=null))?(cb.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_vb, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().vb(code, deviceId, cb);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      public static github.tornaco.android.thanos.core.plus.IRS sDefaultImpl;
    }
    static final int TRANSACTION_bc = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_vb = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    public static boolean setDefaultImpl(github.tornaco.android.thanos.core.plus.IRS impl) {
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
    public static github.tornaco.android.thanos.core.plus.IRS getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  // Bind code.

  public void bc(java.lang.String code, java.lang.String deviceId, github.tornaco.android.thanos.core.plus.ICallback cb) throws android.os.RemoteException;
  // Verify binding.

  public void vb(java.lang.String code, java.lang.String deviceId, github.tornaco.android.thanos.core.plus.ICallback cb) throws android.os.RemoteException;
}
