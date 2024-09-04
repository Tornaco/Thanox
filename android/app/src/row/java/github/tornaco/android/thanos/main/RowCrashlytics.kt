package github.tornaco.android.thanos.main

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import github.tornaco.android.thanos.core.analytics.Crashlytics

class RowCrashlytics : Crashlytics {
    private val firebaseCrashlytics: FirebaseCrashlytics? by lazy {
        runCatching { Firebase.crashlytics }.getOrNull()
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