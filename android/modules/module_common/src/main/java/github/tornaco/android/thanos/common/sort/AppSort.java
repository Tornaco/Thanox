package github.tornaco.android.thanos.common.sort;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import java.util.Comparator;

import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.module.common.R;

public enum AppSort {

    Default(R.string.common_sort_by_default, null),
    AppLabel(R.string.common_sort_by_install_app_label, new AppLabelComparator()),
    InstallTime(R.string.common_sort_by_install_time, new AppLabelComparator()),
    UpdateTime(R.string.common_sort_by_update_time, new AppLabelComparator()),
    LastUsedTime(R.string.common_sort_by_last_used_time, new AppLabelComparator()),
    TotalUsedTime(R.string.common_sort_by_total_used_time, new AppLabelComparator()),
    SdkVersion(R.string.common_sort_by_install_sdk_version, new AppLabelComparator()),
    ApkSize(R.string.common_sort_by_install_apk_size, new AppLabelComparator()),
    AppUid(R.string.common_sort_by_install_app_uid, new AppLabelComparator());

    @StringRes
    public final int labelRes;
    @Nullable
    public final Comparator<AppListModel> comparator;

    AppSort(@StringRes int labelRes, @Nullable Comparator<AppListModel> comparator) {
        this.labelRes = labelRes;
        this.comparator = comparator;
    }
}
