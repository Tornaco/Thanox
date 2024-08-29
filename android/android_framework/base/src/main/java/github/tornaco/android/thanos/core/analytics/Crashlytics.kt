package github.tornaco.android.thanos.core.analytics

interface Crashlytics {
    fun log(message: String)
    fun logError(e: Throwable)
}