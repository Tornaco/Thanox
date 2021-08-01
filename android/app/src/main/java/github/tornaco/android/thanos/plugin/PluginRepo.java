package github.tornaco.android.thanos.plugin;

import android.content.Context;

import androidx.annotation.NonNull;

import com.elvishew.xlog.XLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import util.AssetUtils;
import util.Consumer;

public class PluginRepo {

    @NonNull
    public static List<PluginResponse> getAvailablePlugins(Consumer<Throwable> onError) {
        try {
            return PluginService.Factory.create()
                    .getAvailablePlugins()
                    .toFuture().get();
        } catch (Throwable e) {
            XLog.e("getAvailablePlugins, error", e);
            onError.accept(e);
            return new ArrayList<>(0);
        }
    }

    @NonNull
    public static List<PluginResponse> getPrebuiltPlugins(Context context, Consumer<Throwable> onError) {
        try {
            String pluginJsonString = AssetUtils.readFileToString(context, "plugins/plugins.json");
            return new Gson().fromJson(pluginJsonString, new TypeToken<List<PluginResponse>>() {
            }.getType());
        } catch (Throwable e) {
            XLog.e("getPrebuiltPlugins, error", e);
            onError.accept(e);
            return new ArrayList<>(0);
        }
    }
}
