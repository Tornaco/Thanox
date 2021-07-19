package github.tornaco.android.thanos.core.app;

import android.content.Context;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;

import com.elvishew.xlog.XLog;

import github.tornaco.android.thanos.core.IThanos;
import github.tornaco.android.thanos.core.T;
import lombok.Setter;
import lombok.val;
import util.Singleton;

public class ThanosManagerNative {
    @Setter
    private static IThanos localService;

    private static Singleton<IThanos> sIThanosSingleton = new Singleton<IThanos>() {
        @Override
        protected IThanos create() {
            if (localService != null) {
                return localService;
            }
            IThanos thanos = IThanos.Stub.asInterface(
                    ServiceManager.getService(T.serviceInstallName()));
            if (thanos != null) {
                return thanos;
            }
            XLog.w("Get Thanos from IPC_TRANS_CODE_THANOS_SERVER");

            val data = Parcel.obtain();
            val reply = Parcel.obtain();
            try {
                val backup = ServiceManager.getService(Context.DROPBOX_SERVICE);
                if (backup == null) {
                    XLog.w("Get Thanos from IPC_TRANS_CODE_THANOS_SERVER, service is null.");
                    return null;
                }
                data.writeInterfaceToken(IThanos.class.getName());
                backup.transact(ThanosManager.IPC_TRANS_CODE_THANOS_SERVER, data, reply, 0);
                IBinder binder = reply.readStrongBinder();
                XLog.d("Get Thanos from IPC_TRANS_CODE_THANOS_SERVER: %s", binder);
                return IThanos.Stub.asInterface(binder);
            } catch (RemoteException e) {
                XLog.e("Get Thanos from IPC_TRANS_CODE_THANOS_SERVER err", e);
            } finally {
                data.recycle();
                reply.recycle();
            }
            return null;
        }
    };

    public static IThanos getDefault() {
        return sIThanosSingleton.get();
    }

    public static IThanos getLocalService() {
        return localService;
    }

    public static boolean isServiceInstalled() {
        return getDefault() != null;
    }
}
