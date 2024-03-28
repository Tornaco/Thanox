/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.n;
public interface INotificationManager extends android.os.IInterface
{
  /** Default implementation for INotificationManager. */
  public static class Default implements github.tornaco.android.thanos.core.n.INotificationManager
  {
    @Override public java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> getShowingNotificationRecordsForPackage(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
      return null;
    }
    @Override public boolean hasShowingNotificationRecordsForPackage(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
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
    @Override public void onSetPrimaryClip(android.content.ClipData clip, github.tornaco.android.thanos.core.pm.Pkg caller) throws android.os.RemoteException
    {
    }
    @Override public void setPackageRedactionNotificationEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isPackageRedactionNotificationEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setPackageRedactionNotificationTitle(github.tornaco.android.thanos.core.pm.Pkg pkg, java.lang.String title) throws android.os.RemoteException
    {
    }
    @Override public java.lang.String getPackageRedactionNotificationTitle(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
      return null;
    }
    @Override public void setPackageRedactionNotificationText(github.tornaco.android.thanos.core.pm.Pkg pkg, java.lang.String text) throws android.os.RemoteException
    {
    }
    @Override public java.lang.String getPackageRedactionNotificationText(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
      return null;
    }
    @Override public void setPersistAllPkgEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isPersistAllPkgEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setPkgNREnabled(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isPkgNREnabled(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
    {
      return false;
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
        case TRANSACTION_getShowingNotificationRecordsForPackage:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> _result = this.getShowingNotificationRecordsForPackage(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_hasShowingNotificationRecordsForPackage:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _result = this.hasShowingNotificationRecordsForPackage(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_registerObserver:
        {
          github.tornaco.android.thanos.core.n.INotificationObserver _arg0;
          _arg0 = github.tornaco.android.thanos.core.n.INotificationObserver.Stub.asInterface(data.readStrongBinder());
          this.registerObserver(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_unRegisterObserver:
        {
          github.tornaco.android.thanos.core.n.INotificationObserver _arg0;
          _arg0 = github.tornaco.android.thanos.core.n.INotificationObserver.Stub.asInterface(data.readStrongBinder());
          this.unRegisterObserver(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_setScreenOnNotificationEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setScreenOnNotificationEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isScreenOnNotificationEnabled:
        {
          boolean _result = this.isScreenOnNotificationEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setScreenOnNotificationEnabledForPkg:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setScreenOnNotificationEnabledForPkg(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isScreenOnNotificationEnabledForPkg:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.isScreenOnNotificationEnabledForPkg(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_nextNotificationId:
        {
          int _result = this.nextNotificationId();
          reply.writeNoException();
          reply.writeInt(_result);
          break;
        }
        case TRANSACTION_setPersistOnNewNotificationEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setPersistOnNewNotificationEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isPersistOnNewNotificationEnabled:
        {
          boolean _result = this.isPersistOnNewNotificationEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_cleanUpPersistNotificationRecords:
        {
          this.cleanUpPersistNotificationRecords();
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getAllNotificationRecordsByPage:
        {
          int _arg0;
          _arg0 = data.readInt();
          int _arg1;
          _arg1 = data.readInt();
          java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> _result = this.getAllNotificationRecordsByPage(_arg0, _arg1);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getNotificationRecordsForPackage:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> _result = this.getNotificationRecordsForPackage(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_onAddNotificationRecord:
        {
          github.tornaco.android.thanos.core.n.NotificationRecord _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.n.NotificationRecord.CREATOR);
          this.onAddNotificationRecord(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_setShowToastAppInfoEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setShowToastAppInfoEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isShowToastAppInfoEnabled:
        {
          boolean _result = this.isShowToastAppInfoEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_isNREnabled:
        {
          int _arg0;
          _arg0 = data.readInt();
          boolean _result = this.isNREnabled(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setNREnabled:
        {
          int _arg0;
          _arg0 = data.readInt();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setNREnabled(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getAllNotificationRecordsByPageAndKeyword:
        {
          int _arg0;
          _arg0 = data.readInt();
          int _arg1;
          _arg1 = data.readInt();
          java.lang.String _arg2;
          _arg2 = data.readString();
          java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> _result = this.getAllNotificationRecordsByPageAndKeyword(_arg0, _arg1, _arg2);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getAllNotificationRecordsByPageAndKeywordInDateRange:
        {
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
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_onSetPrimaryClip:
        {
          android.content.ClipData _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.content.ClipData.CREATOR);
          github.tornaco.android.thanos.core.pm.Pkg _arg1;
          _arg1 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          this.onSetPrimaryClip(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_setPackageRedactionNotificationEnabled:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setPackageRedactionNotificationEnabled(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isPackageRedactionNotificationEnabled:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _result = this.isPackageRedactionNotificationEnabled(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setPackageRedactionNotificationTitle:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.setPackageRedactionNotificationTitle(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getPackageRedactionNotificationTitle:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          java.lang.String _result = this.getPackageRedactionNotificationTitle(_arg0);
          reply.writeNoException();
          reply.writeString(_result);
          break;
        }
        case TRANSACTION_setPackageRedactionNotificationText:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.setPackageRedactionNotificationText(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getPackageRedactionNotificationText:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          java.lang.String _result = this.getPackageRedactionNotificationText(_arg0);
          reply.writeNoException();
          reply.writeString(_result);
          break;
        }
        case TRANSACTION_setPersistAllPkgEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setPersistAllPkgEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isPersistAllPkgEnabled:
        {
          boolean _result = this.isPersistAllPkgEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setPkgNREnabled:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setPkgNREnabled(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isPkgNREnabled:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _result = this.isPkgNREnabled(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
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
      @Override public java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> getShowingNotificationRecordsForPackage(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getShowingNotificationRecordsForPackage, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.n.NotificationRecord.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean hasShowingNotificationRecordsForPackage(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_hasShowingNotificationRecordsForPackage, _data, _reply, 0);
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
          _data.writeStrongInterface(obs);
          boolean _status = mRemote.transact(Stub.TRANSACTION_registerObserver, _data, _reply, 0);
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
          _data.writeStrongInterface(obs);
          boolean _status = mRemote.transact(Stub.TRANSACTION_unRegisterObserver, _data, _reply, 0);
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
          _Parcel.writeTypedObject(_data, record, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onAddNotificationRecord, _data, _reply, 0);
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
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.n.NotificationRecord.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void onSetPrimaryClip(android.content.ClipData clip, github.tornaco.android.thanos.core.pm.Pkg caller) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, clip, 0);
          _Parcel.writeTypedObject(_data, caller, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onSetPrimaryClip, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setPackageRedactionNotificationEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPackageRedactionNotificationEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isPackageRedactionNotificationEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPackageRedactionNotificationEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setPackageRedactionNotificationTitle(github.tornaco.android.thanos.core.pm.Pkg pkg, java.lang.String title) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          _data.writeString(title);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPackageRedactionNotificationTitle, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.lang.String getPackageRedactionNotificationTitle(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getPackageRedactionNotificationTitle, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readString();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setPackageRedactionNotificationText(github.tornaco.android.thanos.core.pm.Pkg pkg, java.lang.String text) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          _data.writeString(text);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPackageRedactionNotificationText, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.lang.String getPackageRedactionNotificationText(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getPackageRedactionNotificationText, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readString();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setPersistAllPkgEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPersistAllPkgEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isPersistAllPkgEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPersistAllPkgEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setPkgNREnabled(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPkgNREnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isPkgNREnabled(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPkgNREnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
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
    static final int TRANSACTION_onSetPrimaryClip = (android.os.IBinder.FIRST_CALL_TRANSACTION + 21);
    static final int TRANSACTION_setPackageRedactionNotificationEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 22);
    static final int TRANSACTION_isPackageRedactionNotificationEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 23);
    static final int TRANSACTION_setPackageRedactionNotificationTitle = (android.os.IBinder.FIRST_CALL_TRANSACTION + 24);
    static final int TRANSACTION_getPackageRedactionNotificationTitle = (android.os.IBinder.FIRST_CALL_TRANSACTION + 25);
    static final int TRANSACTION_setPackageRedactionNotificationText = (android.os.IBinder.FIRST_CALL_TRANSACTION + 26);
    static final int TRANSACTION_getPackageRedactionNotificationText = (android.os.IBinder.FIRST_CALL_TRANSACTION + 27);
    static final int TRANSACTION_setPersistAllPkgEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 28);
    static final int TRANSACTION_isPersistAllPkgEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 29);
    static final int TRANSACTION_setPkgNREnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 30);
    static final int TRANSACTION_isPkgNREnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 31);
  }
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.n.INotificationManager";
  public java.util.List<github.tornaco.android.thanos.core.n.NotificationRecord> getShowingNotificationRecordsForPackage(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  public boolean hasShowingNotificationRecordsForPackage(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
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
  public void onSetPrimaryClip(android.content.ClipData clip, github.tornaco.android.thanos.core.pm.Pkg caller) throws android.os.RemoteException;
  public void setPackageRedactionNotificationEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean enable) throws android.os.RemoteException;
  public boolean isPackageRedactionNotificationEnabled(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  public void setPackageRedactionNotificationTitle(github.tornaco.android.thanos.core.pm.Pkg pkg, java.lang.String title) throws android.os.RemoteException;
  public java.lang.String getPackageRedactionNotificationTitle(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  public void setPackageRedactionNotificationText(github.tornaco.android.thanos.core.pm.Pkg pkg, java.lang.String text) throws android.os.RemoteException;
  public java.lang.String getPackageRedactionNotificationText(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
  public void setPersistAllPkgEnabled(boolean enable) throws android.os.RemoteException;
  public boolean isPersistAllPkgEnabled() throws android.os.RemoteException;
  public void setPkgNREnabled(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean enable) throws android.os.RemoteException;
  public boolean isPkgNREnabled(github.tornaco.android.thanos.core.pm.Pkg pkg) throws android.os.RemoteException;
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
    static private <T extends android.os.Parcelable> void writeTypedList(
        android.os.Parcel parcel, java.util.List<T> value, int parcelableFlags) {
      if (value == null) {
        parcel.writeInt(-1);
      } else {
        int N = value.size();
        int i = 0;
        parcel.writeInt(N);
        while (i < N) {
    writeTypedObject(parcel, value.get(i), parcelableFlags);
          i++;
        }
      }
    }
  }
}
