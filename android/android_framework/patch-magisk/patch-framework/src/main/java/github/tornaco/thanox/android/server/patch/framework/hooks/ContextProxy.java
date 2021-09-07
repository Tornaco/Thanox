package github.tornaco.thanox.android.server.patch.framework.hooks;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Handler;
import android.os.UserHandle;

import com.elvishew.xlog.XLog;

import github.tornaco.android.thanos.core.util.PkgUtils;

public class ContextProxy extends ContextWrapper {
    private final String tag;

    public ContextProxy(Context base, String tag) {
        super(base);
        this.tag = tag;
    }

    @Override
    public void enforceCallingPermission(String permission, String message) {
        XLog.i("SystemServiceContextHooks enforceCallingPermission@%s %s %s", tag, permission, message);
        long callingUid = Binder.getCallingUid();
        if (permission.equals(Manifest.permission.CHANGE_APP_IDLE_STATE) && PkgUtils.isSystemCall(callingUid)) {
            XLog.w("SystemServiceContextHooks, grant CHANGE_APP_IDLE_STATE fro uid: %s", callingUid);
            return;
        }
        super.enforceCallingPermission(permission, message);
    }

    @Override
    public boolean bindServiceAsUser(Intent service, ServiceConnection conn, int flags, UserHandle user) {
        XLog.i("SystemServiceContextHooks bindServiceAsUser %s", service);
        try {
            return super.bindServiceAsUser(service, conn, flags, user);
        } catch (Throwable e) {
            XLog.e("SystemServiceContextHooks bindServiceAsUser error", e);
            return false;
        }
    }

    @Override
    public boolean bindServiceAsUser(Intent service, ServiceConnection conn, int flags, Handler handler, UserHandle user) {
        XLog.i("SystemServiceContextHooks bindServiceAsUser %s", service);
        try {
            return super.bindServiceAsUser(service, conn, flags, handler, user);
        } catch (Throwable e) {
            XLog.e("SystemServiceContextHooks bindServiceAsUser error", e);
            return false;
        }
    }
}