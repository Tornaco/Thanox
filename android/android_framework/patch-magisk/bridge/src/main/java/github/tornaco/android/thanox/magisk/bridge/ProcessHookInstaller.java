package github.tornaco.android.thanox.magisk.bridge;

import android.content.pm.ApplicationInfo;

import com.elvishew.xlog.XLog;

import github.tornaco.android.thanox.magisk.bridge.proxy.SystemServiceRegistryHookInstaller;

public class ProcessHookInstaller implements ProcessHandler {
    @Override
    public void onStartSystemServer() {
        XLog.d("ProcessHookInstaller onSystemServerProcess");
        SystemSeverProcessFrameworkHookInstaller.install(true);
    }

    @Override
    public void onStartApplication(ApplicationInfo currentApp) {
        XLog.d("ProcessHookInstaller onAppProcess: %s", currentApp);
        String pkgName = currentApp.packageName;
        if (pkgName == null) {
            return;
        }
        AppProcessSystemServiceHookInstaller.install();
    }

    @Override
    public void onAppProcess() {
        // Hooks for Register.
        // It should run early on app process start.
        SystemServiceRegistryHookInstaller.install();
    }
}
