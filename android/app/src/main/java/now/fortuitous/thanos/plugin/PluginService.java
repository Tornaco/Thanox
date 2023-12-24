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

import java.util.List;

import github.tornaco.android.thanos.BuildProp;
import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface PluginService {

    String API_URL = BuildProp.THANOX_SERVER_GITHUB_BASE_URL;

    @GET("plugins/available_plugins")
    Observable<List<PluginResponse>> getAvailablePlugins();

    class Factory {
        private static PluginService service;

        public synchronized static PluginService create() {
            if (service == null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(API_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(new OkHttpClient.Builder().build())
                        .build();
                service = retrofit.create(PluginService.class);
            }
            return service;
        }
    }
}
