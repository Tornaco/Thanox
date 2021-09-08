package github.tornaco.android.thanos.core

import android.content.Context
import android.os.Environment
import android.os.UserHandle
import com.elvishew.xlog.XLog
import com.google.common.io.Files
import github.tornaco.android.thanos.BuildProp
import github.tornaco.android.thanos.core.app.activity.ActivityStackSupervisor
import java.io.File

object T {

    @JvmStatic
    fun baseServerDir(): File {
        val systemFile = File(Environment.getDataDirectory(), "system")
        return File(systemFile, "thanos")
    }

    @JvmStatic
    fun baseServerSecureDataDir(): File {
        return File(baseServerDir(), "sec_data/u/" + UserHandle.USER_SYSTEM)
    }

    @JvmStatic
    fun baseServerDataDir(): File {
        return File(baseServerDir(), "data/u/" + UserHandle.USER_SYSTEM)
    }

    @JvmStatic
    fun baseProfileUserIoDir(): File {
        return File(baseServerDir(), "profile_user_io")
    }

    @JvmStatic
    fun baseServerTmpDir(): File {
        return File(baseServerDir(), "tmp")
    }

    @JvmStatic
    fun baseLoggingDir(): File {
        return File(baseServerDir(), "logging")
    }

    @JvmStatic
    fun baseServerLoggingDir(): File {
        return File(baseLoggingDir(), "server_logging")
    }

    @JvmStatic
    fun basePatchLoggingDir(): File {
        return File(baseLoggingDir(), "patches_logging")
    }

    @JvmStatic
    fun baseApplicationLoggingDir(): File {
        return File(baseLoggingDir(), "application_logging")
    }

    @JvmStatic
    fun baseDatabaseDir(): File {
        return File(baseServerSecureDataDir(), "db")
    }

    @JvmStatic
    fun pluginDir(): File {
        return File(baseServerDataDir(), "plugins")
    }

    @JvmStatic
    fun profileRulesDir(): File {
        return File(baseServerDataDir(), "rules")
    }

    @JvmStatic
    fun configTemplatesDir(): File {
        return File(baseServerDataDir(), "config_templates")
    }

    @JvmStatic
    fun profileEnabledRulesRepoFile(): File {
        return File(profileRulesDir(), "enabled_rules.xml")
    }

    @JvmStatic
    fun globalRuleVarsRepoFile(): File {
        return File(profileRulesDir(), "global_rule_vars.xml")
    }

    @JvmStatic
    fun startBlockerRepoFile(): File {
        return File(baseServerDataDir(), "start_blocking_pkgs.xml")
    }

    @JvmStatic
    fun bgRestrictRepoFile(): File {
        return File(baseServerDataDir(), "bg_restrict_pkgs.xml")
    }

    @JvmStatic
    fun cleanUpOnTaskRemovalRepoFile(): File {
        return File(baseServerDataDir(), "clean_up_on_task_removed_pkgs.xml")
    }

    @JvmStatic
    fun recentTaskBlurRepoFile(): File {
        return File(baseServerDataDir(), "recent_task_blur_pkgs.xml")
    }

    @JvmStatic
    fun smartStandByRepoFile(): File {
        return File(baseServerDataDir(), "smart_stand_by_pkgs.xml")
    }

    @JvmStatic
    fun startRulesRepoFile(): File {
        return File(baseServerDataDir(), "start_rules.xml")
    }

    @JvmStatic
    fun standbyRulesRepoFile(): File {
        return File(baseServerDataDir(), "standby_rules.xml")
    }

    @JvmStatic
    fun smartFreezeByRepoFile(): File {
        return File(baseServerDataDir(), "smart_freeze_pkgs.xml")
    }

    @JvmStatic
    fun blockUninstallRepoFile(): File {
        return File(baseServerDataDir(), "block_uninstall_pkgs.xml")
    }

    @JvmStatic
    fun blockClearDataRepoFile(): File {
        return File(baseServerDataDir(), "block_clear_data_pkgs.xml")
    }

    @JvmStatic
    fun recentTaskExcludingSettingsRepoFile(): File {
        return File(baseServerDataDir(), "recent_task_excluding_settings.xml")
    }

