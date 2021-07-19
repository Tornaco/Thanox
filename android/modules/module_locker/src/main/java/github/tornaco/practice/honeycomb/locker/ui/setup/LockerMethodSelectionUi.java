package github.tornaco.practice.honeycomb.locker.ui.setup;

import android.app.Activity;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.practice.honeycomb.locker.R;
import util.Consumer;

public class LockerMethodSelectionUi {

    public static void showLockerMethodSelections(Activity activity, Consumer<Integer> selection) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setTitle(R.string.module_locker_title_verify_lock_method)
                .setSingleChoiceItems(R.array.module_locker_locker_methods,
                        ThanosManager.from(activity).getActivityStackSupervisor().getLockerMethod(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                int methodSelected = which;

                                new AlertDialog.Builder(activity)
                                        .setTitle(R.string.module_locker_pwd_unresetable_title)
                                        .setTitle(R.string.module_locker_pwd_unresetable_message)
                                        .setCancelable(false)
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                selection.accept(methodSelected);
                                            }
                                        })
                                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Noop.
                                            }
                                        })
                                        .show();
                            }
                        })
                .create();
        alertDialog.show();
    }
}
