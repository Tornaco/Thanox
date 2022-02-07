package github.tornaco.android.thanox.magisk.bridge;

import static github.tornaco.android.thanos.core.util.AppUtils.currentApplicationInfo;

import android.app.ActivityThread;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.os.HandlerThread;

import com.elvishew.xlog.XLog;

import github.tornaco.android.thanox.magisk.bridge.proxy.SystemServiceRegistryHookInstaller;
import util.Consumer;

public class ProcessHookInstaller implements ProcessHandler {
    private static final long WAIT_ACTIVITY_INTERVAL = 100;
    private static final long WAIT_ACTIVITY_TIMES = 20;

    @Override
    public void onSystemServerProcess() {
        XLog.d("ProcessHookInstaller onSystemServerProcess");
        SystemSeverProcessFrameworkHookInstaller.install(true);
    }

    @Override
    public void onAppProcess(String processName) {
        XLog.d("ProcessHookInstaller onAppProcess: %s", processName);
        // Hooks for Register.
        // It should run early on app process start.
        SystemServiceRegistryHookInstaller.install();
        waitForActivityThreadAsync(success -> {
            if (success) {
                onStartApplication();
            } else {
                XLog.w("ProcessHookInstaller, timeout waitForActivityThread for process: %s", processName);
            }
        });
    }

    private void waitForActivityThreadAsync(Consumer<Boolean> consumer) {
        HandlerThread hr = new HandlerThread("waitForActivityThread");
        hr.start();
        Handler h = new Handler(hr.getLooper());

        final int[] times = {0};

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ActivityThread.currentActivityThread() != null) {
                    consumer.accept(true);
                    hr.quitSafely();
                    return;
                }
                if (times[0] >= WAIT_ACTIVITY_TIMES) {
                    consumer.accept(false);
                    hr.quitSafely();
                    return;
                }
                times[0] += 1;
                h.postDelayed(this, WAIT_ACTIVITY_INTERVAL);
            }
        }, WAIT_ACTIVITY_INTERVAL);
    }

    private void onStartApplication() {
        XLog.d("ProcessHookInstaller onStartApplication");
        ActivityThread am = ActivityThread.currentActivityThread();
        if (am == null) {
            XLog.e("ProcessHookInstaller onStartApplication ActivityThread is null");
            return;
        }

        ApplicationInfo currentApp = currentApplicationInfo();
        if (currentApp == null) {
            XLog.w("ProcessHookInstaller onStartApplication ApplicationInfo is null");
            return;
        }

        XLog.d("ProcessHookInstaller onStartApplication, currentApp: %s", currentApp);
        String pkgName = currentApp.packageName;
        if (pkgName == null) {
            XLog.e("ProcessHookInstaller, onStartApplication packageName is null");
            return;
        }

        AppProcessSystemServiceHookInstaller.install();
    }
}