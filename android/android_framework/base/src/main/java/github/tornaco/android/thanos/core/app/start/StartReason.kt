package github.tornaco.android.thanos.core.app.start

interface StartReason {
    companion object {
        const val ACTIVITY = 0x1
        const val SERVICE = 0x2
        const val RESTART_SERVICE = 0x3
        const val PROVIDER = 0x4
        const val BROADCAST = 0x5
        const val OTHERS = 0x6

        const val PRE_ACTIVITY = 0x7
        const val PRE_TOP_ACTIVITY = 0x8
        const val TOP_ACTIVITY = 0x9
    }
}
