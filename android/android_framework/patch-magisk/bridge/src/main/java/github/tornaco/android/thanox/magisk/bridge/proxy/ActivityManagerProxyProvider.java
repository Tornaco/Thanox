package github.tornaco.android.thanox.magisk.bridge.proxy;

import android.app.IActivityManager;
import android.os.IBinder;
import android.os.IInterface;

import java.lang.reflect.Proxy;
import java.util.Arrays;

import static github.tornaco.android.thanox.magisk.bridge.Logging.logging;

public class ActivityManagerProxyProvider implements ProxyProvider, ExceptionTransformedInvocationHandler {
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
                                if (!"checkPermission".equals(method.getName())
                                        && !"handleIncomingUser".equals(method.getName())) {
                                    logging("IActivityManager %s %s", method.getName(), Arrays.toString(args));
                                }
                                return tryInvoke(am, method, args);
                            });
                }
                return iInterface;
            }
        });
    }
}
