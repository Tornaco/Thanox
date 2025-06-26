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

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.n.NotificationRecord;
import github.tornaco.android.thanos.core.pm.PackageSet;
import now.fortuitous.thanos.main.PrebuiltFeatureIds;

public class AppPreference {

    private static final String PREF_KEY_FIRST_RUN = "PREF_KEY_FIRST_RUN";
    private static final String PREF_KEY_ON_BOARDING = "PREF_KEY_ON_BOARDING";
    private static final String PREF_KEY_PROCESS_MANAGE_UI_V2 = "PREF_KEY_PROCESS_MANAGE_UI_V2";
    private static final String PREF_KEY_CURRENT_TIP_INDEX = "PREF_KEY_CURRENT_TIP_INDEX";
    private static final String PREF_KEY_PKG_SET_SORT_PREFIX = "PREF_KEY_PKG_SORT_";
    private static final String PREF_KEY_FEATURE_FLAG_ = "PREF_KEY_FEATURE_FLAG_";
    private static final String PREF_KEY_APP_SX = "PREF_KEY_APP_SX";

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

    public static String getAppType(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_KEY_APP_SX, null);
    }

    public static void setAppType(Context context, String first) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_KEY_APP_SX, first)
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

    public static void setPkgSetSort(Context context, List<String> packageSetIds) {
        for (int i = 0; i < packageSetIds.size(); i++) {
            String p = packageSetIds.get(i);
            PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .putInt(PREF_KEY_PKG_SET_SORT_PREFIX + p, i * 100)
                    .apply();
        }
    }

    public static void setPkgSetSort(Context context, String id, int sort) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(PREF_KEY_PKG_SET_SORT_PREFIX + id, sort * 100)
                .apply();
    }

    public static boolean isAppFeatureEnabled(Context context, int featureId) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_KEY_FEATURE_FLAG_ + featureId, true);
    }


    public static void setAppFeatureEnabled(Context context, int featureId, boolean value) {
        ThanosManager thanos = ThanosManager.from(context);

        if (featureId == PrebuiltFeatureIds.ID_LAUNCH_OTHER_APP_BLOCKER) {
            thanos.getActivityStackSupervisor().setLaunchOtherAppBlockerEnabled(value);
        }

        if (!value) {
            if (featureId == PrebuiltFeatureIds.ID_APP_LOCK) {
                thanos.getActivityStackSupervisor().setAppLockEnabled(false);
            } else if (featureId == PrebuiltFeatureIds.ID_BACKGROUND_RESTRICT) {
                thanos.getActivityManager().setBgRestrictEnabled(false);
            } else if (featureId == PrebuiltFeatureIds.ID_OPS_BY_APP || featureId == PrebuiltFeatureIds.ID_OPS_BY_OPS || featureId == PrebuiltFeatureIds.ID_THANOX_OPS) {
                thanos.getAppOpsManager().setOpsEnabled(false);
            } else if (featureId == PrebuiltFeatureIds.ID_BACKGROUND_START) {
                thanos.getActivityManager().setStartBlockEnabled(false);
            } else if (featureId == PrebuiltFeatureIds.ID_LAUNCH_OTHER_APP_BLOCKER) {
                thanos.getActivityStackSupervisor().setLaunchOtherAppBlockerEnabled(false);
            } else if (featureId == PrebuiltFeatureIds.ID_CLEAN_TASK_REMOVAL) {
                thanos.getActivityManager().setCleanUpOnTaskRemovalEnabled(false);
            } else if (featureId == PrebuiltFeatureIds.ID_NOTIFICATION_RECORDER) {
                thanos.getNotificationManager().setNREnabled(NotificationRecord.Types.TYPE_CLIPBOARD, false);
                thanos.getNotificationManager().setNREnabled(NotificationRecord.Types.TYPE_TOAST, false);
                thanos.getNotificationManager().setNREnabled(NotificationRecord.Types.TYPE_GENERAL_NOTIFICATION, false);
            } else if (featureId == PrebuiltFeatureIds.ID_PRIVACY_CHEAT) {
                thanos.getPrivacyManager().setPrivacyEnabled(false);
            } else if (featureId == PrebuiltFeatureIds.ID_PROFILE) {
                thanos.getProfileManager().setProfileEnabled(false);
            } else if (featureId == PrebuiltFeatureIds.ID_SCREEN_ON_NOTIFICATION) {
                thanos.getNotificationManager().setScreenOnNotificationEnabled(false);
            } else if (featureId == PrebuiltFeatureIds.ID_SMART_STANDBY) {
                thanos.getActivityManager().setSmartStandByEnabled(false);
            } else if (featureId == PrebuiltFeatureIds.ID_TASK_BLUR) {
                thanos.getActivityManager().setRecentTaskBlurEnabled(false);
            } else if (featureId == PrebuiltFeatureIds.ID_TRAMPOLINE) {
                thanos.getActivityStackSupervisor().setActivityTrampolineEnabled(false);
            } else if (featureId == PrebuiltFeatureIds.ID_WAKELOCK_REMOVER) {
                thanos.getPowerManager().setWakeLockBlockerEnabled(false);
            } else if (featureId == PrebuiltFeatureIds.ID_WECHAT_PUSH) {
                thanos.getPushDelegateManager().setWeChatEnabled(false);
            }
        }

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_KEY_FEATURE_FLAG_ + featureId, value)
                .apply();
    }

}
