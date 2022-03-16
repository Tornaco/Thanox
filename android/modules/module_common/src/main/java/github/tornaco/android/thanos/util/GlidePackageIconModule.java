package github.tornaco.android.thanos.util;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.signature.ObjectKey;
import com.elvishew.xlog.XLog;

import github.tornaco.android.thanos.core.pm.AppInfo;
import util.Singleton2;

/**
 * Created by guohao4 on 2017/12/23.
 * Email: Tornaco@163.com
 */
@GlideModule
public class GlidePackageIconModule extends AppGlideModule {

    @SuppressWarnings("NullableProblems")
    private static class PackageInfoDataFetcher implements DataFetcher<Bitmap> {

        private final AppInfo info;
        private final Context context;

        public PackageInfoDataFetcher(AppInfo info, Context context) {
            this.info = info;
            this.context = context;
        }

        @Override
        public void loadData(Priority priority, DataCallback<? super Bitmap> callback) {
            XLog.d("loadData: " + info.getPkgName() + "@" + Thread.currentThread());
            if (info.getPkgName() == null) {
                callback.onLoadFailed(new NullPointerException("Package name is null"));
                return;
            }
            Bitmap bd = AppIconLoaderUtil.loadAppIconBitmapWithIconPack(context, getInfo().getPkgName(), getInfo().getUid());
            callback.onDataReady(bd);
        }

        @Override
        public void cleanup() {

        }

        @Override
        public void cancel() {

        }

        @NonNull
        @Override
        public Class<Bitmap> getDataClass() {
            return Bitmap.class;
        }

        @NonNull
        @Override
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }

        public AppInfo getInfo() {
            return this.info;
        }
    }

    private static class PackageIconModuleLoaderFactory
            implements ModelLoaderFactory<AppInfo, Bitmap> {
        private final Context context;

        private static final Singleton2<Context, ModelLoader<AppInfo, Bitmap>> sLoader
                = new Singleton2<Context, ModelLoader<AppInfo, Bitmap>>() {
            @Override
            protected ModelLoader<AppInfo, Bitmap> create(Context context) {
                return new ModelLoader<AppInfo, Bitmap>() {
                    @NonNull
                    @Override
                    public LoadData<Bitmap> buildLoadData(AppInfo info, int width,
                                                          int height, Options options) {
                        Key diskCacheKey = new ObjectKey(info.getPkgName() + "-" + info.getUid() + "-" + info.getVersionCode());

                        return new LoadData<>(diskCacheKey, new PackageInfoDataFetcher(info, context));
                    }

                    @Override
                    public boolean handles(AppInfo info) {
                        return info.getPkgName() != null;
                    }
                };
            }
        };

        public PackageIconModuleLoaderFactory(Context context) {
            this.context = context;
        }


        private static ModelLoader<AppInfo, Bitmap> singleInstanceLoader(Context context) {
            return sLoader.get(context);
        }

        @Override
        public ModelLoader<AppInfo, Bitmap> build(MultiModelLoaderFactory multiFactory) {
            return singleInstanceLoader(context);
        }

        @Override
        public void teardown() {

        }
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        super.registerComponents(context, glide, registry);
        registry.append(AppInfo.class,
                Bitmap.class, new PackageIconModuleLoaderFactory(context));
    }
}
