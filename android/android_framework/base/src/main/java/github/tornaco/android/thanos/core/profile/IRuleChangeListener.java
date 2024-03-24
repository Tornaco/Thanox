/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.profile;
public interface IRuleChangeListener extends android.os.IInterface
{
  /** Default implementation for IRuleChangeListener. */
  public static class Default implements github.tornaco.android.thanos.core.profile.IRuleChangeListener
  {
    @Override public void onRuleEnabledStateChanged(int ruleId, boolean enabled) throws android.os.RemoteException
    {
    }
    @Override public void onRuleUpdated(int ruleId) throws android.os.RemoteException
    {
    }
    @Override public void onRuleRemoved(int ruleId) throws android.os.RemoteException
    {
    }
    @Override public void onRuleAdd(int ruleId) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.profile.IRuleChangeListener
  {
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.profile.IRuleChangeListener interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.profile.IRuleChangeListener asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.profile.IRuleChangeListener))) {
        return ((github.tornaco.android.thanos.core.profile.IRuleChangeListener)iin);
      }
      return new github.tornaco.android.thanos.core.profile.IRuleChangeListener.Stub.Proxy(obj);
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
        case TRANSACTION_onRuleEnabledStateChanged:
        {
          int _arg0;
          _arg0 = data.readInt();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.onRuleEnabledStateChanged(_arg0, _arg1);
          break;
        }
        case TRANSACTION_onRuleUpdated:
        {
          int _arg0;
          _arg0 = data.readInt();
          this.onRuleUpdated(_arg0);
          break;
        }
        case TRANSACTION_onRuleRemoved:
        {
          int _arg0;
          _arg0 = data.readInt();
          this.onRuleRemoved(_arg0);
          break;
        }
        case TRANSACTION_onRuleAdd:
        {
          int _arg0;
          _arg0 = data.readInt();
          this.onRuleAdd(_arg0);
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static class Proxy implements github.tornaco.android.thanos.core.profile.IRuleChangeListener
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
      @Override public void onRuleEnabledStateChanged(int ruleId, boolean enabled) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(ruleId);
          _data.writeInt(((enabled)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_onRuleEnabledStateChanged, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      @Override public void onRuleUpdated(int ruleId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(ruleId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onRuleUpdated, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      @Override public void onRuleRemoved(int ruleId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(ruleId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onRuleRemoved, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      @Override public void onRuleAdd(int ruleId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(ruleId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onRuleAdd, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
    }
    static final int TRANSACTION_onRuleEnabledStateChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_onRuleUpdated = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_onRuleRemoved = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_onRuleAdd = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
  }
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.profile.IRuleChangeListener";
  public void onRuleEnabledStateChanged(int ruleId, boolean enabled) throws android.os.RemoteException;
  public void onRuleUpdated(int ruleId) throws android.os.RemoteException;
  public void onRuleRemoved(int ruleId) throws android.os.RemoteException;
  public void onRuleAdd(int ruleId) throws android.os.RemoteException;
}
