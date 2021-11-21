package github.tornaco.android.thanos.common.sort;

import java.util.Comparator;

import github.tornaco.android.thanos.common.AppListModel;

public class AppUidComparator implements Comparator<AppListModel> {
    @Override
    public int compare(AppListModel o1, AppListModel o2) {
        return Integer.compare(o1.appInfo.getUid(), o2.appInfo.getUid());
    }
}
