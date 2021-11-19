package github.tornaco.thanos.android.ops.ops.remind;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import github.tornaco.android.thanos.common.AppListItemDescriptionComposer;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.common.CommonAppListFilterViewModel;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterActivity;
import github.tornaco.android.thanos.common.OnAppItemSelectStateChangeListener;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.widget.SwitchBar;
import github.tornaco.thanos.android.ops.R;
import util.CollectionUtils;

public class AppListActivity extends CommonFuncToggleAppListFilterActivity {

    public static void start(Context context) {
        ActivityUtils.startActivity(context, AppListActivity.class);
    }

    @NonNull
    @Override
    protected String getTitleString() {
        return getString(R.string.module_ops_title_op_remind_apps);
    }

    @Override
    protected void onSetupSwitchBar(SwitchBar switchBar) {
        super.onSetupSwitchBar(switchBar);
        switchBar.hide();
    }

    @NonNull
    @Override
    protected OnAppItemSelectStateChangeListener onCreateAppItemSelectStateChangeListener() {
        return (appInfo, selected) -> ThanosManager.from(getApplicationContext())
                .ifServiceInstalled(thanosManager
                        -> thanosManager.getAppOpsManager().setPkgOpRemindEnable(appInfo.getPkgName(), selected));
    }


    @NonNull
    @Override
    protected CommonAppListFilterViewModel.ListModelLoader onCreateListModelLoader() {
        AppListItemDescriptionComposer composer = new AppListItemDescriptionComposer(thisActivity());
        return index -> {
            Context context = getApplicationContext();
            ThanosManager thanos = ThanosManager.from(context);
            if (!thanos.isServiceInstalled())
                return Lists.newArrayListWithCapacity(0);

            List<AppInfo> installed = thanos.getPkgManager().getInstalledPkgsByPackageSetId(index.pkgSetId);
            List<AppListModel> res = new ArrayList<>();
            CollectionUtils.consumeRemaining(installed, appInfo -> {
                appInfo.setSelected(thanos.getAppOpsManager().isPkgOpRemindEnable(appInfo.getPkgName()));
                res.add(new AppListModel(appInfo, null, null, composer.getAppItemDescription(appInfo)));
            });
            Collections.sort(res);
            return res;
        };
    }
}
