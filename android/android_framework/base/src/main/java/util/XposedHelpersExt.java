package util;

import android.os.Build;

import com.elvishew.xlog.XLog;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import github.tornaco.android.thanos.core.annotation.RequiresApi;
import github.tornaco.android.thanos.core.util.OsUtils;

public class XposedHelpersExt {

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

  public static Class<?> anyClassFromNames(ClassLoader classLoader, String... classNames)
          throws ClassNotFoundException {
    for (String className: classNames) {
      try {
        Class<?> res = XposedHelpers.findClass(className, classLoader);
        XLog.w("anyClassFromNames, find class for name: " + className);
        return res;
      } catch (Throwable e) {
        XLog.w("anyClassFromNames, no class for this name: " + className);
      }
    }
    throw new ClassNotFoundException(Arrays.toString(classNames));
  }

  public static int getIntFieldWithPotentialNames(Object obj, String... potentialFieldNames) throws NoSuchFieldException {
    for (String fieldName: potentialFieldNames) {
      try {
        int res = XposedHelpers.getIntField(obj, fieldName);
        XLog.w("getIntField, find field for name: " + fieldName);
        return res;
      } catch (Throwable e){
        XLog.w("getIntField, no field for this name: " + fieldName);
      }
    }
    throw new NoSuchFieldException(Arrays.toString(potentialFieldNames));
  }
}
