package github.tornaco.android.thanos.core.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemProperties;
import android.text.TextUtils;

import com.elvishew.xlog.XLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import util.IoUtils;

/**
 * Created by guohao4 on 2017/10/24.
 * Email: Tornaco@163.com
 */

public abstract class OsUtils {

    public static boolean isFlyme() {
        String id = SystemProperties.get("ro.build.display.id", "");
        return !TextUtils.isEmpty(id) && (id.contains("flyme")
                || id.toLowerCase().contains("flyme"));
    }

    public static boolean isMIUI() {
        return Build.FINGERPRINT.startsWith("Xiaomi") || Build.FINGERPRINT.startsWith("xiaomi");
    }

    public static boolean isLOS() {
        return false;
    }

    public static boolean isMOrAbove() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean isNOrAbove() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    public static boolean isOOrAbove() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    public static boolean isO() {
        return Build.VERSION.SDK_INT == 26 || Build.VERSION.SDK_INT == 27;
    }

    public static boolean isPOrAbove() {
        return Build.VERSION.SDK_INT >= 28;
    }

    // android 10
    public static boolean isQOrAbove() {
        return Build.VERSION.SDK_INT >= 29;
    }

    public static boolean isQ() {
        return Build.VERSION.SDK_INT == 29;
    }

    // android 11
    public static boolean isROrAbove() {
        return Build.VERSION.SDK_INT >= 30;
    }

    // android 12
    public static boolean isSOrAbove() {
        return Build.VERSION.SDK_INT >= 31;
    }

    // android 13
    public static boolean isTOrAbove() {
        return Build.VERSION.SDK_INT >= 33;
    }

    public static boolean hasTvFeature(Context context) {
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_LIVE_TV);
    }

    public static boolean isLenovoDevice() {
        return Build.MANUFACTURER.contains("ZUK") || Build.MANUFACTURER.contains("Lenovo");
    }

    public static boolean isNTDDevice() {
        return Build.MANUFACTURER.contains("NTD");
    }

    public static boolean isHuaWeiDevice() {
        return Build.MANUFACTURER.contains("HUAWEI");
    }

    public static boolean isEMUI() {
        return !TextUtils.isEmpty(SystemProperties.get("ro.build.version.emui", ""));
    }

    public static boolean isNubiaDevice() {
        return Build.FINGERPRINT.contains("nubia");
    }

    @Deprecated
    public static String getCpuUsagePercent() {
        java.lang.Process p = null;
        BufferedReader in = null;
        StringBuilder returnString = new StringBuilder();
        try {
            p = Runtime.getRuntime().exec("top -n 1");
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                returnString.append(line);
            }
        } catch (IOException e) {
            XLog.e("error in getting first line of top");
            e.printStackTrace();
        } finally {
            IoUtils.closeQuietly(in);
            IoUtils.closeQuietly(p);
        }
        return returnString.toString();
    }
}
