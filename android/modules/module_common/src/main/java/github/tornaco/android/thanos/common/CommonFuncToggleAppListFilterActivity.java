package github.tornaco.android.thanos.common;

import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import github.tornaco.android.thanos.module.common.R;
import github.tornaco.android.thanos.widget.ModernAlertDialog;

public abstract class CommonFuncToggleAppListFilterActivity extends BaseAppListFilterActivity<CommonFuncToggleAppListFilterViewModel> {

    @Override
    protected void setupView() {
        super.setupView();
        // List.
        binding.apps.setLayoutManager(new LinearLayoutManager(this));
        binding.apps.setAdapter(new CommonFuncToggleAppListFilterAdapter(onCreateAppItemSelectStateChangeListener(), null));
        binding.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.start();
            }
        });
        binding.swipe.setColorSchemeColors(getResources().getIntArray(R.array.common_swipe_refresh_colors));
        viewModel.setSelectStateChangeListener(onCreateAppItemSelectStateChangeListener());
    }

    @NonNull
    protected abstract OnAppItemSelectStateChangeListener onCreateAppItemSelectStateChangeListener();

    @NonNull
    protected abstract CommonFuncToggleAppListFilterViewModel.ListModelLoader onCreateListModelLoader();

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (android.R.id.home == item.getItemId()) finish();
        if (R.id.action_select_all == item.getItemId()) {
            ModernAlertDialog dialog = new ModernAlertDialog(thisActivity());
            dialog.setDialogTitle(getString(github.tornaco.android.thanos.res.R.string.common_menu_title_select_all));
            dialog.setDialogMessage(getString(github.tornaco.android.thanos.res.R.string.common_dialog_message_are_you_sure));
            dialog.setPositive(getString(android.R.string.ok));
            dialog.setNegative(getString(android.R.string.cancel));
            dialog.setOnPositive(() -> viewModel.selectAll());
            dialog.show();
            return true;
        }
        if (R.id.action_un_select_all == item.getItemId()) {
            ModernAlertDialog dialog = new ModernAlertDialog(thisActivity());
            dialog.setDialogTitle(getString(github.tornaco.android.thanos.res.R.string.common_menu_title_un_select_all));
            dialog.setDialogMessage(getString(github.tornaco.android.thanos.res.R.string.common_dialog_message_are_you_sure));
            dialog.setPositive(getString(android.R.string.ok));
            dialog.setNegative(getString(android.R.string.cancel));
            dialog.setOnPositive(() -> viewModel.unSelectAll());
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        onInflateOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_search);
        binding.searchView.setMenuItem(item);
        return true;
    }

    protected void onInflateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_common_func_toggle_list_filter, menu);
    }

    @Override
    protected CommonFuncToggleAppListFilterViewModel onCreateViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory =
                ViewModelProvider.AndroidViewModelFactory.getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(CommonFuncToggleAppListFilterViewModel.class);
    }
}
