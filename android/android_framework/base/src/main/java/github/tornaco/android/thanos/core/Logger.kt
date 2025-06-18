package github.tornaco.android.thanos.core

import android.util.Log
import github.tornaco.android.thanos.BuildProp

class Logger(private val tag: String = "") {
    val fullTag
        get() = "ThanoxL${
            tag.takeIf { it.isNotBlank() }?.let { "-$it" }.orEmpty()
        }"

    inline fun p(any: Any?) {
        // TODO Change to false
        if (BuildProp.THANOS_BUILD_DEBUG) logAdapter(
            Log.DEBUG,
            fullTag,
            "[${Thread.currentThread().name}] $any"
        )
    }

    inline fun d(any: Any?) {
        if (BuildProp.THANOS_BUILD_DEBUG) logAdapter(
            Log.DEBUG,
            fullTag,
            "[${Thread.currentThread().name}] $any"
        )
    }

    fun i(any: Any?) {
        logAdapter(Log.INFO, fullTag, "[${Thread.currentThread().name}] $any")
    }


    fun w(any: Any?) {
        logAdapter(Log.WARN, fullTag, "[${Thread.currentThread().name}] $any")
    }

    fun e(any: Any?) {
        logAdapter(Log.ERROR, fullTag, "[${Thread.currentThread().name}] $any")
    }

    fun e(throwable: Throwable, any: Any?) {
        logAdapter(
            Log.ERROR,
            fullTag,
            "[${Thread.currentThread().name}] $any --- ${throwable.stackTraceToString()}"
        )
    }
}

var logAdapter: (level: Int, String, String) -> Unit = { level: Int, tag: String, msg: String ->
    Log.println(level, tag, msg)
}