package github.tornaco.android.thanos.main

import now.fortuitous.thanos.analytics.Analytics
import now.fortuitous.thanos.analytics.Event

val Analytics = object : Analytics {
    override fun reportEvent(event: Event) {
    }
}