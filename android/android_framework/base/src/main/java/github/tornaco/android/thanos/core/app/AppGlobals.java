package github.tornaco.android.thanos.core.app;

import android.annotation.SuppressLint;
import android.content.Context;

import com.elvishew.xlog.XLog;

public final class AppGlobals {

    @SuppressLint("StaticFieldLeak")
    public static Context context;

    private AppGlobals() {
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        XLog.w("AppGlobals.setContext: %s", context);
        AppGlobals.context = context;
    }
}
