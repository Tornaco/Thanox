package github.tornaco.android.thanox.magisk.bridge.proxy;

import android.app.IActivityTaskManager;
import android.os.IBinder;
import android.os.IInterface;

import java.lang.reflect.Proxy;
import java.util.Arrays;

import github.tornaco.android.thanos.core.app.ThanosManagerNative;

import static github.tornaco.android.thanox.magisk.bridge.Logging.logging;

public class ActivityTaskManagerProxyProvider implements ProxyProvider, ExceptionTransformedInvocationHandler {
    @Override
    public IBinder provide(IBinder legacyBinder) {
        return proxyActivityTaskManager(legacyBinder);
    }

    @Override
    public boolean isForService(String name) {
        return name.equals("activity_task");
    }

    private IBinder proxyActivityTaskManager(IBinder original) {
        return new BinderProxy(original, new BinderProxy.InvocationHandler() {
            @Override
            public IInterface onQueryLocalInterface(String descriptor, IInterface iInterface) {
                if (IActivityTaskManager.class.getName().equals(descriptor)) {
                    IActivityTaskManager am = IActivityTaskManager.Stub.asInterface(original);
                    return (IInterface) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                            new Class[]{IActivityTaskManager.class},
                            (instance, method, args) -> {
                                logging("IActivityTaskManager %s %s", method.getName(), Arrays.toString(args));

                                if ("removeTask".equals(method.getName())) {
                                    logging("removeTask called. isServiceInstalled= " + ThanosManagerNative.isServiceInstalled());
                                    if (ThanosManagerNative.isServiceInstalled()) {
                                        ThanosManagerNative.getDefault()
                                                .getActivityManager()
                                                .reportOnRemoveTask((Integer) args[0]);
                                    } else {
                                        logging("removeTask, service not installed.");
                                    }
                                }
                                return tryInvoke(am, method, args);
                            });
                }
                return iInterface;
            }
        });
    }
}
