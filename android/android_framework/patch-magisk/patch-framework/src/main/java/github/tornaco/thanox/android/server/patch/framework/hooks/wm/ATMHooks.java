package github.tornaco.thanox.android.server.patch.framework.hooks.wm;

import static github.tornaco.thanox.android.server.patch.framework.hooks.wm.RecentTaskHooks.installRecentTasksCallback;

import com.android.server.wm.ActivityTaskManagerService;
import com.elvishew.xlog.XLog;

import github.tornaco.thanox.android.server.patch.framework.LocalServices;

public class ATMHooks {

    public static void install() {
        installHooksForATM();
    }

    private static void installHooksForATM() {
        XLog.i("ATMHooks installHooksForAMS");
        try {
            LocalServices.getService(ActivityTaskManagerService.Lifecycle.class)
                    .ifPresent(lifecycle -> {
                        XLog.d("ATMHooks Lifecycle: %s", lifecycle);
                        ActivityTaskManagerService atm = lifecycle.getService();
                        XLog.i("ATMHooks installHooksForAMS, ams: %s", atm);
                        if (atm == null) {
                            XLog.w("ATMHooks atm is null...");
                            return;
                        }

                        installRecentTasksCallback(atm);
                    });

        } catch (Throwable e) {
            XLog.e("ATMHooks Error SystemServerHooks installHooksForATM", e);
        }
    }

}