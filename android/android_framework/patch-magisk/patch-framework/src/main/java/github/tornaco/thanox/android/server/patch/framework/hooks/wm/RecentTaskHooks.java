package github.tornaco.thanox.android.server.patch.framework.hooks.wm;

import android.annotation.SuppressLint;

import com.android.server.wm.ActivityStackSupervisor;
import com.android.server.wm.ActivityTaskManagerService;
import com.elvishew.xlog.XLog;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

import github.tornaco.android.thanos.core.util.AbstractSafeR;
import util.XposedHelpers;

/**
 * We use this hook to listen for task add and removal.
 */
public class RecentTaskHooks {

    static void installRecentTasksCallback(ActivityTaskManagerService atm) {
        new AbstractSafeR() {
            @Override
            public void runSafety() throws ClassNotFoundException {
                installRecentTasksCallback0(atm);
            }
        }.setName("RecentTaskHooks installRecentTasksCallback").run();
    }

    private static void installRecentTasksCallback0(ActivityTaskManagerService atm) throws ClassNotFoundException {
        final Object callbackShell = new String("RecentTaskHooks#callbackInstance");
        @SuppressLint("PrivateApi")
        Class<?> recentTaskCallbackClass = Class.forName("com.android.server.wm.RecentTasks$Callbacks");
        Object callbackInstance = Proxy.newProxyInstance(recentTaskCallbackClass.getClassLoader(),
                new Class[]{recentTaskCallbackClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object o, Method method, Object[] objects) {
                        XLog.d("RecentTaskHooks %s %s", method.getName(), Arrays.toString(objects));
                        if ("toString".equals(method.getName())) {
                            return callbackShell.toString();
                        }
                        if ("hashcode".equals(method.getName())) {
                            return callbackShell.hashCode();
                        }
                        return null;
                    }
                });
        XLog.i("RecentTaskHooks, callbackInstance: %s", callbackInstance);

        ActivityStackSupervisor ass = (ActivityStackSupervisor) XposedHelpers.getObjectField(atm, "mStackSupervisor");
        XLog.i("RecentTaskHooks ass: %s", ass);

        Object recentTasks = XposedHelpers.getObjectField(ass, "mRecentTasks");
        XLog.i("RecentTaskHooks recentTasks: %s", recentTasks);

        XposedHelpers.callMethod(recentTasks, "registerCallback", callbackInstance);
        XLog.i("RecentTaskHooks recentTasks installed.");
    }
}
