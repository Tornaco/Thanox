/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.plugin.push.message.delegate;
public interface IPushDelegateManager extends android.os.IInterface
{
  /** Default implementation for IPushDelegateManager. */
  public static class Default implements github.tornaco.android.plugin.push.message.delegate.IPushDelegateManager
  {
    @Override public boolean wechatEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setWeChatEnabled(boolean enabled) throws android.os.RemoteException
    {
    }
    @Override public boolean wechatSoundEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setWechatSoundEnabled(boolean enabled) throws android.os.RemoteException
    {
    }
    @Override public boolean wechatContentEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setWechatContentEnabled(boolean enabled) throws android.os.RemoteException
    {
    }
    @Override public boolean wechatVibrateEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setWechatVibrateEnabled(boolean enabled) throws android.os.RemoteException
    {
    }
    @Override public void mockWechatMessage() throws android.os.RemoteException
    {
    }
    @Override public boolean startWechatOnPushEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setStartWechatOnPushEnabled(boolean enabled) throws android.os.RemoteException
    {
    }
    @Override public boolean skipIfWeChatAppRunningEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setSkipIfWeChatAppRunningEnabled(boolean enabled) throws android.os.RemoteException
    {
    }
    @Override public boolean shouldHookBroadcastPerformResult() throws android.os.RemoteException
    {
      return false;
    }
    @Override public int onHookBroadcastPerformResult(android.content.Intent intent, int resultCode) throws android.os.RemoteException
    {
      return 0;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.plugin.push.message.delegate.IPushDelegateManager
  {
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.plugin.push.message.delegate.IPushDelegateManager interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.plugin.push.message.delegate.IPushDelegateManager asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.plugin.push.message.delegate.IPushDelegateManager))) {
        return ((github.tornaco.android.plugin.push.message.delegate.IPushDelegateManager)iin);
      }
      return new github.tornaco.android.plugin.push.message.delegate.IPushDelegateManager.Stub.Proxy(obj);
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
        case TRANSACTION_wechatEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.wechatEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setWeChatEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setWeChatEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_wechatSoundEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.wechatSoundEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setWechatSoundEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setWechatSoundEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_wechatContentEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.wechatContentEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setWechatContentEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setWechatContentEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_wechatVibrateEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.wechatVibrateEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setWechatVibrateEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setWechatVibrateEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_mockWechatMessage:
        {
          data.enforceInterface(descriptor);
          this.mockWechatMessage();
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_startWechatOnPushEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.startWechatOnPushEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setStartWechatOnPushEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setStartWechatOnPushEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_skipIfWeChatAppRunningEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.skipIfWeChatAppRunningEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setSkipIfWeChatAppRunningEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setSkipIfWeChatAppRunningEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_shouldHookBroadcastPerformResult:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.shouldHookBroadcastPerformResult();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_onHookBroadcastPerformResult:
        {
          data.enforceInterface(descriptor);
          android.content.Intent _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.content.Intent.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          int _arg1;
          _arg1 = data.readInt();
          int _result = this.onHookBroadcastPerformResult(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements github.tornaco.android.plugin.push.message.delegate.IPushDelegateManager
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
      @Override public boolean wechatEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_wechatEnabled, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().wechatEnabled();
            }
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
      @Override public void setWeChatEnabled(boolean enabled) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enabled)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setWeChatEnabled, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              getDefaultImpl().setWeChatEnabled(enabled);
              return;
            }
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean wechatSoundEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_wechatSoundEnabled, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().wechatSoundEnabled();
            }
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
      @Override public void setWechatSoundEnabled(boolean enabled) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enabled)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setWechatSoundEnabled, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              getDefaultImpl().setWechatSoundEnabled(enabled);
              return;
            }
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean wechatContentEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_wechatContentEnabled, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().wechatContentEnabled();
            }
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
      @Override public void setWechatContentEnabled(boolean enabled) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enabled)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setWechatContentEnabled, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              getDefaultImpl().setWechatContentEnabled(enabled);
              return;
            }
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean wechatVibrateEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_wechatVibrateEnabled, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().wechatVibrateEnabled();
            }
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
      @Override public void setWechatVibrateEnabled(boolean enabled) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enabled)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setWechatVibrateEnabled, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              getDefaultImpl().setWechatVibrateEnabled(enabled);
              return;
            }
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void mockWechatMessage() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_mockWechatMessage, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              getDefaultImpl().mockWechatMessage();
              return;
            }
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean startWechatOnPushEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_startWechatOnPushEnabled, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().startWechatOnPushEnabled();
            }
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
      @Override public void setStartWechatOnPushEnabled(boolean enabled) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enabled)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setStartWechatOnPushEnabled, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              getDefaultImpl().setStartWechatOnPushEnabled(enabled);
              return;
            }
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean skipIfWeChatAppRunningEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_skipIfWeChatAppRunningEnabled, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().skipIfWeChatAppRunningEnabled();
            }
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
      @Override public void setSkipIfWeChatAppRunningEnabled(boolean enabled) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enabled)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setSkipIfWeChatAppRunningEnabled, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              getDefaultImpl().setSkipIfWeChatAppRunningEnabled(enabled);
              return;
            }
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean shouldHookBroadcastPerformResult() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_shouldHookBroadcastPerformResult, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().shouldHookBroadcastPerformResult();
            }
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
      @Override public int onHookBroadcastPerformResult(android.content.Intent intent, int resultCode) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((intent!=null)) {
            _data.writeInt(1);
            intent.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          _data.writeInt(resultCode);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onHookBroadcastPerformResult, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().onHookBroadcastPerformResult(intent, resultCode);
            }
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
      public static github.tornaco.android.plugin.push.message.delegate.IPushDelegateManager sDefaultImpl;
    }
    static final int TRANSACTION_wechatEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_setWeChatEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_wechatSoundEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_setWechatSoundEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_wechatContentEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_setWechatContentEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_wechatVibrateEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_setWechatVibrateEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    static final int TRANSACTION_mockWechatMessage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
    static final int TRANSACTION_startWechatOnPushEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
    static final int TRANSACTION_setStartWechatOnPushEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
    static final int TRANSACTION_skipIfWeChatAppRunningEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
    static final int TRANSACTION_setSkipIfWeChatAppRunningEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
    static final int TRANSACTION_shouldHookBroadcastPerformResult = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
    static final int TRANSACTION_onHookBroadcastPerformResult = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
    public static boolean setDefaultImpl(github.tornaco.android.plugin.push.message.delegate.IPushDelegateManager impl) {
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
    public static github.tornaco.android.plugin.push.message.delegate.IPushDelegateManager getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.plugin.push.message.delegate.IPushDelegateManager";
  public boolean wechatEnabled() throws android.os.RemoteException;
  public void setWeChatEnabled(boolean enabled) throws android.os.RemoteException;
  public boolean wechatSoundEnabled() throws android.os.RemoteException;
  public void setWechatSoundEnabled(boolean enabled) throws android.os.RemoteException;
  public boolean wechatContentEnabled() throws android.os.RemoteException;
  public void setWechatContentEnabled(boolean enabled) throws android.os.RemoteException;
  public boolean wechatVibrateEnabled() throws android.os.RemoteException;
  public void setWechatVibrateEnabled(boolean enabled) throws android.os.RemoteException;
  public void mockWechatMessage() throws android.os.RemoteException;
  public boolean startWechatOnPushEnabled() throws android.os.RemoteException;
  public void setStartWechatOnPushEnabled(boolean enabled) throws android.os.RemoteException;
  public boolean skipIfWeChatAppRunningEnabled() throws android.os.RemoteException;
  public void setSkipIfWeChatAppRunningEnabled(boolean enabled) throws android.os.RemoteException;
  public boolean shouldHookBroadcastPerformResult() throws android.os.RemoteException;
  public int onHookBroadcastPerformResult(android.content.Intent intent, int resultCode) throws android.os.RemoteException;
}
