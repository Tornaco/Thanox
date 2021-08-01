package github.tornaco.thanox.android.server.patch.framework;

import com.elvishew.xlog.XLog;

import github.tornaco.thanox.android.server.patch.framework.hooks.AMSHooks;

public class SystemServerHooks {

    public static void install() {
        dumpLockGuards();
        AMSHooks.install();
    }

    private static void dumpLockGuards() {
        for (int i = 0; i < LockGuard.INDEX_DPMS; i++) {
            Object service = LockGuard.retrieveServices(i);
            XLog.i("SystemServerHooks, LockGuard service: %s", service);
        }
    }
}
