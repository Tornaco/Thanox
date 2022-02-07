package github.tornaco.thanox.android.server.patch.framework;

import com.elvishew.xlog.XLog;

import github.tornaco.android.thanos.services.patch.common.notification.NMSHelper;
import github.tornaco.thanox.android.server.patch.framework.hooks.SystemServiceContextHooks;
import github.tornaco.thanox.android.server.patch.framework.hooks.am.AMSHooks;
import github.tornaco.thanox.android.server.patch.framework.hooks.appop.AppOpsHooks;
import github.tornaco.thanox.android.server.patch.framework.hooks.content.NotificationManagerServiceHooks;
import github.tornaco.thanox.android.server.patch.framework.hooks.content.SyncManagerHooks;
import github.tornaco.thanox.android.server.patch.framework.hooks.wm.ATMHooks;

public class SystemServerHooks {
    public static void install(ClassLoader classLoader) {
        // TODO Test
        Class clz = NMSHelper.INSTANCE.nmsClass(classLoader);
        XLog.w("SystemServerHooks===> " + clz);

        AMSHooks.install(classLoader);
        ATMHooks.install(classLoader);
        AppOpsHooks.install();
        SystemServiceContextHooks.install(classLoader);
        NotificationManagerServiceHooks.install(classLoader);
        SyncManagerHooks.install(classLoader);
    }
}
