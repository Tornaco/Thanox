package github.tornaco.android.thanos.main

import github.tornaco.android.thanos.BuildProp
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.apps.AppsManageActivity
import github.tornaco.android.thanos.apps.PackageSetListActivity
import github.tornaco.android.thanos.feature.access.AppFeatureManager.showDonateIntroDialog
import github.tornaco.android.thanos.feature.access.AppFeatureManager.withSubscriptionStatus
import github.tornaco.android.thanos.power.SmartFreezeSettingsActivity
import github.tornaco.android.thanos.power.SmartStandbySettingsActivity
import github.tornaco.android.thanos.power.StandByRuleActivity
import github.tornaco.android.thanos.privacy.CheatRecordViewerActivity
import github.tornaco.android.thanos.privacy.FieldsTemplateListActivity
import github.tornaco.android.thanos.start.BgRestrictSettingsActivity
import github.tornaco.android.thanos.start.StartRuleActivity
import github.tornaco.android.thanos.start.chart.ComposeStartChartActivity.Starter.start
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

object PrebuiltFeatures {
    private val boost = FeatureItemGroup(
        titleRes = R.string.nav_title_boost,
        items = listOf(
            FeatureItem(
                id = PrebuiltFeatureIds.ID_ONE_KEY_CLEAR,
                iconRes = R.drawable.ic_nav_boost,
                titleRes = R.string.feature_title_one_key_boost_abbr,
                requiredFeature = BuildProp.THANOX_FEATURE_BG_TASK_CLEAN,
                themeColor = R.color.nav_icon_boost
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_BACKGROUND_START,
                iconRes = R.drawable.ic_nav_bg_start,
                titleRes = R.string.feature_title_start_restrict_abbr,
                requiredFeature = BuildProp.THANOX_FEATURE_START_BLOCKER,
                themeColor = R.color.nav_icon_bg_start,
                menuItems = listOf(
                    R.string.menu_title_start_restrict_charts to {
                        withSubscriptionStatus(it) { subscribed: Boolean ->
                            if (subscribed) {
                                start(it)
                            } else {
                                showDonateIntroDialog(it)
                            }
                        }
                    },
                    R.string.menu_title_rules to {
                        withSubscriptionStatus(it) { subscribed: Boolean ->
                            if (subscribed) {
                                StartRuleActivity.start(it)
                            } else {
                                showDonateIntroDialog(it)
                            }
                        }
                    }
                )
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_BACKGROUND_RESTRICT,
                iconRes = R.drawable.ic_nav_bg_restrict,
                titleRes = R.string.feature_title_bg_restrict_abbr,
                requiredFeature = BuildProp.THANOX_FEATURE_BG_TASK_CLEAN,
                themeColor = R.color.nav_icon_bg_restrict,
                menuItems = listOf(
                    R.string.nav_title_settings to {
                        BgRestrictSettingsActivity.start(it)
                    }
                )
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_CLEAN_TASK_REMOVAL,
                iconRes = R.drawable.ic_nav_task_removal,
                titleRes = R.string.feature_title_clean_when_task_removed,
                requiredFeature = BuildProp.THANOX_FEATURE_RECENT_TASK_REMOVAL,
                themeColor = R.color.nav_icon_task_removal
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_SMART_FREEZE,
                iconRes = R.drawable.ic_nav_smart_freeze,
                titleRes = R.string.feature_title_smart_app_freeze,
                requiredFeature = BuildProp.THANOX_FEATURE_EXT_APP_SMART_FREEZE,
                themeColor = R.color.nav_icon_smart_freeze,
                menuItems = listOf(
                    R.string.nav_title_settings to {
                        SmartFreezeSettingsActivity.start(it)
                    }
                )
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_APPS_MANAGER,
                iconRes = R.drawable.ic_nav_app_manager,
                titleRes = R.string.feature_title_apps_manager,
                requiredFeature = BuildProp.THANOX_FEATURE_COMPONENT_MANAGER,
                themeColor = R.color.nav_icon_apps_manager,
                menuItems = listOf(
                    R.string.feature_title_apps_manager to {
                        AppsManageActivity.start(it)
                    },
                    R.string.title_package_sets to {
                        PackageSetListActivity.start(it)
                    },
                )
            )
        )
    )

