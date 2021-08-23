package github.tornaco.android.thanos.qs;

import android.os.IBinder;
import android.service.quicksettings.TileService;

import com.elvishew.xlog.XLog;

import util.XposedHelpers;

class QsHelper {

    static void collp(TileService service) {
        try {
            IBinder token = (IBinder) XposedHelpers.getObjectField(service, "mTileToken");
            Object tileService = XposedHelpers.getObjectField(service, "mService");
            // mService.onStartActivity(mTileToken);
            XposedHelpers.callMethod(tileService, "onStartActivity", token);
        } catch (Throwable e) {
            XLog.e(e);
        }
    }
}
