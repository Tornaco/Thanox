package github.tornaco.thanox.sdk.demo;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.common.CategoryIndex;
import github.tornaco.android.thanos.common.CommonAppListFilterViewModel;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterActivity;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterViewModel;
import github.tornaco.android.thanos.common.OnAppItemSelectStateChangeListener;
import github.tornaco.android.thanos.core.pm.AppInfo;

public class MainActivity extends CommonFuncToggleAppListFilterActivity {
    @NonNull
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

            }
        };
    }

    @NonNull
    @Override
    protected CommonFuncToggleAppListFilterViewModel.ListModelLoader onCreateListModelLoader() {
        return new CommonAppListFilterViewModel.ListModelLoader() {
            @Override
            public List<AppListModel> load(@NonNull CategoryIndex index) {
                return new ArrayList<>();
            }
        };
    }
}
