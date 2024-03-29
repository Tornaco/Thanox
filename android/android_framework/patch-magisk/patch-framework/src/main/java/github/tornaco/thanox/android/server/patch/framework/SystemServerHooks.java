package github.tornaco.thanox.android.server.patch.framework;

import github.tornaco.thanox.android.server.patch.framework.hooks.SystemServiceContextHooks;
import github.tornaco.thanox.android.server.patch.framework.hooks.am.AMSHooks;
import github.tornaco.thanox.android.server.patch.framework.hooks.appop.AppOpsHooks;
import github.tornaco.thanox.android.server.patch.framework.hooks.content.NotificationManagerServiceHooks;
import github.tornaco.thanox.android.server.patch.framework.hooks.wm.ATMHooks;

public class SystemServerHooks {
    public static void install(ClassLoader classLoader) {
        SystemServiceContextHooks.install(classLoader);
        AMSHooks.install(classLoader);
        ATMHooks.install(classLoader);
        AppOpsHooks.install();
        NotificationManagerServiceHooks.install(classLoader);
    }
}
