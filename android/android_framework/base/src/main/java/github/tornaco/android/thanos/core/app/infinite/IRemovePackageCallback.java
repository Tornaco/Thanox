/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.app.infinite;
public interface IRemovePackageCallback extends android.os.IInterface
{
  /** Default implementation for IRemovePackageCallback. */
  public static class Default implements github.tornaco.android.thanos.core.app.infinite.IRemovePackageCallback
  {
    @Override public void onSuccess() throws android.os.RemoteException
    {
    }
    @Override public void onError(java.lang.String errorMessage, int errorCode) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.app.infinite.IRemovePackageCallback
  {
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.app.infinite.IRemovePackageCallback interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.app.infinite.IRemovePackageCallback asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.app.infinite.IRemovePackageCallback))) {
        return ((github.tornaco.android.thanos.core.app.infinite.IRemovePackageCallback)iin);
      }
      return new github.tornaco.android.thanos.core.app.infinite.IRemovePackageCallback.Stub.Proxy(obj);
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
        case TRANSACTION_onSuccess:
        {
          data.enforceInterface(descriptor);
          this.onSuccess();
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_onError:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          this.onError(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements github.tornaco.android.thanos.core.app.infinite.IRemovePackageCallback
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
      @Override public void onSuccess() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onSuccess, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              getDefaultImpl().onSuccess();
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
      @Override public void onError(java.lang.String errorMessage, int errorCode) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(errorMessage);
          _data.writeInt(errorCode);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onError, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              getDefaultImpl().onError(errorMessage, errorCode);
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
      public static github.tornaco.android.thanos.core.app.infinite.IRemovePackageCallback sDefaultImpl;
    }
    static final int TRANSACTION_onSuccess = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_onError = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    public static boolean setDefaultImpl(github.tornaco.android.thanos.core.app.infinite.IRemovePackageCallback impl) {
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
    public static github.tornaco.android.thanos.core.app.infinite.IRemovePackageCallback getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.app.infinite.IRemovePackageCallback";
  public void onSuccess() throws android.os.RemoteException;
  public void onError(java.lang.String errorMessage, int errorCode) throws android.os.RemoteException;
}
