package github.tornaco.android.thanos.sdk.demo;

import android.widget.Switch;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.common.CategoryIndex;
import github.tornaco.android.thanos.common.CommonAppListFilterViewModel;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterActivity;
import github.tornaco.android.thanos.common.OnAppItemSelectStateChangeListener;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.theme.Theme;
import github.tornaco.android.thanos.widget.ModernAlertDialog;

public class DemoWithSwitchActivity extends CommonFuncToggleAppListFilterActivity {
    @Override
    protected Theme getAppTheme() {
        return Theme.LightPink;
    }

    @Override
    protected void onSwitchBarCheckChanged(Switch switchBar, boolean isChecked) {
        super.onSwitchBarCheckChanged(switchBar, isChecked);
    }

    @NonNull
    @Override
    protected CommonAppListFilterViewModel.ListModelLoader onCreateListModelLoader() {
        return new CommonAppListFilterViewModel.ListModelLoader() {
            @Override
            public List<AppListModel> load(@NonNull CategoryIndex index) {
                List<AppListModel> res = new ArrayList<>();
                for (int i = 0; i < 100; i++) {
                    AppInfo app = new AppInfo();
                    app.setPkgName(getPackageName());
                    app.setAppLabel("App " + i);
                    res.add(new AppListModel(app));
                }
                return res;
            }
        };
    }

    @Override
    protected String getTitleString() {
        return "Sdk demo";
    }

    @NonNull
    @Override
    protected OnAppItemSelectStateChangeListener onCreateAppItemSelectStateChangeListener() {
        return new OnAppItemSelectStateChangeListener() {
            @Override
            public void onAppItemSelectionChanged(AppInfo appInfo, boolean selected) {
                ModernAlertDialog dialog = new ModernAlertDialog(DemoWithSwitchActivity.this);
                dialog.setDialogTitle("Title");
                dialog.setDialogMessage("Hello world!");
                dialog.setCancelable(false);
                dialog.setPositive(getString(android.R.string.ok));
                dialog.setNegative(getString(android.R.string.cancel));
                dialog.show();
            }
        };
    }
}
