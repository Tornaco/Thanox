package github.tornaco.android.thanos.widget;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import github.tornaco.android.thanos.module.common.R;
import lombok.Setter;

@Setter
public class ModernAlertDialog extends AlertDialog {
    private String dialogTitle;
    private String dialogMessage;
    private String positive;
    private String negative;
    private String neutral;
    @Nullable
    private Runnable onPositive;
    @Nullable
    private Runnable onNegative;
    @Nullable
    private Runnable onNeutral;

    public ModernAlertDialog(@NonNull Context context) {
        super(context);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_modern_dialog);

        // getWindow().getAttributes().gravity = Gravity.BOTTOM;

        TextView titleView = findViewById(R.id.title);
        TextView messageView = findViewById(R.id.message);
        TextView positiveView = findViewById(R.id.positive);
        TextView negativeView = findViewById(R.id.negative);
        TextView neutralView = findViewById(R.id.neutral);

        titleView.setVisibility(dialogTitle == null ? View.GONE : View.VISIBLE);
        messageView.setVisibility(dialogMessage == null ? View.GONE : View.VISIBLE);
        positiveView.setVisibility(positive == null ? View.GONE : View.VISIBLE);
        negativeView.setVisibility(negative == null ? View.GONE : View.VISIBLE);
        neutralView.setVisibility(neutral == null ? View.GONE : View.VISIBLE);

        titleView.setText(dialogTitle);
        messageView.setText(dialogMessage);
        positiveView.setText(positive);
        negativeView.setText(negative);
        neutralView.setText(neutral);

        positiveView.setOnClickListener(view -> {
            if (onPositive != null) {
                onPositive.run();
            }
            dismiss();
        });

        negativeView.setOnClickListener(view -> {
            if (onNegative != null) {
                onNegative.run();
            }
            dismiss();
        });

        neutralView.setOnClickListener(view -> {
            if (onNeutral != null) {
                onNeutral.run();
            }
            dismiss();
        });
    }
}
