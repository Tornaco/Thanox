package github.tornaco.practice.honeycomb.locker.util;

import android.content.Intent;

public class IntentUtils {

    public static boolean isHomeIntent(Intent intent) {
        return intent != null && intent.hasCategory(Intent.CATEGORY_HOME);
    }
}
