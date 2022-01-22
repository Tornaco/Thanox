package github.tornaco.android.thanos.core.profile;

interface IRuleChangeListener {
    oneway void onRuleEnabledStateChanged(int ruleId, boolean enabled);
    oneway void onRuleUpdated(int ruleId);
    oneway void onRuleRemoved(int ruleId);
    oneway void onRuleAdd(int ruleId);
}