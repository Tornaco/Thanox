package github.tornaco.android.thanox.magisk.bridge.proxy;

import android.os.IBinder;

import com.google.common.collect.Lists;

import java.util.List;

import github.tornaco.android.thanox.magisk.bridge.proxy.notification.NotificationManagerProxyProvider;

public class Proxies {
    private static final List<ProxyProvider> PROVIDERS_CACHE = Lists.newArrayList(
            new NotificationManagerProxyProvider()
    );

    public static IBinder forCached(String name, IBinder legacyBinder) {
        if (name == null || legacyBinder == null) {
            return null;
        }
        for (ProxyProvider p : PROVIDERS_CACHE) {
            IBinder pb = p.isForService(name) ? p.provide(legacyBinder) : null;
            if (pb != null) {
                return pb;
            }
        }
        return null;
    }
}