package github.tornaco.notro.gradle.plugin.core;

public class Logger {

    public static void log(String format, Object... args) {
        System.out.println("[Nitro-Plugin] [INFO]   " + String.format(format, args));
    }

    public static void warn(String format, Object... args) {
        System.out.println("[******Nitro-Plugin******] [WARN]   " + String.format(format, args));
    }
}
