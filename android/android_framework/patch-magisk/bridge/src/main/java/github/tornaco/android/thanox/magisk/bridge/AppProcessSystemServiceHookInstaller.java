package github.tornaco.android.thanox.magisk.bridge;


import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.AppGlobals;
import android.app.IActivityManager;
import android.app.IActivityTaskManager;
import android.content.Context;
import android.os.IBinder;
import android.os.ServiceManager;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Singleton;

import com.elvishew.xlog.XLog;

import java.util.Arrays;

import github.tornaco.android.thanos.core.util.PkgUtils;
import github.tornaco.android.thanox.magisk.bridge.proxy.Proxies;
import github.tornaco.android.thanox.magisk.bridge.proxy.am.ActivityManagerProxyProvider;
import github.tornaco.android.thanox.magisk.bridge.proxy.am.ActivityTaskManagerProxyProvider;
import util.XposedHelpers;

public class AppProcessSystemServiceHookInstaller {
    private final String processName;

    public AppProcessSystemServiceHookInstaller(String processName) {
        this.processName = processName;
    }

    public void install() {
        String initialPackage = AppGlobals.getInitialPackage();
        XLog.d("AppProcessSystemServiceHookInstaller about install to process: %s, initialPackage: %s", processName, initialPackage);
        if (!TextUtils.isEmpty(initialPackage) && PkgUtils.isAndroid(initialPackage)) {
            XLog.e("AppProcessSystemServiceHookInstaller skip install to android!!!");
            return;
        }
        // These service set into ServiceManager by AMS when bind add.
        // We should only install hooks for no-system-server process
        installServiceManagerCacheHook();
        installActivityManager();
        installActivityTaskManager();
    }

    private final Singleton<IActivityManager> IActivityManagerSingletonProxy =
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

    private final Singleton<IActivityTaskManager> IActivityTaskManagerSingleton =
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


    private void installActivityManager() {
        // private static final Singleton<IActivityManager> IActivityManagerSingleton
        XposedHelpers.setStaticObjectField(ActivityManager.class, "IActivityManagerSingleton", IActivityManagerSingletonProxy);
        XLog.d("installActivityManager done");
    }

    private void installActivityTaskManager() {
        // private static final Singleton<IActivityTaskManager> IActivityTaskManagerSingleton
        XposedHelpers.setStaticObjectField(ActivityTaskManager.class, "IActivityTaskManagerSingleton", IActivityTaskManagerSingleton);
        XLog.d("installActivityTaskManager done");
    }

    private void installServiceManagerCacheHook() {
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
}
