package github.tornaco.android.thanos.services.xposed;

import de.robv.android.xposed.XposedBridge;

public class XposedLogger {
  public static final String LOG_PREFIX = "Thanos_";

  public static boolean isDebug() {
    return Boolean.TRUE;
  }

  public static void log(String tag, String f, Object... a) {
    if (isDebug()) XposedBridge.log(tag + "\t" + String.format(f, a));
  }
}
