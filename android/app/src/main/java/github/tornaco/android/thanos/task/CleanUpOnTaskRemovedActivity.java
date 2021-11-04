package github.tornaco.android.thanos.task;

import android.content.Context;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterActivity;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterViewModel;
import github.tornaco.android.thanos.common.OnAppItemSelectStateChangeListener;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.util.ActivityUtils;

public class CleanUpOnTaskRemovedActivity extends CommonFuncToggleAppListFilterActivity {

    public static void start(Context context) {
        ActivityUtils.startActivity(context, CleanUpOnTaskRemovedActivity.class);
    }

    @NonNull
    @Override
    protected String getTitleString() {
        return getString(R.string.activity_title_clean_when_task_removed);
    }

    @Nullable
    @Override
    protected String provideFeatureDescText() {
        return getString(R.string.feature_desc_clean_when_task_removed);
    }

    @NonNull
    @Override
    protected OnAppItemSelectStateChangeListener onCreateAppItemSelectStateChangeListener() {
        return (appInfo, selected) -> ThanosManager.from(getApplicationContext())
                .getActivityManager().setPkgCleanUpOnTaskRemovalEnabled(appInfo.getPkgName(), selected);
    }

    @NonNull
    @Override
    protected CommonFuncToggleAppListFilterViewModel.ListModelLoader onCreateListModelLoader() {
        return new CleanUpTaskRemovalAppsLoader(this.getApplicationContext());
    }

    @Override
    protected boolean getSwitchBarCheckState() {
        return ThanosManager.from(this).isServiceInstalled()
                && ThanosManager.from(this).getActivityManager().isCleanUpOnTaskRemovalEnabled();
    }

    @Override
    protected void onSwitchBarCheckChanged(com.google.android.material.switchmaterial.SwitchMaterial switchBar, boolean isChecked) {
        super.onSwitchBarCheckChanged(switchBar, isChecked);
        ThanosManager.from(this).getActivityManager().setCleanUpOnTaskRemovalEnabled(isChecked);
    }
}
