package github.tornaco.android.nitro.framework.plugin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;

import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

@Setter
public class PluginContext extends SubDirContextThemeWrapper {

    private Resources mixResources;
    @Getter
    private Resources pluginResources;
    private ClassLoader pluginClassLoader;
    private LayoutInflater layoutInflater;

    public PluginContext() {
    }

    public PluginContext(Context base, int themeResId) {
        super(base, themeResId);
    }

    public PluginContext(Context base, Resources.Theme theme) {
        super(base, theme);
    }

    public final void attachHostBaseContext(Context context) {
        attachBaseContext(context);
    }

    @Override
    public Resources getResources() {
        if (mixResources == null) {
            Context baseContext = getBaseContext();
            Resources hostResources = baseContext.getResources();
            Log.w(getClass().getSimpleName(), "hostResources:" + hostResources);
            mixResources = new MixResources(hostResources, pluginResources);
        }
        return mixResources;
    }

    @Override
    public AssetManager getAssets() {
        if (pluginResources != null) {
            return pluginResources.getAssets();
        }
        return super.getAssets();
    }

    @Override
    public ClassLoader getClassLoader() {
        return pluginClassLoader;
    }

    @Override
    public Object getSystemService(String name) {
        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
            if (layoutInflater == null) {
                LayoutInflater inflater = (LayoutInflater) super.getSystemService(name);
                layoutInflater = Objects.requireNonNull(inflater).cloneInContext(this);
            }
            return layoutInflater;
        }
        return super.getSystemService(name);
    }

    @Override
    String getSubDirName() {
        return "NitroPlugin_" + getPackageName();
    }

    @Override
    public String getPackageName() {
        return super.getPackageName();
    }

    @Override
    public PackageManager getPackageManager() {
        return new PackageManagerWrapper(super.getPackageManager());
    }
}
