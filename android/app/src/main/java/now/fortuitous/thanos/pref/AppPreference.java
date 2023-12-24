/*
 * (C) Copyright 2022 Thanox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package now.fortuitous.thanos.pref;

import android.content.Context;

import androidx.preference.PreferenceManager;

import java.util.List;


import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.core.pm.PackageSet;

public class AppPreference {

    private static final String PREF_KEY_FIRST_RUN = "PREF_KEY_FIRST_RUN_" + BuildProp.THANOS_BUILD_FINGERPRINT;
    private static final String PREF_KEY_ON_BOARDING = "PREF_KEY_ON_BOARDING_" + BuildProp.THANOS_BUILD_FINGERPRINT;
    private static final String PREF_KEY_PROCESS_MANAGE_UI_V2 = "PREF_KEY_PROCESS_MANAGE_UI_V2";
    private static final String PREF_KEY_CURRENT_TIP_INDEX = "PREF_KEY_CURRENT_TIP_INDEX";
    private static final String PREF_KEY_PKG_SET_SORT_PREFIX = "PREF_KEY_PKG_SORT_";
    private static final String PREF_KEY_FEATURE_FLAG_ = "PREF_KEY_FEATURE_FLAG_";


    public static boolean isFirstRun(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_KEY_FIRST_RUN, true);
    }


    public static void setFirstRun(Context context, boolean first) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_KEY_FIRST_RUN, first)
                .apply();
    }


    public static boolean isFeatureNoticeAccepted(Context context, String feature) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(feature, false);
    }


    public static void setFeatureNoticeAccepted(Context context, String feature, boolean first) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(feature, first)
                .apply();
    }



    public static boolean hasOnBoarding(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_KEY_ON_BOARDING, false);
    }


    public static void setHasOnBoarding(Context context, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_KEY_ON_BOARDING, value)
                .apply();
    }


    @Deprecated
    public static boolean isProcessManagerV2Enabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_KEY_PROCESS_MANAGE_UI_V2, false);
    }


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


    public static boolean isAppFeatureEnabled(Context context, int featureId) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_KEY_FEATURE_FLAG_ + featureId, true);
    }


    public static void setAppFeatureEnabled(Context context, int featureId, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_KEY_FEATURE_FLAG_ + featureId, value)
                .apply();
    }

}
