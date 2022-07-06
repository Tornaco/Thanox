package github.tornaco.android.thanos.common;

import android.content.Context;

import androidx.annotation.Nullable;

import github.tornaco.android.thanos.core.pm.AppInfo;

public class AppListItemDescriptionComposer {
    private final Context context;

    public AppListItemDescriptionComposer(Context context) {
        this.context = context;
    }

    @Nullable
    public String getAppItemDescription(AppInfo appInfo) {
        boolean showPkg = CommonPreferences.getInstance().isAppListShowPkgNameEnabled(context);
        boolean showVersion = CommonPreferences.getInstance().isAppListShowVersionEnabled(context);
        boolean showUserId = !appInfo.isCurrentUser();
        if (!showPkg && !showVersion && !showUserId) {
            return null;
        }
        String description = "";
        if (showPkg) {
            description += appInfo.getPkgName();
        }
        if (showVersion) {
            description += "\n";
            description += appInfo.getVersionName();
        }
        if (showUserId) {
            description += "\n";
            description += "user ";
            description += appInfo.getUserId();
        }
        return description.trim();
    }
}
