package github.tornaco.android.thanos.main;

import android.content.Context;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.util.BrowserUtils;

public class DialogUtils {
    private DialogUtils() {
    }

    public static void showNotActivated(Context context) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(R.string.status_not_active)
                .setMessage(R.string.message_active_needed)
                .setCancelable(true)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(R.string.common_menu_title_wiki,
                        (dialog, which) -> {
                            BrowserUtils.launch(context, BuildProp.THANOX_URL_DOCS_HOME);
                        })
                .show();
    }
}
