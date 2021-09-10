package github.tornaco.android.thanox.magisk.bridge;


import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.IActivityManager;
import android.app.IActivityTaskManager;
import android.content.Context;
import android.os.IBinder;
import android.os.IClientCallback;
import android.os.IServiceCallback;
import android.os.IServiceManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.ArrayMap;
import android.util.Singleton;

import com.elvishew.xlog.XLog;

import java.util.Arrays;

import github.tornaco.android.thanox.magisk.bridge.proxy.ActivityManagerProxyProvider;
import github.tornaco.android.thanox.magisk.bridge.proxy.ActivityTaskManagerProxyProvider;
import github.tornaco.android.thanox.magisk.bridge.proxy.Proxies;
import lombok.AllArgsConstructor;
import util.XposedHelpers;

public class AppProcessSystemServiceHookInstaller {
    public static void install() {
        // Remote binder ins.
        AppProcessSystemServiceHookInstaller.installIServiceManagerHook();
        // These service set into ServiceManager by AMS when bind add.
        AppProcessSystemServiceHookInstaller.installServiceManagerCacheHook();
        AppProcessSystemServiceHookInstaller.installActivityManager();
        AppProcessSystemServiceHookInstaller.installActivityTaskManager();
    }

    private static final Singleton<IActivityManager> IActivityManagerSingletonProxy =
            new Singleton<IActivityManager>() {
                @Override
                protected IActivityManager create() {
                    final IBinder orig = ServiceManager.getService(Context.ACTIVITY_SERVICE);
                    final IBinder b = orig == null ? null : new ActivityManagerProxyProvider().provide(orig);
                    final IActivityManager am = IActivityManager.Stub.asInterface(b);
                    XLog.d("IActivityManagerSingletonProxy create " + am);
                    return am;
                }
            };

    private static final Singleton<IActivityTaskManager> IActivityTaskManagerSingleton =
            new Singleton<IActivityTaskManager>() {
                @Override
                protected IActivityTaskManager create() {
                    final IBinder orig = ServiceManager.getService(Context.ACTIVITY_TASK_SERVICE);
                    final IBinder b = orig == null ? null : new ActivityTaskManagerProxyProvider().provide(orig);
                    final IActivityTaskManager am = IActivityTaskManager.Stub.asInterface(b);
                    XLog.d("IActivityTaskManagerSingletonProxy create " + am);
                    return am;
                }
            };


    private static void installActivityManager() {
        // private static final Singleton<IActivityManager> IActivityManagerSingleton
        XposedHelpers.setStaticObjectField(ActivityManager.class, "IActivityManagerSingleton", IActivityManagerSingletonProxy);
        XLog.d("installActivityManager done");
    }

    private static void installActivityTaskManager() {
        // private static final Singleton<IActivityTaskManager> IActivityTaskManagerSingleton
        XposedHelpers.setStaticObjectField(ActivityTaskManager.class, "IActivityTaskManagerSingleton", IActivityTaskManagerSingleton);
        XLog.d("installActivityTaskManager done");
    }

    private static void installServiceManagerCacheHook() {
        @SuppressWarnings("unchecked")
        ArrayMap<String, IBinder> arrayMap = (ArrayMap<String, IBinder>) XposedHelpers.getStaticObjectField(ServiceManager.class, "sCache");
        XLog.d("installServiceManagerCacheHook.. %s", Arrays.toString(arrayMap.keySet().toArray()));
        for (String name : arrayMap.keySet().toArray(new String[0])) {
            IBinder legacy = arrayMap.get(name);
            if (legacy != null) {
                IBinder proxyBinder = Proxies.forCached(name, legacy);
                if (proxyBinder != null) {
                    arrayMap.put(name, proxyBinder);
                    XLog.d("SMProxy#installServiceManagerHook，replace for: %s", name);
                }
            } else {
                XLog.d("SMProxy#installServiceManagerHook， legacy == null， for: %s", name);
            }
        }
    }

    private static void installIServiceManagerHook() {
        // getIServiceManager
        IServiceManager originalSM = (IServiceManager) XposedHelpers.callStaticMethod(ServiceManager.class, "getIServiceManager");
        XLog.d("installServiceManagerHook, originalSM=" + originalSM);
        if (originalSM instanceof SMProxy) {
            XLog.d("installServiceManagerHook, proxySM already installed...");
            return;
        }
        IServiceManager proxySM = new SMProxy(originalSM);
        // private static IServiceManager sServiceManager;
        XposedHelpers.setStaticObjectField(ServiceManager.class, "sServiceManager", proxySM);
        XLog.d("installServiceManagerHook, proxySM installed.");
    }

    @AllArgsConstructor
    private static class SMProxy implements IServiceManager {
        private final IServiceManager orig;

        @Override
        public IBinder getService(String name) throws RemoteException {
            XLog.d("SMProxy#getService: %s", name);
            IBinder legacy = orig.getService(name);
            if (legacy == null) {
                XLog.d("SMProxy#getService， legacy == null， for: %s", name);
                return null;
            }
            IBinder proxyBinder = Proxies.forBinderInternal(name, legacy);
            if (proxyBinder != null) {
                return proxyBinder;
            }
            return legacy;
        }

        @Override
        public IBinder checkService(String name) throws RemoteException {
            return orig.checkService(name);
        }

        @Override
        public void addService(String name, IBinder service, boolean allowIsolated, int dumpPriority) throws RemoteException {
            XLog.d("SMProxy addService: %s %s", name, service);
            orig.addService(name, service, allowIsolated, dumpPriority);
        }

        @Override
        public String[] listServices(int dumpPriority) throws RemoteException {
            return orig.listServices(dumpPriority);
        }

        @Override
        public void registerForNotifications(String name, IServiceCallback callback) throws RemoteException {
            orig.registerForNotifications(name, callback);
        }

        @Override
        public void unregisterForNotifications(String name, IServiceCallback callback) throws RemoteException {
            orig.unregisterForNotifications(name, callback);
        }

        @Override
        public boolean isDeclared(String name) throws RemoteException {
            return orig.isDeclared(name);
        }

        @Override
        public void registerClientCallback(String name, IBinder service, IClientCallback callback) throws RemoteException {
            orig.registerClientCallback(name, service, callback);
        }

        @Override
        public void tryUnregisterService(String name, IBinder service) throws RemoteException {
            orig.tryUnregisterService(name, service);
        }

        @Override
        public IBinder asBinder() {
            return orig.asBinder();
        }
    }
}
