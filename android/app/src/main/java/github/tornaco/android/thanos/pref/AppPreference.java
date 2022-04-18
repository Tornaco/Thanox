package github.tornaco.android.thanos.pref;

import android.content.Context;

import androidx.preference.PreferenceManager;

import github.tornaco.android.rhino.plugin.Verify;
import github.tornaco.android.thanos.BuildProp;

public class AppPreference {

    private static final String PREF_KEY_FIRST_RUN = "PREF_KEY_FIRST_RUN_" + BuildProp.THANOS_BUILD_FINGERPRINT;
    private static final String PREF_KEY_ON_BOARDING = "PREF_KEY_ON_BOARDING_" + BuildProp.THANOS_BUILD_FINGERPRINT;
    private static final String PREF_KEY_PROCESS_MANAGE_UI_V2 = "PREF_KEY_PROCESS_MANAGE_UI_V2";
    private static final String PREF_KEY_CURRENT_TIP_INDEX = "PREF_KEY_CURRENT_TIP_INDEX";

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


    @Verify
    public static boolean hasOnBoarding(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_KEY_ON_BOARDING, false);
    }

    @Verify
    public static void setHasOnBoarding(Context context, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_KEY_ON_BOARDING, value)
                .apply();
    }

    @Verify
    public static boolean isProcessManagerV2Enabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_KEY_PROCESS_MANAGE_UI_V2, false);
    }

    @Verify
    public static void setProcessManagerV2Enabled(Context context, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_KEY_PROCESS_MANAGE_UI_V2, value)
                .apply();
    }

    public static int getCurrentTipIndex(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(PREF_KEY_CURRENT_TIP_INDEX, 0);
    }

    public static void setCurrentTipIndex(Context context, int index) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(PREF_KEY_CURRENT_TIP_INDEX, index)
                .apply();
    }
}
