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

package now.fortuitous.thanos.power;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.core.content.ContextCompat;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.content.res.ResourcesCompat;
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
import github.tornaco.android.thanos.util.BitmapUtil;
import github.tornaco.android.thanos.util.GlideApp;
import github.tornaco.android.thanos.util.GlideUtils;
import github.tornaco.android.thanos.util.ShortcutReceiver;

@SuppressWarnings("UnstableApiUsage")
public class ShortcutHelper {
    private static final String STUB_APK_TEMPLATE_PATH = "shortcut_stub_template.apk";
    private static final String OUT_APK_PATH = "shortcut_stub_apks";
    private static final String WORK_DIR_PATH = "tmp_";
    private static final String ICON_DIR_PATH = "icon.png";

    public static void addShortcut(Context context, AppInfo appInfo) {
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
                            Intent shortcutInfoIntent = ShortcutStubActivity.createIntent(context, appInfo.getPkgName(), appInfo.getUserId());
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

    static void addShortcutForFreezeAll(Context context) {
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
            Intent shortcutInfoIntent = FreezeAllShortcutActivity.createIntent(context);
            shortcutInfoIntent.setAction(Intent.ACTION_VIEW);
            Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_nav_smart_freeze, null);
            LayerDrawable ld = (LayerDrawable) drawable;
            Drawable layer = ld.findDrawableByLayerId(R.id.settings_ic_foreground);
            if (layer != null) {
                layer.setTint(ContextCompat.getColor(context, R.color.nav_icon_smart_freeze));
                ld.setDrawableByLayerId(R.id.settings_ic_foreground, layer);
            }
            Bitmap resource = BitmapUtil.getBitmap(context, drawable);
            ShortcutInfoCompat info = new ShortcutInfoCompat.Builder(context, "Shortcut-freeze-all-" + System.currentTimeMillis())
                    .setIcon(IconCompat.createWithBitmap(Objects.requireNonNull(resource)))
                    .setShortLabel(context.getString(R.string.feature_category_app_clean_up))
                    .setIntent(shortcutInfoIntent)
                    .build();
            ShortcutManagerCompat.requestPinShortcut(context, info, ShortcutReceiver.getPinRequestAcceptedIntent(context).getIntentSender());
            XLog.d("addShortcutForFreezeAll done");
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
            iconBitmap = BitmapFactory.decodeResource(context.getResources(), github.tornaco.android.thanos.module.common.R.mipmap.ic_fallback_app_icon);
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
