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

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.elvishew.xlog.XLog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.common.io.Files;
import com.nononsenseapps.filepicker.FilePickerActivity;
import com.nononsenseapps.filepicker.Utils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
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
import github.tornaco.android.thanos.core.util.ObjectToStringUtils;
import github.tornaco.android.thanos.core.util.OsUtils;
import github.tornaco.android.thanos.core.util.PkgUtils;
import github.tornaco.android.thanos.databinding.ActivityAppDetailsBinding;
import github.tornaco.android.thanos.feature.access.AppFeatureManager;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.util.IntentUtils;
import github.tornaco.android.thanos.util.ToastUtils;
import github.tornaco.android.thanos.widget.ModernAlertDialog;
import github.tornaco.android.thanos.widget.ModernProgressDialog;
import github.tornaco.permission.requester.RequiresPermission;
import github.tornaco.permission.requester.RuntimePermissions;
import now.fortuitous.app.BaseTrustedActivity;
import now.fortuitous.thanos.settings.StrategySettingsActivity;
import util.CollectionUtils;

@RuntimePermissions
public class AppDetailsActivity extends BaseTrustedActivity {
    private final static int REQUEST_CODE_BACKUP_FILE_PICK = 0x100;
    private final static int REQUEST_CODE_RESTORE_FILE_PICK = 0x200;
    private final static int REQUEST_CODE_BACKUP_FILE_PICK_Q = 0x300;

    private ActivityAppDetailsBinding binding;
    private AppInfo appInfo;
    private FeatureConfigFragment featureConfigFragment;

    private ModernProgressDialog progressDialog;

