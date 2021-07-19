package github.tornaco.android.thanos.core.app.infinite;

interface ILaunchPackageCallback {
    void onSuccess();
    void onError(String errorMessage, int errorCode);
}