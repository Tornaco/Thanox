package github.tornaco.android.thanos.module.compose.common.infra.sort;

import java.util.Comparator;

import github.tornaco.android.thanos.module.compose.common.infra.AppUiModel;
import util.PinyinComparatorUtils;

public class AppLabelComparator implements Comparator<AppUiModel> {
    @Override
    public int compare(AppUiModel o1, AppUiModel o2) {
        return PinyinComparatorUtils.compare(o1.getAppInfo().getAppLabel(), o2.getAppInfo().getAppLabel());
    }
}
