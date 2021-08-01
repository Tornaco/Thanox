package github.tornaco.android.thanos.task;

import android.content.Context;
import android.widget.Switch;

import androidx.annotation.NonNull;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterActivity;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterViewModel;
import github.tornaco.android.thanos.common.OnAppItemSelectStateChangeListener;
import github.tornaco.android.thanos.core.app.ActivityManager;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.util.ActivityUtils;
import util.CollectionUtils;

public class RecentTaskBlurListActivity extends CommonFuncToggleAppListFilterActivity {

    public static void start(Context context) {
        ActivityUtils.startActivity(context, RecentTaskBlurListActivity.class);
    }

    @NonNull
    @Override
    protected String getTitleString() {
        return getString(R.string.feature_title_recent_task_blur);
    }

    @Override
    protected boolean getSwitchBarCheckState() {
        return ThanosManager.from(getApplicationContext()).isServiceInstalled()
                && ThanosManager.from(getApplicationContext()).getActivityManager().isRecentTaskBlurEnabled();
    }

    @Override
    protected void onSwitchBarCheckChanged(Switch switchBar, boolean isChecked) {
        super.onSwitchBarCheckChanged(switchBar, isChecked);
        ThanosManager.from(getApplicationContext()).ifServiceInstalled(thanosManager -> thanosManager.getActivityManager().setRecentTaskBlurEnabled(isChecked));
    }

    @NonNull
    @Override
    protected OnAppItemSelectStateChangeListener onCreateAppItemSelectStateChangeListener() {
        return (appInfo, selected)
                -> ThanosManager.from(getApplicationContext()).ifServiceInstalled(thanosManager -> thanosManager.getActivityManager().setPkgRecentTaskBlurEnabled(appInfo.getPkgName(), selected));
    }

    @NonNull
    @Override
    protected CommonFuncToggleAppListFilterViewModel.ListModelLoader onCreateListModelLoader() {
        return index -> {
            ThanosManager thanos = ThanosManager.from(getApplicationContext());
            if (!thanos.isServiceInstalled()) return Lists.newArrayListWithCapacity(0);
            ActivityManager am = thanos.getActivityManager();
            List<AppInfo> installed = thanos.getPkgManager().getInstalledPkgs(index.flag);
            List<AppListModel> res = new ArrayList<>();
            CollectionUtils.consumeRemaining(installed, appInfo -> {
                appInfo.setSelected(am.isPkgRecentTaskBlurEnabled(appInfo.getPkgName()));
                res.add(new AppListModel(appInfo));
            });
            Collections.sort(res);
            return res;
        };
    }
}
