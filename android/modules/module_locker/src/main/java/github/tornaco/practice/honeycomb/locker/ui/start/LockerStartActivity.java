package github.tornaco.practice.honeycomb.locker.ui.start;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import github.tornaco.android.rhino.annotations.Verify;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterActivity;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterViewModel;
import github.tornaco.android.thanos.common.OnAppItemSelectStateChangeListener;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.app.activity.ActivityStackSupervisor;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.practice.honeycomb.locker.R;
import github.tornaco.practice.honeycomb.locker.ui.setup.LockerMethodSelectionUi;
import github.tornaco.practice.honeycomb.locker.ui.setup.SettingsActivity;
import util.CollectionUtils;
import util.Consumer;

public class LockerStartActivity extends CommonFuncToggleAppListFilterActivity {

    private LockerStartViewModel lockerStartViewModel;

    public static void start(Context context) {
        ActivityUtils.startActivity(context, LockerStartActivity.class);
    }

    @Override
    @Verify
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lockerStartViewModel = obtainLockerViewModel(this);
    }

    @NonNull
    @Override
    protected String getTitleString() {
        return getString(R.string.module_locker_app_name);
    }

    @NonNull
    @Override
    protected OnAppItemSelectStateChangeListener onCreateAppItemSelectStateChangeListener() {
        return (appInfo, selected) -> {
            ThanosManager thanosManager = ThanosManager.from(getApplicationContext());
            if (thanosManager.isServiceInstalled()) {
                thanosManager.getActivityStackSupervisor()
                        .setPackageLocked(appInfo.getPkgName(), selected);
            }
        };
    }

    @NonNull
    @Override
    protected CommonFuncToggleAppListFilterViewModel.ListModelLoader onCreateListModelLoader() {
        return index -> {
            ThanosManager thanos = ThanosManager.from(getApplicationContext());
            if (!thanos.isServiceInstalled()) return Lists.newArrayListWithCapacity(0);

            ActivityStackSupervisor am = thanos.getActivityStackSupervisor();
            List<AppInfo> installed = thanos.getPkgManager().getInstalledPkgs(index.flag);
            List<AppListModel> res = new ArrayList<>();
            CollectionUtils.consumeRemaining(installed, appInfo -> {
                appInfo.setSelected(am.isPackageLocked(appInfo.getPkgName()));
                res.add(new AppListModel(appInfo));
            });
            Collections.sort(res);
            return res;
        };
    }

    @Override
    protected boolean getSwitchBarCheckState() {
        ThanosManager thanosManager = ThanosManager.from(getApplicationContext());
        return thanosManager.isServiceInstalled() && thanosManager.getActivityStackSupervisor().isAppLockEnabled();
    }

    @Override
    protected void onSwitchBarCheckChanged(Switch switchBar, boolean isChecked) {
        super.onSwitchBarCheckChanged(switchBar, isChecked);
        ThanosManager thanosManager = ThanosManager.from(getApplicationContext());
        if (thanosManager.isServiceInstalled()) {
            thanosManager.getActivityStackSupervisor().setAppLockEnabled(isChecked);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!lockerStartViewModel.isCurrentLockMethodKeySet()) {
            LockerMethodSelectionUi.showLockerMethodSelections(this, new Consumer<Integer>() {
                @Override
                public void accept(Integer method) {
                    lockerStartViewModel.startSetupActivity(method);
                }
            });
        }
    }

    @Override
    protected void onInflateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.module_locker_start_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.action_settings == item.getItemId()) {
            SettingsActivity.start(thisActivity());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static LockerStartViewModel obtainLockerViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(LockerStartViewModel.class);
    }
}
