package github.tornaco.android.thanox.magisk.bridge.proxy.content;

import android.content.Context;
import android.os.IBinder;
import android.os.Parcel;

import com.elvishew.xlog.XLog;

import github.tornaco.android.thanos.core.IThanos;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.app.ThanosManagerNative;
import github.tornaco.android.thanox.magisk.bridge.proxy.BinderProxy;
import github.tornaco.android.thanox.magisk.bridge.proxy.ProxyProvider;

public class DropBoxManagerProxyProvider implements ProxyProvider {
    @Override
    public IBinder provide(IBinder legacyBinder) {
        XLog.v("DropBoxManagerProxyProvider onTransact");
        return new BinderProxy(legacyBinder, new BinderProxy.InvocationHandler() {
            @Override
            public Boolean onTransact(int code, Parcel data, Parcel reply, int flags) {
                if (code == ThanosManager.IPC_TRANS_CODE_THANOS_SERVER) {
                    XLog.d("DropBoxManagerProxyProvider IPC_TRANS_CODE_THANOS_SERVER");
                    IThanos thanos = ThanosManagerNative.getLocalService();
                    if (thanos == null) {
                        XLog.d("DropBoxManagerProxyProvider IPC_TRANS_CODE_THANOS_SERVER, thanos == null");
                        return null;
                    }
                    data.enforceInterface(IThanos.class.getName());
                    reply.writeStrongBinder(thanos.asBinder());
                    XLog.d("DropBoxManagerProxyProvider IPC_TRANS_CODE_THANOS_SERVER, writeStrongBinder");
                    return true;
                }
                return null;
            }
        });
    }

    @Override
    public boolean isForService(String name) {
        return Context.DROPBOX_SERVICE.equals(name);
    }

}
