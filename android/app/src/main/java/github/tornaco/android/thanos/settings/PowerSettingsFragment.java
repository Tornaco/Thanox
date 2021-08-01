package github.tornaco.android.thanos.settings;

import android.os.Bundle;

import androidx.preference.SwitchPreferenceCompat;

import github.tornaco.android.thanos.BaseWithFabPreferenceFragmentCompat;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.core.app.ThanosManager;

public class PowerSettingsFragment extends BaseWithFabPreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.power_settings_pref, rootKey);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onBindPreferences() {
        super.onBindPreferences();
        ThanosManager thanos = ThanosManager.from(getContext());
        SwitchPreferenceCompat powerSavePref = findPreference(getString(R.string.key_enable_power_save));
        if (thanos.isServiceInstalled()) {
            powerSavePref.setChecked(thanos.getPowerManager().isPowerSaveModeEnabled());
            powerSavePref.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean checked = (boolean) newValue;
                thanos.getPowerManager().setPowerSaveModeEnabled(checked);
                return true;
            });
        }
    }
}
