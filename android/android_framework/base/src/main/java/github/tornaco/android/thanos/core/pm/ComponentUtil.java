package github.tornaco.android.thanos.core.pm;

import static android.content.pm.PackageManager.GET_ACTIVITIES;
import static android.content.pm.PackageManager.GET_PROVIDERS;
import static android.content.pm.PackageManager.GET_RECEIVERS;
import static android.content.pm.PackageManager.GET_SERVICES;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.util.Log;

import com.elvishew.xlog.XLog;
import com.google.common.collect.Lists;

import java.util.ArrayList;

import github.tornaco.android.thanos.core.annotation.NonNull;

/**
 * Created by guohao4 on 2017/11/17.
 * Email: Tornaco@163.com
 */

public class ComponentUtil {

    /**
     * Those that has not service and broadcast and no activity.
     */
    public static boolean isGreenPackage(PackageInfo packageInfo) {
        return (packageInfo.receivers == null || packageInfo.receivers.length == 0)
                && (packageInfo.services == null || packageInfo.services.length == 0)
                && (packageInfo.activities == null || packageInfo.activities.length == 0);
    }

    @NonNull
    public static ArrayList<ServiceInfo> getServices(Context context, String pkg) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                packageInfo = pm.getPackageInfo(pkg, GET_SERVICES | PackageManager.MATCH_DISABLED_COMPONENTS);
            } else {
                packageInfo = pm.getPackageInfo(pkg, GET_SERVICES | PackageManager.GET_DISABLED_COMPONENTS);
            }
            if (packageInfo == null) return Lists.newArrayListWithCapacity(0);
            ServiceInfo[] serviceInfoArray = packageInfo.services;
            if (serviceInfoArray == null || serviceInfoArray.length == 0)
                return Lists.newArrayListWithCapacity(0);
            return Lists.newArrayList(serviceInfoArray);
        } catch (Exception e) {
            XLog.e("getServices: " + Log.getStackTraceString(e));
            return Lists.newArrayListWithCapacity(0);
        }
    }

    @NonNull
    public static ArrayList<ActivityInfo> getActivities(Context context, String pkg) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                packageInfo = pm.getPackageInfo(pkg, GET_ACTIVITIES | PackageManager.MATCH_DISABLED_COMPONENTS);
            } else {
                packageInfo = pm.getPackageInfo(pkg, GET_ACTIVITIES | PackageManager.GET_DISABLED_COMPONENTS);
            }
            if (packageInfo == null) return Lists.newArrayListWithCapacity(0);
            ActivityInfo[] activityInfos = packageInfo.activities;
            if (activityInfos == null || activityInfos.length == 0)
                return Lists.newArrayListWithCapacity(0);
            return Lists.newArrayList(activityInfos);
        } catch (Exception e) {
            XLog.e("getActivities: " + Log.getStackTraceString(e));
            return Lists.newArrayListWithCapacity(0);
        }
    }

    @NonNull
    public static ArrayList<ActivityInfo> getBroadcasts(Context context, String pkg) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                packageInfo = pm.getPackageInfo(pkg, GET_RECEIVERS | PackageManager.MATCH_DISABLED_COMPONENTS);
            } else {
                packageInfo = pm.getPackageInfo(pkg, GET_RECEIVERS | PackageManager.GET_DISABLED_COMPONENTS);
            }
            if (packageInfo == null) return Lists.newArrayListWithCapacity(0);
            ActivityInfo[] activityInfos = packageInfo.receivers;
            if (activityInfos == null || activityInfos.length == 0)
                return Lists.newArrayListWithCapacity(0);
            return Lists.newArrayList(activityInfos);
        } catch (Exception e) {
            XLog.e("getBroadcasts: " + Log.getStackTraceString(e));
            return Lists.newArrayListWithCapacity(0);
        }
    }

    @NonNull
    public static ArrayList<ProviderInfo> getProviders(Context context, String pkg) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                packageInfo = pm.getPackageInfo(pkg, GET_PROVIDERS | PackageManager.MATCH_DISABLED_COMPONENTS);
            } else {
                packageInfo = pm.getPackageInfo(pkg, GET_PROVIDERS | PackageManager.GET_DISABLED_COMPONENTS);
            }
            if (packageInfo == null) return Lists.newArrayListWithCapacity(0);
            ProviderInfo[] providerInfos = packageInfo.providers;
            if (providerInfos == null || providerInfos.length == 0)
                return Lists.newArrayListWithCapacity(0);
            return Lists.newArrayList(providerInfos);
        } catch (Exception e) {
            XLog.e("getProviders: " + Log.getStackTraceString(e));
            return Lists.newArrayListWithCapacity(0);
        }
    }

    public static ComponentName getComponentName(ActivityInfo activityInfo) {
        return new ComponentName(activityInfo.packageName, activityInfo.name);
    }

    public static ComponentName getComponentName(ServiceInfo serviceInfo) {
        return new ComponentName(serviceInfo.packageName, serviceInfo.name);
    }

    public static boolean isComponentDisabled(int enableSetting) {
        return enableSetting != PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                && enableSetting != PackageManager.COMPONENT_ENABLED_STATE_DEFAULT;
    }

    public static boolean isComponentEnabled(int enableSetting) {
        return !isComponentDisabled(enableSetting);
    }
}
