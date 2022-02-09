package github.tornaco.android.thanox.magisk.bridge;

import static github.tornaco.android.thanos.core.util.AppUtils.currentApplicationInfo;

import android.app.ActivityThread;
import android.content.pm.ApplicationInfo;

import com.elvishew.xlog.XLog;

import github.tornaco.android.thanox.magisk.bridge.proxy.SystemServiceRegistryHookInstaller;
import util.Consumer;

public class ProcessHookInstaller implements ProcessHandler {
    private static final long WAIT_ACTIVITY_INTERVAL = 50;
    private static final long WAIT_ACTIVITY_TIMES = 30;

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
                onStartApplication(processName);
            } else {
                XLog.w("ProcessHookInstaller, timeout waitForActivityThread for process: %s", processName);
            }
        });
    }

    @SuppressWarnings("BusyWait")
    private void waitForActivityThreadAsync(Consumer<Boolean> consumer) {
        new Thread(() -> {
            int times = 0;
            while (ActivityThread.currentOpPackageName() == null && times < WAIT_ACTIVITY_TIMES) {
                try {
                    Thread.sleep(WAIT_ACTIVITY_INTERVAL);
                    times += 1;
                } catch (InterruptedException e) {
                    // Noop.
                }
            }
            consumer.accept(ActivityThread.currentOpPackageName() != null);
        }).start();
    }

    private void onStartApplication(String processName) {
        XLog.d("ProcessHookInstaller onStartApplication: %s", processName);
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