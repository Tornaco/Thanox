package util;

import java.lang.reflect.Method;

public class ProxyUtils {

    private ProxyUtils() {
    }

    public static Object relaxedMethodCallRes(String tag, Object proxy, Method method, Object[] args) {
        if (ReflectionUtils.isToStringMethod(method)) {
            return tag;
        }

        if (ReflectionUtils.isEqualsMethod(method)) {
            return false;
        }

        if (ReflectionUtils.isHashCodeMethod(method)) {
            return tag.hashCode();
        }

        Class<?> returnType = method.getReturnType();

        if (returnType == boolean.class) return false;
        if (returnType == int.class) return 0;
        if (returnType == long.class) return 0L;
        if (returnType == float.class) return 0;
        if (returnType == double.class) return 0;
        if (returnType == char.class) return "";
        if (returnType == short.class) return 0;

        return null;
    }
}
