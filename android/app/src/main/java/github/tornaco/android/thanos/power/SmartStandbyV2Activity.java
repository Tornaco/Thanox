package github.tornaco.android.thanos.power;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import github.tornaco.android.rhino.plugin.Verify;
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

public class SmartStandbyV2Activity extends CommonFuncToggleAppListFilterActivity {

    public static void start(Context context) {
        ActivityUtils.startActivity(context, SmartStandbyV2Activity.class);
    }

    @NonNull
    @Override
    protected String getTitleString() {
        return getString(R.string.feature_title_smart_app_standby);
    }

    @Nullable
    @Override
    protected String provideFeatureDescText() {
        return getString(R.string.feature_summary_smart_app_standby);
    }

    @Override
    @Verify
    protected void onSwitchBarCheckChanged(Switch switchBar, boolean isChecked) {
        super.onSwitchBarCheckChanged(switchBar, isChecked);
        ThanosManager.from(getApplicationContext()).ifServiceInstalled(thanosManager ->
                thanosManager.getActivityManager().setSmartStandByEnabled(isChecked));
    }

    @Override
    protected boolean getSwitchBarCheckState() {
        return ThanosManager.from(getApplicationContext()).isServiceInstalled()
                && ThanosManager.from(getApplicationContext()).getActivityManager().isSmartStandByEnabled();
    }

    @NonNull
    @Override
    protected OnAppItemSelectStateChangeListener onCreateAppItemSelectStateChangeListener() {
        return (appInfo, selected) -> ThanosManager.from(getApplicationContext())
                .ifServiceInstalled(thanosManager ->
                        thanosManager.getActivityManager().setPkgSmartStandByEnabled(appInfo.getPkgName(), selected));
    }

    @NonNull
    @Override
    protected CommonFuncToggleAppListFilterViewModel.ListModelLoader onCreateListModelLoader() {
        return index -> {
            ThanosManager thanos = ThanosManager.from(getApplicationContext());
            if (!thanos.isServiceInstalled()) return Lists.newArrayListWithCapacity(0);

            String runningBadge = getApplicationContext().getString(R.string.badge_app_running);
            String idleBadge = getApplicationContext().getString(R.string.badge_app_idle);
            ActivityManager am = thanos.getActivityManager();
            List<AppInfo> installed = thanos.getPkgManager().getInstalledPkgs(index.flag);
            List<AppListModel> res = new ArrayList<>();
            CollectionUtils.consumeRemaining(installed, appInfo -> {
                appInfo.setSelected(am.isPkgSmartStandByEnabled(appInfo.getPkgName()));
                res.add(new AppListModel(appInfo,
                        thanos.getActivityManager().isPackageRunning(appInfo.getPkgName()) ? runningBadge : null,
                        thanos.getActivityManager().isPackageIdle(appInfo.getPkgName()) ? idleBadge : null));
            });
            Collections.sort(res);
            return res;
        };
    }

    @Override
    protected void onInflateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.smart_standby_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_standby_rules) {
            StandByRuleActivity.start(thisActivity());
            return true;
        }
        if (item.getItemId() == R.id.action_settings) {
            SmartStandbySettingsActivity.start(thisActivity());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
