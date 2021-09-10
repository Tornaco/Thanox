package github.tornaco.android.thanox.magisk.bridge;

import android.content.pm.ApplicationInfo;

public interface ProcessHandler {
    void onSystemServerProcess();

    void onAppProcess(ApplicationInfo currentApp);
}
