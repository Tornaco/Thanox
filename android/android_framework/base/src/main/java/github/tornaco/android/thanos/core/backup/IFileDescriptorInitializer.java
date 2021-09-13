/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.backup;
// oneway
public interface IFileDescriptorInitializer extends android.os.IInterface
{
  /** Default implementation for IFileDescriptorInitializer. */
  public static class Default implements github.tornaco.android.thanos.core.backup.IFileDescriptorInitializer
  {
    @Override public void initParcelFileDescriptor(java.lang.String domain, java.lang.String path, github.tornaco.android.thanos.core.backup.IFileDescriptorConsumer consumer) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.backup.IFileDescriptorInitializer
  {
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.backup.IFileDescriptorInitializer interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.backup.IFileDescriptorInitializer asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.backup.IFileDescriptorInitializer))) {
        return ((github.tornaco.android.thanos.core.backup.IFileDescriptorInitializer)iin);
      }
      return new github.tornaco.android.thanos.core.backup.IFileDescriptorInitializer.Stub.Proxy(obj);
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
        case TRANSACTION_initParcelFileDescriptor:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          github.tornaco.android.thanos.core.backup.IFileDescriptorConsumer _arg2;
          _arg2 = github.tornaco.android.thanos.core.backup.IFileDescriptorConsumer.Stub.asInterface(data.readStrongBinder());
          this.initParcelFileDescriptor(_arg0, _arg1, _arg2);
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements github.tornaco.android.thanos.core.backup.IFileDescriptorInitializer
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
      @Override public void initParcelFileDescriptor(java.lang.String domain, java.lang.String path, github.tornaco.android.thanos.core.backup.IFileDescriptorConsumer consumer) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(domain);
          _data.writeString(path);
          _data.writeStrongBinder((((consumer!=null))?(consumer.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_initParcelFileDescriptor, _data, null, android.os.IBinder.FLAG_ONEWAY);
          if (!_status) {
            if (getDefaultImpl() != null) {
              getDefaultImpl().initParcelFileDescriptor(domain, path, consumer);
              return;
            }
          }
        }
        finally {
          _data.recycle();
        }
      }
      public static github.tornaco.android.thanos.core.backup.IFileDescriptorInitializer sDefaultImpl;
    }
    static final int TRANSACTION_initParcelFileDescriptor = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    public static boolean setDefaultImpl(github.tornaco.android.thanos.core.backup.IFileDescriptorInitializer impl) {
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
    public static github.tornaco.android.thanos.core.backup.IFileDescriptorInitializer getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.backup.IFileDescriptorInitializer";
  public void initParcelFileDescriptor(java.lang.String domain, java.lang.String path, github.tornaco.android.thanos.core.backup.IFileDescriptorConsumer consumer) throws android.os.RemoteException;
}
