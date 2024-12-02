/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /Users/haoguo/Library/Android/sdk/build-tools/35.0.0/aidl -I/Users/haoguo/Documents/workspace/Thanox/android/android_framework/base/src/main/java -p/Users/haoguo/Documents/workspace/Thanox/android/android_sdk/framework.aidl -p/Users/haoguo/Documents/workspace/Thanox/android/android_sdk/thanos.aidl /Users/haoguo/Documents/workspace/Thanox/android/android_framework/base/src/main/java/github/tornaco/android/thanos/core/push/wechat/IPushDelegateManager.aidl
 */
package github.tornaco.android.thanos.core.push.wechat;
public interface IPushDelegateManager extends android.os.IInterface
{
  /** Default implementation for IPushDelegateManager. */
  public static class Default implements github.tornaco.android.thanos.core.push.wechat.IPushDelegateManager
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
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.push.wechat.IPushDelegateManager
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.push.wechat.IPushDelegateManager interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.push.wechat.IPushDelegateManager asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.push.wechat.IPushDelegateManager))) {
        return ((github.tornaco.android.thanos.core.push.wechat.IPushDelegateManager)iin);
      }
      return new github.tornaco.android.thanos.core.push.wechat.IPushDelegateManager.Stub.Proxy(obj);
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
      if (code == INTERFACE_TRANSACTION) {
        reply.writeString(descriptor);
        return true;
      }
      switch (code)
      {
        case TRANSACTION_wechatEnabled:
        {
          boolean _result = this.wechatEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setWeChatEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setWeChatEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_wechatSoundEnabled:
        {
          boolean _result = this.wechatSoundEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setWechatSoundEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setWechatSoundEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_wechatContentEnabled:
        {
          boolean _result = this.wechatContentEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setWechatContentEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setWechatContentEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_wechatVibrateEnabled:
        {
          boolean _result = this.wechatVibrateEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setWechatVibrateEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setWechatVibrateEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_mockWechatMessage:
        {
          this.mockWechatMessage();
          reply.writeNoException();
          break;
        }
        case TRANSACTION_startWechatOnPushEnabled:
        {
          boolean _result = this.startWechatOnPushEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setStartWechatOnPushEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setStartWechatOnPushEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_skipIfWeChatAppRunningEnabled:
        {
          boolean _result = this.skipIfWeChatAppRunningEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setSkipIfWeChatAppRunningEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setSkipIfWeChatAppRunningEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_shouldHookBroadcastPerformResult:
        {
          boolean _result = this.shouldHookBroadcastPerformResult();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_onHookBroadcastPerformResult:
        {
          android.content.Intent _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.content.Intent.CREATOR);
          int _arg1;
          _arg1 = data.readInt();
          int _result = this.onHookBroadcastPerformResult(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(_result);
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static class Proxy implements github.tornaco.android.thanos.core.push.wechat.IPushDelegateManager
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
          _Parcel.writeTypedObject(_data, intent, 0);
          _data.writeInt(resultCode);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onHookBroadcastPerformResult, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
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
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.push.wechat.IPushDelegateManager";
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
