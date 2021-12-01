package github.tornaco.android.thanos.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.UserHandle;
import android.util.Log;

import com.elvishew.xlog.XLog;

import github.tornaco.android.common.util.ApkUtil;
import github.tornaco.android.thanos.theme.AppThemePreferences;
import github.tornaco.android.thanos.util.iconpack.IconPack;
import github.tornaco.android.thanos.util.iconpack.IconPackManager;

public class AppIconLoaderUtil {
    public static Bitmap loadAppIconBitmapWithIconPack(Context context, String pkgName, int pkgUid) {
        Drawable icon = loadAppIconDrawableWithIconPack(context, pkgName);
        Drawable badged = context.getPackageManager().getUserBadgedIcon(icon, UserHandle.getUserHandleForUid(pkgUid));
        return BitmapUtil.getBitmap(context, badged);
    }

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
        } catch (Throwable e) {
            XLog.e("Fail load icon from pack: " + Log.getStackTraceString(e));
        }
        return ApkUtil.loadIconByPkgName(context, pkgName);
    }
}
