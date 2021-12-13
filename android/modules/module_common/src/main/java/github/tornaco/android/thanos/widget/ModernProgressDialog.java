package github.tornaco.android.thanos.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.StringRes;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import github.tornaco.android.thanos.module.common.R;

public class ModernProgressDialog {
    private final Context context;
    private String title;
    private String message;

    private Dialog dialog;
    private TextView messageView;

    public ModernProgressDialog(Context context) {
        this.context = context;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
        if (isShowing() && messageView != null) {
            messageView.setText(message);
        }
    }

    public void setTitle(@StringRes int titleRes) {
        this.title = context.getString(titleRes);
    }

    public void setMessage(@StringRes int messageRes) {
        this.message = context.getString(messageRes);
    }

    public void show() {
        View progressLayout = LayoutInflater.from(context)
                .inflate(R.layout.common_dialog_progress, null, false);
        messageView = progressLayout.findViewById(R.id.message);
        messageView.setText(message);
        this.dialog = new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setView(progressLayout)
                .setCancelable(false)
                .show();
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public boolean isShowing() {
        return dialog != null && dialog.isShowing();
    }
}
