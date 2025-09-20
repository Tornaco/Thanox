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

import github.tornaco.android.thanos.BuildProp
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.core.util.OsUtils
import github.tornaco.android.thanos.support.AppFeatureManager.showSubscribeDialog
import github.tornaco.android.thanos.support.AppFeatureManager.withSubscriptionStatus
import github.tornaco.android.thanos.util.BrowserUtils
import github.tornaco.android.thanox.module.notification.recorder.NotificationRecordSettingsActivity
import github.tornaco.android.thanox.module.notification.recorder.ui.stats.StatsActivity
import github.tornaco.practice.honeycomb.locker.ui.setup.LockSettingsActivity
import github.tornaco.thanos.android.module.profile.ConsoleActivity
import github.tornaco.thanos.android.module.profile.GlobalVarListActivity
import github.tornaco.thanos.android.module.profile.LogActivity
import github.tornaco.thanos.android.module.profile.RuleEngineSettingsActivity
import github.tornaco.thanos.android.module.profile.example.ProfileExampleActivity
import github.tornaco.thanos.android.module.profile.online.OnlineProfileActivity
import now.fortuitous.thanos.start.chart.ComposeStartChartActivity

object PrebuiltFeatures {
    private val boost = FeatureItemGroup(
        titleRes = github.tornaco.android.thanos.res.R.string.nav_title_boost,
        items = listOf(
            FeatureItem(
                id = PrebuiltFeatureIds.ID_ONE_KEY_CLEAR,
                packedIconRes = R.drawable.ic_nav_boost,
                iconRes = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_rocket_fill,
                titleRes = github.tornaco.android.thanos.res.R.string.feature_title_one_key_boost_abbr,
                requiredFeature = BuildProp.THANOX_FEATURE_BG_TASK_CLEAN,
                themeColor = R.color.nav_icon_boost,
                menuItems = listOf(
                    github.tornaco.android.thanos.res.R.string.nav_title_settings to {
                        OneKeyBoostSettingsActivity.start(it)
                    }
                )
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_BACKGROUND_START,
                packedIconRes = R.drawable.ic_nav_bg_start,
                iconRes = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_forbid_2_fill,
                titleRes = github.tornaco.android.thanos.res.R.string.feature_title_start_restrict_abbr,
                requiredFeature = BuildProp.THANOX_FEATURE_START_BLOCKER,
                themeColor = R.color.nav_icon_bg_start,
                menuItems = listOf(
                    github.tornaco.android.thanos.res.R.string.menu_title_start_restrict_charts to {
                        withSubscriptionStatus(it) { subscribed: Boolean ->
                            if (subscribed) {
                                ComposeStartChartActivity.Starter.start(it)
                            } else {
                                showSubscribeDialog(it)
                            }
                        }
                    },
                    github.tornaco.android.thanos.res.R.string.menu_title_rules to {
                        withSubscriptionStatus(it) { subscribed: Boolean ->
                            if (subscribed) {
                                now.fortuitous.thanos.start.StartRuleActivity.start(it)
                            } else {
                                showSubscribeDialog(it)
                            }
                        }
                    }
                )
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_BACKGROUND_RESTRICT,
                packedIconRes = R.drawable.ic_nav_bg_restrict,
                iconRes = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_refresh_fill,
                titleRes = github.tornaco.android.thanos.res.R.string.feature_title_bg_restrict_abbr,
                requiredFeature = BuildProp.THANOX_FEATURE_BG_TASK_CLEAN,
                themeColor = R.color.nav_icon_bg_restrict,
                menuItems = listOf(
                    github.tornaco.android.thanos.res.R.string.nav_title_settings to {
                        now.fortuitous.thanos.start.BgRestrictSettingsActivity.start(it)
                    }
                )
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_CLEAN_TASK_REMOVAL,
                packedIconRes = R.drawable.ic_nav_task_removal,
                iconRes = R.drawable.ic_clear_all_black_24dp,
                titleRes = github.tornaco.android.thanos.res.R.string.feature_title_clean_when_task_removed,
                requiredFeature = BuildProp.THANOX_FEATURE_RECENT_TASK_REMOVAL,
                themeColor = R.color.nav_icon_task_removal
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_SMART_FREEZE,
                packedIconRes = R.drawable.ic_nav_smart_freeze,
                iconRes = R.drawable.ic_ac_unit_black_24dp,
                titleRes = github.tornaco.android.thanos.res.R.string.feature_title_smart_app_freeze,
                requiredFeature = BuildProp.THANOX_FEATURE_EXT_APP_SMART_FREEZE,
                themeColor = R.color.nav_icon_smart_freeze,
                menuItems = listOf(
                    github.tornaco.android.thanos.res.R.string.nav_title_settings to {
                        now.fortuitous.thanos.power.SmartFreezeSettingsActivity.start(it)
                    }
                )
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_APPS_MANAGER_RECENT_USED,
                packedIconRes = R.drawable.ic_nav_app_manager_recent_used,
                iconRes = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_history_fill,
                titleRes = github.tornaco.android.thanos.res.R.string.titile_suggested_apps_recent_used,
                themeColor = R.color.nav_icon_apps_manager,
                menuItems = listOf(
                    github.tornaco.android.thanos.res.R.string.feature_title_apps_manager to {
                        now.fortuitous.thanos.apps.AioAppListActivity.start(
                            it,
                            PrebuiltFeatureIds.ID_APPS_MANAGER
                        )
                    },
                    github.tornaco.android.thanos.res.R.string.title_package_sets to {
                        now.fortuitous.thanos.apps.PackageSetListActivity.start(it)
                    },
                )
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_APPS_MANAGER,
                packedIconRes = R.drawable.ic_nav_app_manager,
                iconRes = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_apps_2_fill,
                titleRes = github.tornaco.android.thanos.res.R.string.title_suggested_apps_view_all,
                themeColor = R.color.nav_icon_apps_manager,
                menuItems = listOf(
                    github.tornaco.android.thanos.res.R.string.feature_title_apps_manager to {
                        now.fortuitous.thanos.apps.AioAppListActivity.start(
                            it,
                            PrebuiltFeatureIds.ID_APPS_MANAGER
                        )
                    },
                    github.tornaco.android.thanos.res.R.string.title_package_sets to {
                        now.fortuitous.thanos.apps.PackageSetListActivity.start(it)
                    },
                )
            )
        )
    )

