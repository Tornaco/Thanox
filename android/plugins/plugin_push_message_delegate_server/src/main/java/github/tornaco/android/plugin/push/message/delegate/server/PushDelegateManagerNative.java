package github.tornaco.android.plugin.push.message.delegate.server;

import android.os.IBinder;
import android.os.RemoteException;

import github.tornaco.android.plugin.push.message.delegate.IPushDelegateManager;
import github.tornaco.android.plugin.push.message.delegate.Logging;
import github.tornaco.android.thanos.core.IThanos;
import github.tornaco.android.thanos.core.app.ThanosManagerNative;
import util.Singleton;

public class PushDelegateManagerNative {

    private static Singleton<IPushDelegateManager> singleton = new Singleton<IPushDelegateManager>() {
        @Override
        protected IPushDelegateManager create() {
            if (!ThanosManagerNative.isServiceInstalled()) {
                return null;
            }
            IThanos thanos = ThanosManagerNative.getDefault();
            try {
                IBinder binder = thanos.getServiceManager().getService("push_delegate");
                return IPushDelegateManager.Stub.asInterface(binder);
            } catch (RemoteException e) {
                Logging.logE("PushDelegateManagerNative-" + e);
            }
            return null;
        }
    };

    public static IPushDelegateManager getDefault() {
        return singleton.get();
    }

    public static boolean isServiceInstalled() {
        return getDefault() != null;
    }
}
