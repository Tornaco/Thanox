package github.tornaco.android.thanos.sdk.demo;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.common.CategoryIndex;
import github.tornaco.android.thanos.common.CommonAppListFilterActivity;
import github.tornaco.android.thanos.common.CommonAppListFilterViewModel;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.theme.Theme;
import github.tornaco.android.thanos.util.DialogUtils;

public class DemoActivity extends CommonAppListFilterActivity {
    @Override
    protected Theme getAppTheme() {
        return Theme.LightPink;
    }

    @Override
    protected void onSwitchBarCheckChanged(com.google.android.material.materialswitch.MaterialSwitch switchBar, boolean isChecked) {
        super.onSwitchBarCheckChanged(switchBar, isChecked);
        DialogUtils.showError(thisActivity(), new IllegalStateException("Here."));
    }

    @NonNull
    @Override
    protected CommonAppListFilterViewModel.ListModelLoader onCreateListModelLoader() {
        return index -> {
            List<AppListModel> res = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                AppInfo app = new AppInfo();
                app.setPkgName(getPackageName());
                app.setAppLabel("App " + i);
                res.add(new AppListModel(app));
            }
            return res;
        };
    }

    @Override
    protected String getTitleString() {
        return "Sdk demo";
    }
}
