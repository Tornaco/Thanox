/*
 * (C) Copyright 2022 Thanox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package now.fortuitous.thanos.process;

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
                (view, model) -> viewModel.killApp(model.appInfo)));

        binding.swipe.setOnRefreshListener(() -> viewModel.start());
        binding.swipe.setColorSchemeColors(getResources().getIntArray(R.array.swipe_refresh_colors));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
    }

    private void setupViewModel() {
        viewModel = obtainViewModel(requireActivity());
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