    @JvmStatic
    fun appLockRepoFile(): File {
        return File(baseServerDataDir(), "app_lock_pkgs.xml")
    }

    @JvmStatic
    fun pluginsConfFile(): File {
        return File(pluginDir(), "plugins_conf.xml")
    }

    @JvmStatic
    fun componentReplacementRepoFile(): File {
        return File(baseServerDataDir(), "component_replacements.xml")
    }

    @JvmStatic
    fun appOpsRepoFile(): File {
        return File(baseServerDataDir(), "app_ops.xml")
    }

    @JvmStatic
    fun pushChannelsFile(): File {
        return File(baseServerDataDir(), "push_channels.xml")
    }

    @JvmStatic
    fun privacyFieldsDir(): File {
        return File(baseServerDataDir(), "priv_fields")
    }

    @JvmStatic
    fun privacyPkgFieldsFile(): File {
        return File(baseServerDataDir(), "priv_pkg_fields.xml")
    }

    @JvmStatic
    fun opRemindOpsFile(): File {
        return File(baseServerDataDir(), "reminding_ops.xml")
    }

    @JvmStatic
    fun opRemindPkgFile(): File {
        return File(baseServerDataDir(), "op_reminding_pkgs.xml")
    }

    @JvmStatic
    fun opTemplateFile(): File {
        return File(baseServerDataDir(), "op_template.xml")
    }

    @JvmStatic
    fun opSettingsFile(): File {
        return File(baseServerDataDir(), "op_settings.xml")
    }

    @JvmStatic
    fun screenOnNotificationPkgsFile(): File {
        return File(baseServerDataDir(), "screen_on_notification_pkgs.xml")
    }

    @JvmStatic
    fun disabledComponentsRepoFile(): File {
        return File(baseServerDataDir(), "disabled_components.xml")
    }

    @JvmStatic
    fun packageSetsFile(): File {
        return File(baseServerDataDir(), "pkg_sets.xml")
    }

    @JvmStatic
    fun serviceInstallName(): String {
        return Context.TV_INPUT_SERVICE
    }

    @JvmStatic
    fun serviceContextName(): String {
        return "thanos"
    }

    @JvmStatic
    fun serviceSilenceNotificationChannel(): String {
        return "dev.tornaco.notification.channel.id.Thanos-DEFAULT"
    }

    @JvmStatic
    fun serviceImportanrNotificationChannel(): String {
        return "dev.tornaco.notification.channel.id.Thanos-IMPORTANT"
    }

    @JvmStatic
    fun createShitDeviceMarker() {
        try {
            @Suppress("UnstableApiUsage")
            Files.touch(File(baseServerDataDir(), "sdm"))
        } catch (e: Throwable) {
            XLog.e("Error create sdm", e)
        }
    }

    @JvmStatic
    fun hasShitDeviceMarker(): Boolean {
        try {
            @Suppress("UnstableApiUsage")
            return File(baseServerDataDir(), "sdm").exists()
        } catch (e: Throwable) {
            return false
        }
    }

    @JvmStatic
    fun hasTVFlags(): Boolean {
        try {
            @Suppress("UnstableApiUsage")
            return File(baseServerDataDir(), "tv").exists()
        } catch (e: Throwable) {
            return false
        }
    }

    object Settings {
        // Server Settings.
        @JvmField
        val PREF_BY_PASS_SYSTEM_APPS_ENABLED =
                ThanosFeature("PREF_BY_PASS_SYSTEM_APPS_ENABLED", true)

        @JvmField
        val PREF_START_BLOCKER_ENABLED = ThanosFeature("PREF_START_BLOCKER_ENABLED", false)

        @JvmField
        val PREF_START_RULE_ENABLED = ThanosFeature("PREF_START_RULE_ENABLED", false)

        @JvmField
        val PREF_BG_RESTRICT_ENABLED = ThanosFeature("PREF_BG_RESTRICT_ENABLED", false)

        @JvmField
        val PREF_BG_TASK_CLEAN_UP_SKIP_AUDIO_FOCUSED =
                ThanosFeature("PREF_BG_TASK_CLEAN_UP_SKIP_AUDIO_FOCUSED", false)

