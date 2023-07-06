package github.tornaco.android.thanos.core.ops

enum class PermState {
    // Align with System. no UID mode.
    DEFAULT,

    // Has not been set
    NOT_SET,

    // Requested/Set but denied
    DENY,

    // Ask everytime, for Runtime perm only?
    ASK,

    ALLOW_ALWAYS,
    ALLOW_FOREGROUND_ONLY,

    // May got error
    UNKNOWN
}