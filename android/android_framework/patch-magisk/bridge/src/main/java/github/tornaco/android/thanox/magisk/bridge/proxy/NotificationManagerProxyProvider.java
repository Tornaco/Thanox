package github.tornaco.android.thanox.magisk.bridge.proxy;

import static github.tornaco.android.thanox.magisk.bridge.Logging.logging;

import android.app.INotificationManager;
import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;

import java.lang.reflect.Proxy;
import java.util.Arrays;

public class NotificationManagerProxyProvider implements ProxyProvider, ExceptionTransformedInvocationHandler {
    @Override
    public IBinder provide(IBinder legacyBinder) {
        return proxyNotificationManager(legacyBinder);
    }

    @Override
    public boolean isForService(String name) {
        return name.equals(Context.NOTIFICATION_SERVICE);
    }

    private IBinder proxyNotificationManager(IBinder original) {
        return new BinderProxy(original, new BinderProxy.InvocationHandler() {
            @Override
            public IInterface onQueryLocalInterface(String descriptor, IInterface iInterface) {
                if (INotificationManager.class.getName().equals(descriptor)) {
                    INotificationManager am = INotificationManager.Stub.asInterface(original);
                    return (IInterface) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                            new Class[]{INotificationManager.class},
                            (instance, method, args) -> {
                                logging("INotificationManager %s %s", method.getName(), Arrays.toString(args));
                                return tryInvoke(am, method, args);
                            });
                }
                return iInterface;
            }
        });
    }
}
