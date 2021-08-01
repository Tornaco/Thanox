package github.tornaco.android.thanos.start;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.common.CategoryIndex;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterViewModel;
import github.tornaco.android.thanos.core.app.ActivityManager;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import lombok.AllArgsConstructor;
import util.CollectionUtils;

@AllArgsConstructor
public class BgRestrictAppsLoader implements CommonFuncToggleAppListFilterViewModel.ListModelLoader {
    @NonNull
    private final Context context;

    @Override
    public List<AppListModel> load(@NonNull CategoryIndex index) {
        ThanosManager thanos = ThanosManager.from(context);
        if (!thanos.isServiceInstalled()) return Lists.newArrayListWithCapacity(0);

        String runningBadge = context.getString(R.string.badge_app_running);
        ActivityManager am = thanos.getActivityManager();
        List<AppInfo> installed = thanos.getPkgManager().getInstalledPkgs(index.flag);
        List<AppListModel> res = new ArrayList<>();
        CollectionUtils.consumeRemaining(installed, appInfo -> {
            appInfo.setSelected(!am.isPkgBgRestricted(appInfo.getPkgName()));
            res.add(new AppListModel(appInfo, thanos.getActivityManager().isPackageRunning(appInfo.getPkgName()) ? runningBadge : null));
        });
        Collections.sort(res);
        return res;
    }
}
