package github.tornaco.android.thanos.common;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import util.Singleton;

public class CommonPreferences {

    private static final String PREF_KEY_FEATURE_DESC_READ_PREFIX = "PREF_FEATURE_DESC_READ_";
    private static final String PREF_KEY_APP_LIST_SHOW_PKG_NAME = "PREF_KEY_APP_LIST_SHOW_PKG_NAME";
    private static final String PREF_KEY_APP_LIST_SHOW_VERSION = "PREF_KEY_APP_LIST_SHOW_VERSION";

    private static final Singleton<CommonPreferences> sPref = new Singleton<CommonPreferences>() {
        @Override
        protected CommonPreferences create() {
            return new CommonPreferences();
        }
    };

    private CommonPreferences() {
    }

    public static CommonPreferences getInstance() {
        return sPref.get();
    }

    public boolean isFeatureDescRead(@NonNull Context context, String feature) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_KEY_FEATURE_DESC_READ_PREFIX + feature, false);
    }

    public void setFeatureDescRead(@NonNull Context context, String feature, boolean read) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_KEY_FEATURE_DESC_READ_PREFIX + feature, read)
                .apply();
    }

    public boolean isAppListShowPkgNameEnabled(@NonNull Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_KEY_APP_LIST_SHOW_PKG_NAME, false);
    }

    public void setAppListShowPkgNameEnabled(@NonNull Context context, boolean enable) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_KEY_APP_LIST_SHOW_PKG_NAME, enable)
                .apply();
    }

    public boolean isAppListShowVersionEnabled(@NonNull Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_KEY_APP_LIST_SHOW_VERSION, false);
    }

    public void setAppListShowVersionEnabled(@NonNull Context context, boolean enable) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_KEY_APP_LIST_SHOW_VERSION, enable)
                .apply();
    }
}
