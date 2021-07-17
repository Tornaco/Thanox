package github.tornaco.android.nitro.framework.host.install;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import com.elvishew.xlog.XLog;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.nitro.framework.host.install.util.DexUtils;
import github.tornaco.android.nitro.framework.host.install.util.parser.manifest.IntentFilters;
import github.tornaco.android.nitro.framework.host.loader.Loader;
import github.tornaco.android.nitro.framework.host.manager.data.Repo;
import github.tornaco.android.nitro.framework.host.manager.data.model.ActivityIntentFilter;
import github.tornaco.android.nitro.framework.host.manager.data.model.InstalledPlugin;
import github.tornaco.android.nitro.framework.host.manager.data.model.PluginActivityInfo;
import github.tornaco.android.nitro.framework.host.manager.data.model.PluginApplicationInfo;
import github.tornaco.android.nitro.framework.plugin.PluginFile;
import github.tornaco.android.thanos.core.util.ArrayUtils;
import github.tornaco.android.thanos.core.util.FileUtils;
import github.tornaco.android.thanos.core.util.PkgUtils;
import github.tornaco.android.thanos.core.util.Preconditions;
import util.CollectionUtils;
import util.IoUtils;
import util.ObjectsUtils;

@SuppressWarnings("UnstableApiUsage")
public class Installer {
    private static final String EXPECTED_PLUGIN_FILE_EXT = "tp";

    private static File pluginInstallDirForPackage(Context context, String pluginPkgName) {
        return new File(context.getFilesDir(), "plugins/" + pluginPkgName);
    }

    private static File pluginSourceDirForPackage(Context context, String pluginPkgName) {
        return new File(pluginInstallDirForPackage(context, pluginPkgName), "source");
    }

    private static File pluginApkFileForPackage(Context context, String pluginPkgName, long versionCode) {
        return new File(pluginSourceDirForPackage(context, pluginPkgName), pluginPkgName + "_" + versionCode + ".apk");
    }

    private static File pluginDexFileForPackage(Context context, String pluginPkgName, long versionCode) {
        return new File(pluginSourceDirForPackage(context, pluginPkgName), pluginPkgName + "_" + versionCode + ".dex");
    }

    @WorkerThread
    public void install(Context context, Uri uri, @NonNull InstallCallback callback) {
        File temp = new File(context.getExternalCacheDir(),
                String.format("temp_%s.%s", System.currentTimeMillis(), EXPECTED_PLUGIN_FILE_EXT));

        // Prepare plugin file.
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            if (inputStream == null) {
                XLog.e("Input stream is null for uri: %s", uri);
                callback.onInstallFail(ErrorCodes.PLUGIN_FILE_PARSE_ERROR_GENERIC_FAILURE, new IOException("Input stream is null"));
                return;
            }
            Files.createParentDirs(temp);
            Files.asByteSink(temp).writeFrom(inputStream);
            IoUtils.closeQuietly(inputStream);
        } catch (IOException e) {
            XLog.e("Copy to temp", e);
            callback.onInstallFail(ErrorCodes.IO_ERROR, new IOException(e));
            return;
        }

