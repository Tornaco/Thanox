package github.tornaco.android.thanos.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.UserHandle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.elvishew.xlog.XLog;

import github.tornaco.android.common.util.ApkUtil;
import github.tornaco.android.thanos.theme.AppThemePreferences;
import github.tornaco.android.thanos.util.iconpack.IconPack;
import github.tornaco.android.thanos.util.iconpack.IconPackManager;

public class AppIconLoaderUtil {

    @Nullable
    public static Bitmap loadAppIconBitmapWithIconPack(Context context, String pkgName, int pkgUid) {
        try {
            Drawable icon = loadAppIconDrawableWithIconPack(context, pkgName);
            XLog.d("loadAppIconBitmapWithIconPack, icon: " + icon);
            Drawable badged = context.getPackageManager().getUserBadgedIcon(icon, UserHandle.getUserHandleForUid(pkgUid));
            return BitmapUtil.getBitmap(context, badged);
        } catch (Throwable e) {
            XLog.e("loadAppIconBitmapWithIconPack error: " + Log.getStackTraceString(e));
            return null;
        }
    }

    @Nullable
    public static Drawable loadAppIconDrawableWithIconPack(Context context, String pkgName) {
        try {
            String iconPackPackage = AppThemePreferences.getInstance().getIconPack(context, null);
            if (iconPackPackage != null) {
                IconPackManager iconPackManager = IconPackManager.getInstance();
                IconPack pack = iconPackManager.getIconPackage(context, iconPackPackage);
                XLog.d("AppIconLoaderUtil loadAppIcon, icon pack: " + pack);
                if (pack != null) {
                    boolean installed = pack.isInstalled();
                    if (installed) {
                        Drawable iconPackDrawable = pack.getDrawableIconForPackage(pkgName);
                        XLog.d("IconPack iconPackDrawable: " + iconPackDrawable);
                        if (iconPackDrawable != null) {
                            return iconPackDrawable;
                        }
                    }
                }
            }
            Drawable d = ApkUtil.loadIconByPkgName(context, pkgName);
            XLog.d("loadIconByPkgName, res: " + d);
            return d;
        } catch (Throwable e) {
            XLog.e("loadAppIconDrawableWithIconPack error: " + Log.getStackTraceString(e));
            return null;
        }
    }
}
