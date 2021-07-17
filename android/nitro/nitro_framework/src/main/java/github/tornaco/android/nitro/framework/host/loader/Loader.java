package github.tornaco.android.nitro.framework.host.loader;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import androidx.annotation.NonNull;

import com.elvishew.xlog.XLog;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import github.tornaco.android.nitro.framework.host.loader.boost.ClassLoaderCache;
import github.tornaco.android.nitro.framework.plugin.PluginClassLoader;

import static android.content.pm.PackageManager.GET_META_DATA;
import static java.io.File.separator;

@SuppressWarnings("UnstableApiUsage")
public enum Loader {
    INS;

    private final ClassLoaderCache classLoaderCache = new ClassLoaderCache();

    @NonNull
    public LoadedPlugin loadPlugin(Context context, ClassLoader hostClassLoader, String apkPath, String packageName) {
        return LoadedPlugin.builder()
                .pluginClassLoader(getOrCreatePluginClassLoader(context, hostClassLoader, apkPath, packageName))
                .pluginRes(createResources(context, apkPath))
                .packageName(packageName)
                .build();
    }

    public void unLoadPlugin(String packageName) {
        classLoaderCache.remove(packageName);
    }

    @NonNull
    private PluginClassLoader getOrCreatePluginClassLoader(Context context, ClassLoader hostClassLoader, String apkPath, String packageName) {
        PluginClassLoader cached = classLoaderCache.get(packageName);
        if (cached != null) return cached;
        PluginClassLoader created = createPluginClassLoader(context, hostClassLoader, apkPath);
        classLoaderCache.put(packageName, created);
        return created;
    }

    @NonNull
    private PluginClassLoader createPluginClassLoader(Context context, ClassLoader hostClassLoader, String apkPath) {
        String optimizedDirectory = context.getCacheDir().getPath() + separator + "dex";
        // Create dir.
        try {
            Files.createParentDirs(new File(new File(optimizedDirectory), "dummy"));
        } catch (IOException e) {
            XLog.e("createParentDirs for optimizedDirectory", e);
        }
        return new PluginClassLoader(
                hostClassLoader,
                apkPath,
                new File(optimizedDirectory),
                null);
    }

    @NonNull
    private Resources createResources(Context baseContext, String apkPath) {
        PackageManager packageManager = baseContext.getPackageManager();
        PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(apkPath, GET_META_DATA);
        Objects.requireNonNull(packageArchiveInfo).applicationInfo.publicSourceDir = apkPath;
        packageArchiveInfo.applicationInfo.sourceDir = apkPath;
        try {
            return packageManager.getResourcesForApplication(packageArchiveInfo.applicationInfo);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
