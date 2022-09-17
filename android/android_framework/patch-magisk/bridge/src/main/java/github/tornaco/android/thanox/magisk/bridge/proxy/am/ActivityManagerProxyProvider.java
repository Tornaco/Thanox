package github.tornaco.android.thanox.magisk.bridge.proxy.am;

import android.app.IActivityManager;
import android.content.ComponentName;
import android.os.IBinder;
import android.os.IInterface;

import com.elvishew.xlog.XLog;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

import github.tornaco.android.thanos.core.app.ThanosManagerNative;
import github.tornaco.android.thanox.magisk.bridge.proxy.ProxyProvider;
import util.ExceptionTransformedInvocationHandler;
import util.os.BinderProxy;

public class ActivityManagerProxyProvider implements ProxyProvider, ExceptionTransformedInvocationHandler {
    private static final boolean DEBUG_AMS = false;

    @Override
    public IBinder provide(IBinder legacyBinder) {
        return proxyActivityManager(legacyBinder);
    }

    @Override
    public boolean isForService(String name) {
        return name.equals("activity");
    }

    private IBinder proxyActivityManager(IBinder original) {
        return new BinderProxy(original, new BinderProxy.InvocationHandler() {
            @Override
            public IInterface onQueryLocalInterface(String descriptor, IInterface iInterface) {
                if (IActivityManager.class.getName().equals(descriptor)) {
                    IActivityManager am = IActivityManager.Stub.asInterface(original);
                    return (IInterface) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                            new Class[]{IActivityManager.class},
                            (instance, method, args) -> {
                                if (DEBUG_AMS) {
                                    XLog.d("IActivityManager %s %s", method.getName(), Arrays.toString(args));
                                }

                                if ("bindIsolatedService".equals(method.getName())) {
                                    // FIX Service binding error.
                                    int res = (int) tryInvoke(am, method, args);
                                    XLog.d("IActivityManager bindIsolatedService, res=" + res);
                                    if (res < 0) {
                                        XLog.d("IActivityManager bindIsolatedService result < 0, we will fix it to 0, "
                                                + Arrays.toString(args));
                                        res = 0;
                                    }
                                    return res;
                                    // BREAK!!!
                                }

                                if ("startService".equals(method.getName())) {
                                    // FIX Service start error.
                                    ComponentName componentName =
                                            (ComponentName) tryInvoke(am, method, args);
                                    if (componentName == null) return null;
                                    if (componentName.getPackageName() == null)
                                        return componentName;
                                    if (componentName.getPackageName().equals("!")
                                            || componentName.getPackageName().equals("?")
                                            || componentName.getPackageName().equals("!!")) {
                                        XLog.d("IActivityManager Try to fix startServiceLocked ERROR throw by system!!!"
                                                + Arrays.toString(args));
                                        return null;
                                    }
                                    return componentName;
                                    // BREAK!!!
                                }

                                // ContentProviderHolder getContentProvider(IApplicationThread caller, String callingPackage,
                                // String name, int userId, boolean stable);
                                if ("getContentProvider".equals(method.getName())) {
                                    return handleGetProvider(am, method, args);
                                }

                                return tryInvoke(am, method, args);
                            });
                }
                return iInterface;
            }
        });
    }

    private Object handleGetProvider(IActivityManager am, Method method, Object[] args) throws Throwable {
        if (!ThanosManagerNative.isServiceInstalled()) {
            XLog.d("IActivityTaskManager, handleGetProvider, Thanox not installed...");
            return tryInvoke(am, method, args);
        }
        String callingPackage = (String) args[1];
        String name = (String) args[2];
        int userId = (int) args[args.length - 2];
        XLog.d("IActivityManager getContentProvider: %s %s %s", callingPackage, name, userId);
        boolean res =
                ThanosManagerNative.getDefault()
                        .getActivityManager()
                        .checkGetContentProvider(callingPackage, name, userId);
        if (!res) {
            return null;
        }
        return tryInvoke(am, method, args);
    }
}
