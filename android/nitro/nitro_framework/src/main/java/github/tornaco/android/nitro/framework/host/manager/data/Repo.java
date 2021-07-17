package github.tornaco.android.nitro.framework.host.manager.data;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import com.elvishew.xlog.XLog;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.nitro.framework.host.manager.data.model.InstalledPlugin;
import github.tornaco.android.nitro.framework.host.manager.data.source.local.InstalledPluginDatabase;
import util.CollectionUtils;

public final class Repo {

    private Repo() {
    }

    @WorkerThread
    public static List<InstalledPlugin> getAllInstalledPlugin(Context context) {
        try {
            List<InstalledPlugin> plugins = InstalledPluginDatabase.getInstance(context).installedPluginDao().loadAll();
            if (CollectionUtils.isNullOrEmpty(plugins)) return new ArrayList<>(0);
            for (InstalledPlugin plugin : plugins) {
                plugin.setPluginApplicationInfo(InstalledPluginDatabase.getInstance(context)
                        .pluginAppDao()
                        .loadByPackageName(plugin.getPackageName()));
                plugin.setPluginActivityInfoList(InstalledPluginDatabase.getInstance(context)
                        .pluginActivityDao()
                        .loadByPackageName(plugin.getPackageName()));
                plugin.setActivityIntentFilterList(InstalledPluginDatabase.getInstance(context)
                        .intentFilterDao()
                        .loadByPackageName(plugin.getPackageName()));
            }
            return plugins;
        } catch (Throwable e) {
            XLog.e("Error getAllInstalledPlugin", e);
            return new ArrayList<>(0);
        }
    }

    @Nullable
    public static InstalledPlugin loadByPackageName(Context context, String pkg) {
        try {
            InstalledPlugin plugin = InstalledPluginDatabase
                    .getInstance(context)
                    .installedPluginDao()
                    .loadByPackageName(pkg);
            if (plugin == null) return null;
            plugin.setPluginApplicationInfo(InstalledPluginDatabase.getInstance(context)
                    .pluginAppDao()
                    .loadByPackageName(plugin.getPackageName()));
            plugin.setPluginActivityInfoList(InstalledPluginDatabase.getInstance(context)
                    .pluginActivityDao()
                    .loadByPackageName(plugin.getPackageName()));
            plugin.setActivityIntentFilterList(InstalledPluginDatabase.getInstance(context)
                    .intentFilterDao()
                    .loadByPackageName(plugin.getPackageName()));
            return plugin;
        } catch (Throwable e) {
            XLog.e("Error loadByPackageName", e);
            return null;
        }
    }

    @WorkerThread
    public static void addInstalledPlugin(Context context, InstalledPlugin plugin) {
        try {
            InstalledPluginDatabase.getInstance(context).installedPluginDao().insert(plugin);
            InstalledPluginDatabase.getInstance(context).pluginAppDao().insert(plugin.getPluginApplicationInfo());
            InstalledPluginDatabase.getInstance(context).pluginActivityDao().insert(plugin.getPluginActivityInfoList());
            InstalledPluginDatabase.getInstance(context).intentFilterDao().insert(plugin.getActivityIntentFilterList());
        } catch (Throwable e) {
            XLog.e("Error addInstalledPlugin", e);
        }
    }

    @WorkerThread
    public static void deleteInstalledPlugin(Context context, InstalledPlugin plugin) {
        try {
            InstalledPluginDatabase.getInstance(context).installedPluginDao().delete(plugin);
        } catch (Throwable e) {
            XLog.e("Error deleteInstalledPlugin", e);
        }
    }
}
