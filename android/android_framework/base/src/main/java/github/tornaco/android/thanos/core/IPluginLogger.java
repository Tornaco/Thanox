/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /Users/haoguo/Library/Android/sdk/build-tools/35.0.0/aidl -I/Users/haoguo/Documents/workspace/Thanox/android/android_framework/base/src/main/java -p/Users/haoguo/Documents/workspace/Thanox/android/android_sdk/framework.aidl -p/Users/haoguo/Documents/workspace/Thanox/android/android_sdk/thanos.aidl /Users/haoguo/Documents/workspace/Thanox/android/android_framework/base/src/main/java/github/tornaco/android/thanos/core/IPluginLogger.aidl
 */
package github.tornaco.android.thanos.core;
public interface IPluginLogger extends android.os.IInterface
{
  /** Default implementation for IPluginLogger. */
  public static class Default implements github.tornaco.android.thanos.core.IPluginLogger
  {
    @Override public void logV(java.lang.String content) throws android.os.RemoteException
    {
    }
    @Override public void logD(java.lang.String content) throws android.os.RemoteException
    {
    }
    @Override public void logW(java.lang.String content) throws android.os.RemoteException
    {
    }
    @Override public void logE(java.lang.String content) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.IPluginLogger
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.IPluginLogger interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.IPluginLogger asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.IPluginLogger))) {
        return ((github.tornaco.android.thanos.core.IPluginLogger)iin);
      }
      return new github.tornaco.android.thanos.core.IPluginLogger.Stub.Proxy(obj);
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
      if (code == INTERFACE_TRANSACTION) {
        reply.writeString(descriptor);
        return true;
      }
      switch (code)
      {
        case TRANSACTION_logV:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.logV(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_logD:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.logD(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_logW:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.logW(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_logE:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.logE(_arg0);
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
    private static class Proxy implements github.tornaco.android.thanos.core.IPluginLogger
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
      @Override public void logV(java.lang.String content) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(content);
          boolean _status = mRemote.transact(Stub.TRANSACTION_logV, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void logD(java.lang.String content) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(content);
          boolean _status = mRemote.transact(Stub.TRANSACTION_logD, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void logW(java.lang.String content) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(content);
          boolean _status = mRemote.transact(Stub.TRANSACTION_logW, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void logE(java.lang.String content) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(content);
          boolean _status = mRemote.transact(Stub.TRANSACTION_logE, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
    }
    static final int TRANSACTION_logV = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_logD = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_logW = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_logE = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.IPluginLogger";
  public void logV(java.lang.String content) throws android.os.RemoteException;
  public void logD(java.lang.String content) throws android.os.RemoteException;
  public void logW(java.lang.String content) throws android.os.RemoteException;
  public void logE(java.lang.String content) throws android.os.RemoteException;
}
