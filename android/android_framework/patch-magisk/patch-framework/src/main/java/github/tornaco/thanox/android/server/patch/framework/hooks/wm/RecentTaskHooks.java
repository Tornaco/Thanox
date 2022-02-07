package github.tornaco.thanox.android.server.patch.framework.hooks.wm;

import android.annotation.SuppressLint;

import com.elvishew.xlog.XLog;

import java.lang.reflect.Proxy;

import github.tornaco.android.thanos.core.util.AbstractSafeR;
import github.tornaco.android.thanos.services.BootStrap;
import github.tornaco.android.thanos.services.patch.common.wm.RecentTasksHelper;
import github.tornaco.android.thanos.services.patch.common.wm.XTask;
import github.tornaco.android.thanos.services.patch.common.wm.XTaskHelper;
import util.ProxyUtils;
import util.XposedHelpers;

/**
 * We use this hook to listen for task add and removal.
 */
public class RecentTaskHooks {

    static void installRecentTasksCallback(Object atm, ClassLoader classLoader) {
        new AbstractSafeR() {
            @Override
            public void runSafety() {
                installRecentTasksCallback0(atm, classLoader);
            }
        }.setName("RecentTaskHooks installRecentTasksCallback").run();
    }

    private static void installRecentTasksCallback0(Object atm, ClassLoader classLoader) {
        XLog.i("RecentTaskHooks, installRecentTasksCallback0: %s", atm);
        // com.android.server.wm.RecentTasks$Callbacks
        @SuppressLint("PrivateApi")
        Class<?> recentTaskCallbackClass = RecentTasksHelper.INSTANCE.recentTaskCallbacksClass(classLoader);
        Object callbackInstance = Proxy.newProxyInstance(recentTaskCallbackClass.getClassLoader(),
                new Class[]{recentTaskCallbackClass},
                (o, method, objects) -> {
                    if ("onRecentTaskAdded".equals(method.getName())) {
                        onRecentTaskAdded(objects);
                    } else if ("onRecentTaskRemoved".equals(method.getName())) {
                        onRecentTaskRemoved(objects);
                    }
                    return ProxyUtils.relaxedMethodCallRes("RecentTaskHooks#Callback", o, method, objects);
                });
        XLog.i("RecentTaskHooks, callbackInstance: %s", callbackInstance);

        Object recentTasks = XposedHelpers.getObjectField(atm, "mRecentTasks");
        XLog.i("RecentTaskHooks recentTasks: %s", recentTasks);

        XposedHelpers.callMethod(recentTasks, "registerCallback", callbackInstance);
        XLog.i("RecentTaskHooks recentTasks installed.");
    }

    private static void onRecentTaskAdded(Object[] args) {
        XTask tr = XTaskHelper.toXTask(args[0]);
        XLog.i("RecentTaskHooks onRecentTaskAdded: %s", tr);
        BootStrap.THANOS_X.getActivityManagerService().notifyTaskCreated(tr.getTaskId(), tr.getIntent().getComponent());
    }

    private static void onRecentTaskRemoved(Object[] args) {
        XTask tr = XTaskHelper.toXTask(args[0]);
        XLog.i("RecentTaskHooks onRecentTaskRemoved: %s", tr);
        BootStrap.THANOS_X.getActivityManagerService().onTaskRemoving(tr.getIntent(), tr.getUserId());
    }
}
