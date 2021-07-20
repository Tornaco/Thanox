package github.tornaco.android.thanox.magisk.bridge.proxy;

import android.os.IBinder;

import java.lang.reflect.Method;

public interface ProxyProvider {
    IBinder provide(IBinder legacyBinder);

    boolean isForService(String name);
}
