package github.tornaco.android.thanos.core.app.infinite;

import java.util.List;

import github.tornaco.android.thanos.core.annotation.NonNull;
import github.tornaco.android.thanos.core.pm.AppInfo;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class InfiniteZManager {
    public static final String PROFILE_INF = "android.os.usertype.profile.THANOX.INFZ";
    private final InfiniteZ server;

    @SneakyThrows
    public boolean isEnabled() {
        return server.isEnabled();
    }

    @SneakyThrows
    public void setEnabled(boolean enable, @NonNull EnableCallback callback) {
        server.setEnabled(enable, callback);
    }

    @SneakyThrows
    public void addPackage(String pkg, @NonNull AddPackageCallback callback) {
        server.addPackage(pkg, callback);
    }

    @SneakyThrows
    public void removePackage(String pkg, @NonNull RemovePackageCallback callback) {
        server.removePackage(pkg, callback);
    }

    @SneakyThrows
    public void launchPackage(String pkg, @NonNull LaunchPackageCallback callback) {
        server.launchPackage(pkg, callback);
    }

    @SneakyThrows
    public List<AppInfo> getInstalledPackages() {
        return server.getInstalledPackages();
    }
}
