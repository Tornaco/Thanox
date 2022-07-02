package github.tornaco.android.thanos.settings;

import android.os.Bundle;

import androidx.preference.SwitchPreferenceCompat;

import github.tornaco.android.thanos.BasePreferenceFragmentCompat;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.settings.access.SettingsAccessRecordViewerActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.thanos.android.noroot.ServiceBindings;

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

        findPreference(getString(R.string.key_rootless_support)).setOnPreferenceClickListener(preference -> {
            if (ServiceBindings.INSTANCE.checkPermission(1)) {
                ServiceBindings.INSTANCE.bindUserService();
            }
            return false;
        });

        findPreference(getString(R.string.key_theme_attr_preview)).setOnPreferenceClickListener(preference -> {
            ActivityUtils.startActivity(requireActivity(), ThemeAttrPreviewActivity.class);
            return true;
        });

        findPreference(getString(R.string.key_settings_record_viewer)).setOnPreferenceClickListener(preference -> {
            SettingsAccessRecordViewerActivity.Starter.INSTANCE.start(requireActivity());
            return true;
        });
    }
}
