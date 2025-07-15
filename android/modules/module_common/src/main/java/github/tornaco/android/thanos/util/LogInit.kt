package github.tornaco.android.thanos.util

import android.content.Context
import android.util.Log
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.elvishew.xlog.printer.AndroidPrinter
import com.elvishew.xlog.printer.Printer
import com.elvishew.xlog.printer.file.FilePrinter
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator
import github.tornaco.android.thanos.core.logAdapter
import github.tornaco.android.thanos.logFolderPath

fun initAppLog(context: Context, logTag: String) {
    val androidPrinter: Printer = AndroidPrinter()
    val filePrinter: Printer = FilePrinter.Builder(context.logFolderPath)
        .fileNameGenerator(DateFileNameGenerator())
        .build()
    XLog.init(
        LogConfiguration.Builder()
            .logLevel(LogLevel.ALL)
            .tag(logTag)
            .build(),
        filePrinter,
        androidPrinter
    )

    logAdapter = { level: Int, tag: String, msg: String ->
        when (level) {
            Log.VERBOSE -> XLog.v("$tag $msg")
            Log.DEBUG -> XLog.d("$tag $msg")
            Log.INFO -> XLog.i("$tag $msg")
            Log.WARN -> XLog.w("$tag $msg")
            Log.ERROR -> XLog.e("$tag $msg")
        }
    }
}