package github.tornaco.android.nitro.framework.host;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.elvishew.xlog.XLog;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.Callable;

import github.tornaco.android.nitro.framework.Nitro;
import github.tornaco.android.nitro.framework.host.loader.LoadedPlugin;
import github.tornaco.android.nitro.framework.host.loader.Loader;
import github.tornaco.android.nitro.framework.host.manager.data.model.InstalledPlugin;
import github.tornaco.android.nitro.framework.host.manager.data.model.PluginActivityInfo;
import github.tornaco.android.nitro.framework.plugin.HostActivityInvoker;
import github.tornaco.android.nitro.framework.plugin.wrapper.PluginActivityWrapper;
import github.tornaco.android.thanos.core.util.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import util.ReflectionUtils;

public class Launcher {

    private static final String EXTRA_PLUGIN_PKG_NAME = "github.tornaco.android.nitro.framework.host.launcher.extra.plugin.pkg";
    private static final String EXTRA_COMPONENT_NAME = "github.tornaco.android.nitro.framework.host.launcher.extra.component";

    public static boolean launchMainActivity(@NonNull Context context,
                                             @NonNull InstalledPlugin plugin) {
        Intent intent = Launcher.newActivityLaunchIntent(
                Objects.requireNonNull(context),
                plugin,
                new ComponentName(plugin.getPackageName(), plugin.getMainActivityName()));
        context.startActivity(intent);
        return true;
    }

    @NonNull
    public static Intent newActivityLaunchIntent(@NonNull Context context,
                                                 @NonNull InstalledPlugin plugin,
                                                 @NonNull ComponentName componentName) {
        Intent intent = new Intent(Preconditions.checkNotNull(context), ContainerActivity.class);
        intent.putExtra(EXTRA_PLUGIN_PKG_NAME, Preconditions.checkNotNull(plugin).getPackageName());
        intent.putExtra(EXTRA_COMPONENT_NAME, Preconditions.checkNotNull(componentName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Nullable
    private static Request parseLaunchIntent(@NonNull Context context, @NonNull Intent intent) {
        if (!Preconditions.checkNotNull(intent).hasExtra(EXTRA_PLUGIN_PKG_NAME)) return null;
        if (!intent.hasExtra(EXTRA_COMPONENT_NAME)) return null;
        String pluginPkgName = intent.getStringExtra(EXTRA_PLUGIN_PKG_NAME);
        InstalledPlugin installedPlugin = Nitro.getInstalledPlugin(context, pluginPkgName);
        XLog.v("parseLaunchIntent, installedPlugin: %s", installedPlugin);
        if (installedPlugin == null) return null;
        ComponentName componentName = intent.getParcelableExtra(EXTRA_COMPONENT_NAME);
        return new Request(componentName, installedPlugin);
    }

    @Nullable
    public static PluginActivityWrapper loadPluginActivityInstance(HostActivityInvoker hostActivityInvoker) {
        Request request = parseLaunchIntent(hostActivityInvoker.getHostActivity(), hostActivityInvoker.getIntent());
        XLog.d("loadPluginActivityInstance, request: %s", request);
        if (request == null) return null;
        ActivityInfo activityInfo = request.getActivityInfo();
        XLog.d("loadPluginActivityInstance, activityInfo: %s", activityInfo);
        if (activityInfo == null) return null;
        try {
            LoadedPlugin loadedPlugin = Loader.INS.loadPlugin(
                    hostActivityInvoker.getHostActivity(),
                    hostActivityInvoker.getClassLoader(),
                    request.plugin.getApkFile(),
                    request.plugin.getPackageName());
            XLog.v("loadedPlugin: %s", loadedPlugin);
            ClassLoader pluginClassLoader = loadedPlugin.getPluginClassLoader();
            Class<?> factoryClazz = pluginClassLoader.loadClass("github.tornaco.android.nitro.framework.plugin.ComponentFactory");
            XLog.v("factoryClazz: %s", factoryClazz);
            Method newPluginActivity = factoryClazz.getDeclaredMethod("newPluginActivity", ComponentName.class);
            Object pluginActivity = ReflectionUtils.invokeMethod(newPluginActivity, null, request.componentName);
            XLog.v("pluginActivity: %s", pluginActivity);
            PluginActivityWrapper activityWrapper = new PluginActivityWrapper(pluginActivity);
            activityWrapper.setThemeResource(activityInfo.theme);
            activityWrapper.setPluginClassLoader(pluginClassLoader);
            activityWrapper.setPluginResources(loadedPlugin.getPluginRes());
            activityWrapper.setHostActivityInvoker(hostActivityInvoker);
            return activityWrapper;
        } catch (Exception e) {
            XLog.e("loadPluginActivityInstance", e);
            return null;
        }
    }

    public static int invokePluginStatus(Context hostContext, InstalledPlugin plugin, int def) {
        Callable<Integer> callable = loadPluginStatusCallable(hostContext, plugin);
        if (callable == null) {
            return def;
        }
        try {
            return callable.call();
        } catch (Exception e) {
            XLog.e(Log.getStackTraceString(e));
            return def;
        }
    }

    public static Callable<Integer> loadPluginStatusCallable(Context hostContext, InstalledPlugin plugin) {
        String clazz = plugin.getStatusCallableClass();
        if (TextUtils.isEmpty(clazz)) {
            return null;
        }
        try {
            LoadedPlugin loadedPlugin = Loader.INS.loadPlugin(
                    hostContext,
                    hostContext.getClassLoader(),
                    plugin.getApkFile(),
                    plugin.getPackageName());
            XLog.v("loadedPlugin: %s", loadedPlugin);
            ClassLoader pluginClassLoader = loadedPlugin.getPluginClassLoader();
            Class<?> callerClazz = pluginClassLoader.loadClass(clazz);
            XLog.v("callerClazz: %s", callerClazz);
            @SuppressWarnings("unchecked")
            Callable<Integer> caller = (Callable<Integer>) callerClazz.newInstance();
            return caller;
        } catch (Exception e) {
            XLog.e("loadPluginStatusCallable", e);
            return null;
        }
    }

    @AllArgsConstructor
    @Getter
    @ToString
    public static class Request {
        private ComponentName componentName;
        private InstalledPlugin plugin;

        @Nullable
        public ActivityInfo getActivityInfo() {
            if (plugin == null || componentName == null) return null;
            if (plugin.getPluginActivityInfoList() == null) return null;
            for (PluginActivityInfo activityInfo : plugin.getPluginActivityInfoList()) {
                if (activityInfo.name.equals(componentName.getClassName())) {
                    return activityInfo.activityInfo;
                }
            }
            return null;
        }
    }
}
