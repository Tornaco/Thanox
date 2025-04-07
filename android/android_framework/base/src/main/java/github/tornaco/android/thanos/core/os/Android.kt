package github.tornaco.android.thanos.core.os

import github.tornaco.android.thanos.core.util.OsUtils


object PackageNames {
    const val android = "android"
    const val systemUI = "com.android.systemui"

    val mediaProvider = if (OsUtils.isROrAbove()) {
        "com.android.providers.media.module"
    } else {
        "com.android.providers.media"
    }

    val nfc = "com.android.nfc"

}

object Methods {
    const val start = "start"
    const val systemReady = "systemReady"
    const val filterInputEvent = "filterInputEvent"
    const val reportEvent = "reportEvent"
    const val getService = "getService"
    const val onTransact = "onTransact"
    const val print = "print"
    const val startListening = "startListening"
    const val registerPostedByApp = "registerPostedByApp"
    const val registerUpdatedByApp = "registerUpdatedByApp"
    const val registerRemovedByApp = "registerRemovedByApp"
    const val registerDismissedByUser = "registerDismissedByUser"
    const val cleanUpRemovedTaskLocked = "cleanUpRemovedTaskLocked"

    // Android 14
    const val cleanUpRemovedTask = "cleanUpRemovedTask"
    const val removeLruProcessLocked = "removeLruProcessLocked"
    const val handleProcessStartedLocked = "handleProcessStartedLocked"
    const val activityResumed = "activityResumed"
    const val activityResumedLocked = "activityResumedLocked"
    const val activityStopped = "activityStopped"
    const val activityStoppedLocked = "activityStoppedLocked"
    const val destroyed = "destroyed"
    const val updateState = "updateState"
    const val onMediaStoreInsert = "onMediaStoreInsert"
    const val onMediaStoreDelete = "onMediaStoreDelete"
    const val onDelete = "onDelete"
    const val onInsert = "onInsert"
    const val interceptKeyBeforeQueueing = "interceptKeyBeforeQueueing"
    const val onCreate = "onCreate"
    const val updateScreenStateLocked = "updateScreenStateLocked"
    const val setState = "setState"

    // Android 13-14
    const val onBackNavigationDone = "onBackNavigationDone"
    const val startBackNavigation = "startBackNavigation"

    const val requestPinItem = "requestPinItem"
    const val getPrimaryClip = "getPrimaryClip"
}

object ClassNames {
    const val AMS = "com.android.server.am.ActivityManagerService"
    const val AMS_LIFECYCLE = "com.android.server.am.ActivityManagerService\$Lifecycle"
    const val ATMS = "com.android.server.wm.ActivityTaskManagerService"
    const val NMS = "com.android.server.notification.NotificationManagerService"
    const val INPUT_MANAGER_SERVICE = "com.android.server.inputmethod.InputMethodManagerService"
    const val NOTIFICATION_USAGE_STATS_SERVICE =
        "com.android.server.notification.NotificationUsageStats"
    const val PROCESS_LIST = "com.android.server.am.ProcessList"
    const val PROCESS_RECORD = "com.android.server.am.ProcessRecord"
    const val ACTIVITY_RECORD_WM = "com.android.server.wm.ActivityRecord"
    const val ACTIVITY_RECORD_AM = "com.android.server.am.ActivityRecord"
    const val VPN = "com.android.server.connectivity.Vpn"
    const val MEDIA_DOCUMENTS_PROVIDER = "com.android.providers.media.MediaDocumentsProvider"
    const val MEDIA_PROVIDER = "com.android.providers.media.MediaProvider"
    const val PHONE_WINDOW_MANAGER = "com.android.server.policy.PhoneWindowManager"
    const val CLIPBOARD_SERVICE = "com.android.server.clipboard.ClipboardService"
    const val CLIPBOARD_SERVICE_IMPL =
        "com.android.server.clipboard.ClipboardService\$ClipboardImpl"
    const val SYSTEM_UI_APP = "com.android.systemui.SystemUIApplication"
    const val SYSTEM_UI_CUSTOM_TILE = "com.android.systemui.qs.external.CustomTile"
    const val NFC_SERVICE_HANDLER = "com.android.nfc.NfcService\$NfcServiceHandler"
}

object Fields {
    const val mContext = "mContext"
}

fun String.lifecycleClassName() = "$this\$Lifecycle"