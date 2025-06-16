package github.tornaco.android.thanos.module.compose.common.infra.sort;

import android.content.Context;

import com.elvishew.xlog.XLog;

import java.util.Comparator;

import github.tornaco.android.thanos.core.app.ActivityManager;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.Pkg;
import github.tornaco.android.thanos.module.compose.common.infra.AppUiModel;
import util.PinyinComparatorUtils;

public class AppRunningStateComparator implements Comparator<AppUiModel> {
    private final ActivityManager activityManager;

    public AppRunningStateComparator(Context context) {
        this.activityManager = ThanosManager.from(context).getActivityManager();
    }

    @Override
    public int compare(AppUiModel o1, AppUiModel o2) {
        try {
            boolean isO1Running = activityManager.isPackageRunning(Pkg.fromAppInfo(o1.getAppInfo()));
            boolean isO2Running = activityManager.isPackageRunning(Pkg.fromAppInfo(o2.getAppInfo()));

            if (isO1Running != isO2Running) {
                if (isO1Running) return -1;
                else return 1;
            }

        } catch (Throwable e) {
            XLog.e("AppLabelComparator error", e);
        }

        return PinyinComparatorUtils.compare(o1.getAppInfo().getAppLabel(), o2.getAppInfo().getAppLabel());
    }
}
