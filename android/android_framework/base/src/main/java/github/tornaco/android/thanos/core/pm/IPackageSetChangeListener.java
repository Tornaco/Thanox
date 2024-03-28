/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.pm;
public interface IPackageSetChangeListener extends android.os.IInterface
{
  /** Default implementation for IPackageSetChangeListener. */
  public static class Default implements github.tornaco.android.thanos.core.pm.IPackageSetChangeListener
  {
    @Override public void onPackageSetAdded(java.lang.String pkgSetId) throws android.os.RemoteException
    {
    }
    @Override public void onPackageSetRemoved(java.lang.String pkgSetId) throws android.os.RemoteException
    {
    }
    @Override public void onPackageSetChanged(java.lang.String pkgSetId) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.pm.IPackageSetChangeListener
  {
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.pm.IPackageSetChangeListener interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.pm.IPackageSetChangeListener asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.pm.IPackageSetChangeListener))) {
        return ((github.tornaco.android.thanos.core.pm.IPackageSetChangeListener)iin);
      }
      return new github.tornaco.android.thanos.core.pm.IPackageSetChangeListener.Stub.Proxy(obj);
    }
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      java.lang.String descriptor = DESCRIPTOR;
      if (code >= android.os.IBinder.FIRST_CALL_TRANSACTION && code <= android.os.IBinder.LAST_CALL_TRANSACTION) {
        data.enforceInterface(descriptor);
      }
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
        case TRANSACTION_onPackageSetAdded:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.onPackageSetAdded(_arg0);
          break;
        }
        case TRANSACTION_onPackageSetRemoved:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.onPackageSetRemoved(_arg0);
          break;
        }
        case TRANSACTION_onPackageSetChanged:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.onPackageSetChanged(_arg0);
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static class Proxy implements github.tornaco.android.thanos.core.pm.IPackageSetChangeListener
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
      @Override public void onPackageSetAdded(java.lang.String pkgSetId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgSetId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onPackageSetAdded, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      @Override public void onPackageSetRemoved(java.lang.String pkgSetId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgSetId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onPackageSetRemoved, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      @Override public void onPackageSetChanged(java.lang.String pkgSetId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgSetId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onPackageSetChanged, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
    }
    static final int TRANSACTION_onPackageSetAdded = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_onPackageSetRemoved = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_onPackageSetChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
  }
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.pm.IPackageSetChangeListener";
  public void onPackageSetAdded(java.lang.String pkgSetId) throws android.os.RemoteException;
  public void onPackageSetRemoved(java.lang.String pkgSetId) throws android.os.RemoteException;
  public void onPackageSetChanged(java.lang.String pkgSetId) throws android.os.RemoteException;
}
