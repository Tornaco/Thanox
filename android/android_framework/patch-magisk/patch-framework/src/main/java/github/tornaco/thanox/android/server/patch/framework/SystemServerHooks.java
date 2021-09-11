package github.tornaco.thanox.android.server.patch.framework;

import github.tornaco.thanox.android.server.patch.framework.hooks.AMSHooks;
import github.tornaco.thanox.android.server.patch.framework.hooks.AppOpsHooks;
import github.tornaco.thanox.android.server.patch.framework.hooks.NotificationManagerServiceHooks;
import github.tornaco.thanox.android.server.patch.framework.hooks.SyncManagerHooks;
import github.tornaco.thanox.android.server.patch.framework.hooks.SystemServiceContextHooks;

public class SystemServerHooks {

    public static void install() {
        AMSHooks.install();
        AppOpsHooks.install();
        SystemServiceContextHooks.install();
        NotificationManagerServiceHooks.install();
        SyncManagerHooks.install();
    }
}
