package github.tornaco.android.thanos.module.compose.common.infra.sort;

import java.util.Comparator;

import github.tornaco.android.thanos.module.compose.common.infra.AppUiModel;

public class AppUpdateTimeComparator implements Comparator<AppUiModel> {
    @Override
    public int compare(AppUiModel o1, AppUiModel o2) {
        return -Long.compare(o1.getAppInfo().getLastUpdateTime(), o2.getAppInfo().getLastUpdateTime());
    }
}
