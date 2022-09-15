package github.tornaco.thanox.android.server.patch.framework.hooks.app.usage;

import android.app.usage.UsageEvents;
import android.content.ComponentName;
import android.content.Intent;

import com.android.dx.stock.BaseProxyFactory;
import com.android.dx.stock.ProxyBuilder;
import com.elvishew.xlog.XLog;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

import github.tornaco.android.thanos.core.util.AbstractSafeR;
import github.tornaco.android.thanos.services.BootStrap;
import github.tornaco.android.thanos.services.config.ServiceConfigs;
import github.tornaco.android.thanos.services.patch.common.usage.UsageStatsManagerInternalHelper;
import util.ExceptionTransformedInvocationHandler;
import util.XposedHelpers;
import util.XposedHelpersExt;

public class UsageStatsManagerInternalHooks {

    public static void installUsageStatsService(Object ams, ClassLoader classLoader) {
        new AbstractSafeR() {
            @Override
            public void runSafety() {
                installUsageStatsService0(ams, classLoader);
            }
        }.setName("UsageStatsManagerInternalHooks.install").run();
    }

    private static void installUsageStatsService0(Object ams, ClassLoader classLoader) {
        XLog.i("UsageStatsManagerInternalHooks install, ams: %s", ams);
        // UsageStatsManagerInternal mUsageStatsService;
        // android.app.usage.UsageStatsManagerInternal is in services package.
        Object original = XposedHelpers.getObjectField(ams, "mUsageStatsService");
        XLog.i("UsageStatsManagerInternalHooks install, original: %s", original);
        Object proxy = new UsageStatsManagerProxyFactory(classLoader).newProxy(original, ServiceConfigs.baseServerTmpDir());
        XLog.i("UsageStatsManagerInternalHooks install, proxy: %s", proxy);
        XposedHelpers.setObjectField(ams, "mUsageStatsService", proxy);
        XLog.i("UsageStatsManagerInternalHooks installed");
    }

    private static class UsageStatsManagerProxyFactory extends BaseProxyFactory<Object> {
        private final ClassLoader classLoader;

        public UsageStatsManagerProxyFactory(ClassLoader classLoader) {
            this.classLoader = classLoader;
        }

        @Override
        protected Object onCreateProxy(Object original, File dexCacheDir) throws Exception {
            return ProxyBuilder.forClass(UsageStatsManagerInternalHelper.INSTANCE.usmInternalClass(classLoader))
                    .dexCache(dexCacheDir)
                    .withSharedClassLoader()
                    .markTrusted()
                    .handler(new UsageStatsManagerInvocationHandler(original))
                    .build();
        }
    }

    private static class UsageStatsManagerInvocationHandler implements InvocationHandler,
            ExceptionTransformedInvocationHandler {
        // UsageStatsManagerInternal
        private final Object original;

        public UsageStatsManagerInvocationHandler(Object original) {
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
                int userId = (int) args[1];
                int type = (int) args[2];
                ComponentName component = (ComponentName) args[0];
                if (type == UsageEvents.Event.MOVE_TO_BACKGROUND) {
                    XLog.d("UsageStatsManagerInternalHooks, MOVE_TO_BACKGROUND: %s", component);
                    BootStrap.THANOS_X.getActivityStackSupervisor().reportOnActivityStopped(new Intent().setComponent(component));
                } else if (type == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    XLog.d("UsageStatsManagerInternalHooks, MOVE_TO_FOREGROUND: %s", component);
                    BootStrap.THANOS_X.getActivityStackSupervisor().reportOnActivityResumed(new Intent().setComponent(component), userId);
                }
            } catch (Throwable e) {
                XLog.e("UsageStatsManagerInternalHooks reportEvent error", e);
            }
        }
    }
}
