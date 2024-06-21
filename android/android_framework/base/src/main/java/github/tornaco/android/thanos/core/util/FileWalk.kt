package github.tornaco.android.thanos.core.util

import java.io.File

object FileWalk {
    fun walk(file: File, action: (File) -> Unit) {
        file.walkTopDown().forEach {
            action(it)
        }
    }
}