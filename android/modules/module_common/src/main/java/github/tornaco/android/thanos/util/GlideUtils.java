package github.tornaco.android.thanos.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.elvishew.xlog.XLog;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import github.tornaco.android.thanos.core.pm.AppInfo;

public class GlideUtils {
    @WorkerThread
    public static Bitmap loadInCurrentThread(Context context, AppInfo appInfo) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        AtomicReference<Bitmap> ref = new AtomicReference<>();
        GlideApp.with(context)
                .asBitmap()
                .load(appInfo)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(github.tornaco.android.thanos.module.common.R.mipmap.ic_fallback_app_icon)
                .fallback(github.tornaco.android.thanos.module.common.R.mipmap.ic_fallback_app_icon)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        ref.set(resource);
                        countDownLatch.countDown();
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        ref.set(null);
                        countDownLatch.countDown();
                    }
                });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            XLog.e(Log.getStackTraceString(e));
        }

        return ref.get();
    }
}
