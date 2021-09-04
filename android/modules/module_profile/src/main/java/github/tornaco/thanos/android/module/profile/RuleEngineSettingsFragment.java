package github.tornaco.thanos.android.module.profile;

import android.os.Bundle;

import androidx.preference.SwitchPreferenceCompat;

import java.util.Objects;

import github.tornaco.android.thanos.BaseWithFabPreferenceFragmentCompat;
import github.tornaco.android.thanos.core.app.ThanosManager;

public class RuleEngineSettingsFragment extends BaseWithFabPreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.module_profile_rule_engines, rootKey);
    }

    @Override
    protected void onBindPreferences() {
        super.onBindPreferences();
        ThanosManager thanos = ThanosManager.from(getContext());
        if (!thanos.isServiceInstalled()) {
            getPreferenceScreen().setEnabled(false);
            return;
        }

        SwitchPreferenceCompat automationPref = findPreference(getString(R.string.module_profile_pref_key_rule_engine_automation));
        assert automationPref != null;
        automationPref.setVisible(false);
        automationPref.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean value = (boolean) newValue;
            thanos.getProfileManager().setProfileEngineUiAutomationEnabled(value);
            return true;
        });

        SwitchPreferenceCompat pushPref = findPreference(getString(R.string.module_profile_pref_key_rule_engine_push));
        Objects.requireNonNull(pushPref).setChecked(thanos.getProfileManager().isProfileEnginePushEnabled());
        pushPref.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean value = (boolean) newValue;
            thanos.getProfileManager().setProfileEnginePushEnabled(value);
            return true;
        });

    }
}
