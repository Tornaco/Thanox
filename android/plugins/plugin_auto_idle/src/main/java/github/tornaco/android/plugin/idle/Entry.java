package github.tornaco.android.plugin.idle;

import android.content.Context;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.Printer;
import com.elvishew.xlog.printer.file.FilePrinter;
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator;
import com.google.common.io.Files;

import java.io.File;

import github.tornaco.android.thanos.core.T;
import github.tornaco.android.thanos.core.util.FileUtils;

@SuppressWarnings("UnstableApiUsage")
public class Entry {

    static {
        // Prepare dir.
        try {
            FileUtils.deleteDir(T.baseLoggingDir());
            Files.createParentDirs(new File(T.baseLoggingDir(), "sub"));
        } catch (Throwable e) {
            e.printStackTrace();
        }

        Printer filePrinter = new FilePrinter
                .Builder(new File(T.baseServerLoggingDir(), "plugin_smart_standby_v1_log").getAbsolutePath())
                .fileNameGenerator(new DateFileNameGenerator())
                .build();

        Printer androidPrinter = new AndroidPrinter();
        XLog.init(new LogConfiguration.Builder()
                        .logLevel(LogLevel.ALL)
                        .tag("PushDelegate")
                        .build(),
                androidPrinter,
                filePrinter);
    }

    public static void boot(Context context) {
    }
}
