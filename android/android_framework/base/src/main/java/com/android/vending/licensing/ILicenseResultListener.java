/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package com.android.vending.licensing;
public interface ILicenseResultListener extends android.os.IInterface
{
  /** Default implementation for ILicenseResultListener. */
  public static class Default implements com.android.vending.licensing.ILicenseResultListener
  {
    @Override public void verifyLicense(int responseCode, java.lang.String signedData, java.lang.String signature) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements com.android.vending.licensing.ILicenseResultListener
  {
    private static final java.lang.String DESCRIPTOR = "com.android.vending.licensing.ILicenseResultListener";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an com.android.vending.licensing.ILicenseResultListener interface,
     * generating a proxy if needed.
     */
    public static com.android.vending.licensing.ILicenseResultListener asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof com.android.vending.licensing.ILicenseResultListener))) {
        return ((com.android.vending.licensing.ILicenseResultListener)iin);
      }
      return new com.android.vending.licensing.ILicenseResultListener.Stub.Proxy(obj);
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
        case TRANSACTION_verifyLicense:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.lang.String _arg2;
          _arg2 = data.readString();
          this.verifyLicense(_arg0, _arg1, _arg2);
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements com.android.vending.licensing.ILicenseResultListener
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
      @Override public void verifyLicense(int responseCode, java.lang.String signedData, java.lang.String signature) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(responseCode);
          _data.writeString(signedData);
          _data.writeString(signature);
          boolean _status = mRemote.transact(Stub.TRANSACTION_verifyLicense, _data, null, android.os.IBinder.FLAG_ONEWAY);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().verifyLicense(responseCode, signedData, signature);
            return;
          }
        }
        finally {
          _data.recycle();
        }
      }
      public static com.android.vending.licensing.ILicenseResultListener sDefaultImpl;
    }
    static final int TRANSACTION_verifyLicense = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    public static boolean setDefaultImpl(com.android.vending.licensing.ILicenseResultListener impl) {
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
    public static com.android.vending.licensing.ILicenseResultListener getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public void verifyLicense(int responseCode, java.lang.String signedData, java.lang.String signature) throws android.os.RemoteException;
}
