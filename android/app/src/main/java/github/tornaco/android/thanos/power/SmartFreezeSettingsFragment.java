package github.tornaco.android.thanos.power;

import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.DropDownPreference;
import androidx.preference.Preference;
import androidx.preference.SwitchPreferenceCompat;

import java.util.Objects;

import github.tornaco.android.thanos.BaseWithFabPreferenceFragmentCompat;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.ThanosApp;
import github.tornaco.android.thanos.app.donate.DonateSettings;
import github.tornaco.android.thanos.core.app.ThanosManager;

public class SmartFreezeSettingsFragment extends BaseWithFabPreferenceFragmentCompat {

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
        hidePackagePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (ThanosApp.isPrc() && !DonateSettings.isActivated(getActivity())) {
                    Toast.makeText(getActivity(), R.string.module_donate_donated_available, Toast.LENGTH_SHORT).show();
                    return false;
                }
                boolean checked = (boolean) newValue;
                thanos.getPkgManager().setSmartFreezeHidePackageEventEnabled(checked);
                return true;
            }
        });


        SwitchPreferenceCompat suspendPref = findPreference(getString(R.string.key_smart_freeze_with_suspend));
        Objects.requireNonNull(suspendPref).setChecked(thanos.getPkgManager().isFreezePkgWithSuspendEnabled());
        suspendPref.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean checked = (boolean) newValue;
            thanos.getPkgManager().setFreezePkgWithSuspendEnabled(checked);
            return true;
        });
    }
}