    private val secure = FeatureItemGroup(
        titleRes = R.string.nav_title_secure,
        items = listOf(
            FeatureItem(
                id = PrebuiltFeatureIds.ID_PRIVACY_CHEAT,
                iconRes = R.drawable.ic_nav_priv_cheat,
                titleRes = R.string.feature_title_data_cheat,
                requiredFeature = BuildProp.THANOX_FEATURE_PRIVACY_DATA_CHEAT,
                themeColor = R.color.nav_icon_priv_cheat,
                menuItems = listOf(
                    R.string.priv_title_fields_template to {
                        FieldsTemplateListActivity.start(it, 10086)
                    },
                    R.string.privacy_record to {
                        withSubscriptionStatus(it) { isSubscribed: Boolean ->
                            if (isSubscribed) {
                                CheatRecordViewerActivity.start(it)
                            } else {
                                showDonateIntroDialog(it)
                            }
                        }
                    }
                )
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_OPS_BY_OPS,
                iconRes = R.drawable.ic_nav_ops,
                titleRes = R.string.module_ops_feature_title_ops_app_list,
                requiredFeature = BuildProp.THANOX_FEATURE_PRIVACY_OPS,
                themeColor = R.color.nav_icon_ops
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_APP_LOCK,
                iconRes = R.drawable.ic_nav_app_lock,
                titleRes = R.string.feature_title_app_lock,
                requiredFeature = BuildProp.THANOX_FEATURE_PRIVACY_APPLOCK,
                themeColor = R.color.nav_icon_app_lock,
                menuItems = listOf(
                    R.string.module_locker_title_settings to {
                        LockSettingsActivity.start(it)
                    }
                )
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_TASK_BLUR,
                iconRes = R.drawable.ic_nav_task_blur,
                titleRes = R.string.feature_title_recent_task_blur,
                requiredFeature = BuildProp.THANOX_FEATURE_PRIVACY_TASK_BLUR,
                themeColor = R.color.nav_icon_task_blur
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_OP_REMIND,
                iconRes = R.drawable.ic_nav_op_remind,
                titleRes = R.string.module_ops_feature_title_ops_remind_list,
                requiredFeature = BuildProp.THANOX_FEATURE_PRIVACY_OPS_REMINDER,
                themeColor = R.color.nav_icon_op_remind
            )
        )
    )

