package github.tornaco.android.thanos.core.os

import github.tornaco.android.thanos.core.os.ClassNames.ACTIVITY_RECORD_AM
import github.tornaco.android.thanos.core.os.ClassNames.ACTIVITY_RECORD_WM
import github.tornaco.android.thanos.core.os.ClassNames.AMS
import github.tornaco.android.thanos.core.os.ClassNames.AMS_LIFECYCLE
import github.tornaco.android.thanos.core.os.ClassNames.ATMS
import github.tornaco.android.thanos.core.os.ClassNames.CLIPBOARD_SERVICE
import github.tornaco.android.thanos.core.os.ClassNames.MEDIA_DOCUMENTS_PROVIDER
import github.tornaco.android.thanos.core.os.ClassNames.MEDIA_PROVIDER
import github.tornaco.android.thanos.core.os.ClassNames.NFC_SERVICE_HANDLER
import github.tornaco.android.thanos.core.os.ClassNames.NOTIFICATION_USAGE_STATS_SERVICE
import github.tornaco.android.thanos.core.os.ClassNames.PHONE_WINDOW_MANAGER
import github.tornaco.android.thanos.core.os.ClassNames.PROCESS_LIST
import github.tornaco.android.thanos.core.os.ClassNames.PROCESS_RECORD
import github.tornaco.android.thanos.core.os.ClassNames.VPN
import util.XposedHelpers
import util.XposedHelpersExt
import java.lang.reflect.Method

object Classes {

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.amsClass(): Class<*> {
        return XposedHelpers.findClass(AMS, this)
    }

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.amsLifecycleClass(): Class<*> {
        return XposedHelpers.findClass(AMS_LIFECYCLE, this)
    }

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.amsShellCommandClass(): Class<*> {
        return XposedHelpers.findClass("com.android.server.am.ActivityManagerShellCommand", this)
    }

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.atmsClass(): Class<*> {
        return XposedHelpers.findClass(ATMS, this)
    }

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.activeServicesClass(): Class<*> {
        return XposedHelpers.findClass("com.android.server.am.ActiveServices", this)
    }

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.activityStarterClass(): Class<*> {
        return XposedHelpers.findClass("com.android.server.wm.ActivityStarter", this)
    }

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.activityMetricsLoggerClass(): Class<*> {
        return XposedHelpers.findClass("com.android.server.wm.ActivityMetricsLogger", this)
    }

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.inputManagerService(): Class<*> {
        return XposedHelpers.findClass("com.android.server.input.InputManagerService", this)
    }

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.imeService(): Class<*> {
        return XposedHelpers.findClass(
            "com.android.server.inputmethod.InputMethodManagerService", this
        )
    }

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.accManager(): Class<*> {
        return XposedHelpers.findClass(
            "com.android.server.accessibility.UiAutomationManager", this
        )
    }

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.accProxyManager(): Class<*> {
        return XposedHelpers.findClass(
            "com.android.server.accessibility.AccessibilityUserState", this
        )
    }

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.windowManagerServiceClass(): Class<*> =
        XposedHelpers.findClass("com.android.server.wm.WindowManagerService", this)

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.windowStateClass(): Class<*> =
        XposedHelpers.findClass("com.android.server.wm.WindowState", this)

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.usageStatsServiceClass(): Class<*> =
        XposedHelpers.findClass("com.android.server.usage.UsageStatsService", this)

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.printManagerImplClass(): Class<*> = XposedHelpers.findClass(
        "com.android.server.print.PrintManagerService\$PrintManagerImpl", this
    )

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.appWidgetServiceImplClass(): Class<*> = XposedHelpers.findClass(
        "com.android.server.appwidget.AppWidgetServiceImpl", this
    )

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.notificationUsageStatsClass(): Class<*> = XposedHelpers.findClass(
        NOTIFICATION_USAGE_STATS_SERVICE, this
    )

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.activityStackSupervisorClass(methodName: String): Class<*> =
        XposedHelpersExt.anyClassFromNames(
            this, methodName, arrayOf(
                "com.android.server.am.ActivityStackSupervisor",
                "com.android.server.wm.ActivityStackSupervisor",
                // Android S emulator named.
                "com.android.server.wm.ActivityTaskSupervisor"
            )
        )

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.processListClass(): Class<*> = XposedHelpers.findClass(
        PROCESS_LIST, this
    )

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.processRecordClass(): Class<*> = XposedHelpers.findClass(
        PROCESS_RECORD, this
    )

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.activityRecordClass(): Class<*> = XposedHelpersExt.anyClassFromNames(
        this, arrayOf(
            ACTIVITY_RECORD_WM, ACTIVITY_RECORD_AM
        )
    )

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.vpnClass(): Class<*> = XposedHelpers.findClass(
        VPN, this
    )

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.mediaDocumentProviderClass(): Class<*> = XposedHelpers.findClass(
        MEDIA_DOCUMENTS_PROVIDER, this
    )

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.mediaProviderClass(): Class<*> = XposedHelpers.findClass(
        MEDIA_PROVIDER, this
    )

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.nfcServiceHandlerClass(): Class<*> = XposedHelpers.findClass(
        NFC_SERVICE_HANDLER, this
    )

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.phoneWindowManagerClass(): Class<*> = XposedHelpers.findClass(
        PHONE_WINDOW_MANAGER, this
    )

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.clipboardServiceClass(): Class<*> = XposedHelpers.findClass(
        CLIPBOARD_SERVICE, this
    )

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.clipboardServiceImplClass(): Class<*> = XposedHelpers.findClass(
        "$CLIPBOARD_SERVICE\$ClipboardImpl", this
    )

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.backNavControllerClass(): Class<*> = XposedHelpers.findClass(
        "com.android.server.wm.BackNavigationController", this
    )

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.taskOrganizerControllerClass(): Class<*> = XposedHelpers.findClass(
        "com.android.server.wm.TaskOrganizerController", this
    )

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.activityClientControllerClass(): Class<*> = XposedHelpers.findClass(
        "com.android.server.wm.ActivityClientController", this
    )

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.shortcutServiceClass(): Class<*> = XposedHelpers.findClass(
        "com.android.server.pm.ShortcutService", this
    )


    @Throws(ClassNotFoundException::class)
    fun ClassLoader.statusBarManagerServiceClass(): Class<*> = XposedHelpers.findClass(
        "com.android.server.statusbar.StatusBarManagerService", this
    )

    @Throws(ClassNotFoundException::class)
    fun ClassLoader.launcherAppsServiceImplClass(): Class<*> =
        XposedHelpers.findClass(
            "com.android.server.pm.LauncherAppsService\$LauncherAppsImpl",
            this
        )

    fun Class<*>.activityStoppedMethod(): Method? {
        return declaredMethods.firstOrNull { it.name == Methods.activityStopped || it.name == Methods.activityStoppedLocked }
    }
}