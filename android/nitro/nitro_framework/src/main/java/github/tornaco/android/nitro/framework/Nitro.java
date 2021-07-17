package github.tornaco.android.nitro.framework;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import github.tornaco.android.nitro.framework.host.Launcher;
import github.tornaco.android.nitro.framework.host.install.InstallCallback;
import github.tornaco.android.nitro.framework.host.install.Installer;
import github.tornaco.android.nitro.framework.host.install.UnInstallCallback;
import github.tornaco.android.nitro.framework.host.manager.data.Repo;
import github.tornaco.android.nitro.framework.host.manager.data.model.InstalledPlugin;

public class Nitro {
    private static final Installer INSTALLER = new Installer();
    private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();

    @MainThread
    public static void install(@NonNull Context context, @NonNull File apkFile, @NonNull InstallCallback callback) {
        EXECUTOR.execute(() -> INSTALLER.install(context, apkFile, callback));
    }

    @MainThread
    public static void install(@NonNull Context context, @NonNull Uri uri, @NonNull InstallCallback callback) {
        EXECUTOR.execute(() -> INSTALLER.install(context, uri, callback));
    }

    @MainThread
    public static void unInstall(@NonNull Context context, @NonNull InstalledPlugin plugin, @NonNull UnInstallCallback callback) {
        EXECUTOR.execute(() -> INSTALLER.uninstall(context, plugin, callback));
    }

    @WorkerThread
    public static List<InstalledPlugin> getAllInstalledPlugin(Context context) {
        return Repo.getAllInstalledPlugin(context);
    }

    @WorkerThread
    public static InstalledPlugin getInstalledPlugin(Context context, String pkgName) {
        return Repo.loadByPackageName(context, pkgName);
    }

    @WorkerThread
    public static boolean isPluginInstalled(Context context, String pkgName) {
        return Repo.loadByPackageName(context, pkgName) != null;
    }

    public static boolean launchMainActivity(@NonNull Context context,
                                             @NonNull InstalledPlugin plugin) {
        return Launcher.launchMainActivity(context, plugin);
    }

    public static int invokePluginStatus(Context hostContext, InstalledPlugin plugin, int def) {
        return Launcher.invokePluginStatus(hostContext, plugin, def);
    }
}
