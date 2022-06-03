package github.tornaco.android.thanos.core.wm;

import github.tornaco.android.thanos.core.wm.WindowState;

interface IWindowManager {
    int[] getScreenSize();

    void setDialogForceCancelable(String packageName, boolean forceCancelable);
    boolean isDialogForceCancelable(String packageName);
    void reportDialogHasBeenForceSetCancelable(String packageName);

    List<WindowState> getVisibleWindows();
}