        @JvmField
        val PREF_BG_TASK_CLEAN_UP_SKIP_NOTIFICATION =
                ThanosFeature("PREF_BG_TASK_CLEAN_UP_SKIP_NOTIFICATION", false)

        @JvmField
        val PREF_BG_TASK_CLEAN_UP_DELAY_MILLS =
                ThanosFeature("PREF_BG_TASK_CLEAN_UP_DELAY_MILLS", 0L /*Noop*/)

        @JvmField
        val PREF_BG_TASK_CLEAN_UP_SKIP_WHEN_HAS_RECENT_TASK =
                ThanosFeature("PREF_BG_TASK_CLEAN_UP_SKIP_WHEN_HAS_RECENT_TASK", false)

        @JvmField
        val PREF_CLEAN_UP_ON_TASK_REMOVED = ThanosFeature("PREF_CLEAN_UP_ON_TASK_REMOVED", false)

        @JvmField
        val PREF_SHOW_BG_RESTRICT_APPS_NOTIFICATION_ENABLED =
                ThanosFeature("PREF_SHOW_BG_RESTRICT_APPS_NOTIFICATION_ENABLED", false)

        @JvmField
        val PREF_RECENT_TASK_BLUR_ENABLED =
                ThanosFeature("PREF_RECENT_TASK_BLUR_ENABLED", false)

        @JvmField
        @Deprecated("Use v2 instead.")
        val PREF_SMART_STANDBY_ENABLED =
                ThanosFeature("PREF_SMART_STANDBY_ENABLED", false)

        @JvmField
        val PREF_SMART_STANDBY_V2_ENABLED =
                ThanosFeature("PREF_SMART_STANDBY_V2_ENABLED", false)

        @JvmField
        val PREF_SMART_STANDBY_STOP_SERVICE_ENABLED =
                ThanosFeature("PREF_SMART_STANDBY_STOP_SERVICE_ENABLED", false)

        @JvmField
        val PREF_SMART_STANDBY_INACTIVE_ENABLED =
                ThanosFeature("PREF_SMART_STANDBY_INACTIVE_ENABLED", true)

        @JvmField
        val PREF_SMART_STANDBY_BY_PASS_IF_HAS_N_ENABLED =
                ThanosFeature("PREF_SMART_STANDBY_BY_PASS_IF_HAS_N_ENABLED", false)

        @JvmField
        val PREF_SMART_STANDBY_BY_BLOCK_BG_SERVICE_START_ENABLED =
                ThanosFeature("PREF_SMART_STANDBY_BY_BLOCK_BG_SERVICE_START_ENABLED", false)

        @JvmField
        val PREF_SMART_STANDBY_RULE_ENABLED =
                ThanosFeature("PREF_SMART_STANDBY_RULE_ENABLED", false)

        @JvmField
        val PREF_NET_STAT_TRACKER_ENABLED =
                ThanosFeature("PREF_NET_STAT_TRACKER_ENABLED", false)

        // No toggle.
        @JvmField
        val PREF_SMART_FREEZE_ENABLED =
                ThanosFeature("PREF_SMART_FREEZE_ENABLED", true)

        @JvmField
        val PREF_SMART_FREEZE_SCREEN_OFF_CHECK_ENABLED =
                ThanosFeature("PREF_SMART_FREEZE_SCREEN_OFF_CHECK_ENABLED", false)

        @JvmField
        val PREF_SMART_FREEZE_HIDE_PACKAGE_CHANGE_EVENT_ENABLED =
                ThanosFeature("PREF_SMART_FREEZE_HIDE_PACKAGE_CHANGE_EVENT_ENABLED", false)

        @JvmField
        val PREF_SMART_FREEZE_SCREEN_OFF_CHECK_DELAY_MILLS =
                ThanosFeature("PREF_SMART_FREEZE_SCREEN_OFF_CHECK_DELAY_MILLS", 0L /*Noop*/)

        @JvmField
        val PREF_ACTIVITY_TRAMPOLINE_ENABLED =
                ThanosFeature("PREF_ACTIVITY_TRAMPOLINE_ENABLED", false)

