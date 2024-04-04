/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.profile;
public interface IRuleAddCallback extends android.os.IInterface
{
  /** Default implementation for IRuleAddCallback. */
  public static class Default implements github.tornaco.android.thanos.core.profile.IRuleAddCallback
  {
    @Override public void onRuleAddSuccess() throws android.os.RemoteException
    {
    }
    @Override public void onRuleAddFail(int errorCode, java.lang.String errorMessage) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.profile.IRuleAddCallback
  {
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.profile.IRuleAddCallback interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.profile.IRuleAddCallback asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.profile.IRuleAddCallback))) {
        return ((github.tornaco.android.thanos.core.profile.IRuleAddCallback)iin);
      }
      return new github.tornaco.android.thanos.core.profile.IRuleAddCallback.Stub.Proxy(obj);
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
        case TRANSACTION_onRuleAddSuccess:
        {
          this.onRuleAddSuccess();
          break;
        }
        case TRANSACTION_onRuleAddFail:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.onRuleAddFail(_arg0, _arg1);
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static class Proxy implements github.tornaco.android.thanos.core.profile.IRuleAddCallback
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
      @Override public void onRuleAddSuccess() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onRuleAddSuccess, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      @Override public void onRuleAddFail(int errorCode, java.lang.String errorMessage) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(errorCode);
          _data.writeString(errorMessage);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onRuleAddFail, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
    }
    static final int TRANSACTION_onRuleAddSuccess = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_onRuleAddFail = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
  }
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.profile.IRuleAddCallback";
  public void onRuleAddSuccess() throws android.os.RemoteException;
  public void onRuleAddFail(int errorCode, java.lang.String errorMessage) throws android.os.RemoteException;
}
