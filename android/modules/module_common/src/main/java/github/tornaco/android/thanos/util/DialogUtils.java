package github.tornaco.android.thanos.util;

import android.content.Context;
import android.util.Log;

import androidx.annotation.StringRes;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import github.tornaco.android.thanos.core.util.ClipboardUtils;
import github.tornaco.android.thanos.module.common.R;

public class DialogUtils {
    private DialogUtils() {
    }

    public static void showMessage(Context context, @StringRes int titleRes, @StringRes int messageRes) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(titleRes)
                .setMessage(messageRes)
                .setCancelable(true)
                .show();
    }

    public static void showMessage(Context context, String title, String message) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .show();
    }

    public static void showError(Context context, Throwable error) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(R.string.module_common_error_occur)
                .setMessage(Log.getStackTraceString(error))
                .setCancelable(false)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(R.string.module_common_copy_error_message,
                        (dialog, which) -> ClipboardUtils.copyToClipboard(context, "error", Log.getStackTraceString(error)))
                .show();
    }
}
