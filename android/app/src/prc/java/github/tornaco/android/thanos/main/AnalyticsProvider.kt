package github.tornaco.android.thanos.main

import github.tornaco.android.thanos.core.analytics.Analytics
import github.tornaco.android.thanos.core.analytics.Crashlytics
import github.tornaco.android.thanos.core.analytics.Event

val Analytics = object : Analytics {
    override fun reportEvent(event: Event) {
    }
}

val Crashlytics = object : Crashlytics {
    override fun log(message: String) {
    }

    override fun logError(e: Throwable) {
    }
}