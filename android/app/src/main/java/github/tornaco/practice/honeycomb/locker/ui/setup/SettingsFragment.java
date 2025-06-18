package github.tornaco.practice.honeycomb.locker.ui.setup;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.preference.DropDownPreference;
import androidx.preference.Preference;
import androidx.preference.SwitchPreferenceCompat;

import java.util.Objects;

import github.tornaco.android.thanos.BasePreferenceFragmentCompat;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.app.activity.ActivityStackSupervisor;
import github.tornaco.practice.honeycomb.locker.ui.verify.PatternSettingsActivity;

public class SettingsFragment extends BasePreferenceFragmentCompat {

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
        ThanosManager thanos = ThanosManager.from(getContext());

        SwitchPreferenceCompat reVerifyScreenOff = findPreference(getString(R.string.module_locker_key_re_verify_on_screen_off));
        SwitchPreferenceCompat reVerifyAppSwitch = findPreference(getString(R.string.module_locker_key_re_verify_on_app_switch));
        SwitchPreferenceCompat reVerifyTaskRemoved = findPreference(getString(R.string.module_locker_key_re_verify_on_task_removed));

        ActivityStackSupervisor supervisor = ThanosManager.from(getContext()).getActivityStackSupervisor();

        Objects.requireNonNull(reVerifyScreenOff).setChecked(supervisor.isVerifyOnScreenOffEnabled());
        Objects.requireNonNull(reVerifyAppSwitch).setChecked(supervisor.isVerifyOnAppSwitchEnabled());
        Objects.requireNonNull(reVerifyTaskRemoved).setChecked(supervisor.isVerifyOnTaskRemovedEnabled());

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

        Preference whiteListPref = findPreference(getString(R.string.module_locker_key_white_list_components));
        Objects.requireNonNull(whiteListPref).setOnPreferenceClickListener(preference -> {
            WhiteListComponentViewerActivity.Starter.INSTANCE.start(requireActivity());
            return true;
        });

        Preference patternSettingsPref = findPreference(getString(R.string.module_locker_key_verify_method_custom_pattern));
        if (TextUtils.isEmpty(thanos.getActivityStackSupervisor().getLockPattern())) {
            patternSettingsPref.setSummary(github.tornaco.android.thanos.res.R.string.common_text_value_not_set);
        } else {
            patternSettingsPref.setSummary("******");
        }
        patternSettingsPref.setOnPreferenceClickListener(preference -> {
            PatternSettingsActivity.start(requireContext());
            return true;
        });
        SwitchPreferenceCompat hidePatternLinesPref = findPreference(getString(R.string.module_locker_key_verify_method_custom_pattern_hide_line));
        hidePatternLinesPref.setChecked(thanos.getActivityStackSupervisor().isLockPatternLineHidden());
        hidePatternLinesPref.setOnPreferenceChangeListener((preference, newValue) -> {
            thanos.getActivityStackSupervisor().setLockPatternLineHidden((boolean) newValue);
            return true;
        });

        DropDownPreference methodPref = findPreference(getString(R.string.module_locker_key_verify_method));
        int currentMethod = thanos.getActivityStackSupervisor().getLockMethod();
        methodPref.setValue(String.valueOf(currentMethod));
        methodPref.setOnPreferenceChangeListener((preference, newValue) -> {
            int method = Integer.parseInt(String.valueOf(newValue));
            thanos.getActivityStackSupervisor().setLockMethod(method);
            return true;
        });

    }
}
