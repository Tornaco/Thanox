package github.tornaco.thanos.android.module.profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;


import github.tornaco.android.thanos.core.profile.GlobalVar;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.thanos.android.module.profile.databinding.ModuleProfileGlobalVarListActivityBinding;

public class GlobalVarListActivity extends ThemeActivity implements VarItemClickListener {

    private GlobalVarViewModel viewModel;
    private ModuleProfileGlobalVarListActivityBinding binding;


    public static void start(Context context) {
        ActivityUtils.startActivity(context, GlobalVarListActivity.class);
    }

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ModuleProfileGlobalVarListActivityBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        setupView();
        setupViewModel();
    }


    private void setupView() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // List.
        binding.varListView.setLayoutManager(new LinearLayoutManager(this));
        binding.varListView.setAdapter(new VarListAdapter(this));
        binding.swipe.setOnRefreshListener(() -> viewModel.start());
        binding.swipe.setColorSchemeColors(getResources().getIntArray(
                github.tornaco.android.thanos.module.common.R.array.common_swipe_refresh_colors));

        binding.fab.setOnClickListener((View v) -> GlobalVarEditorActivity.start(thisActivity(), null));
    }

    private void setupViewModel() {
        viewModel = obtainViewModel(this);
        viewModel.start();

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.resume();
    }

    @Override

    public void onItemClick(@NonNull GlobalVar var) {
        GlobalVarEditorActivity.start(thisActivity(), var);
    }

    public static GlobalVarViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(GlobalVarViewModel.class);
    }
}
