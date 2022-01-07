package github.tornaco.android.thanos.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.StringRes;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import github.tornaco.android.thanos.module.common.R;
import util.Consumer;

public class EditTextDialog {

    public static void show(Context context, @StringRes int titleRes, Consumer<String> stringConsumer) {
        show(context, context.getString(titleRes), stringConsumer);
    }

    public static void show(Context context, String title, Consumer<String> stringConsumer) {
        show(context, title, null, stringConsumer);
    }

    public static void show(Context context, String title, String hint, Consumer<String> stringConsumer) {
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        View editorLayout = LayoutInflater.from(context).inflate(R.layout.common_dialog_edittext, null, false);
        builder.setView(editorLayout);
        final EditText editText = editorLayout.findViewById(R.id.editor);
        editText.setHint(hint);

        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setPositiveButton(
                context.getString(android.R.string.ok),
                (dialog, which) -> {
                    String content = editText.getEditableText().toString();
                    stringConsumer.accept(content);
                });
        builder.setNegativeButton(context.getString(android.R.string.cancel), (dialog, which) -> {
            // Noop.
        });
        builder.show();
    }
}
