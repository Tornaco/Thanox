package github.tornaco.android.thanos.core.util;

import android.app.ActivityThread;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import util.XposedHelpers;

public class AppUtils {

    /**
     * Returns information about the main application in the current process.
     *
     * <p>In a few cases, multiple apps might run in the same process, e.g. the SystemUI and the
     * Keyguard which both have {@code android:process="com.android.systemui"} set in their
     * manifest. In those cases, the first application that was initialized will be returned.
     */
    public static ApplicationInfo currentApplicationInfo() {
        ActivityThread am = ActivityThread.currentActivityThread();
        if (am == null)
            return null;

        Object boundApplication = XposedHelpers.getObjectField(am, "mBoundApplication");
        if (boundApplication == null)
            return null;

        return (ApplicationInfo) XposedHelpers.getObjectField(boundApplication, "appInfo");
    }

    /**
     * Returns the name of the current process. It's usually the same as the main package name.
     */
    public static String currentProcessName() {
        String processName = ActivityThread.currentPackageName();
        if (processName == null)
            return "android";
        return processName;
    }

    public static Application currentApplication() {
        ActivityThread am = ActivityThread.currentActivityThread();
        if (am == null)
            return null;
        return am.getApplication();
    }
}
