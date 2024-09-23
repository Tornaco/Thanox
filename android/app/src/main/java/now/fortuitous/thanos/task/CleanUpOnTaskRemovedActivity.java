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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterActivity;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterViewModel;
import github.tornaco.android.thanos.common.OnAppItemSelectStateChangeListener;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.Pkg;
import github.tornaco.android.thanos.util.ActivityUtils;

public class CleanUpOnTaskRemovedActivity extends CommonFuncToggleAppListFilterActivity {

    public static void start(Context context) {
        ActivityUtils.startActivity(context, CleanUpOnTaskRemovedActivity.class);
    }

    @NonNull
    @Override
    protected String getTitleString() {
        return getString(github.tornaco.android.thanos.res.R.string.activity_title_clean_when_task_removed);
    }

    @Nullable
    @Override
    protected String provideFeatureDescText() {
        return getString(github.tornaco.android.thanos.res.R.string.feature_desc_clean_when_task_removed);
    }

    @NonNull
    @Override
    protected OnAppItemSelectStateChangeListener onCreateAppItemSelectStateChangeListener() {
        return (appInfo, selected) -> ThanosManager.from(getApplicationContext())
                .getActivityManager().setPkgCleanUpOnTaskRemovalEnabled(Pkg.fromAppInfo(appInfo), selected);
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
    protected void onSwitchBarCheckChanged(com.google.android.material.materialswitch.MaterialSwitch switchBar, boolean isChecked) {
        super.onSwitchBarCheckChanged(switchBar, isChecked);
        ThanosManager.from(this).getActivityManager().setCleanUpOnTaskRemovalEnabled(isChecked);
    }
}
