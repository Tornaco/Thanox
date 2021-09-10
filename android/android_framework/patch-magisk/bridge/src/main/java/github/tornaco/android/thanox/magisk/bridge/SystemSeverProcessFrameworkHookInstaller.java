package github.tornaco.android.thanox.magisk.bridge;

import android.annotation.Nullable;
import android.util.Log;

import com.elvishew.xlog.XLog;

import java.io.File;

import dalvik.system.PathClassLoader;
import github.tornaco.android.thanox.magisk.bridge.util.PackageUtils;
import github.tornaco.android.thanox.magisk.bridge.util.ReflectionUtils;

public class SystemSeverProcessFrameworkHookInstaller {
    private static final String THANOX_MAGISK_SERVER_CLASS_NAME = "github.tornaco.thanox.android.server.patch.framework.ThanoxHookImpl";

    public static void install(boolean isSystemServer) {
        try {
            long start = System.currentTimeMillis();
            Object thanox = getThanox();
            XLog.d("ThanoxHookInstance init, thanox: %s %s", thanox, (System.currentTimeMillis() - start));
            if (thanox == null) {
                XLog.d("ThanoxHookInstance init fail, thanox instance is null");
                return;
            }
            ReflectionUtils.invokeMethod(ReflectionUtils.findMethod(thanox.getClass(), "installHooks", boolean.class), thanox, isSystemServer);
        } catch (Throwable e) {
            XLog.d("ThanoxHookInstance install error: %s", Log.getStackTraceString(e));
        }
    }

    @Nullable
    private static Object getThanox() {
        // Check row first.
        File rowApkPath = PackageUtils.getApkPath("github.tornaco.android.thanos.pro");
        XLog.d("Row apk path: %s", rowApkPath);
        if (rowApkPath != null) {
            return getThanox(rowApkPath);
        }
        // Prc then.
        File prcApkPath = PackageUtils.getApkPath("github.tornaco.android.thanos");
        XLog.d("Prc apk path: %s", prcApkPath);
        if (prcApkPath != null) {
            return getThanox(prcApkPath);
        }
        XLog.d("Can not detect thanox apk path, give up.");
        return null;
    }

    @Nullable
    private static Object getThanox(File apkPath) {
        try {
            ClassLoader appCl =
                    new PathClassLoader(
                            apkPath.getAbsolutePath(), Thread.currentThread().getContextClassLoader());
            Class<?> thanoxHookImplClass =
                    appCl.loadClass(THANOX_MAGISK_SERVER_CLASS_NAME);
            XLog.d("thanoxHookImplClass=%s", thanoxHookImplClass);
            Object thanox = thanoxHookImplClass.newInstance();
            XLog.d("Thanox created and Context has been set: %s", thanox);
            return thanox;
        } catch (Throwable e) {
            XLog.d("Error getThanox: %s", Log.getStackTraceString(e));
            return null;
        }
    }
}
