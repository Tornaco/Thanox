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

package github.tornaco.android.thanos.main;

import android.app.Activity;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.elvishew.xlog.XLog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import github.tornaco.android.plugin.push.message.delegate.WechatPushDeleteMainActivity;
import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.ThanosApp;
import github.tornaco.android.thanos.app.donate.DonateIntroDialogKt;
import github.tornaco.android.thanos.app.donate.DonateSettings;
import github.tornaco.android.thanos.apps.SuggestedAppsActivity;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.infinite.InfiniteZActivity;
import github.tornaco.android.thanos.notification.ScreenOnNotificationActivity;
import github.tornaco.android.thanos.power.SmartFreezeActivity;
import github.tornaco.android.thanos.power.SmartStandbyV2Activity;
import github.tornaco.android.thanos.power.wakelock.WakeLockRemoverActivity;
import github.tornaco.android.thanos.privacy.DataCheatActivity;
import github.tornaco.android.thanos.start.BackgroundRestrictActivity;
import github.tornaco.android.thanos.start.StartRestrictActivity;
import github.tornaco.android.thanos.task.CleanUpOnTaskRemovedActivity;
import github.tornaco.android.thanos.task.RecentTaskBlurListActivity;
import github.tornaco.android.thanos.util.BrowserUtils;
import github.tornaco.android.thanox.module.activity.trampoline.ActivityTrampolineActivity;
import github.tornaco.android.thanox.module.notification.recorder.ui.NotificationRecordActivity;
import github.tornaco.practice.honeycomb.locker.ui.start.LockerStartActivity;
import github.tornaco.thanos.android.module.profile.RuleListActivity;
import github.tornaco.thanos.android.ops.OpsBottomNavActivity;
import github.tornaco.thanos.android.ops.ops.remind.RemindOpsActivity;

public class PrebuiltFeatureLauncher {
    private final Activity context;
    @Nullable
    private final NavViewModel navViewModel;
    private final Handler uiHandler;

    public PrebuiltFeatureLauncher(Activity context,
                                   @Nullable NavViewModel navViewModel,
                                   Handler uiHandler) {
        this.context = context;
        this.navViewModel = navViewModel;
        this.uiHandler = uiHandler;
    }

    void launch(int featureId) {
        XLog.d("PrebuiltFeatureLauncher, launch: %s", featureId);
        ThanosManager thanosManager = ThanosManager.from(context);
        if (featureId == PrebuiltFeatureIds.ID_ONE_KEY_CLEAR && navViewModel != null) {
            if (ThanosApp.isPrc() && !DonateSettings.isActivated(context)) {
                DonateIntroDialogKt.showDonateIntroDialog(context);
                return;
            }
            navViewModel.cleanUpBackgroundTasks();
            // Delay 1.5s to refresh
            uiHandler.postDelayed(navViewModel::start, 1500);
        } else if (featureId == PrebuiltFeatureIds.ID_BACKGROUND_START) {
            StartRestrictActivity.start(context);
        } else if (featureId == PrebuiltFeatureIds.ID_BACKGROUND_RESTRICT) {
            BackgroundRestrictActivity.start(context);
        } else if (featureId == PrebuiltFeatureIds.ID_CLEAN_TASK_REMOVAL) {
            CleanUpOnTaskRemovedActivity.start(context);
        } else if (featureId == PrebuiltFeatureIds.ID_APPS_MANAGER) {
            SuggestedAppsActivity.start(context);
        } else if (featureId == PrebuiltFeatureIds.ID_SCREEN_ON_NOTIFICATION) {
            ScreenOnNotificationActivity.start(context);
        } else if (featureId == PrebuiltFeatureIds.ID_NOTIFICATION_RECORDER) {
            NotificationRecordActivity.Starter.start(context);
        } else if (featureId == PrebuiltFeatureIds.ID_TRAMPOLINE) {
            if (ThanosApp.isPrc() && !DonateSettings.isActivated(context)) {
                DonateIntroDialogKt.showDonateIntroDialog(context);
                // Disable this feature, since it is free to use before.
                thanosManager.ifServiceInstalled(manager -> {
                    XLog.w("Disabling ActivityTrampoline.");
                    manager.getActivityStackSupervisor().setActivityTrampolineEnabled(false);
                });
                return;
            }
            ActivityTrampolineActivity.start(context);
        } else if (featureId == PrebuiltFeatureIds.ID_PROFILE) {
            RuleListActivity.start(context);
        } else if (featureId == PrebuiltFeatureIds.ID_SMART_STANDBY) {
            if (ThanosApp.isPrc() && !DonateSettings.isActivated(context)) {
                DonateIntroDialogKt.showDonateIntroDialog(context);
                return;
            }
            SmartStandbyV2Activity.start(context);
        } else if (featureId == PrebuiltFeatureIds.ID_SMART_FREEZE) {
            SmartFreezeActivity.Starter.start(context);
        } else if (featureId == PrebuiltFeatureIds.ID_PRIVACY_CHEAT) {
            DataCheatActivity.start(context);
        } else if (featureId == PrebuiltFeatureIds.ID_OPS_BY_OPS) {
            OpsBottomNavActivity.Starter.start(context);
        } else if (featureId == PrebuiltFeatureIds.ID_TASK_BLUR) {
            RecentTaskBlurListActivity.start(context);
        } else if (featureId == PrebuiltFeatureIds.ID_OP_REMIND) {
            RemindOpsActivity.start(context);
        } else if (featureId == PrebuiltFeatureIds.ID_APP_LOCK) {
            if (ThanosApp.isPrc() && !DonateSettings.isActivated(context)) {
                DonateIntroDialogKt.showDonateIntroDialog(context);
                return;
            }
            LockerStartActivity.start(context);
        } else if (featureId == PrebuiltFeatureIds.ID_INFINITE_Z) {
            if (ThanosApp.isPrc() && !DonateSettings.isActivated(context)) {
                DonateIntroDialogKt.showDonateIntroDialog(context);
                return;
            }
            InfiniteZActivity.start(context);
        } else if (featureId == PrebuiltFeatureIds.ID_PLUGINS) {
            PluginListActivity.start(context);
        } else if (featureId == PrebuiltFeatureIds.ID_FEEDBACK) {
            showFeedbackDialog();
        } else if (featureId == PrebuiltFeatureIds.ID_GUIDE) {
            BrowserUtils.launch(context, BuildProp.THANOX_URL_DOCS_HOME);
        } else if (featureId == PrebuiltFeatureIds.ID_WECHAT_PUSH) {
            if (ThanosApp.isPrc() && !DonateSettings.isActivated(context)) {
                DonateIntroDialogKt.showDonateIntroDialog(context);
                return;
            }
            WechatPushDeleteMainActivity.start(context);
        } else if (featureId == PrebuiltFeatureIds.ID_WAKELOCK_REMOVER) {
            if (ThanosApp.isPrc() && !DonateSettings.isActivated(context)) {
                DonateIntroDialogKt.showDonateIntroDialog(context);
                return;
            }
            WakeLockRemoverActivity.Starter.start(context);
        }
    }

    private void showFeedbackDialog() {
        new MaterialAlertDialogBuilder(context)
                .setTitle(R.string.nav_title_feedback)
                .setMessage(R.string.dialog_message_feedback)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}
