/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.n;
public interface INotificationManager extends android.os.IInterface
{
  /** Default implementation for INotificationManager. */
  public static class Default implements github.tornaco.android.thanos.core.n.INotificationManager
  {
    @Override public java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> getShowingNotificationRecordsForPackage(java.lang.String packageName) throws android.os.RemoteException
    {
      return null;
    }
    @Override public boolean hasShowingNotificationRecordsForPackage(java.lang.String packageName) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void registerObserver(github.tornaco.android.thanos.core.n.INotificationObserver obs) throws android.os.RemoteException
    {
    }
    @Override public void unRegisterObserver(github.tornaco.android.thanos.core.n.INotificationObserver obs) throws android.os.RemoteException
    {
    }
    @Override public void setScreenOnNotificationEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isScreenOnNotificationEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setScreenOnNotificationEnabledForPkg(java.lang.String pkg, boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isScreenOnNotificationEnabledForPkg(java.lang.String pkg) throws android.os.RemoteException
    {
      return false;
    }
    @Override public int nextNotificationId() throws android.os.RemoteException
    {
      return 0;
    }
    @Override public void setPersistOnNewNotificationEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isPersistOnNewNotificationEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void cleanUpPersistNotificationRecords() throws android.os.RemoteException
    {
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> getAllNotificationRecordsByPage(int start, int limit) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> getNotificationRecordsForPackage(java.lang.String packageName) throws android.os.RemoteException
    {
      return null;
    }
    @Override public void onAddNotificationRecord(github.tornaco.android.thanos.core.n.NotificationRecord record) throws android.os.RemoteException
    {
    }
    @Override public void setShowToastAppInfoEnabled(boolean enabled) throws android.os.RemoteException
    {
    }
    @Override public boolean isShowToastAppInfoEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean isNREnabled(int type) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setNREnabled(int type, boolean enabled) throws android.os.RemoteException
    {
    }
    // For searching.
    @Override public java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> getAllNotificationRecordsByPageAndKeyword(int start, int limit, java.lang.String keyword) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> getAllNotificationRecordsByPageAndKeywordInDateRange(int start, int limit, long startTimeMills, long endTimeMills, java.lang.String keyword) throws android.os.RemoteException
    {
      return null;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.n.INotificationManager
  {
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.n.INotificationManager interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.n.INotificationManager asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.n.INotificationManager))) {
        return ((github.tornaco.android.thanos.core.n.INotificationManager)iin);
      }
      return new github.tornaco.android.thanos.core.n.INotificationManager.Stub.Proxy(obj);
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
        case TRANSACTION_getShowingNotificationRecordsForPackage:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> _result = this.getShowingNotificationRecordsForPackage(_arg0);
          reply.writeNoException();
          reply.writeTypedList(_result);
          return true;
        }
        case TRANSACTION_hasShowingNotificationRecordsForPackage:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.hasShowingNotificationRecordsForPackage(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_registerObserver:
        {
          data.enforceInterface(descriptor);
          github.tornaco.android.thanos.core.n.INotificationObserver _arg0;
          _arg0 = github.tornaco.android.thanos.core.n.INotificationObserver.Stub.asInterface(data.readStrongBinder());
          this.registerObserver(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_unRegisterObserver:
        {
          data.enforceInterface(descriptor);
          github.tornaco.android.thanos.core.n.INotificationObserver _arg0;
          _arg0 = github.tornaco.android.thanos.core.n.INotificationObserver.Stub.asInterface(data.readStrongBinder());
          this.unRegisterObserver(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_setScreenOnNotificationEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setScreenOnNotificationEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isScreenOnNotificationEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.isScreenOnNotificationEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setScreenOnNotificationEnabledForPkg:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setScreenOnNotificationEnabledForPkg(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isScreenOnNotificationEnabledForPkg:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.isScreenOnNotificationEnabledForPkg(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_nextNotificationId:
        {
          data.enforceInterface(descriptor);
          int _result = this.nextNotificationId();
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        case TRANSACTION_setPersistOnNewNotificationEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setPersistOnNewNotificationEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isPersistOnNewNotificationEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.isPersistOnNewNotificationEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_cleanUpPersistNotificationRecords:
        {
          data.enforceInterface(descriptor);
          this.cleanUpPersistNotificationRecords();
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_getAllNotificationRecordsByPage:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          int _arg1;
          _arg1 = data.readInt();
          java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> _result = this.getAllNotificationRecordsByPage(_arg0, _arg1);
          reply.writeNoException();
          reply.writeTypedList(_result);
          return true;
        }
        case TRANSACTION_getNotificationRecordsForPackage:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> _result = this.getNotificationRecordsForPackage(_arg0);
          reply.writeNoException();
          reply.writeTypedList(_result);
          return true;
        }
        case TRANSACTION_onAddNotificationRecord:
        {
          data.enforceInterface(descriptor);
          github.tornaco.android.thanos.core.n.NotificationRecord _arg0;
          if ((0!=data.readInt())) {
            _arg0 = github.tornaco.android.thanos.core.n.NotificationRecord.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          this.onAddNotificationRecord(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_setShowToastAppInfoEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setShowToastAppInfoEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isShowToastAppInfoEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.isShowToastAppInfoEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_isNREnabled:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          boolean _result = this.isNREnabled(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setNREnabled:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setNREnabled(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_getAllNotificationRecordsByPageAndKeyword:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          int _arg1;
          _arg1 = data.readInt();
          java.lang.String _arg2;
          _arg2 = data.readString();
          java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> _result = this.getAllNotificationRecordsByPageAndKeyword(_arg0, _arg1, _arg2);
          reply.writeNoException();
          reply.writeTypedList(_result);
          return true;
        }
        case TRANSACTION_getAllNotificationRecordsByPageAndKeywordInDateRange:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          int _arg1;
          _arg1 = data.readInt();
          long _arg2;
          _arg2 = data.readLong();
          long _arg3;
          _arg3 = data.readLong();
          java.lang.String _arg4;
          _arg4 = data.readString();
          java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> _result = this.getAllNotificationRecordsByPageAndKeywordInDateRange(_arg0, _arg1, _arg2, _arg3, _arg4);
          reply.writeNoException();
          reply.writeTypedList(_result);
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements github.tornaco.android.thanos.core.n.INotificationManager
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
      @Override public java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> getShowingNotificationRecordsForPackage(java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getShowingNotificationRecordsForPackage, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().getShowingNotificationRecordsForPackage(packageName);
            }
          }
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.n.NotificationRecord.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean hasShowingNotificationRecordsForPackage(java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_hasShowingNotificationRecordsForPackage, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().hasShowingNotificationRecordsForPackage(packageName);
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
      @Override public void registerObserver(github.tornaco.android.thanos.core.n.INotificationObserver obs) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongBinder((((obs!=null))?(obs.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_registerObserver, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              getDefaultImpl().registerObserver(obs);
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
      @Override public void unRegisterObserver(github.tornaco.android.thanos.core.n.INotificationObserver obs) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongBinder((((obs!=null))?(obs.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_unRegisterObserver, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              getDefaultImpl().unRegisterObserver(obs);
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
      @Override public void setScreenOnNotificationEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setScreenOnNotificationEnabled, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              getDefaultImpl().setScreenOnNotificationEnabled(enable);
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
      @Override public boolean isScreenOnNotificationEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isScreenOnNotificationEnabled, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().isScreenOnNotificationEnabled();
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
      @Override public void setScreenOnNotificationEnabledForPkg(java.lang.String pkg, boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setScreenOnNotificationEnabledForPkg, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              getDefaultImpl().setScreenOnNotificationEnabledForPkg(pkg, enable);
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
      @Override public boolean isScreenOnNotificationEnabledForPkg(java.lang.String pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isScreenOnNotificationEnabledForPkg, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().isScreenOnNotificationEnabledForPkg(pkg);
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
      @Override public int nextNotificationId() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_nextNotificationId, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().nextNotificationId();
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
      @Override public void setPersistOnNewNotificationEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPersistOnNewNotificationEnabled, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              getDefaultImpl().setPersistOnNewNotificationEnabled(enable);
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
      @Override public boolean isPersistOnNewNotificationEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPersistOnNewNotificationEnabled, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().isPersistOnNewNotificationEnabled();
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
      @Override public void cleanUpPersistNotificationRecords() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_cleanUpPersistNotificationRecords, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              getDefaultImpl().cleanUpPersistNotificationRecords();
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
      @Override public java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> getAllNotificationRecordsByPage(int start, int limit) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(start);
          _data.writeInt(limit);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAllNotificationRecordsByPage, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().getAllNotificationRecordsByPage(start, limit);
            }
          }
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.n.NotificationRecord.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> getNotificationRecordsForPackage(java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getNotificationRecordsForPackage, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().getNotificationRecordsForPackage(packageName);
            }
          }
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.n.NotificationRecord.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void onAddNotificationRecord(github.tornaco.android.thanos.core.n.NotificationRecord record) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((record!=null)) {
            _data.writeInt(1);
            record.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_onAddNotificationRecord, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              getDefaultImpl().onAddNotificationRecord(record);
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
      @Override public void setShowToastAppInfoEnabled(boolean enabled) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enabled)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setShowToastAppInfoEnabled, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              getDefaultImpl().setShowToastAppInfoEnabled(enabled);
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
      @Override public boolean isShowToastAppInfoEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isShowToastAppInfoEnabled, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().isShowToastAppInfoEnabled();
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
      @Override public boolean isNREnabled(int type) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(type);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isNREnabled, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().isNREnabled(type);
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
      @Override public void setNREnabled(int type, boolean enabled) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(type);
          _data.writeInt(((enabled)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setNREnabled, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              getDefaultImpl().setNREnabled(type, enabled);
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
      // For searching.
      @Override public java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> getAllNotificationRecordsByPageAndKeyword(int start, int limit, java.lang.String keyword) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(start);
          _data.writeInt(limit);
          _data.writeString(keyword);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAllNotificationRecordsByPageAndKeyword, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().getAllNotificationRecordsByPageAndKeyword(start, limit, keyword);
            }
          }
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.n.NotificationRecord.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> getAllNotificationRecordsByPageAndKeywordInDateRange(int start, int limit, long startTimeMills, long endTimeMills, java.lang.String keyword) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(start);
          _data.writeInt(limit);
          _data.writeLong(startTimeMills);
          _data.writeLong(endTimeMills);
          _data.writeString(keyword);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAllNotificationRecordsByPageAndKeywordInDateRange, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().getAllNotificationRecordsByPageAndKeywordInDateRange(start, limit, startTimeMills, endTimeMills, keyword);
            }
          }
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.n.NotificationRecord.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      public static github.tornaco.android.thanos.core.n.INotificationManager sDefaultImpl;
    }
    static final int TRANSACTION_getShowingNotificationRecordsForPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_hasShowingNotificationRecordsForPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_registerObserver = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_unRegisterObserver = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_setScreenOnNotificationEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_isScreenOnNotificationEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_setScreenOnNotificationEnabledForPkg = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_isScreenOnNotificationEnabledForPkg = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    static final int TRANSACTION_nextNotificationId = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
    static final int TRANSACTION_setPersistOnNewNotificationEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
    static final int TRANSACTION_isPersistOnNewNotificationEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
    static final int TRANSACTION_cleanUpPersistNotificationRecords = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
    static final int TRANSACTION_getAllNotificationRecordsByPage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
    static final int TRANSACTION_getNotificationRecordsForPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
    static final int TRANSACTION_onAddNotificationRecord = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
    static final int TRANSACTION_setShowToastAppInfoEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
    static final int TRANSACTION_isShowToastAppInfoEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
    static final int TRANSACTION_isNREnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
    static final int TRANSACTION_setNREnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
    static final int TRANSACTION_getAllNotificationRecordsByPageAndKeyword = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
    static final int TRANSACTION_getAllNotificationRecordsByPageAndKeywordInDateRange = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
    public static boolean setDefaultImpl(github.tornaco.android.thanos.core.n.INotificationManager impl) {
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
    public static github.tornaco.android.thanos.core.n.INotificationManager getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.n.INotificationManager";
  public java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> getShowingNotificationRecordsForPackage(java.lang.String packageName) throws android.os.RemoteException;
  public boolean hasShowingNotificationRecordsForPackage(java.lang.String packageName) throws android.os.RemoteException;
  public void registerObserver(github.tornaco.android.thanos.core.n.INotificationObserver obs) throws android.os.RemoteException;
  public void unRegisterObserver(github.tornaco.android.thanos.core.n.INotificationObserver obs) throws android.os.RemoteException;
  public void setScreenOnNotificationEnabled(boolean enable) throws android.os.RemoteException;
  public boolean isScreenOnNotificationEnabled() throws android.os.RemoteException;
  public void setScreenOnNotificationEnabledForPkg(java.lang.String pkg, boolean enable) throws android.os.RemoteException;
  public boolean isScreenOnNotificationEnabledForPkg(java.lang.String pkg) throws android.os.RemoteException;
  public int nextNotificationId() throws android.os.RemoteException;
  public void setPersistOnNewNotificationEnabled(boolean enable) throws android.os.RemoteException;
  public boolean isPersistOnNewNotificationEnabled() throws android.os.RemoteException;
  public void cleanUpPersistNotificationRecords() throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> getAllNotificationRecordsByPage(int start, int limit) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> getNotificationRecordsForPackage(java.lang.String packageName) throws android.os.RemoteException;
  public void onAddNotificationRecord(github.tornaco.android.thanos.core.n.NotificationRecord record) throws android.os.RemoteException;
  public void setShowToastAppInfoEnabled(boolean enabled) throws android.os.RemoteException;
  public boolean isShowToastAppInfoEnabled() throws android.os.RemoteException;
  public boolean isNREnabled(int type) throws android.os.RemoteException;
  public void setNREnabled(int type, boolean enabled) throws android.os.RemoteException;
  // For searching.
  public java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> getAllNotificationRecordsByPageAndKeyword(int start, int limit, java.lang.String keyword) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> getAllNotificationRecordsByPageAndKeywordInDateRange(int start, int limit, long startTimeMills, long endTimeMills, java.lang.String keyword) throws android.os.RemoteException;
}
