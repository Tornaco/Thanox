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

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.elvishew.xlog.XLog;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.util.DateUtils;
import github.tornaco.android.thanos.core.util.FileUtils;
import github.tornaco.android.thanos.core.util.ObjectToStringUtils;
import github.tornaco.android.thanos.core.util.OsUtils;
import github.tornaco.android.thanos.widget.ModernAlertDialog;
import github.tornaco.android.thanos.widget.ModernProgressDialog;
import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;
import rx2.android.schedulers.AndroidSchedulers;
import util.IoUtils;

public class ExportLogUi {
    public final static int REQUEST_CODE_EXPORT_LOG_FILE_PICKED = 0x500;

    private Activity activity;
    private Fragment fragment;

    public ExportLogUi(Activity activity) {
        this.activity = activity;
    }

    public ExportLogUi(Fragment fragment) {
        this.fragment = fragment;
    }

    public static ExportLogUi from(Activity activity) {
        return new ExportLogUi(activity);
    }

    public static ExportLogUi from(Fragment fragment) {
        return new ExportLogUi(fragment);
    }

    public void show(Runnable permissionRequester) {
        Context context = requireContext();
        ModernAlertDialog dialog = new ModernAlertDialog(requireContext());
        dialog.setDialogTitle(context.getString(R.string.nav_title_feedback));
        dialog.setDialogMessage(context.getString(R.string.dialog_message_feedback));
        dialog.setCancelable(true);
        dialog.setPositive(context.getString(R.string.feedback_export_log));
        dialog.setNegative(context.getString(android.R.string.cancel));
        dialog.setOnPositive(() -> {
            if (hasPermission(context)) {
                permissionRequester.run();
            } else {
                exportLogZipQAndAbove();
            }
        });
        dialog.show();
    }

    private boolean hasPermission(Context context) {
        if (OsUtils.isTOrAbove()) return hasPermissionTOrAbove(context);
        else return hasPermissionTBelow(context);
    }

    private boolean hasPermissionTBelow(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED;
    }

    private boolean hasPermissionTOrAbove(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED;
    }

    public void handleActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        XLog.d("onActivityResult: %s %s %s", requestCode, resultCode, ObjectToStringUtils.intentToString(data));
        if (requestCode == REQUEST_CODE_EXPORT_LOG_FILE_PICKED && resultCode == Activity.RESULT_OK) {
            onExportLogFilePickRequestResultQAsync(data);
        }
    }

    private void exportLogZipQAndAbove() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // you can set file mime-type
        intent.setType("*/*");
        // default file name
        String backupFileNameWithExt = "Thanox-Log-" + DateUtils.formatForFileName(System.currentTimeMillis()) + ".zip";
        intent.putExtra(Intent.EXTRA_TITLE, backupFileNameWithExt);
        try {
            if (activity != null) {
                activity.startActivityForResult(intent, REQUEST_CODE_EXPORT_LOG_FILE_PICKED);
            } else {
                fragment.startActivityForResult(intent, REQUEST_CODE_EXPORT_LOG_FILE_PICKED);
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(requireContext(), "Activity not found, please install Files app", Toast.LENGTH_LONG).show();
        }
    }

    private void onExportLogFilePickRequestResultQAsync(Intent data) {
        ModernProgressDialog dialog = new ModernProgressDialog(requireContext());
        dialog.setMessage(github.tornaco.android.thanos.module.common.R.string.common_text_wait_a_moment);
        dialog.show();
        Completable.fromAction(() -> {
            boolean success = onExportLogFilePickRequestResultQ(data);
            Completable.fromRunnable(() -> {
                dialog.dismiss();
                if (success) {
                    Toast.makeText(requireContext(), R.string.feedback_export_log_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), R.string.feedback_export_log_fail, Toast.LENGTH_SHORT).show();
                }
            }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    private boolean onExportLogFilePickRequestResultQ(Intent data) {
        if (data == null) {
            XLog.e("onExportLogFilePickRequestResultQ No data.");
            return false;
        }

        Uri fileUri = data.getData();

        if (fileUri == null) {
            Toast.makeText(requireContext(), "fileUri == null", Toast.LENGTH_LONG).show();
            XLog.e("onExportLogFilePickRequestResultQ No fileUri.");
            return false;
        }

        XLog.d("fileUri == %s", fileUri);
        OutputStream os = null;
        try {
            os = requireContext().getContentResolver().openOutputStream(fileUri);
            if (os == null) {
                XLog.e("onExportLogFilePickRequestResultQ. os is null.");
                return false;
            }
            File logZipFile = ExportLogsKt.exportLogs(requireContext(), ThanosManager.from(requireContext()));
            if (logZipFile == null) {
                return false;
            }
            //noinspection UnstableApiUsage
            Files.asByteSource(logZipFile).copyTo(os);
            FileUtils.delete(logZipFile);
            return true;
        } catch (IOException e) {
            XLog.e(e);
        } finally {
            IoUtils.closeQuietly(os);
        }
        return false;
    }

    private Context requireContext() {
        return Objects.requireNonNull(activity != null ? activity : fragment.getContext(), "Context is null");
    }
}
