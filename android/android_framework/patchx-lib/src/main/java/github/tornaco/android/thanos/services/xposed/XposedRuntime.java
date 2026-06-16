package github.tornaco.android.thanos.services.xposed;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.function.Consumer;

public final class XposedRuntime {
    private static XposedAdapter adapter;
    private static boolean isSystemServer;

    private XposedRuntime() {
    }

    public static void init(XposedAdapter xposedAdapter, boolean systemServer) {
        adapter = xposedAdapter;
        isSystemServer = systemServer;
    }

    public static XposedAdapter current() {
        XposedAdapter a = adapter;
        if (a == null) {
            throw new IllegalStateException("XposedAdapter is not initialized");
        }
        return a;
    }

    public static boolean isSystemServer() {
        return isSystemServer;
    }

    public static void hookBefore(Class<?> clazz, String methodName, XposedAdapter.ExceptionModeCompat exceptionModeCompat, Consumer<ThanoxHookParam> callback) {
        XposedAdapter a = current();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                a.hookBefore(method, exceptionModeCompat, callback);
            }
        }
    }

    public static void hookBefore(Class<?> clazz, String methodName, Consumer<ThanoxHookParam> callback) {
        XposedAdapter a = current();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                a.hookBefore(method, XposedAdapter.ExceptionModeCompat.PROTECTIVE, callback);
            }
        }
    }

    public static void hookAfter(Class<?> clazz, String methodName, Consumer<ThanoxHookParam> callback) {
        XposedAdapter a = current();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                a.hookAfter(method, XposedAdapter.ExceptionModeCompat.PROTECTIVE, callback);
            }
        }
    }

    public static void hookBefore(Method method, Consumer<ThanoxHookParam> callback) {
        current().hookBefore(method, XposedAdapter.ExceptionModeCompat.PROTECTIVE, callback);
    }

    public static void hookAfter(Method method, Consumer<ThanoxHookParam> callback) {
        current().hookAfter(method, XposedAdapter.ExceptionModeCompat.PROTECTIVE, callback);
    }

    public static void hookBefore(Constructor<?> constructor, Consumer<ThanoxHookParam> callback) {
        current().hookBefore(constructor, XposedAdapter.ExceptionModeCompat.PROTECTIVE, callback);
    }

    public static void hookAfter(Constructor<?> constructor, Consumer<ThanoxHookParam> callback) {
        current().hookAfter(constructor, XposedAdapter.ExceptionModeCompat.PROTECTIVE, callback);
    }

    public static void hookAllConstructorsBefore(Class<?> clazz, Consumer<ThanoxHookParam> callback) {
        XposedAdapter a = current();
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            a.hookBefore(constructor, XposedAdapter.ExceptionModeCompat.PROTECTIVE, callback);
        }
    }

    public static void hookAllConstructorsAfter(Class<?> clazz, Consumer<ThanoxHookParam> callback) {
        XposedAdapter a = current();
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            a.hookAfter(constructor, XposedAdapter.ExceptionModeCompat.PROTECTIVE, callback);
        }
    }
}
