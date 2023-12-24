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

package now.fortuitous.thanos.privacy;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.thanos.common.AppListItemDescriptionComposer;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.common.CategoryIndex;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterViewModel;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.secure.PrivacyManager;
import github.tornaco.android.thanos.core.secure.PrivacyManager.PrivacyOp;
import github.tornaco.android.thanos.core.secure.field.Fields;
import util.CollectionUtils;

public class DataCheatAppsLoader implements CommonFuncToggleAppListFilterViewModel.ListModelLoader {

    @NonNull
    private final Context context;

    public DataCheatAppsLoader(Context context) {
        this.context = context;
    }

    @Override
    public List<AppListModel> load(@NonNull CategoryIndex index) {
        AppListItemDescriptionComposer composer = new AppListItemDescriptionComposer(context);
        ThanosManager thanos = ThanosManager.from(context);
        if (!thanos.isServiceInstalled()) {
            return Lists.newArrayListWithCapacity(0);
        }
        PrivacyManager priv = thanos.getPrivacyManager();
        List<AppInfo> installed =
                thanos.getPkgManager().getInstalledPkgsByPackageSetId(index.pkgSetId);
        List<AppListModel> res = new ArrayList<>();
        CollectionUtils.consumeRemaining(
                installed,
                appInfo -> {
                    Fields f = priv.getSelectedFieldsProfileForPackage(appInfo.getPkgName(), PrivacyOp.OP_NO_OP);
                    appInfo.setObj(f);
                    res.add(new AppListModel(appInfo, f == null ? null : f.getLabel(), null, composer.getAppItemDescription(appInfo)));
                });
        res.sort((o1, o2) -> {
            boolean o1Selected = o1.appInfo.getObj() != null;
            boolean o2Selected = o2.appInfo.getObj() != null;
            if (o1Selected == o2Selected) {
                return o1.appInfo.compareTo(o2.appInfo);
            }
            return o1Selected ? -1 : 1;
        });
        return res;
    }
}
