package github.tornaco.android.thanos.common;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import java.util.Observable;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import util.Singleton;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonPreferences extends Observable {

    private static final String PREF_KEY_FEATURE_DESC_READ_PREFIX = "PREF_FEATURE_DESC_READ_";

    private static Singleton<CommonPreferences> sPref = new Singleton<CommonPreferences>() {
        @Override
        protected CommonPreferences create() {
            return new CommonPreferences();
        }
    };

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
}
