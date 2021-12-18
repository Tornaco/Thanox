package github.tornaco.android.thanos.start;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import github.tornaco.android.rhino.plugin.Verify;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.ThanosApp;
import github.tornaco.android.thanos.app.donate.DonateSettings;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterActivity;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterViewModel;
import github.tornaco.android.thanos.common.OnAppItemSelectStateChangeListener;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.start.chart.ComposeStartChartActivity;
import github.tornaco.android.thanos.util.ActivityUtils;

public class StartRestrictActivity extends CommonFuncToggleAppListFilterActivity {

    @Verify
    public static void start(Context context) {
        ActivityUtils.startActivity(context, StartRestrictActivity.class);
    }

    @NonNull
    @Override
    @Verify
    protected String getTitleString() {
        return getString(R.string.activity_title_start_restrict);
    }

    @Nullable
    @Override
    @Verify
    protected String provideFeatureDescText() {
        return getString(R.string.feature_desc_start_restrict);
    }

    @NonNull
    @Override
    protected OnAppItemSelectStateChangeListener onCreateAppItemSelectStateChangeListener() {
        return (appInfo, selected) -> ThanosManager.from(getApplicationContext())
                .getActivityManager().setPkgStartBlockEnabled(appInfo.getPkgName(), !selected);
    }

    @NonNull
    @Override
    protected CommonFuncToggleAppListFilterViewModel.ListModelLoader onCreateListModelLoader() {
        return new StartAppsLoader(this.getApplicationContext());
    }

    @Override
    protected boolean getSwitchBarCheckState() {
        return ThanosManager.from(this).isServiceInstalled() && ThanosManager.from(this).getActivityManager().isStartBlockEnabled();
    }

    @Override
    @Verify
    protected void onInflateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.start_restrict_menu, menu);
    }

    @Override
    @Verify
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.action_view_start_restrict_chart == item.getItemId()) {
            if (ThanosApp.isPrc() && !DonateSettings.isActivated(thisActivity())) {
                Toast.makeText(thisActivity(), R.string.module_donate_donated_available, Toast.LENGTH_SHORT).show();
                return false;
            }
            ComposeStartChartActivity.Starter.INSTANCE.start(this);
        }
        if (R.id.action_start_rule == item.getItemId()) {
            if (ThanosApp.isPrc() && !DonateSettings.isActivated(thisActivity())) {
                Toast.makeText(thisActivity(), R.string.module_donate_donated_available, Toast.LENGTH_SHORT).show();
                return false;
            }
            StartRuleActivity.start(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    @Verify
    protected void onSwitchBarCheckChanged(com.google.android.material.switchmaterial.SwitchMaterial switchBar, boolean isChecked) {
        super.onSwitchBarCheckChanged(switchBar, isChecked);
        ThanosManager.from(this).getActivityManager().setStartBlockEnabled(isChecked);
    }
}
