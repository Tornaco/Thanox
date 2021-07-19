package github.tornaco.android.thanos.core.app.infinite;

interface IEnableCallback {
    oneway void onSuccess(int userId);
    oneway void onError(String errorMessage, int errorCode);
}