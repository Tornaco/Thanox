/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.pm;
public interface IPackageEnableStateChangeListener extends android.os.IInterface
{
  /** Default implementation for IPackageEnableStateChangeListener. */
  public static class Default implements github.tornaco.android.thanos.core.pm.IPackageEnableStateChangeListener
  {
    @Override public void onPackageEnableStateChanged(java.util.List<github.tornaco.android.thanos.core.pm.Pkg> pkgs) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.pm.IPackageEnableStateChangeListener
  {
    private static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.pm.IPackageEnableStateChangeListener";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.pm.IPackageEnableStateChangeListener interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.pm.IPackageEnableStateChangeListener asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.pm.IPackageEnableStateChangeListener))) {
        return ((github.tornaco.android.thanos.core.pm.IPackageEnableStateChangeListener)iin);
      }
      return new github.tornaco.android.thanos.core.pm.IPackageEnableStateChangeListener.Stub.Proxy(obj);
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
        case TRANSACTION_onPackageEnableStateChanged:
        {
          data.enforceInterface(descriptor);
          java.util.List<github.tornaco.android.thanos.core.pm.Pkg> _arg0;
          _arg0 = data.createTypedArrayList(github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          this.onPackageEnableStateChanged(_arg0);
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements github.tornaco.android.thanos.core.pm.IPackageEnableStateChangeListener
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
      @Override public void onPackageEnableStateChanged(java.util.List<github.tornaco.android.thanos.core.pm.Pkg> pkgs) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeTypedList(pkgs);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onPackageEnableStateChanged, _data, null, android.os.IBinder.FLAG_ONEWAY);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().onPackageEnableStateChanged(pkgs);
            return;
          }
        }
        finally {
          _data.recycle();
        }
      }
      public static github.tornaco.android.thanos.core.pm.IPackageEnableStateChangeListener sDefaultImpl;
    }
    static final int TRANSACTION_onPackageEnableStateChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    public static boolean setDefaultImpl(github.tornaco.android.thanos.core.pm.IPackageEnableStateChangeListener impl) {
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
    public static github.tornaco.android.thanos.core.pm.IPackageEnableStateChangeListener getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public void onPackageEnableStateChanged(java.util.List<github.tornaco.android.thanos.core.pm.Pkg> pkgs) throws android.os.RemoteException;
}
