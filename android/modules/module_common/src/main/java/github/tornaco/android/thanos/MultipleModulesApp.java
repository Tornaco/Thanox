package github.tornaco.android.thanos;

import android.app.Application;
import android.content.Context;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.Printer;
import com.elvishew.xlog.printer.file.FilePrinter;
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator;

import java.io.File;

public class MultipleModulesApp extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Printer androidPrinter = new AndroidPrinter();
        Printer filePrinter = new FilePrinter
                .Builder(getExternalCacheDir() + File.separator + "logs")
                .fileNameGenerator(new DateFileNameGenerator())
                .build();
        XLog.init(new LogConfiguration.Builder()
                        .logLevel(LogLevel.ALL)
                        .tag("ThanoxApp")
                        .build(),
                filePrinter,
                androidPrinter);
    }
}
