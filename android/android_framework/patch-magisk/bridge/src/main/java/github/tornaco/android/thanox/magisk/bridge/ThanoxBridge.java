package github.tornaco.android.thanox.magisk.bridge;

import android.os.Build;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.Printer;

import github.tornaco.android.thanos.core.util.AbstractSafeR;
import github.tornaco.android.thanos.core.util.OsUtils;

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

    private void run(String arg) {
        if (!OsUtils.isROrAbove()) {
            System.err.println("Not supported android version: " + Build.VERSION.SDK_INT);
            return;
        }

        NativeEvent event = NativeEvent.valueOf(arg);

        XLog.d("ThanoxBridge, Bridge main with args: %s", event);
        switch (event) {
            case postSpecializeSystemServer:
                onSystemServerProcess();
                break;
            case postSpecializeApp:
                onAppProcess();
                break;
        }
    }

    private void onSystemServerProcess() {
        new AbstractSafeR() {
            @Override
            public void runSafety() {
                processHandler.onSystemServerProcess();
            }
        }.setName("onStartSystemServer").run();
    }

    private void onAppProcess() {
        new AbstractSafeR() {
            @Override
            public void runSafety() {
                processHandler.onAppProcess();
            }
        }.setName("onAppProcess").run();
    }

    /**
     * Do not bother the typo and case.
     */
    enum NativeEvent {
        preSpecializeApp,
        preSpecializeSystemServer,
        postSpecializeApp,
        postSpecializeSystemServer,
    }
}
