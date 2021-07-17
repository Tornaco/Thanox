package github.tornaco.android.nitro.framework.host.loader.boost;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import github.tornaco.android.nitro.framework.plugin.PluginClassLoader;

public class ClassLoaderCache {

    private final Map<String, PluginClassLoader> sMap = new HashMap<>();

    public PluginClassLoader remove(String key) {
        return sMap.remove(key);
    }

    public void put(String key, PluginClassLoader classLoader) {
        sMap.put(key, classLoader);
    }

    @Nullable
    public PluginClassLoader get(String key) {
        return sMap.get(key);
    }
}
