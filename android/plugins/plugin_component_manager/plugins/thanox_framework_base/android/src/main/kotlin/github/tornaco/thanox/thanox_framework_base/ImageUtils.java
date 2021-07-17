package github.tornaco.thanox.thanox_framework_base;

import android.content.Context;

import java.io.File;

import github.tornaco.android.thanos.core.pm.AppInfo;

public class ImageUtils {

    public static File getAppIconCachedFile(Context context, AppInfo appInfo) {
        try {
            return GlideApp.with(context)
                    .asFile()
                    .load(appInfo)
                    .submit(300, 300)
                    .get();
        } catch (Throwable e) {
            return null;
        }
    }
}
