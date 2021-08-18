package github.tornaco.thanox.android.server.patch.framework.hooks;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Binder;

import com.android.server.SystemService;
import com.elvishew.xlog.XLog;

import github.tornaco.android.thanos.core.util.PkgUtils;
import github.tornaco.thanox.android.server.patch.framework.LocalServices;
import util.Consumer;
import util.XposedHelpers;

public class SystemServiceContextHooks {
    public static void install() {
        installContextHooksForAllSystemServices();
    }

    private static void installContextHooksForAllSystemServices() {
        XLog.i("SystemServiceContextHooks installContextHooksForAllSystemServices");
        try {
            LocalServices.allServices(new Consumer<SystemService>() {
                @Override
                public void accept(SystemService systemService) {
                    installContextHooksForSystemService(systemService);
                }
            });
        } catch (Throwable e) {
            XLog.e("SystemServiceContextHooks installContextHooksForAllSystemServices error ", e);
        }
    }

    private static void installContextHooksForSystemService(SystemService service) {
        XLog.i("SystemServiceContextHooks installContextHooksForSystemService: %s", service);

        Context originalContext = (Context) XposedHelpers.getObjectField(service, "mContext");
        XLog.i("SystemServiceContextHooks installContextHooksForSystemService, originalContext: %s", originalContext);

        Context proxyContext = new ContextProxy(originalContext, service.getClass().getSimpleName());
        XposedHelpers.setObjectField(service, "mContext", proxyContext);
        XLog.i("SystemServiceContextHooks installContextHooksForSystemService done.");
    }

    private static class ContextProxy extends ContextWrapper {
        private final String serviceName;

        public ContextProxy(Context base, String serviceName) {
            super(base);
            this.serviceName = serviceName;
        }

        @Override
        public void enforceCallingPermission(String permission, String message) {
            XLog.i("SystemServiceContextHooks enforceCallingPermission@%s %s %s", serviceName, permission, message);
            long callingUid = Binder.getCallingUid();
            if (permission.equals(Manifest.permission.CHANGE_APP_IDLE_STATE) && PkgUtils.isSystemCall(callingUid)) {
                XLog.w("SystemServiceContextHooks, grant CHANGE_APP_IDLE_STATE fro uid: %s", callingUid);
                return;
            }
            super.enforceCallingPermission(permission, message);
        }
    }
}
