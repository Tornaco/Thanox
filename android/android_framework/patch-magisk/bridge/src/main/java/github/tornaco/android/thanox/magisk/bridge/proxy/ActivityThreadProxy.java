package github.tornaco.android.thanox.magisk.bridge.proxy;

import static github.tornaco.android.thanos.core.util.AbstractSafeR.runNamed;

import android.app.ActivityThread;
import android.app.servertransaction.ActivityLifecycleItem;
import android.app.servertransaction.ClientTransaction;
import android.app.servertransaction.PauseActivityItem;
import android.app.servertransaction.ResumeActivityItem;
import android.app.servertransaction.StopActivityItem;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

import com.elvishew.xlog.XLog;

import github.tornaco.android.thanos.core.app.ThanosManagerNative;
import github.tornaco.android.thanos.core.util.AbstractSafeR;
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
            Handler.Callback cb = new XHandler(oh);
            XposedHelpers.setObjectField(oh, "mCallback", cb);
            XLog.e("ActivityThreadProxy handler installed.");
        } catch (Throwable e) {
            XLog.e("ActivityThreadProxy install error", e);
        }
    }
}

final class XHandler implements Handler.Callback {
    private final Handler original;

    public XHandler(Handler original) {
        this.original = original;
    }

    @Override
    public boolean handleMessage(Message msg) {
        original.handleMessage(msg);
        handleMessageX(msg);
        return true;
    }

    private void handleMessageX(Message msg) {
        runNamed(new AbstractSafeR() {
            @Override
            public void runSafety() throws RemoteException {
                XLog.v("XHandler handleMessageX: %s %s", msg, HandlerCodes.codeToString(msg.what));
                if (msg.what == HandlerCodes.EXECUTE_TRANSACTION) {
                    handleClientTransactionMsg(msg);
                }
            }
        }, "handleMessageX");
    }

    private void handleClientTransactionMsg(Message msg) throws RemoteException {
        if (msg.obj instanceof ClientTransaction) {
            ClientTransaction transaction = (ClientTransaction) msg.obj;
            ActivityLifecycleItem lifecycleItem = transaction.getLifecycleStateRequest();
            XLog.d("XHandler ClientTransaction lifecycleItem: %s", lifecycleItem);
            if (lifecycleItem instanceof ResumeActivityItem) {
                handleResumeActivity(transaction.getActivityToken());
            } else if (lifecycleItem instanceof PauseActivityItem) {
                handlePauseActivity(transaction.getActivityToken());
            } else if (lifecycleItem instanceof StopActivityItem) {
                handleStopActivity(transaction.getActivityToken());
            }

        } else {
            XLog.e("XHandler handleClientTransactionMsg, msg obj is not instance of ClientTransaction...");
        }
    }

    private void handleResumeActivity(IBinder activityToken) throws RemoteException {
        XLog.d("XHandler handleResumeActivity: %s", activityToken);
        if (activityToken != null) {
            ThanosManagerNative.getDefault()
                    .getActivityStackSupervisor()
                    .reportOnActivityResumed(activityToken);
        }
    }

    private void handlePauseActivity(IBinder activityToken) {
        XLog.d("XHandler handlePauseActivity: %s", activityToken);
    }

    private void handleStopActivity(IBinder activityToken) throws RemoteException {
        XLog.d("XHandler handleStopActivity: %s", activityToken);
        if (activityToken != null) {
            ThanosManagerNative.getDefault()
                    .getActivityStackSupervisor()
                    .reportOnActivityStopped(activityToken);
        }
    }
}

class HandlerCodes {
    public static final int BIND_APPLICATION = 110;

    public static final int EXIT_APPLICATION = 111;

    public static final int RECEIVER = 113;

    public static final int CREATE_SERVICE = 114;

    public static final int SERVICE_ARGS = 115;

    public static final int STOP_SERVICE = 116;
    public static final int CONFIGURATION_CHANGED = 118;
    public static final int CLEAN_UP_CONTEXT = 119;

    public static final int GC_WHEN_IDLE = 120;

    public static final int BIND_SERVICE = 121;

    public static final int UNBIND_SERVICE = 122;
    public static final int DUMP_SERVICE = 123;
    public static final int LOW_MEMORY = 124;
    public static final int PROFILER_CONTROL = 127;
    public static final int CREATE_BACKUP_AGENT = 128;
    public static final int DESTROY_BACKUP_AGENT = 129;
    public static final int SUICIDE = 130;

    public static final int REMOVE_PROVIDER = 131;
    public static final int DISPATCH_PACKAGE_BROADCAST = 133;

    public static final int SCHEDULE_CRASH = 134;
    public static final int DUMP_HEAP = 135;
    public static final int DUMP_ACTIVITY = 136;
    public static final int SLEEPING = 137;
    public static final int SET_CORE_SETTINGS = 138;
    public static final int UPDATE_PACKAGE_COMPATIBILITY_INFO = 139;

