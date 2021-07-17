package github.tornaco.thanox.thanox_framework_base;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

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

import github.tornaco.android.thanos.core.annotation.NonNull;
import github.tornaco.android.thanos.core.pm.AppInfo;
import com.elvishew.xlog.XLog;
import lombok.AllArgsConstructor;
import lombok.Getter;
import util.Singleton2;

/**
 * Created by guohao4 on 2017/12/23.
 * Email: Tornaco@163.com
 */
@GlideModule
public class GlidePackageIconModule extends AppGlideModule {

    @SuppressWarnings("NullableProblems")
    @AllArgsConstructor
    private static class PackageInfoDataFetcher implements DataFetcher<Bitmap> {

        @Getter
        private AppInfo info;
        private Context context;

        @Override
        public void loadData(Priority priority, DataCallback<? super Bitmap> callback) {
            XLog.d("loadData: " + info.getPkgName());
            if (info.getPkgName() == null) {
                callback.onLoadFailed(new NullPointerException("Package name is null"));
                return;
            }
            Drawable d = loadIconByPkgName(context, info.getPkgName());
            Bitmap bd = BitmapUtil.getBitmap(context, d);
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

        public static Drawable loadIconByPkgName(Context context, String pkg) {
            PackageManager pm = context.getPackageManager();

            try {
                ApplicationInfo info;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    info = pm.getApplicationInfo(pkg, PackageManager.MATCH_UNINSTALLED_PACKAGES);
                } else {
                    info = pm.getApplicationInfo(pkg, PackageManager.GET_UNINSTALLED_PACKAGES);
                }
                return info == null ? null : info.loadIcon(pm);
            } catch (PackageManager.NameNotFoundException var4) {
                return null;
            }
        }
    }

    @AllArgsConstructor
    private static class PackageIconModuleLoaderFactory
            implements ModelLoaderFactory<AppInfo, Bitmap> {
        private Context context;

        private static Singleton2<Context, ModelLoader<AppInfo, Bitmap>> sLoader
                = new Singleton2<Context, ModelLoader<AppInfo, Bitmap>>() {
            @Override
            protected ModelLoader<AppInfo, Bitmap> create(final Context context) {
                return new ModelLoader<AppInfo, Bitmap>() {
                    @NonNull
                    @Override
                    public ModelLoader.LoadData<Bitmap> buildLoadData(AppInfo info, int width,
                                                                      int height, Options options) {
                        Key diskCacheKey = new ObjectKey(info.getPkgName());

                        return new LoadData<>(diskCacheKey, new PackageInfoDataFetcher(info, context));
                    }

                    @Override
                    public boolean handles(AppInfo info) {
                        return info.getPkgName() != null;
                    }
                };
            }
        };


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
