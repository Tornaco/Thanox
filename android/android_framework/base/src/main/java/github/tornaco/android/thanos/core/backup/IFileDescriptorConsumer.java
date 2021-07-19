/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.backup;
// oneway

public interface IFileDescriptorConsumer extends android.os.IInterface
{
  /** Default implementation for IFileDescriptorConsumer. */
  public static class Default implements github.tornaco.android.thanos.core.backup.IFileDescriptorConsumer
  {
    // IO.

    @Override public void acceptAppParcelFileDescriptor(android.os.ParcelFileDescriptor pfd) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.backup.IFileDescriptorConsumer
  {
    private static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.backup.IFileDescriptorConsumer";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.backup.IFileDescriptorConsumer interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.backup.IFileDescriptorConsumer asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.backup.IFileDescriptorConsumer))) {
        return ((github.tornaco.android.thanos.core.backup.IFileDescriptorConsumer)iin);
      }
      return new github.tornaco.android.thanos.core.backup.IFileDescriptorConsumer.Stub.Proxy(obj);
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
        case TRANSACTION_acceptAppParcelFileDescriptor:
        {
          data.enforceInterface(descriptor);
          android.os.ParcelFileDescriptor _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.os.ParcelFileDescriptor.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          this.acceptAppParcelFileDescriptor(_arg0);
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements github.tornaco.android.thanos.core.backup.IFileDescriptorConsumer
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
      // IO.

      @Override public void acceptAppParcelFileDescriptor(android.os.ParcelFileDescriptor pfd) throws android.os.RemoteException
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
          boolean _status = mRemote.transact(Stub.TRANSACTION_acceptAppParcelFileDescriptor, _data, null, android.os.IBinder.FLAG_ONEWAY);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().acceptAppParcelFileDescriptor(pfd);
            return;
          }
        }
        finally {
          _data.recycle();
        }
      }
      public static github.tornaco.android.thanos.core.backup.IFileDescriptorConsumer sDefaultImpl;
    }
    static final int TRANSACTION_acceptAppParcelFileDescriptor = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    public static boolean setDefaultImpl(github.tornaco.android.thanos.core.backup.IFileDescriptorConsumer impl) {
      if (Stub.Proxy.sDefaultImpl == null && impl != null) {
        Stub.Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static github.tornaco.android.thanos.core.backup.IFileDescriptorConsumer getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  // IO.

  public void acceptAppParcelFileDescriptor(android.os.ParcelFileDescriptor pfd) throws android.os.RemoteException;
}