    public static final int DUMP_PROVIDER = 141;
    public static final int UNSTABLE_PROVIDER_DIED = 142;
    public static final int REQUEST_ASSIST_CONTEXT_EXTRAS = 143;
    public static final int TRANSLUCENT_CONVERSION_COMPLETE = 144;

    public static final int INSTALL_PROVIDER = 145;
    public static final int ON_NEW_ACTIVITY_OPTIONS = 146;

    public static final int ENTER_ANIMATION_COMPLETE = 149;
    public static final int START_BINDER_TRACKING = 150;
    public static final int STOP_BINDER_TRACKING_AND_DUMP = 151;
    public static final int LOCAL_VOICE_INTERACTION_STARTED = 154;
    public static final int ATTACH_AGENT = 155;
    public static final int APPLICATION_INFO_CHANGED = 156;
    public static final int RUN_ISOLATED_ENTRY_POINT = 158;
    public static final int EXECUTE_TRANSACTION = 159;
    public static final int RELAUNCH_ACTIVITY = 160;
    public static final int PURGE_RESOURCES = 161;
    public static final int ATTACH_STARTUP_AGENTS = 162;

    static String codeToString(int code) {
        switch (code) {
            case BIND_APPLICATION:
                return "BIND_APPLICATION";
            case EXIT_APPLICATION:
                return "EXIT_APPLICATION";
            case RECEIVER:
                return "RECEIVER";
            case CREATE_SERVICE:
                return "CREATE_SERVICE";
            case SERVICE_ARGS:
                return "SERVICE_ARGS";
            case STOP_SERVICE:
                return "STOP_SERVICE";
            case CONFIGURATION_CHANGED:
                return "CONFIGURATION_CHANGED";
            case CLEAN_UP_CONTEXT:
                return "CLEAN_UP_CONTEXT";
            case GC_WHEN_IDLE:
                return "GC_WHEN_IDLE";
            case BIND_SERVICE:
                return "BIND_SERVICE";
            case UNBIND_SERVICE:
                return "UNBIND_SERVICE";
            case DUMP_SERVICE:
                return "DUMP_SERVICE";
            case LOW_MEMORY:
                return "LOW_MEMORY";
            case PROFILER_CONTROL:
                return "PROFILER_CONTROL";
            case CREATE_BACKUP_AGENT:
                return "CREATE_BACKUP_AGENT";
            case DESTROY_BACKUP_AGENT:
                return "DESTROY_BACKUP_AGENT";
            case SUICIDE:
                return "SUICIDE";
            case REMOVE_PROVIDER:
                return "REMOVE_PROVIDER";
            case DISPATCH_PACKAGE_BROADCAST:
                return "DISPATCH_PACKAGE_BROADCAST";
            case SCHEDULE_CRASH:
                return "SCHEDULE_CRASH";
            case DUMP_HEAP:
                return "DUMP_HEAP";
            case DUMP_ACTIVITY:
                return "DUMP_ACTIVITY";
            case SET_CORE_SETTINGS:
                return "SET_CORE_SETTINGS";
            case UPDATE_PACKAGE_COMPATIBILITY_INFO:
                return "UPDATE_PACKAGE_COMPATIBILITY_INFO";
            case DUMP_PROVIDER:
                return "DUMP_PROVIDER";
            case UNSTABLE_PROVIDER_DIED:
                return "UNSTABLE_PROVIDER_DIED";
            case REQUEST_ASSIST_CONTEXT_EXTRAS:
                return "REQUEST_ASSIST_CONTEXT_EXTRAS";
            case TRANSLUCENT_CONVERSION_COMPLETE:
                return "TRANSLUCENT_CONVERSION_COMPLETE";
            case INSTALL_PROVIDER:
                return "INSTALL_PROVIDER";
            case ON_NEW_ACTIVITY_OPTIONS:
                return "ON_NEW_ACTIVITY_OPTIONS";
            case ENTER_ANIMATION_COMPLETE:
                return "ENTER_ANIMATION_COMPLETE";
            case LOCAL_VOICE_INTERACTION_STARTED:
                return "LOCAL_VOICE_INTERACTION_STARTED";
            case ATTACH_AGENT:
                return "ATTACH_AGENT";
            case APPLICATION_INFO_CHANGED:
                return "APPLICATION_INFO_CHANGED";
            case RUN_ISOLATED_ENTRY_POINT:
                return "RUN_ISOLATED_ENTRY_POINT";
            case EXECUTE_TRANSACTION:
                return "EXECUTE_TRANSACTION";
            case RELAUNCH_ACTIVITY:
                return "RELAUNCH_ACTIVITY";
            case PURGE_RESOURCES:
                return "PURGE_RESOURCES";
            case ATTACH_STARTUP_AGENTS:
                return "ATTACH_STARTUP_AGENTS";
        }
        return "UNKNOWN_CODE: " + code;
    }
}

