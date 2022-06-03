package github.tornaco.android.thanos.core.wm;

import android.os.UserHandle;

import java.util.List;

import github.tornaco.android.thanos.core.pm.Pkg;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class WindowManager {

    private final IWindowManager server;

    @SneakyThrows
    public void setDialogForceCancelable(String packageName, boolean forceCancelable) {
        server.setDialogForceCancelable(packageName, forceCancelable);
    }

    @SneakyThrows
    public boolean isDialogForceCancelable(String packageName) {
        return server.isDialogForceCancelable(packageName);
    }

    @SneakyThrows
    public List<WindowState> getVisibleWindows() {
        return server.getVisibleWindows();
    }

    @SneakyThrows
    public boolean hasVisibleWindows(String pkgName) {
        return getVisibleWindows().stream().anyMatch(windowState -> windowState.packageName.equals(pkgName));
    }

    @SneakyThrows
    public boolean hasVisibleWindows(Pkg pkg) {
        return getVisibleWindows().stream().anyMatch(windowState -> windowState.packageName.equals(pkg.getPkgName())
                && UserHandle.getUserId(windowState.uid) == pkg.getUserId());
    }
}
