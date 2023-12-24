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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import github.tornaco.android.rhino.plugin.Verify;
import github.tornaco.android.thanos.R;
import now.fortuitous.app.BaseTrustedActivity;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.pm.Pkg;
import github.tornaco.android.thanos.core.profile.ConfigTemplate;
import github.tornaco.android.thanos.core.profile.ProfileManager;
import github.tornaco.android.thanos.core.util.PkgUtils;
import github.tornaco.android.thanos.databinding.ActivityAppDetailsBinding;
import github.tornaco.android.thanos.feature.access.AppFeatureManager;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.util.ToastUtils;
import now.fortuitous.thanos.settings.StrategySettingsActivity;

public class AppDetailsActivity extends BaseTrustedActivity {
    private ActivityAppDetailsBinding binding;
    private AppInfo appInfo;
    private FeatureConfigFragment featureConfigFragment;

    @Verify
    public static void start(Context context, AppInfo appInfo) {
        Bundle data = new Bundle();
        data.putParcelable("app", appInfo);
        ActivityUtils.startActivity(context, AppDetailsActivity.class, data);
    }

    @Override
    @Verify
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!resolveIntent()) {
            finish();
            return;
        }
        binding = ActivityAppDetailsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        initView();
        initViewModel();

        if (savedInstanceState == null) {
            reAddFragment();
        }
    }

    private void reAddFragment() {
        if (this.featureConfigFragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(featureConfigFragment)
                    .commitAllowingStateLoss();
        }
        this.featureConfigFragment = null;

        this.featureConfigFragment = new FeatureConfigFragment();
        Bundle data = new Bundle();
        data.putParcelable("app", appInfo);
        featureConfigFragment.setArguments(data);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, featureConfigFragment)
                .commit();
    }

    private boolean resolveIntent() {
        if (getIntent() == null) {
            return false;
        }
        appInfo = getIntent().getParcelableExtra("app");
        return appInfo != null && (appInfo.isDummy() || PkgUtils.isPkgInstalled(thisActivity(), appInfo.getPkgName()));
    }

    private void initView() {
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setTitle(appInfo.getAppLabel());
        binding.setApp(appInfo);
    }

    @Verify
    private void initViewModel() {
        AppDetailsViewModel viewModel = obtainViewModel(this);
        binding.setViewmodel(viewModel);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.action_apply_template == item.getItemId()) {
            AppFeatureManager.INSTANCE.withSubscriptionStatus(thisActivity(), isSubscribed -> {
                if (isSubscribed) {
                    requestApplyTemplateSelection();
                } else {
                    AppFeatureManager.INSTANCE.showDonateIntroDialog(thisActivity());
                }
                return null;
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Verify
    private void requestApplyTemplateSelection() {
        ThanosManager thanos = ThanosManager.from(thisActivity());
        ProfileManager profileManager = thanos.getProfileManager();
        List<String> entries = new ArrayList<>();
        List<String> values = new ArrayList<>();

        List<ConfigTemplate> allConfigTemplates = profileManager.getAllConfigTemplates();

        if (allConfigTemplates.size() == 0) {
            Toast.makeText(thisActivity(), R.string.pref_action_create_new_config_template, Toast.LENGTH_LONG).show();
            StrategySettingsActivity.start(thisActivity());
            return;
        }

        for (int i = 0; i < allConfigTemplates.size(); i++) {
            ConfigTemplate template = allConfigTemplates.get(i);
            entries.add(template.getTitle());
            values.add(template.getId());
        }

        new MaterialAlertDialogBuilder(thisActivity())
                .setTitle(R.string.pref_action_apply_config_template)
                .setItems(entries.toArray(new String[0]),
                        (dialog, which) -> {
                            dialog.dismiss();

                            ConfigTemplate selectedTemplate = profileManager.getConfigTemplateById(values.get(which));

                            if (selectedTemplate == null) {
                                ToastUtils.nook(getApplicationContext());
                                return;
                            }

                            Toast.makeText(thisActivity(), selectedTemplate.getTitle(), Toast.LENGTH_SHORT).show();

                            if (profileManager.applyConfigTemplateForPackage(Pkg.fromAppInfo(appInfo), selectedTemplate)) {
                                ToastUtils.ok(getApplicationContext());
                                reAddFragment();
                            } else {
                                ToastUtils.nook(getApplicationContext());
                            }
                        }).show();
    }

    public static AppDetailsViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(AppDetailsViewModel.class);
    }
}
