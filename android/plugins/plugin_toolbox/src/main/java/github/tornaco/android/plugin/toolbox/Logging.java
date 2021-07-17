package github.tornaco.android.plugin.toolbox;

import android.util.Log;

import de.robv.android.xposed.XposedBridge;
import github.tornaco.android.thanos.core.IPluginLogger;
import github.tornaco.android.thanos.core.app.ThanosManagerNative;

public class Logging {

    private static IPluginLogger sLogger;

    public static void main(String... args) {
        try {
            if (ThanosManagerNative.isServiceInstalled()) {
                sLogger = ThanosManagerNative.getDefault().getPluginLogger(args[0]);
                Log.w("Logging", "Using plugin logger for plugin: " + sLogger);
                if (sLogger == null) {
                    sLogger = fallbackLogger();
                }
                sLogger.logV("Logger init: " + args[0]);
            }
        } catch (Throwable e) {
            // Fallback.
            sLogger = fallbackLogger();
            Log.w("Logging", "Using fallback logger for plugin: " + args[0]);
        }
    }

    private static IPluginLogger fallbackLogger() {
        return new IPluginLogger.Default();
    }

    public static void logV(String content) {
        ensureLogger();
        try {
            XposedBridge.log(content);
            sLogger.logV(content);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void logD(String content) {
        ensureLogger();
        try {
            XposedBridge.log(content);
            sLogger.logD(content);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void logW(String content) {
        ensureLogger();
        try {
            XposedBridge.log(content);
            sLogger.logW(content);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void logE(String content) {
        ensureLogger();
        try {
            XposedBridge.log(content);
            sLogger.logE(content);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void ensureLogger() {
        if (sLogger == null || sLogger.asBinder() == null) {
            main("Toolbox");
        }
    }
}
