package now.fortuitous.thanos.task;

import android.os.Bundle;

import androidx.preference.SwitchPreferenceCompat;

import java.util.Objects;

import github.tornaco.android.thanos.BasePreferenceFragmentCompat;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.core.app.ThanosManager;

public class CleanTaskRemovalSettingsFragment extends BasePreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.clean_task_removal_pref, rootKey);
    }

    @Override
    protected void onBindPreferences() {
        super.onBindPreferences();
        ThanosManager thanos = ThanosManager.from(getContext());
        if (!thanos.isServiceInstalled()) {
            getPreferenceScreen().setEnabled(false);
            return;
        }

        SwitchPreferenceCompat skipNotification = findPreference(getString(R.string.key_clean_task_removal_skip_notification));
        boolean enabledN = thanos.getActivityManager().isCleanUpOnTaskRemovalSkipNotificationEnabled();
        Objects.requireNonNull(skipNotification).setChecked(enabledN);
        skipNotification.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean checked = (boolean) newValue;
            thanos.getActivityManager().setCleanUpOnTaskRemovalSkipNotificationEnabled(checked);
            return true;
        });

        SwitchPreferenceCompat skipAudio = findPreference(getString(R.string.key_clean_task_removal_skip_audio));
        boolean enabledA = thanos.getActivityManager().isCleanUpOnTaskRemovalSkipAudioFocusedEnabled();
        Objects.requireNonNull(skipAudio).setChecked(enabledA);
        skipAudio.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean checked = (boolean) newValue;
            thanos.getActivityManager().setCleanUpOnTaskRemovalSkipAudioFocusedEnabled(checked);
            return true;
        });

        SwitchPreferenceCompat skipVpn = findPreference(getString(R.string.key_clean_task_removal_skip_vpn));
        boolean enabledV = thanos.getActivityManager().isCleanUpOnTaskRemovalSkipVpnEnabled();
        Objects.requireNonNull(skipVpn).setChecked(enabledV);
        skipVpn.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean checked = (boolean) newValue;
            thanos.getActivityManager().setCleanUpOnTaskRemovalSkipVpnEnabled(checked);
            return true;
        });
    }
}
