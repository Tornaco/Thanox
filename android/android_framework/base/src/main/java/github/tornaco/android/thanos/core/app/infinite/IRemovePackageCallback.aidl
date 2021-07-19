package github.tornaco.android.thanos.core.app.infinite;

interface IRemovePackageCallback {
    void onSuccess();
    void onError(String errorMessage, int errorCode);
}