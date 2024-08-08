package now.fortuitous.thanos.analytics

import now.fortuitous.thanos.analytics.Event.SelectActiveMethod

interface Analytics {
    fun reportEvent(event: Event)
}

sealed interface Event {
    val name: String
    val params: Map<String, Any>

    data class SelectActiveMethod(val method: String) : Event {
        override val name: String
            get() = "select_active_method"
        override val params: Map<String, Any>
            get() = mapOf("method" to method)
    }
}

val SelectActiveMethodXposedOrMagisk get() = SelectActiveMethod("xposed-magisk")
val SelectActiveMethodShizuku get() = SelectActiveMethod("shizuku")