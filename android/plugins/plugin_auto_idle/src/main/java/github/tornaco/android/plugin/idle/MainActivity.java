package github.tornaco.android.plugin.idle;

import androidx.annotation.NonNull;

import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterActivity;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterViewModel;
import github.tornaco.android.thanos.common.OnAppItemSelectStateChangeListener;

public class MainActivity extends CommonFuncToggleAppListFilterActivity {

    @NonNull
    @Override
    protected String getTitleString() {
        return null;
    }

    @NonNull
    @Override
    protected OnAppItemSelectStateChangeListener onCreateAppItemSelectStateChangeListener() {
        return null;
    }

    @NonNull
    @Override
    protected CommonFuncToggleAppListFilterViewModel.ListModelLoader onCreateListModelLoader() {
        return null;
    }
}