    public static void start(Context context, AppInfo appInfo) {
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
                    AppFeatureManager.INSTANCE.showDonateIntroDialog(thisActivity());
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
            if (OsUtils.isTOrAbove()) {
                AppDetailsActivityPermissionRequester.restoreComponentsRequestedTOrAboveChecked(AppDetailsActivity.this);
            } else {
                AppDetailsActivityPermissionRequester.restoreComponentsRequestedTBelowChecked(AppDetailsActivity.this);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void requestApplyTemplateSelection() {
        ThanosManager thanos = ThanosManager.from(thisActivity());
        ProfileManager profileManager = thanos.getProfileManager();
        List<String> entries = new ArrayList<>();
        List<String> values = new ArrayList<>();

        List<ConfigTemplate> allConfigTemplates = profileManager.getAllConfigTemplates();

        if (allConfigTemplates.isEmpty()) {
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

    // ----------------------- BACK UP START ---------------------

    private void backupComponentSettingsRequested() {
        ModernAlertDialog dialog = new ModernAlertDialog(thisActivity());
        dialog.setDialogTitle(getString(R.string.pref_action_backup_component_settings));
        dialog.setDialogMessage(getString(R.string.pref_action_backup_component_settings_summary));
        dialog.setCancelable(true);
        dialog.setPositive(getString(R.string.pre_title_backup));
        dialog.setNegative(getString(android.R.string.cancel));
        dialog.setOnPositive(() -> {
            if (OsUtils.isTOrAbove()) {
                AppDetailsActivityPermissionRequester.backupComponentsRequestedTOrAboveChecked(AppDetailsActivity.this);
            } else {
                AppDetailsActivityPermissionRequester.backupComponentsRequestedTBelowChecked(AppDetailsActivity.this);
            }
        });
        dialog.show();
    }

    @RequiresPermission({
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO,
    })
    void backupComponentsRequestedTOrAbove() {
        if (OsUtils.isQOrAbove()) {
            backupComponentsRequestedQAndAbove();
        } else {
            backupComponentsRequestedQBelow();
        }
    }

    @RequiresPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void backupComponentsRequestedTBelow() {
        if (OsUtils.isQOrAbove()) {
            backupComponentsRequestedQAndAbove();
        } else {
            backupComponentsRequestedQBelow();
        }
    }


    private void backupComponentsRequestedQAndAbove() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // you can set file mime-type
        intent.setType("*/*");
        // default file name
        String backupFileNameWithExt = appInfo.getAppLabel() + "-" + DateUtils.formatForFileName(System.currentTimeMillis()) + ".json";
        intent.putExtra(Intent.EXTRA_TITLE, backupFileNameWithExt);
        try {
            startActivityForResult(intent, REQUEST_CODE_BACKUP_FILE_PICK_Q);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(thisActivity(), "Activity not found, please install Files app", Toast.LENGTH_LONG).show();
        }

    }

    private void backupComponentsRequestedQBelow() {
        Intent i = new Intent(thisActivity(), FilePickerActivity.class);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, true);
        i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR);
        i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
        try {
            startActivityForResult(i, REQUEST_CODE_BACKUP_FILE_PICK);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(thisActivity(), "Activity not found, please install Files app", Toast.LENGTH_LONG).show();
        }
    }


    private void onBackupComponentsFilePickRequestResultQ(Intent data) {
        if (data == null) {
            XLog.e("No data.");
            return;
        }

        Uri fileUri = data.getData();

        if (fileUri == null) {
            Toast.makeText(thisActivity(), "fileUri == null", Toast.LENGTH_LONG).show();
            XLog.e("No fileUri.");
            return;
        }

        XLog.d("fileUri == %s", fileUri);

        try {
            OutputStream os = thisActivity().getContentResolver().openOutputStream(fileUri);
            invokeComponentsBackup(os);
        } catch (IOException e) {
            XLog.e(e);
            Toast.makeText(thisActivity(), Log.getStackTraceString(e), Toast.LENGTH_LONG).show();
        }
    }

    private void onBackupComponentsFilePickRequestResult(Intent data) {
        if (data == null) {
            XLog.e("No data.");
            return;
        }

        if (thisActivity() == null) return;

        List<Uri> files = Utils.getSelectedFilesFromResult(data);
        if (CollectionUtils.isNullOrEmpty(files)) {
            Toast.makeText(thisActivity(), "No selection", Toast.LENGTH_LONG).show();
            return;
        }
        File file = Utils.getFileForUriNoThrow(files.get(0));
        XLog.w("onBackupFilePickRequestResult file is: %s", file);

        if (file == null) {
            Toast.makeText(thisActivity(), "file == null", Toast.LENGTH_LONG).show();
            return;
        }

        if (!file.isDirectory()) {
            Toast.makeText(thisActivity(), "file is not dir", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            String backupFileNameWithExt = appInfo.getAppLabel() + "-" + DateUtils.formatForFileName(System.currentTimeMillis()) + ".json";
            File destFile = new File(file, backupFileNameWithExt);
            Files.createParentDirs(destFile);
            //noinspection UnstableApiUsage
            invokeComponentsBackup(Files.asByteSink(destFile).openStream());
        } catch (IOException e) {
            XLog.e(e);
            Toast.makeText(thisActivity(), Log.getStackTraceString(e), Toast.LENGTH_LONG).show();
        }
    }

    private void invokeComponentsBackup(OutputStream os) {
        progressDialog.show();
        obtainViewModel(this)
                .performComponentsBackup(
                        thisActivity(),
                        new AppDetailsViewModel.BackupListener() {
                            @Override
                            public void onSuccess() {
                                new MaterialAlertDialogBuilder(thisActivity())
                                        .setMessage(getString(R.string.pre_message_backup_success))
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
                        os,
                        appInfo);
    }

    // ----------------------- BACK UP END ---------------------


    // -----------------------  RESTORE START ---------------------
    @RequiresPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void restoreComponentsRequestedTBelow() {
        IntentUtils.startFilePickerActivityForRes(this, REQUEST_CODE_RESTORE_FILE_PICK);
    }

    @RequiresPermission({
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO,
    })
    void restoreComponentsRequestedTOrAbove() {
        IntentUtils.startFilePickerActivityForRes(this, REQUEST_CODE_RESTORE_FILE_PICK);
    }

    private void onRestoreComponentsFilePickRequestResult(@Nullable Intent data) {
        if (data == null) {
            XLog.e("No data.");
            return;
        }

        Uri uri = data.getData();
        if (uri == null) {
            Toast.makeText(thisActivity(), "uri == null", Toast.LENGTH_LONG).show();
            XLog.e("No uri.");
            return;
        }

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
                }, uri, appInfo);
    }
// -----------------------  RESTORE END ---------------------


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        XLog.d("onActivityResult: %s %s %s", requestCode, resultCode, ObjectToStringUtils.intentToString(data));
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_RESTORE_FILE_PICK) {
            onRestoreComponentsFilePickRequestResult(data);
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_BACKUP_FILE_PICK) {
            onBackupComponentsFilePickRequestResult(data);
        } else if (requestCode == REQUEST_CODE_BACKUP_FILE_PICK_Q && resultCode == Activity.RESULT_OK) {
            onBackupComponentsFilePickRequestResultQ(data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AppDetailsActivityPermissionRequester.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public static AppDetailsViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(AppDetailsViewModel.class);
    }
}
