package github.tornaco.android.thanox.magisk.bridge;

import static github.tornaco.android.thanos.core.util.AppUtils.currentApplicationInfo;

import android.app.ActivityThread;
import android.content.pm.ApplicationInfo;

import com.elvishew.xlog.XLog;

import github.tornaco.android.thanox.magisk.bridge.proxy.ActivityThreadProxy;
import github.tornaco.android.thanox.magisk.bridge.proxy.SystemServiceRegistryHookInstaller;

public class ProcessHookInstaller implements ProcessHandler {
    @Override
    public void onStartSystemServer() {
        XLog.d("ProcessHookInstaller onSystemServerProcess");
        SystemSeverProcessFrameworkHookInstaller.install(true);
    }

    @Override
    public void onStartApplication() {
        ActivityThread am = ActivityThread.currentActivityThread();
        if (am == null) {
            XLog.w("onStartApplication ActivityThread is null");
            return;
        }

        ApplicationInfo currentApp = currentApplicationInfo();
        if (currentApp == null) {
            XLog.w("onStartApplication ApplicationInfo is null");
            return;
        }

        XLog.d("ProcessHookInstaller onAppProcess: %s", currentApp);
        String pkgName = currentApp.packageName;
        if (pkgName == null) {
            XLog.w("onStartApplication packageName is null");
            return;
        }

        AppProcessSystemServiceHookInstaller.install();
        ActivityThreadProxy.install();
    }

    @Override
    public void onAppProcess() {
        // Hooks for Register.
        // It should run early on app process start.
        SystemServiceRegistryHookInstaller.install();
    }
}