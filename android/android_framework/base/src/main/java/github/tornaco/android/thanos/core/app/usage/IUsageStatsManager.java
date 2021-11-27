/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.app.usage;
public interface IUsageStatsManager extends android.os.IInterface
{
  /** Default implementation for IUsageStatsManager. */
  public static class Default implements github.tornaco.android.thanos.core.app.usage.IUsageStatsManager
  {
    @Override public java.util.List<android.app.usage.UsageStats> queryUsageStats(int intervalType, long beginTime, long endTime) throws android.os.RemoteException
    {
      return null;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.app.usage.IUsageStatsManager
  {
    private static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.app.usage.IUsageStatsManager";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.app.usage.IUsageStatsManager interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.app.usage.IUsageStatsManager asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.app.usage.IUsageStatsManager))) {
        return ((github.tornaco.android.thanos.core.app.usage.IUsageStatsManager)iin);
      }
      return new github.tornaco.android.thanos.core.app.usage.IUsageStatsManager.Stub.Proxy(obj);
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
        case TRANSACTION_queryUsageStats:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          long _arg1;
          _arg1 = data.readLong();
          long _arg2;
          _arg2 = data.readLong();
          java.util.List<android.app.usage.UsageStats> _result = this.queryUsageStats(_arg0, _arg1, _arg2);
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
    private static class Proxy implements github.tornaco.android.thanos.core.app.usage.IUsageStatsManager
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
      @Override public java.util.List<android.app.usage.UsageStats> queryUsageStats(int intervalType, long beginTime, long endTime) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<android.app.usage.UsageStats> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(intervalType);
          _data.writeLong(beginTime);
          _data.writeLong(endTime);
          boolean _status = mRemote.transact(Stub.TRANSACTION_queryUsageStats, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().queryUsageStats(intervalType, beginTime, endTime);
          }
          _reply.readException();
          _result = _reply.createTypedArrayList(android.app.usage.UsageStats.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      public static github.tornaco.android.thanos.core.app.usage.IUsageStatsManager sDefaultImpl;
    }
    static final int TRANSACTION_queryUsageStats = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    public static boolean setDefaultImpl(github.tornaco.android.thanos.core.app.usage.IUsageStatsManager impl) {
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
    public static github.tornaco.android.thanos.core.app.usage.IUsageStatsManager getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public java.util.List<android.app.usage.UsageStats> queryUsageStats(int intervalType, long beginTime, long endTime) throws android.os.RemoteException;
}
