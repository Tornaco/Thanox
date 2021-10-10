package github.tornaco.thanox.android.server.patch.framework.hooks.am;

import static github.tornaco.thanox.android.server.patch.framework.hooks.am.ActiveServicesHooks.attachActiveServices;
import static github.tornaco.thanox.android.server.patch.framework.hooks.am.AppExitInfoTrackerHooks.installAppExitInfoTracker;
import static github.tornaco.thanox.android.server.patch.framework.hooks.am.IFWHooks.installIFW;
import static github.tornaco.thanox.android.server.patch.framework.hooks.app.usage.UsageStatsManagerInternalHooks.installUsageStatsService;

import com.android.server.am.ActivityManagerService;
import com.elvishew.xlog.XLog;

import github.tornaco.thanox.android.server.patch.framework.LocalServices;

public class AMSHooks {

    public static void install() {
        installHooksForAMS();
    }

    private static void installHooksForAMS() {
        XLog.i("AMSHooks installHooksForAMS");
        try {
            LocalServices.getService(ActivityManagerService.Lifecycle.class)
                    .ifPresent(lifecycle -> {
                        XLog.d("AMSHooks Lifecycle: %s", lifecycle);
                        ActivityManagerService ams = lifecycle.getService();
                        XLog.i("AMSHooks installHooksForAMS, ams: %s", ams);
                        if (ams == null) {
                            XLog.w("AMSHooks ams is null...");
                            return;
                        }

                        attachActiveServices(ams);
                        installIFW(ams);
                        installAppExitInfoTracker(ams);
                        installUsageStatsService(ams);
                    });

        } catch (Throwable e) {
            XLog.e("AMSHooks Error SystemServerHooks installHooksForAMS", e);
        }
    }

}