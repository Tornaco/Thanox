/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.input;
public interface IInputManager extends android.os.IInterface
{
  /** Default implementation for IInputManager. */
  public static class Default implements github.tornaco.android.thanos.core.input.IInputManager
  {
    @Override public boolean injectKey(int keyCode) throws android.os.RemoteException
    {
      return false;
    }
    @Override public int getLastKey() throws android.os.RemoteException
    {
      return 0;
    }
    @Override public void onKeyEvent(android.view.KeyEvent keyEvent, java.lang.String source) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.input.IInputManager
  {
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.input.IInputManager interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.input.IInputManager asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.input.IInputManager))) {
        return ((github.tornaco.android.thanos.core.input.IInputManager)iin);
      }
      return new github.tornaco.android.thanos.core.input.IInputManager.Stub.Proxy(obj);
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
        case TRANSACTION_injectKey:
        {
          int _arg0;
          _arg0 = data.readInt();
          boolean _result = this.injectKey(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_getLastKey:
        {
          int _result = this.getLastKey();
          reply.writeNoException();
          reply.writeInt(_result);
          break;
        }
        case TRANSACTION_onKeyEvent:
        {
          android.view.KeyEvent _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.view.KeyEvent.CREATOR);
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.onKeyEvent(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static class Proxy implements github.tornaco.android.thanos.core.input.IInputManager
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
      @Override public boolean injectKey(int keyCode) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(keyCode);
          boolean _status = mRemote.transact(Stub.TRANSACTION_injectKey, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public int getLastKey() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getLastKey, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void onKeyEvent(android.view.KeyEvent keyEvent, java.lang.String source) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, keyEvent, 0);
          _data.writeString(source);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onKeyEvent, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
    }
    static final int TRANSACTION_injectKey = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_getLastKey = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_onKeyEvent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
  }
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.input.IInputManager";
  public boolean injectKey(int keyCode) throws android.os.RemoteException;
  public int getLastKey() throws android.os.RemoteException;
  public void onKeyEvent(android.view.KeyEvent keyEvent, java.lang.String source) throws android.os.RemoteException;
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
