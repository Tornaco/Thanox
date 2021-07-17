package github.tornaco.android.thanos.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;

import github.tornaco.android.thanos.module.common.R;
import util.Consumer;

public class EditTextDialog extends AlertDialog {
  private AppCompatEditText editText;

  public static void show(Context context, String title, Consumer<String> stringConsumer) {
    new EditTextDialog(context, title, stringConsumer).show();
  }

  static int resolveDialogTheme(@NonNull Context context, @StyleRes int resid) {
    // Check to see if this resourceId has a valid package ID.
    if (((resid >>> 24) & 0x000000ff) >= 0x00000001) { // start of real resource IDs.
      return resid;
    } else {
      TypedValue outValue = new TypedValue();
      context.getTheme().resolveAttribute(R.attr.alertDialogTheme, outValue, true);
      return outValue.resourceId;
    }
  }

  @SuppressLint("InflateParams")
  protected EditTextDialog(Context context, String title, Consumer<String> stringConsumer) {
    super(context, resolveDialogTheme(context, 0));
    setView(getLayoutInflater().inflate(R.layout.common_dialog_edittext, null, false));
    setCancelable(false);
    setTitle(title);
    setButton(
        BUTTON_POSITIVE,
        context.getString(android.R.string.ok),
        (dialog, which) -> {
          String content = editText.getEditableText().toString();
          stringConsumer.accept(content);
        });
    setButton(BUTTON_NEGATIVE, context.getString(android.R.string.cancel), (dialog, which) -> {});
  }

  @Override
  protected void onStart() {
    super.onStart();
    editText = findViewById(R.id.editor);
  }
}
