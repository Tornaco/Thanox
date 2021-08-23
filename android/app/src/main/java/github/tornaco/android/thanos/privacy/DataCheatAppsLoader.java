package github.tornaco.android.thanos.privacy;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
    ThanosManager thanos = ThanosManager.from(context);
    if (!thanos.isServiceInstalled()) {
      return Lists.newArrayListWithCapacity(0);
    }
    PrivacyManager priv = thanos.getPrivacyManager();
    List<AppInfo> installed =
        thanos.getPkgManager().getInstalledPkgs(index.flag);
    List<AppListModel> res = new ArrayList<>();
    CollectionUtils.consumeRemaining(
        installed,
        appInfo -> {
          Fields f = priv
              .getSelectedFieldsProfileForPackage(appInfo.getPkgName(), PrivacyOp.OP_NO_OP);
          appInfo.setObj(f);
          res.add(new AppListModel(appInfo, f == null ? null : f.getLabel()));
        });
    Collections.sort(
        res,
        new Comparator<AppListModel>() {
          @Override
          public int compare(AppListModel o1, AppListModel o2) {
            boolean o1Selected = o1.appInfo.getObj() != null;
            boolean o2Selected = o2.appInfo.getObj() != null;
            if (o1Selected == o2Selected) {
              return o1.appInfo.compareTo(o2.appInfo);
            }
            return o1Selected ? -1 : 1;
          }
        });
    return res;
  }
}