        @JvmField
        val PREF_SHOW_CURRENT_ACTIVITY_COMPONENT_ENABLED =
                ThanosFeature("PREF_SHOW_CURRENT_ACTIVITY_COMPONENT_ENABLED", false)

        @JvmField
        val PREF_APP_LOCK_ENABLED = ThanosFeature("PREF_APP_LOCK_ENABLED", false)

        @JvmField
        val PREF_APP_LOCK_VERIFY_ON_SCREEN_OFF =
                ThanosFeature("PREF_APP_LOCK_VERIFY_ON_SCREEN_OFF", true)

        @JvmField
        val PREF_APP_LOCK_VERIFY_ON_APP_SWITCH =
                ThanosFeature("PREF_APP_LOCK_VERIFY_ON_APP_SWITCH", false)

        @JvmField
        val PREF_APP_LOCK_VERIFY_ON_TASK_REMOVED =
                ThanosFeature("PREF_APP_LOCK_VERIFY_ON_TASK_REMOVED", true)

        @JvmField
        val PREF_APP_LOCK_METHOD =
                ThanosFeature("PREF_APP_LOCK_METHOD", ActivityStackSupervisor.LockerMethod.NONE)

        @JvmField
        val PREF_APP_LOCK_FP_ENABLED = ThanosFeature("PREF_APP_LOCK_FP_ENABLED", false)

        @JvmField
        val PREF_APP_LOCK_WORKAROUND_ENABLED =
                ThanosFeature("PREF_APP_LOCK_WORKAROUND_ENABLED", false)

        @JvmField
        val PREF_APP_LOCK_KEY_PREFIX_ =
                ThanosFeature("PREF_APP_LOCK_KEY_PREFIX_", "PREF_APP_LOCK_KEY_PREFIX_")

        @JvmField
        val PREF_APP_LOCK_WORKAROUND_DELAY = ThanosFeature("PREF_APP_LOCK_WORKAROUND_DELAY", 500L)

        @JvmField
        val PREF_PRIVACY_ENABLED = ThanosFeature("PREF_PRIVACY_ENABLED", false)

        @JvmField
        val PREF_SCREEN_ON_NOTIFICATION_ENABLED =
                ThanosFeature("PREF_SCREEN_ON_NOTIFICATION_ENABLED", false)

        @JvmField
        val PREF_PERSIST_ON_NEW_NOTIFICATION_ENABLED =
                ThanosFeature("PREF_PERSIST_ON_NEW_NOTIFICATION_ENABLED", false)

        @JvmField
        val PREF_SHOW_TOAST_APP_INFO_ENABLED =
            ThanosFeature("PREF_SHOW_TOAST_APP_INFO_ENABLED", false)
        @JvmField
        val PREF_NR_N_ENABLED =
            ThanosFeature("PREF_NR_N_ENABLED", true)
        @JvmField
        val PREF_NR_T_ENABLED =
            ThanosFeature("PREF_NR_T_ENABLED", true)

        @JvmField
        val PREF_LOGGING_ENABLED = ThanosFeature("PREF_LOGGING_ENABLED", true)

        @JvmField
        val PREF_AUTO_CONFIG_NEW_INSTALLED_APPS_ENABLED =
                ThanosFeature("PREF_AUTO_CONFIG_NEW_INSTALLED_APPS_ENABLED", false)

        @JvmField
        val PREF_PROFILE_AUTO_CONFIG_TEMPLATE_SELECTION_ID =
                ThanosFeature("PREF_PROFILE_AUTO_CONFIG_TEMPLATE_SELECTION_ID", null)

        @JvmField
        val PREF_PROFILE_ENABLED =
                ThanosFeature("PREF_PROFILE_ENABLED", false)

        @JvmField
        val PREF_PROFILE_SU_ENABLED =
                ThanosFeature("PREF_PROFILE_SU_ENABLED", false)

        @JvmField
        @Deprecated("Not supported any more.")
        val PREF_PROFILE_ENGINE_UI_AUTOMATION_ENABLED =
                ThanosFeature("PREF_PROFILE_ENGINE_UI_AUTOMATION_ENABLED", false)

