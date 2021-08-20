package github.tornaco.android.thanox.magisk.bridge;

import static github.tornaco.android.thanox.magisk.bridge.Logging.logging;

import android.os.Build;
import android.util.Log;

import github.tornaco.android.thanos.core.util.OsUtils;
import github.tornaco.android.thanox.magisk.bridge.proxy.SystemPropProxy;
import github.tornaco.android.thanox.magisk.bridge.proxy.SystemServiceRegistryProxy;

public class ThanoxBridge {

    public static void main(String arg) {
        if (!OsUtils.isROrAbove()) {
            System.err.println("Not supported android version: " + Build.VERSION.SDK_INT);
            return;
        }

        logging("ThanoxBridge, Bridge main with args: %s", arg);
        switch (arg) {
            case "system":
                ClassLoaderPatch.install();
                break;
            case "app":
                SystemServiceRegistryProxy.install();
                break;
        }
    }

    @Deprecated
    public static String getProp(String key) {
        if (key == null) {
            return null;
        }
        try {
            return SystemPropProxy.getProp(key);
        } catch (Throwable e) {
            logging("SystemPropProxy.getProp error %s", Log.getStackTraceString(e));
            return null;
        }
    }

    // Deprecated： Use Java hook instead.
    @Deprecated
    public static native void nativeInstallAppHook();
}
