/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.pm;
public interface IAddPluginCallback extends android.os.IInterface
{
  /** Default implementation for IAddPluginCallback. */
  public static class Default implements github.tornaco.android.thanos.core.pm.IAddPluginCallback
  {
    @Override public void onPluginAdd() throws android.os.RemoteException
    {
    }
    @Override public void onFail(java.lang.String message) throws android.os.RemoteException
    {
    }
    @Override public void onProgress(java.lang.String progressMessage) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.pm.IAddPluginCallback
  {
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.pm.IAddPluginCallback interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.pm.IAddPluginCallback asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.pm.IAddPluginCallback))) {
        return ((github.tornaco.android.thanos.core.pm.IAddPluginCallback)iin);
      }
      return new github.tornaco.android.thanos.core.pm.IAddPluginCallback.Stub.Proxy(obj);
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
        case TRANSACTION_onPluginAdd:
        {
          data.enforceInterface(descriptor);
          this.onPluginAdd();
          return true;
        }
        case TRANSACTION_onFail:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.onFail(_arg0);
          return true;
        }
        case TRANSACTION_onProgress:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.onProgress(_arg0);
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements github.tornaco.android.thanos.core.pm.IAddPluginCallback
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
      @Override public void onPluginAdd() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onPluginAdd, _data, null, android.os.IBinder.FLAG_ONEWAY);
          if (!_status) {
            if (getDefaultImpl() != null) {
              getDefaultImpl().onPluginAdd();
              return;
            }
          }
        }
        finally {
          _data.recycle();
        }
      }
      @Override public void onFail(java.lang.String message) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(message);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onFail, _data, null, android.os.IBinder.FLAG_ONEWAY);
          if (!_status) {
            if (getDefaultImpl() != null) {
              getDefaultImpl().onFail(message);
              return;
            }
          }
        }
        finally {
          _data.recycle();
        }
      }
      @Override public void onProgress(java.lang.String progressMessage) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(progressMessage);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onProgress, _data, null, android.os.IBinder.FLAG_ONEWAY);
          if (!_status) {
            if (getDefaultImpl() != null) {
              getDefaultImpl().onProgress(progressMessage);
              return;
            }
          }
        }
        finally {
          _data.recycle();
        }
      }
      public static github.tornaco.android.thanos.core.pm.IAddPluginCallback sDefaultImpl;
    }
    static final int TRANSACTION_onPluginAdd = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_onFail = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_onProgress = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    public static boolean setDefaultImpl(github.tornaco.android.thanos.core.pm.IAddPluginCallback impl) {
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
    public static github.tornaco.android.thanos.core.pm.IAddPluginCallback getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.pm.IAddPluginCallback";
  public void onPluginAdd() throws android.os.RemoteException;
  public void onFail(java.lang.String message) throws android.os.RemoteException;
  public void onProgress(java.lang.String progressMessage) throws android.os.RemoteException;
}
