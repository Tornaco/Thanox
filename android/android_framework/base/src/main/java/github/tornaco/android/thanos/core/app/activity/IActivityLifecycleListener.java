/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.app.activity;
public interface IActivityLifecycleListener extends android.os.IInterface
{
  /** Default implementation for IActivityLifecycleListener. */
  public static class Default implements github.tornaco.android.thanos.core.app.activity.IActivityLifecycleListener
  {
    @Override public void onAboutToLaunchActivity(android.content.Intent intent) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.app.activity.IActivityLifecycleListener
  {
    private static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.app.activity.IActivityLifecycleListener";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.app.activity.IActivityLifecycleListener interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.app.activity.IActivityLifecycleListener asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.app.activity.IActivityLifecycleListener))) {
        return ((github.tornaco.android.thanos.core.app.activity.IActivityLifecycleListener)iin);
      }
      return new github.tornaco.android.thanos.core.app.activity.IActivityLifecycleListener.Stub.Proxy(obj);
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
        case TRANSACTION_onAboutToLaunchActivity:
        {
          data.enforceInterface(descriptor);
          android.content.Intent _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.content.Intent.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          this.onAboutToLaunchActivity(_arg0);
          reply.writeNoException();
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements github.tornaco.android.thanos.core.app.activity.IActivityLifecycleListener
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
      @Override public void onAboutToLaunchActivity(android.content.Intent intent) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((intent!=null)) {
            _data.writeInt(1);
            intent.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_onAboutToLaunchActivity, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().onAboutToLaunchActivity(intent);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      public static github.tornaco.android.thanos.core.app.activity.IActivityLifecycleListener sDefaultImpl;
    }
    static final int TRANSACTION_onAboutToLaunchActivity = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    public static boolean setDefaultImpl(github.tornaco.android.thanos.core.app.activity.IActivityLifecycleListener impl) {
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
    public static github.tornaco.android.thanos.core.app.activity.IActivityLifecycleListener getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public void onAboutToLaunchActivity(android.content.Intent intent) throws android.os.RemoteException;
}
