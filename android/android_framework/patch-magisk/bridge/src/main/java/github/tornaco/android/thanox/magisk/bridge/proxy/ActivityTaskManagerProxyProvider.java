package github.tornaco.android.thanox.magisk.bridge.proxy;

import static github.tornaco.android.thanox.magisk.bridge.Logging.logging;

import android.app.IActivityTaskManager;
import android.content.Intent;
import android.os.IBinder;
import android.os.IInterface;
import android.util.Log;

import java.lang.reflect.Proxy;
import java.util.Arrays;

import github.tornaco.android.thanos.core.app.ThanosManagerNative;

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
                                // Early return...
                                if (!ThanosManagerNative.isServiceInstalled()) {
                                    logging("IActivityTaskManager, Thanox not installed...");
                                    return tryInvoke(am, method, args);
                                }

                                try {
                                    if ("removeTask".equals(method.getName())) {
                                        handleRemoveTask(args);
                                    } else if ("activityResumed".equals(method.getName())) {
                                        handleActivityResumed(args);
                                    } else if ("activityStopped".equals(method.getName())) {
                                        handleActivityStopped(args);
                                    } else if ("startActivity".equals(method.getName())) {
                                        handleStartActivity(args);
                                    }
                                } catch (Throwable e) {
                                    logging("Error handle IActivityTaskManager" + Log.getStackTraceString(e));
                                }

                                return tryInvoke(am, method, args);
                            });
                }
                return iInterface;
            }
        });
    }

    private void handleRemoveTask(Object[] args) throws android.os.RemoteException {
        ThanosManagerNative.getDefault()
                .getActivityManager()
                .reportOnRemoveTask((Integer) args[0]);
        logging("ActivityTaskManagerProxyProvider reportOnRemoveTask");
    }

    private void handleActivityResumed(Object[] args) throws android.os.RemoteException {
        ThanosManagerNative.getDefault()
                .getActivityStackSupervisor()
                .reportOnActivityResumed((IBinder) args[0]);
        logging("ActivityTaskManagerProxyProvider handleActivityResumed");
    }

    private void handleActivityStopped(Object[] args) throws android.os.RemoteException {
        ThanosManagerNative.getDefault()
                .getActivityStackSupervisor()
                .reportOnActivityStopped((IBinder) args[0]);
        logging("ActivityTaskManagerProxyProvider handleActivityStopped");
    }

    // Android 30.
    //  int startActivity(in IApplicationThread caller, in String callingPackage,
    //            in String callingFeatureId, in Intent intent, in String resolvedType,
    //            in IBinder resultTo, in String resultWho, int requestCode,
    //            int flags, in ProfilerInfo profilerInfo, in Bundle options);
    //    int startActivities(in IApplicationThread caller, in String callingPackage,
    private void handleStartActivity(Object[] args) throws android.os.RemoteException {
        String callingPackage = (String) args[1];
        // We'd better dynamic look up.
        // Android 29 -> 30 arg index has been changed.
        int intentIndex = Args.getFirstTypeOfArgIndexOr(
                Intent.class,
                args,
                "ActivityTaskManagerProxyProvider#handleStartActivity",
                -1);
        Intent intent = intentIndex > 0 ? (Intent) args[intentIndex] : null;
        if (callingPackage != null && intent != null) {
            Intent realIntent = ThanosManagerNative.getDefault()
                    .getActivityStackSupervisor()
                    .reportOnStartActivity(callingPackage, intent);
            if (realIntent != null) {
                logging("handleStartActivity, Replacing Intent");
                args[intentIndex] = realIntent;
            }
        }
    }
}
