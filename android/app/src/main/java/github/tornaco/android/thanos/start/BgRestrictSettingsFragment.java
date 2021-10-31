package github.tornaco.android.thanos.start;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.SwitchPreferenceCompat;

import java.util.Objects;

import github.tornaco.android.thanos.BasePreferenceFragmentCompat;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.core.app.ThanosManager;
import mobi.upod.timedurationpicker.TimeDurationPicker;
import mobi.upod.timedurationpicker.TimeDurationPickerDialog;
import mobi.upod.timedurationpicker.TimeDurationUtil;

public class BgRestrictSettingsFragment extends BasePreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.bg_restrict_pref, rootKey);
    }

    @Override
    protected void onBindPreferences() {
        super.onBindPreferences();
        ThanosManager thanos = ThanosManager.from(getContext());
        if (!thanos.isServiceInstalled()) {
            getPreferenceScreen().setEnabled(false);
            return;
        }
        Preference delayPref = findPreference(getString(R.string.key_bg_screen_off_clean_up_delay));
        Objects.requireNonNull(delayPref).setSummary(TimeDurationUtil.formatHoursMinutesSeconds(
                thanos.getActivityManager().getBgTaskCleanUpDelayTimeMills()));
        delayPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new TimeDurationPickerDialog(getActivity(), new TimeDurationPickerDialog.OnDurationSetListener() {
                    @Override
                    public void onDurationSet(TimeDurationPicker view, long duration) {
                        thanos.getActivityManager().setBgTaskCleanUpDelayTimeMills(duration);
                        Objects.requireNonNull(delayPref).setSummary(TimeDurationUtil.formatHoursMinutesSeconds(duration));
                    }
                }, thanos.getActivityManager().getBgTaskCleanUpDelayTimeMills()).show();
                return true;
            }
        });

        SwitchPreferenceCompat skipAudio = findPreference(getString(R.string.key_bg_screen_off_clean_up_skip_audio));
        boolean enabledA = thanos.getActivityManager().isBgTaskCleanUpSkipAudioFocusedAppEnabled();
        Objects.requireNonNull(skipAudio).setChecked(enabledA);
        skipAudio.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean checked = (boolean) newValue;
            thanos.getActivityManager().setBgTaskCleanUpSkipAudioFocusedAppEnabled(checked);
            return true;
        });

        SwitchPreferenceCompat skipN = findPreference(getString(R.string.key_bg_screen_off_clean_up_skip_notification));
        boolean enabledN = thanos.getActivityManager().isBgTaskCleanUpSkipWhichHasNotificationEnabled();
        Objects.requireNonNull(skipN).setChecked(enabledN);
        skipN.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean checked = (boolean) newValue;
            thanos.getActivityManager().setBgTaskCleanUpSkipWhichHasNotificationEnabled(checked);
            return true;
        });

        SwitchPreferenceCompat skipT = findPreference(getString(R.string.key_bg_screen_off_clean_up_skip_recent_task));
        boolean enabledT = thanos.getActivityManager().isBgTaskCleanUpSkipWhenHasRecentTaskEnabled();
        Objects.requireNonNull(skipT).setChecked(enabledT);
        skipT.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean checked = (boolean) newValue;
            thanos.getActivityManager().setBgTaskCleanUpSkipWhenHasRecentTaskEnabled(checked);
            return true;
        });

        SwitchPreferenceCompat showN = findPreference(getString(R.string.key_bg_restrict_show_notification));
        boolean show = thanos.getActivityManager().isBgRestrictNotificationEnabled();
        Objects.requireNonNull(showN).setChecked(show);
        showN.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean checked = (boolean) newValue;
            thanos.getActivityManager().setBgRestrictNotificationEnabled(checked);
            return true;
        });
    }
}
