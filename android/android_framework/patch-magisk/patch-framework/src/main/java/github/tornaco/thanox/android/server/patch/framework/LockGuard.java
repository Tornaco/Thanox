package github.tornaco.thanox.android.server.patch.framework;

import com.elvishew.xlog.XLog;

import util.XposedHelpers;

// frameworks/base/services/core/java/com/android/server/LockGuard.java
public class LockGuard {

    /**
     * Well-known locks ordered by fixed index. Locks with a specific index
     * should never be acquired while holding a lock of a lower index.
     */
    public static final int INDEX_APP_OPS = 0;
    public static final int INDEX_POWER = 1;
    public static final int INDEX_USER = 2;
    public static final int INDEX_PACKAGES = 3;
    public static final int INDEX_STORAGE = 4;
    public static final int INDEX_WINDOW = 5;
    public static final int INDEX_ACTIVITY = 6;
    public static final int INDEX_DPMS = 7;

    public static Object retrieveServices(int index) {
        // AMS: LockGuard.installLock(this, LockGuard.INDEX_ACTIVITY);
        // private static Object[] sKnownFixed = new Object[INDEX_DPMS + 1];
        try {
            Object[] sKnownFixed = (Object[]) XposedHelpers.getStaticObjectField(
                    Class.forName("com.android.server.LockGuard"),
                    "sKnownFixed");
            if (sKnownFixed != null) {
                return sKnownFixed[index];
            }
        } catch (Throwable e) {
            XLog.e("LockGuard#@retrieveServices error", e);
        }
        return null;
    }
}
