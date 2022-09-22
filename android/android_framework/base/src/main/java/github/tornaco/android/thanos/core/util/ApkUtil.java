package github.tornaco.android.thanos.core.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.elvishew.xlog.XLog;

/**
 * Created by Nick@NewStand.org on 2017/3/13 9:56
 * E-Mail: NewStand@163.com
 * All right reserved.
 */

public abstract class ApkUtil {

    public static CharSequence loadNameByPkgName(Context context, String pkg) {
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(pkg, PackageManager.GET_UNINSTALLED_PACKAGES);
            if (info == null) return null;
            return info.loadLabel(pm);
        } catch (Throwable e) {
            XLog.e("loadNameByPkgName error", e);
            return null;
        }
    }

    public static Drawable loadIconByPkgName(Context context, String pkg) {
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(pkg, PackageManager.GET_UNINSTALLED_PACKAGES);
            if (info == null) return null;
            return info.loadIcon(pm);
        } catch (Throwable e) {
            XLog.e("loadIconByPkgName error", e);
            return null;
        }
    }

    public static Drawable loadIconByFilePath(Context context, String filePath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
        if (info == null) return null; // FIX NPE
        ApplicationInfo appInfo = info.applicationInfo;
        appInfo.sourceDir = filePath;
        appInfo.publicSourceDir = filePath;
        return appInfo.loadIcon(pm);
    }

    public static String loadVersionByFilePath(Context context, String filePath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
        if (info == null) return null; // FIX NPE
        return info.versionName;
    }

    public static String loadPkgNameByFilePath(Context context, String filePath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
        if (info == null) return null; // FIX NPE
        return info.packageName;
    }

    public static String loadAppNameByFilePath(Context context, String filePath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
        if (info == null) return null; // FIX NPE
        return info.applicationInfo.name;
    }
}
