package github.tornaco.android.thanox.magisk.bridge;

import android.content.pm.ApplicationInfo;

public interface ProcessHandler {
    void onStartSystemServer();

    void onStartApplication(ApplicationInfo currentApp);

    void onAppProcess();
}
