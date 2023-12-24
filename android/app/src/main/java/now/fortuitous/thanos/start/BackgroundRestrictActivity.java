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

package now.fortuitous.thanos.start;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import github.tornaco.android.rhino.plugin.Verify;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterActivity;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterViewModel;
import github.tornaco.android.thanos.common.OnAppItemSelectStateChangeListener;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.util.ActivityUtils;

public class BackgroundRestrictActivity extends CommonFuncToggleAppListFilterActivity {

    @Verify
    public static void start(Context context) {
        ActivityUtils.startActivity(context, BackgroundRestrictActivity.class);
    }

    @NonNull
    @Override
    @Verify
    protected String getTitleString() {
        return getString(R.string.activity_title_bg_restrict);
    }

    @Nullable
    @Override
    protected String provideFeatureDescText() {
        return getString(R.string.feature_desc_bg_restrict);
    }

    @NonNull
    @Override
    protected OnAppItemSelectStateChangeListener onCreateAppItemSelectStateChangeListener() {
        return (appInfo, selected) -> ThanosManager.from(getApplicationContext())
                .getActivityManager().setPkgBgRestrictEnabled(appInfo, !selected);
    }

    @NonNull
    @Override
    protected CommonFuncToggleAppListFilterViewModel.ListModelLoader onCreateListModelLoader() {
        return new BgRestrictAppsLoader(this.getApplicationContext());
    }

    @Override
    protected boolean getSwitchBarCheckState() {
        return ThanosManager.from(this).isServiceInstalled() && ThanosManager.from(this).getActivityManager().isBgRestrictEnabled();
    }

    @Override
    @Verify
    protected void onSwitchBarCheckChanged(com.google.android.material.materialswitch.MaterialSwitch switchBar, boolean isChecked) {
        super.onSwitchBarCheckChanged(switchBar, isChecked);
        ThanosManager.from(this).getActivityManager().setBgRestrictEnabled(isChecked);
    }

    @Override
    @Verify
    protected void onInflateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bg_restrict_menu, menu);
    }

    @Override
    @Verify
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.action_settings == item.getItemId()) {
            BgRestrictSettingsActivity.start(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
