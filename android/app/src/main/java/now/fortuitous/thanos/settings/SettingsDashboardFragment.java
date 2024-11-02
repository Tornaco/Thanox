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

package now.fortuitous.thanos.settings;

import static now.fortuitous.thanos.pref.AppPreference.PREF_KEY_CLASSIC_HOME;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.DropDownPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.SwitchPreferenceCompat;

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
import java.util.UUID;

import github.tornaco.android.thanos.BasePreferenceFragmentCompat;
import github.tornaco.android.thanos.BuildConfig;
import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.common.CommonPreferences;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.profile.ConfigTemplate;
import github.tornaco.android.thanos.core.profile.ProfileManager;
import github.tornaco.android.thanos.core.util.ClipboardUtils;
import github.tornaco.android.thanos.core.util.DateUtils;
import github.tornaco.android.thanos.core.util.ObjectToStringUtils;
import github.tornaco.android.thanos.core.util.Optional;
import github.tornaco.android.thanos.core.util.OsUtils;
import github.tornaco.android.thanos.main.NavActivityPluginKt;
import github.tornaco.android.thanos.support.AppFeatureManager;
import github.tornaco.android.thanos.theme.AppThemePreferences;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.util.BrowserUtils;
import github.tornaco.android.thanos.util.GlideApp;
import github.tornaco.android.thanos.util.IntentUtils;
import github.tornaco.android.thanos.util.iconpack.IconPack;
import github.tornaco.android.thanos.util.iconpack.IconPackManager;
import github.tornaco.android.thanos.widget.EditTextDialog;
import github.tornaco.android.thanos.widget.QuickDropdown;
import github.tornaco.android.thanos.widget.pref.ViewAwarePreference;
import github.tornaco.permission.requester.RequiresPermission;
import github.tornaco.permission.requester.RuntimePermissions;
import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;
import now.fortuitous.app.donate.DonateSettingsKt;
import now.fortuitous.thanos.apps.AppDetailsActivity;
import now.fortuitous.thanos.main.ChooserActivity;
import now.fortuitous.thanos.pref.AppPreference;
import now.fortuitous.thanos.settings.access.SettingsAccessRecordViewerActivity;
import rx2.android.schedulers.AndroidSchedulers;
import util.CollectionUtils;

@RuntimePermissions
public class SettingsDashboardFragment extends BasePreferenceFragmentCompat {
    private final static int REQUEST_CODE_BACKUP_FILE_PICK = 0x100;
    private final static int REQUEST_CODE_RESTORE_FILE_PICK = 0x200;
    private final static int REQUEST_CODE_BACKUP_FILE_PICK_Q = 0x300;

