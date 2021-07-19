/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.app.event;
public interface IEventSubscriber extends android.os.IInterface
{
  /** Default implementation for IEventSubscriber. */
  public static class Default implements github.tornaco.android.thanos.core.app.event.IEventSubscriber
  {
    @Override public void onEvent(github.tornaco.android.thanos.core.app.event.ThanosEvent e) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.app.event.IEventSubscriber
  {
    private static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.app.event.IEventSubscriber";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.app.event.IEventSubscriber interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.app.event.IEventSubscriber asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.app.event.IEventSubscriber))) {
        return ((github.tornaco.android.thanos.core.app.event.IEventSubscriber)iin);
      }
      return new github.tornaco.android.thanos.core.app.event.IEventSubscriber.Stub.Proxy(obj);
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
        case TRANSACTION_onEvent:
        {
          data.enforceInterface(descriptor);
          github.tornaco.android.thanos.core.app.event.ThanosEvent _arg0;
          if ((0!=data.readInt())) {
            _arg0 = github.tornaco.android.thanos.core.app.event.ThanosEvent.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          this.onEvent(_arg0);
          reply.writeNoException();
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements github.tornaco.android.thanos.core.app.event.IEventSubscriber
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
      @Override public void onEvent(github.tornaco.android.thanos.core.app.event.ThanosEvent e) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((e!=null)) {
            _data.writeInt(1);
            e.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_onEvent, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().onEvent(e);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      public static github.tornaco.android.thanos.core.app.event.IEventSubscriber sDefaultImpl;
    }
    static final int TRANSACTION_onEvent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    public static boolean setDefaultImpl(github.tornaco.android.thanos.core.app.event.IEventSubscriber impl) {
      if (Stub.Proxy.sDefaultImpl == null && impl != null) {
        Stub.Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static github.tornaco.android.thanos.core.app.event.IEventSubscriber getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public void onEvent(github.tornaco.android.thanos.core.app.event.ThanosEvent e) throws android.os.RemoteException;
}
