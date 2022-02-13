package github.tornaco.thanox.android.server.patch.framework.hooks.accessibility;

import android.content.Context;

import com.elvishew.xlog.XLog;

import github.tornaco.android.thanos.services.patch.common.accessibility.AccessibilityManagerServiceHelper;
import github.tornaco.thanox.android.server.patch.framework.LocalServices;
import github.tornaco.thanox.android.server.patch.framework.hooks.ContextProxy;
import util.XposedHelpers;

public class AccessibilityManagerServiceHooks {
    public static void install(ClassLoader classLoader) {
        try {
            installContextForAccService(classLoader);
        } catch (Throwable e) {
            XLog.e("AccessibilityManagerServiceHooks error install", e);
        }
    }

    private static void installContextForAccService(ClassLoader classLoader) {
        XLog.w("AccessibilityManagerServiceHooks installContextForSyncManager");
        new LocalServices(classLoader).getService(AccessibilityManagerServiceHelper
                .INSTANCE.accessibilityManagerServiceClass(classLoader)).ifPresent(service -> {
            XLog.w("AccessibilityManagerServiceHooks service: %s", service);
            // Update mContext.
            Context context = (Context) XposedHelpers.getObjectField(service, "mContext");
            XLog.w("AccessibilityManagerServiceHooks service.context: %s", context);
            XposedHelpers.setObjectField(service, "mContext", new ContextProxy(context, "AccessibilityManagerService"));
        });

    }
}
