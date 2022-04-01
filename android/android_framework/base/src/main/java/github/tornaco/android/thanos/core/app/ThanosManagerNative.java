package github.tornaco.android.thanos.core.app;

import static github.tornaco.android.thanos.core.app.ThanosManager.PROXIED_ANDROID_SERVICE_NAME;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;

import com.elvishew.xlog.XLog;

import github.tornaco.android.thanos.core.IThanos;
import github.tornaco.android.thanos.core.T;
import util.Singleton;

public class ThanosManagerNative {
    private static IThanos localService;

    public static void setLocalService(IThanos localService) {
        XLog.w("ThanosManagerNative, setLocalService: " + localService);
        ThanosManagerNative.localService = localService;
    }

    private static final Singleton<IThanos> sIThanosSingleton = new Singleton<IThanos>() {
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

            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                IBinder backup = ServiceManager.getService(PROXIED_ANDROID_SERVICE_NAME);
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
