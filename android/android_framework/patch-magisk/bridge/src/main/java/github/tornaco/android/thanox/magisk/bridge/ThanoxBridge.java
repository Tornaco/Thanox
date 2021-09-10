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

public class ThanoxBridge {
    private final ProcessHandler processHandler = new ProcessHookInstaller();

    static {
        Printer androidPrinter = new AndroidPrinter();
        XLog.init(new LogConfiguration.Builder()
                        .logLevel(LogLevel.ALL)
                        .tag("Thanox-Magisk-Bridge")
                        .build(),
                androidPrinter);
    }

    public static void main(String arg) {
        new ThanoxBridge().run(arg);
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


    private void run(String arg) {
        if (!OsUtils.isROrAbove()) {
            System.err.println("Not supported android version: " + Build.VERSION.SDK_INT);
            return;
        }

        NativeEvent event = NativeEvent.valueOf(arg);

        XLog.d("ThanoxBridge, Bridge main with args: %s", event);
        switch (event) {
            case onRuntimeStart:
                ClassLoaderPatchInstaller.install(processHandler);
                break;
            case forkAndSpecializePostApp:
                processHandler.onAppProcess();
                break;
            case forkSystemServerPost:
            case specializeAppProcessPost:
                break;
        }
    }

    /**
     * Do not bother the typo and case.
     */
    enum NativeEvent {
        forkAndSpecializePostApp,
        specializeAppProcessPost,
        forkSystemServerPost,
        onRuntimeStart
    }
}
