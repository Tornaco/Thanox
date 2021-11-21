package github.tornaco.android.thanos.common.sort;

import java.util.Comparator;

import github.tornaco.android.thanos.common.AppListModel;

public class AppUpdateTimeComparator implements Comparator<AppListModel> {
    @Override
    public int compare(AppListModel o1, AppListModel o2) {
        return -Long.compare(o1.appInfo.getLastUpdateTime(), o2.appInfo.getLastUpdateTime());
    }
}
