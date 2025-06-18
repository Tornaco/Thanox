package github.tornaco.android.thanox.module.notification.recorder;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.SwitchPreferenceCompat;

import java.util.Objects;

import github.tornaco.android.thanos.BasePreferenceFragmentCompat;
import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.R;
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

        SwitchPreferenceCompat nrClip = findPreference(getString(R.string.key_enable_nr_clip));
        if (thanos.isServiceInstalled() && thanos.hasFeature(BuildProp.THANOX_FEATURE_EXT_N_RECORDER_CLIPBOARD)) {
            Objects.requireNonNull(nrClip).setVisible(true);
            Objects.requireNonNull(nrClip).setChecked(thanos.getNotificationManager().isNREnabled(NotificationRecord.Types.TYPE_CLIPBOARD));
            nrClip.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean checked = (boolean) newValue;
                thanos.getNotificationManager().setNREnabled(NotificationRecord.Types.TYPE_CLIPBOARD, checked);
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

        Preference nrAppsPref = findPreference(getString(R.string.key_nr_apps));
        nrAppsPref.setEnabled(!thanos.getNotificationManager().isPersistAllPkgEnabled());
        nrAppsPref.setOnPreferenceClickListener(preference -> {
            AppListActivity.start(requireContext());
            return true;
        });

        SwitchPreferenceCompat persistAllApps = findPreference(getString(R.string.key_persist_all_apps));
        if (thanos.isServiceInstalled()) {
            Objects.requireNonNull(persistAllApps).setChecked(thanos.getNotificationManager().isPersistAllPkgEnabled());
            persistAllApps.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean checked = (boolean) newValue;
                thanos.getNotificationManager().setPersistAllPkgEnabled(checked);
                nrAppsPref.setEnabled(!thanos.getNotificationManager().isPersistAllPkgEnabled());
                return true;
            });
        }
    }
}
