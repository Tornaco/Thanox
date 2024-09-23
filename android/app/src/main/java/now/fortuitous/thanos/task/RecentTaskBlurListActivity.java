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

package now.fortuitous.thanos.task;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;

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

public class RecentTaskBlurListActivity extends CommonFuncToggleAppListFilterActivity {

    public static void start(Context context) {
        ActivityUtils.startActivity(context, RecentTaskBlurListActivity.class);
    }

    @NonNull
    @Override
    protected String getTitleString() {
        return getString(github.tornaco.android.thanos.res.R.string.feature_title_recent_task_blur);
    }

    @Override
    protected boolean getSwitchBarCheckState() {
        return ThanosManager.from(getApplicationContext()).isServiceInstalled()
                && ThanosManager.from(getApplicationContext()).getActivityManager().isRecentTaskBlurEnabled();
    }

    @Override
    protected void onSwitchBarCheckChanged(com.google.android.material.materialswitch.MaterialSwitch switchBar, boolean isChecked) {
        super.onSwitchBarCheckChanged(switchBar, isChecked);
        ThanosManager.from(getApplicationContext()).ifServiceInstalled(thanosManager -> thanosManager.getActivityManager().setRecentTaskBlurEnabled(isChecked));
    }

    @NonNull
    @Override
    protected OnAppItemSelectStateChangeListener onCreateAppItemSelectStateChangeListener() {
        return (appInfo, selected)
                -> ThanosManager.from(getApplicationContext()).ifServiceInstalled(thanosManager ->
                thanosManager.getActivityManager().setPkgRecentTaskBlurEnabled(Pkg.fromAppInfo(appInfo), selected));
    }

    @NonNull
    @Override
    protected CommonFuncToggleAppListFilterViewModel.ListModelLoader onCreateListModelLoader() {
        AppListItemDescriptionComposer composer = new AppListItemDescriptionComposer(thisActivity());
        return index -> {
            ThanosManager thanos = ThanosManager.from(getApplicationContext());
            if (!thanos.isServiceInstalled())
                return Lists.newArrayListWithCapacity(0);
            ActivityManager am = thanos.getActivityManager();
            List<AppInfo> installed = thanos.getPkgManager().getInstalledPkgsByPackageSetId(index.pkgSetId);
            List<AppListModel> res = new ArrayList<>();
            CollectionUtils.consumeRemaining(installed, appInfo -> {
                appInfo.setSelected(am.isPkgRecentTaskBlurEnabled(Pkg.fromAppInfo(appInfo)));
                res.add(new AppListModel(appInfo, null, null, composer.getAppItemDescription(appInfo)));
            });
            Collections.sort(res);
            return res;
        };
    }

    @Override
    protected void onInflateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recent_task_blur_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.action_settings == item.getItemId()) {
            RecentTaskBlurSettingsActivity.start(thisActivity());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
