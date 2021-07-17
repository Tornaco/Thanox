package github.tornaco.android.thanos.util;

import android.content.Context;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

public class DialogUtils {
    private DialogUtils() {
    }

    public static void showMessage(Context context, @StringRes int titleRes, @StringRes int messageRes) {
        new AlertDialog.Builder(context)
                .setTitle(titleRes)
                .setMessage(messageRes)
                .setCancelable(true)
                .show();
    }

    public static void showMessage(Context context, String titleRes, String messageRes) {
        new AlertDialog.Builder(context)
                .setTitle(titleRes)
                .setMessage(messageRes)
                .setCancelable(true)
                .show();
    }
}
