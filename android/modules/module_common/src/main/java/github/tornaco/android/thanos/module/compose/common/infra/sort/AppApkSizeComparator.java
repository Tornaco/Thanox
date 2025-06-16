package github.tornaco.android.thanos.module.compose.common.infra.sort;

import android.content.Context;

import java.io.File;
import java.util.Comparator;

import github.tornaco.android.thanos.core.util.PkgUtils;
import github.tornaco.android.thanos.module.compose.common.infra.AppUiModel;

public class AppApkSizeComparator implements Comparator<AppUiModel> {
    private final Context context;

    public AppApkSizeComparator(Context context) {
        this.context = context;
    }

    @Override
    public int compare(AppUiModel o1, AppUiModel o2) {
        long size1 = getAppApkSize(context, o1);
        long size2 = getAppApkSize(context, o2);
        return Long.compare(size1, size2);
    }

    private static long getFileSizeChecked(String path) {
        if (path == null) return 0L;
        return new File(path).length();
    }

    static long getAppApkSize(Context context, AppUiModel model) {
        String path = PkgUtils.getApkPath(context, model.getAppInfo().getPkgName());
        return getFileSizeChecked(path);
    }
}
