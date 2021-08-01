package github.tornaco.android.thanos.process;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Objects;

import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.databinding.FragmentProcessManageBinding;

public class ProcessManageFragment extends Fragment {
    private FragmentProcessManageBinding binding;
    private ProcessManageViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProcessManageBinding.inflate(inflater, container, false);
        setupView();
        return binding.getRoot();
    }

    private void setupView() {
        binding.apps.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.apps.setAdapter(new ProcessManageAdapter(
                mergedItem -> {
                    RunningServiceDetails.sSelectedItem = mergedItem;
                    Bundle args = RunningServiceDetails.makeServiceDetailsActivityBundle(mergedItem);
                    RunningServicesDetailsActivity.start(getActivity(), args);
                },
                (view, appInfo) -> viewModel.killApp(appInfo)));

        binding.swipe.setOnRefreshListener(() -> viewModel.start());
        binding.swipe.setColorSchemeColors(getResources().getIntArray(R.array.swipe_refresh_colors));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
    }

    private void setupViewModel() {
        viewModel = obtainViewModel(Objects.requireNonNull(getActivity()));
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();
    }

    private static ProcessManageViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(ProcessManageViewModel.class);
    }

}
