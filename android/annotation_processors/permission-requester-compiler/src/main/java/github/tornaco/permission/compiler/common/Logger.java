package github.tornaco.permission.compiler.common;

/**
 * Created by Nick@NewStand.org on 2017/4/12 13:35
 * E-Mail: NewStand@163.com
 * All right reserved.
 */

public abstract class Logger {

    public static void debug(String message, Object... obj) {
        System.out.println("[RuntimePermissionsCompiler] " + String.format(message, obj));
    }

    public static void report(String message, Object... obj) {
        report(new Throwable(), message, obj);
    }

    public static void report(Throwable throwable, String message, Object... obj) {
        System.err.println(String.format(message, obj));
        printStackTrace(throwable);
    }

    public static void printStackTrace(Throwable throwable) {
        if (throwable == null) return;
        throwable.printStackTrace();
    }
}
