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

package now.fortuitous.thanos.main

import android.app.Activity
import android.content.Intent
import com.elvishew.xlog.XLog
import github.tornaco.android.plugin.push.message.delegate.WechatPushDeleteMainActivity
import github.tornaco.android.thanos.BuildProp
import github.tornaco.android.thanos.core.T
import github.tornaco.android.thanos.support.AppFeatureManager
import github.tornaco.android.thanos.support.withThanos
import github.tornaco.android.thanos.util.BrowserUtils
import github.tornaco.android.thanox.module.activity.trampoline.ActivityTrampolineActivity
import github.tornaco.android.thanox.module.notification.recorder.ui.NotificationRecordActivity
import github.tornaco.thanos.android.module.profile.RuleListActivity
import github.tornaco.thanos.android.ops.ops.by.ops.OpsBottomNavActivity
import github.tornaco.thanos.android.ops.ops.by.ops.ThanoxOpsBottomNavActivity
import github.tornaco.thanos.android.ops.ops.remind.RemindOpsActivity
import github.tornaco.thanos.android.ops2.byop.Ops2Activity
import now.fortuitous.thanos.apps.AioAppListActivity
import now.fortuitous.thanos.power.SmartFreezeActivity
import now.fortuitous.thanos.power.wakelock.WakeLockBlockerActivity

class PrebuiltFeatureLauncher(
    private val context: Activity,
    private val onProcessCleared: () -> Unit,
) {
    fun launch(featureId: Int) {
        XLog.d("PrebuiltFeatureLauncher, launch: %s", featureId)
        val handled = context.withThanos {
            when (featureId) {
                PrebuiltFeatureIds.ID_ONE_KEY_CLEAR -> {
                    AppFeatureManager.withSubscriptionStatus(context) {
                        if (it) {
                            context.sendBroadcast(Intent(T.Actions.ACTION_RUNNING_PROCESS_CLEAR))
                            onProcessCleared()
                        } else {
                            AppFeatureManager.showSubscribeDialog(context)
                        }
                    }
                }

                PrebuiltFeatureIds.ID_APPS_MANAGER,
                PrebuiltFeatureIds.ID_BACKGROUND_START,
                PrebuiltFeatureIds.ID_BACKGROUND_RESTRICT,
                PrebuiltFeatureIds.ID_CLEAN_TASK_REMOVAL,
                PrebuiltFeatureIds.ID_SMART_STANDBY,
                PrebuiltFeatureIds.ID_TASK_BLUR -> {
                    AioAppListActivity.start(context, featureId)
                }

                PrebuiltFeatureIds.ID_APP_LOCK,
                PrebuiltFeatureIds.ID_LAUNCH_OTHER_APP_BLOCKER,
                PrebuiltFeatureIds.ID_SENSOR_OFF,
                PrebuiltFeatureIds.ID_PRIVACY_CHEAT,
                PrebuiltFeatureIds.ID_SCREEN_ON_NOTIFICATION,
                PrebuiltFeatureIds.ID_RESIDENT -> {
                    AppFeatureManager.withSubscriptionStatus(context) {
                        if (it) {
                            AioAppListActivity.start(context, featureId)
                        } else {
                            AppFeatureManager.showSubscribeDialog(context)
                        }
                    }
                }

                PrebuiltFeatureIds.ID_NOTIFICATION_RECORDER -> {
                    NotificationRecordActivity.start(context)
                }

                PrebuiltFeatureIds.ID_TRAMPOLINE -> {
                    AppFeatureManager.withSubscriptionStatus(context) {
                        if (it) {
                            ActivityTrampolineActivity.start(context)
                        } else {
                            AppFeatureManager.showSubscribeDialog(context)
                            // Disable this feature, since it is free to use before.
                            XLog.w("Disabling ActivityTrampoline.")
                            activityStackSupervisor.isActivityTrampolineEnabled = false
                        }
                    }
                }

                PrebuiltFeatureIds.ID_PROFILE -> {
                    RuleListActivity.start(context)
                }

                PrebuiltFeatureIds.ID_SMART_FREEZE -> {
                    SmartFreezeActivity.start(context)
                }

                PrebuiltFeatureIds.ID_OPS_BY_OPS -> {
                    if (now.fortuitous.thanos.pref.AppPreference.isFeatureNoticeAccepted(
                            context,
                            "NEW_OPS"
                        )
                    ) {
                        Ops2Activity.start(context)
                    } else {
                        OpsBottomNavActivity.start(context)
                    }
                }

                PrebuiltFeatureIds.ID_THANOX_OPS -> {
                    ThanoxOpsBottomNavActivity.start(context)
                }

                PrebuiltFeatureIds.ID_OP_REMIND -> {
                    RemindOpsActivity.start(context)
                }

                PrebuiltFeatureIds.ID_INFINITE_Z -> {
                    AppFeatureManager.withSubscriptionStatus(context) {
                        if (it) {
                            now.fortuitous.thanos.infinite.InfiniteZActivity.start(context)
                        } else {
                            AppFeatureManager.showSubscribeDialog(context)
                        }
                    }
                }

                PrebuiltFeatureIds.ID_GUIDE -> {
                    BrowserUtils.launch(context, BuildProp.THANOX_URL_DOCS_HOME)
                }

                PrebuiltFeatureIds.ID_WECHAT_PUSH -> {
                    AppFeatureManager.withSubscriptionStatus(context) {
                        if (it) {
                            WechatPushDeleteMainActivity.start(context)
                        } else {
                            AppFeatureManager.showSubscribeDialog(context)
                        }
                    }
                }

                PrebuiltFeatureIds.ID_WAKELOCK_REMOVER -> {
                    AppFeatureManager.withSubscriptionStatus(context) {
                        if (it) {
                            WakeLockBlockerActivity.Starter.start(context)
                        } else {
                            AppFeatureManager.showSubscribeDialog(context)
                        }
                    }
                }
            }
            true
        } ?: false

        if (!handled) {
            DialogUtils.showNotActivated(context)
        }
    }
}