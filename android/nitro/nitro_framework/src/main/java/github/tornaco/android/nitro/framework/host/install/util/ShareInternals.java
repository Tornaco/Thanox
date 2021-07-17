package github.tornaco.android.nitro.framework.host.install.util;

import android.os.Build;
import android.text.TextUtils;

import com.elvishew.xlog.XLog;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShareInternals {

    private static final boolean VM_IS_ART = isVmArt(System.getProperty("java.vm.version"));
    private static final boolean VM_IS_JIT = isVmJitInternal();

    private static String currentInstructionSet = null;

    public static boolean isVmArt() {
        return VM_IS_ART || Build.VERSION.SDK_INT >= 21;
    }

    public static boolean isVmJit() {
        return VM_IS_JIT && Build.VERSION.SDK_INT < 24;
    }

    public static boolean isAfterAndroidO() {
        return Build.VERSION.SDK_INT > 25;
    }

    public static String getCurrentInstructionSet() throws Exception {
        if (currentInstructionSet != null) {
            return currentInstructionSet;
        }
        Class<?> clazz = Class.forName("dalvik.system.VMRuntime");
        Method currentGet = clazz.getDeclaredMethod("getCurrentInstructionSet");

        currentInstructionSet = (String) currentGet.invoke(null);
        XLog.d("getCurrentInstructionSet: %s", currentInstructionSet);
        return currentInstructionSet;
    }

    /**
     * vm whether it is art
     *
     * @return
     */
    private static boolean isVmArt(String versionString) {
        boolean isArt = false;
        if (versionString != null) {
            Matcher matcher = Pattern.compile("(\\d+)\\.(\\d+)(\\.\\d+)?").matcher(versionString);
            if (matcher.matches()) {
                try {
                    int major = Integer.parseInt(matcher.group(1));
                    int minor = Integer.parseInt(matcher.group(2));
                    isArt = (major > 2)
                            || ((major == 2)
                            && (minor >= 1));
                } catch (NumberFormatException e) {
                    // let isMultidexCapable be false
                }
            }
        }
        return isArt;
    }

    private static boolean isVmJitInternal() {
        try {
            Class<?> clazz = Class.forName("android.os.SystemProperties");
            Method mthGet = clazz.getDeclaredMethod("get", String.class);

            String jit = (String) mthGet.invoke(null, "dalvik.vm.usejit");
            String jitProfile = (String) mthGet.invoke(null, "dalvik.vm.usejitprofiles");

            //usejit is true and usejitprofiles is null
            if (!TextUtils.isEmpty(jit) && TextUtils.isEmpty(jitProfile) && jit.equals("true")) {
                return true;
            }
        } catch (Throwable e) {
            XLog.e(e, "Check isVmJitInternal");
        }
        return false;
    }

}
