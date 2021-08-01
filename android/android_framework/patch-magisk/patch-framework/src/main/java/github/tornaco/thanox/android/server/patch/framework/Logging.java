package github.tornaco.thanox.android.server.patch.framework;

import android.util.Log;

import lombok.Synchronized;

public class Logging {
    @Synchronized
    public static void logging(String format, Object... args) {
        Log.e("Thanox-Magisk-Framework", String.format(format, args));
    }
}
