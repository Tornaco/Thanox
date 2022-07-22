package github.tornaco.android.thanos.settings;

import android.os.Bundle;

import github.tornaco.android.thanos.BasePreferenceFragmentCompat;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.feature.access.AppFeatureManager;

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
            AppFeatureManager.INSTANCE.withSubscriptionStatus(requireContext(), isSubscribed -> {
                if (isSubscribed) {
                    DataSettingsActivity.start(getActivity());
                } else {
                    AppFeatureManager.INSTANCE.showDonateIntroDialog(requireActivity());
                }
                return null;
            });
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
