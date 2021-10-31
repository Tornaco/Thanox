package github.tornaco.android.thanos.settings;

import android.os.Bundle;
import android.widget.Toast;

import github.tornaco.android.thanos.BasePreferenceFragmentCompat;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.ThanosApp;
import github.tornaco.android.thanos.app.donate.DonateSettings;

public class SettingsDashboardFragment extends BasePreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_dashboard, rootKey);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onBindPreferences() {
        super.onBindPreferences();
        findPreference(getString(R.string.key_app_ui)).setOnPreferenceClickListener(preference -> {
            UiSettingsActivity.start(getActivity());
            return true;
        });
        findPreference(getString(R.string.key_data)).setOnPreferenceClickListener(preference -> {
            if (ThanosApp.isPrc() && !DonateSettings.isActivated(getActivity())) {
                Toast.makeText(getActivity(), R.string.module_donate_donated_available, Toast.LENGTH_SHORT).show();
                return false;
            }
            DataSettingsActivity.start(getActivity());
            return true;
        });
        findPreference(getString(R.string.key_strategy)).setOnPreferenceClickListener(preference -> {
            StrategySettingsActivity.start(getActivity());
            return true;
        });
        findPreference(getString(R.string.key_developer)).setOnPreferenceClickListener(preference -> {
            DevSettingsActivity.start(getActivity());
            return true;
        });
        findPreference(getString(R.string.key_power)).setOnPreferenceClickListener(preference -> {
            PowerSettingsActivity.start(getActivity());
            return true;
        });
        findPreference(getString(R.string.key_about)).setOnPreferenceClickListener(preference -> {
            AboutSettingsActivity.start(getActivity());
            return false;
        });
    }
}