    private val ext = FeatureItemGroup(
        titleRes = R.string.nav_title_exp,
        items = listOf(
            FeatureItem(
                id = PrebuiltFeatureIds.ID_TRAMPOLINE,
                iconRes = R.drawable.ic_nav_activity_replacement,
                titleRes = R.string.module_activity_trampoline_app_name_abbr,
                requiredFeature = BuildProp.THANOX_FEATURE_APP_TRAMPOLINE,
                themeColor = R.color.nav_icon_activity_replacement
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_PROFILE,
                iconRes = R.drawable.ic_nav_profile,
                titleRes = R.string.module_profile_feature_name,
                requiredFeature = BuildProp.THANOX_FEATURE_PROFILE,
                themeColor = R.color.nav_icon_profile,
                menuItems = listOf(
                    R.string.module_profile_rule_wiki to {
                        BrowserUtils.launch(it, BuildProp.THANOX_URL_DOCS_PROFILE)
                    },
                    R.string.module_profile_rule_impor_example to {
                        ProfileExampleActivity.Starter.start(it)
                    },
                    R.string.module_profile_rule_online to { activity ->
                        withSubscriptionStatus(activity) {
                            if (it) {
                                OnlineProfileActivity.Starter.start(activity)
                            } else {
                                showDonateIntroDialog(activity)
                            }
                        }
                    },
                    R.string.module_profile_title_global_var to {
                        GlobalVarListActivity.start(it)
                    },
                    R.string.module_profile_title_rule_engine to {
                        RuleEngineSettingsActivity.start(it)
                    },
                    R.string.module_profile_title_action_console to { activity ->
                        withSubscriptionStatus(activity) {
                            if (it) {
                                ConsoleActivity.Starter.start(activity)
                            } else {
                                showDonateIntroDialog(activity)
                            }
                        }
                    },
                    R.string.module_profile_title_log to {
                        LogActivity.Starter.start(it)
                    },
                )
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_SMART_STANDBY,
                iconRes = R.drawable.ic_nav_smart_standby,
                titleRes = R.string.feature_title_smart_app_standby,
                requiredFeature = BuildProp.THANOX_FEATURE_APP_SMART_STAND_BY,
                themeColor = R.color.nav_icon_smart_standby,
                menuItems = listOf(
                    R.string.menu_title_rules to {
                        StandByRuleActivity.start(it)
                    },
                    R.string.nav_title_settings to {
                        SmartStandbySettingsActivity.start(it)
                    },
                )
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_WAKELOCK_REMOVER,
                iconRes = R.drawable.ic_nav_wakelock_remover,
                titleRes = R.string.feature_title_wakelock_blocker,
                requiredFeature = BuildProp.THANOX_FEATURE_WAKELOCK_REMOVER,
                themeColor = R.color.nav_icon_wakelock_remover
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_INFINITE_Z,
                iconRes = R.drawable.ic_nav_app_clone,
                titleRes = R.string.feature_title_infinite_z,
                requiredFeature = BuildProp.THANOX_FEATURE_IZ,
                themeColor = R.color.nav_icon_app_clone
            )
        )
    )

    private val notification = FeatureItemGroup(
        titleRes = R.string.module_notification_recorder_feature_title_notification_center,
        items = listOf(
            FeatureItem(
                id = PrebuiltFeatureIds.ID_SCREEN_ON_NOTIFICATION,
                iconRes = R.drawable.ic_nav_screen_on_notification,
                titleRes = R.string.feature_title_light_on_notification,
                requiredFeature = BuildProp.THANOX_FEATURE_EXT_N_UP,
                themeColor = R.color.nav_icon_screen_on_notification
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_NOTIFICATION_RECORDER,
                iconRes = R.drawable.ic_nav_nr,
                titleRes = R.string.module_notification_recorder_feature_title_notification_recorder,
                requiredFeature = BuildProp.THANOX_FEATURE_EXT_N_RECORDER,
                themeColor = R.color.nav_icon_nr,
                menuItems = listOf(
                    R.string.module_notification_recorder_stats to {
                        StatsActivity.Starter.start(it)
                    },
                    R.string.module_notification_recorder_settings to {
                        NotificationRecordSettingsActivity.start(it)
                    }
                )
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_WECHAT_PUSH,
                iconRes = R.drawable.ic_nav_wechat_push,
                titleRes = R.string.module_push_message_delegate_title_wechat_proxy,
                requiredFeature = BuildProp.THANOX_FEATURE_PUSH_DELEGATE,
                themeColor = R.color.nav_icon_wechat_push
            )
        )
    )

    private val guide = FeatureItemGroup(
        titleRes = R.string.nav_title_guide,
        items = listOf(
            FeatureItem(
                id = PrebuiltFeatureIds.ID_FEEDBACK,
                iconRes = R.drawable.ic_nav_feedback,
                titleRes = R.string.nav_title_feedback,
                themeColor = R.color.nav_icon_feedback
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_GUIDE,
                iconRes = R.drawable.ic_nav_guide,
                titleRes = R.string.common_menu_title_wiki,
                themeColor = R.color.nav_icon_guide
            )
        )
    )


    fun all(filter: (FeatureItem) -> Boolean = { true }): List<FeatureItemGroup> {
        return listOf(
            boost, secure, ext, notification, guide
        ).map {
            it.copy(items = it.items.filter(filter))
        }
    }
}