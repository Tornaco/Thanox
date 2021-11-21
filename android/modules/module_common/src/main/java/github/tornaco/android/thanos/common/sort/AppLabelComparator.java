package github.tornaco.android.thanos.common.sort;

import java.util.Comparator;

import github.tornaco.android.thanos.common.AppListModel;
import util.PinyinComparatorUtils;

public class AppLabelComparator implements Comparator<AppListModel> {
    @Override
    public int compare(AppListModel o1, AppListModel o2) {
        return PinyinComparatorUtils.compare(o1.appInfo.getAppLabel(), o2.appInfo.getAppLabel());
    }
}
