package github.tornaco.android.thanos.core

import android.content.Context

object T {

    @JvmStatic
    fun serviceInstallName(): String {
        return Context.TV_INPUT_SERVICE
    }

    @JvmStatic
    fun serviceContextName(): String {
        return "thanos"
    }

    object Actions {
        const val ACTION_FRONT_PKG_CHANGED = "thanox.a.front_pkg.changed"

        const val ACTION_FRONT_ACTIVITY_CHANGED = "thanox.a.front_activity.changed"

        const val ACTION_FRONT_PKG_CHANGED_EXTRA_PACKAGE_FROM =
            "thanox.a.extra.front_activity.changed.pkg.from"
        const val ACTION_FRONT_PKG_CHANGED_EXTRA_PACKAGE_TO =
            "thanox.a.extra.front_activity.changed.pkg.to"
        const val ACTION_FRONT_PKG_CHANGED_EXTRA_PACKAGE_FROM_USER_ID =
            "thanox.a.extra.front_activity.changed.pkg.from.userId"
        const val ACTION_FRONT_PKG_CHANGED_EXTRA_PACKAGE_TO_USER_ID =
            "thanox.a.extra.front_activity.changed.pkg.to.userId"

        const val ACTION_ACTIVITY_RESUMED = "thanox.a.activity.resumed"
        const val ACTION_ACTIVITY_RESUMED_EXTRA_COMPONENT_NAME =
            "thanox.a.activity.resumed.extra.name"
        const val ACTION_ACTIVITY_RESUMED_EXTRA_PACKAGE_NAME = "thanox.a.activity.resumed.extra.pkg"
        const val ACTION_ACTIVITY_RESUMED_EXTRA_USER_ID = "thanox.a.activity.resumed.extra.userId"

        const val ACTION_ACTIVITY_CREATED = "thanox.a.activity.created"
        const val ACTION_ACTIVITY_CREATED_EXTRA_COMPONENT_NAME =
            "thanox.a.activity.created.extra.name"
        const val ACTION_ACTIVITY_CREATED_EXTRA_PACKAGE_NAME = "thanox.a.activity.created.extra.pkg"

        const val ACTION_PACKAGE_STOPPED = "thanox.a.package.stopped"
        const val ACTION_PACKAGE_STOPPED_EXTRA_PACKAGE_NAME = "thanox.a.package.stopped.extra.pkg"
        const val ACTION_PACKAGE_STOPPED_EXTRA_PACKAGE_UID = "thanox.a.package.stopped.extra.uid"

        const val ACTION_TASK_REMOVED = "thanox.a.task.removed"
        const val ACTION_TASK_REMOVED_EXTRA_PACKAGE_NAME = "thanox.a.task.removed.pkg"
        const val ACTION_TASK_REMOVED_EXTRA_USER_ID = "thanox.a.task.removed.pkg.user.id"

        const val ACTION_RUNNING_PROCESS_VIEWER = "thanox.a.running_process.viewer"
        const val ACTION_RUNNING_PROCESS_CLEAR = "thanox.a.running_process.clear"

        const val ACTION_RESTART_DEVICE = "thanox.a.device.restart"

        const val ACTION_LOCKER_VERIFY_ACTION =
            "github.tornaco.practice.honeycomb.locker.action.VERIFY"
        const val ACTION_LOCKER_VERIFY_EXTRA_PACKAGE = "pkg"
        const val ACTION_LOCKER_VERIFY_EXTRA_REQUEST_CODE = "request_code"

        const val ACTION_LAUNCH_OTHER_APP =
            "github.tornaco.action.launcher.other.app"
        const val ACTION_LAUNCH_OTHER_APP_DENY =
            "github.tornaco.action.launcher.other.app.deny"
        const val ACTION_LAUNCH_OTHER_APP_EXTRA_CALLER_PKG =
            "github.tornaco.action.launcher.other.app.caller"
        const val ACTION_LAUNCH_OTHER_APP_EXTRA_TARGET_PKG =
            "github.tornaco.action.launcher.other.app.target"
        const val ACTION_LAUNCH_OTHER_APP_EXTRA_IS_FROM_CHECK =
            "github.tornaco.action.launcher.other.app.is.from.check"

        const val ACTION_GET_PATCH_SOURCES_PREFIX = "thanox.a.get.patch.sources."
    }

    object Tags {
        const val N_TAG_BG_RESTRICT_APPS_CHANGED = "thanox.n.tag.bg.restrict.apps.changed"
        const val N_TAG_PKG_PRIVACY_DATA_CHEATING = "thanox.n.tag.privacy.pkg.cheating"
        const val N_TAG_THANOX_ACTIVATED = "thanox.n.tag.thanox.core.activated"
    }
}
