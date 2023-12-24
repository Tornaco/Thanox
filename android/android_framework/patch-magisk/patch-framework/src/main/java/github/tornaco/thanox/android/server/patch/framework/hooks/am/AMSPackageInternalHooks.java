package github.tornaco.thanox.android.server.patch.framework.hooks.am;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;

import com.android.dx.stock.BaseProxyFactory;
import com.android.dx.stock.ProxyBuilder;
import com.elvishew.xlog.XLog;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import github.tornaco.android.thanos.core.util.AbstractSafeR;
import now.fortuitous.BootStrap;
import now.fortuitous.config.ServiceConfigs;
import github.tornaco.android.thanos.services.patch.common.content.pm.PackageManagerInternalHelper;
import util.XposedHelpers;
import util.XposedHelpersExt;

/**
 * We provide this hook to interrupt Android Services startup.
 * AMS calls ActiveServices to bind or start a Service, PackageManagerInternal#resolveService
 * will be called while ActiveServices retrieveServiceLocked.
 * <p>
 * So, here we proxy PackageManagerInternal and return null if the Service should not be start.
 */
public class AMSPackageInternalHooks {

    static void installPackageManagerInternalHooks(Object ams, ClassLoader classLoader) {
        new AbstractSafeR() {
            @Override
            @SuppressWarnings("unchecked")
            public void runSafety() {
                // PackageManagerInternal mPackageManagerInt;
                // https://github.com/LineageOS/android_frameworks_base/blob/lineage-17.1/services/core/java/com/android/server/am/ActivityManagerService.java
                Object mPackageManagerInt = XposedHelpersExt.callMethodWithPreferredNames(ams, new String[]{"getPackageManagerInternal", "getPackageManagerInternalLocked"});
                XLog.w("AMSPackageInternalHooks installPackageManagerInternalHooks, mPackageManagerInt: %s", mPackageManagerInt);
                if (mPackageManagerInt == null) return;
                Object proxy = new PackageManagerInternalProxyFactory(classLoader)
                        .newProxy(mPackageManagerInt, ServiceConfigs.baseServerTmpDir());
                XLog.w("AMSPackageInternalHooks installPackageManagerInternalHooks, proxy: %s", proxy);
                XposedHelpers.setObjectField(ams, "mPackageManagerInt", proxy);
                XLog.w("AMSPackageInternalHooks installPackageManagerInternalHooks success");
            }
        }.setName("AMSPackageInternalHooks installPackageManagerInternalHooks").run();
    }


    @SuppressWarnings({"rawtypes", "Convert2Lambda"})
    private static class PackageManagerInternalProxyFactory extends BaseProxyFactory {
        private final ClassLoader systemServerClassLoader;

        public PackageManagerInternalProxyFactory(ClassLoader systemServerClassLoader) {
            this.systemServerClassLoader = systemServerClassLoader;
        }

        @Override
        protected Object onCreateProxy(Object original, File dexCacheDir) throws Exception {
            return newProxy0(original, dexCacheDir);
        }

        private Object newProxy0(final Object original, File dexCacheDir) throws IOException {
            if (original == null) return null;
            XLog.w("AMSPackageInternalHooks PackageManagerInternalProxyFactory#newProxy0, original: %s", original);

            return ProxyBuilder.forClass(PackageManagerInternalHelper.INSTANCE.packageManagerInternalClass(systemServerClassLoader))
                    .dexCache(dexCacheDir)
                    .withSharedClassLoader()
                    .markTrusted()
                    .handler(new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args)
                                throws Throwable {
                            method.setAccessible(true);
                            if ("resolveService".equals(method.getName())) {
                                try {
                                    return handleCheckService(original, method, args);
                                } catch (Throwable e) {
                                    XLog.e("handleCheckService error", e);
                                }
                            }
                            return method.invoke(original, args);
                        }
                    })
                    .build();
        }

        //  public abstract ResolveInfo resolveService(Intent intent, String resolvedType,
        //           int flags, int userId, int callingUid);
        // resolveService [Intent { cmp=com.google.android.gms/.auth.GetToken }, null, 268436480, 0, 10153]
        private ResolveInfo handleCheckService(Object original, Method method, Object[] args) throws Exception {
            ResolveInfo resolveInfo = (ResolveInfo) method.invoke(original, args);
            if (resolveInfo == null) {
                XLog.v("AMSPackageInternalHooks handleCheckService, resolveInfo is null...");
                return null;
            }
            ServiceInfo serviceInfo = resolveInfo.serviceInfo;
            if (serviceInfo == null) {
                XLog.v("AMSPackageInternalHooks handleCheckService, serviceInfo is null...");
                return resolveInfo;
            }
            ComponentName resolvedName = serviceInfo.getComponentName();
            if (resolvedName == null) {
                XLog.v("AMSPackageInternalHooks handleCheckService, resolvedName is null...");
                return resolveInfo;
            }

            Intent intent = (Intent) args[0];
            int callingUid = (int) args[args.length - 1];
            int userId = (int) args[args.length - 2];
            XLog.v("AMSPackageInternalHooks handleCheckService, resolvedName: %s, callingUid: %s, userId: %s, intent: %s",
                    resolvedName, callingUid, userId, intent);

            boolean res = BootStrap.THANOS_X
                    .getActivityManagerService()
                    .checkService(intent, resolvedName, callingUid, userId);
            if (!res) {
                return null;
            }
            return resolveInfo;
        }
    }
}
