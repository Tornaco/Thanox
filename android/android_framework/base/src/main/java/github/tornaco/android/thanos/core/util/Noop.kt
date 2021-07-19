package github.tornaco.android.thanos.core.util

object Noop {

    @JvmStatic
    fun <T> notSupported(): T {
        throw Error("Not supported")
    }
}
