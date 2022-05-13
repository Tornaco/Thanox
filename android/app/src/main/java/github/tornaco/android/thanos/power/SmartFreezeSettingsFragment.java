package github.tornaco.android.thanos.power;

import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.DropDownPreference;
import androidx.preference.Preference;
import androidx.preference.SwitchPreferenceCompat;

import com.google.common.collect.Lists;

import java.util.Objects;

import github.tornaco.android.thanos.BasePreferenceFragmentCompat;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.ThanosApp;
import github.tornaco.android.thanos.app.donate.DonateIntroDialogKt;
import github.tornaco.android.thanos.app.donate.DonateSettings;
import github.tornaco.android.thanos.core.app.ThanosManager;
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
            thanos.getPkgManager().setSmartFreezeScreenOffCheckDelay(newMin * 1000 * 60);
            return true;
        });

        SwitchPreferenceCompat hidePackagePref = findPreference(getString(R.string.key_smart_freeze_hide_package_change_event));
        Objects.requireNonNull(hidePackagePref).setChecked(thanos.getPkgManager().isSmartFreezeHidePackageEventEnabled());
        hidePackagePref.setEnabled(!thanos.getPkgManager().isFreezePkgWithSuspendEnabled());
        hidePackagePref.setOnPreferenceChangeListener((preference, newValue) -> {
            if (ThanosApp.isPrc() && !DonateSettings.isActivated(getActivity())) {
                DonateIntroDialogKt.showDonateIntroDialog(requireActivity());
                return false;
            }
            boolean checked = (boolean) newValue;
            thanos.getPkgManager().setSmartFreezeHidePackageEventEnabled(checked);
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
