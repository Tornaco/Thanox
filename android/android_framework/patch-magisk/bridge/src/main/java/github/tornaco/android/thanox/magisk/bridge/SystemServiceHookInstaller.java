package github.tornaco.android.thanox.magisk.bridge;

import android.app.ActivityManager;
import android.app.IActivityManager;
import android.content.Context;
import android.os.IBinder;
import android.os.IClientCallback;
import android.os.IServiceCallback;
import android.os.IServiceManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.ArrayMap;
import android.util.Singleton;

import java.util.Arrays;

import github.tornaco.android.thanox.magisk.bridge.proxy.ActivityManagerProxyProvider;
import github.tornaco.android.thanox.magisk.bridge.proxy.Proxies;
import lombok.AllArgsConstructor;
import util.XposedHelpers;

import static github.tornaco.android.thanox.magisk.bridge.Logging.logging;

public class SystemServiceHookInstaller {
    private static final Singleton<IActivityManager> IActivityManagerSingletonProxy =
            new Singleton<IActivityManager>() {
                @Override
                protected IActivityManager create() {
                    final IBinder b = new ActivityManagerProxyProvider().provide(ServiceManager.getService(Context.ACTIVITY_SERVICE));
                    final IActivityManager am = IActivityManager.Stub.asInterface(b);
                    logging("IActivityManagerSingletonProxy create " + am);
                    return am;
                }
            };

    public static void installActivityManager() {
        // private static final Singleton<IActivityManager> IActivityManagerSingleton
        XposedHelpers.setStaticObjectField(ActivityManager.class, "IActivityManagerSingleton", IActivityManagerSingletonProxy);
        logging("installActivityManager done.");
    }

    public static void installServiceManagerCacheHook() {
        @SuppressWarnings("unchecked")
        ArrayMap<String, IBinder> arrayMap = (ArrayMap<String, IBinder>) XposedHelpers.getStaticObjectField(ServiceManager.class, "sCache");
        logging("installServiceManagerCacheHook.. %s", Arrays.toString(arrayMap.keySet().toArray()));
        for (String name : arrayMap.keySet().toArray(new String[0])) {
            IBinder legacy = arrayMap.get(name);
            if (legacy != null) {
                IBinder proxyBinder = Proxies.forCached(name, legacy);
                if (proxyBinder != null) {
                    arrayMap.put(name, proxyBinder);
                    logging("SMProxy#installServiceManagerHook，replace for: %s", name);
                }
            } else {
                logging("SMProxy#installServiceManagerHook， legacy == null， for: %s", name);
            }
        }
    }

    public static void installIServiceManagerHook() {
        // getIServiceManager
        IServiceManager originalSM = (IServiceManager) XposedHelpers.callStaticMethod(ServiceManager.class, "getIServiceManager");
        logging("installServiceManagerHook, originalSM=" + originalSM);
        if (originalSM instanceof SMProxy) {
            logging("installServiceManagerHook, proxySM already installed...");
            return;
        }
        IServiceManager proxySM = new SMProxy(originalSM);
        // private static IServiceManager sServiceManager;
        XposedHelpers.setStaticObjectField(ServiceManager.class, "sServiceManager", proxySM);
        logging("installServiceManagerHook, proxySM installed.");
    }

    @AllArgsConstructor
    private static class SMProxy implements IServiceManager {
        private final IServiceManager orig;

        @Override
        public IBinder getService(String name) throws RemoteException {
            logging("SMProxy#getService: %s", name);
            IBinder legacy = orig.getService(name);
            if (legacy == null) {
                logging("SMProxy#getService， legacy == null， for: %s", name);
                return null;
            }
            IBinder proxyBinder = Proxies.forBinderInternal(name, legacy);
            if (proxyBinder != null) {
                return proxyBinder;
            }
            // ava.lang.RuntimeException: java.lang.ClassCastException: github.tornaco.android.thanox.magisk.bridge.system.IBinderProxy cannot be cast to com.android.server.pm.PackageManagerService
            //07-04 21:21:13.834 16316 16316 E AndroidRuntime: 	at android.app.job.JobServiceEngine$JobHandler.handleMessage(JobServiceEngine.java:112)
            //07-04 21:21:13.834 16316 16316 E AndroidRuntime: 	at android.os.Handler.dispatchMessage(Handler.java:106)
            //07-04 21:21:13.834 16316 16316 E AndroidRuntime: 	at android.os.Looper.loop(Looper.java:223)
            //07-04 21:21:13.834 16316 16316 E AndroidRuntime: 	at com.android.server.SystemServer.run(SystemServer.java:622)
            //07-04 21:21:13.834 16316 16316 E AndroidRuntime: 	at com.android.server.SystemServer.main(SystemServer.java:408)
            //07-04 21:21:13.834 16316 16316 E AndroidRuntime: 	at java.lang.reflect.Method.invoke(Native Method)
            //07-04 21:21:13.834 16316 16316 E AndroidRuntime: 	at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:592)
            //07-04 21:21:13.834 16316 16316 E AndroidRuntime: 	at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:925)
            //07-04 21:21:13.834 16316 16316 E AndroidRuntime: Caused by: java.lang.ClassCastException: github.tornaco.android.thanox.magisk.bridge.system.IBinderProxy cannot be cast to com.android.server.pm.PackageManagerService
            //07-04 21:21:13.834 16316 16316 E AndroidRuntime: 	at com.android.server.pm.BackgroundDexOptService.onStartJob(BackgroundDexOptService.java:602)
            //07-04 21:21:13.834 16316 16316 E AndroidRuntime: 	at android.app.job.JobService$1.onStartJob(JobService.java:62)
            //07-04 21:21:13.834 16316 16316 E AndroidRuntime: 	at android.app.job.JobServiceEngine$JobHandler.handleMessage(JobServiceEngine.java:108)
            //07-04 21:21:13.834 16316 16316 E AndroidRuntime: 	... 7 more
            return legacy;
        }

        @Override
        public IBinder checkService(String name) throws RemoteException {
            return orig.checkService(name);
        }

        @Override
        public void addService(String name, IBinder service, boolean allowIsolated, int dumpPriority) throws RemoteException {
            logging("SMProxy addService: %s %s", name, service);
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
