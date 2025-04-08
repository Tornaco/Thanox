package github.tornaco.thanos.android.ops.ops.by.ops;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

public class ThanoxOpsListFragment extends AllOpsListFragment {
    public static AllOpsListFragment newInstance() {
        return new ThanoxOpsListFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        setHasOptionsMenu(false);
    }

    @Override
    public AllOpsListViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(ThanoxOpsViewModel.class);
    }

    @Override
    protected String getAppBarTitle() {
        return getString(github.tornaco.android.thanos.res.R.string.module_ops_feature_title_thanox_ops);
    }
}
