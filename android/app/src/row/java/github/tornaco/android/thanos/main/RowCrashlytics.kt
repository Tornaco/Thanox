package github.tornaco.android.thanos.main

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import github.tornaco.android.thanos.core.analytics.Crashlytics

class RowCrashlytics : Crashlytics {
    private var firebaseCrashlytics: FirebaseCrashlytics? = null

    init {
        firebaseCrashlytics = Firebase.crashlytics
    }

    override fun log(message: String) {
        firebaseCrashlytics?.log(message)
    }

    override fun logError(e: Throwable) {
        firebaseCrashlytics?.log(e.stackTraceToString())
    }
}