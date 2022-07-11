package github.tornaco.android.thanos.core.app.infinite;

interface IRemovePackageCallback {
    oneway void onSuccess();
    oneway void onError(String errorMessage, int errorCode);
}