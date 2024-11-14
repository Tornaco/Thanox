/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.app.infinite;
public interface IAddPackageCallback extends android.os.IInterface
{
  /** Default implementation for IAddPackageCallback. */
  public static class Default implements github.tornaco.android.thanos.core.app.infinite.IAddPackageCallback
  {
    @Override public void onSuccess(int userId) throws android.os.RemoteException
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
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.app.infinite.IAddPackageCallback
  {
    private static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.app.infinite.IAddPackageCallback";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.app.infinite.IAddPackageCallback interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.app.infinite.IAddPackageCallback asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.app.infinite.IAddPackageCallback))) {
        return ((github.tornaco.android.thanos.core.app.infinite.IAddPackageCallback)iin);
      }
      return new github.tornaco.android.thanos.core.app.infinite.IAddPackageCallback.Stub.Proxy(obj);
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
        case TRANSACTION_onSuccess:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          this.onSuccess(_arg0);
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
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements github.tornaco.android.thanos.core.app.infinite.IAddPackageCallback
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
      @Override public void onSuccess(int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onSuccess, _data, null, android.os.IBinder.FLAG_ONEWAY);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().onSuccess(userId);
            return;
          }
        }
        finally {
          _data.recycle();
        }
      }
      @Override public void onError(java.lang.String errorMessage, int errorCode) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(errorMessage);
          _data.writeInt(errorCode);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onError, _data, null, android.os.IBinder.FLAG_ONEWAY);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().onError(errorMessage, errorCode);
            return;
          }
        }
        finally {
          _data.recycle();
        }
      }
      public static github.tornaco.android.thanos.core.app.infinite.IAddPackageCallback sDefaultImpl;
    }
    static final int TRANSACTION_onSuccess = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_onError = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    public static boolean setDefaultImpl(github.tornaco.android.thanos.core.app.infinite.IAddPackageCallback impl) {
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
    public static github.tornaco.android.thanos.core.app.infinite.IAddPackageCallback getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public void onSuccess(int userId) throws android.os.RemoteException;
  public void onError(java.lang.String errorMessage, int errorCode) throws android.os.RemoteException;
}
