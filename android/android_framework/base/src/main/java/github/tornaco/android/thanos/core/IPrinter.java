/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core;
public interface IPrinter extends android.os.IInterface
{
  /** Default implementation for IPrinter. */
  public static class Default implements github.tornaco.android.thanos.core.IPrinter
  {
    @Override public void println(java.lang.String content) throws android.os.RemoteException
    {
    }
    @Override public void print(java.lang.String content) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.IPrinter
  {
    private static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.IPrinter";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.IPrinter interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.IPrinter asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.IPrinter))) {
        return ((github.tornaco.android.thanos.core.IPrinter)iin);
      }
      return new github.tornaco.android.thanos.core.IPrinter.Stub.Proxy(obj);
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
        case TRANSACTION_println:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.println(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_print:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.print(_arg0);
          reply.writeNoException();
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements github.tornaco.android.thanos.core.IPrinter
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
      @Override public void println(java.lang.String content) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(content);
          boolean _status = mRemote.transact(Stub.TRANSACTION_println, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().println(content);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void print(java.lang.String content) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(content);
          boolean _status = mRemote.transact(Stub.TRANSACTION_print, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().print(content);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      public static github.tornaco.android.thanos.core.IPrinter sDefaultImpl;
    }
    static final int TRANSACTION_println = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_print = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    public static boolean setDefaultImpl(github.tornaco.android.thanos.core.IPrinter impl) {
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
    public static github.tornaco.android.thanos.core.IPrinter getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public void println(java.lang.String content) throws android.os.RemoteException;
  public void print(java.lang.String content) throws android.os.RemoteException;
}
