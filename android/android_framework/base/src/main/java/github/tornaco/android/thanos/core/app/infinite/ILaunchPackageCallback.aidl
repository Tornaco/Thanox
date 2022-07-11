package github.tornaco.android.thanos.core.app.infinite;

interface ILaunchPackageCallback {
    oneway void onSuccess();
    oneway void onError(String errorMessage, int errorCode);
}