/*
 * (C) Copyright 2022 Thanox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package now.fortuitous.thanos.settings;

import android.os.Bundle;

import androidx.preference.SwitchPreferenceCompat;

import github.tornaco.android.thanos.BasePreferenceFragmentCompat;
import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.thanos.android.noroot.ServiceBindings;
import now.fortuitous.thanos.settings.access.SettingsAccessRecordViewerActivity;

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

        findPreference(getString(R.string.key_rootless_support)).setVisible(BuildProp.THANOS_BUILD_DEBUG);
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
