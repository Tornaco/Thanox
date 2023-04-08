package github.tornaco.android.thanos.settings;

import android.os.Bundle;

import androidx.preference.SwitchPreferenceCompat;

import github.tornaco.android.thanos.BasePreferenceFragmentCompat;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.core.app.ThanosManager;

public class GeneralSettingsFragment extends BasePreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.general_settings_pref, rootKey);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onBindPreferences() {
        super.onBindPreferences();
        ThanosManager thanos = ThanosManager.from(getContext());
        if (thanos.isServiceInstalled()) {
            SwitchPreferenceCompat powerSavePref = findPreference(getString(R.string.key_enable_power_save));
            powerSavePref.setChecked(thanos.getPowerManager().isPowerSaveModeEnabled());
            powerSavePref.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean checked = (boolean) newValue;
                thanos.getPowerManager().setPowerSaveModeEnabled(checked);
                return true;
            });

            SwitchPreferenceCompat globalWhitelistPref = findPreference(getString(R.string.key_enable_global_white_list));
            if (thanos.isServiceInstalled()) {
                globalWhitelistPref.setChecked(thanos.getPkgManager().isProtectedWhitelistEnabled());
                globalWhitelistPref.setOnPreferenceChangeListener((preference, newValue) -> {
                    boolean checked = (boolean) newValue;
                    thanos.getPkgManager().setProtectedWhitelistEnabled(checked);
                    return true;
                });
            }


        }
    }
}
