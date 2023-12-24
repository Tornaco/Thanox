package github.tornaco.thanox.android.server.patch.framework.hooks.am;

import github.tornaco.android.thanos.core.util.AbstractSafeR;
import now.fortuitous.BootStrap;
import util.XposedHelpers;

class ActiveServicesHooks {

    static void attachActiveServices(Object ams) {
        new AbstractSafeR() {
            @Override
            public void runSafety() {
                // final ActiveServices mServices;
                Object service = XposedHelpers.getObjectField(ams, "mServices");
                BootStrap.THANOS_X.getAndroidSystemServices().attachService(service);
                // Call attach.
                BootStrap.THANOS_X.getAndroidSystemServices().attachService(ams);
            }
        }.setName("ActiveServicesHooks attachActiveServices").run();
    }
}
