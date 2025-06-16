package github.tornaco.practice.honeycomb.locker.ui.start;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import github.tornaco.android.thanos.common.AppListItemDescriptionComposer;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterActivity;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterViewModel;
import github.tornaco.android.thanos.common.OnAppItemSelectStateChangeListener;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.app.activity.ActivityStackSupervisor;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.support.ThanoxAppContext;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.widget.ModernAlertDialog;
import github.tornaco.practice.honeycomb.locker.R;
import github.tornaco.practice.honeycomb.locker.ui.setup.LockSettingsActivity;
import github.tornaco.practice.honeycomb.locker.ui.verify.BiometricsKt;
import util.CollectionUtils;

public class LockerStartActivity extends CommonFuncToggleAppListFilterActivity {

    public static void start(Context context) {
        ActivityUtils.startActivity(context, LockerStartActivity.class);
    }

    @Override
    public Context getApplicationContext() {
        return new ThanoxAppContext(super.getApplicationContext());
    }

    @NonNull
    @Override
    protected String getTitleString() {
        return getString(github.tornaco.android.thanos.res.R.string.module_locker_app_name);
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
        AppListItemDescriptionComposer composer = new AppListItemDescriptionComposer(thisActivity());
        return index -> {
            ThanosManager thanos = ThanosManager.from(getApplicationContext());
            if (!thanos.isServiceInstalled()) return Lists.newArrayListWithCapacity(0);

            ActivityStackSupervisor am = thanos.getActivityStackSupervisor();
            List<AppInfo> installed = thanos.getPkgManager().getInstalledPkgsByPackageSetId(index.pkgSetId);
            List<AppListModel> res = new ArrayList<>();
            CollectionUtils.consumeRemaining(installed, appInfo -> {
                appInfo.setSelected(am.isPackageLocked(appInfo.getPkgName()));
                res.add(new AppListModel(appInfo, null, null, composer.getAppItemDescription(appInfo)));
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
    protected void onSwitchBarCheckChanged(MaterialSwitch switchBar, boolean isChecked) {
        super.onSwitchBarCheckChanged(switchBar, isChecked);

        if (isChecked && !BiometricsKt.isBiometricReady(thisActivity())) {
            showBiometricNotReadyDialog();
            switchBar.setChecked(false);
            return;
        }

        ThanosManager thanosManager = ThanosManager.from(getApplicationContext());
        if (thanosManager.isServiceInstalled()) {
            thanosManager.getActivityStackSupervisor().setAppLockEnabled(isChecked);
        }
    }

    @Override
    protected void onInflateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.module_locker_start_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.action_settings == item.getItemId()) {
            LockSettingsActivity.start(thisActivity());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showBiometricNotReadyDialog() {
        ModernAlertDialog dialog = new ModernAlertDialog(thisActivity());
        dialog.setDialogTitle(getString(github.tornaco.android.thanos.res.R.string.module_locker_biometric_not_set_dialog_title));
        dialog.setDialogMessage(getString(github.tornaco.android.thanos.res.R.string.module_locker_biometric_not_set_dialog_message));
        dialog.setCancelable(true);
        dialog.setPositive(getString(github.tornaco.android.thanos.res.R.string.module_locker_title_settings));
        dialog.setNegative(getString(android.R.string.cancel));
        dialog.setOnPositive(() -> {
            Intent enrollIntent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(enrollIntent);
        });
        dialog.show();
    }
}
