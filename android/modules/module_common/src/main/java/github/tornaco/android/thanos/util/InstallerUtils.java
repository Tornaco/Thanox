package github.tornaco.android.thanos.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import com.elvishew.xlog.XLog;

import java.io.File;

public class InstallerUtils {

    public static void uninstallUserAppWithIntent(Context context, String pkg) {
        XLog.w("uninstallUserAppWithIntent: %s %s", context, pkg);
        Uri packageURI = Uri.parse("package:" + pkg);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        uninstallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(uninstallIntent);
    }

    public static void installUserAppWithIntent(Context context, File apk) {
        Intent install = new Intent();
        install.setAction(Intent.ACTION_VIEW);
        install.addCategory(Intent.CATEGORY_DEFAULT);
        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(context,
                    context.getPackageName() + ".provider", apk);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            install.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            install.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive");
        }
        context.startActivity(install);
    }

}
