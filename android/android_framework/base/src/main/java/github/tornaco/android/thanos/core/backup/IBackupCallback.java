/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /Users/haoguo/Library/Android/sdk/build-tools/35.0.0/aidl -I/Users/haoguo/Documents/workspace/Thanox/android/android_framework/base/src/main/java -p/Users/haoguo/Documents/workspace/Thanox/android/android_sdk/framework.aidl -p/Users/haoguo/Documents/workspace/Thanox/android/android_sdk/thanos.aidl /Users/haoguo/Documents/workspace/Thanox/android/android_framework/base/src/main/java/github/tornaco/android/thanos/core/backup/IBackupCallback.aidl
 */
package github.tornaco.android.thanos.core.backup;
public interface IBackupCallback extends android.os.IInterface
{
  /** Default implementation for IBackupCallback. */
  public static class Default implements github.tornaco.android.thanos.core.backup.IBackupCallback
  {
    @Override public void onBackupFinished(java.lang.String domain, java.lang.String path) throws android.os.RemoteException
    {
    }
    @Override public void onRestoreFinished(java.lang.String domain, java.lang.String path) throws android.os.RemoteException
    {
    }
    @Override public void onFail(java.lang.String message) throws android.os.RemoteException
    {
    }
    @Override public void onProgress(java.lang.String progressMessage) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.backup.IBackupCallback
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.backup.IBackupCallback interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.backup.IBackupCallback asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.backup.IBackupCallback))) {
        return ((github.tornaco.android.thanos.core.backup.IBackupCallback)iin);
      }
      return new github.tornaco.android.thanos.core.backup.IBackupCallback.Stub.Proxy(obj);
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
        case TRANSACTION_onBackupFinished:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.onBackupFinished(_arg0, _arg1);
          break;
        }
        case TRANSACTION_onRestoreFinished:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.onRestoreFinished(_arg0, _arg1);
          break;
        }
        case TRANSACTION_onFail:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.onFail(_arg0);
          break;
        }
        case TRANSACTION_onProgress:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.onProgress(_arg0);
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static class Proxy implements github.tornaco.android.thanos.core.backup.IBackupCallback
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
      @Override public void onBackupFinished(java.lang.String domain, java.lang.String path) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(domain);
          _data.writeString(path);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onBackupFinished, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      @Override public void onRestoreFinished(java.lang.String domain, java.lang.String path) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(domain);
          _data.writeString(path);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onRestoreFinished, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      @Override public void onFail(java.lang.String message) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(message);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onFail, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      @Override public void onProgress(java.lang.String progressMessage) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(progressMessage);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onProgress, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
    }
    static final int TRANSACTION_onBackupFinished = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_onRestoreFinished = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_onFail = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_onProgress = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.backup.IBackupCallback";
  public void onBackupFinished(java.lang.String domain, java.lang.String path) throws android.os.RemoteException;
  public void onRestoreFinished(java.lang.String domain, java.lang.String path) throws android.os.RemoteException;
  public void onFail(java.lang.String message) throws android.os.RemoteException;
  public void onProgress(java.lang.String progressMessage) throws android.os.RemoteException;
}
