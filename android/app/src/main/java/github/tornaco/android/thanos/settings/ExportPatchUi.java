package github.tornaco.android.thanos.settings;

import android.app.Activity;

import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.widget.ModernAlertDialog;

public class ExportPatchUi {

    static void show(Activity context, Runnable exportMagisk) {
        ModernAlertDialog dialog = new ModernAlertDialog(context);
        dialog.setDialogTitle(context.getString(R.string.export_patch_title));
        dialog.setDialogMessage(context.getString(R.string.export_patch_message));
        dialog.setCancelable(true);
        dialog.setPositive(context.getString(R.string.export_patch_export_magisk));
        dialog.setNegative(context.getString(android.R.string.cancel));
        dialog.setOnPositive(exportMagisk);
        dialog.show();
    }
}
