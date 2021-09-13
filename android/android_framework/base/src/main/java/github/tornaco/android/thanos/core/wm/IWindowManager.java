/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.wm;
public interface IWindowManager extends android.os.IInterface
{
  /** Default implementation for IWindowManager. */
  public static class Default implements github.tornaco.android.thanos.core.wm.IWindowManager
  {
    @Override public int[] getScreenSize() throws android.os.RemoteException
    {
      return null;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.wm.IWindowManager
  {
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.wm.IWindowManager interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.wm.IWindowManager asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.wm.IWindowManager))) {
        return ((github.tornaco.android.thanos.core.wm.IWindowManager)iin);
      }
      return new github.tornaco.android.thanos.core.wm.IWindowManager.Stub.Proxy(obj);
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
        case TRANSACTION_getScreenSize:
        {
          data.enforceInterface(descriptor);
          int[] _result = this.getScreenSize();
          reply.writeNoException();
          reply.writeIntArray(_result);
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements github.tornaco.android.thanos.core.wm.IWindowManager
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
      @Override public int[] getScreenSize() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getScreenSize, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().getScreenSize();
            }
          }
          _reply.readException();
          _result = _reply.createIntArray();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      public static github.tornaco.android.thanos.core.wm.IWindowManager sDefaultImpl;
    }
    static final int TRANSACTION_getScreenSize = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    public static boolean setDefaultImpl(github.tornaco.android.thanos.core.wm.IWindowManager impl) {
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
    public static github.tornaco.android.thanos.core.wm.IWindowManager getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.wm.IWindowManager";
  public int[] getScreenSize() throws android.os.RemoteException;
}