    private val secure = FeatureItemGroup(
        titleRes = github.tornaco.android.thanos.res.R.string.nav_title_secure,
        items = listOfNotNull(
            FeatureItem(
                id = PrebuiltFeatureIds.ID_PRIVACY_CHEAT,
                packedIconRes = R.drawable.ic_nav_priv_cheat,
                iconRes = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_spy_fill,
                titleRes = github.tornaco.android.thanos.res.R.string.feature_title_data_cheat,
                requiredFeature = BuildProp.THANOX_FEATURE_PRIVACY_DATA_CHEAT,
                themeColor = R.color.nav_icon_priv_cheat,
                menuItems = listOf(
                    github.tornaco.android.thanos.res.R.string.priv_title_fields_template to {
                        now.fortuitous.thanos.privacy.FieldsTemplateListActivity.start(it, 10086)
                    },
                    github.tornaco.android.thanos.res.R.string.privacy_record to {
                        withSubscriptionStatus(it) { isSubscribed: Boolean ->
                            if (isSubscribed) {
                                now.fortuitous.thanos.privacy.CheatRecordViewerActivity.start(it)
                            } else {
                                showSubscribeDialog(it)
                            }
                        }
                    }
                )
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_OPS_BY_OPS,
                packedIconRes = R.drawable.ic_nav_ops,
                iconRes = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_shield_star_fill,
                titleRes = github.tornaco.android.thanos.res.R.string.module_ops_feature_title_ops_app_list,
                requiredFeature = BuildProp.THANOX_FEATURE_PRIVACY_OPS,
                themeColor = R.color.nav_icon_ops
            ),
            if (false) FeatureItem(
                id = PrebuiltFeatureIds.ID_THANOX_OPS,
                packedIconRes = R.drawable.ic_nav_ops,
                iconRes = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_shield_star_fill,
                titleRes = github.tornaco.android.thanos.res.R.string.module_ops_feature_title_thanox_ops,
                requiredFeature = BuildProp.THANOX_FEATURE_PRIVACY_OPS,
                themeColor = R.color.nav_icon_ops
            ) else null,
            FeatureItem(
                id = PrebuiltFeatureIds.ID_APP_LOCK,
                packedIconRes = R.drawable.ic_nav_app_lock,
                iconRes = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_phone_lock_fill,
                titleRes = github.tornaco.android.thanos.res.R.string.feature_title_app_lock,
                requiredFeature = BuildProp.THANOX_FEATURE_PRIVACY_APPLOCK,
                themeColor = R.color.nav_icon_app_lock,
                menuItems = listOf(
                    github.tornaco.android.thanos.res.R.string.module_locker_title_settings to {
                        LockSettingsActivity.start(it)
                    }
                )
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_TASK_BLUR,
                packedIconRes = R.drawable.ic_nav_task_blur,
                iconRes = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_brush_2_fill,
                titleRes = github.tornaco.android.thanos.res.R.string.feature_title_recent_task_blur,
                requiredFeature = BuildProp.THANOX_FEATURE_PRIVACY_TASK_BLUR,
                themeColor = R.color.nav_icon_task_blur
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_OP_REMIND,
                packedIconRes = R.drawable.ic_nav_op_remind,
                iconRes = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_alarm_warning_fill,
                titleRes = github.tornaco.android.thanos.res.R.string.module_ops_feature_title_ops_remind_list,
                requiredFeature = BuildProp.THANOX_FEATURE_PRIVACY_OPS_REMINDER,
                themeColor = R.color.nav_icon_op_remind
            ),
            if (OsUtils.isSOrAbove()) {
                FeatureItem(
                    id = PrebuiltFeatureIds.ID_SENSOR_OFF,
                    packedIconRes = R.drawable.ic_nav_sensor_off,
                    iconRes = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_sensor_fill,
                    titleRes = github.tornaco.android.thanos.res.R.string.sensor_off,
                    themeColor = R.color.nav_icon_sensor_off
                )
            } else null,
            FeatureItem(
                id = PrebuiltFeatureIds.ID_UNINSTALL_BLOCKER,
                packedIconRes = R.drawable.ic_nav_uninstall_blocker,
                iconRes = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_shield_check_fill,
                titleRes = github.tornaco.android.thanos.res.R.string.feature_title_uninstall_blocker,
                themeColor = R.color.nav_icon_uninstall_blocker
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_CLEAR_DATA_BLOCKER,
                packedIconRes = R.drawable.ic_nav_clear_data_blocker,
                iconRes = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_shield_fill,
                titleRes = github.tornaco.android.thanos.res.R.string.feature_title_clear_data_blocker,
                themeColor = R.color.nav_icon_clear_data_blocker
            ),
        )
    )

