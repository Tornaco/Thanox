package github.tornaco.android.thanos.core.profile;

import java.util.List;

import lombok.SneakyThrows;

public class ProfileManager {
    public static final int RULE_FORMAT_JSON = 0;
    public static final int RULE_FORMAT_YAML = 1;

    public static final String PROFILE_AUTO_APPLY_NEW_INSTALLED_APPS_CONFIG_TEMPLATE_PACKAGE_PREFIX
            = "thanox.config.template.";

    private final IProfileManager server;

    public ProfileManager(IProfileManager server) {
        this.server = server;
    }

    @SneakyThrows
    public void setAutoApplyForNewInstalledAppsEnabled(boolean enable) {
        server.setAutoApplyForNewInstalledAppsEnabled(enable);
    }

    @SneakyThrows
    public boolean isAutoApplyForNewInstalledAppsEnabled() {
        return server.isAutoApplyForNewInstalledAppsEnabled();
    }

    @SneakyThrows
    public void addRule(String ruleString, RuleAddCallback callback, int format) {
        server.addRule(ruleString, callback.getStub(), format);
    }

    @SneakyThrows
    public void updateRule(int ruleId, String ruleJson, RuleAddCallback callback, int format) {
        server.updateRule(ruleId, ruleJson, callback.getStub(), format);
    }

    @SneakyThrows
    public void addRuleIfNotExists(String ruleString, RuleAddCallback callback, int format) {
        server.addRuleIfNotExists(ruleString, callback.getStub(), format);
    }

    @SneakyThrows
    public void deleteRule(int ruleId) {
        server.deleteRule(ruleId);
    }

    @SneakyThrows
    public boolean enableRule(int ruleId) {
        return server.enableRule(ruleId);
    }

    @SneakyThrows
    public boolean disableRule(int ruleId) {
        return server.disableRule(ruleId);
    }

    @SneakyThrows
    public void checkRule(String ruleJson, RuleCheckCallback callback, int format) {
        server.checkRule(ruleJson, callback.getStub(), format);
    }

    @SneakyThrows
    public void isRuleEnabled(int ruleId) {
        server.isRuleEnabled(ruleId);
    }

    @SneakyThrows
    public RuleInfo[] getAllRules() {
        return server.getAllRules();
    }

    @SneakyThrows
    public RuleInfo[] getEnabledRules() {
        return server.getEnabledRules();
    }

    @SneakyThrows
    public void setProfileEnabled(boolean enable) {
        server.setProfileEnabled(enable);
    }

    @SneakyThrows
    public boolean isProfileEnabled() {
        return server.isProfileEnabled();
    }

    @SneakyThrows
    public boolean addGlobalRuleVar(String varName, String[] varArray) {
        return server.addGlobalRuleVar(varName, varArray);
    }

    @SneakyThrows
    public boolean appendGlobalRuleVar(String varName, String[] varArray) {
        return server.appendGlobalRuleVar(varName, varArray);
    }

    @SneakyThrows
    public boolean removeGlobalRuleVar(String varName) {
        return server.removeGlobalRuleVar(varName);
    }

    @SneakyThrows
    public String[] getAllGlobalRuleVarNames() {
        return server.getAllGlobalRuleVarNames();
    }

    @SneakyThrows
    public String[] getGlobalRuleVarByName(String varName) {
        return server.getGlobalRuleVarByName(varName);
    }

    @SneakyThrows
    public boolean isGlobalRuleVarByNameExists(String varName) {
        return server.isGlobalRuleVarByNameExists(varName);
    }

    @SneakyThrows
    public GlobalVar[] getAllGlobalRuleVar() {
        return server.getAllGlobalRuleVar();
    }

    @SneakyThrows
    public void setProfileEngineUiAutomationEnabled(boolean enabled) {
        server.setProfileEngineUiAutomationEnabled(enabled);
    }

    @SneakyThrows
    public boolean isProfileEngineUiAutomationEnabled() {
        return server.isProfileEngineUiAutomationEnabled();
    }

    @SneakyThrows
    public void setProfileEnginePushEnabled(boolean enabled) {
        server.setProfileEnginePushEnabled(enabled);
    }

    @SneakyThrows
    public boolean isProfileEnginePushEnabled() {
        return server.isProfileEnginePushEnabled();
    }

    @SneakyThrows
    public void setShellSuSupportInstalled(boolean enable) {
        server.setShellSuSupportInstalled(enable);
    }

    @SneakyThrows
    public boolean isShellSuSupportInstalled() {
        return server.isShellSuSupportInstalled();
    }

    @SneakyThrows
    public boolean addConfigTemplate(ConfigTemplate template) {
        return server.addConfigTemplate(template);
    }

    @SneakyThrows
    public boolean deleteConfigTemplate(ConfigTemplate template) {
        return server.deleteConfigTemplate(template);
    }

    @SneakyThrows
    public List<ConfigTemplate> getAllConfigTemplates() {
        return server.getAllConfigTemplates();
    }

    @SneakyThrows
    public ConfigTemplate getConfigTemplateById(String id) {
        return server.getConfigTemplateById(id);
    }

    @SneakyThrows
    public void setAutoConfigTemplateSelection(String id) {
        server.setAutoConfigTemplateSelection(id);
    }

    @SneakyThrows
    public String getAutoConfigTemplateSelectionId() {
        return server.getAutoConfigTemplateSelectionId();
    }

    @SneakyThrows
    public boolean applyConfigTemplateForPackage(String packageName, ConfigTemplate template) {
        return server.applyConfigTemplateForPackage(packageName, template);
    }

    @SneakyThrows
    public void publishStringFact(String factValue, long delayMills) {
        server.publishStringFact(factValue, delayMills);
    }
}
