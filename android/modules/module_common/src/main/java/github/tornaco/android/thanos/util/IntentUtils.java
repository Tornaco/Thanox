package github.tornaco.android.thanos.util;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.fragment.app.Fragment;

import com.elvishew.xlog.XLog;

public class IntentUtils {
    @MainThread
    public static void startFilePickerActivityForRes(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        try {
            activity.startActivityForResult(intent, requestCode);
        } catch (Throwable e) {
            XLog.e(Log.getStackTraceString(e));

            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            try {
                activity.startActivityForResult(intent, requestCode);
            } catch (Throwable e2) {
                Toast.makeText(activity, Log.getStackTraceString(e2), Toast.LENGTH_LONG).show();
            }
        }
    }


    @MainThread
    public static void startFilePickerActivityForRes(Fragment fragment, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        try {
            fragment.startActivityForResult(intent, requestCode);
        } catch (Throwable e) {
            XLog.e(Log.getStackTraceString(e));

            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            try {
                fragment.startActivityForResult(intent, requestCode);
            } catch (Throwable e2) {
                Toast.makeText(fragment.getContext(), Log.getStackTraceString(e2), Toast.LENGTH_LONG).show();
            }
        }
    }
}
