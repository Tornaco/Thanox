package github.tornaco.android.thanox.magisk.bridge.proxy;

import android.annotation.Nullable;
import android.annotation.SuppressLint;
import android.app.SystemServiceRegistry;

import com.elvishew.xlog.XLog;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Map;

import github.tornaco.android.thanox.magisk.bridge.util.ReflectionUtils;

class ServiceFetcherProxy {
    private static final boolean DEBUG = false;

    static void installInto(Map<String, Object> fetcherMap) {
        for (String name : fetcherMap.keySet().toArray(new String[0])) {
            Object originalFetcher = fetcherMap.get(name);
            XLog.d("ServiceFetcherProxy, installInto, originalFetcher " + name);
            if (originalFetcher != null) {
                Object proxyFetcher = newProxy(originalFetcher, name);
                XLog.d("ServiceFetcherProxy, installInto, proxyFetcher: %s", proxyFetcher);
                if (proxyFetcher != null) {
                    fetcherMap.put(name, proxyFetcher);
                    XLog.d("ServiceFetcherProxy, installed for: %s", name);
                } else {
                    XLog.d("ServiceFetcherProxy, installed failed for: %s", name);
                }
            }
        }
    }

    @Nullable
    private static Object newProxy(Object originalFetcher, String name) {
        try {
            XLog.d("ServiceFetcherProxy newProxy: " + name);
            @SuppressLint("PrivateApi")
            Class<?> serviceFetcherClass = Class.forName("android.app.SystemServiceRegistry$ServiceFetcher");
            XLog.d("ServiceFetcherProxy serviceFetcherClass: %s", serviceFetcherClass);
            Object res = Proxy.newProxyInstance(SystemServiceRegistry.class.getClassLoader(), new Class[]{
                    serviceFetcherClass
            }, new InvocationHandler() {
                @Override
                public Object invoke(Object object, Method method, Object[] args) throws Throwable {
                    if (ReflectionUtils.isToStringMethod(method)) {
                        return "ServiceFetcherProxy@" + name;
                    }
                    Object res = method.invoke(originalFetcher, args);
                    if (DEBUG) {
                        XLog.d("ServiceFetcherProxy invoke: %s %s %s", method.getName(), Arrays.toString(args), res);
                    }
                    return res;
                }
            });
            XLog.d("ServiceFetcherProxy newProxyInstance: %s", res);
            return res;
        } catch (Throwable e) {
            XLog.e("ServiceFetcherProxy newProxy error", e);
            return null;
        }
    }
}
