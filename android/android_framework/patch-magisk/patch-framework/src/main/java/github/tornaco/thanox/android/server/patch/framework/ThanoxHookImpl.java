package github.tornaco.thanox.android.server.patch.framework;

import android.app.ActivityManagerInternal;
import android.app.ActivityThread;
import android.app.Application;
import android.app.LoadedApk;
import android.content.Context;
import android.os.Build;
import android.os.ServiceManager;

import com.android.server.LocalServices;
import com.elvishew.xlog.XLog;

import java.util.HashSet;
import java.util.Set;

import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.core.util.AbstractSafeR;
import github.tornaco.android.thanos.services.BootStrap;

public class ThanoxHookImpl implements IThanoxHook {
    private static final Set<String> FEATURES = new HashSet<>();

    static {
        FEATURES.add(BuildProp.THANOX_FEATURE_RECENT_TASK_REMOVAL);
        FEATURES.add(BuildProp.THANOX_FEATURE_BG_TASK_CLEAN);
        FEATURES.add(BuildProp.THANOX_FEATURE_EXT_APP_SMART_FREEZE);

        FEATURES.add(BuildProp.THANOX_FEATURE_PRIVACY_OPS_REMINDER);
        FEATURES.add(BuildProp.THANOX_FEATURE_PRIVACY_DATA_CHEAT);
        FEATURES.add(BuildProp.THANOX_FEATURE_COMPONENT_MANAGER);
        FEATURES.add(BuildProp.THANOX_FEATURE_APP_TRAMPOLINE);
        FEATURES.add(BuildProp.THANOX_FEATURE_PROFILE);
        FEATURES.add(BuildProp.THANOX_FEATURE_APP_SMART_SERVICE_STOPPER);
        FEATURES.add(BuildProp.THANOX_FEATURE_START_BLOCKER);
        FEATURES.add(BuildProp.THANOX_FEATURE_APP_SMART_STAND_BY);
        FEATURES.add(BuildProp.THANOX_FEATURE_EXT_N_RECORDER);
        FEATURES.add(BuildProp.THANOX_FEATURE_PRIVACY_APPLOCK);

        FEATURES.add(BuildProp.THANOX_FEATURE_RECENT_TASK_FORCE_EXCLUDE);

        if (BuildProp.THANOS_BUILD_DEBUG) {
            FEATURES.add(BuildProp.THANOX_FEATURE_PLUGIN_SUPPORT);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            FEATURES.add(BuildProp.THANOX_FEATURE_PROFILE_A11Y);
            FEATURES.add(BuildProp.THANOX_FEATURE_PRIVACY_FIELD_MEID);
            FEATURES.add(BuildProp.THANOX_FEATURE_PRIVACY_FIELD_IMEI);
        }
    }

    @Override
    public void installHooks(boolean isSystemServer) {
        XLog.d("ThanoxHookImpl, installHooks...isSystemServer? " + isSystemServer);
        if (isSystemServer) {
            waitForSystemReady(new AbstractSafeR() {
                @Override
                public void runSafety() {
                    ActivityThread at = ActivityThread.currentActivityThread();
                    XLog.w("ActivityThread.currentActivityThread()= " + at);
                    if (at == null) {
                        return;
                    }
                    Application application = at.getApplication();
                    XLog.w("ActivityThread.getApplication()= " + application);
                    Context context = at.getSystemContext();
                    XLog.w("ActivityThread.getSystemContext()= " + context);

                    LoadedApk loadedApk = application.mLoadedApk;
                    XLog.w("ActivityThread.loadedApk()= " + loadedApk);

                    final ClassLoader classLoader = loadedApk.getClassLoader();
                    XLog.w("ActivityThread.classLoader= " + classLoader);

                    if (context != null) {
                        BootStrap.main("Magisk", FEATURES.toArray(new String[0]));
                        BootStrap.start(context);
                        BootStrap.ready();
                        SystemServerHooks.install(classLoader);
                        XLog.w("Invoke BootStrap!");
                    }
                }
            });
        }
    }

    @SuppressWarnings("BusyWait")
    private void waitForSystemReady(Runnable runnable) {
        XLog.w("waitForSystemReady, current thread: %s", Thread.currentThread());
        new Thread(() -> {
            ServiceManager.waitForService("package");
            ServiceManager.waitForService("activity");
            ServiceManager.waitForService(Context.USER_SERVICE);
            ServiceManager.waitForService(Context.APP_OPS_SERVICE);
            while (!ActivityThread.isSystem() || !isSystemReady()) {
                try {
                    XLog.d("waitForSystemReady, wait a moment.");
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // Noop.
                }
            }
            runnable.run();
        }).start();
    }

    public static boolean isSystemReady() {
        if (!ActivityThread.isSystem()) {
            return false;
        }
        ActivityManagerInternal activityManagerInternal =
                LocalServices.getService(ActivityManagerInternal.class);
        return activityManagerInternal != null && activityManagerInternal.isSystemReady();
    }
}
