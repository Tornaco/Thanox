package github.tornaco.thanox.android.server.patch.framework.hooks.accessibility;

import android.content.Context;

import com.elvishew.xlog.XLog;

import github.tornaco.android.thanos.services.patch.common.accessibility.AccessibilityManagerServiceHelper;
import github.tornaco.thanox.android.server.patch.framework.LocalServices;
import github.tornaco.thanox.android.server.patch.framework.hooks.ContextProxy;
import util.XposedHelpers;

/**
 * Mainly want to fix(Acc service may be bypassed by AMS when checkService):
 * <p>
 * : Caused by: java.lang.SecurityException: Not allowed to bind to service Intent { cmp=com.xiaomi.scanner/.qrcodeautoprocessing.MyAccessibilityService (has extras) }
 * 02-13 19:47:26.327  5086  5086 E AndroidRuntime: 	at android.app.ContextImpl.bindServiceCommon(ContextImpl.java:1994)
 * 02-13 19:47:26.327  5086  5086 E AndroidRuntime: 	at android.app.ContextImpl.bindServiceAsUser(ContextImpl.java:1929)
 * 02-13 19:47:26.327  5086  5086 E AndroidRuntime: 	at com.android.server.accessibility.AccessibilityServiceConnection.bindLocked(AccessibilityServiceConnection.java:112)
 * 02-13 19:47:26.327  5086  5086 E AndroidRuntime: 	at com.android.server.accessibility.AccessibilityManagerService.updateServicesLocked(AccessibilityManagerService.java:1938)
 * 02-13 19:47:26.327  5086  5086 E AndroidRuntime: 	at com.android.server.accessibility.AccessibilityManagerService.onUserStateChangedLocked(AccessibilityManagerService.java:2162)
 * 02-13 19:47:26.327  5086  5086 E AndroidRuntime: 	at com.android.server.accessibility.AccessibilityManagerService.unlockUser(AccessibilityManagerService.java:1426)
 * 02-13 19:47:26.327  5086  5086 E AndroidRuntime: 	at com.android.server.accessibility.AccessibilityManagerService.access$1000(AccessibilityManagerService.java:150)
 * 02-13 19:47:26.327  5086  5086 E AndroidRuntime: 	at com.android.server.accessibility.AccessibilityManagerService$2.onReceive(AccessibilityManagerService.java:594)
 * 02-13 19:47:26.327  5086  5086 E AndroidRuntime: 	at android.app.LoadedApk$ReceiverDispatcher$Args.lambda$getRunnable$0$LoadedApk$ReceiverDispatcher$Args(LoadedApk.java:1694)
 * 02-13 19:47:26.327  5086  5086 E AndroidRuntime: 	... 10 more
 */
public class AccessibilityManagerServiceHooks {
    public static void install(ClassLoader classLoader) {
        try {
            installContextForAccService(classLoader);
        } catch (Throwable e) {
            XLog.e("AccessibilityManagerServiceHooks error install", e);
        }
    }

    // FIXME:
    // Currently this hook can not retrieve AccService, maybe the service lifecycle
    // later added after Thanox bootstrap systemReady.
    private static void installContextForAccService(ClassLoader classLoader) {
        XLog.w("AccessibilityManagerServiceHooks installContextForAccService");
        new LocalServices(classLoader).getService(AccessibilityManagerServiceHelper.INSTANCE.lifeCycleClass(classLoader)).ifPresent(lifecycle -> {
            XLog.i("AccessibilityManagerServiceHooks AccService.Lifecycle: %s", lifecycle);
            Object accService = XposedHelpers.getObjectField(lifecycle, "mService");
            XLog.i("AccessibilityManagerServiceHooks accService: %s", accService);
            // Update mContext.
            Context context = (Context) XposedHelpers.getObjectField(accService, "mContext");
            XLog.i("AccessibilityManagerServiceHooks accService.context: %s", context);
            XposedHelpers.setObjectField(accService, "mContext", new ContextProxy(context, "AccService"));
        });

    }
}