    private int buildInfoClickTimes = 0;
    private ExportPatchUi exportPatchUi;
    private ExportLogUi exportLogUi;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        exportPatchUi = ExportPatchUi.from(this);
        exportLogUi = ExportLogUi.from(this);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_dashboard, rootKey);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onBindPreferences() {
        super.onBindPreferences();

        onBindUIPreferences();
        onBindStrategyPreferences();
        onBindGeneralPreferences();
        onBindDataPreferences();
        onBindDevPreferences();
        onBindAboutPreferences();
    }

    protected void onBindUIPreferences() {
        SwitchPreferenceCompat classicHomeStyle = findPreference(getString(R.string.key_classic_home));
        classicHomeStyle.setChecked(AppPreference.isFeatureNoticeAccepted(getContext(), PREF_KEY_CLASSIC_HOME));
        classicHomeStyle.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                boolean classicHomeChecked = (boolean) newValue;
                AppPreference.setFeatureNoticeAccepted(getContext(), PREF_KEY_CLASSIC_HOME, classicHomeChecked);
                requireActivity().finishAffinity();
                return true;
            }
        });

        DropDownPreference iconPref = findPreference(getString(R.string.key_app_icon_pack));
        IconPackManager iconPackManager = IconPackManager.getInstance();
        final List<IconPack> packs = iconPackManager.getAvailableIconPacks(requireContext());
        final List<String> entries = new ArrayList<>();
        final List<String> values = new ArrayList<>();
        for (IconPack pack : packs) {
            entries.add(String.valueOf(pack.label));
            values.add(String.valueOf(pack.packageName));
        }
        // Default
        entries.add("Noop");
        values.add("Noop");

        iconPref.setEntries(entries.toArray(new String[0]));
        iconPref.setEntryValues(values.toArray(new String[0]));
        String current = AppThemePreferences.getInstance().getIconPack(requireContext(), null);

        iconPref.setSummary("Noop");
        iconPref.setValue("Noop");
        if (current != null) {
            IconPack pack = IconPackManager.getInstance().getIconPackage(requireContext(), current);
            if (pack != null) {
                iconPref.setSummary(pack.label);
            }
        }
        iconPref.setOnPreferenceChangeListener((preference, newValue) -> {
            AppThemePreferences.getInstance().setIconPack(requireContext(), String.valueOf(newValue));
            IconPack pack = IconPackManager.getInstance().getIconPackage(requireContext(), String.valueOf(newValue));
            if (pack != null && pack.isInstalled()) {
                preference.setSummary(pack.label);
                invalidateIconPack(pack);
            } else {
                preference.setSummary("Noop");
            }
            return true;
        });

        SwitchPreferenceCompat usedCircleIconPref = findPreference(getString(R.string.key_use_round_icon));
        AppFeatureManager.INSTANCE.withSubscriptionStatus(requireContext(), isSubscribed -> {
            usedCircleIconPref.setVisible(isSubscribed);
            return null;
        });

        usedCircleIconPref.setChecked(AppThemePreferences.getInstance().useRoundIcon(requireContext()));
        usedCircleIconPref.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean use = (boolean) newValue;
            AppThemePreferences.getInstance().setUseRoundIcon(requireContext(), use);
            return true;
        });

        SwitchPreferenceCompat showPkg = findPreference(R.string.key_app_list_item_show_pkg);
        showPkg.setChecked(CommonPreferences.getInstance().isAppListShowPkgNameEnabled(requireContext()));
        showPkg.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean newBoolValue = (boolean) newValue;
            CommonPreferences.getInstance().setAppListShowPkgNameEnabled(requireContext(), newBoolValue);
            return true;
        });

        SwitchPreferenceCompat showVersion = findPreference(R.string.key_app_list_item_show_version);
        showVersion.setChecked(CommonPreferences.getInstance().isAppListShowVersionEnabled(requireContext()));
        showVersion.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean newBoolValue = (boolean) newValue;
            CommonPreferences.getInstance().setAppListShowVersionEnabled(requireContext(), newBoolValue);
            return true;
        });
    }

    private void invalidateIconPack(IconPack pack) {
        GlideApp.get(requireContext()).clearMemory();
        Completable.fromRunnable(() -> {
            GlideApp.get(requireContext()).clearDiskCache();
            pack.getAllDrawables();
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    protected void onBindStrategyPreferences() {
        ThanosManager thanos = ThanosManager.from(getContext());
        if (!thanos.isServiceInstalled()) {
            return;
        }

        // Auto config.
        SwitchPreferenceCompat autoConfigPref =
                findPreference(getString(R.string.key_new_installed_apps_config_enabled));
        autoConfigPref.setChecked(
                thanos.isServiceInstalled()
                        && thanos.getProfileManager().isAutoApplyForNewInstalledAppsEnabled());
        autoConfigPref.setOnPreferenceChangeListener(
                (preference, newValue) -> {
                    if (thanos.isServiceInstalled()) {
                        boolean checked = (boolean) newValue;
                        thanos.getProfileManager().setAutoApplyForNewInstalledAppsEnabled(checked);
                    }
                    return true;
                });
        updateAutoConfigSelection();
        updateConfigTemplatePrefs();
    }

    private void updateAutoConfigSelection() {
        ThanosManager thanos = ThanosManager.from(getContext());
        ProfileManager profileManager = thanos.getProfileManager();
        List<String> entries = new ArrayList<>();
        List<String> values = new ArrayList<>();
        String selectedId = profileManager.getAutoConfigTemplateSelectionId();
        ConfigTemplate selectedTemplate = profileManager.getConfigTemplateById(selectedId);
        String valueNotSet = getString(github.tornaco.android.thanos.res.R.string.common_text_value_not_set);

        CollectionUtils.consumeRemaining(
                profileManager.getAllConfigTemplates(),
                template -> {
                    entries.add(template.getTitle());
                    values.add(template.getId());
                });
        DropDownPreference newInstalledAppsConfig =
                findPreference(getString(R.string.key_new_installed_apps_config));
        Objects.requireNonNull(newInstalledAppsConfig).setEntries(entries.toArray(new String[0]));
        newInstalledAppsConfig.setEntryValues(values.toArray(new String[0]));
        newInstalledAppsConfig.setValue(selectedId);
        newInstalledAppsConfig.setSummary(
                selectedTemplate == null ? valueNotSet : selectedTemplate.getTitle());
        newInstalledAppsConfig.setOnPreferenceChangeListener(
                (preference, newValue) -> {
                    String newId = (String) newValue;
                    profileManager.setAutoConfigTemplateSelection(newId);
                    updateAutoConfigSelection();
                    return true;
                });
    }

    private void updateConfigTemplatePrefs() {
        PreferenceCategory templatesCategory =
                findPreference(getString(R.string.key_config_template_category));
        Objects.requireNonNull(templatesCategory).removeAll();

        ViewAwarePreference addPref = new ViewAwarePreference(requireContext());
        addPref.setTitle(github.tornaco.android.thanos.res.R.string.common_fab_title_add);
        addPref.setIcon(github.tornaco.android.thanos.module.common.R.drawable.module_common_ic_add_fill);
        addPref.setOnPreferenceClickListener(preference -> {
            requestAddTemplate();
            return true;
        });
        templatesCategory.addPreference(addPref);

        ThanosManager thanos = ThanosManager.from(getContext());
        ProfileManager profileManager = thanos.getProfileManager();
        for (ConfigTemplate template : profileManager.getAllConfigTemplates()) {
            ViewAwarePreference tp = new ViewAwarePreference(requireContext());
            tp.setTitle(template.getTitle());
            tp.setKey(template.getId());
            tp.setDefaultValue(template);
            tp.setOnPreferenceClickListener(
                    preference -> {
                        ViewAwarePreference vp = (ViewAwarePreference) preference;
                        showConfigTemplateOptionsDialog(template, vp.getView());
                        return true;
                    });
            templatesCategory.addPreference(tp);
        }
    }

    private void requestAddTemplate() {
        EditTextDialog.show(
                getActivity(),
                getString(github.tornaco.android.thanos.res.R.string.pref_action_create_new_config_template),
                content -> {
                    if (TextUtils.isEmpty(content)) {
                        return;
                    }
                    String uuid = UUID.randomUUID().toString();
                    ConfigTemplate template =
                            ConfigTemplate.builder()
                                    .title(content)
                                    .id(uuid)
                                    .dummyPackageName(
                                            ProfileManager
                                                    .PROFILE_AUTO_APPLY_NEW_INSTALLED_APPS_CONFIG_TEMPLATE_PACKAGE_PREFIX
                                                    + uuid)
                                    .createAt(System.currentTimeMillis())
                                    .build();
                    boolean added =
                            ThanosManager.from(getContext()).getProfileManager().addConfigTemplate(template);
                    if (added) {
                        updateConfigTemplatePrefs();
                        updateAutoConfigSelection();
                    }
                });
    }

    private void showConfigTemplateOptionsDialog(ConfigTemplate template, View anchor) {
        QuickDropdown.show(
                requireActivity(),
                anchor,
                input -> {
                    switch (input) {
                        case 0:
                            return getString(github.tornaco.android.thanos.res.R.string.pref_action_edit_or_view_config_template);
                        case 1:
                            return getString(github.tornaco.android.thanos.res.R.string.pref_action_delete_config_template);
                    }
                    return null;
                },
                id -> {
                    switch (id) {
                        case 0:
                            AppInfo appInfo = new AppInfo();
                            appInfo.setSelected(false);
                            appInfo.setPkgName(template.getDummyPackageName());
                            appInfo.setAppLabel(template.getTitle());
                            appInfo.setDummy(true);
                            appInfo.setVersionCode(-1);
                            appInfo.setVersionCode(-1);
                            appInfo.setUid(-1);
                            appInfo.setUserId(0);
                            AppDetailsActivity.start(getActivity(), appInfo);
                            break;
                        case 1:
                            if (ThanosManager.from(getContext())
                                    .getProfileManager()
                                    .deleteConfigTemplate(template)) {
                                updateConfigTemplatePrefs();
                                updateAutoConfigSelection();
                            }
                            break;
                    }
                });
    }

    protected void onBindGeneralPreferences() {
        ThanosManager thanos = ThanosManager.from(getContext());
        if (thanos.isServiceInstalled()) {
            SwitchPreferenceCompat powerSavePref = findPreference(getString(R.string.key_enable_power_save));
            powerSavePref.setChecked(thanos.getPowerManager().isPowerSaveModeEnabled());
            powerSavePref.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean checked = (boolean) newValue;
                thanos.getPowerManager().setPowerSaveModeEnabled(checked);
                return true;
            });

            SwitchPreferenceCompat globalWhitelistPref = findPreference(getString(R.string.key_enable_global_white_list));
            if (thanos.isServiceInstalled()) {
                globalWhitelistPref.setChecked(thanos.getPkgManager().isProtectedWhitelistEnabled());
                globalWhitelistPref.setOnPreferenceChangeListener((preference, newValue) -> {
                    boolean checked = (boolean) newValue;
                    thanos.getPkgManager().setProtectedWhitelistEnabled(checked);
                    return true;
                });
            }

            findPreference(getString(R.string.key_feature_toggle)).setOnPreferenceClickListener(preference -> {
                FeatureToggleActivity.start(getContext());
                return true;
            });
        }
    }


    @SuppressWarnings("ConstantConditions")
    protected void onBindDataPreferences() {
        // Backup
        findPreference(getString(R.string.key_data_backup)).setOnPreferenceClickListener(preference -> {
            if (OsUtils.isTOrAbove()) {
                SettingsDashboardFragmentPermissionRequester.backupRequestedTOrAboveChecked(SettingsDashboardFragment.this);
            } else {
                SettingsDashboardFragmentPermissionRequester.backupRequestedTBelowChecked(SettingsDashboardFragment.this);
            }
            return true;
        });
        findPreference(getString(R.string.key_data_restore)).setOnPreferenceClickListener(preference -> {
            if (OsUtils.isTOrAbove()) {
                SettingsDashboardFragmentPermissionRequester.restoreRequestedTOrAboveChecked(SettingsDashboardFragment.this);
            } else {
                SettingsDashboardFragmentPermissionRequester.restoreRequestedTBelowChecked(SettingsDashboardFragment.this);
            }
            return true;
        });
        findPreference(getString(R.string.key_restore_default)).setOnPreferenceClickListener(preference -> {
            new MaterialAlertDialogBuilder(getActivity())
                    .setTitle(github.tornaco.android.thanos.res.R.string.pre_title_restore_default)
                    .setMessage(github.tornaco.android.thanos.res.R.string.common_dialog_message_are_you_sure)
                    .setPositiveButton(android.R.string.ok, (dialog, which) ->
                            ThanosManager.from(getActivity())
                                    .ifServiceInstalled(thanosManager -> {
                                        if (thanosManager.getBackupAgent().restoreDefault()) {
                                            new MaterialAlertDialogBuilder(getActivity())
                                                    .setMessage(getString(github.tornaco.android.thanos.res.R.string.pre_message_restore_success))
                                                    .setCancelable(false)
                                                    .setPositiveButton(android.R.string.ok, null)
                                                    .show();
                                        } else {
                                            new MaterialAlertDialogBuilder(getActivity())
                                                    .setMessage("Error:(")
                                                    .setCancelable(false)
                                                    .setPositiveButton(android.R.string.ok, null)
                                                    .show();
                                        }
                                    }))
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();

            return true;
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        XLog.d("onActivityResult: %s %s %s", requestCode, resultCode, ObjectToStringUtils.intentToString(data));
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_RESTORE_FILE_PICK) {
            onRestoreFilePickRequestResult(data);
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_BACKUP_FILE_PICK) {
            onBackupFilePickRequestResult(data);
        } else if (requestCode == REQUEST_CODE_BACKUP_FILE_PICK_Q && resultCode == Activity.RESULT_OK) {
            onBackupFilePickRequestResultQ(data);
        } else if (requestCode == ExportPatchUi.REQUEST_CODE_EXPORT_MAGISK_FILE_PICKED && resultCode == Activity.RESULT_OK) {
            exportPatchUi.handleActivityResult(requestCode, resultCode, data);
        } else if (requestCode == ExportLogUi.REQUEST_CODE_EXPORT_LOG_FILE_PICKED && resultCode == Activity.RESULT_OK) {
            exportLogUi.handleActivityResult(requestCode, resultCode, data);
        }
    }

    private void onRestoreFilePickRequestResult(@Nullable Intent data) {
        if (data == null) {
            XLog.e("No data.");
            return;
        }

        if (getActivity() == null) return;

        Uri uri = data.getData();
        if (uri == null) {
            Toast.makeText(getActivity(), "uri == null", Toast.LENGTH_LONG).show();
            XLog.e("No uri.");
            return;
        }

        Optional.ofNullable(getActivity())
                .ifPresent(fragmentActivity -> obtainViewModel(fragmentActivity)
                        .performRestore(new SettingsViewModel.RestoreListener() {
                            @Override
                            public void onSuccess() {
                                if (getActivity() == null) return;
                                Completable.fromAction(() ->
                                                new MaterialAlertDialogBuilder(getActivity())
                                                        .setMessage(getString(github.tornaco.android.thanos.res.R.string.pre_message_restore_success))
                                                        .setCancelable(false)
                                                        .setPositiveButton(android.R.string.ok, null)
                                                        .show())
                                        .subscribeOn(AndroidSchedulers.mainThread())
                                        .subscribe();
                            }

                            @Override
                            public void onFail(String errMsg) {
                                Completable.fromAction(() ->
                                                Toast.makeText(
                                                        fragmentActivity.getApplicationContext(),
                                                        errMsg, Toast.LENGTH_LONG).show())
                                        .subscribeOn(AndroidSchedulers.mainThread())
                                        .subscribe();
                            }
                        }, uri));
    }

    private void onBackupFilePickRequestResultQ(Intent data) {
        if (data == null) {
            XLog.e("No data.");
            return;
        }

        Uri fileUri = data.getData();

        if (fileUri == null) {
            Toast.makeText(getActivity(), "fileUri == null", Toast.LENGTH_LONG).show();
            XLog.e("No fileUri.");
            return;
        }

        XLog.d("fileUri == %s", fileUri);

        onBackupFileAvailableQ(fileUri);
    }

    private void onBackupFileAvailableQ(@NonNull Uri fileUri) {
        try {
            OutputStream os = requireContext().getContentResolver().openOutputStream(fileUri);
            invokeBackup(fileUri.getPath(), os);
        } catch (IOException e) {
            XLog.e(e);
            Toast.makeText(getActivity(), Log.getStackTraceString(e), Toast.LENGTH_LONG).show();
        }
    }

    private void onBackupFilePickRequestResult(Intent data) {
        if (data == null) {
            XLog.e("No data.");
            return;
        }

        if (getActivity() == null) return;

        List<Uri> files = Utils.getSelectedFilesFromResult(data);
        if (CollectionUtils.isNullOrEmpty(files)) {
            Toast.makeText(getActivity(), "No selection", Toast.LENGTH_LONG).show();
            return;
        }
        File file = Utils.getFileForUriNoThrow(files.get(0));
        XLog.w("onBackupFilePickRequestResult file is: %s", file);

        if (file == null) {
            Toast.makeText(getActivity(), "file == null", Toast.LENGTH_LONG).show();
            return;
        }

        if (!file.isDirectory()) {
            Toast.makeText(getActivity(), "file is not dir", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            String backupFileNameWithExt = "Thanox-Backup-" + DateUtils.formatForFileName(System.currentTimeMillis()) + ".zip";
            File destFile = new File(file, backupFileNameWithExt);
            Files.createParentDirs(destFile);
            //noinspection UnstableApiUsage
            invokeBackup(destFile.getAbsolutePath(), Files.asByteSink(destFile).openStream());
        } catch (IOException e) {
            XLog.e(e);
            Toast.makeText(getActivity(), Log.getStackTraceString(e), Toast.LENGTH_LONG).show();
        }
    }

    private void invokeBackup(String destPathToTellUser, OutputStream os) {
        Optional.ofNullable(getActivity())
                .ifPresent(fragmentActivity -> obtainViewModel(fragmentActivity)
                        .performBackup(new SettingsViewModel.BackupListener() {
                            @Override
                            public void onSuccess() {
                                if (getActivity() == null) return;
                                new MaterialAlertDialogBuilder(getActivity())
                                        .setMessage(getString(github.tornaco.android.thanos.res.R.string.pre_message_backup_success) + "\n" + destPathToTellUser)
                                        .setCancelable(true)
                                        .setPositiveButton(android.R.string.ok, null)
                                        .show();
                            }

                            @Override
                            public void onFail(String errMsg) {
                                Toast.makeText(fragmentActivity.getApplicationContext(), errMsg, Toast.LENGTH_LONG).show();
                            }
                        }, os));
    }

    @RequiresPermission({
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO,
    })
    void backupRequestedTOrAbove() {
        if (OsUtils.isQOrAbove()) {
            backupRequestedQAndAbove();
        } else {
            backupRequestedQBelow();
        }
    }

    @RequiresPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void backupRequestedTBelow() {
        if (OsUtils.isQOrAbove()) {
            backupRequestedQAndAbove();
        } else {
            backupRequestedQBelow();
        }
    }


    private void backupRequestedQAndAbove() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // you can set file mime-type
        intent.setType("*/*");
        // default file name
        String backupFileNameWithExt = "Thanox-Backup-" + DateUtils.formatForFileName(System.currentTimeMillis()) + ".zip";
        intent.putExtra(Intent.EXTRA_TITLE, backupFileNameWithExt);
        try {
            startActivityForResult(intent, REQUEST_CODE_BACKUP_FILE_PICK_Q);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(requireContext(), "Activity not found, please install Files app", Toast.LENGTH_LONG).show();
        }

    }

    private void backupRequestedQBelow() {
        Intent i = new Intent(getContext(), FilePickerActivity.class);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, true);
        i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR);
        // Configure initial directory by specifying a String.
        // You could specify a String like "/storage/emulated/0/", but that can
        // dangerous. Always use Android's API calls to getSingleton paths to the SD-card or
        // internal memory.
        i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
        try {
            startActivityForResult(i, REQUEST_CODE_BACKUP_FILE_PICK);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(requireContext(), "Activity not found, please install Files app", Toast.LENGTH_LONG).show();
        }
    }

    @RequiresPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void restoreRequestedTBelow() {
        IntentUtils.startFilePickerActivityForRes(this, REQUEST_CODE_RESTORE_FILE_PICK);
    }

    @RequiresPermission({
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO,
    })
    void restoreRequestedTOrAbove() {
        IntentUtils.startFilePickerActivityForRes(this, REQUEST_CODE_RESTORE_FILE_PICK);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SettingsDashboardFragmentPermissionRequester.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private static SettingsViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(SettingsViewModel.class);
    }

    protected void onBindDevPreferences() {
        ThanosManager thanos = ThanosManager.from(getContext());

        Preference crashPref = findPreference(getString(R.string.key_crash));
        crashPref.setOnPreferenceClickListener(preference -> {
            throw new RuntimeException("Crash");
        });
        crashPref.setVisible(BuildProp.THANOS_BUILD_DEBUG);

        SwitchPreferenceCompat showCurrentActivityPref = findPreference(getString(R.string.key_show_current_activity));
        if (thanos.isServiceInstalled()) {
            showCurrentActivityPref.setChecked(thanos.getActivityStackSupervisor().isShowCurrentComponentViewEnabled());
            showCurrentActivityPref.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean checked = (boolean) newValue;
                thanos.getActivityStackSupervisor().setShowCurrentComponentViewEnabled(checked);
                return true;
            });

        } else {
            showCurrentActivityPref.setEnabled(false);
        }

        SwitchPreferenceCompat showNetStatPref = findPreference(getString(R.string.key_show_net_stat));
        if (thanos.isServiceInstalled()) {
            showNetStatPref.setChecked(thanos.getActivityManager().isNetStatTrackerEnabled());
            showNetStatPref.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean checked = (boolean) newValue;
                thanos.getActivityManager().setNetStatTrackerEnabled(checked);
                return true;
            });

        } else {
            showNetStatPref.setEnabled(false);
        }

        SwitchPreferenceCompat testOpsPref = findPreference(getString(R.string.key_test_new_ops));
        testOpsPref.setChecked(AppPreference.isFeatureNoticeAccepted(getContext(), "NEW_OPS"));
        testOpsPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                boolean useNewOps = (boolean) newValue;
                if (useNewOps) {
                    thanos.getAppOpsManager().setOpsEnabled(false);
                }
                AppPreference.setFeatureNoticeAccepted(getContext(), "NEW_OPS", useNewOps);
                return true;
            }
        });

        findPreference(getString(R.string.key_theme_attr_preview)).setVisible(BuildProp.THANOS_BUILD_DEBUG);
        findPreference(getString(R.string.key_theme_attr_preview)).setOnPreferenceClickListener(preference -> {
            ActivityUtils.startActivity(requireActivity(), ThemeAttrPreviewActivity.class);
            return true;
        });

        findPreference(getString(R.string.key_settings_record_viewer)).setVisible(BuildProp.THANOS_BUILD_DEBUG);
        findPreference(getString(R.string.key_settings_record_viewer)).setOnPreferenceClickListener(preference -> {
            SettingsAccessRecordViewerActivity.Starter.INSTANCE.start(requireActivity());
            return true;
        });
    }

    protected void onBindAboutPreferences() {
        ThanosManager thanos = ThanosManager.from(getContext());

        findPreference(getString(R.string.key_feedback)).setOnPreferenceClickListener(preference -> {
            if (OsUtils.isROrAbove()) {
                exportLogUi.show(() -> {
                    if (OsUtils.isTOrAbove()) {
                        SettingsDashboardFragmentPermissionRequester.exportLogRequestedTOrAboveChecked(SettingsDashboardFragment.this);
                    } else {
                        SettingsDashboardFragmentPermissionRequester.exportLogRequestedTBelowChecked(SettingsDashboardFragment.this);
                    }
                });
            }
            return true;
        });

        // Build.
        findPreference(getString(R.string.key_build_info_app))
                .setSummary(
                        BuildConfig.VERSION_NAME
                                + "\n"
                                + BuildProp.THANOS_BUILD_FINGERPRINT
                                + "\n"
                                + BuildProp.THANOS_BUILD_DATE
                                + "\n"
                                + BuildProp.THANOS_BUILD_HOST);

        if (thanos.isServiceInstalled()) {
            findPreference(getString(R.string.key_build_info_server))
                    .setSummary(thanos.getVersionName() + "\n" + thanos.fingerPrint());
            findPreference(getString(R.string.key_patch_info)).setSummary(String.join("\n", thanos.getPatchingSource()));
        } else {
            findPreference(getString(R.string.key_build_info_server))
                    .setSummary(github.tornaco.android.thanos.res.R.string.status_not_active);
            findPreference(getString(R.string.key_patch_info)).setSummary("N/A");
        }

        findPreference(getString(R.string.key_patch_info)).setOnPreferenceClickListener(preference -> {
            if (OsUtils.isROrAbove()) {
                exportPatchUi.show(() -> {
                    if (OsUtils.isTOrAbove()) {
                        SettingsDashboardFragmentPermissionRequester.exportMagiskZipRequestedTOrAboveChecked(SettingsDashboardFragment.this);
                    } else {
                        SettingsDashboardFragmentPermissionRequester.exportMagiskZipRequestedTBelowChecked(SettingsDashboardFragment.this);
                    }
                });
            }
            return true;
        });

        findPreference(getString(R.string.key_build_info_server))
                .setOnPreferenceClickListener(preference -> {
                    buildInfoClickTimes += 1;
                    if (buildInfoClickTimes >= 10) {
                        buildInfoClickTimes = 0;
                        // showBuildProp();
                        ChooserActivity.Starter.start(requireContext());
                    }
                    return false;
                });

        // About.
        Preference donatePref = findPreference(getString(R.string.key_donate));
        donatePref.setOnPreferenceClickListener(
                preference -> {
                    NavActivityPluginKt.launchSubscribeActivity(requireActivity(), () -> null);
                    return true;
                });

        if (DonateSettingsKt.INSTANCE.isActivated(requireContext(), () -> null)) {
            donatePref.setSummary(github.tornaco.android.thanos.res.R.string.module_donate_donated);
        }

        Preference licensePref = findPreference(getString(R.string.key_open_source_license));
        licensePref.setOnPreferenceClickListener(
                preference12 -> {
                    LicenseHelper.showLicenseDialog(getActivity());
                    return true;
                });

        donatePref.setVisible(BuildProp.THANOS_BUILD_FLAVOR.equals("prc"));

        findPreference(getString(R.string.key_email)).setSummary(BuildProp.THANOX_CONTACT_EMAIL);

        findPreference(getString(R.string.key_rss_e)).setOnPreferenceClickListener(preference -> {
            showTgAndQQDialog();
            return true;
        });
    }

    private void showTgAndQQDialog() {
        new MaterialAlertDialogBuilder(requireActivity())
                .setTitle(github.tornaco.android.thanos.res.R.string.pref_title_rss_e)
                .setMessage(github.tornaco.android.thanos.res.R.string.pref_summary_rss_e)
                .setPositiveButton("QQ", (dialog, which) -> {
                    ClipboardUtils.copyToClipboard(requireActivity(), "thanox QQ", BuildProp.THANOX_QQ_PRIMARY);
                    Toast.makeText(requireContext(), github.tornaco.android.thanos.res.R.string.common_toast_copied_to_clipboard, Toast.LENGTH_LONG).show();
                }).setNegativeButton("TG", (dialog, which) -> BrowserUtils.launch(getActivity(), BuildProp.THANOX_TG_CHANNEL)).show();
    }

    private void showBuildProp() {
        BuildPropActivity.Starter.INSTANCE.start(getActivity());
    }

    @RequiresPermission({
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO,
    })
    void exportMagiskZipRequestedTOrAbove() {
        // Noop, just request perm.
    }

    @RequiresPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void exportMagiskZipRequestedTBelow() {
        // Noop, just request perm.
    }

    @RequiresPermission({
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO,
    })
    void exportLogRequestedTOrAbove() {
        // Noop, just request perm.
    }

    @RequiresPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void exportLogRequestedTBelow() {
        // Noop, just request perm.
    }
}
