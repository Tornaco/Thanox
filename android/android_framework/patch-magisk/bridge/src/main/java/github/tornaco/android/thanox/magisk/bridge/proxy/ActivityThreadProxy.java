package github.tornaco.android.thanox.magisk.bridge.proxy;

import android.app.ActivityThread;
import android.os.Handler;

import com.elvishew.xlog.XLog;

import util.XposedHelpers;

public class ActivityThreadProxy {

    public static void install() {
        try {
            // final ActivityThread.H mH = new ActivityThread.H();
            ActivityThread ar = ActivityThread.currentActivityThread();
            if (ar == null) {
                XLog.e("ActivityThreadProxy#install currentActivityThread is null");
                return;
            }

            Handler oh = (Handler) XposedHelpers.getObjectField(ar, "mH");
            Handler.Callback cb = msg -> {
                // XLog.w("ActivityThreadProxy handleMessage@ " + msg);
                oh.handleMessage(msg);
                return true;
            };
            XposedHelpers.setObjectField(oh, "mCallback", cb);
        } catch (Throwable e) {
            XLog.e("ActivityThreadProxy install error", e);
        }
    }
}
