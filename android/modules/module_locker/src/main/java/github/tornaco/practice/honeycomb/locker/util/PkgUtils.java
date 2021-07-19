package github.tornaco.practice.honeycomb.locker.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class PkgUtils {

    public static boolean hasActivity(Context context, String pkg) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(pkg, PackageManager.GET_ACTIVITIES);
            return packageInfo != null
                    && packageInfo.activities != null
                    && packageInfo.activities.length > 0;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
