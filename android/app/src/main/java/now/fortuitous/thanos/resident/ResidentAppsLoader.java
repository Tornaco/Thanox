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

package now.fortuitous.thanos.resident;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import github.tornaco.android.thanos.common.AppListItemDescriptionComposer;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.common.CategoryIndex;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterViewModel;
import github.tornaco.android.thanos.core.app.ActivityManager;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.pm.Pkg;
import util.CollectionUtils;

public class ResidentAppsLoader implements CommonFuncToggleAppListFilterViewModel.ListModelLoader {
    @NonNull
    private final Context context;

    public ResidentAppsLoader(Context context) {
        this.context = context;
    }

    @Override
    public List<AppListModel> load(@NonNull CategoryIndex index) {
        ThanosManager thanos = ThanosManager.from(context);
        AppListItemDescriptionComposer composer = new AppListItemDescriptionComposer(context);
        if (!thanos.isServiceInstalled())
            return Lists.newArrayListWithCapacity(0);

        String runningBadge = context.getString(github.tornaco.android.thanos.res.R.string.badge_app_running);
        ActivityManager am = thanos.getActivityManager();
        List<AppInfo> installed = thanos.getPkgManager().getInstalledPkgsByPackageSetId(index.pkgSetId);
        List<AppListModel> res = new ArrayList<>();
        CollectionUtils.consumeRemaining(installed, appInfo -> {
            appInfo.setSelected(am.isPkgResident(Pkg.fromAppInfo(appInfo)));
            res.add(new AppListModel(
                    appInfo,
                    thanos.getActivityManager().isPackageRunning(Pkg.fromAppInfo(appInfo)) ? runningBadge : null,
                    null,
                    composer.getAppItemDescription(appInfo)));
        });
        Collections.sort(res);
        return res;
    }
}
