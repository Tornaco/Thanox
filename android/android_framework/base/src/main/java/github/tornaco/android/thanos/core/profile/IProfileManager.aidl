package github.tornaco.android.thanos.core.profile;

import github.tornaco.android.thanos.core.profile.IRuleAddCallback;
import github.tornaco.android.thanos.core.profile.IRuleCheckCallback;
import github.tornaco.android.thanos.core.profile.ConfigTemplate;

interface IProfileManager {

    void setAutoApplyForNewInstalledAppsEnabled(boolean enable);
    boolean isAutoApplyForNewInstalledAppsEnabled();

    void addRule(String ruleJson, in IRuleAddCallback callback, int format);
    void deleteRule(int ruleId);

    boolean enableRule(int ruleId);
    boolean disableRule(int ruleId);
    boolean isRuleEnabled(int ruleId);
    boolean isRuleExists(int ruleId);

    void checkRule(String ruleJson, in IRuleCheckCallback callback, int format);

    RuleInfo[] getAllRules();
    RuleInfo[] getEnabledRules();

    void setProfileEnabled(boolean enable);
    boolean isProfileEnabled();

    boolean addGlobalRuleVar(String varName, in String[] varArray);
    boolean appendGlobalRuleVar(String varName, in String[] varArray);
    boolean removeGlobalRuleVar(String varName);

    String[] getAllGlobalRuleVarNames();
    String[] getGlobalRuleVarByName(String varName);
    GlobalVar[] getAllGlobalRuleVar();
    boolean isGlobalRuleVarByNameExists(String varName);

    void setProfileEngineUiAutomationEnabled(boolean enabled);
    boolean isProfileEngineUiAutomationEnabled();

    void setProfileEnginePushEnabled(boolean enabled);
    boolean isProfileEnginePushEnabled();

    void setShellSuSupportInstalled(boolean enable);
    boolean isShellSuSupportInstalled();

    boolean addConfigTemplate(in ConfigTemplate template);
    boolean deleteConfigTemplate(in ConfigTemplate template);
    List<ConfigTemplate> getAllConfigTemplates();

    ConfigTemplate getConfigTemplateById(String id);

    void setAutoConfigTemplateSelection(String id);
    String getAutoConfigTemplateSelectionId();

    boolean applyConfigTemplateForPackage(String packageName, in ConfigTemplate template);

    void addRuleIfNotExists(String ruleJson, in IRuleAddCallback callback, int format);

    void publishStringFact(String factValue, long delayMills);

    void updateRule(int ruleId, String ruleJson, in IRuleAddCallback callback, int format);
}