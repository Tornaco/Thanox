package github.tornaco.android.thanos.core.profile;

interface IRuleAddCallback {
    oneway void onRuleAddSuccess();
    oneway void onRuleAddFail(int errorCode, String errorMessage);
}