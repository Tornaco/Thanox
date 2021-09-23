package github.tornaco.thanox.android.server.patch.framework.hooks.am;

import android.os.Handler;
import android.os.Message;

import com.android.server.am.ActivityManagerService;
import com.android.server.am.AppExitInfoTracker;
import com.android.server.am.ProcessList;
import com.elvishew.xlog.XLog;

import github.tornaco.android.thanos.core.util.AbstractSafeR;
import util.XposedHelpers;

class AppExitInfoTrackerHooks {

    static void installAppExitInfoTracker(ActivityManagerService ams) {
        new AbstractSafeR() {
            @Override
            public void runSafety() {
                installAppExitInfoTracker0(ams);
            }
        }.setName("AppExitInfoTrackerHooks installAppExitInfoTracker").run();
    }

    private static void installAppExitInfoTracker0(ActivityManagerService ams) {
        XLog.i("AppExitInfoTrackerHooks installAppExitInfoTracker");
        // final ProcessList mProcessList
        ProcessList processList = (ProcessList) XposedHelpers.getObjectField(ams, "mProcessList");
        XLog.i("AppExitInfoTrackerHooks installAppExitInfoTracker, processList: %s", processList);
        if (processList == null) {
            return;
        }
        // final AppExitInfoTracker mAppExitInfoTracker
        AppExitInfoTracker appExitInfoTracker = (AppExitInfoTracker) XposedHelpers.getObjectField(processList, "mAppExitInfoTracker");
        XLog.i("AppExitInfoTrackerHooks installAppExitInfoTracker, appExitInfoTracker: %s", appExitInfoTracker);
        if (appExitInfoTracker == null) {
            return;
        }

        // private KillHandler mKillHandler;
        Handler killHandler = (Handler) XposedHelpers.getObjectField(appExitInfoTracker, "mKillHandler");
        XLog.i("AppExitInfoTrackerHooks installAppExitInfoTracker, killHandler: %s", killHandler);
        if (killHandler == null) {
            return;
        }

        Handler.Callback cb = new XKillHandler(killHandler);
        XposedHelpers.setObjectField(killHandler, "mCallback", cb);
    }

    static final class XKillHandler implements Handler.Callback {
        private final Handler handler;

        public XKillHandler(Handler handler) {
            this.handler = handler;
        }

        @Override
        public boolean handleMessage(Message msg) {
            handler.handleMessage(msg);
            onKillMessage(msg);
            return true;
        }

        private void onKillMessage(Message msg) {
            XLog.i("AppExitInfoTrackerHooks onKillMessage: %s", msg);
        }
    }
}
