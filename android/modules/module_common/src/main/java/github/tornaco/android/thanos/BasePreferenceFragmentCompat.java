package github.tornaco.android.thanos;

import androidx.annotation.StringRes;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Objects;

public abstract class BasePreferenceFragmentCompat extends PreferenceFragmentCompat {

    public <T extends Preference> T findPreference(@StringRes int keyRes) {
        String key = getString(keyRes);
        return Objects.requireNonNull(super.findPreference(key), "Pref is null: " + key);
    }
}
