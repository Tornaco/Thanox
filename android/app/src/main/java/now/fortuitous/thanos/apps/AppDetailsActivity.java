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

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.anggrayudi.storage.SimpleStorageHelper;
import com.anggrayudi.storage.file.FileFullPath;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.pm.Pkg;
import github.tornaco.android.thanos.core.profile.ConfigTemplate;
import github.tornaco.android.thanos.core.profile.ProfileManager;
import github.tornaco.android.thanos.core.util.DateUtils;
import github.tornaco.android.thanos.databinding.ActivityAppDetailsBinding;
import github.tornaco.android.thanos.support.AppFeatureManager;
import github.tornaco.android.thanos.support.ContextExtKt;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.util.ToastUtils;
import github.tornaco.android.thanos.widget.ModernAlertDialog;
import github.tornaco.android.thanos.widget.ModernProgressDialog;

public class AppDetailsActivity extends ThemeActivity {
    private final static int REQUEST_CODE_BACKUP_FILE_PICK = 0x100;
    private final static int REQUEST_CODE_RESTORE_FILE_PICK = 0x200;

    private ActivityAppDetailsBinding binding;
    private AppInfo appInfo;
    private FeatureConfigFragment featureConfigFragment;

    private ModernProgressDialog progressDialog;

    private final SimpleStorageHelper storageHelper = new SimpleStorageHelper(this);

    public static void start(Context context, AppInfo appInfo) {
        ThanosManager.from(context).getPkgManager().setComponentEnabledSetting(
                new ComponentName(context.getPackageName(), AppDetailsActivity.class.getName()),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
        );
        Bundle data = new Bundle();
        data.putParcelable("app", appInfo);
        ActivityUtils.startActivity(context, AppDetailsActivity.class, data);
    }

    @Override
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

        storageHelper.setOnFileCreated((code, documentFile) -> {
            invokeComponentsBackup(documentFile);
            return null;
        });
        storageHelper.setOnFileSelected((code, documentFiles) -> {
            if (documentFiles == null || documentFiles.isEmpty()) return null;
            //noinspection SequencedCollectionMethodCanBeUsed
            DocumentFile firstFile = documentFiles.get(0);
            restoreComponentsFile(firstFile);
            return null;
        });

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
            Toast.makeText(this, "ERROR, Intent is null!!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        appInfo = getIntent().getParcelableExtra("app");
        if (appInfo == null) {
            Toast.makeText(this, "ERROR, App info is null!!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void initView() {
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setTitle(appInfo.getAppLabel());
        binding.setApp(appInfo);

        progressDialog = new ModernProgressDialog(this);
        progressDialog.setMessage("...");
    }


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
                    AppFeatureManager.INSTANCE.showSubscribeDialog(thisActivity());
                }
                return null;
            });
            return true;
        }


        if (R.id.action_backup_component_settings == item.getItemId()) {
            backupComponentSettingsRequested();
            return true;
        }

        if (R.id.action_restore_component_settings == item.getItemId()) {
            AppFeatureManager.INSTANCE.withSubscriptionStatus(this, isSub -> {
                if (isSub) {
                    storageHelper.openFilePicker(
                            REQUEST_CODE_RESTORE_FILE_PICK,
                            false,
                            (FileFullPath) null,
                            "application/json"
                    );
                } else {
                    AppFeatureManager.INSTANCE.showSubscribeDialog(AppDetailsActivity.this);
                }
                return null;
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void requestApplyTemplateSelection() {
        ContextExtKt.withThanos(getApplicationContext(), thanos -> {
            ProfileManager profileManager = thanos.getProfileManager();
            List<String> entries = new ArrayList<>();
            List<String> values = new ArrayList<>();

            List<ConfigTemplate> allConfigTemplates = profileManager.getAllConfigTemplates();

            if (allConfigTemplates.isEmpty()) {
                Toast.makeText(thisActivity(), github.tornaco.android.thanos.res.R.string.pref_action_create_new_config_template, Toast.LENGTH_LONG).show();
                return null;
            }

            for (int i = 0; i < allConfigTemplates.size(); i++) {
                ConfigTemplate template = allConfigTemplates.get(i);
                entries.add(template.getTitle());
                values.add(template.getId());
            }

            new MaterialAlertDialogBuilder(thisActivity())
                    .setTitle(github.tornaco.android.thanos.res.R.string.pref_action_apply_config_template)
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

            return null;
        });
    }

    // ----------------------- BACK UP START ---------------------

    private void backupComponentSettingsRequested() {
        ModernAlertDialog dialog = new ModernAlertDialog(thisActivity());
        dialog.setDialogTitle(getString(github.tornaco.android.thanos.res.R.string.pref_action_backup_component_settings));
        dialog.setDialogMessage(getString(github.tornaco.android.thanos.res.R.string.pref_action_backup_component_settings_summary));
        dialog.setCancelable(true);
        dialog.setPositive(getString(github.tornaco.android.thanos.res.R.string.pre_title_backup));
        dialog.setNegative(getString(android.R.string.cancel));
        dialog.setOnPositive(() -> {
            AppFeatureManager.INSTANCE.withSubscriptionStatus(this, isSub -> {
                if (isSub) {
                    String backupFileNameWithExt = appInfo.getAppLabel() + "-" + DateUtils.formatForFileName(System.currentTimeMillis()) + ".json";
                    storageHelper.createFile(
                            "application/json",
                            backupFileNameWithExt,
                            null,
                            REQUEST_CODE_BACKUP_FILE_PICK
                    );
                } else {
                    AppFeatureManager.INSTANCE.showSubscribeDialog(AppDetailsActivity.this);
                }
                return null;
            });

        });
        dialog.show();
    }

    private void invokeComponentsBackup(DocumentFile file) {
        progressDialog.show();
        obtainViewModel(this)
                .performComponentsBackup(
                        thisActivity(),
                        new AppDetailsViewModel.BackupListener() {
                            @Override
                            public void onSuccess() {
                                new MaterialAlertDialogBuilder(thisActivity())
                                        .setMessage(getString(github.tornaco.android.thanos.res.R.string.pre_message_backup_success))
                                        .setCancelable(true)
                                        .setPositiveButton(android.R.string.ok, null)
                                        .show();
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onFail(String errMsg) {
                                new MaterialAlertDialogBuilder(thisActivity())
                                        .setMessage(errMsg)
                                        .setCancelable(true)
                                        .setPositiveButton(android.R.string.ok, null)
                                        .show();
                                progressDialog.dismiss();
                            }
                        },
                        file,
                        appInfo);
    }

    // ----------------------- BACK UP END ---------------------


    // -----------------------  RESTORE START ---------------------
    private void restoreComponentsFile(DocumentFile file) {
        progressDialog.show();
        obtainViewModel(this).performComponentsRestore(thisActivity(),
                new AppDetailsViewModel.RestoreListener() {
                    @Override
                    public void onSuccess() {
                        ToastUtils.ok(thisActivity());
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFail(String errMsg) {
                        new MaterialAlertDialogBuilder(thisActivity())
                                .setMessage(errMsg)
                                .setCancelable(true)
                                .setPositiveButton(android.R.string.ok, null)
                                .show();
                        progressDialog.dismiss();
                    }
                }, file, appInfo);
    }
// -----------------------  RESTORE END ---------------------

    public static AppDetailsViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(AppDetailsViewModel.class);
    }
}
