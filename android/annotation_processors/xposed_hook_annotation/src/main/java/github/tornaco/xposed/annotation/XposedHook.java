package github.tornaco.xposed.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface XposedHook {
  int[] targetSdkVersion();

  boolean active() default true;

  int priority() default 1;

  interface SdkVersions {
    int _21 = 21;
    int _22 = 22;
    int _23 = 23;
    int _24 = 24;
    int _25 = 25;
    int _26 = 26;
    int _27 = 27;
    int _28 = 28;
    int _29 = 29;
    int _30 = 30;
    int _31 = 31;
    int _32 = 32;

    int[] ALL = {_21, _22, _23, _24, _25, _26, _27, _28, _29, _30, _31, _32};
  }
}
