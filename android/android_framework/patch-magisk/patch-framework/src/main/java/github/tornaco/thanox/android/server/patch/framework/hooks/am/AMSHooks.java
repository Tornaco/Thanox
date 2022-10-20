package github.tornaco.thanox.android.server.patch.framework.hooks.am;

import static github.tornaco.thanox.android.server.patch.framework.hooks.am.AMSPackageInternalHooks.installPackageManagerInternalHooks;
import static github.tornaco.thanox.android.server.patch.framework.hooks.am.ActiveServicesHooks.attachActiveServices;
import static github.tornaco.thanox.android.server.patch.framework.hooks.am.AppExitInfoTrackerHooks.installAppExitInfoTracker;
import static github.tornaco.thanox.android.server.patch.framework.hooks.am.IFWHooks.installIFW;
import static github.tornaco.thanox.android.server.patch.framework.hooks.app.usage.UsageStatsManagerInternalHooks.installUsageStatsService;

import com.elvishew.xlog.XLog;

import github.tornaco.android.thanos.services.patch.common.LocalServices;
import github.tornaco.android.thanos.services.patch.common.am.AMSLifeCycleHelper;

public class AMSHooks {

    public static void install(ClassLoader classLoader) {
        installHooksForAMS(classLoader);
    }

    private static void installHooksForAMS(ClassLoader classLoader) {
        XLog.i("AMSHooks installHooksForAMS");
        try {
            new LocalServices(classLoader).getService(AMSLifeCycleHelper.INSTANCE.lifeCycleClass(classLoader))
                    .ifPresent(lifecycle -> {
                        XLog.d("AMSHooks Lifecycle: %s", lifecycle);
                        Object ams = AMSLifeCycleHelper.INSTANCE.getService(lifecycle);
                        XLog.i("AMSHooks installHooksForAMS, ams: %s", ams);
                        if (ams == null) {
                            XLog.w("AMSHooks ams is null...");
                            return;
                        }

                        attachActiveServices(ams);
                        installPackageManagerInternalHooks(ams, classLoader);
                        installIFW(ams, classLoader);
                        installAppExitInfoTracker(ams);
                        installUsageStatsService(ams, classLoader);
                    });

        } catch (Throwable e) {
            XLog.e("AMSHooks Error SystemServerHooks installHooksForAMS", e);
        }
    }

}