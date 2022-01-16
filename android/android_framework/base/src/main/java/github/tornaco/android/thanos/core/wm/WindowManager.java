package github.tornaco.android.thanos.core.wm;

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
}
