/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.secure;
public interface IPrivacyManager extends android.os.IInterface
{
  /** Default implementation for IPrivacyManager. */
  public static class Default implements github.tornaco.android.thanos.core.secure.IPrivacyManager
  {
    @Override public boolean isPrivacyEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setPrivacyEnabled(boolean enabled) throws android.os.RemoteException
    {
    }
    @Override public int getPrivacyDataCheatPkgCount() throws android.os.RemoteException
    {
      return 0;
    }
    @Override public long getPrivacyDataCheatRequestCount() throws android.os.RemoteException
    {
      return 0L;
    }
    @Override public java.lang.String getOriginalDeviceId() throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.lang.String getOriginalLine1Number() throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.lang.String getOriginalSimSerialNumber() throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.lang.String getOriginalAndroidId() throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.lang.String getOriginalImei(int slotIndex) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.lang.String getOriginalMeid(int slotIndex) throws android.os.RemoteException
    {
      return null;
    }
    @Override public int getPhoneCount() throws android.os.RemoteException
    {
      return 0;
    }
    @Override public android.telephony.SubscriptionInfo[] getAccessibleSubscriptionInfoList() throws android.os.RemoteException
    {
      return null;
    }
    @Override public github.tornaco.android.thanos.core.secure.PrivacyCheatRecord[] getPrivacyCheatRecords() throws android.os.RemoteException
    {
      return null;
    }
    @Override public void clearPrivacyCheatRecords() throws android.os.RemoteException
    {
    }
    @Override public boolean addOrUpdateFieldsProfile(github.tornaco.android.thanos.core.secure.field.Fields f) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean deleteFieldsProfile(github.tornaco.android.thanos.core.secure.field.Fields f) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean deleteFieldsProfileById(java.lang.String id) throws android.os.RemoteException
    {
      return false;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.secure.field.Fields> getAllFieldsProfiles() throws android.os.RemoteException
    {
      return null;
    }
    @Override public void selectFieldsProfileForPackage(java.lang.String pkg, java.lang.String profileId) throws android.os.RemoteException
    {
    }
    @Override public java.lang.String getSelectedFieldsProfileIdForPackage(java.lang.String pkg) throws android.os.RemoteException
    {
      return null;
    }
    @Override public github.tornaco.android.thanos.core.secure.field.Fields getSelectedFieldsProfileForPackage(java.lang.String pkg, int checkingOp) throws android.os.RemoteException
    {
      return null;
    }
    @Override public github.tornaco.android.thanos.core.secure.field.Fields getFieldsProfileById(java.lang.String id) throws android.os.RemoteException
    {
      return null;
    }
    @Override public boolean isUidFieldsProfileSelected(int uid) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean isPackageFieldsProfileSelected(java.lang.String pkg) throws android.os.RemoteException
    {
      return false;
    }
    @Override public int getUsageForFieldsProfile(java.lang.String id) throws android.os.RemoteException
    {
      return 0;
    }
    @Override public java.util.List<java.lang.String> getUsagePackagesForFieldsProfile(java.lang.String id) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.lang.String getOriginalSimCountryIso() throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.lang.String getOriginalSimOp(int subId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.lang.String getOriginalSimOpName(int subId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.lang.String getOriginalNetworkCountryIso() throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.lang.String getOriginalNetworkOp(int subId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.lang.String getOriginalNetworkOpName(int subId) throws android.os.RemoteException
    {
      return null;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.secure.IPrivacyManager
  {
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.secure.IPrivacyManager interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.secure.IPrivacyManager asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.secure.IPrivacyManager))) {
        return ((github.tornaco.android.thanos.core.secure.IPrivacyManager)iin);
      }
      return new github.tornaco.android.thanos.core.secure.IPrivacyManager.Stub.Proxy(obj);
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
        case TRANSACTION_isPrivacyEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.isPrivacyEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setPrivacyEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setPrivacyEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_getPrivacyDataCheatPkgCount:
        {
          data.enforceInterface(descriptor);
          int _result = this.getPrivacyDataCheatPkgCount();
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        case TRANSACTION_getPrivacyDataCheatRequestCount:
        {
          data.enforceInterface(descriptor);
          long _result = this.getPrivacyDataCheatRequestCount();
          reply.writeNoException();
          reply.writeLong(_result);
          return true;
        }
        case TRANSACTION_getOriginalDeviceId:
        {
          data.enforceInterface(descriptor);
          java.lang.String _result = this.getOriginalDeviceId();
          reply.writeNoException();
          reply.writeString(_result);
          return true;
        }
        case TRANSACTION_getOriginalLine1Number:
        {
          data.enforceInterface(descriptor);
          java.lang.String _result = this.getOriginalLine1Number();
          reply.writeNoException();
          reply.writeString(_result);
          return true;
        }
        case TRANSACTION_getOriginalSimSerialNumber:
        {
          data.enforceInterface(descriptor);
          java.lang.String _result = this.getOriginalSimSerialNumber();
          reply.writeNoException();
          reply.writeString(_result);
          return true;
        }
        case TRANSACTION_getOriginalAndroidId:
        {
          data.enforceInterface(descriptor);
          java.lang.String _result = this.getOriginalAndroidId();
          reply.writeNoException();
          reply.writeString(_result);
          return true;
        }
        case TRANSACTION_getOriginalImei:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _result = this.getOriginalImei(_arg0);
          reply.writeNoException();
          reply.writeString(_result);
          return true;
        }
        case TRANSACTION_getOriginalMeid:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _result = this.getOriginalMeid(_arg0);
          reply.writeNoException();
          reply.writeString(_result);
          return true;
        }
        case TRANSACTION_getPhoneCount:
        {
          data.enforceInterface(descriptor);
          int _result = this.getPhoneCount();
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        case TRANSACTION_getAccessibleSubscriptionInfoList:
        {
          data.enforceInterface(descriptor);
          android.telephony.SubscriptionInfo[] _result = this.getAccessibleSubscriptionInfoList();
          reply.writeNoException();
          reply.writeTypedArray(_result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          return true;
        }
        case TRANSACTION_getPrivacyCheatRecords:
        {
          data.enforceInterface(descriptor);
          github.tornaco.android.thanos.core.secure.PrivacyCheatRecord[] _result = this.getPrivacyCheatRecords();
          reply.writeNoException();
          reply.writeTypedArray(_result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          return true;
        }
        case TRANSACTION_clearPrivacyCheatRecords:
        {
          data.enforceInterface(descriptor);
          this.clearPrivacyCheatRecords();
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_addOrUpdateFieldsProfile:
        {
          data.enforceInterface(descriptor);
          github.tornaco.android.thanos.core.secure.field.Fields _arg0;
          if ((0!=data.readInt())) {
            _arg0 = github.tornaco.android.thanos.core.secure.field.Fields.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          boolean _result = this.addOrUpdateFieldsProfile(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_deleteFieldsProfile:
        {
          data.enforceInterface(descriptor);
          github.tornaco.android.thanos.core.secure.field.Fields _arg0;
          if ((0!=data.readInt())) {
            _arg0 = github.tornaco.android.thanos.core.secure.field.Fields.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          boolean _result = this.deleteFieldsProfile(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_deleteFieldsProfileById:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.deleteFieldsProfileById(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_getAllFieldsProfiles:
        {
          data.enforceInterface(descriptor);
          java.util.List<github.tornaco.android.thanos.core.secure.field.Fields> _result = this.getAllFieldsProfiles();
          reply.writeNoException();
          reply.writeTypedList(_result);
          return true;
        }
        case TRANSACTION_selectFieldsProfileForPackage:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.selectFieldsProfileForPackage(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_getSelectedFieldsProfileIdForPackage:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _result = this.getSelectedFieldsProfileIdForPackage(_arg0);
          reply.writeNoException();
          reply.writeString(_result);
          return true;
        }
        case TRANSACTION_getSelectedFieldsProfileForPackage:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          github.tornaco.android.thanos.core.secure.field.Fields _result = this.getSelectedFieldsProfileForPackage(_arg0, _arg1);
          reply.writeNoException();
          if ((_result!=null)) {
            reply.writeInt(1);
            _result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          }
          else {
            reply.writeInt(0);
          }
          return true;
        }
        case TRANSACTION_getFieldsProfileById:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          github.tornaco.android.thanos.core.secure.field.Fields _result = this.getFieldsProfileById(_arg0);
          reply.writeNoException();
          if ((_result!=null)) {
            reply.writeInt(1);
            _result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          }
          else {
            reply.writeInt(0);
          }
          return true;
        }
        case TRANSACTION_isUidFieldsProfileSelected:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          boolean _result = this.isUidFieldsProfileSelected(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_isPackageFieldsProfileSelected:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.isPackageFieldsProfileSelected(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_getUsageForFieldsProfile:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _result = this.getUsageForFieldsProfile(_arg0);
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        case TRANSACTION_getUsagePackagesForFieldsProfile:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.util.List<java.lang.String> _result = this.getUsagePackagesForFieldsProfile(_arg0);
          reply.writeNoException();
          reply.writeStringList(_result);
          return true;
        }
        case TRANSACTION_getOriginalSimCountryIso:
        {
          data.enforceInterface(descriptor);
          java.lang.String _result = this.getOriginalSimCountryIso();
          reply.writeNoException();
          reply.writeString(_result);
          return true;
        }
        case TRANSACTION_getOriginalSimOp:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _result = this.getOriginalSimOp(_arg0);
          reply.writeNoException();
          reply.writeString(_result);
          return true;
        }
        case TRANSACTION_getOriginalSimOpName:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _result = this.getOriginalSimOpName(_arg0);
          reply.writeNoException();
          reply.writeString(_result);
          return true;
        }
        case TRANSACTION_getOriginalNetworkCountryIso:
        {
          data.enforceInterface(descriptor);
          java.lang.String _result = this.getOriginalNetworkCountryIso();
          reply.writeNoException();
          reply.writeString(_result);
          return true;
        }
        case TRANSACTION_getOriginalNetworkOp:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _result = this.getOriginalNetworkOp(_arg0);
          reply.writeNoException();
          reply.writeString(_result);
          return true;
        }
        case TRANSACTION_getOriginalNetworkOpName:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _result = this.getOriginalNetworkOpName(_arg0);
          reply.writeNoException();
          reply.writeString(_result);
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements github.tornaco.android.thanos.core.secure.IPrivacyManager
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
      @Override public boolean isPrivacyEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPrivacyEnabled, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().isPrivacyEnabled();
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
      @Override public void setPrivacyEnabled(boolean enabled) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enabled)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPrivacyEnabled, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              getDefaultImpl().setPrivacyEnabled(enabled);
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
      @Override public int getPrivacyDataCheatPkgCount() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getPrivacyDataCheatPkgCount, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().getPrivacyDataCheatPkgCount();
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
      @Override public long getPrivacyDataCheatRequestCount() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        long _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getPrivacyDataCheatRequestCount, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().getPrivacyDataCheatRequestCount();
            }
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
      @Override public java.lang.String getOriginalDeviceId() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getOriginalDeviceId, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().getOriginalDeviceId();
            }
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
      @Override public java.lang.String getOriginalLine1Number() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getOriginalLine1Number, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().getOriginalLine1Number();
            }
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
      @Override public java.lang.String getOriginalSimSerialNumber() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getOriginalSimSerialNumber, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().getOriginalSimSerialNumber();
            }
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
      @Override public java.lang.String getOriginalAndroidId() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getOriginalAndroidId, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().getOriginalAndroidId();
            }
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
      @Override public java.lang.String getOriginalImei(int slotIndex) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(slotIndex);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getOriginalImei, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().getOriginalImei(slotIndex);
            }
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
      @Override public java.lang.String getOriginalMeid(int slotIndex) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(slotIndex);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getOriginalMeid, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().getOriginalMeid(slotIndex);
            }
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
      @Override public int getPhoneCount() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getPhoneCount, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().getPhoneCount();
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
      @Override public android.telephony.SubscriptionInfo[] getAccessibleSubscriptionInfoList() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.telephony.SubscriptionInfo[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAccessibleSubscriptionInfoList, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().getAccessibleSubscriptionInfoList();
            }
          }
          _reply.readException();
          _result = _reply.createTypedArray(android.telephony.SubscriptionInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public github.tornaco.android.thanos.core.secure.PrivacyCheatRecord[] getPrivacyCheatRecords() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.secure.PrivacyCheatRecord[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getPrivacyCheatRecords, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().getPrivacyCheatRecords();
            }
          }
          _reply.readException();
          _result = _reply.createTypedArray(github.tornaco.android.thanos.core.secure.PrivacyCheatRecord.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void clearPrivacyCheatRecords() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_clearPrivacyCheatRecords, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              getDefaultImpl().clearPrivacyCheatRecords();
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
      @Override public boolean addOrUpdateFieldsProfile(github.tornaco.android.thanos.core.secure.field.Fields f) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((f!=null)) {
            _data.writeInt(1);
            f.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_addOrUpdateFieldsProfile, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().addOrUpdateFieldsProfile(f);
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
      @Override public boolean deleteFieldsProfile(github.tornaco.android.thanos.core.secure.field.Fields f) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((f!=null)) {
            _data.writeInt(1);
            f.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_deleteFieldsProfile, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().deleteFieldsProfile(f);
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
      @Override public boolean deleteFieldsProfileById(java.lang.String id) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(id);
          boolean _status = mRemote.transact(Stub.TRANSACTION_deleteFieldsProfileById, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().deleteFieldsProfileById(id);
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
      @Override public java.util.List<github.tornaco.android.thanos.core.secure.field.Fields> getAllFieldsProfiles() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.secure.field.Fields> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAllFieldsProfiles, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().getAllFieldsProfiles();
            }
          }
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.secure.field.Fields.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void selectFieldsProfileForPackage(java.lang.String pkg, java.lang.String profileId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          _data.writeString(profileId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_selectFieldsProfileForPackage, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              getDefaultImpl().selectFieldsProfileForPackage(pkg, profileId);
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
      @Override public java.lang.String getSelectedFieldsProfileIdForPackage(java.lang.String pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getSelectedFieldsProfileIdForPackage, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().getSelectedFieldsProfileIdForPackage(pkg);
            }
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
      @Override public github.tornaco.android.thanos.core.secure.field.Fields getSelectedFieldsProfileForPackage(java.lang.String pkg, int checkingOp) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.secure.field.Fields _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          _data.writeInt(checkingOp);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getSelectedFieldsProfileForPackage, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().getSelectedFieldsProfileForPackage(pkg, checkingOp);
            }
          }
          _reply.readException();
          if ((0!=_reply.readInt())) {
            _result = github.tornaco.android.thanos.core.secure.field.Fields.CREATOR.createFromParcel(_reply);
          }
          else {
            _result = null;
          }
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public github.tornaco.android.thanos.core.secure.field.Fields getFieldsProfileById(java.lang.String id) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.secure.field.Fields _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(id);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getFieldsProfileById, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().getFieldsProfileById(id);
            }
          }
          _reply.readException();
          if ((0!=_reply.readInt())) {
            _result = github.tornaco.android.thanos.core.secure.field.Fields.CREATOR.createFromParcel(_reply);
          }
          else {
            _result = null;
          }
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean isUidFieldsProfileSelected(int uid) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(uid);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isUidFieldsProfileSelected, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().isUidFieldsProfileSelected(uid);
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
      @Override public boolean isPackageFieldsProfileSelected(java.lang.String pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPackageFieldsProfileSelected, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().isPackageFieldsProfileSelected(pkg);
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
      @Override public int getUsageForFieldsProfile(java.lang.String id) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(id);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getUsageForFieldsProfile, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().getUsageForFieldsProfile(id);
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
      @Override public java.util.List<java.lang.String> getUsagePackagesForFieldsProfile(java.lang.String id) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<java.lang.String> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(id);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getUsagePackagesForFieldsProfile, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().getUsagePackagesForFieldsProfile(id);
            }
          }
          _reply.readException();
          _result = _reply.createStringArrayList();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.lang.String getOriginalSimCountryIso() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getOriginalSimCountryIso, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().getOriginalSimCountryIso();
            }
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
      @Override public java.lang.String getOriginalSimOp(int subId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(subId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getOriginalSimOp, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().getOriginalSimOp(subId);
            }
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
      @Override public java.lang.String getOriginalSimOpName(int subId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(subId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getOriginalSimOpName, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().getOriginalSimOpName(subId);
            }
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
      @Override public java.lang.String getOriginalNetworkCountryIso() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getOriginalNetworkCountryIso, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().getOriginalNetworkCountryIso();
            }
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
      @Override public java.lang.String getOriginalNetworkOp(int subId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(subId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getOriginalNetworkOp, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().getOriginalNetworkOp(subId);
            }
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
      @Override public java.lang.String getOriginalNetworkOpName(int subId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(subId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getOriginalNetworkOpName, _data, _reply, 0);
          if (!_status) {
            if (getDefaultImpl() != null) {
              return getDefaultImpl().getOriginalNetworkOpName(subId);
            }
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
      public static github.tornaco.android.thanos.core.secure.IPrivacyManager sDefaultImpl;
    }
    static final int TRANSACTION_isPrivacyEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_setPrivacyEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_getPrivacyDataCheatPkgCount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_getPrivacyDataCheatRequestCount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_getOriginalDeviceId = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_getOriginalLine1Number = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_getOriginalSimSerialNumber = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_getOriginalAndroidId = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    static final int TRANSACTION_getOriginalImei = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
    static final int TRANSACTION_getOriginalMeid = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
    static final int TRANSACTION_getPhoneCount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
    static final int TRANSACTION_getAccessibleSubscriptionInfoList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
    static final int TRANSACTION_getPrivacyCheatRecords = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
    static final int TRANSACTION_clearPrivacyCheatRecords = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
    static final int TRANSACTION_addOrUpdateFieldsProfile = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
    static final int TRANSACTION_deleteFieldsProfile = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
    static final int TRANSACTION_deleteFieldsProfileById = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
    static final int TRANSACTION_getAllFieldsProfiles = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
    static final int TRANSACTION_selectFieldsProfileForPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
    static final int TRANSACTION_getSelectedFieldsProfileIdForPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
    static final int TRANSACTION_getSelectedFieldsProfileForPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
    static final int TRANSACTION_getFieldsProfileById = (android.os.IBinder.FIRST_CALL_TRANSACTION + 21);
    static final int TRANSACTION_isUidFieldsProfileSelected = (android.os.IBinder.FIRST_CALL_TRANSACTION + 22);
    static final int TRANSACTION_isPackageFieldsProfileSelected = (android.os.IBinder.FIRST_CALL_TRANSACTION + 23);
    static final int TRANSACTION_getUsageForFieldsProfile = (android.os.IBinder.FIRST_CALL_TRANSACTION + 24);
    static final int TRANSACTION_getUsagePackagesForFieldsProfile = (android.os.IBinder.FIRST_CALL_TRANSACTION + 25);
    static final int TRANSACTION_getOriginalSimCountryIso = (android.os.IBinder.FIRST_CALL_TRANSACTION + 26);
    static final int TRANSACTION_getOriginalSimOp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 27);
    static final int TRANSACTION_getOriginalSimOpName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 28);
    static final int TRANSACTION_getOriginalNetworkCountryIso = (android.os.IBinder.FIRST_CALL_TRANSACTION + 29);
    static final int TRANSACTION_getOriginalNetworkOp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 30);
    static final int TRANSACTION_getOriginalNetworkOpName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 31);
    public static boolean setDefaultImpl(github.tornaco.android.thanos.core.secure.IPrivacyManager impl) {
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
    public static github.tornaco.android.thanos.core.secure.IPrivacyManager getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.secure.IPrivacyManager";
  public boolean isPrivacyEnabled() throws android.os.RemoteException;
  public void setPrivacyEnabled(boolean enabled) throws android.os.RemoteException;
  public int getPrivacyDataCheatPkgCount() throws android.os.RemoteException;
  public long getPrivacyDataCheatRequestCount() throws android.os.RemoteException;
  public java.lang.String getOriginalDeviceId() throws android.os.RemoteException;
  public java.lang.String getOriginalLine1Number() throws android.os.RemoteException;
  public java.lang.String getOriginalSimSerialNumber() throws android.os.RemoteException;
  public java.lang.String getOriginalAndroidId() throws android.os.RemoteException;
  public java.lang.String getOriginalImei(int slotIndex) throws android.os.RemoteException;
  public java.lang.String getOriginalMeid(int slotIndex) throws android.os.RemoteException;
  public int getPhoneCount() throws android.os.RemoteException;
  public android.telephony.SubscriptionInfo[] getAccessibleSubscriptionInfoList() throws android.os.RemoteException;
  public github.tornaco.android.thanos.core.secure.PrivacyCheatRecord[] getPrivacyCheatRecords() throws android.os.RemoteException;
  public void clearPrivacyCheatRecords() throws android.os.RemoteException;
  public boolean addOrUpdateFieldsProfile(github.tornaco.android.thanos.core.secure.field.Fields f) throws android.os.RemoteException;
  public boolean deleteFieldsProfile(github.tornaco.android.thanos.core.secure.field.Fields f) throws android.os.RemoteException;
  public boolean deleteFieldsProfileById(java.lang.String id) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.secure.field.Fields> getAllFieldsProfiles() throws android.os.RemoteException;
  public void selectFieldsProfileForPackage(java.lang.String pkg, java.lang.String profileId) throws android.os.RemoteException;
  public java.lang.String getSelectedFieldsProfileIdForPackage(java.lang.String pkg) throws android.os.RemoteException;
  public github.tornaco.android.thanos.core.secure.field.Fields getSelectedFieldsProfileForPackage(java.lang.String pkg, int checkingOp) throws android.os.RemoteException;
  public github.tornaco.android.thanos.core.secure.field.Fields getFieldsProfileById(java.lang.String id) throws android.os.RemoteException;
  public boolean isUidFieldsProfileSelected(int uid) throws android.os.RemoteException;
  public boolean isPackageFieldsProfileSelected(java.lang.String pkg) throws android.os.RemoteException;
  public int getUsageForFieldsProfile(java.lang.String id) throws android.os.RemoteException;
  public java.util.List<java.lang.String> getUsagePackagesForFieldsProfile(java.lang.String id) throws android.os.RemoteException;
  public java.lang.String getOriginalSimCountryIso() throws android.os.RemoteException;
  public java.lang.String getOriginalSimOp(int subId) throws android.os.RemoteException;
  public java.lang.String getOriginalSimOpName(int subId) throws android.os.RemoteException;
  public java.lang.String getOriginalNetworkCountryIso() throws android.os.RemoteException;
  public java.lang.String getOriginalNetworkOp(int subId) throws android.os.RemoteException;
  public java.lang.String getOriginalNetworkOpName(int subId) throws android.os.RemoteException;
}
