/*
 * This file is auto-generated.  DO NOT MODIFY.
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
    private static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.backup.IBackupAgent";
    /** Construct the stub at attach it to the interface. */
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
      switch (code)
      {
        case INTERFACE_TRANSACTION:
        {
          reply.writeString(descriptor);
          return true;
        }
        case TRANSACTION_performBackup:
        {
          data.enforceInterface(descriptor);
          github.tornaco.android.thanos.core.backup.IFileDescriptorInitializer _arg0;
          _arg0 = github.tornaco.android.thanos.core.backup.IFileDescriptorInitializer.Stub.asInterface(data.readStrongBinder());
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.lang.String _arg2;
          _arg2 = data.readString();
          github.tornaco.android.thanos.core.backup.IBackupCallback _arg3;
          _arg3 = github.tornaco.android.thanos.core.backup.IBackupCallback.Stub.asInterface(data.readStrongBinder());
          this.performBackup(_arg0, _arg1, _arg2, _arg3);
          return true;
        }
        case TRANSACTION_performRestore:
        {
          data.enforceInterface(descriptor);
          android.os.ParcelFileDescriptor _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.os.ParcelFileDescriptor.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.lang.String _arg2;
          _arg2 = data.readString();
          github.tornaco.android.thanos.core.backup.IBackupCallback _arg3;
          _arg3 = github.tornaco.android.thanos.core.backup.IBackupCallback.Stub.asInterface(data.readStrongBinder());
          this.performRestore(_arg0, _arg1, _arg2, _arg3);
          return true;
        }
        case TRANSACTION_restoreDefault:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.restoreDefault();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
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
          _data.writeStrongBinder((((init!=null))?(init.asBinder()):(null)));
          _data.writeString(domain);
          _data.writeString(path);
          _data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_performBackup, _data, null, android.os.IBinder.FLAG_ONEWAY);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().performBackup(init, domain, path, callback);
            return;
          }
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
          if ((pfd!=null)) {
            _data.writeInt(1);
            pfd.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          _data.writeString(domain);
          _data.writeString(path);
          _data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_performRestore, _data, null, android.os.IBinder.FLAG_ONEWAY);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().performRestore(pfd, domain, path, callback);
            return;
          }
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
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().restoreDefault();
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      public static github.tornaco.android.thanos.core.backup.IBackupAgent sDefaultImpl;
    }
    static final int TRANSACTION_performBackup = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_performRestore = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_restoreDefault = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    public static boolean setDefaultImpl(github.tornaco.android.thanos.core.backup.IBackupAgent impl) {
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
    public static github.tornaco.android.thanos.core.backup.IBackupAgent getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public void performBackup(github.tornaco.android.thanos.core.backup.IFileDescriptorInitializer init, java.lang.String domain, java.lang.String path, github.tornaco.android.thanos.core.backup.IBackupCallback callback) throws android.os.RemoteException;
  public void performRestore(android.os.ParcelFileDescriptor pfd, java.lang.String domain, java.lang.String path, github.tornaco.android.thanos.core.backup.IBackupCallback callback) throws android.os.RemoteException;
  public boolean restoreDefault() throws android.os.RemoteException;
}
