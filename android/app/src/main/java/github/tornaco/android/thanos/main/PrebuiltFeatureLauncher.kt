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

package github.tornaco.android.thanos.main

import android.app.Activity
import android.content.Intent
import com.elvishew.xlog.XLog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import github.tornaco.android.plugin.push.message.delegate.WechatPushDeleteMainActivity
import github.tornaco.android.thanos.BuildProp
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.apps.SuggestedAppsActivity
import github.tornaco.android.thanos.core.T
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.feature.access.AppFeatureManager
import github.tornaco.android.thanos.infinite.InfiniteZActivity
import github.tornaco.android.thanos.notification.NotificationCenterActivity
import github.tornaco.android.thanos.notification.ScreenOnNotificationActivity
import github.tornaco.android.thanos.power.SmartFreezeActivity
import github.tornaco.android.thanos.power.SmartStandbyV2Activity
import github.tornaco.android.thanos.power.wakelock.WakeLockBlockerActivity
import github.tornaco.android.thanos.privacy.DataCheatActivity
import github.tornaco.android.thanos.start.BackgroundRestrictActivity
import github.tornaco.android.thanos.start.StartRestrictActivity
import github.tornaco.android.thanos.task.CleanUpOnTaskRemovedActivity
import github.tornaco.android.thanos.task.RecentTaskBlurListActivity
import github.tornaco.android.thanos.util.BrowserUtils
import github.tornaco.android.thanox.module.activity.trampoline.ActivityTrampolineActivity
import github.tornaco.android.thanox.module.notification.recorder.ui.NotificationRecordActivity
import github.tornaco.practice.honeycomb.locker.ui.start.LockerStartActivity
import github.tornaco.thanos.android.module.profile.RuleListActivity
import github.tornaco.thanos.android.ops.OpsBottomNavActivity
import github.tornaco.thanos.android.ops.ops.remind.RemindOpsActivity

class PrebuiltFeatureLauncher(
    private val context: Activity,
    private val onProcessCleared: () -> Unit,
) {
    fun launch(featureId: Int) {
        XLog.d("PrebuiltFeatureLauncher, launch: %s", featureId)
        val thanosManager = ThanosManager.from(context)
        when (featureId) {
            PrebuiltFeatureIds.ID_ONE_KEY_CLEAR -> {
                AppFeatureManager.withSubscriptionStatus(context) {
                    if (it) {
                        context.sendBroadcast(Intent(T.Actions.ACTION_RUNNING_PROCESS_CLEAR))
                        onProcessCleared()
                    } else {
                        AppFeatureManager.showDonateIntroDialog(this.context)
                    }
                }
            }
            PrebuiltFeatureIds.ID_BACKGROUND_START -> {
                StartRestrictActivity.start(context)
            }
            PrebuiltFeatureIds.ID_BACKGROUND_RESTRICT -> {
                BackgroundRestrictActivity.start(context)
            }
            PrebuiltFeatureIds.ID_CLEAN_TASK_REMOVAL -> {
                CleanUpOnTaskRemovedActivity.start(context)
            }
            PrebuiltFeatureIds.ID_APPS_MANAGER -> {
                SuggestedAppsActivity.start(context)
            }
            PrebuiltFeatureIds.ID_SCREEN_ON_NOTIFICATION -> {
                ScreenOnNotificationActivity.start(context)
            }
            PrebuiltFeatureIds.ID_NOTIFICATION_RECORDER -> {
                NotificationRecordActivity.start(context)
            }
            PrebuiltFeatureIds.ID_NOTIFICATION_CENTER -> {
                NotificationCenterActivity.Starter.start(context)
            }
            PrebuiltFeatureIds.ID_TRAMPOLINE -> {
                AppFeatureManager.withSubscriptionStatus(context) {
                    if (it) {
                        ActivityTrampolineActivity.start(context)
                    } else {
                        AppFeatureManager.showDonateIntroDialog(this.context)
                        // Disable this feature, since it is free to use before.
                        thanosManager.ifServiceInstalled { manager: ThanosManager ->
                            XLog.w("Disabling ActivityTrampoline.")
                            manager.activityStackSupervisor.isActivityTrampolineEnabled = false
                        }
                    }
                }
            }
            PrebuiltFeatureIds.ID_PROFILE -> {
                RuleListActivity.start(context)
            }
            PrebuiltFeatureIds.ID_SMART_STANDBY -> {
                AppFeatureManager.withSubscriptionStatus(context) {
                    if (it) {
                        SmartStandbyV2Activity.start(context)
                    } else {
                        AppFeatureManager.showDonateIntroDialog(context)
                    }
                }
            }
            PrebuiltFeatureIds.ID_SMART_FREEZE -> {
                SmartFreezeActivity.start(context)
            }
            PrebuiltFeatureIds.ID_PRIVACY_CHEAT -> {
                DataCheatActivity.start(context)
            }
            PrebuiltFeatureIds.ID_OPS_BY_OPS -> {
                OpsBottomNavActivity.start(context)
            }
            PrebuiltFeatureIds.ID_TASK_BLUR -> {
                RecentTaskBlurListActivity.start(context)
            }
            PrebuiltFeatureIds.ID_OP_REMIND -> {
                RemindOpsActivity.start(context)
            }
            PrebuiltFeatureIds.ID_APP_LOCK -> {
                AppFeatureManager.withSubscriptionStatus(context) {
                    if (it) {
                        LockerStartActivity.start(context)
                    } else {
                        AppFeatureManager.showDonateIntroDialog(context)
                    }
                }
            }
            PrebuiltFeatureIds.ID_INFINITE_Z -> {
                AppFeatureManager.withSubscriptionStatus(context) {
                    if (it) {
                        InfiniteZActivity.start(context)
                    } else {
                        AppFeatureManager.showDonateIntroDialog(context)
                    }
                }
            }
            PrebuiltFeatureIds.ID_PLUGINS -> {
            }
            PrebuiltFeatureIds.ID_FEEDBACK -> {
                showFeedbackDialog()
            }
            PrebuiltFeatureIds.ID_GUIDE -> {
                BrowserUtils.launch(context, BuildProp.THANOX_URL_DOCS_HOME)
            }
            PrebuiltFeatureIds.ID_WECHAT_PUSH -> {
                AppFeatureManager.withSubscriptionStatus(context) {
                    if (it) {
                        WechatPushDeleteMainActivity.start(context)
                    } else {
                        AppFeatureManager.showDonateIntroDialog(context)
                    }
                }
            }
            PrebuiltFeatureIds.ID_WAKELOCK_REMOVER -> {
                AppFeatureManager.withSubscriptionStatus(context) {
                    if (it) {
                        WakeLockBlockerActivity.Starter.start(context)
                    } else {
                        AppFeatureManager.showDonateIntroDialog(context)
                    }
                }
            }
        }
    }

    private fun showFeedbackDialog() {
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.nav_title_feedback)
            .setMessage(R.string.dialog_message_feedback)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }
}