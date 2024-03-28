/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.push;
public interface IPushManager extends android.os.IInterface
{
  /** Default implementation for IPushManager. */
  public static class Default implements github.tornaco.android.thanos.core.push.IPushManager
  {
    @Override public void registerChannel(github.tornaco.android.thanos.core.push.PushChannel channel) throws android.os.RemoteException
    {
    }
    @Override public void unRegisterChannel(github.tornaco.android.thanos.core.push.PushChannel channel) throws android.os.RemoteException
    {
    }
    @Override public void registerChannelHandler(java.lang.String channelId, github.tornaco.android.thanos.core.push.IChannelHandler handler) throws android.os.RemoteException
    {
    }
    @Override public void unRegisterChannelHandler(github.tornaco.android.thanos.core.push.IChannelHandler handler) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.push.IPushManager
  {
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.push.IPushManager interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.push.IPushManager asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.push.IPushManager))) {
        return ((github.tornaco.android.thanos.core.push.IPushManager)iin);
      }
      return new github.tornaco.android.thanos.core.push.IPushManager.Stub.Proxy(obj);
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
        case TRANSACTION_registerChannel:
        {
          github.tornaco.android.thanos.core.push.PushChannel _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.push.PushChannel.CREATOR);
          this.registerChannel(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_unRegisterChannel:
        {
          github.tornaco.android.thanos.core.push.PushChannel _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.push.PushChannel.CREATOR);
          this.unRegisterChannel(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_registerChannelHandler:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          github.tornaco.android.thanos.core.push.IChannelHandler _arg1;
          _arg1 = github.tornaco.android.thanos.core.push.IChannelHandler.Stub.asInterface(data.readStrongBinder());
          this.registerChannelHandler(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_unRegisterChannelHandler:
        {
          github.tornaco.android.thanos.core.push.IChannelHandler _arg0;
          _arg0 = github.tornaco.android.thanos.core.push.IChannelHandler.Stub.asInterface(data.readStrongBinder());
          this.unRegisterChannelHandler(_arg0);
          reply.writeNoException();
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static class Proxy implements github.tornaco.android.thanos.core.push.IPushManager
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
      @Override public void registerChannel(github.tornaco.android.thanos.core.push.PushChannel channel) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, channel, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_registerChannel, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void unRegisterChannel(github.tornaco.android.thanos.core.push.PushChannel channel) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, channel, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_unRegisterChannel, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void registerChannelHandler(java.lang.String channelId, github.tornaco.android.thanos.core.push.IChannelHandler handler) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(channelId);
          _data.writeStrongInterface(handler);
          boolean _status = mRemote.transact(Stub.TRANSACTION_registerChannelHandler, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void unRegisterChannelHandler(github.tornaco.android.thanos.core.push.IChannelHandler handler) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(handler);
          boolean _status = mRemote.transact(Stub.TRANSACTION_unRegisterChannelHandler, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
    }
    static final int TRANSACTION_registerChannel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_unRegisterChannel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_registerChannelHandler = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_unRegisterChannelHandler = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
  }
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.push.IPushManager";
  public void registerChannel(github.tornaco.android.thanos.core.push.PushChannel channel) throws android.os.RemoteException;
  public void unRegisterChannel(github.tornaco.android.thanos.core.push.PushChannel channel) throws android.os.RemoteException;
  public void registerChannelHandler(java.lang.String channelId, github.tornaco.android.thanos.core.push.IChannelHandler handler) throws android.os.RemoteException;
  public void unRegisterChannelHandler(github.tornaco.android.thanos.core.push.IChannelHandler handler) throws android.os.RemoteException;
  /** @hide */
  static class _Parcel {
    static private <T> T readTypedObject(
        android.os.Parcel parcel,
        android.os.Parcelable.Creator<T> c) {
      if (parcel.readInt() != 0) {
          return c.createFromParcel(parcel);
      } else {
          return null;
      }
    }
    static private <T extends android.os.Parcelable> void writeTypedObject(
        android.os.Parcel parcel, T value, int parcelableFlags) {
      if (value != null) {
        parcel.writeInt(1);
        value.writeToParcel(parcel, parcelableFlags);
      } else {
        parcel.writeInt(0);
      }
    }
  }
}
