package github.tornaco.android.thanox.magisk.bridge;

import android.content.pm.ApplicationInfo;

import com.elvishew.xlog.XLog;

public class ProcessHookInstaller implements ProcessHandler {
    @Override
    public void onSystemServerProcess() {
        XLog.d("ProcessHookInstaller onSystemServerProcess");
        SystemSeverProcessFrameworkHookInstaller.install(true);
    }

    @Override
    public void onAppProcess(ApplicationInfo currentApp) {
        XLog.d("ProcessHookInstaller onAppProcess: %s", currentApp);
        String pkgName = currentApp.packageName;
        if (pkgName == null) {
            return;
        }
        AppProcessSystemServiceHookInstaller.install();
    }
}
