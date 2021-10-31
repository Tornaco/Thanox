package github.tornaco.android.thanox.module.notification.recorder;

import android.os.Bundle;

import androidx.preference.SwitchPreferenceCompat;

import java.util.Objects;

import github.tornaco.android.thanos.BasePreferenceFragmentCompat;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.n.NotificationRecord;

public class NotificationSettingsFragment extends BasePreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.module_notification_recorder_settings, rootKey);
    }

    @Override
    protected void onBindPreferences() {
        super.onBindPreferences();
        ThanosManager thanos = ThanosManager.from(getContext());
        if (!thanos.isServiceInstalled()) {
            getPreferenceScreen().setEnabled(false);
            return;
        }

        SwitchPreferenceCompat nrt = findPreference(getString(R.string.key_enable_nr_t));
        if (thanos.isServiceInstalled()) {
            Objects.requireNonNull(nrt).setChecked(thanos.getNotificationManager().isNREnabled(NotificationRecord.Types.TYPE_TOAST));
            nrt.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean checked = (boolean) newValue;
                thanos.getNotificationManager().setNREnabled(NotificationRecord.Types.TYPE_TOAST, checked);
                return true;
            });
        }

        SwitchPreferenceCompat nrn = findPreference(getString(R.string.key_enable_nr_n));
        if (thanos.isServiceInstalled()) {
            Objects.requireNonNull(nrn).setChecked(thanos.getNotificationManager().isNREnabled(NotificationRecord.Types.TYPE_GENERAL_NOTIFICATION));
            nrn.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean checked = (boolean) newValue;
                thanos.getNotificationManager().setNREnabled(NotificationRecord.Types.TYPE_GENERAL_NOTIFICATION, checked);
                return true;
            });
        }

        SwitchPreferenceCompat toastInfo = findPreference(getString(R.string.key_enable_toast_info));
        if (thanos.isServiceInstalled()) {
            Objects.requireNonNull(toastInfo).setChecked(thanos.getNotificationManager().isShowToastAppInfoEnabled());
            toastInfo.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean checked = (boolean) newValue;
                thanos.getNotificationManager().setShowToastAppInfoEnabled(checked);
                return true;
            });
        }
    }
}
