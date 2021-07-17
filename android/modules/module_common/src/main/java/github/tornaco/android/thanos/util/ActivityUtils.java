package github.tornaco.android.thanos.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ActivityUtils {

    public static void startActivity(Activity activity, Class<? extends Activity> clazz) {
        activity.startActivity(new Intent(activity, clazz));
    }

    public static void startActivity(Context context, Class<? extends Activity> clazz) {
        Intent intent = new Intent(context, clazz);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class<? extends Activity> clazz, Bundle data) {
        Intent intent = new Intent(context, clazz);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtras(data);
        context.startActivity(intent);
    }

    public static void startActivityForResult(Activity context, Class<? extends Activity> clazz, int requestCode) {
        startActivityForResult(context, clazz, requestCode, null);
    }

    public static void startActivityForResult(Activity context, Class<? extends Activity> clazz, int requestCode, Bundle data) {
        Intent intent = new Intent(context, clazz);
        if (data != null) {
            intent.putExtras(data);
        }
        context.startActivityForResult(intent, requestCode);
    }
}
