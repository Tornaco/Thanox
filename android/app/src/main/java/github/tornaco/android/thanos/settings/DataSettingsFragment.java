package github.tornaco.android.thanos.settings;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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
import java.util.List;

import github.tornaco.android.thanos.BaseWithFabPreferenceFragmentCompat;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.util.DateUtils;
import github.tornaco.android.thanos.core.util.ObjectToStringUtils;
import github.tornaco.android.thanos.core.util.Optional;
import github.tornaco.android.thanos.core.util.OsUtils;
import github.tornaco.android.thanos.util.IntentUtils;
import github.tornaco.permission.requester.RequiresPermission;
import github.tornaco.permission.requester.RuntimePermissions;
import io.reactivex.Completable;
import rx2.android.schedulers.AndroidSchedulers;
import util.CollectionUtils;

@RuntimePermissions
public class DataSettingsFragment extends BaseWithFabPreferenceFragmentCompat {

    private final static int REQUEST_CODE_BACKUP_FILE_PICK = 0x100;
    private final static int REQUEST_CODE_BACKUP_FILE_PICK_Q = 0x300;
    private final static int REQUEST_CODE_RESTORE_FILE_PICK = 0x200;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.data_settings_pref, rootKey);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onBindPreferences() {
        super.onBindPreferences();
        // Backup
        findPreference(getString(R.string.key_data_backup)).setOnPreferenceClickListener(preference -> {
            DataSettingsFragmentPermissionRequester.backupRequestedChecked(DataSettingsFragment.this);
            return true;
        });
        findPreference(getString(R.string.key_data_restore)).setOnPreferenceClickListener(preference -> {
            DataSettingsFragmentPermissionRequester.restoreRequestedChecked(DataSettingsFragment.this);
            return true;
        });
        findPreference(getString(R.string.key_restore_default)).setOnPreferenceClickListener(preference -> {
            new MaterialAlertDialogBuilder(getActivity())
                    .setMessage(R.string.pre_title_restore_default)
                    .setPositiveButton(android.R.string.ok, (dialog, which) ->
                            ThanosManager.from(getActivity())
                                    .ifServiceInstalled(thanosManager -> {
                                        if (thanosManager.getBackupAgent().restoreDefault()) {
                                            new MaterialAlertDialogBuilder(getActivity())
                                                    .setMessage(getString(R.string.pre_message_restore_success))
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
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_BACKUP_FILE_PICK) {
            onBackupFilePickRequestResult(data);
        }
        if (requestCode == REQUEST_CODE_BACKUP_FILE_PICK_Q && resultCode == Activity.RESULT_OK) {
            onBackupFilePickRequestResultQ(data);
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
                                                .setMessage(getString(R.string.pre_message_restore_success))
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
                                        .setMessage(getString(R.string.pre_message_backup_success) + "\n" + destPathToTellUser)
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

    @RequiresPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void backupRequested() {
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
    void restoreRequested() {
        IntentUtils.startFilePickerActivityForRes(this, REQUEST_CODE_RESTORE_FILE_PICK);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        DataSettingsFragmentPermissionRequester.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private static SettingsViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(SettingsViewModel.class);
    }
}
