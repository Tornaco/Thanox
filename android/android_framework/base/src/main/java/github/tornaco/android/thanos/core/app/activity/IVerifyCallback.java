/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.app.activity;
public interface IVerifyCallback extends android.os.IInterface
{
  /** Default implementation for IVerifyCallback. */
  public static class Default implements github.tornaco.android.thanos.core.app.activity.IVerifyCallback
  {
    @Override public void onVerifyResult(int verifyResult, int reason) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.app.activity.IVerifyCallback
  {
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.app.activity.IVerifyCallback interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.app.activity.IVerifyCallback asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.app.activity.IVerifyCallback))) {
        return ((github.tornaco.android.thanos.core.app.activity.IVerifyCallback)iin);
      }
      return new github.tornaco.android.thanos.core.app.activity.IVerifyCallback.Stub.Proxy(obj);
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
      }
      switch (code)
      {
        case TRANSACTION_onVerifyResult:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          int _arg1;
          _arg1 = data.readInt();
          this.onVerifyResult(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements github.tornaco.android.thanos.core.app.activity.IVerifyCallback
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
      @Override public void onVerifyResult(int verifyResult, int reason) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(verifyResult);
          _data.writeInt(reason);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onVerifyResult, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              getDefaultImpl().onVerifyResult(verifyResult, reason);
              return;
            }
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      public static github.tornaco.android.thanos.core.app.activity.IVerifyCallback sDefaultImpl;
    }
    static final int TRANSACTION_onVerifyResult = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    public static boolean setDefaultImpl(github.tornaco.android.thanos.core.app.activity.IVerifyCallback impl) {
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
    public static github.tornaco.android.thanos.core.app.activity.IVerifyCallback getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.app.activity.IVerifyCallback";
  public void onVerifyResult(int verifyResult, int reason) throws android.os.RemoteException;
}
