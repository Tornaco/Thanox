/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.plus;
public interface ICallback extends android.os.IInterface
{
  /** Default implementation for ICallback. */
  public static class Default implements github.tornaco.android.thanos.core.plus.ICallback
  {
    @Override public void onRes(github.tornaco.android.thanos.core.plus.RR res) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.plus.ICallback
  {
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.plus.ICallback interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.plus.ICallback asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.plus.ICallback))) {
        return ((github.tornaco.android.thanos.core.plus.ICallback)iin);
      }
      return new github.tornaco.android.thanos.core.plus.ICallback.Stub.Proxy(obj);
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
        case TRANSACTION_onRes:
        {
          data.enforceInterface(descriptor);
          github.tornaco.android.thanos.core.plus.RR _arg0;
          if ((0!=data.readInt())) {
            _arg0 = github.tornaco.android.thanos.core.plus.RR.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          this.onRes(_arg0);
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements github.tornaco.android.thanos.core.plus.ICallback
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
      @Override public void onRes(github.tornaco.android.thanos.core.plus.RR res) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((res!=null)) {
            _data.writeInt(1);
            res.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_onRes, _data, null, android.os.IBinder.FLAG_ONEWAY);
          if (!_status) {
            if (getDefaultImpl() != null) {
              getDefaultImpl().onRes(res);
              return;
            }
          }
        }
        finally {
          _data.recycle();
        }
      }
      public static github.tornaco.android.thanos.core.plus.ICallback sDefaultImpl;
    }
    static final int TRANSACTION_onRes = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    public static boolean setDefaultImpl(github.tornaco.android.thanos.core.plus.ICallback impl) {
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
    public static github.tornaco.android.thanos.core.plus.ICallback getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.plus.ICallback";
  public void onRes(github.tornaco.android.thanos.core.plus.RR res) throws android.os.RemoteException;
}
