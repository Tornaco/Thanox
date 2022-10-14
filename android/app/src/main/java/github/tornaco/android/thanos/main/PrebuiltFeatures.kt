package github.tornaco.android.thanos.main

import github.tornaco.android.thanos.BuildProp
import github.tornaco.android.thanos.R

object PrebuiltFeatures {
    private val boost = FeatureItemGroup(
        titleRes = R.string.nav_title_boost,
        items = listOf(
            FeatureItem(
                id = PrebuiltFeatureIds.ID_ONE_KEY_CLEAR,
                iconRes = R.drawable.ic_nav_boost,
                titleRes = R.string.feature_title_one_key_boost,
                requiredFeature = BuildProp.THANOX_FEATURE_BG_TASK_CLEAN
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_BACKGROUND_START,
                iconRes = R.drawable.ic_nav_bg_start,
                titleRes = R.string.feature_title_start_restrict,
                requiredFeature = BuildProp.THANOX_FEATURE_START_BLOCKER
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_BACKGROUND_RESTRICT,
                iconRes = R.drawable.ic_nav_bg_restrict,
                titleRes = R.string.feature_title_bg_restrict,
                requiredFeature = BuildProp.THANOX_FEATURE_BG_TASK_CLEAN
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_CLEAN_TASK_REMOVAL,
                iconRes = R.drawable.ic_nav_task_removal,
                titleRes = R.string.feature_title_clean_when_task_removed,
                requiredFeature = BuildProp.THANOX_FEATURE_RECENT_TASK_REMOVAL
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_SMART_FREEZE,
                iconRes = R.drawable.ic_nav_smart_freeze,
                titleRes = R.string.feature_title_smart_app_freeze,
                requiredFeature = BuildProp.THANOX_FEATURE_EXT_APP_SMART_FREEZE
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_APPS_MANAGER,
                iconRes = R.drawable.ic_nav_app_manager,
                titleRes = R.string.feature_title_apps_manager,
                requiredFeature = BuildProp.THANOX_FEATURE_COMPONENT_MANAGER
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
                requiredFeature = BuildProp.THANOX_FEATURE_PRIVACY_DATA_CHEAT
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_OPS_BY_OPS,
                iconRes = R.drawable.ic_nav_ops,
                titleRes = R.string.module_ops_feature_title_ops_app_list,
                requiredFeature = BuildProp.THANOX_FEATURE_PRIVACY_OPS
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_APP_LOCK,
                iconRes = R.drawable.ic_nav_app_lock,
                titleRes = R.string.feature_title_app_lock,
                requiredFeature = BuildProp.THANOX_FEATURE_PRIVACY_APPLOCK
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_TASK_BLUR,
                iconRes = R.drawable.ic_nav_task_blur,
                titleRes = R.string.feature_title_recent_task_blur,
                requiredFeature = BuildProp.THANOX_FEATURE_PRIVACY_TASK_BLUR
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_OP_REMIND,
                iconRes = R.drawable.ic_nav_op_remind,
                titleRes = R.string.module_ops_feature_title_ops_remind_list,
                requiredFeature = BuildProp.THANOX_FEATURE_PRIVACY_OPS_REMINDER
            )
        )
    )

    private val ext = FeatureItemGroup(
        titleRes = R.string.nav_title_exp,
        items = listOf(
            FeatureItem(
                id = PrebuiltFeatureIds.ID_TRAMPOLINE,
                iconRes = R.drawable.ic_nav_activity_replacement,
                titleRes = R.string.module_activity_trampoline_app_name,
                requiredFeature = BuildProp.THANOX_FEATURE_APP_TRAMPOLINE
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_PROFILE,
                iconRes = R.drawable.ic_nav_profile,
                titleRes = R.string.module_profile_feature_name,
                requiredFeature = BuildProp.THANOX_FEATURE_PROFILE
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_SMART_STANDBY,
                iconRes = R.drawable.ic_nav_smart_standby,
                titleRes = R.string.feature_title_smart_app_standby,
                requiredFeature = BuildProp.THANOX_FEATURE_APP_SMART_STAND_BY
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_WAKELOCK_REMOVER,
                iconRes = R.drawable.ic_nav_wakelock_remover,
                titleRes = R.string.feature_title_wakelock_blocker,
                requiredFeature = BuildProp.THANOX_FEATURE_WAKELOCK_REMOVER
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_INFINITE_Z,
                iconRes = R.drawable.ic_nav_app_clone,
                titleRes = R.string.feature_title_infinite_z,
                requiredFeature = BuildProp.THANOX_FEATURE_IZ
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_PLUGINS,
                iconRes = R.drawable.ic_nav_plugins,
                titleRes = R.string.nav_title_plugin,
                requiredFeature = BuildProp.THANOX_FEATURE_PLUGIN_SUPPORT
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
                requiredFeature = BuildProp.THANOX_FEATURE_EXT_N_UP
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_NOTIFICATION_RECORDER,
                iconRes = R.drawable.ic_nav_nr,
                titleRes = R.string.module_notification_recorder_feature_title_notification_recorder,
                requiredFeature = BuildProp.THANOX_FEATURE_EXT_N_RECORDER
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_WECHAT_PUSH,
                iconRes = R.drawable.ic_nav_wechat_push,
                titleRes = R.string.module_push_message_delegate_title_wechat_proxy,
                requiredFeature = BuildProp.THANOX_FEATURE_PUSH_DELEGATE
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
            ),
            FeatureItem(
                id = PrebuiltFeatureIds.ID_GUIDE,
                iconRes = R.drawable.ic_nav_guide,
                titleRes = R.string.common_menu_title_wiki,
            )
        )
    )


    fun all(): List<FeatureItemGroup> {
        return listOf(
            boost, secure, ext, notification, guide
        )
    }
}