package github.tornaco.android.thanos.module.compose.common.settings


sealed interface Preference {
    val title: String
    val summary: String?
    val hasLongSummary: Boolean

    val icon: Int?
    val badge: String?

    val isDecor: Boolean

    data object Divider : Preference {
        override val title: String = "Divider"
        override val summary: String? = null
        override val hasLongSummary: Boolean
            get() = false
        override val icon: Int? = null
        override val badge: String? = null
        override val isDecor: Boolean = true
    }

    data class Category(
        override val title: String,
    ) : Preference {
        override val summary: String? = null
        override val hasLongSummary: Boolean
            get() = false
        override val icon: Int? = null
        override val badge: String? = null
        override val isDecor: Boolean = true
    }

    data class TextPreference(
        override val title: String,
        override val summary: String? = null,
        override val hasLongSummary: Boolean = false,
        override val icon: Int? = null,
        override val badge: String? = null,
        val withCardBg: Boolean = false,
        val onClick: (() -> Unit)? = null,
        override val isDecor: Boolean = false
    ) : Preference

    data class SwitchPreference(
        override val title: String,
        override val summary: String? = null,
        override val hasLongSummary: Boolean = false,
        override val icon: Int? = null,
        override val badge: String? = null,
        val value: Boolean,
        val onClick: (() -> Unit)? = null,
        val onCheckChanged: (Boolean) -> Unit,
        override val isDecor: Boolean = false
    ) : Preference

    data class ExpandablePreference(
        override val title: String,
        override val summary: String? = null,
        override val hasLongSummary: Boolean = false,
        override val icon: Int? = null,
        override val badge: String? = null,
        val preferences: List<Preference>,
        val onClick: (() -> Unit)? = null,
        override val isDecor: Boolean = false
    ) : Preference
}