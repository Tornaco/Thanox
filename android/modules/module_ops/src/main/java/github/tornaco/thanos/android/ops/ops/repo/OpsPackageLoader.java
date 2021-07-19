package github.tornaco.thanos.android.ops.ops.repo;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.common.CategoryIndex;
import github.tornaco.android.thanos.common.CommonAppListFilterViewModel;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.secure.ops.AppOpsManager;
import github.tornaco.android.thanos.core.util.PkgUtils;
import github.tornaco.thanos.android.ops.model.Op;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OpsPackageLoader implements CommonAppListFilterViewModel.ListModelLoader {
    private Context context;
    private Op op;

    @Override
    public List<AppListModel> load(@NonNull CategoryIndex index) {
        ThanosManager thanosManager = ThanosManager.from(context);
        if (!thanosManager.isServiceInstalled()) {
            return new ArrayList<>(0);
        }
        List<AppInfo> installed = Lists.newArrayList(thanosManager.getPkgManager().getInstalledPkgs(index.flag));
        List<AppListModel> res = new ArrayList<>();
        String perm = AppOpsManager.opToPermission(op.getCode());
        for (AppInfo appInfo : installed) {
            int mode = thanosManager.getAppOpsManager().checkOperationNonCheck(op.getCode(), appInfo.getUid(), appInfo.getPkgName());
            appInfo.setStr(String.valueOf(mode));
            // If it is disabled, always show.
            if (perm != null && mode == AppOpsManager.MODE_ALLOWED) {
                Set<String> permissions = Sets.newHashSet(PkgUtils.getAllDeclaredPermissions(context, appInfo.getPkgName()));
                if (permissions.contains(perm)) {
                    res.add(new AppListModel(appInfo));
                }
            } else {
                res.add(new AppListModel(appInfo));
            }
        }
        Collections.sort(res);
        return res;
    }
}
