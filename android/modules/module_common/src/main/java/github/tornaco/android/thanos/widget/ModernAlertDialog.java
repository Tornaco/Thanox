package github.tornaco.android.thanos.widget;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ModernAlertDialog {
    private final Context context;

    private String dialogTitle;
    private CharSequence dialogMessage;
    private String positive;
    private String negative;
    private String neutral;
    @Nullable
    private Runnable onPositive;
    @Nullable
    private Runnable onNegative;
    @Nullable
    private Runnable onNeutral;

    private boolean cancelable;

    public ModernAlertDialog(@NonNull Context context) {
        this.context = context;
    }

    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

    public void setDialogMessage(CharSequence dialogMessage) {
        this.dialogMessage = dialogMessage;
    }

    public void setPositive(String positive) {
        this.positive = positive;
    }

    public void setNegative(String negative) {
        this.negative = negative;
    }

    public void setNeutral(String neutral) {
        this.neutral = neutral;
    }

    public void setOnPositive(@Nullable Runnable onPositive) {
        this.onPositive = onPositive;
    }

    public void setOnNegative(@Nullable Runnable onNegative) {
        this.onNegative = onNegative;
    }

    public void setOnNeutral(@Nullable Runnable onNeutral) {
        this.onNeutral = onNeutral;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public AlertDialog show() {
        return new MaterialAlertDialogBuilder(context)
                .setTitle(dialogTitle)
                .setMessage(dialogMessage)
                .setPositiveButton(positive, (dialog, which) -> {
                    if (onPositive != null) {
                        onPositive.run();
                    }
                })
                .setNegativeButton(negative, (dialog, which) -> {
                    if (onNegative != null) {
                        onNegative.run();
                    }
                })
                .setNeutralButton(neutral, (dialog, which) -> {
                    if (onNeutral != null) {
                        onNeutral.run();
                    }
                })
                .setCancelable(cancelable)
                .show();
    }
}
