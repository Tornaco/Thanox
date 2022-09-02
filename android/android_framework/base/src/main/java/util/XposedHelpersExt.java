package util;

import android.os.Build;

import com.elvishew.xlog.XLog;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import github.tornaco.android.thanos.core.annotation.RequiresApi;

public class XposedHelpersExt {
  private static final Map<String, Integer> METHOD_PARAM_INDEX_CACHE = new HashMap<>();

  public static Object getObjectFieldOrNull(Object obj, String fieldName) {
    try {
      return XposedHelpers.getObjectField(obj, fieldName);
    } catch (Throwable e) {
      XLog.e(e, "getObjectFieldOrNull");
      return null;
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  public static Method findMethodWithMostArgs(Class<?> clazz, String methodName) {
    List<Method> methodList = new ArrayList<>();
    for (Method m : clazz.getDeclaredMethods()) {
      if (m.getName().equals(methodName)) {
        methodList.add(m);
      }
    }
    if (methodList.isEmpty()) {
      return null;
    }
    methodList.sort(new Comparator<Method>() {
      @Override
      public int compare(Method a, Method b) {
        return -Integer.compare(a.getParameterTypes().length, b.getParameterTypes().length);
      }
    });
    return methodList.get(0);
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  public static int getFirstArgIndexWithTypeForMethod(Method method, String className) {
    try {
      Parameter[] parameters = method.getParameters();
      if (parameters.length == 0) {
        return -1;
      }
      for (int i = 0; i < parameters.length; i++) {
        Parameter p = parameters[i];
        if (ObjectsUtils.equals(p.getType().getName(), className)) {
          return i;
        }
      }
    } catch (Throwable e) {
      XLog.e(e, "getFirstArgIndexWithType");
    }
    return -1;
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  public static int getFirstArgIndexLikeTypeForMethod(Method method, String classNameOrSimpleName) {
    try {
      Parameter[] parameters = method.getParameters();
      if (parameters.length == 0) {
        return -1;
      }
      for (int i = 0; i < parameters.length; i++) {
        Parameter p = parameters[i];
        if (ObjectsUtils.equals(p.getType().getName(), classNameOrSimpleName)
            || ObjectsUtils.equals(p.getType().getSimpleName(), classNameOrSimpleName)) {
          return i;
        }
      }
    } catch (Throwable e) {
      XLog.e(e, "getFirstArgIndexWithType");
    }
    return -1;
  }

  public static Class<?> anyClassFromNames(ClassLoader classLoader, String[] classNames)
          throws ClassNotFoundException {
    XLog.d("anyClassFromNames, " + Arrays.toString(classNames));
    for (String className: classNames) {
      try {
        Class<?> res = XposedHelpers.findClass(className, classLoader);
        XLog.d("anyClassFromNames, find class for name: " + className);
        return res;
      } catch (Throwable e) {
        XLog.d("anyClassFromNames, no class for this name: " + className);
      }
    }
    throw new ClassNotFoundException(Arrays.toString(classNames));
  }

  public static Class<?> anyClassFromNames(ClassLoader classLoader, String methodNameToFind, String[] classNames)
          throws ClassNotFoundException {
    XLog.d("anyClassFromNames, classNames: %s, methodNameToFind: %s", Arrays.toString(classNames), methodNameToFind);
    for (String className : classNames) {
      try {
        Class<?> res = XposedHelpers.findClass(className, classLoader);
        XLog.d("anyClassFromNames, found class for this name: %s, will check if method exists", className);
        boolean methodExists = false;
        for (Method method : res.getDeclaredMethods()) {
          if (method.getName().equals(methodNameToFind)) {
            methodExists = true;
          }
        }
        XLog.d("anyClassFromNames, find class for name: %s, methodExists? %s", className, methodExists);
        if (methodExists) return res;
      } catch (Throwable e) {
        XLog.d("anyClassFromNames, no class for this name: " + className);
      }
    }
    throw new ClassNotFoundException(Arrays.toString(classNames));
  }

  public static int getIntFieldWithPotentialNames(Object obj, String... potentialFieldNames) throws NoSuchFieldException {
    for (String fieldName: potentialFieldNames) {
      try {
        int res = XposedHelpers.getIntField(obj, fieldName);
        XLog.d("getIntField, find field for name: " + fieldName);
        return res;
      } catch (Throwable e){
        XLog.d("getIntField, no field for this name: " + fieldName);
      }
    }
    throw new NoSuchFieldException(Arrays.toString(potentialFieldNames));
  }

  public static boolean matchMethodNameAndArgs(Method methodToCheck, String methodNameExpected, Class<?>... argTypesExpected) {
    if (!methodNameExpected.equals(methodToCheck.getName())){
      return false;
    }
    return Arrays.equals(methodToCheck.getParameterTypes(), argTypesExpected);
  }

  public static Object callMethodWithPreferredNames(Object obj, String[] methodNames, Object... args) {
    for (String methodName : methodNames) {
      try {
        return XposedHelpers.callMethod(obj, methodName, args);
      } catch (NoSuchMethodError ignored) {
        // Try next one.
      }
    }
    return null;
  }

  /**
   * This may not work on Android class, param name may not present.
   * */
  public static int getParamIndex(Class<?> clazz, Method method, String paramName, int defaultValue) {
    try {
      String cacheKey = clazz.getName() + method.toString();
      Integer cachedIndex = METHOD_PARAM_INDEX_CACHE.get(cacheKey);
      if (cachedIndex != null) return cachedIndex;

      Parameter[] parameters = method.getParameters();
      for (int i = 0; i < parameters.length; i++) {
        Parameter p = parameters[i];
        XLog.i("getParamIndex, name = "+ p.getName());
        if (p.isNamePresent() && paramName.equals(p.getName())) {
          METHOD_PARAM_INDEX_CACHE.put(cacheKey, i);
          return i;
        }
      }

      METHOD_PARAM_INDEX_CACHE.put(cacheKey, defaultValue);
      return defaultValue;
    } catch (Throwable e) {
      XLog.e("getParamIndexOr error", e);
      return defaultValue;
    }
  }
}
