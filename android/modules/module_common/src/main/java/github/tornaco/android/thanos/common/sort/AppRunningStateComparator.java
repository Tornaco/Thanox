package github.tornaco.android.thanos.common.sort;

import android.content.Context;

import com.elvishew.xlog.XLog;

import java.util.Comparator;

import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.core.app.ActivityManager;
import github.tornaco.android.thanos.core.app.ThanosManager;
import util.PinyinComparatorUtils;

public class AppRunningStateComparator implements Comparator<AppListModel> {
    private final ActivityManager activityManager;

    public AppRunningStateComparator(Context context) {
        this.activityManager = ThanosManager.from(context).getActivityManager();
    }

    @Override
    public int compare(AppListModel o1, AppListModel o2) {
        try {
            boolean isO1Running = activityManager.isPackageRunning(o1.appInfo.getPkgName());
            boolean isO2Running = activityManager.isPackageRunning(o2.appInfo.getPkgName());

            if (isO1Running != isO2Running) {
                if (isO1Running) return -1;
                else return 1;
            }

        } catch (Throwable e) {
            XLog.e("AppLabelComparator error", e);
        }

        return PinyinComparatorUtils.compare(o1.appInfo.getAppLabel(), o2.appInfo.getAppLabel());
    }
}
