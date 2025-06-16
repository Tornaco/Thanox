package github.tornaco.android.thanos.common.sort;

import static github.tornaco.android.thanos.common.sort.AppApkSizeComparator.getAppApkSize;

import android.content.Context;
import android.os.Build;
import android.text.format.Formatter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import java.time.Duration;
import java.util.Comparator;
import java.util.Date;

import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.core.util.DateUtils;
import si.virag.fuzzydateformatter.FuzzyDateTimeFormatter;

@Deprecated
public enum AppSort {

    Default(github.tornaco.android.thanos.res.R.string.common_sort_by_default, new AppSorterProvider() {
        @NonNull
        @Override
        public Comparator<AppListModel> comparator(@NonNull Context context) {
            return AppListModel::compareTo;
        }

        @Nullable
        @Override
        public String getAppSortDescription(@NonNull Context context, @NonNull AppListModel model) {
            return null;
        }
    }),
    Running(github.tornaco.android.thanos.res.R.string.chip_title_app_only_running, new AppSorterProvider() {
        @NonNull
        @Override
        public Comparator<AppListModel> comparator(@NonNull Context context) {
            return new AppRunningStateComparator(context);
        }

        @Override
        public String getAppSortDescription(@NonNull Context context, @NonNull AppListModel model) {
            return null;
        }
    }),
    AppLabel(github.tornaco.android.thanos.res.R.string.common_sort_by_install_app_label, new AppSorterProvider() {
        @NonNull
        @Override
        public Comparator<AppListModel> comparator(@NonNull Context context) {
            return new AppLabelComparator();
        }

        @Override
        public String getAppSortDescription(@NonNull Context context, @NonNull AppListModel model) {
            return null;
        }
    }),
    InstallTime(github.tornaco.android.thanos.res.R.string.common_sort_by_install_time, new AppSorterProvider() {
        @NonNull
        @Override
        public Comparator<AppListModel> comparator(@NonNull Context context) {
            return new AppInstallTimeComparator();
        }

        @Override
        public String getAppSortDescription(@NonNull Context context, @NonNull AppListModel model) {
            return DateUtils.formatLongForMessageTime(model.appInfo.firstInstallTime);
        }
    }),
    UpdateTime(github.tornaco.android.thanos.res.R.string.common_sort_by_update_time, new AppSorterProvider() {
        @NonNull
        @Override
        public Comparator<AppListModel> comparator(@NonNull Context context) {
            return new AppUpdateTimeComparator();
        }

        @Override
        public String getAppSortDescription(@NonNull Context context, @NonNull AppListModel model) {
            return DateUtils.formatLongForMessageTime(model.appInfo.lastUpdateTime);
        }
    }),
    LastUsedTime(github.tornaco.android.thanos.res.R.string.common_sort_by_last_used_time, new AppSorterProvider() {
        @NonNull
        @Override
        public Comparator<AppListModel> comparator(@NonNull Context context) {
            return new AppLastUsedTimeComparator();
        }

        @Override
        public String getAppSortDescription(@NonNull Context context, @NonNull AppListModel model) {
            return FuzzyDateTimeFormatter.getTimeAgo(context, new Date(model.lastUsedTimeMills));
        }
    }),
    TotalUsedTime(github.tornaco.android.thanos.res.R.string.common_sort_by_total_used_time, new AppSorterProvider() {
        @NonNull
        @Override
        public Comparator<AppListModel> comparator(@NonNull Context context) {
            return new AppTotalUsedTimeComparator();
        }

        @Override
        public String getAppSortDescription(@NonNull Context context, @NonNull AppListModel model) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return DateUtils.formatDuration(Duration.ofMillis(model.totalUsedTimeMills));
            } else return DateUtils.formatLongForMessageTime(model.totalUsedTimeMills);
        }
    }),
    SdkVersion(github.tornaco.android.thanos.res.R.string.common_sort_by_install_sdk_version, new AppSorterProvider() {
        @NonNull
        @Override
        public Comparator<AppListModel> comparator(@NonNull Context context) {
            return new AppSDKVersionComparator();
        }

        @Override
        public String getAppSortDescription(@NonNull Context context, @NonNull AppListModel model) {
            return String.valueOf(model.appInfo.getTargetSdkVersion());
        }
    }),
    ApkSize(github.tornaco.android.thanos.res.R.string.common_sort_by_install_apk_size, new AppSorterProvider() {
        @NonNull
        @Override
        public Comparator<AppListModel> comparator(@NonNull Context context) {
            return new AppApkSizeComparator(context);
        }

        @Override
        public String getAppSortDescription(@NonNull Context context, @NonNull AppListModel model) {
            return Formatter.formatFileSize(context, getAppApkSize(context, model));
        }
    }),
    AppUid(github.tornaco.android.thanos.res.R.string.common_sort_by_install_app_uid, new AppSorterProvider() {
        @NonNull
        @Override
        public Comparator<AppListModel> comparator(@NonNull Context context) {
            return new AppUidComparator();
        }

        @Override
        public String getAppSortDescription(@NonNull Context context, @NonNull AppListModel model) {
            return String.valueOf(model.appInfo.getUid());
        }
    });

    @StringRes
    public final int labelRes;
    @NonNull
    public final AppSorterProvider provider;

    AppSort(@StringRes int labelRes, @NonNull AppSorterProvider provider) {
        this.labelRes = labelRes;
        this.provider = provider;
    }

    public boolean relyOnUsageStats() {
        return this == TotalUsedTime || this == LastUsedTime;
    }

    public interface AppSorterProvider {
        @NonNull
        Comparator<AppListModel> comparator(@NonNull Context context);

        @Nullable
        String getAppSortDescription(@NonNull Context context, @NonNull AppListModel model);
    }
}
