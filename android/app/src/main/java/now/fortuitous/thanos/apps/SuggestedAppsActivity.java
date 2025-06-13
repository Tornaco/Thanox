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

package now.fortuitous.thanos.apps;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import github.tornaco.android.thanos.common.AppItemActionListener;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.databinding.ActivitySuggestAppsBinding;
import github.tornaco.android.thanos.util.ActivityUtils;
import now.fortuitous.app.BaseTrustedActivity;
import util.Consumer;

@Deprecated
public class SuggestedAppsActivity extends BaseTrustedActivity {
    private SuggestedAppsViewModel viewModel;
    private ActivitySuggestAppsBinding binding;

    public static void start(Context context) {
        ActivityUtils.startActivity(context, SuggestedAppsActivity.class);
    }

    @Override
    public boolean isF() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySuggestAppsBinding.inflate(
                LayoutInflater.from(this), null, false);
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
        binding.apps.setLayoutManager(new GridLayoutManager(this, 5));
        binding.apps.setAdapter(new SuggestedAppsAdapter(new AppItemActionListener() {
            @Override
            public void onAppItemClick(AppInfo appInfo) {
                AppDetailsActivity.start(getApplicationContext(), appInfo);
            }

            @Override
            public void onAppItemSwitchStateChange(AppInfo appInfo, boolean checked) {

            }
        }));
        ThanosManager.from(this).ifServiceInstalled(new Consumer<ThanosManager>() {
            @Override
            public void accept(ThanosManager thanosManager) {
                binding.textViewAllApps.setText(
                        getString(github.tornaco.android.thanos.res.R.string.title_suggested_apps_view_all,
                                String.valueOf(thanosManager.getPkgManager().getInstalledPackagesCount(AppInfo.FLAGS_ALL))));

            }
        });
        binding.textViewAllApps.setOnClickListener(view -> AppsManageActivity.start(SuggestedAppsActivity.this));
        binding.packageSets.setOnClickListener(view -> PackageSetListActivity.start(SuggestedAppsActivity.this));
    }

    private void setupViewModel() {
        viewModel = obtainViewModel(this);
        viewModel.start();

        binding.setViewmodel(viewModel);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();
    }

    public static SuggestedAppsViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(SuggestedAppsViewModel.class);
    }
}
