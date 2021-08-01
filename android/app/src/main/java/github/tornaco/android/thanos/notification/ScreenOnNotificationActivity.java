package github.tornaco.android.thanos.notification;

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
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.util.ActivityUtils;
import util.CollectionUtils;

public class ScreenOnNotificationActivity extends CommonFuncToggleAppListFilterActivity {

    public static void start(Context context) {
        ActivityUtils.startActivity(context, ScreenOnNotificationActivity.class);
    }

    @NonNull
    @Override
    protected String getTitleString() {
        return getString(R.string.feature_title_light_on_notification);
    }

    @NonNull
    @Override
    protected OnAppItemSelectStateChangeListener onCreateAppItemSelectStateChangeListener() {
        return (appInfo, selected) -> ThanosManager.from(getApplicationContext())
                .ifServiceInstalled(thanosManager -> thanosManager.getNotificationManager()
                        .setScreenOnNotificationEnabledForPkg(appInfo.getPkgName(), selected));
    }

    @NonNull
    @Override
    protected CommonFuncToggleAppListFilterViewModel.ListModelLoader onCreateListModelLoader() {
        return index -> {
            ThanosManager thanos = ThanosManager.from(getApplicationContext());
            if (!thanos.isServiceInstalled()) return Lists.newArrayListWithCapacity(0);
            List<AppInfo> installed = thanos.getPkgManager().getInstalledPkgs(index.flag);
            List<AppListModel> res = new ArrayList<>();
            CollectionUtils.consumeRemaining(installed, appInfo -> {
                appInfo.setSelected(thanos.getNotificationManager().isScreenOnNotificationEnabledForPkg(appInfo.getPkgName()));
                res.add(new AppListModel(appInfo));
            });
            Collections.sort(res);
            return res;
        };
    }

    @Override
    protected boolean getSwitchBarCheckState() {
        return ThanosManager.from(getApplicationContext())
                .isServiceInstalled() && ThanosManager.from(getApplicationContext())
                .getNotificationManager().isScreenOnNotificationEnabled();
    }

    @Override
    protected void onSwitchBarCheckChanged(Switch switchBar, boolean isChecked) {
        super.onSwitchBarCheckChanged(switchBar, isChecked);
        ThanosManager.from(getApplicationContext())
                .ifServiceInstalled(thanosManager ->
                        thanosManager.getNotificationManager().setScreenOnNotificationEnabled(isChecked));
    }
}
