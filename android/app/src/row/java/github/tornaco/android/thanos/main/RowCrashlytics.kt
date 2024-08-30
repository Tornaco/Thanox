package github.tornaco.android.thanos.main

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.crashlytics.setCustomKeys
import com.google.firebase.ktx.Firebase
import github.tornaco.android.thanos.core.analytics.Crashlytics
import github.tornaco.android.thanos.core.app.AppGlobals
import now.fortuitous.app.donate.DonateSettings
import tornaco.apps.thanox.subscribe.DeviceId

class RowCrashlytics : Crashlytics {
    private var firebaseCrashlytics: FirebaseCrashlytics? = null

    init {
        firebaseCrashlytics = Firebase.crashlytics.apply {
            setCustomKeys {
                key("thanos_device", DeviceId.requireDeviceId())
                key("thanox_device", DonateSettings.getDeviceId(AppGlobals.context))
            }
        }
    }

    override fun log(message: String) {
        kotlin.runCatching {
            firebaseCrashlytics?.log(message)
        }
    }

    override fun logError(e: Throwable) {
        kotlin.runCatching {
            firebaseCrashlytics?.log(e.stackTraceToString())
        }
    }
}