        @JvmField
        val PREF_PROFILE_ENGINE_PUSH_ENABLED =
                ThanosFeature("PREF_PROFILE_ENGINE_PUSH_ENABLED", false)

        @JvmField
        val PREF_OPS_ENABLED = ThanosFeature("PREF_OPS_ENABLED", false)

        @JvmField
        val PREF_OPS_HAS_MIGRATE_TO_ANDROID_S = ThanosFeature("PREF_OPS_HAS_MIGRATE_TO_ANDROID_S", false)

        @JvmField
        val PREF_FIRST_ACTIVATE =
                ThanosFeature("PREF_FIRST_ACTIVATE_" + BuildProp.THANOS_BUILD_FINGERPRINT, true)

        @JvmField
        val PREF_PROTECTED_WHITE_LIST_ENABLED =
                ThanosFeature("PREF_PROTECTED_WHITE_LIST_ENABLED", true)

        @JvmField
        val PREF_POWER_SAVE_ENABLED =
                ThanosFeature("PREF_POWER_SAVE_ENABLED", false)

        @JvmField
        val PREF_GEN_DID =
            ThanosFeature("PREF_GEN_DID", "")
    }

    object Actions {
        const val ACTION_FRONT_PKG_CHANGED = "thanox.a.front_pkg.changed"
        const val ACTION_FRONT_ACTIVITY_CHANGED = "thanox.a.front_activity.changed"
        const val ACTION_FRONT_PKG_CHANGED_EXTRA_PACKAGE_FROM =
                "thanox.a.extra.front_activity.changed.pkg.from"
        const val ACTION_FRONT_PKG_CHANGED_EXTRA_PACKAGE_TO =
                "thanox.a.extra.front_activity.changed.pkg.to"

        const val ACTION_ACTIVITY_RESUMED = "thanox.a.activity.resumed"
        const val ACTION_ACTIVITY_RESUMED_EXTRA_COMPONENT_NAME =
                "thanox.a.activity.resumed.extra.name"
        const val ACTION_ACTIVITY_RESUMED_EXTRA_PACKAGE_NAME = "thanox.a.activity.resumed.extra.pkg"

        const val ACTION_ACTIVITY_CREATED = "thanox.a.activity.created"
        const val ACTION_ACTIVITY_CREATED_EXTRA_COMPONENT_NAME =
                "thanox.a.activity.created.extra.name"
        const val ACTION_ACTIVITY_CREATED_EXTRA_PACKAGE_NAME = "thanox.a.activity.created.extra.pkg"

        const val ACTION_PACKAGE_STOPPED = "thanox.a.package.stopped"
        const val ACTION_PACKAGE_STOPPED_EXTRA_PACKAGE_NAME = "thanox.a.package.stopped.extra.pkg"

        const val ACTION_TASK_REMOVED = "thanox.a.task.removed"
        const val ACTION_TASK_REMOVED_EXTRA_PACKAGE_NAME = "thanox.a.task.removed.pkg"
        const val ACTION_TASK_REMOVED_EXTRA_USER_ID = "thanox.a.task.removed.pkg.user.id"

        const val ACTION_RUNNING_PROCESS_VIEWER = "thanox.a.running_process.viewer"
        const val ACTION_RUNNING_PROCESS_CLEAR = "thanox.a.running_process.clear"

        const val ACTION_RESTART_DEVICE = "thanox.a.device.restart"

        const val ACTION_LOCKER_VERIFY_ACTION =
                "github.tornaco.practice.honeycomb.locker.action.VERIFY"
        const val ACTION_LOCKER_VERIFY_EXTRA_PACKAGE = "pkg"
        const val ACTION_LOCKER_VERIFY_EXTRA_METHOD = "method"
        const val ACTION_LOCKER_VERIFY_EXTRA_REQUEST_CODE = "request_code"
    }

    object Tags {
        const val N_TAG_BG_RESTRICT_APPS_CHANGED = "thanox.n.tag.bg.restrict.apps.changed"
        const val N_TAG_PKG_PRIVACY_DATA_CHEATING = "thanox.n.tag.privacy.pkg.cheating"
        const val N_TAG_THANOX_ACTIVATED = "thanox.n.tag.thanox.core.activated"
    }
}
