package github.tornaco.android.thanos.core.util;

import com.elvishew.xlog.XLog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Tornaco on 2018/5/1 19:21.
 * God bless no bug!
 */
public class ClazzDumper {

    public static final Printer ANDROID_UTIL_LOG_PRINTER = new Printer() {
        @Override
        public void start() {
            XLog.w("ANDROID_UTIL_LOG_PRINTER");
        }

        @Override
        public void println(String line) {
            XLog.w(line);
        }

        @Override
        public void end() {
            XLog.w("ANDROID_UTIL_LOG_PRINTER");
        }
    };

    public interface Printer {
        void start();

        void println(String line);

        void end();
    }

    public static void dump(Class clazz) {
        dump(clazz, ANDROID_UTIL_LOG_PRINTER);
    }

    public static void dump(Class clazz, Printer printer) {
        printer.start();
        // Dump class header.
        printer.println(String.format("\n**** CLAZZ DUMPER START DUMP OF %s ***", clazz));
        // Dump methods.
        Class<?> searchType = clazz;
        while (searchType != null) {
            Method[] methods = (searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods());
            for (Method method : methods) {
                printer.println(String.format("METHOD OF CLASS %s: %s ", searchType, method));
            }

            // Dump class fields.
            Field[] fields = searchType.isInterface() ? null : clazz.getDeclaredFields();
            if (fields != null) for (Field field : fields) {
                printer.println(String.format("FIELD OF CLASS %s: %s", searchType, field));
            }

            searchType = searchType.getSuperclass();
        }
        // End.
        printer.println(String.format("**** CLAZZ DUMPER END DUMP OF %s ***\n", clazz));
        printer.end();
    }
}
