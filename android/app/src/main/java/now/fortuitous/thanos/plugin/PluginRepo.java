/*
 * (C) Copyright 2022 Thanox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package now.fortuitous.thanos.plugin;

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
