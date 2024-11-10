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

package now.fortuitous.thanos.power;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.SwitchPreferenceCompat;

import java.util.Objects;

import github.tornaco.android.thanos.BasePreferenceFragmentCompat;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.core.app.ThanosManager;

public class SmartStandbySettingsFragment extends BasePreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.smart_standby_pref, rootKey);
    }

    @Override
    protected void onBindPreferences() {
        super.onBindPreferences();
        ThanosManager thanos = ThanosManager.from(getContext());
        if (!thanos.isServiceInstalled()) {
            getPreferenceScreen().setEnabled(false);
            return;
        }

        bindStopService(thanos);
        bindUnbindService(thanos);
        bindIdle(thanos);
        bindByPassN(thanos);
        bindByPassVisibleWindow(thanos);
        bindBlockBgService(thanos);
    }

    private void bindStopService(ThanosManager thanos) {
        SwitchPreferenceCompat stopServicePref = findPreference(getString(R.string.key_smart_standby_stop_service));
        Objects.requireNonNull(stopServicePref).setChecked(thanos.getActivityManager().isSmartStandByStopServiceEnabled());
        stopServicePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean value = (boolean) newValue;
                thanos.getActivityManager().setSmartStandByStopServiceEnabled(value);
                return true;
            }
        });
    }


    private void bindUnbindService(ThanosManager thanos) {
        SwitchPreferenceCompat ubServicePref = findPreference(getString(R.string.key_smart_standby_unbind_service));
        Objects.requireNonNull(ubServicePref).setChecked(thanos.getActivityManager().isSmartStandByUnbindServiceEnabled());
        ubServicePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean value = (boolean) newValue;
                thanos.getActivityManager().setSmartStandByUnbindServiceEnabled(value);
                return true;
            }
        });
    }

    private void bindIdle(ThanosManager thanos) {
        SwitchPreferenceCompat idlePref = findPreference(getString(R.string.key_smart_standby_set_inactive));
        Objects.requireNonNull(idlePref).setChecked(thanos.getActivityManager().isSmartStandByInactiveEnabled());
        idlePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean value = (boolean) newValue;
                thanos.getActivityManager().setSmartStandByInactiveEnabled(value);
                return true;
            }
        });
    }

    private void bindByPassN(ThanosManager thanos) {
        SwitchPreferenceCompat bypassN = findPreference(getString(R.string.key_smart_standby_bypass_if_has_n));
        Objects.requireNonNull(bypassN).setChecked(thanos.getActivityManager().isSmartStandByByPassIfHasNotificationEnabled());
        bypassN.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean value = (boolean) newValue;
                thanos.getActivityManager().setSmartStandByByPassIfHasNotificationEnabled(value);
                return true;
            }
        });
    }

    private void bindByPassVisibleWindow(ThanosManager thanos) {
        SwitchPreferenceCompat bypassPref = findPreference(getString(R.string.key_smart_standby_bypass_if_has_visible_windows));
        Objects.requireNonNull(bypassPref).setChecked(thanos.getActivityManager().isSmartStandByByPassIfHasVisibleWindows());
        bypassPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean value = (boolean) newValue;
                thanos.getActivityManager().setSmartStandByByPassIfHasVisibleWindowsEnabled(value);
                return true;
            }
        });
    }

    private void bindBlockBgService(ThanosManager thanos) {
        SwitchPreferenceCompat blockBgService = findPreference(getString(R.string.key_smart_standby_block_bg_service_start));
        Objects.requireNonNull(blockBgService).setChecked(thanos.getActivityManager().isSmartStandByBlockBgServiceStartEnabled());
        blockBgService.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean value = (boolean) newValue;
                thanos.getActivityManager().setSmartStandByBlockBgServiceStartEnabled(value);
                return true;
            }
        });
    }
}
