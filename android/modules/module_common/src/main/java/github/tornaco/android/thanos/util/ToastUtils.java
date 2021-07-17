package github.tornaco.android.thanos.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

    private ToastUtils() {
    }

    public static void ok(Context context) {
        Toast.makeText(context, "\uD83D\uDC4C", Toast.LENGTH_LONG).show();
    }

    public static void nook(Context context) {
        Toast.makeText(context, "\uD83D\uDC4E", Toast.LENGTH_LONG).show();
    }
}
