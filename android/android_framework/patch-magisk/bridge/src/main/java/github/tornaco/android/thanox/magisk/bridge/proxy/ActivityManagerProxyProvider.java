package github.tornaco.android.thanox.magisk.bridge.proxy;

import android.app.IActivityManager;
import android.content.ComponentName;
import android.os.IBinder;
import android.os.IInterface;

import com.elvishew.xlog.XLog;

import java.lang.reflect.Proxy;
import java.util.Arrays;

import util.ExceptionTransformedInvocationHandler;

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

                                return tryInvoke(am, method, args);
                            });
                }
                return iInterface;
            }
        });
    }
}
