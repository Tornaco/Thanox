package com.nononsenseapps.filepicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

public class FilePickerActivityUtils {

    public static void pickSingleDir(Activity activity, int code) {
        Intent intent = pickSingleDirIntent(activity);
        activity.startActivityForResult(intent, code);
    }

    public static Intent pickSingleDirIntent(Context context) {
        Intent intent = new Intent(context, FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
        intent.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, true);
        intent.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR);
        intent.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
        return intent;
    }

    public static void pickSingleFile(Activity activity, int code) {
        Intent i = new Intent(activity, FilePickerActivity.class);
        // Set these depending on your use case. These are the defaults.
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, true);
        i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
        i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
        activity.startActivityForResult(i, code);
    }
}
