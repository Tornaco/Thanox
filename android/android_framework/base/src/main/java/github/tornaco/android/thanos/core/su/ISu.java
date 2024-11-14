/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.su;
public interface ISu extends android.os.IInterface
{
  /** Default implementation for ISu. */
  public static class Default implements github.tornaco.android.thanos.core.su.ISu
  {
    @Override public github.tornaco.android.thanos.core.su.SuRes exe(java.lang.String[] command) throws android.os.RemoteException
    {
      return null;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.su.ISu
  {
    private static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.su.ISu";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.su.ISu interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.su.ISu asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.su.ISu))) {
        return ((github.tornaco.android.thanos.core.su.ISu)iin);
      }
      return new github.tornaco.android.thanos.core.su.ISu.Stub.Proxy(obj);
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
        case TRANSACTION_exe:
        {
          data.enforceInterface(descriptor);
          java.lang.String[] _arg0;
          _arg0 = data.createStringArray();
          github.tornaco.android.thanos.core.su.SuRes _result = this.exe(_arg0);
          reply.writeNoException();
          if ((_result!=null)) {
            reply.writeInt(1);
            _result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          }
          else {
            reply.writeInt(0);
          }
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements github.tornaco.android.thanos.core.su.ISu
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
      @Override public github.tornaco.android.thanos.core.su.SuRes exe(java.lang.String[] command) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.su.SuRes _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStringArray(command);
          boolean _status = mRemote.transact(Stub.TRANSACTION_exe, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().exe(command);
          }
          _reply.readException();
          if ((0!=_reply.readInt())) {
            _result = github.tornaco.android.thanos.core.su.SuRes.CREATOR.createFromParcel(_reply);
          }
          else {
            _result = null;
          }
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      public static github.tornaco.android.thanos.core.su.ISu sDefaultImpl;
    }
    static final int TRANSACTION_exe = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    public static boolean setDefaultImpl(github.tornaco.android.thanos.core.su.ISu impl) {
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
    public static github.tornaco.android.thanos.core.su.ISu getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public github.tornaco.android.thanos.core.su.SuRes exe(java.lang.String[] command) throws android.os.RemoteException;
}
