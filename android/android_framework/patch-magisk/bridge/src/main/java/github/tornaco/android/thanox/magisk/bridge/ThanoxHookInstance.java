package github.tornaco.android.thanox.magisk.bridge;

import android.annotation.Nullable;
import android.util.Log;

import java.io.File;

import dalvik.system.PathClassLoader;

import static github.tornaco.android.thanox.magisk.bridge.Logging.logging;

public class ThanoxHookInstance {
    private static final String THANOX_MAGISK_SERVER_CLASS_NAME = "github.tornaco.thanox.android.server.patch.framework.ThanoxHookImpl";
    @Nullable
    private Object thanox;

    private static ThanoxHookInstance sIns = null;

    public static void init() {
        if (sIns == null) {
            sIns = new ThanoxHookInstance();
        }
    }

    public static ThanoxHookInstance get() {
        if (sIns == null) {
            sIns = new ThanoxHookInstance();
        }
        return sIns;
    }

    public ThanoxHookInstance() {
        logging("Delegating construct");
        try {
            long start = System.currentTimeMillis();
            this.thanox = getThanox();
            logging("thanox: %s %s", thanox, (System.currentTimeMillis() - start));
        } catch (Throwable e) {
            logging("getThanox error: %s", Log.getStackTraceString(e));
        }
    }

    public void install(boolean isSystemServer) {
        if (this.thanox != null) {
            ReflectionUtils.invokeMethod(ReflectionUtils.findMethod(thanox.getClass(), "installHooks", boolean.class), this.thanox, isSystemServer);
        } else {
            logging("Init fail. Thanox ins is null.");
        }
    }

    @Nullable
    private static Object getThanox() {
        // Check row first.
        File rowApkPath = PackageUtils.getApkPath("github.tornaco.android.thanos.pro");
        logging("Row apk path: %s", rowApkPath);
        if (rowApkPath != null) {
            return getThanox(rowApkPath);
        }
        // Prc then.
        File prcApkPath = PackageUtils.getApkPath("github.tornaco.android.thanos");
        logging("Prc apk path: %s", prcApkPath);
        if (prcApkPath != null) {
            return getThanox(prcApkPath);
        }
        logging("Can not detect thanox apk path, give up.");
        return null;
    }

    @Nullable
    private static Object getThanox(File apkPath) {
        try {
            ClassLoader appCl =
                    new PathClassLoader(
                            apkPath.getAbsolutePath(), Thread.currentThread().getContextClassLoader());
            Class<?> tsm =
                    appCl.loadClass(THANOX_MAGISK_SERVER_CLASS_NAME);
            logging("tsm=%s", tsm);
            Object thanox = tsm.newInstance();
            logging("Thanox created and Context has been set: %s", thanox);
            return thanox;
        } catch (Throwable e) {
            logging("Error invoke: %s", Log.getStackTraceString(e));
            return null;
        }
    }
}
