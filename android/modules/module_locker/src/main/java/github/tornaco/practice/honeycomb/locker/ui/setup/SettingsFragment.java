package github.tornaco.practice.honeycomb.locker.ui.setup;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.SwitchPreferenceCompat;

import java.util.Objects;

import github.tornaco.android.thanos.BaseWithFabPreferenceFragmentCompat;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.app.activity.ActivityStackSupervisor;
import github.tornaco.practice.honeycomb.locker.R;
import github.tornaco.practice.honeycomb.locker.util.fingerprint.FingerprintManagerCompat;
import util.Consumer;

public class SettingsFragment extends BaseWithFabPreferenceFragmentCompat {

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.module_locker_settings_fragment, rootKey);
    }

    @Override
    public void onResume() {
        super.onResume();
        bindPreferences();
    }

    private void bindPreferences() {

        SwitchPreferenceCompat reVerifyScreenOff = (SwitchPreferenceCompat) findPreference(getString(R.string.module_locker_key_re_verify_on_screen_off));
        SwitchPreferenceCompat reVerifyAppSwitch = (SwitchPreferenceCompat) findPreference(getString(R.string.module_locker_key_re_verify_on_app_switch));
        SwitchPreferenceCompat reVerifyTaskRemoved = (SwitchPreferenceCompat) findPreference(getString(R.string.module_locker_key_re_verify_on_task_removed));
        SwitchPreferenceCompat enableWorkaround = (SwitchPreferenceCompat) findPreference(getString(R.string.module_locker_key_verify_workaround_enabled));
        SwitchPreferenceCompat enableFP = (SwitchPreferenceCompat) findPreference(getString(R.string.module_locker_key_fp));

        Preference lockMethod = findPreference(getString(R.string.module_locker_key_verify_lock_method));

        ActivityStackSupervisor supervisor = ThanosManager.from(getContext()).getActivityStackSupervisor();

        Objects.requireNonNull(reVerifyScreenOff).setChecked(supervisor.isVerifyOnScreenOffEnabled());
        Objects.requireNonNull(reVerifyAppSwitch).setChecked(supervisor.isVerifyOnAppSwitchEnabled());
        Objects.requireNonNull(reVerifyTaskRemoved).setChecked(supervisor.isVerifyOnTaskRemovedEnabled());
        Objects.requireNonNull(enableWorkaround).setChecked(supervisor.isAppLockWorkaroundEnabled());

        boolean isFingerPrintSupported = FingerprintManagerCompat.from(getActivity())
                .isHardwareDetected();
        Objects.requireNonNull(enableFP).setEnabled(isFingerPrintSupported);
        enableFP.setChecked(isFingerPrintSupported && supervisor.isFingerPrintEnabled());

        reVerifyScreenOff.setOnPreferenceClickListener(preference -> {
            supervisor.setVerifyOnScreenOffEnabled(reVerifyScreenOff.isChecked());
            return true;
        });
        reVerifyAppSwitch.setOnPreferenceClickListener(preference -> {
            supervisor.setVerifyOnAppSwitchEnabled(reVerifyAppSwitch.isChecked());
            return true;
        });
        reVerifyTaskRemoved.setOnPreferenceClickListener(preference -> {
            supervisor.setVerifyOnTaskRemovedEnabled(reVerifyTaskRemoved.isChecked());
            return true;
        });
        enableWorkaround.setOnPreferenceClickListener(preference -> {
            supervisor.setAppLockWorkaroundEnabled(enableWorkaround.isChecked());
            return true;
        });
        if (isFingerPrintSupported) {
            enableFP.setOnPreferenceClickListener(preference -> {
                supervisor.setFingerPrintEnabled(enableFP.isChecked());
                return true;
            });
        }

        Objects.requireNonNull(lockMethod).setSummary(getCurrentLockMethodString());
        Objects.requireNonNull(lockMethod).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                LockerMethodSelectionUi.showLockerMethodSelections(getActivity(), new Consumer<Integer>() {
                    @Override
                    public void accept(Integer method) {
                        SetupActivity.start(getContext(), method);
                    }
                });
                return true;
            }
        });
    }

    private String getCurrentLockMethodString() {
        ActivityStackSupervisor supervisor = ThanosManager.from(getContext()).getActivityStackSupervisor();
        int method = supervisor.getLockerMethod();
        if (method == ActivityStackSupervisor.LockerMethod.PIN) {
            return getString(R.string.module_locker_key_verify_lock_method_pin);
        }
        if (method == ActivityStackSupervisor.LockerMethod.PATTERN) {
            return getString(R.string.module_locker_key_verify_lock_method_pattern);
        }
        return "Noop";
    }
}
