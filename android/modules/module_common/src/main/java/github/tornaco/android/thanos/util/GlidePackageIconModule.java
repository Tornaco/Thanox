package github.tornaco.android.thanos.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

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

import github.tornaco.android.common.util.ApkUtil;
import github.tornaco.android.thanos.core.pm.AppInfo;
import com.elvishew.xlog.XLog;
import github.tornaco.android.thanos.theme.AppThemePreferences;
import github.tornaco.android.thanos.util.iconpack.IconPack;
import github.tornaco.android.thanos.util.iconpack.IconPackManager;
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
            try {
                String iconPackPackage = AppThemePreferences.getInstance().getIconPack(context, null);
                if (iconPackPackage != null) {
                    IconPackManager iconPackManager = IconPackManager.getInstance();
                    IconPack pack = iconPackManager.getIconPackage(context, iconPackPackage);
                    XLog.d("IconPack: " + pack);
                    if (pack != null) {
                        boolean installed = pack.isInstalled();
                        if (installed) {
                            Drawable iconPackDrawable = pack.getDrawableIconForPackage(info.getPkgName());
                            XLog.d("IconPack iconPackDrawable: " + iconPackDrawable);
                            if (iconPackDrawable != null) {
                                Bitmap bd = BitmapUtil.getBitmap(context, iconPackDrawable);
                                callback.onDataReady(bd);
                                return;
                            }
                        }
                    }
                }
            } catch (Throwable e) {
                XLog.e("Fail load icon from pack: " + Log.getStackTraceString(e));
            }
            Drawable d = ApkUtil.loadIconByPkgName(context, info.getPkgName());
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
    }

    @AllArgsConstructor
    private static class PackageIconModuleLoaderFactory
            implements ModelLoaderFactory<AppInfo, Bitmap> {
        private Context context;

        private static Singleton2<Context, ModelLoader<AppInfo, Bitmap>> sLoader
                = new Singleton2<Context, ModelLoader<AppInfo, Bitmap>>() {
            @Override
            protected ModelLoader<AppInfo, Bitmap> create(Context context) {
                return new ModelLoader<AppInfo, Bitmap>() {
                    @NonNull
                    @Override
                    public LoadData<Bitmap> buildLoadData(AppInfo info, int width,
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
