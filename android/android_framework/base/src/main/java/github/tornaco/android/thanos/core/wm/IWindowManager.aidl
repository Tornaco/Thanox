package github.tornaco.android.thanos.core.wm;

interface IWindowManager {
    int[] getScreenSize();

    void setDialogForceCancelable(String packageName, boolean forceCancelable);
    boolean isDialogForceCancelable(String packageName);
    void reportDialogHasBeenForceSetCancelable(String packageName);
}