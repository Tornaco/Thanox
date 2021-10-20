package github.tornaco.thanox.android.server.patch.framework.hooks.app.usage;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManagerInternal;
import android.content.ComponentName;
import android.content.Intent;

import com.android.dx.stock.BaseProxyFactory;
import com.android.dx.stock.ProxyBuilder;
import com.android.server.am.ActivityManagerService;
import com.elvishew.xlog.XLog;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

import github.tornaco.android.thanos.core.util.AbstractSafeR;
import github.tornaco.android.thanos.services.BootStrap;
import github.tornaco.android.thanos.services.config.ServiceConfigs;
import util.ExceptionTransformedInvocationHandler;
import util.XposedHelpers;
import util.XposedHelpersExt;

public class UsageStatsManagerInternalHooks {

    public static void installUsageStatsService(ActivityManagerService ams) {
        new AbstractSafeR() {
            @Override
            public void runSafety() {
                installUsageStatsService0(ams);
            }
        }.setName("UsageStatsManagerInternalHooks.install").run();
    }

    private static void installUsageStatsService0(ActivityManagerService ams) {
        XLog.i("UsageStatsManagerInternalHooks install, ams: %s", ams);
        // UsageStatsManagerInternal mUsageStatsService;
        UsageStatsManagerInternal original = (UsageStatsManagerInternal) XposedHelpers.getObjectField(ams, "mUsageStatsService");
        XLog.i("UsageStatsManagerInternalHooks install, original: %s", original);
        UsageStatsManagerInternal proxy = new UsageStatsManagerProxyFactory().newProxy(original, ServiceConfigs.baseServerTmpDir());
        XLog.i("UsageStatsManagerInternalHooks install, proxy: %s", proxy);
        XposedHelpers.setObjectField(ams, "mUsageStatsService", proxy);
        XLog.i("UsageStatsManagerInternalHooks installed");
    }

    private static class UsageStatsManagerProxyFactory extends BaseProxyFactory<UsageStatsManagerInternal> {

        @Override
        protected UsageStatsManagerInternal onCreateProxy(UsageStatsManagerInternal original, File dexCacheDir) throws Exception {
            return ProxyBuilder.forClass(UsageStatsManagerInternal.class)
                    .dexCache(dexCacheDir)
                    .withSharedClassLoader()
                    .markTrusted()
                    .handler(new UsageStatsManagerInvocationHandler(original))
                    .build();
        }
    }

    private static class UsageStatsManagerInvocationHandler implements InvocationHandler,
            ExceptionTransformedInvocationHandler {
        private final UsageStatsManagerInternal original;

        public UsageStatsManagerInvocationHandler(UsageStatsManagerInternal original) {
            this.original = original;
        }

        @Override
        public Object invoke(Object o, Method method, Object[] args) throws Throwable {
            // public abstract void reportEvent(ComponentName component,
            // @UserIdInt int userId, int eventType, int instanceId, ComponentName taskRoot);
            if (XposedHelpersExt.matchMethodNameAndArgs(method, "reportEvent",
                    ComponentName.class,
                    int.class,
                    int.class,
                    int.class,
                    ComponentName.class)) {
                handleReportEvent(args);
            }
            return tryInvoke(original, method, args);
        }

        private void handleReportEvent(Object[] args) {
            XLog.d("UsageStatsManagerInternalHooks handleReportEvent %s", Arrays.toString(args));
            try {
                int type = (int) args[2];
                ComponentName component = (ComponentName) args[0];
                if (type == UsageEvents.Event.MOVE_TO_BACKGROUND) {
                    XLog.d("UsageStatsManagerInternalHooks, MOVE_TO_BACKGROUND: %s", component);
                    BootStrap.THANOS_X.getActivityStackSupervisor().reportOnActivityStopped(new Intent().setComponent(component));
                } else if (type == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    XLog.d("UsageStatsManagerInternalHooks, MOVE_TO_FOREGROUND: %s", component);
                    BootStrap.THANOS_X.getActivityStackSupervisor().reportOnActivityResumed(new Intent().setComponent(component));
                }
            } catch (Throwable e) {
                XLog.e("UsageStatsManagerInternalHooks reportEvent error", e);
            }
        }
    }
}
