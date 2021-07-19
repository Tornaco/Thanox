/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.pref;
public interface IPrefManager extends android.os.IInterface
{
  /** Default implementation for IPrefManager. */
  public static class Default implements github.tornaco.android.thanos.core.pref.IPrefManager
  {
    @Override public boolean putInt(java.lang.String key, int value) throws android.os.RemoteException
    {
      return false;
    }
    @Override public int getInt(java.lang.String key, int def) throws android.os.RemoteException
    {
      return 0;
    }
    @Override public boolean putString(java.lang.String key, java.lang.String value) throws android.os.RemoteException
    {
      return false;
    }
    @Override public java.lang.String getString(java.lang.String key, java.lang.String def) throws android.os.RemoteException
    {
      return null;
    }
    @Override public boolean putBoolean(java.lang.String key, boolean value) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean getBoolean(java.lang.String key, boolean def) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean putLong(java.lang.String key, long value) throws android.os.RemoteException
    {
      return false;
    }
    @Override public long getLong(java.lang.String key, long def) throws android.os.RemoteException
    {
      return 0L;
    }
    @Override public boolean registerSettingsChangeListener(github.tornaco.android.thanos.core.pref.IPrefChangeListener listener) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean unRegisterSettingsChangeListener(github.tornaco.android.thanos.core.pref.IPrefChangeListener listener) throws android.os.RemoteException
    {
      return false;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.pref.IPrefManager
  {
    private static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.pref.IPrefManager";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.pref.IPrefManager interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.pref.IPrefManager asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.pref.IPrefManager))) {
        return ((github.tornaco.android.thanos.core.pref.IPrefManager)iin);
      }
      return new github.tornaco.android.thanos.core.pref.IPrefManager.Stub.Proxy(obj);
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
        case TRANSACTION_putInt:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          boolean _result = this.putInt(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_getInt:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          int _result = this.getInt(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        case TRANSACTION_putString:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          boolean _result = this.putString(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_getString:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.lang.String _result = this.getString(_arg0, _arg1);
          reply.writeNoException();
          reply.writeString(_result);
          return true;
        }
        case TRANSACTION_putBoolean:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          boolean _result = this.putBoolean(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_getBoolean:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          boolean _result = this.getBoolean(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_putLong:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          long _arg1;
          _arg1 = data.readLong();
          boolean _result = this.putLong(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_getLong:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          long _arg1;
          _arg1 = data.readLong();
          long _result = this.getLong(_arg0, _arg1);
          reply.writeNoException();
          reply.writeLong(_result);
          return true;
        }
        case TRANSACTION_registerSettingsChangeListener:
        {
          data.enforceInterface(descriptor);
          github.tornaco.android.thanos.core.pref.IPrefChangeListener _arg0;
          _arg0 = github.tornaco.android.thanos.core.pref.IPrefChangeListener.Stub.asInterface(data.readStrongBinder());
          boolean _result = this.registerSettingsChangeListener(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_unRegisterSettingsChangeListener:
        {
          data.enforceInterface(descriptor);
          github.tornaco.android.thanos.core.pref.IPrefChangeListener _arg0;
          _arg0 = github.tornaco.android.thanos.core.pref.IPrefChangeListener.Stub.asInterface(data.readStrongBinder());
          boolean _result = this.unRegisterSettingsChangeListener(_arg0);
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
    private static class Proxy implements github.tornaco.android.thanos.core.pref.IPrefManager
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
      @Override public boolean putInt(java.lang.String key, int value) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(key);
          _data.writeInt(value);
          boolean _status = mRemote.transact(Stub.TRANSACTION_putInt, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().putInt(key, value);
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
      @Override public int getInt(java.lang.String key, int def) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(key);
          _data.writeInt(def);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getInt, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getInt(key, def);
          }
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean putString(java.lang.String key, java.lang.String value) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(key);
          _data.writeString(value);
          boolean _status = mRemote.transact(Stub.TRANSACTION_putString, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().putString(key, value);
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
      @Override public java.lang.String getString(java.lang.String key, java.lang.String def) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(key);
          _data.writeString(def);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getString, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getString(key, def);
          }
          _reply.readException();
          _result = _reply.readString();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean putBoolean(java.lang.String key, boolean value) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(key);
          _data.writeInt(((value)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_putBoolean, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().putBoolean(key, value);
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
      @Override public boolean getBoolean(java.lang.String key, boolean def) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(key);
          _data.writeInt(((def)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_getBoolean, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getBoolean(key, def);
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
      @Override public boolean putLong(java.lang.String key, long value) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(key);
          _data.writeLong(value);
          boolean _status = mRemote.transact(Stub.TRANSACTION_putLong, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().putLong(key, value);
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
      @Override public long getLong(java.lang.String key, long def) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        long _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(key);
          _data.writeLong(def);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getLong, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getLong(key, def);
          }
          _reply.readException();
          _result = _reply.readLong();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean registerSettingsChangeListener(github.tornaco.android.thanos.core.pref.IPrefChangeListener listener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_registerSettingsChangeListener, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().registerSettingsChangeListener(listener);
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
      @Override public boolean unRegisterSettingsChangeListener(github.tornaco.android.thanos.core.pref.IPrefChangeListener listener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_unRegisterSettingsChangeListener, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().unRegisterSettingsChangeListener(listener);
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
      public static github.tornaco.android.thanos.core.pref.IPrefManager sDefaultImpl;
    }
    static final int TRANSACTION_putInt = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_getInt = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_putString = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_getString = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_putBoolean = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_getBoolean = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_putLong = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_getLong = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    static final int TRANSACTION_registerSettingsChangeListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
    static final int TRANSACTION_unRegisterSettingsChangeListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
    public static boolean setDefaultImpl(github.tornaco.android.thanos.core.pref.IPrefManager impl) {
      if (Stub.Proxy.sDefaultImpl == null && impl != null) {
        Stub.Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static github.tornaco.android.thanos.core.pref.IPrefManager getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public boolean putInt(java.lang.String key, int value) throws android.os.RemoteException;
  public int getInt(java.lang.String key, int def) throws android.os.RemoteException;
  public boolean putString(java.lang.String key, java.lang.String value) throws android.os.RemoteException;
  public java.lang.String getString(java.lang.String key, java.lang.String def) throws android.os.RemoteException;
  public boolean putBoolean(java.lang.String key, boolean value) throws android.os.RemoteException;
  public boolean getBoolean(java.lang.String key, boolean def) throws android.os.RemoteException;
  public boolean putLong(java.lang.String key, long value) throws android.os.RemoteException;
  public long getLong(java.lang.String key, long def) throws android.os.RemoteException;
  public boolean registerSettingsChangeListener(github.tornaco.android.thanos.core.pref.IPrefChangeListener listener) throws android.os.RemoteException;
  public boolean unRegisterSettingsChangeListener(github.tornaco.android.thanos.core.pref.IPrefChangeListener listener) throws android.os.RemoteException;
}
