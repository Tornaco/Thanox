package github.tornaco.android.nitro.framework.host.loader;

import android.content.res.Resources;

import github.tornaco.android.nitro.framework.plugin.PluginClassLoader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@AllArgsConstructor
@ToString
public class LoadedPlugin {
    private PluginClassLoader pluginClassLoader;
    private Resources pluginRes;
    private String packageName;
}