    private val ext = FeatureItemGroup(
        titleRes = github.tornaco.android.thanos.res.R.string.nav_title_exp,
        items = listOfNotNull(
            FeatureItem(
                id = PrebuiltFeatureIds.ID_TRAMPOLINE,
                packedIconRes = R.drawable.ic_nav_activity_replacement,
                iconRes = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_guide_fill,
                titleRes = github.tornaco.android.thanos.res.R.string.module_activity_trampoline_app_name_abbr,
                requiredFeature = BuildProp.THANOX_FEATURE_APP_TRAMPOLINE,
                themeColor = R.color.nav_icon_activity_replacement
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_PROFILE,
                packedIconRes = R.drawable.ic_nav_profile,
                iconRes = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_thunderstorms_fill,
                titleRes = github.tornaco.android.thanos.res.R.string.module_profile_feature_name,
                requiredFeature = BuildProp.THANOX_FEATURE_PROFILE,
                themeColor = R.color.nav_icon_profile,
                menuItems = listOf(
                    github.tornaco.android.thanos.res.R.string.module_profile_rule_wiki to {
                        BrowserUtils.launch(it, BuildProp.THANOX_URL_DOCS_PROFILE)
                    },
                    github.tornaco.android.thanos.res.R.string.module_profile_rule_impor_example to {
                        ProfileExampleActivity.Starter.start(it)
                    },
                    github.tornaco.android.thanos.res.R.string.module_profile_rule_online to { activity ->
                        withSubscriptionStatus(activity) {
                            if (it) {
                                OnlineProfileActivity.Starter.start(activity)
                            } else {
                                showSubscribeDialog(activity)
                            }
                        }
                    },
                    github.tornaco.android.thanos.res.R.string.module_profile_title_global_var to {
                        GlobalVarListActivity.start(it)
                    },
                    github.tornaco.android.thanos.res.R.string.module_profile_title_rule_engine to {
                        RuleEngineSettingsActivity.start(it)
                    },
                    github.tornaco.android.thanos.res.R.string.module_profile_title_action_console to { activity ->
                        withSubscriptionStatus(activity) {
                            if (it) {
                                ConsoleActivity.Starter.start(activity)
                            } else {
                                showSubscribeDialog(activity)
                            }
                        }
                    },
                    github.tornaco.android.thanos.res.R.string.module_profile_title_log to {
                        LogActivity.Starter.start(it)
                    },
                )
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_SMART_STANDBY,
                packedIconRes = R.drawable.ic_nav_smart_standby,
                iconRes = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_mickey_fill,
                titleRes = github.tornaco.android.thanos.res.R.string.feature_title_smart_app_standby,
                requiredFeature = BuildProp.THANOX_FEATURE_APP_SMART_STAND_BY,
                themeColor = R.color.nav_icon_smart_standby,
                menuItems = listOf(
                    github.tornaco.android.thanos.res.R.string.menu_title_rules to {
                        now.fortuitous.thanos.power.StandByRuleActivity.start(it)
                    },
                    github.tornaco.android.thanos.res.R.string.nav_title_settings to {
                        now.fortuitous.thanos.power.SmartStandbySettingsActivity.start(it)
                    },
                )
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_WAKELOCK_REMOVER,
                packedIconRes = R.drawable.ic_nav_wakelock_remover,
                iconRes = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_scissors_fill,
                titleRes = github.tornaco.android.thanos.res.R.string.feature_title_wakelock_blocker,
                requiredFeature = BuildProp.THANOX_FEATURE_WAKELOCK_REMOVER,
                themeColor = R.color.nav_icon_wakelock_remover
            ),
            if (OsUtils.isROrAbove()) {
                FeatureItem(
                    id = PrebuiltFeatureIds.ID_INFINITE_Z,
                    packedIconRes = R.drawable.ic_nav_app_clone,
                    iconRes = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_briefcase_fill,
                    titleRes = github.tornaco.android.thanos.res.R.string.feature_title_infinite_z,
                    requiredFeature = BuildProp.THANOX_FEATURE_IZ,
                    themeColor = R.color.nav_icon_app_clone
                )
            } else null,
            if (OsUtils.isROrAbove() && OsUtils.isTOrAbove()) {
                FeatureItem(
                    id = PrebuiltFeatureIds.ID_INFINITE_Z2,
                    packedIconRes = R.drawable.ic_nav_app_clone,
                    iconRes = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_briefcase_fill,
                    titleRes = github.tornaco.android.thanos.res.R.string.feature_title_infinite_z2,
                    requiredFeature = BuildProp.THANOX_FEATURE_IZ,
                    themeColor = R.color.nav_icon_app_clone
                )
            } else null,
            if (OsUtils.isROrAbove()) {
                FeatureItem(
                    id = PrebuiltFeatureIds.ID_LAUNCH_OTHER_APP_BLOCKER,
                    packedIconRes = R.drawable.ic_nav_launch_other_app,
                    iconRes = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_external_link_fill,
                    titleRes = github.tornaco.android.thanos.res.R.string.launch_other_app,
                    themeColor = R.color.nav_icon_launch_other_app
                )
            } else null,
            if (OsUtils.isOOrAbove()) {
                FeatureItem(
                    id = PrebuiltFeatureIds.ID_RESIDENT,
                    packedIconRes = R.drawable.ic_nav_resident,
                    iconRes = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_shield_cross_fill,
                    titleRes = github.tornaco.android.thanos.res.R.string.pre_title_resident,
                    themeColor = R.color.nav_icon_resident
                )
            } else null,
            if (OsUtils.isSOrAbove()) {
                FeatureItem(
                    id = PrebuiltFeatureIds.ID_SHORTCUT_CLEANER,
                    packedIconRes = R.drawable.ic_nav_shortcut_cleaner,
                    iconRes = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_scissors_cut_fill,
                    titleRes = github.tornaco.android.thanos.res.R.string.feature_title_shortcut_cleaner,
                    themeColor = R.color.nav_icon_shortcut_cleaner
                )
            } else null
        )
    )

