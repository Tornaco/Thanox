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

import androidx.preference.DropDownPreference;
import androidx.preference.SwitchPreferenceCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import github.tornaco.android.thanos.BasePreferenceFragmentCompat;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.common.CommonPreferences;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.support.AppFeatureManager;
import github.tornaco.android.thanos.theme.AppThemePreferences;
import github.tornaco.android.thanos.theme.Theme;
import github.tornaco.android.thanos.util.GlideApp;
import github.tornaco.android.thanos.util.iconpack.IconPack;
import github.tornaco.android.thanos.util.iconpack.IconPackManager;
import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

public class UiSettingsFragment extends BasePreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.ui_settings_pref, rootKey);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onBindPreferences() {
        super.onBindPreferences();
        ThanosManager thanos = ThanosManager.from(getContext());

        // Theme.
        Theme theme = AppThemePreferences.getInstance().getTheme(Objects.requireNonNull(this.getContext()));
        DropDownPreference themePref = findPreference(getString(R.string.key_app_theme));
        Objects.requireNonNull(themePref).setValue(theme.name());
        themePref.setOnPreferenceChangeListener((preference, newValue) -> {
            Theme selectedTheme = Theme.fromStringOrElse(String.valueOf(newValue), Theme.Light);
            AppThemePreferences.getInstance().setTheme(Objects.requireNonNull(getContext()), selectedTheme);
            AppThemePreferences.getInstance().setPreferLDTheme(Objects.requireNonNull(getContext()), selectedTheme);
            return true;
        });

        DropDownPreference iconPref = findPreference(getString(R.string.key_app_icon_pack));
        IconPackManager iconPackManager = IconPackManager.getInstance();
        final List<IconPack> packs = iconPackManager.getAvailableIconPacks(getContext());
        final List<String> entries = new ArrayList<>();
        final List<String> values = new ArrayList<>();
        for (IconPack pack : packs) {
            entries.add(String.valueOf(pack.label));
            values.add(String.valueOf(pack.packageName));
        }
        // Default
        entries.add("Noop");
        values.add("Noop");

        iconPref.setEntries(entries.toArray(new String[0]));
        iconPref.setEntryValues(values.toArray(new String[0]));
        String current = AppThemePreferences.getInstance().getIconPack(getContext(), null);

        iconPref.setSummary("Noop");
        iconPref.setValue("Noop");
        if (current != null) {
            IconPack pack = IconPackManager.getInstance().getIconPackage(getContext(), current);
            if (pack != null) {
                iconPref.setSummary(pack.label);
            }
        }
        iconPref.setOnPreferenceChangeListener((preference, newValue) -> {
            AppThemePreferences.getInstance().setIconPack(getContext(), String.valueOf(newValue));
            IconPack pack = IconPackManager.getInstance().getIconPackage(getContext(), String.valueOf(newValue));
            if (pack != null && pack.isInstalled()) {
                preference.setSummary(pack.label);
                invalidateIconPack(pack);
            } else {
                preference.setSummary("Noop");
            }
            return true;
        });

        SwitchPreferenceCompat usedCircleIconPref = findPreference(getString(R.string.key_use_round_icon));
        AppFeatureManager.INSTANCE.withSubscriptionStatus(requireContext(), isSubscribed -> {
            usedCircleIconPref.setVisible(isSubscribed);
            return null;
        });

        usedCircleIconPref.setChecked(AppThemePreferences.getInstance().useRoundIcon(getContext()));
        usedCircleIconPref.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean use = (boolean) newValue;
            AppThemePreferences.getInstance().setUseRoundIcon(getContext(), use);
            return true;
        });

        SwitchPreferenceCompat showPkg = findPreference(R.string.key_app_list_item_show_pkg);
        showPkg.setChecked(CommonPreferences.getInstance().isAppListShowPkgNameEnabled(requireContext()));
        showPkg.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean newBoolValue = (boolean) newValue;
            CommonPreferences.getInstance().setAppListShowPkgNameEnabled(requireContext(), newBoolValue);
            return true;
        });

        SwitchPreferenceCompat showVersion = findPreference(R.string.key_app_list_item_show_version);
        showVersion.setChecked(CommonPreferences.getInstance().isAppListShowVersionEnabled(requireContext()));
        showVersion.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean newBoolValue = (boolean) newValue;
            CommonPreferences.getInstance().setAppListShowVersionEnabled(requireContext(), newBoolValue);
            return true;
        });
    }

    private void invalidateIconPack(IconPack pack) {
        GlideApp.get(requireContext()).clearMemory();
        Completable.fromRunnable(() -> {
            GlideApp.get(requireContext()).clearDiskCache();
            pack.getAllDrawables();
        }).subscribeOn(Schedulers.io()).subscribe();
    }
}
