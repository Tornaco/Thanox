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