        try {
            install(context, temp, callback);
        } finally {
            FileUtils.delete(temp);
        }
    }

    @WorkerThread
    public void install(@NonNull Context context, @NonNull File apkFile, @NonNull InstallCallback callback) {
        PluginFile info = parse(context, apkFile, callback);
        if (info == null) return;
        install(context, info, callback);
    }

    @Nullable
    private PluginFile parse(@NonNull Context context, @NonNull File pluginFile, @NonNull InstallCallback callback) {
        XLog.i("inflate: %s", pluginFile);

        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(pluginFile);
        Preconditions.checkState(pluginFile.exists());

        if (!ObjectsUtils.equals(EXPECTED_PLUGIN_FILE_EXT, Files.getFileExtension(pluginFile.getPath()))) {
            XLog.e("Not thanox plugin file: %s", Files.getFileExtension(pluginFile.getPath()));
            callback.onInstallFail(ErrorCodes.PLUGIN_FILE_PARSE_ERROR_NOT_A_PLUGIN, new IllegalArgumentException("Not a plugin file"));
            return null;
        }

        PackageInfo info = PkgUtils.getPackageInfo(context, pluginFile.getPath(), false);
        if (info == null) {
            callback.onInstallFail(ErrorCodes.PLUGIN_FILE_PARSE_ERROR_GENERIC_FAILURE, new IllegalArgumentException("Fail resolve package info"));
            XLog.e("Fail resolve package info: %s", pluginFile);
            return null;
        }

        boolean dup = Repo.loadByPackageName(context, info.packageName) != null;
        if (dup) {
            callback.onInstallFail(ErrorCodes.DUP, new IllegalArgumentException("Already installed"));
            return null;
        }

        File destApkFile = pluginApkFileForPackage(context, info.packageName, info.versionCode);
        return PluginFile.builder()
                .apkFile(destApkFile.getPath())
                .dexFile(pluginDexFileForPackage(context, info.packageName, info.versionCode).getPath())
                .libFile(null)
                .sourceDir(pluginSourceDirForPackage(context, info.packageName).getPath())
                .originFile(pluginFile.getPath())
                .packageName(info.packageName)
                .name(info.packageName)
                .versionCode(info.versionCode)
                .versionName(info.versionName)
                .packageInfo(info)
                .build();
    }

    private void install(@NonNull Context context,
                         @NonNull PluginFile pluginFile,
                         @NonNull InstallCallback callback) {
        long start = System.currentTimeMillis();

        // Parse comps.
        ComponentList componentList = new ComponentList(
                pluginFile.getPackageInfo(),
                pluginFile.getOriginFile(),
                pluginFile);

        ApplicationInfo applicationInfo = componentList.getApplication();
        String mainActivityName = componentList.getMainActivityName();
        String label = String.valueOf(applicationInfo.loadLabel(context.getPackageManager()));
        String description = componentList.getDescription();
        int minRequiredVersion = componentList.getRequiredMinThanoxVersion();
        boolean stable = componentList.getStable();
        boolean withHooks = componentList.withHooks();
        String statusCallableClass = componentList.statusCallableClass();
        XLog.w("applicationInfo: %s", applicationInfo);
        XLog.w("mainActivityName: %s", mainActivityName);
        XLog.w("label: %s", label);
        XLog.w("description: %s", description);
        XLog.w("minRequiredVersion: %s", minRequiredVersion);
        XLog.w("stable: %s", stable);
        XLog.w("withHooks: %s", withHooks);
        XLog.w("statusCallableClass: %s", statusCallableClass);

        try {
            Files.createParentDirs(new File(pluginFile.getApkFile()));
            Files.asByteSource(new File(pluginFile.getOriginFile()))
                    .copyTo(Files.asByteSink(new File(pluginFile.getApkFile())));
        } catch (IOException e) {
            XLog.e("Write destApkFile", e);
            callback.onInstallFail(ErrorCodes.FILE_PERSIST_ERROR, new IOException(e));
            return;
        }

        boolean opt = DexUtils.optimize(context, pluginFile);
        XLog.w("install, opt res =%s", opt);
        long taken = System.currentTimeMillis() - start;
        XLog.w("install taken %sms", taken);

        List<PluginActivityInfo> pluginActivityInfoList = new ArrayList<>();
        ActivityInfo[] activityInfos = componentList.getActivities();
        if (!ArrayUtils.isEmpty(activityInfos)) {
            for (ActivityInfo activityInfo : activityInfos) {
                PluginActivityInfo pluginActivityInfo = PluginActivityInfo.builder()
                        .activityInfo(activityInfo)
                        .name(activityInfo.name)
                        .pluginPackageName(pluginFile.getPackageName())
                        .build();
                XLog.v("Add pluginActivityInfo: %s", pluginActivityInfo);
                pluginActivityInfoList.add(pluginActivityInfo);
            }
        }

        PluginApplicationInfo pluginApplicationInfo = PluginApplicationInfo.builder()
                .applicationInfo(componentList.getApplication())
                .pluginPackageName(pluginFile.getPackageName())
                .build();
        XLog.v("Add pluginApplicationInfo: %s", pluginApplicationInfo);

        List<ActivityIntentFilter> activityIntentFilterList = new ArrayList<>();
        IntentFilters intentFilter = componentList.getIntentFilters();
        for (String name : intentFilter.getActivityInfoMap().keySet()) {
            List<IntentFilter> intentFilters = intentFilter.getActivityInfoMap().get(name);
            if (!CollectionUtils.isNullOrEmpty(intentFilters)) {
                for (IntentFilter filter : intentFilters) {
                    ActivityIntentFilter activityIntentFilter = ActivityIntentFilter
                            .builder()
                            .intentFilter(filter)
                            .name(name)
                            .pluginPackageName(pluginFile.getPackageName())
                            .build();
                    activityIntentFilterList.add(activityIntentFilter);
                    XLog.v("Add activityIntentFilter: %s", activityIntentFilter);
                }
            }
        }

        InstalledPlugin plugin = InstalledPlugin.builder()
                .apkFile(pluginFile.getApkFile())
                .dexFile(pluginFile.getDexFile())
                .libFile(pluginFile.getLibFile())
                .originFile(pluginFile.getOriginFile())
                .sourceDir(pluginFile.getSourceDir())
                .packageName(pluginFile.getPackageName())
                .name(pluginFile.getName())
                .label(label)
                .description(description)
                .minRequiredVersion(minRequiredVersion)
                .versionCode(pluginFile.getVersionCode())
                .versionName(pluginFile.getVersionName())
                .mainActivityName(mainActivityName)
                .activityIntentFilterList(activityIntentFilterList)
                .pluginActivityInfoList(pluginActivityInfoList)
                .pluginApplicationInfo(pluginApplicationInfo)
                .statusCallableClass(statusCallableClass)
                .withHooks(withHooks)
                .build();

        // Persist.
        Repo.addInstalledPlugin(context, plugin);
        callback.onInstallSuccess(plugin);
    }

    @WorkerThread
    public void uninstall(@NonNull Context context, @NonNull InstalledPlugin plugin, @NonNull UnInstallCallback callback) {
        Repo.deleteInstalledPlugin(context, plugin);
        FileUtils.delete(new File(plugin.getSourceDir()));
        Loader.INS.unLoadPlugin(plugin.getPackageName());
        callback.onUnInstallSuccess(plugin);
    }

    public interface ErrorCodes {
        int PLUGIN_FILE_PARSE_ERROR_NOT_A_PLUGIN = 0x100;
        int PLUGIN_FILE_PARSE_ERROR_GENERIC_FAILURE = 0x101;
        int DB_PERSIST_ERROR = 0x102;
        int FILE_PERSIST_ERROR = 0x103;
        int IO_ERROR = 0x104;
        int DUP = 0x105;
    }
}
