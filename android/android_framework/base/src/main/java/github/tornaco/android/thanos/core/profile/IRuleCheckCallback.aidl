package github.tornaco.android.thanos.core.profile;

interface IRuleCheckCallback {
    oneway void onValid();
    oneway void onInvalid(int errorCode, String errorMessage);
}