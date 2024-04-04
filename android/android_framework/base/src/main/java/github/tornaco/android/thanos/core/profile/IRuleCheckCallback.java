/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.profile;
public interface IRuleCheckCallback extends android.os.IInterface
{
  /** Default implementation for IRuleCheckCallback. */
  public static class Default implements github.tornaco.android.thanos.core.profile.IRuleCheckCallback
  {
    @Override public void onValid(github.tornaco.android.thanos.core.profile.RuleInfo rule) throws android.os.RemoteException
    {
    }
    @Override public void onInvalid(int errorCode, java.lang.String errorMessage) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.profile.IRuleCheckCallback
  {
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.profile.IRuleCheckCallback interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.profile.IRuleCheckCallback asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.profile.IRuleCheckCallback))) {
        return ((github.tornaco.android.thanos.core.profile.IRuleCheckCallback)iin);
      }
      return new github.tornaco.android.thanos.core.profile.IRuleCheckCallback.Stub.Proxy(obj);
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
        case TRANSACTION_onValid:
        {
          github.tornaco.android.thanos.core.profile.RuleInfo _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.profile.RuleInfo.CREATOR);
          this.onValid(_arg0);
          break;
        }
        case TRANSACTION_onInvalid:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.onInvalid(_arg0, _arg1);
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static class Proxy implements github.tornaco.android.thanos.core.profile.IRuleCheckCallback
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
      @Override public void onValid(github.tornaco.android.thanos.core.profile.RuleInfo rule) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, rule, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onValid, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      @Override public void onInvalid(int errorCode, java.lang.String errorMessage) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(errorCode);
          _data.writeString(errorMessage);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onInvalid, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
    }
    static final int TRANSACTION_onValid = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_onInvalid = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
  }
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.profile.IRuleCheckCallback";
  public void onValid(github.tornaco.android.thanos.core.profile.RuleInfo rule) throws android.os.RemoteException;
  public void onInvalid(int errorCode, java.lang.String errorMessage) throws android.os.RemoteException;
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
