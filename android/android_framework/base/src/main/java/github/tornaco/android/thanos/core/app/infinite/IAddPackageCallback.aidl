package github.tornaco.android.thanos.core.app.infinite;

interface IAddPackageCallback {
    oneway void onSuccess(int userId);
    oneway void onError(String errorMessage, int errorCode);
}