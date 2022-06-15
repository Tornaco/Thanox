package github.tornaco.thanox.android.server.patch.framework.hooks.am;

import android.annotation.Nullable;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;

import com.android.dx.stock.BaseProxyFactory;
import com.android.dx.stock.ProxyBuilder;
import com.elvishew.xlog.XLog;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

import github.tornaco.android.thanos.core.util.AbstractSafeR;
import github.tornaco.android.thanos.services.BootStrap;
import github.tornaco.android.thanos.services.config.ServiceConfigs;
import github.tornaco.android.thanos.services.patch.common.firewall.IFWHelper;
import util.XposedHelpers;

class IFWHooks {

    static void installIFW(Object ams, ClassLoader classLoader) {
        // public final IntentFirewall mIntentFirewall;
        // mIntentFirewall = new IntentFirewall(new IntentFirewallInterface(), mHandler);
        // this.mAms = ams;
        new AbstractSafeR() {
            @Override
            public void runSafety() {
                XposedHelpers.setObjectField(
                        ams, "mIntentFirewall", IFWProxyBuilder.proxy(ams, classLoader));
                XLog.w("IFWHooks installIFW done");
            }
        }.setName("IFWHooks installIFW").run();
    }


    private static class IFWProxyBuilder {
        @SuppressWarnings("unchecked")
        @Nullable
        public static Object proxy(Object ams, ClassLoader classLoader) {
            // IFW.
            // public /*final*/ IntentFirewall mIntentFirewall;
            Object ifw = (Object) XposedHelpers.getObjectField(ams, "mIntentFirewall");
            XLog.i("IFWHooks IFWProxyBuilder installHooksForAMS, ifw: %s", ifw);
            // (IntentFirewall.AMSInterface)
            Object amsInterface = XposedHelpers.getObjectField(ifw, "mAms");
            Handler handler = (Handler) XposedHelpers.getObjectField(ifw, "mHandler");
            return new IWFProxyFactory(amsInterface, handler, classLoader).newProxy(ifw, ServiceConfigs.baseServerTmpDir());
        }

        @SuppressWarnings({"rawtypes", "Convert2Lambda"})
        private static class IWFProxyFactory extends BaseProxyFactory {
            // IntentFirewall.AMSInterface
            private final Object amsInterface;
            private final Handler handler;
            private final ClassLoader systemServerClassLoader;

            public IWFProxyFactory(Object amsInterface, Handler handler, ClassLoader systemServerClassLoader) {
                this.amsInterface = amsInterface;
                this.handler = handler;
                this.systemServerClassLoader = systemServerClassLoader;
            }

            @Override
            protected Object onCreateProxy(Object local, File dexCacheDir) throws Exception {
                return newProxy0(local, dexCacheDir);
            }

            private Object newProxy0(final Object local, File dexCacheDir) throws IOException {
                if (local == null) return null;
                XLog.i("IFWHooks IWFProxyFactory#newProxy0, local: %s", local);

                return ProxyBuilder.forClass(IFWHelper.INSTANCE.ifwClass(systemServerClassLoader))
                        .dexCache(dexCacheDir)
                        // public IntentFirewall(AMSInterface ams, Handler handler)
                        .constructorArgTypes(IFWHelper.INSTANCE.amsInterfaceClass(systemServerClassLoader), Handler.class)
                        .constructorArgValues(amsInterface, handler)
                        .withSharedClassLoader()
                        .markTrusted()
                        .handler(new InvocationHandler() {
                            @Override
                            public Object invoke(Object proxy, Method method, Object[] args)
                                    throws Throwable {
                                method.setAccessible(true);
                                XLog.v("IFWHooks: %s %s", method.getName(), Arrays.toString(args));
                                try {
                                    if ("checkBroadcast".equals(method.getName())) {
                                        Boolean hookRes = handleCheckBroadcast(args);
                                        if (hookRes != null && !hookRes) {
                                            return false;
                                        }
                                    }

                                    if ("checkStartActivity".equals(method.getName())) {
                                        Boolean hookRes = handleCheckStartActivity(args);
                                        if (hookRes != null && !hookRes) {
                                            return false;
                                        }
                                    }
                                } catch (Throwable e) {
                                    XLog.e("IFWHooks IWFProxy error", e);
                                }

                                return method.invoke(local, args);
                            }
                        })
                        .build();
            }

            private Boolean handleCheckBroadcast(Object[] args) {
                int callerUid = (int) args[1];
                int recUid = (int) args[4];
                Intent intent = (Intent) args[0];
                if (intent == null) return null;
                boolean res =
                        BootStrap.THANOS_X
                                .getActivityManagerService()
                                .checkBroadcast(intent, recUid, callerUid);
                if (!res) {
                    return false;
                }
                return null;
            }

            /**
             * The checkService interception will cause the bindService to throw an exception. Based on the magisk mode,
             * it is not easy to prevent the app or system from crashing by hooking the context.
             * So we don't need IFW to intercept service startup.
             */
            @Deprecated
            private Boolean handleCheckService(Object[] args) {
                Intent intent = (Intent) args[1];
                ComponentName componentName = (ComponentName) args[0];
                if (componentName == null) {
                    return null;
                }
                int callerID = (int) args[2];
                boolean res =
                        BootStrap.THANOS_X
                                .getActivityManagerService()
                                .checkService(intent, componentName, callerID);
                if (!res) {
                    return false;
                }
                return null;
            }

            private Boolean handleCheckStartActivity(Object[] args) {
                Intent intent = (Intent) args[0];
                if (intent == null) {
                    return null;
                }
                int callerID = (int) args[1];
                boolean res =
                        BootStrap.THANOS_X
                                .getActivityManagerService()
                                .checkStartActivity(intent, callerID);
                if (!res) {
                    return false;
                }
                return null;
            }
        }
    }
}
