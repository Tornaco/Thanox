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
    @Override public java.util.Map<java.lang.String,android.app.usage.UsageStats> queryAndAggregateUsageStats(long beginTime, long endTime) throws android.os.RemoteException
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
        case TRANSACTION_queryUsageStats:
        {
          int _arg0;
          _arg0 = data.readInt();
          long _arg1;
          _arg1 = data.readLong();
          long _arg2;
          _arg2 = data.readLong();
          java.util.List<android.app.usage.UsageStats> _result = this.queryUsageStats(_arg0, _arg1, _arg2);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_queryAndAggregateUsageStats:
        {
          long _arg0;
          _arg0 = data.readLong();
          long _arg1;
          _arg1 = data.readLong();
          java.util.Map<java.lang.String,android.app.usage.UsageStats> _result = this.queryAndAggregateUsageStats(_arg0, _arg1);
          reply.writeNoException();
          if (_result == null) {
            reply.writeInt(-1);
          } else {
            reply.writeInt(_result.size());
            _result.forEach((k, v) -> {
              reply.writeString(k);
              _Parcel.writeTypedObject(reply, v, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
            });
          }
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
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
          _reply.readException();
          _result = _reply.createTypedArrayList(android.app.usage.UsageStats.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.Map<java.lang.String,android.app.usage.UsageStats> queryAndAggregateUsageStats(long beginTime, long endTime) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.Map<java.lang.String,android.app.usage.UsageStats> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeLong(beginTime);
          _data.writeLong(endTime);
          boolean _status = mRemote.transact(Stub.TRANSACTION_queryAndAggregateUsageStats, _data, _reply, 0);
          _reply.readException();
          {
            int N = _reply.readInt();
            _result = N < 0 ? null : new java.util.HashMap<>();
            java.util.stream.IntStream.range(0, N).forEach(i -> {
              String k = _reply.readString();
              android.app.usage.UsageStats v;
              v = _Parcel.readTypedObject(_reply, android.app.usage.UsageStats.CREATOR);
              _result.put(k, v);
            });
          }
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
    }
    static final int TRANSACTION_queryUsageStats = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_queryAndAggregateUsageStats = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
  }
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.app.usage.IUsageStatsManager";
  public java.util.List<android.app.usage.UsageStats> queryUsageStats(int intervalType, long beginTime, long endTime) throws android.os.RemoteException;
  public java.util.Map<java.lang.String,android.app.usage.UsageStats> queryAndAggregateUsageStats(long beginTime, long endTime) throws android.os.RemoteException;
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
