package github.tornaco.android.thanos.core.app.infinite;

import github.tornaco.android.thanos.core.app.infinite.IEnableCallback;
import github.tornaco.android.thanos.core.app.infinite.IAddPackageCallback;
import github.tornaco.android.thanos.core.app.infinite.IRemovePackageCallback;
import github.tornaco.android.thanos.core.app.infinite.ILaunchPackageCallback;

interface InfiniteZ {
    void setEnabled(boolean enable, in IEnableCallback callback);

    boolean isEnabled();

    void addPackage(String pkg, in IAddPackageCallback callback);

    void removePackage(String pkg, in IRemovePackageCallback callback);

    void launchPackage(String pkg, in ILaunchPackageCallback callback);

    List<AppInfo> getInstalledPackages();
}