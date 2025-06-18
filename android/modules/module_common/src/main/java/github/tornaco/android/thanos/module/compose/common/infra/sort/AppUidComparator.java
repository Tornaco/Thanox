package github.tornaco.android.thanos.module.compose.common.infra.sort;

import java.util.Comparator;

import github.tornaco.android.thanos.module.compose.common.infra.AppUiModel;

public class AppUidComparator implements Comparator<AppUiModel> {
    @Override
    public int compare(AppUiModel o1, AppUiModel o2) {
        return Integer.compare(o1.getAppInfo().getUid(), o2.getAppInfo().getUid());
    }
}
