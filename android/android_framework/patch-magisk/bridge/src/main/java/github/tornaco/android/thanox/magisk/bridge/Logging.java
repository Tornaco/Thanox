package github.tornaco.android.thanox.magisk.bridge;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.Printer;

import lombok.Synchronized;

public class Logging {
    static {
        Printer androidPrinter = new AndroidPrinter();
        XLog.init(new LogConfiguration.Builder()
                        .logLevel(LogLevel.ALL)
                        .tag("Thanox-Magisk-Bridge")
                        .build(),
                androidPrinter);
    }

    @Synchronized
    public static void logging(String format, Object... args) {
        XLog.i(String.format(format, args));
    }
}
