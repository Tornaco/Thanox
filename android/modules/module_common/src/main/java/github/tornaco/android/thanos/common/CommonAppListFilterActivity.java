package github.tornaco.android.thanos.common;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

import github.tornaco.android.thanos.module.common.R;

public abstract class CommonAppListFilterActivity extends BaseAppListFilterActivity<CommonAppListFilterViewModel> {

    @Override
    protected void setupView() {
        super.setupView();
        // List.
        binding.apps.setLayoutManager(onCreateLayoutManager());
        binding.apps.setAdapter(onCreateCommonAppListFilterAdapter());
        binding.swipe.setOnRefreshListener(() -> viewModel.start());
        binding.swipe.setColorSchemeColors(
                getResources().getIntArray(R.array.common_swipe_refresh_colors));
    }

    protected LayoutManager onCreateLayoutManager() {
        return new LinearLayoutManager(this);
    }


    protected AppItemClickListener onCreateAppItemViewClickListener() {
        return null;
    }

    @NonNull
    protected abstract CommonAppListFilterViewModel.ListModelLoader onCreateListModelLoader();

    protected CommonAppListFilterAdapter onCreateCommonAppListFilterAdapter() {
        return new CommonAppListFilterAdapter(onCreateAppItemViewClickListener(), false);
    }

    @Override
    protected CommonAppListFilterViewModel onCreateViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory =
                ViewModelProvider.AndroidViewModelFactory.getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(CommonAppListFilterViewModel.class);
    }
}
