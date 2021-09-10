package github.tornaco.android.thanox.magisk.bridge.proxy;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.IActivityTaskManager;
import android.app.TaskInfo;
import android.content.Intent;
import android.content.pm.ParceledListSlice;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.util.Log;

import com.elvishew.xlog.XLog;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import github.tornaco.android.thanos.core.app.ThanosManagerNative;
import github.tornaco.android.thanos.core.util.PkgUtils;
import util.ObjectsUtils;

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
                                XLog.d("IActivityTaskManager %s %s", method.getName(), Arrays.toString(args));
                                // Early return...
                                if (!ThanosManagerNative.isServiceInstalled()) {
                                    XLog.d("IActivityTaskManager, Thanox not installed...");
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
                                    } else if ("getRecentTasks".equals(method.getName())) {
                                        return handleGetRecentTask(am, method, args);
                                    }
                                } catch (Throwable e) {
                                    XLog.d("Error handle IActivityTaskManager" + Log.getStackTraceString(e));
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
        XLog.d("ActivityTaskManagerProxyProvider reportOnRemoveTask");
    }

    private void handleActivityResumed(Object[] args) throws android.os.RemoteException {
        ThanosManagerNative.getDefault()
                .getActivityStackSupervisor()
                .reportOnActivityResumed((IBinder) args[0]);
        XLog.d("ActivityTaskManagerProxyProvider handleActivityResumed");
    }

    private void handleActivityStopped(Object[] args) throws android.os.RemoteException {
        ThanosManagerNative.getDefault()
                .getActivityStackSupervisor()
                .reportOnActivityStopped((IBinder) args[0]);
        XLog.d("ActivityTaskManagerProxyProvider handleActivityStopped");
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
                XLog.d("handleStartActivity, Replacing Intent");
                args[intentIndex] = realIntent;
            }
        }
    }

    @SuppressWarnings("unchecked")
    @SuppressLint("NewApi" /* We will only support N above */)
    private ParceledListSlice<ActivityManager.RecentTaskInfo> handleGetRecentTask(
            IActivityTaskManager am,
            Method method,
            Object[] args) throws Throwable {
        ParceledListSlice<ActivityManager.RecentTaskInfo> recentTasks
                = (ParceledListSlice<ActivityManager.RecentTaskInfo>) tryInvoke(am, method, args);
        List<ActivityManager.RecentTaskInfo> taskInfoList = recentTasks.getList();
        XLog.d("getRecentTasks: %s", Arrays.toString(taskInfoList.toArray()));
        taskInfoList = taskInfoList.stream().filter((Predicate<TaskInfo>) taskInfo -> {
            try {
                return !shouldHideFromRecent(taskInfo);
            } catch (RemoteException e) {
                XLog.d("shouldHideFromRecent error");
                return true;
            }
        }).collect(Collectors.toList());
        return new ParceledListSlice<>(taskInfoList);
    }

    @SuppressLint("NewApi" /* We will only support P above */)
    private boolean shouldHideFromRecent(TaskInfo taskInfo) throws RemoteException {
        Intent intent = taskInfo.baseIntent;
        if (intent == null) {
            XLog.d("isVisibleRecentTask, intent is null for task: %s", taskInfo);
            return false;
        }
        String pkgName = PkgUtils.packageNameOf(intent);
        if (pkgName == null) {
            return false;
        }

        if (ObjectsUtils.equals(
                ThanosManagerNative.getDefault().getActivityStackSupervisor().getCurrentFrontApp(),
                pkgName)) {
            XLog.d("isVisibleRecentTask, %s is current top, won't check.", pkgName);
            return false;
        }

        int setting =
                ThanosManagerNative.getDefault()
                        .getActivityManager()
                        .getRecentTaskExcludeSettingForPackage(pkgName);
        boolean res = setting
                == github.tornaco.android.thanos.core.app.ActivityManager.ExcludeRecentSetting
                .EXCLUDE;
        XLog.d("isVisibleRecentTask hidden? %s %s", pkgName, res);
        return res;
    }
}
