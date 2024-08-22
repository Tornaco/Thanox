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

import androidx.preference.DropDownPreference;
import androidx.preference.Preference;
import androidx.preference.SwitchPreferenceCompat;

import com.elvishew.xlog.XLog;
import com.google.common.collect.Lists;

import java.util.Objects;

import github.tornaco.android.thanos.BasePreferenceFragmentCompat;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.feature.access.AppFeatureManager;
import github.tornaco.android.thanos.widget.ModernSingleChoiceDialog;
import util.Consumer;

public class SmartFreezeSettingsFragment extends BasePreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.smart_freeze_pref, rootKey);
    }

    @Override
    protected void onBindPreferences() {
        super.onBindPreferences();
        ThanosManager thanos = ThanosManager.from(getContext());
        if (!thanos.isServiceInstalled()) {
            getPreferenceScreen().setEnabled(false);
            return;
        }

        Preference createShortcutPref = findPreference(R.string.key_smart_freeze_create_freeze_all_shortcut);
        XLog.d("createShortcutPref: " + createShortcutPref);
        Objects.requireNonNull(createShortcutPref).setOnPreferenceClickListener(preference -> {
            XLog.d("addShortcutForFreezeAll");
            ShortcutHelper.addShortcutForFreezeAll(requireContext());
            return true;
        });

        SwitchPreferenceCompat screenOffFreeze = findPreference(getString(R.string.key_smart_freeze_screen_off_clean_up));
        boolean screenOffFreezeEnabled = thanos.getPkgManager().isSmartFreezeScreenOffCheckEnabled();
        Objects.requireNonNull(screenOffFreeze).setChecked(screenOffFreezeEnabled);
        screenOffFreeze.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean checked = (boolean) newValue;
            thanos.getPkgManager().setSmartFreezeScreenOffCheckEnabled(checked);
            return true;
        });

        DropDownPreference delayPref = findPreference(getString(R.string.key_smart_freeze_screen_off_clean_up_delay));
        long delay = thanos.getPkgManager().getSmartFreezeScreenOffCheckDelay();
        int min = (int) (delay / (60 * 1000));
        Objects.requireNonNull(delayPref).setValue(String.valueOf(min));
        delayPref.setOnPreferenceChangeListener((preference, newValue) -> {
            int newMin = Integer.parseInt(String.valueOf(newValue));
            thanos.getPkgManager().setSmartFreezeScreenOffCheckDelay((long) newMin * 1000 * 60);
            return true;
        });

        SwitchPreferenceCompat hidePackagePref = findPreference(getString(R.string.key_smart_freeze_hide_package_change_event));
        Objects.requireNonNull(hidePackagePref).setChecked(thanos.getPkgManager().isSmartFreezeHidePackageEventEnabled());
        hidePackagePref.setEnabled(!thanos.getPkgManager().isFreezePkgWithSuspendEnabled());
        hidePackagePref.setOnPreferenceChangeListener((preference, newValue) -> {
            AppFeatureManager.INSTANCE.withSubscriptionStatus(requireContext(), isSubscribed -> {
                if (isSubscribed) {
                    boolean checked = (boolean) newValue;
                    thanos.getPkgManager().setSmartFreezeHidePackageEventEnabled(checked);
                } else {
                    AppFeatureManager.INSTANCE.showDonateIntroDialog(requireActivity());
                }
                return null;
            });
            return true;
        });


        Preference freezeMethodPref = findPreference(getString(R.string.key_smart_freeze_method));
        Runnable updatePrefSummary = () -> Objects.requireNonNull(freezeMethodPref).setSummary(
                thanos.getPkgManager().isFreezePkgWithSuspendEnabled()
                        ? R.string.pre_title_smart_freeze_freeze_method_suspend
                        : R.string.pre_title_smart_freeze_freeze_method_disable);
        updatePrefSummary.run();
        Objects.requireNonNull(freezeMethodPref).setOnPreferenceClickListener(preference -> {
            showSuspendOrDisablePackageDialog(res -> {
                updatePrefSummary.run();
                hidePackagePref.setEnabled(!thanos.getPkgManager().isFreezePkgWithSuspendEnabled());
            });
            return true;
        });

        SwitchPreferenceCompat enableOnLaunchByDefault = findPreference(getString(R.string.key_smart_freeze_enable_on_launch_by_default));
        Objects.requireNonNull(enableOnLaunchByDefault).setChecked(thanos.getPkgManager().isEnablePkgOnLaunchByDefault());
        enableOnLaunchByDefault.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean checked = (boolean) newValue;
            thanos.getPkgManager().setEnablePkgOnLaunchByDefaultEnabled(checked);
            return true;
        });

        SwitchPreferenceCompat dolTipsPref = findPreference(getString(R.string.key_smart_freeze_dol_tips));
        Objects.requireNonNull(dolTipsPref).setChecked(thanos.getPkgManager().isDOLTipsEnabled());
        dolTipsPref.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean checked = (boolean) newValue;
            thanos.getPkgManager().setDOLTipsEnabled(checked);
            return true;
        });

        SwitchPreferenceCompat showTipPref = findPreference(getString(R.string.key_smart_freeze_tips));
        Objects.requireNonNull(showTipPref).setChecked(thanos.getPkgManager().isFreezeTipEnabled());
        showTipPref.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean checked = (boolean) newValue;
            thanos.getPkgManager().setFreezeTipEnabled(checked);
            return true;
        });
    }

    private void showSuspendOrDisablePackageDialog(Consumer<Boolean> suspendConsumer) {
        ThanosManager thanos = ThanosManager.from(getContext());
        ModernSingleChoiceDialog dialog = new ModernSingleChoiceDialog(requireActivity());
        dialog.setTitle(getString(R.string.pre_title_smart_freeze_freeze_method));
        dialog.setItems(Lists.newArrayList(
                new ModernSingleChoiceDialog.Item("disable",
                        getString(R.string.pre_title_smart_freeze_freeze_method_disable),
                        getString(R.string.pre_title_smart_freeze_freeze_method_disable_summary)),

                new ModernSingleChoiceDialog.Item("suspend",
                        getString(R.string.pre_title_smart_freeze_freeze_method_suspend),
                        getString(R.string.pre_title_smart_freeze_freeze_method_suspend_summary))));

        dialog.setCheckedId(thanos.getPkgManager().isFreezePkgWithSuspendEnabled() ? "suspend" : "disable");
        dialog.setOnConfirm(id -> {
            boolean susp = id.equals("suspend");
            thanos.getPkgManager().setFreezePkgWithSuspendEnabled(susp);
            suspendConsumer.accept(susp);
        });
        dialog.show();
    }
}
