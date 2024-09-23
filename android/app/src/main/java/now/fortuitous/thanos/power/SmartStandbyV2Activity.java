/*
 * (C) Copyright 2022 Thanox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package now.fortuitous.thanos.power;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.common.AppListItemDescriptionComposer;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterActivity;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterViewModel;
import github.tornaco.android.thanos.common.OnAppItemSelectStateChangeListener;
import github.tornaco.android.thanos.core.app.ActivityManager;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.pm.Pkg;
import github.tornaco.android.thanos.util.ActivityUtils;
import util.CollectionUtils;

public class SmartStandbyV2Activity extends CommonFuncToggleAppListFilterActivity {

    public static void start(Context context) {
        ActivityUtils.startActivity(context, SmartStandbyV2Activity.class);
    }

    @Override
    public boolean isADVF() {
        return true;
    }

    @NonNull
    @Override
    protected String getTitleString() {
        return getString(github.tornaco.android.thanos.res.R.string.feature_title_smart_app_standby);
    }

    @Nullable
    @Override
    protected String provideFeatureDescText() {
        return getString(github.tornaco.android.thanos.res.R.string.feature_summary_smart_app_standby);
    }

    @Override

    protected void onSwitchBarCheckChanged(com.google.android.material.materialswitch.MaterialSwitch switchBar, boolean isChecked) {
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
                        thanosManager.getActivityManager().setPkgSmartStandByEnabled(Pkg.fromAppInfo(appInfo), selected));
    }

    @NonNull
    @Override
    protected CommonFuncToggleAppListFilterViewModel.ListModelLoader onCreateListModelLoader() {
        return index -> {
            AppListItemDescriptionComposer composer = new AppListItemDescriptionComposer(thisActivity());
            ThanosManager thanos = ThanosManager.from(getApplicationContext());
            if (!thanos.isServiceInstalled())
                return Lists.newArrayListWithCapacity(0);

            String runningBadge = getApplicationContext().getString(github.tornaco.android.thanos.res.R.string.badge_app_running);
            String idleBadge = getApplicationContext().getString(github.tornaco.android.thanos.res.R.string.badge_app_idle);
            ActivityManager am = thanos.getActivityManager();
            List<AppInfo> installed = thanos.getPkgManager().getInstalledPkgsByPackageSetId(index.pkgSetId);
            List<AppListModel> res = new ArrayList<>();
            CollectionUtils.consumeRemaining(installed, appInfo -> {
                appInfo.setSelected(am.isPkgSmartStandByEnabled(Pkg.fromAppInfo(appInfo)));
                res.add(new AppListModel(
                        appInfo,
                        thanos.getActivityManager().isPackageRunning(Pkg.fromAppInfo(appInfo)) ? runningBadge : null,
                        thanos.getActivityManager().isPackageIdle(Pkg.fromAppInfo(appInfo)) ? idleBadge : null,
                        composer.getAppItemDescription(appInfo)));
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