    private val notification = FeatureItemGroup(
        titleRes = github.tornaco.android.thanos.res.R.string.module_notification_recorder_feature_title_notification_center,
        items = listOf(
            FeatureItem(
                id = PrebuiltFeatureIds.ID_SCREEN_ON_NOTIFICATION,
                packedIconRes = R.drawable.ic_nav_screen_on_notification,
                iconRes = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_notification_badge_fill,
                titleRes = github.tornaco.android.thanos.res.R.string.feature_title_light_on_notification,
                requiredFeature = BuildProp.THANOX_FEATURE_EXT_N_UP,
                themeColor = R.color.nav_icon_screen_on_notification
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_NOTIFICATION_RECORDER,
                packedIconRes = R.drawable.ic_nav_nr,
                iconRes = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_chat_1_fill,
                titleRes = github.tornaco.android.thanos.res.R.string.module_notification_recorder_feature_title_notification_recorder,
                requiredFeature = BuildProp.THANOX_FEATURE_EXT_N_RECORDER,
                themeColor = R.color.nav_icon_nr,
                menuItems = listOf(
                    github.tornaco.android.thanos.res.R.string.module_notification_recorder_stats to {
                        StatsActivity.Starter.start(it)
                    },
                    github.tornaco.android.thanos.res.R.string.module_notification_recorder_settings to {
                        NotificationRecordSettingsActivity.start(it)
                    }
                )
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_WECHAT_PUSH,
                packedIconRes = R.drawable.ic_nav_wechat_push,
                iconRes = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_wechat_2_fill,
                titleRes = github.tornaco.android.thanos.res.R.string.module_push_message_delegate_title_wechat_proxy,
                requiredFeature = BuildProp.THANOX_FEATURE_PUSH_DELEGATE,
                themeColor = R.color.nav_icon_wechat_push
            )
        )
    )

    fun all(filter: (FeatureItem) -> Boolean = { true }): List<FeatureItemGroup> {
        return listOf(
            boost, secure, ext, notification
        ).map {
            it.copy(items = it.items.filter(filter))
        }
    }
}