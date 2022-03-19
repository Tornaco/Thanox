package github.tornaco.android.thanos.settings;

import android.os.Bundle;

import androidx.preference.SwitchPreferenceCompat;

import github.tornaco.android.thanos.BasePreferenceFragmentCompat;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.pref.AppPreference;

public class DevSettingsFragment extends BasePreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.dev_settings_pref, rootKey);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onBindPreferences() {
        super.onBindPreferences();
        ThanosManager thanos = ThanosManager.from(getContext());
        // Logging.
        SwitchPreferenceCompat loggingPref = findPreference(getString(R.string.key_enable_logging));
        if (thanos.isServiceInstalled()) {
            loggingPref.setChecked(thanos.isLoggingEnabled());
            loggingPref.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean checked = (boolean) newValue;
                thanos.setLoggingEnabled(checked);
                return true;
            });

        } else {
            loggingPref.setEnabled(false);
        }

        SwitchPreferenceCompat showCurrentActivityPref = findPreference(getString(R.string.key_show_current_activity));
        if (thanos.isServiceInstalled()) {
            showCurrentActivityPref.setChecked(thanos.getActivityStackSupervisor().isShowCurrentComponentViewEnabled());
            showCurrentActivityPref.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean checked = (boolean) newValue;
                thanos.getActivityStackSupervisor().setShowCurrentComponentViewEnabled(checked);
                return true;
            });

        } else {
            showCurrentActivityPref.setEnabled(false);
        }

        SwitchPreferenceCompat showNetStatPref = findPreference(getString(R.string.key_show_net_stat));
        if (thanos.isServiceInstalled()) {
            showNetStatPref.setChecked(thanos.getActivityManager().isNetStatTrackerEnabled());
            showNetStatPref.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean checked = (boolean) newValue;
                thanos.getActivityManager().setNetStatTrackerEnabled(checked);
                return true;
            });

        } else {
            showNetStatPref.setEnabled(false);
        }

        SwitchPreferenceCompat globalWhitelistPref = findPreference(getString(R.string.key_enable_global_white_list));
        if (thanos.isServiceInstalled()) {
            globalWhitelistPref.setChecked(thanos.getPkgManager().isProtectedWhitelistEnabled());
            globalWhitelistPref.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean checked = (boolean) newValue;
                thanos.getPkgManager().setProtectedWhitelistEnabled(checked);
                return true;
            });
        }

        SwitchPreferenceCompat processManagerV2Pref = findPreference(getString(R.string.key_process_manage_ui_v2));
        processManagerV2Pref.setChecked(AppPreference.isProcessManagerV2Enabled(requireContext()));
        processManagerV2Pref.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean checked = (boolean) newValue;
            AppPreference.setProcessManagerV2Enabled(requireContext(), checked);
            return true;
        });
    }
}
