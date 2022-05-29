package github.tornaco.android.thanos.core.profile;

interface IRuleCheckCallback {
    oneway void onValid(in RuleInfo rule);
    oneway void onInvalid(int errorCode, String errorMessage);
}