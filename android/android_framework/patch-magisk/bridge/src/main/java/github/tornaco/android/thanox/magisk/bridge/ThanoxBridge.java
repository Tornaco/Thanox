package github.tornaco.android.thanox.magisk.bridge;

import android.os.Build;
import android.util.Log;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.Printer;

import github.tornaco.android.thanos.core.util.OsUtils;
import github.tornaco.android.thanox.magisk.bridge.proxy.SystemPropProxy;
import github.tornaco.android.thanox.magisk.bridge.proxy.SystemServiceRegistryProxy;

public class ThanoxBridge {

    static {
        Printer androidPrinter = new AndroidPrinter();
        XLog.init(new LogConfiguration.Builder()
                        .logLevel(LogLevel.ALL)
                        .tag("Thanox-Magisk-Bridge")
                        .build(),
                androidPrinter);
    }

    public static void main(String arg) {
        if (!OsUtils.isROrAbove()) {
            System.err.println("Not supported android version: " + Build.VERSION.SDK_INT);
            return;
        }

        XLog.d("ThanoxBridge, Bridge main with args: %s", arg);
        switch (arg) {
            case "system":
                ClassLoaderPatchInstaller.install(new ProcessHookInstaller());
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
            XLog.d("SystemPropProxy.getProp error %s", Log.getStackTraceString(e));
            return null;
        }
    }

    // Deprecated： Use Java hook instead.
    @SuppressWarnings("JavaJniMissingFunction")
    @Deprecated
    public static native void nativeInstallAppHook();
}
