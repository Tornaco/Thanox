package github.tornaco.android.plugin.push.message.delegate;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.SwitchPreferenceCompat;

import github.tornaco.android.thanos.BasePreferenceFragmentCompat;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.push.wechat.PushDelegateManager;

@SuppressWarnings("ConstantConditions")
public class SettingsFragment extends BasePreferenceFragmentCompat {

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.module_push_message_delegate_title_gcm_diag_settings, rootKey);
        bindPreferences();
    }

    private void bindPreferences() {
        if (!ThanosManager.from(requireContext()).isServiceInstalled()) {
            findPreference(getString(R.string.module_push_message_delegate_pref_category_wechat)).setEnabled(false);
            return;
        }
        PushDelegateManager pushDelegateManager = ThanosManager.from(requireContext()).getPushDelegateManager();

        SwitchPreferenceCompat wechatPref = findPreference(getString(R.string.module_push_message_delegate_pref_key_wechat));
        wechatPref.setChecked(pushDelegateManager.wechatEnabled());
        wechatPref.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean checked = (boolean) newValue;
            pushDelegateManager.setWeChatEnabled(checked);
            return true;
        });

        SwitchPreferenceCompat contentPref = findPreference(getString(R.string.module_push_message_delegate_pref_key_wechat_content));
        contentPref.setChecked(pushDelegateManager.wechatContentEnabled());
        contentPref.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean checked = (boolean) newValue;
            pushDelegateManager.setWechatContentEnabled(checked);
            return true;
        });

        SwitchPreferenceCompat startPref = findPreference(getString(R.string.module_push_message_delegate_pref_key_wechat_start_app));
        startPref.setChecked(pushDelegateManager.startWechatOnPushEnabled());
        startPref.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean checked = (boolean) newValue;
            pushDelegateManager.setStartWechatOnPushEnabled(checked);
            return true;
        });

        SwitchPreferenceCompat skipIfRunningPref = findPreference(getString(R.string.module_push_message_delegate_pref_key_wechat_skip_if_running));
        skipIfRunningPref.setChecked(pushDelegateManager.skipIfWeChatAppRunningEnabled());
        skipIfRunningPref.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean checked = (boolean) newValue;
            pushDelegateManager.setSkipIfWeChatAppRunningEnabled(checked);
            return true;
        });

        Preference wechatMock = findPreference(getString(R.string.module_push_message_delegate_pref_key_wechat_mock));
        wechatMock.setOnPreferenceClickListener(preference -> {
            pushDelegateManager.mockWechatMessage();
            return true;
        });
    }
}
