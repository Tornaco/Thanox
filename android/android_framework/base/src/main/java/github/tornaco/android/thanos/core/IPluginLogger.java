/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core;
public interface IPluginLogger extends android.os.IInterface
{
  /** Default implementation for IPluginLogger. */
  public static class Default implements github.tornaco.android.thanos.core.IPluginLogger
  {
    @Override public void logV(java.lang.String content) throws android.os.RemoteException
    {
    }
    @Override public void logD(java.lang.String content) throws android.os.RemoteException
    {
    }
    @Override public void logW(java.lang.String content) throws android.os.RemoteException
    {
    }
    @Override public void logE(java.lang.String content) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.IPluginLogger
  {
    private static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.IPluginLogger";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.IPluginLogger interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.IPluginLogger asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.IPluginLogger))) {
        return ((github.tornaco.android.thanos.core.IPluginLogger)iin);
      }
      return new github.tornaco.android.thanos.core.IPluginLogger.Stub.Proxy(obj);
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
        case TRANSACTION_logV:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.logV(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_logD:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.logD(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_logW:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.logW(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_logE:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.logE(_arg0);
          reply.writeNoException();
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements github.tornaco.android.thanos.core.IPluginLogger
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
      @Override public void logV(java.lang.String content) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(content);
          boolean _status = mRemote.transact(Stub.TRANSACTION_logV, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().logV(content);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void logD(java.lang.String content) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(content);
          boolean _status = mRemote.transact(Stub.TRANSACTION_logD, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().logD(content);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void logW(java.lang.String content) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(content);
          boolean _status = mRemote.transact(Stub.TRANSACTION_logW, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().logW(content);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void logE(java.lang.String content) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(content);
          boolean _status = mRemote.transact(Stub.TRANSACTION_logE, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().logE(content);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      public static github.tornaco.android.thanos.core.IPluginLogger sDefaultImpl;
    }
    static final int TRANSACTION_logV = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_logD = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_logW = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_logE = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    public static boolean setDefaultImpl(github.tornaco.android.thanos.core.IPluginLogger impl) {
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
    public static github.tornaco.android.thanos.core.IPluginLogger getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public void logV(java.lang.String content) throws android.os.RemoteException;
  public void logD(java.lang.String content) throws android.os.RemoteException;
  public void logW(java.lang.String content) throws android.os.RemoteException;
  public void logE(java.lang.String content) throws android.os.RemoteException;
}
