package github.tornaco.android.thanos.core.app;

import static github.tornaco.android.thanos.core.app.ThanosManager.PROXIED_ANDROID_SERVICE_NAME;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ParceledListSlice;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;

import com.android.internal.appwidget.IAppWidgetService;
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

    private static final Singleton<IThanos> sIThanosSingleton = new Singleton<>() {
        @Override
        protected IThanos create() {
            if (localService != null) {
                return localService;
            }

            // Solution 1
            IThanos thanos = IThanos.Stub.asInterface(
                    ServiceManager.getService(T.serviceInstallName()));
            if (thanos != null) {
                XLog.w("ThanosManagerNative serviceInstallName Use it.");
                return thanos;
            }

            // Solution 2
            try {
                IAppWidgetService appWidgetService = IAppWidgetService.Stub.asInterface(ServiceManager.getService(Context.APPWIDGET_SERVICE));
                ParceledListSlice list = appWidgetService.startListening(null, "thanox", 6, new int[]{2, 0, 2, 3});
                XLog.d("ThanosManagerNative IAppWidgetService thanox list: " + list);
                Bundle bundle = (Bundle) list.getList().get(0);
                IBinder thanox = bundle.getBinder("thanox-binder");
                XLog.d("ThanosManagerNative IAppWidgetService thanox binder: " + thanox);
                thanos = IThanos.Stub.asInterface(thanox);
                if (thanos != null) {
                    XLog.w("ThanosManagerNative IAppWidgetService Use it.");
                    return thanos;
                }
            } catch (Throwable e) {
                XLog.e(e, "ThanosManagerNativecall IAppWidgetService");
            }

            // Solution 3
            try {
                ClipboardManager clipboardManager = (ClipboardManager) AppGlobals.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = clipboardManager.getPrimaryClip();
                int clipCount = clipData.getItemCount();
                ClipData.Item lastItem = clipData.getItemAt(clipCount - 1);
                Intent intent = lastItem.getIntent();
                Bundle bundle = intent.getBundleExtra("thanox-bundle");
                IBinder thanox = bundle.getBinder("thanox-binder");
                XLog.d("ThanosManagerNative ClipboardService thanox binder: " + thanox);
                thanos = IThanos.Stub.asInterface(thanox);
                if (thanos != null) {
                    XLog.w("ThanosManagerNative ClipboardService Use it.");
                    return thanos;
                }
            } catch (Throwable e) {
                XLog.e(e, "ThanosManagerNative call ClipboardService");
            }

            // Solution 4
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                IBinder backup = ServiceManager.getService(PROXIED_ANDROID_SERVICE_NAME);
                if (backup == null) {
                    XLog.w("ThanosManagerNative Get Thanos from IPC_TRANS_CODE_THANOS_SERVER, service is null.");
                    return null;
                }
                data.writeInterfaceToken(IThanos.class.getName());
                backup.transact(ThanosManager.IPC_TRANS_CODE_THANOS_SERVER, data, reply, 0);
                IBinder binder = reply.readStrongBinder();
                XLog.w("ThanosManagerNative Get Thanos from IPC_TRANS_CODE_THANOS_SERVER: %s", binder);
                return IThanos.Stub.asInterface(binder);
            } catch (RemoteException e) {
                XLog.e("ThanosManagerNative Get Thanos from IPC_TRANS_CODE_THANOS_SERVER err", e);
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
