package github.tornaco.android.plugin.push.message.delegate;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.SwitchPreferenceCompat;

import github.tornaco.android.thanos.BaseWithFabPreferenceFragmentCompat;

@SuppressWarnings("ConstantConditions")
public class SettingsFragment extends BaseWithFabPreferenceFragmentCompat {

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
        bindPreferences();
    }

    private void bindPreferences() {
        PushDelegateManager pushDelegateManager = new PushDelegateManager(getContext());
        if (!pushDelegateManager.isServiceInstalled()) {
            findPreference(getString(R.string.pref_category_wechat)).setEnabled(false);
            return;
        }

        SwitchPreferenceCompat wechatPref = findPreference(getString(R.string.pref_key_wechat));
        wechatPref.setChecked(pushDelegateManager.wechatEnabled());
        wechatPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean checked = (boolean) newValue;
                pushDelegateManager.setWeChatEnabled(checked);
                return true;
            }
        });

        SwitchPreferenceCompat contentPref = findPreference(getString(R.string.pref_key_wechat_content));
        contentPref.setChecked(pushDelegateManager.wechatContentEnabled());
        contentPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean checked = (boolean) newValue;
                pushDelegateManager.setWechatContentEnabled(checked);
                return true;
            }
        });

        SwitchPreferenceCompat startPref = findPreference(getString(R.string.pref_key_wechat_start_app));
        startPref.setChecked(pushDelegateManager.startWechatOnPushEnabled());
        startPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean checked = (boolean) newValue;
                pushDelegateManager.setStartWechatOnPushEnabled(checked);
                return true;
            }
        });

        SwitchPreferenceCompat skipIfRunningPref = findPreference(getString(R.string.pref_key_wechat_skip_if_running));
        skipIfRunningPref.setChecked(pushDelegateManager.skipIfWeChatAppRunningEnabled());
        skipIfRunningPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean checked = (boolean) newValue;
                pushDelegateManager.setSkipIfWeChatAppRunningEnabled(checked);
                return true;
            }
        });

        Preference wechatMock = findPreference(getString(R.string.pref_key_wechat_mock));
        wechatMock.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                pushDelegateManager.mockWechatMessage();
                return true;
            }
        });
    }
}
