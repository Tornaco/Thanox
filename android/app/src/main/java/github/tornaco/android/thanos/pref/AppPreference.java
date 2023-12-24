package github.tornaco.android.thanos.pref;

import android.content.Context;

import androidx.preference.PreferenceManager;

import java.util.List;

import github.tornaco.android.rhino.plugin.Verify;
import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.core.pm.PackageSet;

public class AppPreference {

    private static final String PREF_KEY_FIRST_RUN = "PREF_KEY_FIRST_RUN_" + BuildProp.THANOS_BUILD_FINGERPRINT;
    private static final String PREF_KEY_ON_BOARDING = "PREF_KEY_ON_BOARDING_" + BuildProp.THANOS_BUILD_FINGERPRINT;
    private static final String PREF_KEY_PROCESS_MANAGE_UI_V2 = "PREF_KEY_PROCESS_MANAGE_UI_V2";
    private static final String PREF_KEY_CURRENT_TIP_INDEX = "PREF_KEY_CURRENT_TIP_INDEX";
    private static final String PREF_KEY_PKG_SET_SORT_PREFIX = "PREF_KEY_PKG_SORT_";
    private static final String PREF_KEY_FEATURE_FLAG_ = "PREF_KEY_FEATURE_FLAG_";

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
    @Deprecated
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

    public static int getPkgSetSort(Context context, PackageSet packageSet) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(PREF_KEY_PKG_SET_SORT_PREFIX + packageSet.getId(), Integer.MAX_VALUE);
    }

    public static void setPkgSetSort(Context context, List<PackageSet> packageSets) {
        for (int i = 0; i < packageSets.size(); i++) {
            PackageSet p = packageSets.get(i);
            PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .putInt(PREF_KEY_PKG_SET_SORT_PREFIX + p.getId(), i * 100)
                    .apply();
        }
    }

    @Verify
    public static boolean isAppFeatureEnabled(Context context, int featureId) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_KEY_FEATURE_FLAG_ + featureId, true);
    }

    @Verify
    public static void setAppFeatureEnabled(Context context, int featureId, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_KEY_FEATURE_FLAG_ + featureId, value)
                .apply();
    }

}
