package github.tornaco.thanox.android.server.patch.framework.hooks;

import android.content.Context;

import com.elvishew.xlog.XLog;

import github.tornaco.thanox.android.server.patch.framework.LocalServices;
import util.XposedHelpers;

public class SystemServiceContextHooks {
    public static void install(ClassLoader classLoader) {
        installContextHooksForAllSystemServices(classLoader);
    }

    private static void installContextHooksForAllSystemServices(ClassLoader classLoader) {
        XLog.w("SystemServiceContextHooks installContextHooksForAllSystemServices");
        try {
            new LocalServices(classLoader).allServices(SystemServiceContextHooks::installContextHooksForSystemService);
        } catch (Throwable e) {
            XLog.e("SystemServiceContextHooks installContextHooksForAllSystemServices error ", e);
        }
    }

    private static void installContextHooksForSystemService(Object service) {
        XLog.w("SystemServiceContextHooks installContextHooksForSystemService: %s", service);

        Context originalContext = (Context) XposedHelpers.getObjectField(service, "mContext");
        XLog.w("SystemServiceContextHooks installContextHooksForSystemService, originalContext: %s", originalContext);

        Context proxyContext = new ContextProxy(originalContext, service.getClass().getSimpleName());
        XposedHelpers.setObjectField(service, "mContext", proxyContext);
        XLog.w("SystemServiceContextHooks installContextHooksForSystemService done.");
    }
}
