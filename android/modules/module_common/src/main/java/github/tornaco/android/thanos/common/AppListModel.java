package github.tornaco.android.thanos.common;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import github.tornaco.android.thanos.core.pm.AppInfo;

public class AppListModel extends ListModel<AppListModel> {
    @NonNull
    public AppInfo appInfo;
    // Extra badge.
    @Nullable
    public String badge;
    @Nullable
    public String badge2;

    @ColorInt
    public int badge1BgColor;
    @ColorInt
    public int badge2BgColor;

    @Nullable
    public String description;

    public boolean showStateBadge = true;

    public long lastUsedTimeMills = 0L;
    public long totalUsedTimeMills = 0L;

    public AppListModel(@NonNull AppInfo appInfo,
                        @Nullable String badge,
                        @Nullable String badge2,
                        @ColorInt int badge1BgColor,
                        @ColorInt int badge2BgColor,
                        @Nullable String description,
                        boolean showStateBadge) {
        this.appInfo = appInfo;
        this.badge = badge;
        this.badge2 = badge2;
        this.badge1BgColor = badge1BgColor;
        this.badge2BgColor = badge2BgColor;
        this.description = description;
        this.showStateBadge = showStateBadge;
    }

    public AppListModel(@NonNull AppInfo appInfo,
                        @Nullable String badge,
                        @Nullable String badge2,
                        @Nullable String description) {
        this.appInfo = appInfo;
        this.badge = badge;
        this.badge2 = badge2;
        this.description = description;
    }

    public AppListModel(@NonNull AppInfo appInfo, @Nullable String badge, @Nullable String badge2) {
        this.appInfo = appInfo;
        this.badge = badge;
        this.badge2 = badge2;
    }

    public AppListModel(@NonNull AppInfo appInfo, @Nullable String badge) {
        this.appInfo = appInfo;
        this.badge = badge;
    }

    public AppListModel(@NonNull AppInfo appInfo) {
        this.appInfo = appInfo;
    }

    @Override
    public int compareTo(@NonNull AppListModel listModel) {
        return this.appInfo.compareTo(listModel.appInfo);
    }
}
