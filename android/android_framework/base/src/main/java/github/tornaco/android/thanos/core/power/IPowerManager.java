/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.power;
public interface IPowerManager extends android.os.IInterface
{
  /** Default implementation for IPowerManager. */
  public static class Default implements github.tornaco.android.thanos.core.power.IPowerManager
  {
    @Override public void reboot() throws android.os.RemoteException
    {
    }
    @Override public void softReboot() throws android.os.RemoteException
    {
    }
    @Override public void goToSleep(long delay) throws android.os.RemoteException
    {
    }
    @Override public void setPowerSaveModeEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isPowerSaveModeEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.power.SeenWakeLock> getSeenWakeLocks(boolean includeHistory) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.power.SeenWakeLock> getSeenWakeLocksForPkg(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean includeHistory) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.power.WakeLockStats> getSeenWakeLocksStats(boolean includeHistory, boolean heldOnly) throws android.os.RemoteException
    {
      return null;
    }
    @Override public boolean isWakeLockHeld(github.tornaco.android.thanos.core.power.SeenWakeLock wakelock) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void dump(github.tornaco.android.thanos.core.IPrinter p) throws android.os.RemoteException
    {
    }
    @Override public void wakeUp(long delay) throws android.os.RemoteException
    {
    }
    @Override public void setBrightness(int level) throws android.os.RemoteException
    {
    }
    @Override public int getBrightness() throws android.os.RemoteException
    {
      return 0;
    }
    @Override public void setAutoBrightnessEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isAutoBrightnessEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean isWakeLockBlockerEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setWakeLockBlockerEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public void setBlockWakeLock(github.tornaco.android.thanos.core.power.SeenWakeLock wl, boolean block) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.power.IPowerManager
  {
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.power.IPowerManager interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.power.IPowerManager asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.power.IPowerManager))) {
        return ((github.tornaco.android.thanos.core.power.IPowerManager)iin);
      }
      return new github.tornaco.android.thanos.core.power.IPowerManager.Stub.Proxy(obj);
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
        case TRANSACTION_reboot:
        {
          this.reboot();
          reply.writeNoException();
          break;
        }
        case TRANSACTION_softReboot:
        {
          this.softReboot();
          reply.writeNoException();
          break;
        }
        case TRANSACTION_goToSleep:
        {
          long _arg0;
          _arg0 = data.readLong();
          this.goToSleep(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_setPowerSaveModeEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setPowerSaveModeEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isPowerSaveModeEnabled:
        {
          boolean _result = this.isPowerSaveModeEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_getSeenWakeLocks:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          java.util.List<github.tornaco.android.thanos.core.power.SeenWakeLock> _result = this.getSeenWakeLocks(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getSeenWakeLocksForPkg:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          java.util.List<github.tornaco.android.thanos.core.power.SeenWakeLock> _result = this.getSeenWakeLocksForPkg(_arg0, _arg1);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getSeenWakeLocksStats:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          java.util.List<github.tornaco.android.thanos.core.power.WakeLockStats> _result = this.getSeenWakeLocksStats(_arg0, _arg1);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_isWakeLockHeld:
        {
          github.tornaco.android.thanos.core.power.SeenWakeLock _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.power.SeenWakeLock.CREATOR);
          boolean _result = this.isWakeLockHeld(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_dump:
        {
          github.tornaco.android.thanos.core.IPrinter _arg0;
          _arg0 = github.tornaco.android.thanos.core.IPrinter.Stub.asInterface(data.readStrongBinder());
          this.dump(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_wakeUp:
        {
          long _arg0;
          _arg0 = data.readLong();
          this.wakeUp(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_setBrightness:
        {
          int _arg0;
          _arg0 = data.readInt();
          this.setBrightness(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getBrightness:
        {
          int _result = this.getBrightness();
          reply.writeNoException();
          reply.writeInt(_result);
          break;
        }
        case TRANSACTION_setAutoBrightnessEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setAutoBrightnessEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isAutoBrightnessEnabled:
        {
          boolean _result = this.isAutoBrightnessEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_isWakeLockBlockerEnabled:
        {
          boolean _result = this.isWakeLockBlockerEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setWakeLockBlockerEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setWakeLockBlockerEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_setBlockWakeLock:
        {
          github.tornaco.android.thanos.core.power.SeenWakeLock _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.power.SeenWakeLock.CREATOR);
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setBlockWakeLock(_arg0, _arg1);
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
    private static class Proxy implements github.tornaco.android.thanos.core.power.IPowerManager
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
      @Override public void reboot() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_reboot, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void softReboot() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_softReboot, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void goToSleep(long delay) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeLong(delay);
          boolean _status = mRemote.transact(Stub.TRANSACTION_goToSleep, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setPowerSaveModeEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPowerSaveModeEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isPowerSaveModeEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPowerSaveModeEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.power.SeenWakeLock> getSeenWakeLocks(boolean includeHistory) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.power.SeenWakeLock> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((includeHistory)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_getSeenWakeLocks, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.power.SeenWakeLock.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.power.SeenWakeLock> getSeenWakeLocksForPkg(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean includeHistory) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.power.SeenWakeLock> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          _data.writeInt(((includeHistory)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_getSeenWakeLocksForPkg, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.power.SeenWakeLock.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.power.WakeLockStats> getSeenWakeLocksStats(boolean includeHistory, boolean heldOnly) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.power.WakeLockStats> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((includeHistory)?(1):(0)));
          _data.writeInt(((heldOnly)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_getSeenWakeLocksStats, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.power.WakeLockStats.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean isWakeLockHeld(github.tornaco.android.thanos.core.power.SeenWakeLock wakelock) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, wakelock, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isWakeLockHeld, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void dump(github.tornaco.android.thanos.core.IPrinter p) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(p);
          boolean _status = mRemote.transact(Stub.TRANSACTION_dump, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void wakeUp(long delay) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeLong(delay);
          boolean _status = mRemote.transact(Stub.TRANSACTION_wakeUp, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setBrightness(int level) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(level);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setBrightness, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public int getBrightness() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getBrightness, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setAutoBrightnessEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setAutoBrightnessEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isAutoBrightnessEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isAutoBrightnessEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean isWakeLockBlockerEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isWakeLockBlockerEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setWakeLockBlockerEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setWakeLockBlockerEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setBlockWakeLock(github.tornaco.android.thanos.core.power.SeenWakeLock wl, boolean block) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, wl, 0);
          _data.writeInt(((block)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setBlockWakeLock, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
    }
    static final int TRANSACTION_reboot = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_softReboot = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_goToSleep = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_setPowerSaveModeEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_isPowerSaveModeEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_getSeenWakeLocks = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_getSeenWakeLocksForPkg = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_getSeenWakeLocksStats = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    static final int TRANSACTION_isWakeLockHeld = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
    static final int TRANSACTION_dump = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
    static final int TRANSACTION_wakeUp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
    static final int TRANSACTION_setBrightness = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
    static final int TRANSACTION_getBrightness = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
    static final int TRANSACTION_setAutoBrightnessEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
    static final int TRANSACTION_isAutoBrightnessEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
    static final int TRANSACTION_isWakeLockBlockerEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
    static final int TRANSACTION_setWakeLockBlockerEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
    static final int TRANSACTION_setBlockWakeLock = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
  }
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.power.IPowerManager";
  public void reboot() throws android.os.RemoteException;
  public void softReboot() throws android.os.RemoteException;
  public void goToSleep(long delay) throws android.os.RemoteException;
  public void setPowerSaveModeEnabled(boolean enable) throws android.os.RemoteException;
  public boolean isPowerSaveModeEnabled() throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.power.SeenWakeLock> getSeenWakeLocks(boolean includeHistory) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.power.SeenWakeLock> getSeenWakeLocksForPkg(github.tornaco.android.thanos.core.pm.Pkg pkg, boolean includeHistory) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.power.WakeLockStats> getSeenWakeLocksStats(boolean includeHistory, boolean heldOnly) throws android.os.RemoteException;
  public boolean isWakeLockHeld(github.tornaco.android.thanos.core.power.SeenWakeLock wakelock) throws android.os.RemoteException;
  public void dump(github.tornaco.android.thanos.core.IPrinter p) throws android.os.RemoteException;
  public void wakeUp(long delay) throws android.os.RemoteException;
  public void setBrightness(int level) throws android.os.RemoteException;
  public int getBrightness() throws android.os.RemoteException;
  public void setAutoBrightnessEnabled(boolean enable) throws android.os.RemoteException;
  public boolean isAutoBrightnessEnabled() throws android.os.RemoteException;
  public boolean isWakeLockBlockerEnabled() throws android.os.RemoteException;
  public void setWakeLockBlockerEnabled(boolean enable) throws android.os.RemoteException;
  public void setBlockWakeLock(github.tornaco.android.thanos.core.power.SeenWakeLock wl, boolean block) throws android.os.RemoteException;
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
