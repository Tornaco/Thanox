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

package github.tornaco.android.thanos.core.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.IActivityManager;
import android.content.Intent;
import android.content.IntentSender;

import com.elvishew.xlog.XLog;

import util.XposedHelpers;

// https://cs.android.com/android/platform/superproject/+/master:packages/apps/Settings/src/com/android/settings/password/ConfirmDeviceCredentialUtils.java;l=36?q=ConfirmDeviceCredentialUtils&ss=android
public class ConfirmDeviceCredentialUtils {
    public static final int FLAG_THANOX_VERIFIED = 0x00200000;
    // https://github.com/crdroidandroid/android_frameworks_base/blob/f860420b16089ca9fe5ce6f2c52c6fe89bb50bff/core/java/android/app/ActivityOptions.java#L135
    public static final int MODE_BACKGROUND_ACTIVITY_START_ALLOWED = 1;

    public static void checkForPendingIntent(Activity activity) {
        // See Change-Id I52c203735fa9b53fd2f7df971824747eeb930f36 for context
        int taskId = activity.getIntent().getIntExtra(Intent.EXTRA_TASK_ID, -1);
        if (taskId != -1) {
            try {
                IActivityManager activityManager = ActivityManager.getService();
                final ActivityOptions options = ActivityOptions.makeBasic();
                activityManager.startActivityFromRecents(taskId, options.toBundle());
                return;
            } catch (Throwable e) {
                // Do nothing.
            }
        }
        IntentSender intentSender = activity.getIntent().getParcelableExtra(Intent.EXTRA_INTENT);
        XLog.d("intentSender: " + intentSender);
        if (intentSender != null) {
            try {
                ActivityOptions activityOptions =
                        ActivityOptions.makeBasic();
                // setPendingIntentBackgroundActivityStartMode(
                //                                        MODE_BACKGROUND_ACTIVITY_START_ALLOWED);
                if (OsUtils.is15OrAbove()) {
                    activityOptions = (ActivityOptions) XposedHelpers.callMethod(
                            activityOptions,
                            "setPendingIntentBackgroundActivityStartMode",
                            MODE_BACKGROUND_ACTIVITY_START_ALLOWED);
                    XLog.i("activityOptions.setPendingIntentBackgroundActivityStartMode: " + activityOptions);
                }

                activity.startIntentSenderForResult(intentSender, -1, null, 0, FLAG_THANOX_VERIFIED, 0, activityOptions.toBundle());
            } catch (Throwable e) {
                XLog.e(e, "startIntentSenderForResult error");
            }
        }
    }

}
