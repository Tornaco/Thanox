package github.tornaco.android.thanos.power;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.elvishew.xlog.XLog;
import com.google.common.io.Files;
import com.stardust.autojs.apkbuilder.ApkBuilder;

import java.io.File;
import java.io.InputStream;
import java.util.Objects;

import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.util.GlideApp;
import github.tornaco.android.thanos.util.GlideUtils;

@SuppressWarnings("UnstableApiUsage")
class ShortcutHelper {
    private static final String STUB_APK_TEMPLATE_PATH = "shortcut_stub_template.apk";
    private static final String OUT_APK_PATH = "shortcut_stub_apks";
    private static final String WORK_DIR_PATH = "tmp_";
    private static final String ICON_DIR_PATH = "icon.png";

    static void addShortcut(Context context, AppInfo appInfo) {
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
            // Post load with glide.
            GlideApp.with(context)
                    .asBitmap()
                    .load(appInfo)
                    .error(github.tornaco.android.thanos.module.common.R.mipmap.ic_fallback_app_icon)
                    .fallback(github.tornaco.android.thanos.module.common.R.mipmap.ic_fallback_app_icon)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            Intent shortcutInfoIntent = ShortcutStubActivity.createIntent(context, appInfo.getPkgName());
                            shortcutInfoIntent.setAction(Intent.ACTION_VIEW);
                            ShortcutInfoCompat info = new ShortcutInfoCompat.Builder(context, "Shortcut-of-thanox-for-" + appInfo.getPkgName())
                                    .setIcon(IconCompat.createWithBitmap(Objects.requireNonNull(resource)))
                                    .setShortLabel(appInfo.getAppLabel())
                                    .setIntent(shortcutInfoIntent)
                                    .build();
                            ShortcutManagerCompat.requestPinShortcut(context, info, ShortcutReceiver.getPinRequestAcceptedIntent(context).getIntentSender());
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            // Noop.
                        }
                    });
        }
    }

    @WorkerThread
    static File createShortcutStubApkFor(Context context,
                                         AppInfo appInfo,
                                         String appLabel,
                                         String versionName,
                                         int versionCode) throws Exception {
        File workDir = new File(Objects.requireNonNull(context.getExternalCacheDir()).getAbsolutePath()
                + File.separator
                + WORK_DIR_PATH
                + System.currentTimeMillis());
        File iconPath = new File(workDir, ICON_DIR_PATH);
        Files.createParentDirs(iconPath);
        Bitmap iconBitmap = GlideUtils.loadInCurrentThread(context, appInfo);
        if (iconBitmap == null) {
            iconBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_fallback_app_icon);
        }
        boolean compressed = iconBitmap.compress(Bitmap.CompressFormat.PNG, 100, Files.asByteSink(iconPath).openStream());
        if (!compressed) {
            throw new IllegalStateException("Compress fail.");
        }
        return generateStubApk(context, appInfo, workDir, iconPath, appLabel, versionName, versionCode);
    }

    private static File generateStubApk(Context context,
                                        AppInfo appInfo,
                                        File workDir,
                                        File iconPath,
                                        String appLabel,
                                        String versionName,
                                        int versionCode) throws Exception {
        File outApk = new File(String.format("%s.apk",
                Objects.requireNonNull(context
                        .getExternalCacheDir()).getAbsolutePath()
                        + File.separator
                        + OUT_APK_PATH
                        + File.separator
                        + appInfo.getPkgName()));
        Files.createParentDirs(outApk);
        Files.createParentDirs(workDir);
        XLog.w("createShortcutStubApkFor: %s %s %s", appInfo, outApk, workDir);
        InputStream inApk = context.getAssets().open(STUB_APK_TEMPLATE_PATH);
        ApkBuilder apkBuilder = new ApkBuilder(inApk, outApk, workDir.getPath()).prepare();
        String appPackageName = ThanosManager.from(context).getPkgManager().createShortcutStubPkgName(appInfo);
        XLog.w("appPackageName, %s", appPackageName);
        apkBuilder.editManifest()
                .setVersionCode(versionCode)
                .setVersionName(versionName)
                .setAppName(appLabel)
                .setPackageName(appPackageName)
                .commit();
        // Replaces/
        apkBuilder
                .replaceFile("res/mipmap-xxxhdpi-v4/ic_launcher.png", iconPath.getPath())
                .replaceFile("res/mipmap-xxhdpi-v4/ic_launcher.png", iconPath.getPath())
                .replaceFile("res/mipmap-xhdpi-v4/ic_launcher.png", iconPath.getPath())
                .replaceFile("res/mipmap-hdpi-v4/ic_launcher.png", iconPath.getPath())
                .setArscPackageName(appPackageName)
                .build()
                .sign()
                .cleanWorkspace();

        return outApk;
    }
}
