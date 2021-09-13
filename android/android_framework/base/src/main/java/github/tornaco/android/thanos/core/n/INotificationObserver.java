/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.n;
public interface INotificationObserver extends android.os.IInterface
{
  /** Default implementation for INotificationObserver. */
  public static class Default implements github.tornaco.android.thanos.core.n.INotificationObserver
  {
    @Override public void onNewNotification(github.tornaco.android.thanos.core.n.NotificationRecord record) throws android.os.RemoteException
    {
    }
    @Override public void onNotificationRemoved(github.tornaco.android.thanos.core.n.NotificationRecord record) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.n.INotificationObserver
  {
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.n.INotificationObserver interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.n.INotificationObserver asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.n.INotificationObserver))) {
        return ((github.tornaco.android.thanos.core.n.INotificationObserver)iin);
      }
      return new github.tornaco.android.thanos.core.n.INotificationObserver.Stub.Proxy(obj);
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
        case TRANSACTION_onNewNotification:
        {
          data.enforceInterface(descriptor);
          github.tornaco.android.thanos.core.n.NotificationRecord _arg0;
          if ((0!=data.readInt())) {
            _arg0 = github.tornaco.android.thanos.core.n.NotificationRecord.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          this.onNewNotification(_arg0);
          return true;
        }
        case TRANSACTION_onNotificationRemoved:
        {
          data.enforceInterface(descriptor);
          github.tornaco.android.thanos.core.n.NotificationRecord _arg0;
          if ((0!=data.readInt())) {
            _arg0 = github.tornaco.android.thanos.core.n.NotificationRecord.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          this.onNotificationRemoved(_arg0);
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements github.tornaco.android.thanos.core.n.INotificationObserver
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
      @Override public void onNewNotification(github.tornaco.android.thanos.core.n.NotificationRecord record) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((record!=null)) {
            _data.writeInt(1);
            record.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_onNewNotification, _data, null, android.os.IBinder.FLAG_ONEWAY);
          if (!_status) {
            if (getDefaultImpl() != null) {
              getDefaultImpl().onNewNotification(record);
              return;
            }
          }
        }
        finally {
          _data.recycle();
        }
      }
      @Override public void onNotificationRemoved(github.tornaco.android.thanos.core.n.NotificationRecord record) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((record!=null)) {
            _data.writeInt(1);
            record.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_onNotificationRemoved, _data, null, android.os.IBinder.FLAG_ONEWAY);
          if (!_status) {
            if (getDefaultImpl() != null) {
              getDefaultImpl().onNotificationRemoved(record);
              return;
            }
          }
        }
        finally {
          _data.recycle();
        }
      }
      public static github.tornaco.android.thanos.core.n.INotificationObserver sDefaultImpl;
    }
    static final int TRANSACTION_onNewNotification = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_onNotificationRemoved = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    public static boolean setDefaultImpl(github.tornaco.android.thanos.core.n.INotificationObserver impl) {
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
    public static github.tornaco.android.thanos.core.n.INotificationObserver getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.n.INotificationObserver";
  public void onNewNotification(github.tornaco.android.thanos.core.n.NotificationRecord record) throws android.os.RemoteException;
  public void onNotificationRemoved(github.tornaco.android.thanos.core.n.NotificationRecord record) throws android.os.RemoteException;
}
