/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /Users/haoguo/Library/Android/sdk/build-tools/35.0.0/aidl -I/Users/haoguo/Documents/workspace/Thanox/android/android_framework/base/src/main/java -p/Users/haoguo/Documents/workspace/Thanox/android/android_sdk/framework.aidl -p/Users/haoguo/Documents/workspace/Thanox/android/android_sdk/thanos.aidl /Users/haoguo/Documents/workspace/Thanox/android/android_framework/base/src/main/java/github/tornaco/android/thanos/core/backup/IBackupAgent.aidl
 */
package github.tornaco.android.thanos.core.backup;
public interface IBackupAgent extends android.os.IInterface
{
  /** Default implementation for IBackupAgent. */
  public static class Default implements github.tornaco.android.thanos.core.backup.IBackupAgent
  {
    @Override public void performBackup(github.tornaco.android.thanos.core.backup.IFileDescriptorInitializer init, java.lang.String domain, java.lang.String path, github.tornaco.android.thanos.core.backup.IBackupCallback callback) throws android.os.RemoteException
    {
    }
    @Override public void performRestore(android.os.ParcelFileDescriptor pfd, java.lang.String domain, java.lang.String path, github.tornaco.android.thanos.core.backup.IBackupCallback callback) throws android.os.RemoteException
    {
    }
    @Override public boolean restoreDefault() throws android.os.RemoteException
    {
      return false;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.backup.IBackupAgent
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.backup.IBackupAgent interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.backup.IBackupAgent asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.backup.IBackupAgent))) {
        return ((github.tornaco.android.thanos.core.backup.IBackupAgent)iin);
      }
      return new github.tornaco.android.thanos.core.backup.IBackupAgent.Stub.Proxy(obj);
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
        case TRANSACTION_performBackup:
        {
          github.tornaco.android.thanos.core.backup.IFileDescriptorInitializer _arg0;
          _arg0 = github.tornaco.android.thanos.core.backup.IFileDescriptorInitializer.Stub.asInterface(data.readStrongBinder());
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.lang.String _arg2;
          _arg2 = data.readString();
          github.tornaco.android.thanos.core.backup.IBackupCallback _arg3;
          _arg3 = github.tornaco.android.thanos.core.backup.IBackupCallback.Stub.asInterface(data.readStrongBinder());
          this.performBackup(_arg0, _arg1, _arg2, _arg3);
          break;
        }
        case TRANSACTION_performRestore:
        {
          android.os.ParcelFileDescriptor _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.os.ParcelFileDescriptor.CREATOR);
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.lang.String _arg2;
          _arg2 = data.readString();
          github.tornaco.android.thanos.core.backup.IBackupCallback _arg3;
          _arg3 = github.tornaco.android.thanos.core.backup.IBackupCallback.Stub.asInterface(data.readStrongBinder());
          this.performRestore(_arg0, _arg1, _arg2, _arg3);
          break;
        }
        case TRANSACTION_restoreDefault:
        {
          boolean _result = this.restoreDefault();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static class Proxy implements github.tornaco.android.thanos.core.backup.IBackupAgent
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
      @Override public void performBackup(github.tornaco.android.thanos.core.backup.IFileDescriptorInitializer init, java.lang.String domain, java.lang.String path, github.tornaco.android.thanos.core.backup.IBackupCallback callback) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(init);
          _data.writeString(domain);
          _data.writeString(path);
          _data.writeStrongInterface(callback);
          boolean _status = mRemote.transact(Stub.TRANSACTION_performBackup, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      @Override public void performRestore(android.os.ParcelFileDescriptor pfd, java.lang.String domain, java.lang.String path, github.tornaco.android.thanos.core.backup.IBackupCallback callback) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pfd, 0);
          _data.writeString(domain);
          _data.writeString(path);
          _data.writeStrongInterface(callback);
          boolean _status = mRemote.transact(Stub.TRANSACTION_performRestore, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      @Override public boolean restoreDefault() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_restoreDefault, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
    }
    static final int TRANSACTION_performBackup = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_performRestore = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_restoreDefault = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.backup.IBackupAgent";
  public void performBackup(github.tornaco.android.thanos.core.backup.IFileDescriptorInitializer init, java.lang.String domain, java.lang.String path, github.tornaco.android.thanos.core.backup.IBackupCallback callback) throws android.os.RemoteException;
  public void performRestore(android.os.ParcelFileDescriptor pfd, java.lang.String domain, java.lang.String path, github.tornaco.android.thanos.core.backup.IBackupCallback callback) throws android.os.RemoteException;
  public boolean restoreDefault() throws android.os.RemoteException;
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
