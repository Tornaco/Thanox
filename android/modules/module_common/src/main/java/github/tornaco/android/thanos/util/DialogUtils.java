package github.tornaco.android.thanos.util;

import android.content.Context;

import androidx.annotation.StringRes;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

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
}
