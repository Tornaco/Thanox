package github.tornaco.thanox.android.server.patch.framework.hooks.am;

import github.tornaco.android.thanos.core.util.AbstractSafeR;
import github.tornaco.android.thanos.services.BootStrap;
import github.tornaco.android.thanos.services.app.ActiveServicesProxy;
import github.tornaco.android.thanos.services.app.ActivityManagerServiceProxy;
import util.XposedHelpers;

class ActiveServicesHooks {

    static void attachActiveServices(Object ams) {
        new AbstractSafeR() {
            @Override
            public void runSafety() {
                // final ActiveServices mServices;
                Object service = XposedHelpers.getObjectField(ams, "mServices");
                ActiveServicesProxy activeServicesProxy = new ActiveServicesProxy(service);
                BootStrap.THANOS_X.getActivityManagerService().attachActiveServices(activeServicesProxy);

                // Call attach.
                ActivityManagerServiceProxy proxy = new ActivityManagerServiceProxy(ams);
                BootStrap.THANOS_X.getActivityManagerService().onAttachActivityManagerServiceProxy(proxy);
            }
        }.setName("ActiveServicesHooks attachActiveServices").run();
    }
}
