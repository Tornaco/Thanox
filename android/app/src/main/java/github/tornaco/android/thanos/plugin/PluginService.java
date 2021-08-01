package github.tornaco.android.thanos.plugin;

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
