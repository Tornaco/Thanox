package github.tornaco.android.thanos.settings;

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

import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.core.util.ObjectToStringUtils;
import github.tornaco.android.thanos.core.util.OsUtils;
import github.tornaco.android.thanos.core.util.PkgUtils;
import github.tornaco.android.thanos.util.ToastUtils;
import github.tornaco.android.thanos.widget.ModernAlertDialog;
import github.tornaco.android.thanos.widget.ModernProgressDialog;
import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;
import rx2.android.schedulers.AndroidSchedulers;
import util.IoUtils;

public class ExportPatchUi {
    public final static int REQUEST_CODE_EXPORT_MAGISK_FILE_PICKED = 0x400;

    private Activity activity;
    private Fragment fragment;

    public ExportPatchUi(Activity activity) {
        this.activity = activity;
    }

    public ExportPatchUi(Fragment fragment) {
        this.fragment = fragment;
    }

    public static ExportPatchUi from(Activity activity) {
        return new ExportPatchUi(activity);
    }

    public static ExportPatchUi from(Fragment fragment) {
        return new ExportPatchUi(fragment);
    }

    public void show(Runnable permissionRequester) {
        Context context = requireContext();
        ModernAlertDialog dialog = new ModernAlertDialog(requireContext());
        dialog.setDialogTitle(context.getString(R.string.export_patch_title));
        dialog.setDialogMessage(context.getString(R.string.export_patch_message));
        dialog.setCancelable(true);
        dialog.setPositive(context.getString(R.string.export_patch_export_magisk));
        dialog.setNegative(context.getString(android.R.string.cancel));
        dialog.setOnPositive(() -> {
            if (hasPermission(context)) {
                permissionRequester.run();
            } else {
                exportMagiskZipQAndAbove();
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
        if (requestCode == REQUEST_CODE_EXPORT_MAGISK_FILE_PICKED && resultCode == Activity.RESULT_OK) {
            onExportMagiskFilePickRequestResultQAsync(data);
        }
    }

    private void exportMagiskZipQAndAbove() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // you can set file mime-type
        intent.setType("*/*");
        // default file name
        String backupFileNameWithExt = "zygisk_thanox-" + BuildProp.THANOS_VERSION_NAME + ".zip";
        intent.putExtra(Intent.EXTRA_TITLE, backupFileNameWithExt);
        try {
            if (activity != null) {
                activity.startActivityForResult(intent, REQUEST_CODE_EXPORT_MAGISK_FILE_PICKED);
            } else {
                fragment.startActivityForResult(intent, REQUEST_CODE_EXPORT_MAGISK_FILE_PICKED);
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(requireContext(), "Activity not found, please install Files app", Toast.LENGTH_LONG).show();
        }
    }

    private void onExportMagiskFilePickRequestResultQAsync(Intent data) {
        ModernProgressDialog dialog = new ModernProgressDialog(requireContext());
        dialog.setMessage(R.string.common_text_wait_a_moment);
        dialog.show();
        Completable.fromAction(() -> {
            boolean success = onExportMagiskFilePickRequestResultQ(data);
            Completable.fromRunnable(() -> {
                dialog.dismiss();
                if (success) {
                    ToastUtils.ok(requireContext());
                } else {
                    ToastUtils.nook(requireContext());
                }
            }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    private boolean onExportMagiskFilePickRequestResultQ(Intent data) {
        if (data == null) {
            XLog.e("No data.");
            return false;
        }

        Uri fileUri = data.getData();

        if (fileUri == null) {
            Toast.makeText(requireContext(), "fileUri == null", Toast.LENGTH_LONG).show();
            XLog.e("No fileUri.");
            return false;
        }

        XLog.d("fileUri == %s", fileUri);
        OutputStream os = null;
        try {
            os = requireContext().getContentResolver().openOutputStream(fileUri);
            //noinspection UnstableApiUsage
            Files.asByteSource(
                            new File(Objects.requireNonNull(PkgUtils.getApkPath(requireContext(), requireContext().getPackageName()))))
                    .copyTo(os);
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
