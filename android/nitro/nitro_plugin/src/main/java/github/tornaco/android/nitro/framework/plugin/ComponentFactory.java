package github.tornaco.android.nitro.framework.plugin;

import android.content.ComponentName;
import android.util.Log;

import java.io.Serializable;
import java.util.Objects;

public class ComponentFactory implements Serializable {

    private static final String TAG = "ComponentFactory";

    public static PluginActivity newPluginActivity(ComponentName componentName)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        ClassLoader classLoader = ComponentFactory.class.getClassLoader();
        Log.w(TAG, "newPluginActivity, calling classloader: " + classLoader);
        Class<?> pluginClazz = Objects.requireNonNull(classLoader)
                .loadClass(componentName.getClassName());
        PluginActivity pluginActivity = (PluginActivity) pluginClazz.newInstance();
        Log.w(TAG, "newPluginActivity, pluginActivity instance: " + pluginActivity);
        Log.w(TAG, "newPluginActivity, pluginActivity try to call method: " + pluginActivity.getPluginResources());
        return pluginActivity;
    }
}
