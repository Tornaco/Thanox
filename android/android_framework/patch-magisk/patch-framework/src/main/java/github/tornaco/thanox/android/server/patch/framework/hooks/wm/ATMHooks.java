package github.tornaco.thanox.android.server.patch.framework.hooks.wm;

import static github.tornaco.thanox.android.server.patch.framework.hooks.wm.RecentTaskHooks.installRecentTasksCallback;

import com.elvishew.xlog.XLog;

import github.tornaco.android.thanos.services.patch.common.LocalServices;
import github.tornaco.android.thanos.services.patch.common.wm.ATMLifeCycleHelper;

public class ATMHooks {

    public static void install(ClassLoader classLoader) {
        installHooksForATM(classLoader);
    }

    private static void installHooksForATM(ClassLoader classLoader) {
        XLog.i("ATMHooks installHooksForAMS");
        try {
            new LocalServices(classLoader).getService(ATMLifeCycleHelper.INSTANCE.lifeCycleClass(classLoader))
                    .ifPresent(lifecycle -> {
                        XLog.d("ATMHooks Lifecycle: %s", lifecycle);
                        Object atm = ATMLifeCycleHelper.INSTANCE.getService(lifecycle);
                        XLog.i("ATMHooks installHooksForAMS, ams: %s", atm);
                        if (atm == null) {
                            XLog.w("ATMHooks atm is null...");
                            return;
                        }

                        installRecentTasksCallback(atm, classLoader);
                    });

        } catch (Throwable e) {
            XLog.e("ATMHooks Error SystemServerHooks installHooksForATM", e);
        }
    }

}