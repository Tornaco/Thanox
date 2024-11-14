/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /home/tornaco/Android/Sdk/build-tools/35.0.0/aidl -I/home/tornaco/Documents/Thanox/android/android_framework/base/src/main/java -p/home/tornaco/Documents/Thanox/android/android_sdk/framework.aidl -p/home/tornaco/Documents/Thanox/android/android_sdk/thanos.aidl /home/tornaco/Documents/Thanox/android/android_framework/base/src/main/java/github/tornaco/android/thanos/core/os/IServiceManager.aidl
 */
package github.tornaco.android.thanos.core.os;
public interface IServiceManager extends android.os.IInterface
{
  /** Default implementation for IServiceManager. */
  public static class Default implements github.tornaco.android.thanos.core.os.IServiceManager
  {
    @Override public boolean hasService(java.lang.String name) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void addService(java.lang.String name, android.os.IBinder binder) throws android.os.RemoteException
    {
    }
    @Override public android.os.IBinder getService(java.lang.String name) throws android.os.RemoteException
    {
      return null;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.os.IServiceManager
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.os.IServiceManager interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.os.IServiceManager asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.os.IServiceManager))) {
        return ((github.tornaco.android.thanos.core.os.IServiceManager)iin);
      }
      return new github.tornaco.android.thanos.core.os.IServiceManager.Stub.Proxy(obj);
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
        case TRANSACTION_hasService:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.hasService(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_addService:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          android.os.IBinder _arg1;
          _arg1 = data.readStrongBinder();
          this.addService(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getService:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          android.os.IBinder _result = this.getService(_arg0);
          reply.writeNoException();
          reply.writeStrongBinder(_result);
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static class Proxy implements github.tornaco.android.thanos.core.os.IServiceManager
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
      @Override public boolean hasService(java.lang.String name) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(name);
          boolean _status = mRemote.transact(Stub.TRANSACTION_hasService, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void addService(java.lang.String name, android.os.IBinder binder) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(name);
          _data.writeStrongBinder(binder);
          boolean _status = mRemote.transact(Stub.TRANSACTION_addService, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public android.os.IBinder getService(java.lang.String name) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.os.IBinder _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(name);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getService, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readStrongBinder();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
    }
    static final int TRANSACTION_hasService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_addService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_getService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.os.IServiceManager";
  public boolean hasService(java.lang.String name) throws android.os.RemoteException;
  public void addService(java.lang.String name, android.os.IBinder binder) throws android.os.RemoteException;
  public android.os.IBinder getService(java.lang.String name) throws android.os.RemoteException;
}
