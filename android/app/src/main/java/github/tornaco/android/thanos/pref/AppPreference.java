package github.tornaco.android.thanos.pref;

import android.content.Context;

import androidx.preference.PreferenceManager;

import github.tornaco.android.rhino.plugin.Verify;
import github.tornaco.android.thanos.BuildProp;

public class AppPreference {

    private static final String PREF_KEY_FIRST_RUN = "PREF_KEY_FIRST_RUN_" + BuildProp.THANOS_BUILD_FINGERPRINT;

    @Verify
    public static boolean isFirstRun(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_KEY_FIRST_RUN, true);
    }

    @Verify
    public static void setFirstRun(Context context, boolean first) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_KEY_FIRST_RUN, first)
                .apply();
    }

    @Verify
    public static boolean isFeatureNoticeAccepted(Context context, String feature) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(feature, false);
    }

    @Verify
    public static void setFeatureNoticeAccepted(Context context, String feature, boolean first) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(feature, first)
                .apply();
    }
}
