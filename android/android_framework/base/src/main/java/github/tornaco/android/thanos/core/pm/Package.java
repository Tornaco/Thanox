package github.tornaco.android.thanos.core.pm;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageParser;
import android.content.pm.Signature;
import android.os.Bundle;

import com.elvishew.xlog.XLog;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import github.tornaco.android.thanos.core.annotation.NonNull;
import github.tornaco.android.thanos.core.util.ArrayUtils;
import lombok.ToString;
import util.XposedHelpers;

/**
 * Representation of a full package parsed from APK files on disk. A package
 * consists of a single base APK, and zero or more split APKs.
 */
@ToString(exclude = {"activities", "receivers", "services", "providers"})
// Changed since Android11
// Ref: https://cs.android.com/android/platform/superproject/+/master:frameworks/base/core/java/android/content/pm/parsing/ParsingPackageImpl.java;l=238?q=ParsingPackageImpl&ss=android
@Deprecated
public class Package {

    public String packageName;

    // The package name declared in the manifest as the package can be
    // renamed, for example static shared libs use synthetic package names.
    public String manifestPackageName;

    /**
     * Names of any split APKs, ordered by parsed splitName
     */
    public String[] splitNames;

    @Ignore
    public String volumeUuid;

    /**
     * Path where this package was found on disk. For monolithic packages
     * this is path to single base APK file; for cluster packages this is
     * path to the cluster directory.
     */
    public String codePath;

    /**
     * Path of base APK
     */
    public String baseCodePath;
    /**
     * Paths of any split APKs, ordered by parsed splitName
     */
    public String[] splitCodePaths;

    /**
     * Revision code of base APK
     */
    public int baseRevisionCode;
    /**
     * Revision codes of any split APKs, ordered by parsed splitName
     */
    public int[] splitRevisionCodes;

    /**
     * Flags of any split APKs; ordered by parsed splitName
     */
    public int[] splitFlags;

    /**
     * Private flags of any split APKs; ordered by parsed splitName.
     */
    @Ignore
    public int[] splitPrivateFlags;

    public boolean baseHardwareAccelerated;

    // For now we only support one application per package.
    public ApplicationInfo applicationInfo;

    @Ignore
    public ArrayList<PackageParser.Permission> permissions;
    @Ignore
    public ArrayList<PackageParser.PermissionGroup> permissionGroups;

    public ArrayList<PackageParser.Activity> activities;
    public ArrayList<PackageParser.Activity> receivers;
    public ArrayList<PackageParser.Provider> providers;
    public ArrayList<PackageParser.Service> services;

    @Ignore
    public ArrayList<PackageParser.Instrumentation> instrumentation;

    public ArrayList<String> requestedPermissions;

    public ArrayList<String> protectedBroadcasts;

    // We store the application meta-data independently to avoid multiple unwanted references
    @Ignore
    public Bundle mAppMetaData;

    // The version code declared for this package.
    public int mVersionCode;

    // The version name declared for this package.
    public String mVersionName;

    // The shared user id that this package wants to use.
    public String mSharedUserId;

    // The shared user label that this package wants to use.
    public int mSharedUserLabel;

    // Signatures that were read from the package.
    @Ignore
    public Signature[] mSignatures;
    @Ignore
    public Certificate[][] mCertificates;

    // For use by package manager to keep track of when a package was last used.
    public long[] mLastPackageUsageTimeInMills =
            new long[android.content.pm.PackageManager.NOTIFY_PACKAGE_USE_REASONS_COUNT];

    public int installLocation;

    public boolean coreApp;

    public List<String> getAllCodePaths() {
        ArrayList<String> paths = new ArrayList<>();
        paths.add(baseCodePath);
        if (!ArrayUtils.isEmpty(splitCodePaths)) {
            Collections.addAll(paths, splitCodePaths);
        }
        return paths;
    }

    /**
     * Filtered set of {@link #getAllCodePaths()} that excludes
     * resource-only APKs.
     */
    public List<String> getAllCodePathsExcludingResourceOnly() {
        ArrayList<String> paths = new ArrayList<>();
        if ((applicationInfo.flags & ApplicationInfo.FLAG_HAS_CODE) != 0) {
            paths.add(baseCodePath);
        }
        if (!ArrayUtils.isEmpty(splitCodePaths)) {
            for (int i = 0; i < splitCodePaths.length; i++) {
                if ((splitFlags[i] & ApplicationInfo.FLAG_HAS_CODE) != 0) {
                    paths.add(splitCodePaths[i]);
                }
            }
        }
        return paths;
    }

    public boolean hasComponentClassName(String name) {
        for (int i = activities.size() - 1; i >= 0; i--) {
            if (name.equals(activities.get(i).className)) {
                return true;
            }
        }
        for (int i = receivers.size() - 1; i >= 0; i--) {
            if (name.equals(receivers.get(i).className)) {
                return true;
            }
        }
        for (int i = providers.size() - 1; i >= 0; i--) {
            if (name.equals(providers.get(i).className)) {
                return true;
            }
        }
        for (int i = services.size() - 1; i >= 0; i--) {
            if (name.equals(services.get(i).className)) {
                return true;
            }
        }
        for (int i = instrumentation.size() - 1; i >= 0; i--) {
            if (name.equals(instrumentation.get(i).className)) {
                return true;
            }
        }
        return false;
    }

    public boolean isForwardLocked() {
        return applicationInfo.isForwardLocked();
    }

    public boolean isSystemApp() {
        return applicationInfo.isSystemApp();
    }

    public boolean isPrivilegedApp() {
        return applicationInfo.isPrivilegedApp();
    }

    public boolean isUpdatedSystemApp() {
        return applicationInfo.isUpdatedSystemApp();
    }

    public boolean isMatch(int flags) {
        if ((flags & android.content.pm.PackageManager.MATCH_SYSTEM_ONLY) != 0) {
            return isSystemApp();
        }
        return true;
    }

    public long getLatestPackageUseTimeInMills() {
        long latestUse = 0L;
        for (long use : mLastPackageUsageTimeInMills) {
            latestUse = Math.max(latestUse, use);
        }
        return latestUse;
    }

    public long getLatestForegroundPackageUseTimeInMills() {
        int[] foregroundReasons = {
                android.content.pm.PackageManager.NOTIFY_PACKAGE_USE_ACTIVITY,
                PackageManager.NOTIFY_PACKAGE_USE_FOREGROUND_SERVICE
        };

        long latestUse = 0L;
        for (int reason : foregroundReasons) {
            latestUse = Math.max(latestUse, mLastPackageUsageTimeInMills[reason]);
        }
        return latestUse;
    }

    public static Package fromLegacy(@NonNull Object legacy) {
        Package res = new Package();
        Class<Package> packageClass = Package.class;
        for (Field field : packageClass.getDeclaredFields()) {
            try {
                if (field.isAnnotationPresent(Ignore.class)) {
                    continue;
                }
                String fieldName = field.getName();
                if (fieldName == null || fieldName.equals("_globalPatchRedirect")) {
                    continue;
                }
                Object fieldValueFromLegacy = XposedHelpers.getObjectField(legacy, fieldName);
                XLog.v("fieldValueFromLegacy: %s %s", fieldName, fieldValueFromLegacy);
                field.set(res, fieldValueFromLegacy);
            } catch (Throwable e) {
                XLog.e(e, "Fail retrieve field from legacy: " + field);
            }
        }
        XLog.v("Package fromLegacy: %s", res);
        return res;
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface Ignore {
    }
}
